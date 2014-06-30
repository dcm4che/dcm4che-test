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

package org.dcm4che.test.integration.stgcmt;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.dcm4che.test.tool.LoadProperties;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.UID;
import org.dcm4che3.net.ApplicationEntity;
import org.dcm4che3.net.Connection;
import org.dcm4che3.net.Device;
import org.dcm4che3.net.IncompatibleConnectionException;
import org.dcm4che3.tool.common.DicomFiles;
import org.dcm4che3.tool.stgcmtscu.StgCmtSCU;

/**
 * @author Umberto Cappellini <umberto.cappellini@agfa.com>
 * 
 */
public class StgCmtTest {

    private String testDescription;
    private String fileName;

    private long totalSize;
    private int filesSent;
    private int warnings;    
    private int failures;

    /**
     * @param testName
     * @param testDescription
     * @param fileName
     */
    public StgCmtTest(String testDescription, String fileName) {
        super();
        this.testDescription = testDescription;
        this.fileName = fileName;
    }
    
    public void stgcmt() throws IOException, InterruptedException,
            IncompatibleConnectionException, GeneralSecurityException {

        long t1, t2;

        Properties config = LoadProperties.load(StgCmtTest.class);
        String host = config.getProperty("remoteConn.hostname");
        int port = new Integer(config.getProperty("remoteConn.port"));
        String aeTitle = config.getProperty("store.aetitle");
        String directory = config.getProperty("store.directory");

        int stgcmtport = new Integer(config.getProperty("stgcmt.port"));
        
        File file = new File(directory, fileName);

        assertTrue(
                "file or directory does not exists: " + file.getAbsolutePath(),
                file.exists());

        Device device = new Device("stgcmtscu");
        Connection conn = new Connection();
        conn.setPort(stgcmtport);
        device.addConnection(conn);
        ApplicationEntity ae = new ApplicationEntity("STGCMTSCU");
        device.addApplicationEntity(ae);
        ae.addConnection(conn);

        final StgCmtSCU stgcmtscu = new StgCmtSCU(ae);

        // configure
        conn.setMaxOpsInvoked(0);
        conn.setMaxOpsPerformed(0);
        
        stgcmtscu.getAAssociateRQ().setCalledAET(aeTitle);
        stgcmtscu.getRemoteConnection().setHostname(host);
        stgcmtscu.getRemoteConnection().setPort(port);
        stgcmtscu.setTransferSyntaxes(new String[]{UID.ImplicitVRLittleEndian, UID.ExplicitVRLittleEndian, UID.ExplicitVRBigEndianRetired});
        stgcmtscu.setAttributes(new Attributes());
        stgcmtscu.setStorageDirectory(new File("."));
        
        // scan
        t1 = System.currentTimeMillis();
        DicomFiles.scan(Arrays.asList(file.getAbsolutePath()), new DicomFiles.Callback() {
            
            @Override
            public boolean dicomFile(File f, Attributes fmi, long dsPos,
                    Attributes ds) {
                return stgcmtscu.addInstance(ds);
            }
        });
        t2 = System.currentTimeMillis();

        // create executor
        ExecutorService executorService =
                Executors.newCachedThreadPool();
        ScheduledExecutorService scheduledExecutorService =
                Executors.newSingleThreadScheduledExecutor();
        device.setExecutor(executorService);
        device.setScheduledExecutor(scheduledExecutorService);
        device.bindConnections();

        // open, send and wait for response
        try {
            stgcmtscu.open();
            stgcmtscu.sendRequests();
         } finally {
            stgcmtscu.close();
            if (conn.isListening()) {
                device.waitForNoOpenConnections();
                device.unbindConnections();
            }
            executorService.shutdown();
            scheduledExecutorService.shutdown();
        }
//        System.out.format(StoreTestSuite.RESULT_FORMAT,
//                ++StoreTestSuite.testNumber,
//                StringUtils.truncate(testDescription, 20), 
//                filesSent, 
//                failures,
//                warnings,
//                FileUtil.humanreadable(totalSize, true),
//                (t2 - t1) + " ms");
    }

}
