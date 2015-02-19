package org.dcm4che.test.samples;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.dcm4che.test.annotations.DcmGenParameters;
import org.dcm4che.test.annotations.StoreParameters;
import org.dcm4che.test.annotations.TestConfig;
import org.dcm4che.test.annotations.TestParamDefaults;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.common.TestToolFactory.TestToolType;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.net.IncompatibleConnectionException;
import org.dcm4che3.tool.dcmgen.test.DcmGenTool;
import org.dcm4che3.tool.findscu.test.QueryResult;
import org.dcm4che3.tool.storescu.test.StoreResult;
import org.junit.Assert;
import org.junit.Test;

@TestConfig(configFile="/config-sample.json")
public class SampleTest extends BasicTest {

//    @Test
//    public void testSendDicom() throws IOException, InterruptedException, IncompatibleConnectionException, GeneralSecurityException {
//        StoreResult result =  (StoreResult) storeResource("test description", "test.dcm");
//        Assert.assertEquals(result.getFailures(), 0);
//    }
//    @Test
//    public void testSendDicomDefaultRemoteConfig() throws IOException, InterruptedException, IncompatibleConnectionException, GeneralSecurityException {
//        StoreResult result =  (StoreResult) storeResource("test description", "test.dcm");
//        Assert.assertEquals(result.getFailures(), 0);
//    }
//
//    @Test
//    public void testQueryNoFuzzyDicomDefaultRemoteConfig() throws Exception {
//        Attributes queryKeys = new Attributes();
//
//        queryKeys.setString(Tag.PatientID, VR.LO, "0002");
//        queryKeys.setString(Tag.IssuerOfPatientID, VR.LO ,"anonymous");
//        queryKeys.setNull(Tag.OtherPatientIDsSequence, VR.SQ);
//        QueryResult result = (QueryResult) query("Query test Patient ID with Issuer", queryKeys, false);
//        Assert.assertEquals(1, result.getNumMatches());
//        System.out.println(result.getQueryResponse().toString());
//    }
    @DcmGenParameters(instanceCount=1,outputDir="/testnew",seedFile="/test.dcm",seriesCount=1)
    @Test
    public void testDcmGenerateNoOverride() {
        DcmGenTool tool = (DcmGenTool) TestToolFactory.createToolForTest(TestToolType.DcmGenTool, this);
        tool.generateFiles("test", null);
    }
}
