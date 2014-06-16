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

package org.dcm4che.test.integration.query;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.junit.Test;

/**
 * @author Umberto Cappellini <umberto.cappellini@agfa.com>
 * 
 */
public class Query_Mesa_IM_404a {

    @Test
    public void step1_query_sop_instances() throws Exception {
        
        QueryTest test = new QueryTest("SOPInstanceUID:<<2>>");
        
        test.addTag(Tag.QueryRetrieveLevel, "IMAGE");
        test.addTag(Tag.StudyInstanceUID, "1.2.840.113674.514.212.200");
        test.addTag(Tag.SeriesInstanceUID, "1.2.840.113674.514.212.81.300");
        
        test.setReturnTag(Tag.SOPInstanceUID);
        
        test.setExpectedResultsNumeber(2);
        
        test.addExpectedResult("1.2.840.113674.950809132354242.100");
        test.addExpectedResult("1.2.840.113674.950809132355260.100");
                
        QueryResult result = test.query();
        QueryTestSuite.printResults(result);
    }
    
    @Test
    public void step2_query_sop_class_uid() throws Exception {
        
        QueryTest test = new QueryTest("SOPClassUID:1.2.840.10008.5.1.4.1.1.2");
        
        test.addTag(Tag.QueryRetrieveLevel, "IMAGE");
        test.addTag(Tag.StudyInstanceUID, "1.2.840.113674.514.212.200");
        test.addTag(Tag.SeriesInstanceUID, "1.2.840.113674.514.212.81.300");
        
        test.setReturnTag(Tag.SOPClassUID);
        
        test.setExpectedResultsNumeber(2);
        
        test.addExpectedResult("1.2.840.10008.5.1.4.1.1.2");
                
        QueryResult result = test.query();
        QueryTestSuite.printResults(result);

    }

    @Test
    public void step3_query_rows() throws Exception {
        
        QueryTest test = new QueryTest("Rows:691");
        
        test.addTag(Tag.QueryRetrieveLevel, "IMAGE");
        test.addTag(Tag.StudyInstanceUID, "1.2.840.113674.514.212.200");
        test.addTag(Tag.SeriesInstanceUID, "1.2.840.113674.514.212.81.300");
        
        test.setReturnTag(Tag.Rows);
        
        test.setExpectedResultsNumeber(2);
        
        test.addExpectedResult("691");
                
        QueryResult result = test.query();
        QueryTestSuite.printResults(result);

    }

    @Test
    public void step4_query_columns() throws Exception {
        
        QueryTest test = new QueryTest("Columns:512");
        
        test.addTag(Tag.QueryRetrieveLevel, "IMAGE");
        test.addTag(Tag.StudyInstanceUID, "1.2.840.113674.514.212.200");
        test.addTag(Tag.SeriesInstanceUID, "1.2.840.113674.514.212.81.300");
        
        test.setReturnTag(Tag.Columns);
        
        test.setExpectedResultsNumeber(2);
        
        test.addExpectedResult("512");
                
        QueryResult result = test.query();
        QueryTestSuite.printResults(result);

    }
    
    @Test
    public void step5_query_bits_allocated() throws Exception {
        
        QueryTest test = new QueryTest("BitsAllocated:16");
        
        test.addTag(Tag.QueryRetrieveLevel, "IMAGE");
        test.addTag(Tag.StudyInstanceUID, "1.2.840.113674.514.212.200");
        test.addTag(Tag.SeriesInstanceUID, "1.2.840.113674.514.212.81.300");
        
        test.setReturnTag(Tag.BitsAllocated);
        
        test.setExpectedResultsNumeber(2);
        
        test.addExpectedResult("16");
                
        QueryResult result = test.query();
        QueryTestSuite.printResults(result);

    }
}
