package org.dcm4che.test.samples.dcmeereq44;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.commons.cli.MissingArgumentException;
import org.dcm4che.test.annotations.StoreParameters;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.common.TestToolFactory.TestToolType;
import org.dcm4che.test.utils.AssertionUtils;
import org.dcm4che.test.utils.TestUtils;
import org.dcm4che3.conf.api.ConfigurationException;
import org.dcm4che3.conf.api.DicomConfiguration;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.net.IncompatibleConnectionException;
import org.dcm4che3.tool.storescu.test.StoreResult;
import org.dcm4che3.tool.storescu.test.StoreTool;
import org.dcm4chee.archive.conf.Entity;
import org.junit.Test;
/**
 * @author Hesham Elbadawi <bsdreko@gmail.com>
 *
 * Tests storing a structured report to the image manager 
 */
public class TestRAD43 extends BasicTest{
    
    //@Test
    public void testStoreSR_DefaultParams_Resource() throws MissingArgumentException {
        StoreTool storeTool = (StoreTool) TestToolFactory
                .createToolForTest(TestToolType.StoreTool, this);
        //file to send is in the resources of the test, using sendResource
        StoreResult result = (StoreResult) storeResource
                ("Test Store SR - REQUIREMENT[DCMEEREQ-44] - "
                        + "IHE - TRANSACTION[RAD-43]", "req44_sr.dcm");
    }

    //included for demonstrating purposes
    @Test
    @StoreParameters(aeTitle="DCM4CHEE", baseDirectory="./",connection="dicom"
    ,sourceAETitle="STORESCU",sourceDevice="storescu")
    public void testStoreSR_CustomParams() throws MissingArgumentException, IOException, InterruptedException, IncompatibleConnectionException, GeneralSecurityException, ConfigurationException, ClassNotFoundException {
        //get archive configuration
        DicomConfiguration remoteConfig = getRemoteConfig();
        //add a new attribute to be used for querying
        //backup remote config
        TestUtils.backUpRemoteConfig(remoteConfig);
        TestUtils.addDBCustomAttribute("dcm4chee-arc", Entity.Instance, remoteConfig, Tag.CodeMeaning, VR.LO, Tag.ConceptNameCodeSequence);
        //reload the archive configuration for changes to take effect
        System.out.println("Reloaded remote Server - Response = "
                //here in the empty url a base url and webcontext should be passed
                //i.e. TestUtils.reloadServerConfig(null,"http://localhost:8080/dcm4chee-arc"));
                //otherwise use default config as follows
        +TestUtils.reloadServerConfig(this));
        //create a store tool
        StoreTool storeTool = (StoreTool) TestToolFactory
                .createToolForTest(TestToolType.StoreTool, this);
        //do C-Store to the archive ( the archive will also create the new query attribute in the database)
        storeTool.store
                ("Test Store SR - REQUIREMENT[DCMEEREQ-44] - "
                        + "IHE - TRANSACTION[RAD-43]", "./target/test-classes/req44_sr.dcm");
        //get the store result
        StoreResult result = (StoreResult) storeTool.getResult();
        //check the status returned was 0
        Attributes status = new Attributes();
        status.setString(Tag.Status, VR.US, "0");
        AssertionUtils.assertContainsAttrs(result.getcStoreRSPAttributes(), status);
        TestUtils.rollBackRemoteConfig(remoteConfig);
        System.out.println("Reloaded remote Server - Response = "
                //here in the empty url a base url and webcontext should be passed
                //i.e. TestUtils.reloadServerConfig(null,"http://localhost:8080/dcm4chee-arc"));
                //otherwise use default config as follows
        +TestUtils.reloadServerConfig(this));
    }

}
