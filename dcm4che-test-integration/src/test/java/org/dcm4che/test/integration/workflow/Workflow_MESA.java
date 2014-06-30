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

package org.dcm4che.test.integration.workflow;

import java.io.IOException;

import org.dcm4che.test.ConnectTest;
import org.dcm4che.test.integration.mpps.MppsTestSuite;
import org.dcm4che.test.integration.store.StoreTestSuite;
import org.dcm4che.test.tool.ConnectionUtil;
import org.dcm4che3.net.Connection;
import org.dcm4che3.tool.mppsscu.test.MppsResult;
import org.dcm4che3.tool.storescu.test.StoreResult;
import org.dcm4che3.util.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Umberto Cappellini <umberto.cappellini@agfa.com>
 * 
 */
public class Workflow_MESA {

    public static final String RESULT_FORMAT = "%n| %-2s | %-45s | %-4s | %-8s |";
    public static final String RESULT_HEADER1 = "%n+----------------------------------------------------------------------+";
    public static final String RESULT_HEADER2 = "%n+                          MESA Workflow Tests                         +";
    public static final String RESULT_HEADER3 = "%n+----+-----------------------------------------------+------+----------+";
    public static final String RESULT_COLUMNS = "%n| #  | Step                                          | res  | time     |";
    public static final String RESULT_FOOTER1 = "%n+----+-----------------------------------------------+------+----------+";

    private int stepNumber = 0;

    @Test
    public void MESA_PIR_Workflow_103() throws Exception {

        String studyDIR = "modality/MR/MR4/MR4S1";
        printLine("Testing Study: " + studyDIR, "OK", 0);

        // test if connection is alive
        Connection conn = new ConnectTest().test();
        printLine("Test DICOM Connection: " + conn.getHostname() + ":"
                        + conn.getPort(), ConnectionUtil.isAlive(conn) ? "OK"
                        : "KO", 0);

        // missing: HL7 scheduling ORM

        // mpps
        MppsResult mpps = MppsTestSuite.getMppsTest().mppsscu("Send MPPS for Study",
                "modality/MR/MR4/MR4S1");
        printLine("MPPS N-CREATE: Sent " + mpps.getnCreateSent() + " Message",
                mpps.getnCreateFailures() > 0 ? "KO" : "OK",
                mpps.getCreatetime());
        printLine("MPPS N-SET: Sent " + mpps.getnSetSent() + " Message",
                mpps.getnSetFailures() > 0 ? "KO" : "OK", mpps.getSettime());

        // storage
        StoreResult store = StoreTestSuite.getStoreTest().store("Send Study: MR/MR4/MR4S1",
                "modality/MR/MR4/MR4S1");
        printLine("DICOM STORAGE: Sent " + store.getFilesSent() + " Objects",
                store.getFailures() > 0 ? "KO" : "OK", store.getTime());

    }

    @BeforeClass
    public static void header() throws IOException {
        System.out.printf(RESULT_HEADER1);
        System.out.printf(RESULT_HEADER2);
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
