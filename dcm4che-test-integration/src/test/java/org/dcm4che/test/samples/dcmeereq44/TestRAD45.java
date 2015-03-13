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
 * Portions created by the Initial Developer are Copyright (C) 2012
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
package org.dcm4che.test.samples.dcmeereq44;

import org.apache.commons.cli.MissingArgumentException;
import org.dcm4che.test.annotations.MoveParameters;
import org.dcm4che.test.annotations.QueryParameters;
import org.dcm4che.test.annotations.StoreSCPParameters;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolException;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.common.TestToolFactory.TestToolType;
import org.dcm4che.test.utils.AssertionUtils;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Sequence;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.tool.findscu.test.QueryResult;
import org.dcm4che3.tool.movescu.test.MoveResult;
import org.dcm4che3.tool.movescu.test.MoveTool;
import org.dcm4che3.tool.storescp.test.StoreSCPResult;
import org.dcm4che3.tool.storescp.test.StoreSCPTool;
import org.junit.Test;
/**
 * @author Hesham Elbadawi <bsdreko@gmail.com>
 *
 * Tests querying a structured report from the image manager
 *  using one of the structured report attributes on IMAGE level
 */
public class TestRAD45 extends BasicTest{
    
    private static String testDescription = "Test Retrieve SR - REQUIREMENT[DCMEEREQ-44] - "
            + "IHE - TRANSACTION[RAD-45]";
    //@Test
    //included for demonstrating purposes
    public void testQuerySR_Defaults_SetLevel() throws MissingArgumentException {
        Attributes moveAttrs = new Attributes();
        moveAttrs.setString(Tag.StudyInstanceUID, VR.UI, "1.2.40.0.13.0.11.2118.1.2008045825.176015.20081021134411");
        MoveResult result = (MoveResult) move(testDescription, moveAttrs, 1);
    }
    
    @Test
    @MoveParameters(retrieveLevel="IMAGE",aeTitle="DCM4CHEE"
            ,connection="dicom",sourceAETitle="MOVESCU",sourceDevice="movescu",destAEtitle="STORESCP")
    @StoreSCPParameters(connection="dicom",noStore=true,sourceAETitle="STORESCP",sourceDevice="storescp")
    public void testQuerySR_CustomParams() throws MissingArgumentException, InterruptedException {
        //create a test SCP to listen for the incoming C-Store
        //scp closes after the association closes
        
        StoreSCPTool scpTool = (StoreSCPTool) TestToolFactory.createToolForTest(TestToolType.StoreSCPTool, this);
        scpTool.start(testDescription);
        
        //move now 
        Attributes moveAttrs = new Attributes();
        moveAttrs.setString(Tag.StudyInstanceUID, VR.UI, "1.2.40.0.13.0.11.2118.1.2008045825.176015.20081021134411");
        MoveTool tool = (MoveTool) TestToolFactory.createToolForTest(TestToolType.MoveTool, this);
        tool.setExpectedMatches(1);
        tool.addAll(moveAttrs);
        
        try {
            tool.move(testDescription);
        } catch (Exception e) {
            throw new TestToolException(e);
        }
        
        MoveResult result = (MoveResult) tool.getResult();
        
        StoreSCPResult scpResult  = (StoreSCPResult) scpTool.getResult();
        AssertionUtils.assertEquals(scpResult.getSopUIDs().size(), 1);
        AssertionUtils.assertTrue(scpResult.getSopUIDs().get(0)
                .equalsIgnoreCase("1.11.111111.100073424111037649484053784200726879018"));
    }

    
}
