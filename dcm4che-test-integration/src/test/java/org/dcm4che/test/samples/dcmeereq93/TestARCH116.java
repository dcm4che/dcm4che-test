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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.MissingArgumentException;
import org.dcm4che.test.annotations.StoreParameters;
import org.dcm4che.test.annotations.WadoRSParameters;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.common.TestToolFactory.TestToolType;
import org.dcm4che.test.utils.AssertionUtils;
import org.dcm4che.test.utils.TestUtils;
import org.dcm4che3.conf.api.ConfigurationException;
import org.dcm4che3.conf.api.DicomConfiguration;
import org.dcm4che3.net.ApplicationEntity;
import org.dcm4che3.net.Connection;
import org.dcm4che3.net.Device;
import org.dcm4che3.net.IncompatibleConnectionException;
import org.dcm4che3.tool.storescu.test.StoreTool;
import org.dcm4che3.tool.wadors.test.WadoRSResult;
import org.dcm4che3.tool.wadors.test.WadoRSTool;
import org.dcm4chee.archive.conf.ArchiveAEExtension;
import org.dcm4chee.archive.conf.ArchiveDeviceExtension; 
import org.dcm4chee.archive.conf.RetrieveSuppressionCriteria;
import org.junit.Test;
/**
 * @author Hesham Elbadawi <bsdreko@gmail.com>
 * 
 */
public class TestARCH116 extends BasicTest {

    @Test
    @WadoRSParameters(url = "wado/DCM4CHEE/studies/1.2.40.0.13.0.11.2118.1.2008045825.176015.20081021134411", retrieveDir = "/tmp/")
    @StoreParameters(aeTitle = "DCM4CHEE", baseDirectory = "./", connection = "dicom", sourceAETitle = "STORESCU", sourceDevice = "storescu")
    public void testWadoRSEliminateStructuredReportObjectFromResponse()
            throws MissingArgumentException, IOException, InterruptedException,
            IncompatibleConnectionException, GeneralSecurityException,
            ConfigurationException {
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
        
        // add the test client to the host AE map so wado can map it to an AE
        ArchiveDeviceExtension arcDevExt = arcDevice
                .getDeviceExtension(ArchiveDeviceExtension.class);
        Map<String, String> hostNameMap = new HashMap<String, String>();
        hostNameMap.put("127.0.0.1", "SOMEWADOCLIENT");
        arcDevExt.setHostNameToAETitleMap(hostNameMap);
        
        // disable dns/hostname lookup
        arcDevExt.setHostnameAEResolution(false);
        
        // now add the test client device to the configuration
        Device client = new Device("somewadoclient");
        Connection conn = new Connection();
        conn.setCommonName("dicom");
        conn.setHostname("localhost");
        client.addConnection(conn);
        try{
        remoteConfig.findDevice("somewadoclient");
            remoteConfig.persist(client);
        }
        catch (ConfigurationException e) {
            remoteConfig.merge(client);
        }
        ApplicationEntity clientAE = new ApplicationEntity("SOMEWADOCLIENT");
        client.addApplicationEntity(clientAE);
        clientAE.addConnection(conn);
        remoteConfig.merge(client);
        
        // the client should not have the transfer capabilities for Basic Text
        // SR
        // here is how to add other TCs (not will be applied here)
        // clientAE.addTransferCapability(new TransferCapability(commonName,
        // sopClass, role, transferSyntaxes))
        remoteConfig.merge(arcDevice);
        // send wado rs request
        WadoRSTool wadorsTool = (WadoRSTool) TestToolFactory.createToolForTest(
                TestToolType.WadoRSTool, this);

        wadorsTool
                .wadoRS("Test Retrieve via wadors Basic Text SR - REQUIREMENT[DCMEEREQ-93] - "
                        + " [ARCH-116]",false);
        WadoRSResult result = (WadoRSResult) wadorsTool.getResult();
        
        TestUtils.reloadServerConfig(this);
        
        // should retrieve nothing at all here except the header and we don't dump them
        AssertionUtils.assertEquals(result.getResponse().getRetrievedInstance()
                .size(), 0);
        
    }
}
