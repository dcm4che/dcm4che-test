package org.dcm4che.test.samples;


import org.dcm4che.test.annotations.DcmGenParameters;
import org.dcm4che.test.annotations.GetParameters;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.common.TestToolFactory.TestToolType;
import org.dcm4che.test.integration.retrieve.RetrieveTestSuite;
import org.dcm4che.test.integration.store.StoreTestSuite;
import org.dcm4che.test.utils.AssertionUtils;
import org.dcm4che.test.utils.DBUtils;
import org.dcm4che.test.utils.TestUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.tool.getscu.test.RetrieveResult;
import org.dcm4che3.tool.getscu.test.RetrieveTool;
import org.dcm4che3.tool.storescu.test.StoreResult;
import org.dcm4che3.tool.storescu.test.StoreTool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public class CGET_RetrieveAtPatientLevel extends BasicTest {

    public void send_Data() throws Exception {
            // send data
            StoreTool storeTool = (StoreTool) TestToolFactory.createToolForTest(TestToolType.StoreTool, this);
            Attributes attrs = new Attributes();
            generateAndSend("PRECONDITION:SENDING_1_CR_STUDY", attrs);
    StoreResult results = (StoreResult) storeTool.getResult();
//    StoreTestSuite.logResults(results);
    }

    @Before
    public void prepareTest() throws Exception{
            TestUtils.removeArchiveSuppressionCriteria("dcm4chee-arc", "DCM4CHEE", getRemoteConfig());
    }


    @Test
    @DcmGenParameters(seedFile="/test.dcm",outputDir="/tmps/")
    @GetParameters(retrieveLevel="PATIENT", retrieveInformationModel= "PatientRoot", relational= false)
    public void get1Instance1Patient_ByPatientIDAndIssuer_PatientModel_isNotRelational() throws Exception{
            send_Data();            

            RetrieveTool retrieveTool = (RetrieveTool) TestToolFactory.createToolForTest(TestToolType.GetTool, this);
    retrieveTool.addTag(Tag.PatientID, "img3_ID");
    retrieveTool.addTag(Tag.IssuerOfPatientID, "HALLO");
    retrieveTool.setExpectedMatches(1);
    retrieveTool.retrieve("RETRIEVEBYPATIENTID_CR_PATIENTLEVEL_PATIENTMODEL_ISNOTRELATIONAL");
    RetrieveResult result = (RetrieveResult) retrieveTool.getResult();
    RetrieveTestSuite.printResults(result);  
    } 
}