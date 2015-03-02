package org.dcm4che.test.samples;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.dcm4che.test.annotations.DcmGenParameters;
import org.dcm4che.test.annotations.GetParameters;
import org.dcm4che.test.annotations.MoveParameters;
import org.dcm4che.test.annotations.MppsParameters;
import org.dcm4che.test.annotations.QueryParameters;
import org.dcm4che.test.annotations.RemoteConnectionParameters;
import org.dcm4che.test.annotations.StgCmtParameters;
import org.dcm4che.test.annotations.StoreParameters;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.common.TestToolFactory.TestToolType;
import org.dcm4che.test.integration.retrieve.RetrieveTestSuite;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.net.IncompatibleConnectionException;
import org.dcm4che3.tool.dcmgen.test.DcmGenTool;
import org.dcm4che3.tool.findscu.test.QueryResult;
import org.dcm4che3.tool.getscu.test.RetrieveResult;
import org.dcm4che3.tool.getscu.test.RetrieveTool;
import org.dcm4che3.tool.movescu.test.MoveResult;
import org.dcm4che3.tool.movescu.test.MoveTool;
import org.dcm4che3.tool.mppsscu.test.MppsResult;
import org.dcm4che3.tool.stgcmtscu.test.StgCmtTool;
import org.dcm4che3.tool.storescu.test.StoreResult;
import org.junit.Assert;
import org.junit.Test;

public class SampleTest extends BasicTest {

    @Test
    @RemoteConnectionParameters(hostName="localhost",port=2762)
    @StoreParameters(aeTitle="DCM4CHEE", baseDirectory="/pacsworld_Share_SlaveVM/Dicom-IHE-Server-Platform/Mesa_12_5_0/", connection="dicom-tls", sourceAETitle="STORESCU", sourceDevice="storescu")
    public void testaSendDicom() throws IOException, InterruptedException, IncompatibleConnectionException, GeneralSecurityException {
        StoreResult result =  (StoreResult) storeResource("test description", "test.dcm");
        Assert.assertEquals(result.getFailures(), 0);
    }
    @Test
    public void testSendDicomDefaultRemoteConfig() throws IOException, InterruptedException, IncompatibleConnectionException, GeneralSecurityException {
        StoreResult result =  (StoreResult) storeResource("test description", "test.dcm");
        Assert.assertEquals(result.getFailures(), 0);
    }

    @Test
    @RemoteConnectionParameters(hostName="localhost",port=2762)
    @QueryParameters(aeTitle="DCM4CHEE", connection="dicom-tls", sourceAETitle="FINDSCU", sourceDevice="findscu")
    public void testbQueryNoFuzzyDicomDefaultRemoteConfig() throws Exception {
        Attributes queryKeys = new Attributes();

        queryKeys.setString(Tag.PatientID, VR.LO, "0002");
        queryKeys.setString(Tag.IssuerOfPatientID, VR.LO ,"anonymous");
        queryKeys.setNull(Tag.OtherPatientIDsSequence, VR.SQ);
        QueryResult result = (QueryResult) query("Query test Patient ID with Issuer", queryKeys, false);
        Assert.assertEquals(1, result.getNumMatches());
        System.out.println(result.getQueryResponse().toString());
    }
    @Test
    @RemoteConnectionParameters(hostName="localhost", port=2762)
    @MppsParameters(aeTitle="DCM4CHEE", connection="dicom-tls", sourceAETitle="MPPSSCU", sourceDevice="mppsscu", baseDirectory="/pacsworld_Share_SlaveVM/Dicom-IHE-Server-Platform/Mesa_12_5_0/")
    public void testMPPS(){
        MppsResult mppsResult = (MppsResult) mpps("Send MPPS for Study",
                "modality/MR/MR4/MR4S1");
    }
    @Test
//    @RemoteConnectionParameters(hostName="localhost", port=2762)
//    @StgCmtParameters(aeTitle="DCM4CHEE", connection="dicom-tls", sourceAETitle="STGCMTSCU", sourceDevice="stgcmtscu", baseDirectory="/pacsworld_Share_SlaveVM/Dicom-IHE-Server-Platform/Mesa_12_5_0/",storageDirectory="/tmp")
    //connection matching problem, to make this secure test work remove the configuration for the dicom connection from the archive and keep dicom-tls connection for STGCMTSCU
    public void testStgCmt() throws InterruptedException, IOException, GeneralSecurityException, IncompatibleConnectionException{
        StgCmtTool stgCmtTool = (StgCmtTool) TestToolFactory.createToolForTest(TestToolFactory.TestToolType.StorageCommitmentTool, this);
        stgCmtTool.stgcmt("Storage Commitment MESA_MR_MR1_MRS1","modality/MR/MR1/MR1S1/MR1S1IM1.dcm");
    }
    @DcmGenParameters(instanceCount=1,outputDir="/testnew",seedFile="/test.dcm",seriesCount=1)
    @Test
    public void testDcmGenerateNoOverride() {
        DcmGenTool tool = (DcmGenTool) TestToolFactory.createToolForTest(TestToolType.DcmGenTool, this);
        tool.generateFiles("test", null);
    }
    @Test
    @RemoteConnectionParameters(hostName="localhost", port=2762)
    @GetParameters(aeTitle="DCM4CHEE",connection="dicom-tls",retrieveDir="/tmp",retrieveLevel="IMAGE")
    public void testRetrieve() throws Exception {
        
        RetrieveTool retrieveTool = (RetrieveTool) TestToolFactory.createToolForTest(TestToolType.GetTool, this);
        retrieveTool.addTag(Tag.SOPInstanceUID, "1.2.392.200036.9125.0.19950720093509");
        retrieveTool.setExpectedMatches(1);
        retrieveTool.retrieve("MESA_12_5,MR1,S1");
        RetrieveResult result = (RetrieveResult) retrieveTool.getResult();
        RetrieveTestSuite.printResults(result);
    }

    @Test
    public void testMoveTool() throws Exception {
        MoveTool moveTool = (MoveTool) TestToolFactory.createToolForTest(TestToolType.MoveTool, this);
        moveTool.addTag(Tag.StudyInstanceUID, "1.2.392.200036.9125.0.198811291108.7");
        moveTool.setExpectedMatches(1);
        moveTool.move("MESA_12_5,MR1,S1");
        MoveResult result = (MoveResult) moveTool.getResult();
        
    }
}
