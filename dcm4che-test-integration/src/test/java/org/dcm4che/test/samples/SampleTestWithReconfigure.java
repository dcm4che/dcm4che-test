package org.dcm4che.test.samples;

import org.junit.Assert;
import junit.framework.AssertionFailedError;
import org.dcm4che.test.annotations.QueryParameters;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolException;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.integration.query.QueryTestSuite;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.net.ApplicationEntity;
import org.dcm4che3.net.Device;
import org.dcm4che3.tool.common.test.TestResult;
import org.dcm4che3.tool.findscu.test.QueryResult;
import org.dcm4che3.tool.findscu.test.QueryTool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

/**
 * @author Roman K
 */
@RunWith(JUnit4.class)
public class SampleTestWithReconfigure extends BasicTest{


    @Test
    @QueryParameters(aeTitle = "DCM4CHEE")
    public void testAERenaming() throws Exception {

        // try to query existing AE - everything should be fine
        query("", new Attributes(), false, false, 0);

        // rename the AE on the server
        Device device = getRemoteConfig().findDevice("dcm4chee-arc");
        device.getApplicationEntity("DCM4CHEE").setAETitle("DCM4CHEE-CHANGED");
        getRemoteConfig().merge(device);

        // try to query the non-existing AE
        try {
            query("", new Attributes(), false,false,  0);
            throw new AssertionFailedError("Query should have failed because of non existing AE title");
        } catch (TestToolException e) {
            Assert.assertTrue(e.getCause() instanceof IOException);
            // it ok, noop
        }

        // rename the AE back
        device.getApplicationEntity("DCM4CHEE-CHANGED").setAETitle("DCM4CHEE");
        getRemoteConfig().merge(device);

    }
}
