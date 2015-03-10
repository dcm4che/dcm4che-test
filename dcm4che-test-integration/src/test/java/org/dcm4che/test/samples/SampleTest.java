package org.dcm4che.test.samples;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.commons.cli.MissingArgumentException;
import org.dcm4che.test.annotations.DcmGenParameters;
import org.dcm4che.test.annotations.GetParameters;
import org.dcm4che.test.annotations.MoveParameters;
import org.dcm4che.test.annotations.MppsParameters;
import org.dcm4che.test.annotations.QidoRSParameters;
import org.dcm4che.test.annotations.QueryParameters;
import org.dcm4che.test.annotations.RemoteConnectionParameters;
import org.dcm4che.test.annotations.StgCmtParameters;
import org.dcm4che.test.annotations.StoreParameters;
import org.dcm4che.test.annotations.StowRSParameters;
import org.dcm4che.test.annotations.WadoURIParameters;
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
import org.dcm4che3.tool.qidors.test.QidoRSResult;
import org.dcm4che3.tool.qidors.test.QidoRSTool;
import org.dcm4che3.tool.stgcmtscu.test.StgCmtTool;
import org.dcm4che3.tool.storescu.test.StoreResult;
import org.dcm4che3.tool.stowrs.test.StowRSResponse;
import org.dcm4che3.tool.stowrs.test.StowRSResult;
import org.dcm4che3.tool.stowrs.test.StowRSTool;
import org.dcm4che3.tool.stowrs.test.StowRSTool.StowMetaDataType;
import org.dcm4che3.tool.wadouri.test.WadoURIResult;
import org.dcm4che3.tool.wadouri.test.WadoURITool;
import org.junit.Assert;
import org.junit.Test;

