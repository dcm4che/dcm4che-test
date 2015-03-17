package org.dcm4che.test.samples.dcmeereq93;

import org.dcm4che.test.annotations.WadoRSParameters;
import org.junit.Test;

public class TestARCH116 {

    @Test
    @WadoRSParameters(url="wado/DCM4CHEE/studies/testStudy", retrieveDir = "/tmp/")
    public void testWadoRSEliminateStructuredReportObjectFromResponse() {
        
    }
}
