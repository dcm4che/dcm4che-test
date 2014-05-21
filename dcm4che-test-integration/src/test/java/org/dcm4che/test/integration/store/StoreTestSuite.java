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

package org.dcm4che.test.integration.store;

import java.io.IOException;

import org.dcm4che.test.ConnectTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Umberto Cappellini <umberto.cappellini@agfa.com>
 *
 */
@RunWith(Suite.class)
@SuiteClasses({
    Store_MESA_CT.class, 
    Store_MESA_CR.class,
    Store_MESA_MG.class,
    Store_MESA_MR.class})

public class StoreTestSuite {
   
    public static final String RESULT_FORMAT = "%n| %-2s | %-20s | %-4d | %-4d | %-4d | %-8s | %-8s |";
    public static final String RESULT_HEADER1 = "%n+----------------------------------------------------------------------+";
    public static final String RESULT_HEADER2 = "%n+                           Store Tests Suite                          +";
    public static final String RESULT_HEADER3 = "%n+----+----------------------+------+------+------+----------+----------+";
    public static final String RESULT_COLUMNS = "%n| #  | Description          | sent | fail | warn | size     | time     |";
    public static final String RESULT_FOOTER1 = "%n+----+----------------------+------+------+------+----------+----------+";  
    
    public static int testNumber;
    
    @BeforeClass
    public static void testConnection() throws IOException {
        
        testNumber=0;
        
        //test if connection is alive
        new ConnectTest().test();
        System.out.printf(RESULT_HEADER1);
        System.out.printf(RESULT_HEADER2);
        System.out.printf(RESULT_HEADER3);
        System.out.printf(RESULT_COLUMNS);
        System.out.printf(RESULT_FOOTER1);
    }
    
    @AfterClass
    public static void endTests() throws IOException {
        
        testNumber=0;
        
        System.out.printf(RESULT_FOOTER1);
        System.out.println();
    }
}
