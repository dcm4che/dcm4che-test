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
import org.dcm4che.test.annotations.QueryParameters;
import org.dcm4che.test.common.BasicTest;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Sequence;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.dcm4che3.tool.findscu.test.QueryResult;
import org.junit.Test;
/**
 * @author Hesham Elbadawi <bsdreko@gmail.com>
 *
 * Tests querying a structured report from the image manager
 *  using one of the structured report attributes on IMAGE level
 */
public class TestRAD44 extends BasicTest{
    
    @Test
    @QueryParameters(queryLevel="IMAGE")
    public void testQuerySR_Defaults_SetLevel() throws MissingArgumentException {
        Attributes attrs = new Attributes();
        Sequence seq = attrs.newSequence(Tag.ConceptNameCodeSequence, 1);
        Attributes itemAttrs = new Attributes();
        itemAttrs.setString(Tag.CodeMeaning, VR.LO, "LN");
        itemAttrs.setString(Tag.CodeValue, VR.SH, "18000-3");
        itemAttrs.setString(Tag.CodingSchemeDesignator, VR.SH, "SOME MAMMO PREPROCESSING RESULTS");
        seq.add(itemAttrs);
        QueryResult result = (QueryResult) query("Test Query SR - REQUIREMENT[DCMEEREQ-44] - "
                + "IHE - TRANSACTION[RAD-44]", attrs, false, 1);
    }
    
    //@Test
    //included for demonstrating purposes
    @QueryParameters(queryLevel="IMAGE",aeTitle="DCM4CHEE",connection="dicom",sourceAETitle="FINDSCU",sourceDevice="findscu")
    public void testQuerySR_CustomParams() throws MissingArgumentException {
        Attributes attrs = new Attributes();
        Sequence seq = attrs.newSequence(Tag.ConceptNameCodeSequence, 1);
        Attributes itemAttrs = new Attributes();
        itemAttrs.setString(Tag.CodeMeaning, VR.LO, "srtitle");
        itemAttrs.setString(Tag.CodeValue, VR.SH, "someval");
        itemAttrs.setString(Tag.CodingSchemeDesignator, VR.SH, "TST");
        seq.add(itemAttrs);
        QueryResult result = (QueryResult) query("Test Query SR - REQUIREMENT[DCMEEREQ-44] - "
                + "IHE - TRANSACTION[RAD-44]", attrs, false, 1);
    }
}