public class SampleTest extends BasicTest {
//
//    @Test
//    @RemoteConnectionParameters(hostName="localhost",port=11112,baseURL="")
//    @StoreParameters(aeTitle="DCM4CHEE", baseDirectory="/pacsworld_Share_SlaveVM/Dicom-IHE-Server-Platform/Mesa_12_5_0/", connection="dicom", sourceAETitle="STORESCU", sourceDevice="storescu")
//    public void testaSendDicom() throws IOException, InterruptedException, IncompatibleConnectionException, GeneralSecurityException, MissingArgumentException {
//        StoreResult result =  (StoreResult) storeResource("test description", "test.dcm");
//        Assert.assertEquals(result.getFailures(), 0);
//    }
//    @Test
//    public void testSendDicomDefaultRemoteConfig() throws IOException, InterruptedException, IncompatibleConnectionException, GeneralSecurityException, MissingArgumentException {
//        StoreResult result =  (StoreResult) storeResource("test description", "test.dcm");
//        Assert.assertEquals(result.getFailures(), 0);
//    }
//
//    @Test
//    @RemoteConnectionParameters(hostName="localhost",port=11112)
//    @QueryParameters(aeTitle="DCM4CHEE", connection="dicom", sourceAETitle="FINDSCU", sourceDevice="findscu")
//    public void testbQueryNoFuzzyDicomDefaultRemoteConfig() throws Exception {
//        Attributes queryKeys = new Attributes();
//
//        queryKeys.setString(Tag.PatientID, VR.LO, "0002");
//        queryKeys.setString(Tag.IssuerOfPatientID, VR.LO ,"anonymous");
//        queryKeys.setNull(Tag.OtherPatientIDsSequence, VR.SQ);
//        QueryResult result = (QueryResult) query("Query test Patient ID with Issuer", queryKeys, false);
//        Assert.assertEquals(1, result.getNumMatches());
//        System.out.println(result.getQueryResponse().toString());
//    }
//    @Test
//    @RemoteConnectionParameters(hostName="localhost", port=11112)
//    @MppsParameters(aeTitle="DCM4CHEE", connection="dicom", sourceAETitle="MPPSSCU", sourceDevice="mppsscu", baseDirectory="/pacsworld_Share_SlaveVM/Dicom-IHE-Server-Platform/Mesa_12_5_0/")
//    public void testMPPS() throws MissingArgumentException{
//        MppsResult mppsResult = (MppsResult) mpps("Send MPPS for Study",
//                "modality/MR/MR4/MR4S1");
//    }
//    @Test
////    @RemoteConnectionParameters(hostName="localhost", port=2762)
////    @StgCmtParameters(aeTitle="DCM4CHEE", connection="dicom-tls", sourceAETitle="STGCMTSCU", sourceDevice="stgcmtscu", baseDirectory="/pacsworld_Share_SlaveVM/Dicom-IHE-Server-Platform/Mesa_12_5_0/",storageDirectory="/tmp")
//    //connection matching problem, to make this secure test work remove the configuration for the dicom connection from the archive and keep dicom-tls connection for STGCMTSCU
//    public void testStgCmt() throws InterruptedException, IOException, GeneralSecurityException, IncompatibleConnectionException, MissingArgumentException{
//        StgCmtTool stgCmtTool = (StgCmtTool) TestToolFactory.createToolForTest(TestToolFactory.TestToolType.StorageCommitmentTool, this);
//        stgCmtTool.stgcmt("Storage Commitment MESA_MR_MR1_MRS1","modality/MR/MR1/MR1S1/MR1S1IM1.dcm");
//    }
//    @DcmGenParameters(instanceCount=1,outputDir="/testnew",seedFile="/test.dcm",seriesCount=1)
//    @Test
//    public void testDcmGenerateNoOverride() throws MissingArgumentException {
//        DcmGenTool tool = (DcmGenTool) TestToolFactory.createToolForTest(TestToolType.DcmGenTool, this);
//        tool.generateFiles("test", null);
//    }
//    @Test
//    @RemoteConnectionParameters(hostName="localhost", port=11112)
//    @GetParameters(aeTitle="DCM4CHEE",connection="dicom",retrieveDir="/tmp",retrieveLevel="IMAGE")
//    public void testRetrieve() throws Exception {
//        
//        RetrieveTool retrieveTool = (RetrieveTool) TestToolFactory.createToolForTest(TestToolType.GetTool, this);
//        retrieveTool.addTag(Tag.SOPInstanceUID, "1.2.392.200036.9125.0.19950720093509");
//        retrieveTool.setExpectedMatches(1);
//        retrieveTool.retrieve("MESA_12_5,MR1,S1");
//        RetrieveResult result = (RetrieveResult) retrieveTool.getResult();
//        RetrieveTestSuite.printResults(result);
//    }
//
//    @Test
//    public void testMoveTool() throws Exception {
//        MoveTool moveTool = (MoveTool) TestToolFactory.createToolForTest(TestToolType.MoveTool, this);
//        moveTool.addTag(Tag.StudyInstanceUID, "1.2.392.200036.9125.0.198811291108.7");
//        moveTool.setExpectedMatches(1);
//        moveTool.move("MESA_12_5,MR1,S1");
//        MoveResult result = (MoveResult) moveTool.getResult();
//        
//    }
//    
    @Test
    @StowRSParameters(url="stow/DCM4CHEE/studies")
    public void testStow() {
        StowRSTool stowTool = null;
        try {
            stowTool = (StowRSTool) TestToolFactory.createToolForTest(TestToolType.StowTool, this);
        } catch (MissingArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            stowTool.send("send dicom via stowrs", StowMetaDataType.NO_METADATA_DICOM, new File("/pacsworld_Share_SlaveVM/DICOM-IHE-Server-Platform/Mesa_12_5_0/modality/MR/MR1/MR1S1/MR1S1IM1.dcm"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        StowRSResult result = (StowRSResult) stowTool.getResult();
        System.out.println("Stow performed with the following responses for each file");
        for(StowRSResponse rsp : result.getResponses())
        System.out.println("Status : " + rsp.getStatus() + "\t" + rsp.getMessage() + "\t" + rsp.getResponseAttributes().toString());
    }
  @Test
  @RemoteConnectionParameters(hostName="localhost",port=11112)
  @QueryParameters(aeTitle="DCM4CHEE", queryLevel = "IMAGE", connection="dicom", sourceAETitle="FINDSCU", sourceDevice="findscu")
  public void testbQueryIMAGELevel() throws Exception {
      Attributes queryKeys = new Attributes();

      queryKeys.setString(Tag.SOPInstanceUID, VR.UI, "1.2.840.113674.950809133358028.100");
      QueryResult result = (QueryResult) query("Query sop instance uid", queryKeys, false);
      Assert.assertEquals(1, result.getNumMatches());
      System.out.println(result.getQueryResponse().toString());
  }
  @Test
  @QidoRSParameters(fuzzyMatching=true, limit="100", offset="0", returnAll=true, timezoneAdjustment=false,url="qido/DCM4CHEE/studies")
  public void testQidoSimpleQuery() throws Exception {
      QidoRSTool tool = (QidoRSTool) TestToolFactory.createToolForTest(TestToolType.QidoTool, this);
      tool.addQueryTag(Tag.StudyInstanceUID, "1.1");
      tool.addReturnTag(Tag.StudyInstanceUID);
      tool.setExpectedMatches(1);
      tool.queryJSON("Qido test json");
      QidoRSResult result = (QidoRSResult) tool.getResult();
      List<Attributes> attrs = result.getQueryResponse();
      System.out.println("The following results were obtained from the qido query:");
      System.out.println("Time taken to get first response : " + result.getTimeFirst() + "ms");
      System.out.println("Number of reponses (Matches): " + result.getNumMatches());
      System.out.println("Time taken to get all responses : " + result.getTime() + "ms");
      System.out.println("Responses : " );
      for(Attributes attr : attrs)
      System.out.println(attr.toString());
  }
  @Test
  @WadoURIParameters(studyUID="1.11.111111.105768226864276439868532330426913662349"
  ,seriesUID="1.11.111111.122914101808394730868803286082090132783"
  ,objectUID="1.2.3.4",contentType="application/dicom", url = "wado/DCM4CHEE", transferSyntax="1.2.840.10008.1.2.4.70",retrieveDir="/tmp/")
  public void testWadoJPEG () throws Exception {
      WadoURITool tool = (WadoURITool) TestToolFactory.createToolForTest(TestToolType.WadoURITool, this);
      tool.wadoURI("test wado uri with instance 1.1.1.1 as jpeg");
      WadoURIResult result = (WadoURIResult) tool.getResult();
      System.out.println("WadoURI performed, resulting in the following:");
      System.out.println("Time taken to get response:"+ result.getTime());
      System.out.println("Response status : "+result.getResponse().getStatus());
      System.out.println("Response message : "+result.getResponse().getMessage());
      System.out.println("Response retrieved instance : "+result.getResponse().getRetrievedInstance().toString());
      
  }
}
