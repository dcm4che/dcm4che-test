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

package org.dcm4che.test.integration.store;

import org.dcm4che.test.annotations.RemoteConnectionParameters;
import org.dcm4che.test.annotations.StoreParameters;
import org.dcm4che3.tool.storescu.test.StoreResult;
import org.dcm4che3.tool.storescu.test.StoreTool;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che.test.common.TestToolFactory;
import org.dcm4che.test.common.TestToolFactory.TestToolType;
import org.junit.Test;

/**
 * @author Umberto Cappellini <umberto.cappellini@agfa.com>
 * @author Hesham Elbadawi <bsdreko@gmail.com>
 */
public class Store_MESA_CR extends BasicTest{

    @Test
    public void Store_MESA_CR_CR1_CRS1() throws Exception {
        StoreTool storeTool = (StoreTool) TestToolFactory.createToolForTest(TestToolType.StoreTool, this);
        storeTool.store(
                "MESA_12_5,CR1,S1", "modality/CR/CR1/CR1S1");
        StoreResult results = (StoreResult) storeTool.getResult();
        StoreTestSuite.printResults(results);
    }

    @Test
    public void Store_MESA_CR_CR2_CRS1() throws Exception {
        StoreTool storeTool = (StoreTool) TestToolFactory.createToolForTest(TestToolType.StoreTool, this);
        storeTool.store(
                "MESA_12_5,CR2,S1", "modality/CR/CR2/CR2S1");
        StoreResult results = (StoreResult) storeTool.getResult();
        StoreTestSuite.printResults(results);
    }

    @Test
    public void Store_MESA_CR_CR3_CRS1() throws Exception {
        StoreTool storeTool = (StoreTool) TestToolFactory.createToolForTest(TestToolType.StoreTool, this);
        storeTool.store(
                "MESA_12_5,CR3,S1", "modality/CR/CR3/CR3S1");
        StoreResult results = (StoreResult) storeTool.getResult();
        StoreTestSuite.printResults(results);
    }

    @Test
    public void Store_MESA_CR_CR4_CRS1() throws Exception {
        StoreTool storeTool = (StoreTool) TestToolFactory.createToolForTest(TestToolType.StoreTool, this);
        storeTool.store(
                "MESA_12_5,CR4,S1", "modality/CR/CR4/CR4S1");
        StoreResult results = (StoreResult) storeTool.getResult();
        StoreTestSuite.printResults(results);
    }
}
