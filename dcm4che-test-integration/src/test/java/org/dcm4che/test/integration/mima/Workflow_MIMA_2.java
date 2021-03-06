/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subjeCR to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is part of dcm4che, an implementation of DICOM(TM) in
 * Java(TM), hosted at https://github.com/gunterze/dcm4che.
 *
 * The Initial Developer of the Original Code is
 * Agfa Healthcare.
 * Portions created by the Initial Developer are Copyright (C) 2011
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * See @authors listed below
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

package org.dcm4che.test.integration.mima;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.dcm4che.test.annotations.RemoteConnectionParameters;
import org.dcm4che.test.annotations.StoreParameters;
import org.dcm4che.test.utils.AssertionUtils;
import org.dcm4che.test.utils.LoadProperties;
import org.dcm4che.test.utils.TestUtils;
import org.dcm4che3.conf.api.DicomConfiguration;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.net.ApplicationEntity;
import org.dcm4che3.net.Device;
import org.dcm4che3.tool.findscu.test.QueryResult;
import org.dcm4che3.tool.findscu.test.QueryTool;
import org.dcm4che3.tool.hl7rcv.test.HL7RcvTest;
import org.dcm4che3.tool.storescu.test.StoreResult;
import org.dcm4che3.tool.storescu.test.StoreTool;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.common.TestToolFactory.TestToolType;
import org.dcm4che3.util.StringUtils;
import org.dcm4chee.archive.conf.ArchiveAEExtension;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Umberto Cappellini <umberto.cappellini@agfa.com>
 * @author Hesham Elbadawi <bsdreko@gmail.com>
 */
public class Workflow_MIMA_2 extends BasicTest{

    public static final String RESULT_FORMAT = "%n| %-2s | %-45s | %-4s | %-8s |";
    public static final String RESULT_HEADER1 = "%n+----------------------------------------------------------------------+";
    public static final String RESULT_HEADER2 = "%n+                          MIMA Workflow Test 2                        +";
    public static final String RESULT_HEADER2b = "%n+ Server triggers a PIX Query to resolve returned IDs of a C-FIND      +";
    public static final String RESULT_HEADER3 = "%n+----+-----------------------------------------------+------+----------+";
    public static final String RESULT_COLUMNS = "%n| #  | Step                                          | res  | time     |";
    public static final String RESULT_FOOTER1 = "%n+----+-----------------------------------------------+------+----------+";

    private int stepNumber = 0;

    @Test
    @StoreParameters(baseDirectory="./target/test-classes/mima/workflow2/")
    public void MIMA_Workflow_2() throws Exception {
        //remoteconfig change
        DicomConfiguration remoteConfig = getRemoteConfig();
        TestUtils.backUpRemoteConfig(remoteConfig);
        if(remoteConfig.findDevice("hl7rcv") == null) {
            remoteConfig.persist(TestUtils.createPixConsumer("hl7rcv", "HL7RCV^DCM4CHEE", "localhost", "2575", "dicom"));
        }
        Device arcDev = remoteConfig.findDevice("dcm4chee-arc");
        ApplicationEntity ae = arcDev.getApplicationEntity("DCM4CHEE");
        ArchiveAEExtension aeExt = ae.getAEExtension(ArchiveAEExtension.class);
        aeExt.setLocalPIXConsumerApplication("HL7RCV^DCM4CHEE");
        aeExt.setRemotePIXManagerApplication("HL7RCV^DCM4CHEE");
        remoteConfig.merge(arcDev);
        // storage
        StoreTool storeTool = (StoreTool) TestToolFactory.createToolForTest(TestToolType.StoreTool, this);
        storeTool.store(
                "Send MIMA Patient 1", "patient_140703_0002.xml");
        StoreResult storeResults = (StoreResult) storeTool.getResult();
        printLine("STORE: Sent patient 140703_0002^^^DCM4CHEE_SOURCE",
                storeResults.getFailures() > 0 ? "KO" : "OK", storeResults.getTime());

        // start pix manager
        long t1 = System.currentTimeMillis();
        Properties config = LoadProperties.load(Workflow_MIMA_2.class);
        File pix_xslt = new File(Workflow_MIMA_2.class.getResource(
                "/mima/workflow2/pix_mgr.xsl").getFile());
        String host = config.getProperty("pixmgr.host");
        int port = new Integer(config.getProperty("pixmgr.port"));
        HL7RcvTest pixmgr = new HL7RcvTest(host, port, pix_xslt);
        printLine("Started PIX Manager", "OK",
                (System.currentTimeMillis() - t1));
        pixmgr.bind();

        // query retrieve
        QueryTool queryTool = (QueryTool) TestToolFactory.createToolForTest(TestToolType.QueryTool, this);
        queryTool.addQueryTag(Tag.PatientID, "140703_0002");
        queryTool.addQueryTag(Tag.IssuerOfPatientID, "DCM4CHEE_SOURCE");
        queryTool.addReturnTag(Tag.OtherPatientIDsSequence);
        queryTool.query("Patient ID:140703_0002^^^DCM4CHEE_SOURCE",false,false);
        QueryResult queryResult = (QueryResult) queryTool.getResult();
        Attributes expectedResult = new Attributes();
        expectedResult.setString(Tag.PatientID, VR.LO, "140703_1002"); // it's the value returned by the
                                               // PIX Query
        AssertionUtils.assertContainsAttrsWithinSequence(queryResult.getQueryResponse()
                , expectedResult, Tag.OtherPatientIDsSequence);
        printLine("[C-FIND:PatientID:140703_0002] ret:140703_1002", "OK",
                queryResult.getTime());

        // stop pix manager
        t1 = System.currentTimeMillis();
        pixmgr.unbind();
        printLine("Stopped PIX Manager", "OK",
                (System.currentTimeMillis() - t1));
        TestUtils.rollBackRemoteConfig(remoteConfig);
    }

    @BeforeClass
    public static void header() throws IOException {
        System.out.printf(RESULT_HEADER1);
        System.out.printf(RESULT_HEADER2);
        System.out.printf(RESULT_HEADER1);
        System.out.printf(RESULT_HEADER2b);
        System.out.printf(RESULT_HEADER3);
        System.out.printf(RESULT_COLUMNS);
        System.out.printf(RESULT_FOOTER1);
    }

    @AfterClass
    public static void footer() throws IOException {
        System.out.printf(RESULT_FOOTER1);
        System.out.println();
    }

    public void printLine(String stepDescription, String result, long time) {

        // format printout
        System.out
                .format(RESULT_FORMAT, ++stepNumber,
                        StringUtils.truncate(stepDescription, 45), result, time
                                + " ms");
    }
}
