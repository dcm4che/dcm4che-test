package org.dcm4che.test.samples.dcmeereq44;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.commons.cli.MissingArgumentException;
import org.dcm4che.test.annotations.StoreParameters;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.common.TestToolFactory.TestToolType;
import org.dcm4che.test.utils.TestUtils;
import org.dcm4che3.conf.api.ConfigurationException;
import org.dcm4che3.conf.api.DicomConfiguration;
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
    
    @Test
    public void testStoreSR_DefaultParams_Resource() throws MissingArgumentException {
        StoreTool storeTool = (StoreTool) TestToolFactory
                .createToolForTest(TestToolType.StoreTool, this);
        //file to send is in the resources of the test, using sendResource
        StoreResult result = (StoreResult) storeResource
                ("Test Store SR - REQUIREMENT[DCMEEREQ-44] - "
                        + "IHE - TRANSACTION[RAD-43]", "req44_sr.dcm");
    }

    //included for demonstrating purposes
    //@Test
    @StoreParameters(aeTitle="DCM4CHEE", baseDirectory="./",connection="dicom"
    ,sourceAETitle="STORESCU",sourceDevice="storescu")
    public void testStoreSR_CustomParams() throws MissingArgumentException, IOException, InterruptedException, IncompatibleConnectionException, GeneralSecurityException, ConfigurationException {
        DicomConfiguration remoteConfig = getRemoteConfig();
        TestUtils.addTagToAttributesFilter(Entity.Instance, remoteConfig, Tag.CodeMeaning, VR.LO);
        StoreTool storeTool = (StoreTool) TestToolFactory
                .createToolForTest(TestToolType.StoreTool, this);
        storeTool.store
                ("Test Store SR - REQUIREMENT[DCMEEREQ-44] - "
                        + "IHE - TRANSACTION[RAD-43]", "./target/test-classes/req44_sr.dcm");
        StoreResult result = (StoreResult) storeTool.getResult();
    }

}
