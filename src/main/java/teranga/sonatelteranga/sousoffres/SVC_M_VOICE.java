/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.sousoffres;

import com.oml.ded.kaabu.offre.Offre;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import teranga.sonatelteranga.charges.CM_FAVORIS_INT_ABO;
import teranga.sonatelteranga.charges.CM_FAVORIS_NAT_ABO;
import teranga.sonatelteranga.charges.CM_FAVORIS_ORANGE_ZONE;
import teranga.sonatelteranga.charges.SVC_M_AOCI;
import teranga.sonatelteranga.charges.SVC_M_BDATA;
import teranga.sonatelteranga.charges.SVC_M_BFAX;
import teranga.sonatelteranga.charges.SVC_M_BTEL;
import teranga.sonatelteranga.charges.SVC_M_CFBUSY;
import teranga.sonatelteranga.charges.SVC_M_CFNREACH;
import teranga.sonatelteranga.charges.SVC_M_CFNREPLY;
import teranga.sonatelteranga.charges.SVC_M_CHS;
import teranga.sonatelteranga.charges.SVC_M_CLIP;
import teranga.sonatelteranga.charges.SVC_M_CLIR;
import teranga.sonatelteranga.charges.SVC_M_CRBT;
import teranga.sonatelteranga.charges.SVC_M_CTS;
import teranga.sonatelteranga.charges.SVC_M_CWS;
import teranga.sonatelteranga.charges.SVC_M_MPTY;
import teranga.sonatelteranga.charges.SVC_M_OPER_BARRIN;
import teranga.sonatelteranga.charges.SVC_M_OPER_BARROUT;
import teranga.sonatelteranga.charges.SVC_M_ROAMING;
import teranga.sonatelteranga.charges.SVC_M_USER_BAOC;
import teranga.sonatelteranga.charges.SVC_M_USER_BOICC;
import teranga.sonatelteranga.charges.SVC_M_VMS;

/**
 *
 * @author sidibe0091
 */
public class SVC_M_VOICE extends Offre {

    public SVC_M_VOICE() {
        setOperateur("OSN");
        setCode("SVC_M_VOICE");
        setProvisioning(true);
        setDescription("Services Voix");
        addCharge(new SVC_M_BDATA("SVC_M_BDATA", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_BFAX("SVC_M_BFAX", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_BTEL("SVC_M_BTEL", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_CFBUSY("SVC_M_CFBUSY", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_CFNREACH("SVC_M_CFNREACH", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_CFNREPLY("SVC_M_CFNREPLY", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_CHS("SVC_M_CHS", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_CLIP("SVC_M_CLIP", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_CLIR("SVC_M_CLIR", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_CRBT("SVC_M_CRBT", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_CTS("SVC_M_CTS", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_CWS("SVC_M_CWS", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_MPTY("SVC_M_MPTY", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_VMS("SVC_M_VMS", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_AOCI("SVC_M_AOCI", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_USER_BAOC("SVC_M_USER_BAOC", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_USER_BOICC("SVC_M_USER_BOICC", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_ROAMING("SVC_M_ROAMING", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_OPER_BARRIN("SVC_M_OPER_BARRIN", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_OPER_BARROUT("SVC_M_OPER_BARROUT", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new CM_FAVORIS_NAT_ABO("CM_FAVORIS_NAT_ABO", TYPE_CHARGE.EXCEPTIONNEL));
        addCharge(new CM_FAVORIS_INT_ABO("CM_FAVORIS_INT_ABO", TYPE_CHARGE.EXCEPTIONNEL));
        addCharge(new CM_FAVORIS_ORANGE_ZONE("CM_FAVORIS_ORANGE_ZONE", TYPE_CHARGE.EXCEPTIONNEL));
    }
}