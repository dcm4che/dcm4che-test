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

package org.dcm4che.test;

import static org.junit.Assert.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.dcm4che.test.integration.query.QueryTestSuite;
import org.dcm4che.test.integration.store.StoreTestSuite;
import org.dcm4che.test.tool.ConnectionUtil;
import org.dcm4che.test.tool.FileUtil;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.ElementDictionary;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.UID;
import org.dcm4che3.data.VR;
import org.dcm4che3.io.DicomOutputStream;
import org.dcm4che3.net.ApplicationEntity;
import org.dcm4che3.net.Association;
import org.dcm4che3.net.Connection;
import org.dcm4che3.net.Device;
import org.dcm4che3.net.DimseRSPHandler;
import org.dcm4che3.net.IncompatibleConnectionException;
import org.dcm4che3.net.QueryOption;
import org.dcm4che3.net.Status;
import org.dcm4che3.tool.common.CLIUtils;
import org.dcm4che3.tool.findscu.FindSCU;
import org.dcm4che3.tool.findscu.FindSCU.InformationModel;
import org.dcm4che3.tool.storescu.StoreSCU;
import org.dcm4che3.tool.storescu.StoreSCU.RSPHandlerFactory;
import org.dcm4che3.util.SafeClose;
import org.dcm4che3.util.StringUtils;
import org.dcm4che3.util.TagUtils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Umberto Cappellini <umberto.cappellini@agfa.com>
 * 
 */
public class QueryTest extends Generic {

    private String testDescription;
    private int numMatches;
    private ArrayList<String> returnedValues = new ArrayList<String>(); 
    private Integer returnTag = null;

    private static String[] IVR_LE_FIRST = { UID.ImplicitVRLittleEndian,
            UID.ExplicitVRLittleEndian, UID.ExplicitVRBigEndianRetired };
    
    private Attributes queryatts = new Attributes();
    private int expectedResult = Integer.MIN_VALUE;
    private List<String> expectedValues = null;

    /**
     * @param testName
     * @param testDescription
     * @param fileName
     */
    public QueryTest(String testDescription) {
        super();
        this.testDescription = testDescription;
    }

    public int query() throws IOException, InterruptedException,
            IncompatibleConnectionException, GeneralSecurityException {

        
        Properties config = loadConfig();
        String host = config.getProperty("remoteConn.hostname");
        int port = new Integer(config.getProperty("remoteConn.port"));
        String aeTitle = config.getProperty("query.aetitle");

        FindSCU main = new FindSCU();
        main.getAAssociateRQ().setCalledAET(aeTitle);
        main.getRemoteConnection().setHostname(host);
        main.getRemoteConnection().setPort(port);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ScheduledExecutorService scheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor();
        main.getDevice().setExecutor(executorService);
        main.getDevice().setScheduledExecutor(scheduledExecutorService);

        main.setInformationModel(InformationModel.StudyRoot, IVR_LE_FIRST,
                EnumSet.noneOf(QueryOption.class));

        main.getKeys().addAll(queryatts);
        
        long t1 = System.currentTimeMillis();
        
        try {
            
            main.open();
            main.query(getDimseRSPHandler(main.getAssociation().nextMessageID()));
            
        } finally {
            main.close(); //is waiting for all the responsens to be complete
            executorService.shutdown();
            scheduledExecutorService.shutdown();
            
        }
        
        long t2 = System.currentTimeMillis();
        
        
        //format printout
        System.out.format(QueryTestSuite.RESULT_FORMAT,
                ++QueryTestSuite.testNumber,
                StringUtils.truncate(testDescription, 38),  
                this.expectedResult,
                numMatches,
                (t2 - t1) + " ms");
        
        if (this.expectedResult >= 0)
            assertTrue(numMatches == this.expectedResult);
        
        if (this.expectedValues!=null)
            for (String expectedValue : expectedValues)
                assertTrue("tag["+ ElementDictionary.keywordOf(returnTag,null) +"] not returned expected value:" + expectedValue,
                        returnedValues.contains(expectedValue));

        return numMatches;
    }
    
    public void addTag(int tag, String value) throws Exception
    {
        VR vr = ElementDictionary.vrOf(tag, null);
        queryatts.setString(tag, vr, value);
    }   
    
    public void setReturnTag(int tag) throws Exception
    {
        VR vr = ElementDictionary.vrOf(tag, null);
        queryatts.setNull(tag, vr);
        returnTag = tag;
    } 

    public void setExpectedResultsNumeber(int expectedResult) {
        this.expectedResult = expectedResult;
    }
    
    public void addExpectedResult(String value) {
        
        if (this.expectedValues == null)
            this.expectedValues = new ArrayList<String>();
        
        this.expectedValues.add(value);
    }

    private DimseRSPHandler getDimseRSPHandler(int messageID) {
        
        DimseRSPHandler rspHandler = new DimseRSPHandler(messageID) {

            @Override
            public void onDimseRSP(Association as, Attributes cmd,
                    Attributes data) {
                super.onDimseRSP(as, cmd, data);
                int status = cmd.getInt(Tag.Status, -1);
                if (Status.isPending(status)) {
                    if (returnTag!=null) {
                        
                        String returnedValue = data.getString(returnTag);
                        if (!returnedValues.contains(returnedValue))
                            returnedValues.add(returnedValue);
                        
                        //System.out.println(returnedValue);
                    }
                    ++numMatches;
                }
            }
        };

        return rspHandler;
    }
}
