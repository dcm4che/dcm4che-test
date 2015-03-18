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
package org.dcm4che.test.samples.dcmeereq187;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.dcm4che.test.annotations.StowRSParameters;
import org.dcm4che.test.annotations.WadoURIParameters;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.common.TestToolFactory.TestToolType;
import org.dcm4che.test.utils.AssertionUtils;
import org.dcm4che.test.utils.TestUtils;
import org.dcm4che3.conf.api.AttributeCoercion;
import org.dcm4che3.conf.api.ConfigurationException;
import org.dcm4che3.conf.api.DicomConfiguration;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.net.ApplicationEntity;
import org.dcm4che3.net.Connection;
import org.dcm4che3.net.Device;
import org.dcm4che3.net.Dimse;
import org.dcm4che3.net.TransferCapability.Role;
import org.dcm4che3.tool.common.SimpleHTTPResponse;
import org.dcm4che3.tool.stowrs.test.StowRSResult;
import org.dcm4che3.tool.stowrs.test.StowRSTool;
import org.dcm4che3.tool.stowrs.test.StowRSTool.StowMetaDataType;
import org.dcm4che3.tool.wadouri.test.WadoURIResult;
import org.dcm4che3.tool.wadouri.test.WadoURITool;
import org.dcm4chee.archive.conf.ArchiveAEExtension;
import org.dcm4chee.archive.conf.ArchiveDeviceExtension;
import org.junit.Test;

/**
 * @author Hesham Elbadawi <bsdreko@gmail.com>
 *
 * This is a sample test with the purpose to demonstrate how a requirement 
 * involving the use of a particular tool can be written with the framework.
 * 
 * This test verifies requirement 187 in the requirements thread DCMEEREQ
 * This test uses the tools stow and wado URI
 */
public class TestDCMEEREQ187 extends BasicTest {

    /*
     * This test is not enough to test the requirement 187, this is just a sample
     * that should serve as a hint for how to use the testing tools in an auto test
     */
    @Test
    @StowRSParameters( url = "stow/DCM4CHEE/studies")
    @WadoURIParameters( studyUID="1.2.40.0.13.1.142958397687231271895664088740396993596"
    ,seriesUID="1.2.40.0.13.1.206652427225600637550020550932681501663"
    ,objectUID="1.2.40.0.13.1.322945880159281691781477138426853161624",contentType="application/dicom"
    , url = "wado/DCM4CHEE", transferSyntax="1.2.840.10008.1.2.4.70",retrieveDir="/tmp/")
    public void testCoercionofAttributesStowRS()
            throws Exception {
        
        
        // add the test client to the host AE map so stow can map it to an AE
        DicomConfiguration remoteConfig = getRemoteConfig();
        Device arcDevice = remoteConfig.findDevice("dcm4chee-arc");
        ArchiveDeviceExtension arcDevExt = arcDevice
                .getDeviceExtension(ArchiveDeviceExtension.class);
        Map<String, String> hostNameMap = new HashMap<String, String>();
        hostNameMap.put("127.0.0.1", "SOMESTOWCLIENT");
        arcDevExt.setHostNameToAETitleMap(hostNameMap);
        
        // disable dns/hostname lookup
        arcDevExt.setHostnameAEResolution(false);
        // first backup device
        TestUtils.backupDevice(arcDevice);

        ApplicationEntity ae = arcDevice.getApplicationEntity("DCM4CHEE");
        ArchiveAEExtension arcAEExt = ae
                .getAEExtension(ArchiveAEExtension.class);
        //add attribute coercion, here the test is only intended to show how to do it
        AttributeCoercion coercion = new AttributeCoercion("anonymizePersonName"
                , null, Dimse.C_STORE_RQ, Role.SCP , new String[] {"SOMESTOWCLIENT"}
                , new String[]{"somestowclient"}, "${jboss.server.config.url}/dcm4chee-arc/nullify-pn.xsl");
        if(arcAEExt.getAttributeCoercion(null, Dimse.C_STORE_RQ, Role.SCP, "SOMESTOWCLIENT")  != null) {
        TestUtils.removeCoercionTemplate(coercion, ae, remoteConfig);
        }
        TestUtils.addCoercionTemplate(coercion, ae, remoteConfig);
                // now add the test client device to the configuration
        Device client = new Device("somestowclient");
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
        ApplicationEntity clientAE = new ApplicationEntity("SOMESTOWCLIENT");
        client.addApplicationEntity(clientAE);
        clientAE.addConnection(conn);
        remoteConfig.merge(client);
        remoteConfig.merge(arcDevice);
        // send wado rs request
        StowRSTool stowRSTool = (StowRSTool) TestToolFactory.createToolForTest(TestToolType.StowTool, this);
        File fileToSend = new File("target/test-classes/test.dcm");
        stowRSTool.send("Test Stow and anonymize - REQUIREMENT[DCMEEREQ-93] - "
                + " [ARCH-116]", StowMetaDataType.NO_METADATA_DICOM, fileToSend);

        StowRSResult result = (StowRSResult) stowRSTool.getResult();
        
        TestUtils.reloadServerConfig(this);
        
        //check stow is done successfully
        AssertionUtils.assertEquals(((SimpleHTTPResponse)result.getResponses().get(0)).getStatus(), 200);
        
        //get the instance via wado uri
        WadoURITool wadoUriTool = (WadoURITool) TestToolFactory.createToolForTest(TestToolType.WadoURITool, this);
        wadoUriTool.wadoURI("Test Stow Basic Text SR - REQUIREMENT[DCMEEREQ-93] - "
                + " [ARCH-187]");
        //check patient is anonymized
        WadoURIResult wadouriResult = (WadoURIResult) wadoUriTool.getResult();
        Attributes attrs = TestUtils.getWadoURIResponseAttributes(wadouriResult.getResponse());
        
        //AssertionUtils.assertAttrsContainTagWithValue(attrs,Tag.PatientMotherBirthName,"");
    }
}
