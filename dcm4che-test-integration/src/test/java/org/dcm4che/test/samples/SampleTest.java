package org.dcm4che.test.samples;


import java.io.IOException;
import java.security.GeneralSecurityException;

import org.dcm4che.test.annotations.QueryParameters;
import org.dcm4che.test.annotations.RemoteConnectionParameters;
import org.dcm4che.test.annotations.StoreParameters;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.net.IncompatibleConnectionException;
import org.dcm4che3.tool.findscu.test.QueryResult;
import org.dcm4che3.tool.storescu.test.StoreResult;
import org.junit.Assert;
import org.junit.Test;

public class SampleTest extends BasicTest {

    @Test
    @StoreParameters(
            aeTitle="DCM4CHEE",
            baseDirectory= "/",
            sourceAETitle="STORESCU",
            sourceDevice="storescu"
    )
    public void testSendDicom() throws IOException, InterruptedException, IncompatibleConnectionException, GeneralSecurityException {
        StoreResult result =  (StoreResult) store("test description", "test.dcm");
        Assert.assertEquals(result.getFailures(), 0);
    }
    @Test
    @StoreParameters(
            aeTitle="DCM4CHEE",
            baseDirectory= "/",
            sourceAETitle="STORESCU",
            sourceDevice="storescu"
    )
    public void testSendDicomDefaultRemoteConfig() throws IOException, InterruptedException, IncompatibleConnectionException, GeneralSecurityException {
        StoreResult result =  (StoreResult) store("test description", "test.dcm");
        Assert.assertEquals(result.getFailures(), 0);
    }
    
    @Test
    @QueryParameters(sourceAETitle="FINDSCU",sourceDevice="findscu",aeTitle="DCM4CHEE")
    public void testQueryNoFuzzyDicomDefaultRemoteConfig() throws Exception {
        Attributes queryKeys = new Attributes();

        queryKeys.setString(Tag.PatientID, VR.LO, "0002");
        queryKeys.setString(Tag.IssuerOfPatientID, VR.LO ,"anonymous");
        queryKeys.setNull(Tag.OtherPatientIDsSequence, VR.SQ);
        QueryResult result = (QueryResult) query("Query test Patient ID with Issuer", queryKeys, false);
        Assert.assertEquals(1, result.getNumMatches());
        System.out.println(result.getQueryResponse().toString());
    }
}
