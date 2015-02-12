/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
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

package org.dcm4che.test.integration.query.performance;

import java.io.IOException;

import org.dcm4che.test.ConnectTest;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che3.tool.findscu.test.QueryResult;
import org.dcm4che3.util.StringUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Umberto Cappellini <umberto.cappellini@agfa.com>
 * @author Hesham Elbadawi <bsdreko@gmail.com>
 */
@RunWith(Suite.class)
@SuiteClasses({ Query_PN.class, Query_Patient_ID.class })
public class QueryPerformanceTestSuite extends BasicTest{

    private static final String RESULT_FORMAT = "%n| %-2s | %-48s | %-6d | %-10s | %-10s |";
    private static final String RESULT_HEADER1 = "%n+------------------------------------------------------------------------------------------+";
    private static final String RESULT_HEADER2 = "%n+                                  Query Tests Suite                                       +";
    private static final String RESULT_HEADER3 = "%n+----+--------------------------------------------------+--------+------------+------------+";
    private static final String RESULT_COLUMNS = "%n| #  | Description                                      | resp   |   first    |   total    |";
    private static final String RESULT_FOOTER1 = "%n+----+--------------------------------------------------+--------+------------+------------+";

    public static int testNumber;

    @BeforeClass
    public static void header() throws Exception {

        testNumber = 0;

        // test if connection is alive
        new ConnectTest().test();

        // printout results header
        System.out.printf(RESULT_HEADER1);
        System.out.printf(RESULT_HEADER2);
        System.out.printf(RESULT_HEADER3);
        System.out.printf(RESULT_COLUMNS);
        System.out.printf(RESULT_FOOTER1);

    }

    @AfterClass
    public static void footer() throws IOException {

        testNumber = 0;

        // printout results footer
        System.out.printf(RESULT_FOOTER1);
        System.out.println();
    }

    public static void printResults(QueryResult result) {

        // format printout
        System.out.format(RESULT_FORMAT, ++QueryPerformanceTestSuite.testNumber,
                StringUtils.truncate(result.getTestDescription(), 48),
                result.getNumMatches(),
                result.getTimeFirst() + " ms",
                result.getTime() + " ms");
    }
    
}
