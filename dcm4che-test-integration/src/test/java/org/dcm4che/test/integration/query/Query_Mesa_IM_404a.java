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

import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.common.TestToolFactory.TestToolType;
import org.dcm4che.test.utils.AssertionUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.tool.findscu.test.QueryResult;
import org.dcm4che3.tool.findscu.test.QueryTool;
import org.junit.Test;

/**
 * @author Umberto Cappellini <umberto.cappellini@agfa.com>
 * 
 */
public class Query_Mesa_IM_404a extends BasicTest{

    @Test
    public void step1_query_sop_instances() throws Exception {
        QueryTool queryTool = (QueryTool) TestToolFactory.createToolForTest(TestToolType.QueryTool, this);
        queryTool.addQueryTag(Tag.QueryRetrieveLevel, "IMAGE");
        queryTool.addQueryTag(Tag.StudyInstanceUID, "1.2.840.113674.514.212.200");
        queryTool.addQueryTag(Tag.SeriesInstanceUID, "1.2.840.113674.514.212.81.300");
        queryTool.addReturnTag(Tag.SOPInstanceUID);
        queryTool.setExpectedMatches(2);
        queryTool.query("SOPInstanceUID:<<2>>",false,false);
        QueryResult result = (QueryResult) queryTool.getResult();
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.setString(Tag.SOPInstanceUID, VR.UI, "1.2.840.113674.950809132354242.100");
        AssertionUtils.assertContainsAttrs(result.getQueryResponse(), expectedAttributes);
        expectedAttributes.setString(Tag.SOPInstanceUID, VR.UI, "1.2.840.113674.950809132355260.100");
        AssertionUtils.assertContainsAttrs(result.getQueryResponse(), expectedAttributes);
        QueryTestSuite.printResults(result);
    }
    
    @Test
    public void step2_query_sop_class_uid() throws Exception {
        
        QueryTool queryTool = (QueryTool) TestToolFactory.createToolForTest(TestToolType.QueryTool, this);
        queryTool.addQueryTag(Tag.QueryRetrieveLevel, "IMAGE");
        queryTool.addQueryTag(Tag.StudyInstanceUID, "1.2.840.113674.514.212.200");
        queryTool.addQueryTag(Tag.SeriesInstanceUID, "1.2.840.113674.514.212.81.300");
        queryTool.addReturnTag(Tag.SOPClassUID);
        queryTool.setExpectedMatches(2);
        queryTool.query("SOPClassUID:1.2.840.10008.5.1.4.1.1.2",false,false);
        QueryResult result = (QueryResult) queryTool.getResult();
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.setString(Tag.SOPClassUID, VR.UI, "1.2.840.10008.5.1.4.1.1.2");
        AssertionUtils.assertContainsAttrs(result.getQueryResponse(), expectedAttributes);
        QueryTestSuite.printResults(result);

    }

    @Test
    public void step3_query_rows() throws Exception {
        
        QueryTool queryTool = (QueryTool) TestToolFactory.createToolForTest(TestToolType.QueryTool, this);
        queryTool.addQueryTag(Tag.QueryRetrieveLevel, "IMAGE");
        queryTool.addQueryTag(Tag.StudyInstanceUID, "1.2.840.113674.514.212.200");
        queryTool.addQueryTag(Tag.SeriesInstanceUID, "1.2.840.113674.514.212.81.300");
        queryTool.addReturnTag(Tag.Rows);
        queryTool.setExpectedMatches(2);
        queryTool.query("Rows:691",false,false);
        QueryResult result = (QueryResult) queryTool.getResult();
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.setString(Tag.Rows, VR.US, "691");
        AssertionUtils.assertContainsAttrs(result.getQueryResponse(), expectedAttributes);
        QueryTestSuite.printResults(result);

    }

    @Test
    public void step4_query_columns() throws Exception {
        
        QueryTool queryTool = (QueryTool) TestToolFactory.createToolForTest(TestToolType.QueryTool, this);
        queryTool.addQueryTag(Tag.QueryRetrieveLevel, "IMAGE");
        queryTool.addQueryTag(Tag.StudyInstanceUID, "1.2.840.113674.514.212.200");
        queryTool.addQueryTag(Tag.SeriesInstanceUID, "1.2.840.113674.514.212.81.300");
        queryTool.addReturnTag(Tag.Columns);
        queryTool.setExpectedMatches(2);
        queryTool.query("Columns:512",false,false);
        QueryResult result = (QueryResult) queryTool.getResult();
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.setString(Tag.Columns, VR.US, "512");
        AssertionUtils.assertContainsAttrs(result.getQueryResponse(), expectedAttributes);
        QueryTestSuite.printResults(result);

    }
    
    @Test
    public void step5_query_bits_allocated() throws Exception {
        
        QueryTool queryTool = (QueryTool) TestToolFactory.createToolForTest(TestToolType.QueryTool, this);
        queryTool.addQueryTag(Tag.QueryRetrieveLevel, "IMAGE");
        queryTool.addQueryTag(Tag.StudyInstanceUID, "1.2.840.113674.514.212.200");
        queryTool.addQueryTag(Tag.SeriesInstanceUID, "1.2.840.113674.514.212.81.300");
        queryTool.addReturnTag(Tag.BitsAllocated);
        queryTool.setExpectedMatches(2);
        queryTool.query("BitsAllocated:16",false,false);
        QueryResult result = (QueryResult) queryTool.getResult();
        Attributes expectedAttributes = new Attributes();
        expectedAttributes.setString(Tag.BitsAllocated, VR.US, "16");
        AssertionUtils.assertContainsAttrs(result.getQueryResponse(), expectedAttributes);
        QueryTestSuite.printResults(result);

    }
}
