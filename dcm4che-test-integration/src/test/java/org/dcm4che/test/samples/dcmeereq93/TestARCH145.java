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
package org.dcm4che.test.samples.dcmeereq93;

import org.dcm4che.test.annotations.GetParameters;
import org.dcm4che.test.annotations.StoreParameters;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.common.TestToolFactory.TestToolType;
import org.dcm4che.test.utils.AssertionUtils;
import org.dcm4che.test.utils.TestUtils;
import org.dcm4che3.conf.api.DicomConfiguration;
import org.dcm4che3.data.Tag;
import org.dcm4che3.net.ApplicationEntity;
import org.dcm4che3.net.Device;
import org.dcm4che3.tool.getscu.test.RetrieveResult;
import org.dcm4che3.tool.getscu.test.RetrieveTool;
import org.dcm4che3.tool.storescu.test.StoreTool;
import org.dcm4chee.archive.conf.ArchiveAEExtension;
import org.dcm4chee.archive.conf.RetrieveSuppressionCriteria;
import org.junit.Test;

/**
 * @author Hesham Elbadawi <bsdreko@gmail.com>
 * 
 */

public class TestARCH145 extends BasicTest {

    @Test
    @GetParameters(aeTitle = "DCM4CHEE", connection="dicom", retrieveLevel="STUDY", retrieveDir="/tmp/", sourceAETitle="GETSCU", sourceDevice = "getscu")
    @StoreParameters(aeTitle = "DCM4CHEE", baseDirectory = "./", connection = "dicom", sourceAETitle = "STORESCU", sourceDevice = "storescu")
    public void testGetEliminateStructuredReportObjectFromResponse()
            throws Exception {
        // store Basic Text SR
        StoreTool storeTool = (StoreTool) TestToolFactory.createToolForTest(
                TestToolType.StoreTool, this);
        storeTool.store(
                "Test Store Basic Text SR - REQUIREMENT[DCMEEREQ-93] - "
                        + " [ARCH-116]", "./target/test-classes/req44_sr.dcm");

        // add supression criteria
        DicomConfiguration remoteConfig = getRemoteConfig();
        Device arcDevice = remoteConfig.findDevice("dcm4chee-arc");
        
        // first backup device
        TestUtils.backupDevice(arcDevice);

        ApplicationEntity ae = arcDevice.getApplicationEntity("DCM4CHEE");
        ArchiveAEExtension arcAEExt = ae
                .getAEExtension(ArchiveAEExtension.class);
        
        RetrieveSuppressionCriteria suppress = new RetrieveSuppressionCriteria();
        
        // checks for transfer capability each time a retrieve is performed
        suppress.setCheckTransferCapabilities(true);
        arcAEExt.setRetrieveSuppressionCriteria(suppress);
        
        // test client already exists in the sample configuration (getscu)
        
        // the client should not have the transfer capabilities for Basic Text
        // SR
        Device getscuDevice = remoteConfig.findDevice("getscu");
        ApplicationEntity getAE = getscuDevice.getApplicationEntity("GETSCU");
        AssertionUtils.assertFalse(TestUtils.containsSOPClassAndTransferSyntax(
                "1.2.840.10008.5.1.4.1.1.88.11", getAE.getTransferCapabilities()
                , null));
        remoteConfig.merge(arcDevice);
        // send wado rs request
        RetrieveTool getTool = (RetrieveTool) TestToolFactory.createToolForTest(TestToolType.GetTool, this);
        getTool.addTag(Tag.StudyInstanceUID, "1.2.40.0.13.0.11.2118.1.2008045825.176015.20081021134411");
        getTool.retrieve("Test Retrieve Basic via C-GET Text SR - REQUIREMENT[DCMEEREQ-93] - "
                        + " [ARCH-116]");
        
        RetrieveResult result = (RetrieveResult) getTool.getResult();
        
        TestUtils.reloadServerConfig(this);
        
        // should retrieve nothing at all here except the header and we don't dump them
        AssertionUtils.assertEquals(result.getNumSuccess(), 0);
        
    }
}
