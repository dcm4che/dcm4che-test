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

package org.dcm4che.test.integration.query.performance;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.dcm4che3.data.Tag;
import org.dcm4che3.soundex.ESoundex;
import org.dcm4che3.soundex.Soundex;
import org.dcm4che3.tool.findscu.test.QueryResult;
import org.dcm4che3.tool.findscu.test.QueryTest;
import org.junit.Test;

/**
 * @author Umberto Cappellini <umberto.cappellini@agfa.com>
 * 
 */
public class Query_PN {

    public static String[] names = null;

    @Test
    public void Query_PN_2() throws Exception {

        String most_common_family = null;
        int most_common_family_count = Integer.MIN_VALUE;

        for (int i = 0; i < 5; i++) {
            String name = randomFamilyName();

            QueryTest test = QueryPerformanceTestSuite.getQueryTest();
            test.addTag(Tag.PatientName, name);
            QueryResult result = test.query("Patient Family Name:" + name);
            QueryPerformanceTestSuite.printResults(result);

            QueryTest testf = QueryPerformanceTestSuite.getQueryTest();
            testf.addTag(Tag.PatientName, name);
            QueryResult resultf = testf.queryfuzzy("Fuzzy Patient Family Name:"
                    + name
                    + " ("
                    + new ESoundex().toFuzzy(name.substring(0,
                            name.length() - 1)) + ")");
            QueryPerformanceTestSuite.printResults(resultf);
            
            if (result.getNumMatches() > most_common_family_count) {
                most_common_family = name;
                most_common_family_count = result.getNumMatches();
            }
        }
        for (int i = 0; i < 5; i++) {
            String name = most_common_family+"^"+randominitial1();

            QueryTest test = QueryPerformanceTestSuite.getQueryTest();
            test.addTag(Tag.PatientName, name);
            QueryResult result = test.query("Patient Name:" + name);
            QueryPerformanceTestSuite.printResults(result);
            
            if (result.getNumMatches() > most_common_family_count) {
                most_common_family = name;
                most_common_family_count = result.getNumMatches();
            }
        }
        
        
    }

    @Test
    public void Query_PN_1() throws Exception {

        for (int i = 0; i < 5; i++) {
            QueryTest test = QueryPerformanceTestSuite.getQueryTest();
            String r1 = randominitial1();
            test.addTag(Tag.PatientName, r1);
            QueryResult result = test.query("Patient Initial 1 letter:"
                    + r1);
            QueryPerformanceTestSuite.printResults(result);
        }

        for (int i = 0; i < 5; i++) {
            QueryTest test = QueryPerformanceTestSuite.getQueryTest();
            String r2 = randominitial2();
            test.addTag(Tag.PatientName, r2);
            QueryResult result = test.query("Patient Initial 2 letters:"
                    + r2);
            QueryPerformanceTestSuite.printResults(result);
        }
    }

    private String randominitial1() {

        Random r = new Random();
        char c1 = (char) (r.nextInt(26) + 'a');
        return new String(new char[] { c1 }).toUpperCase() + "*";
    }

    private String randominitial2() {

        Random r = new Random();
        char c1 = (char) (r.nextInt(26) + 'a');
        char c2 = (char) (r.nextInt(26) + 'a');
        return new String(new char[] { c1, c2 }).toUpperCase() + "*";
    }

    private String randomFamilyName() {

        try {
            if (names == null) {
                String n = FileUtils.readFileToString(new File(Query_PN.class
                        .getResource("/names.txt").getFile()));
                names = n.split("\\r?\\n");
            }
            return names[new Random().nextInt(names.length)];

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String randomGivenName() {

        try {
            if (names == null) {
                String n = FileUtils.readFileToString(new File(Query_PN.class
                        .getResource("/names.txt").getFile()));
                names = n.split("\\r?\\n");
            }
            return names[new Random().nextInt(names.length)];

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
