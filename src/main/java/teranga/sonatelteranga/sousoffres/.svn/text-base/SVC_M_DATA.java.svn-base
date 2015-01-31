/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.sousoffres;

import com.oml.ded.kaabu.offre.Offre;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import teranga.sonatelteranga.charges.CM_IPHONE_REMISE_GEN;
import teranga.sonatelteranga.charges.SVC_M_3G;
import teranga.sonatelteranga.charges.SVC_M_BEW;
import teranga.sonatelteranga.charges.SVC_M_BLBE;
import teranga.sonatelteranga.charges.SVC_M_GPRS;
import teranga.sonatelteranga.charges.SVC_M_IPHONE;
import teranga.sonatelteranga.charges.SVC_M_MAIL;
import teranga.sonatelteranga.charges.SVC_M_SMS_MOMT;
import teranga.sonatelteranga.charges.SVC_M_VPN;

/**
 *
 * @author sidibe0091
 */
public class SVC_M_DATA extends Offre {

    public SVC_M_DATA() {
        setOperateur("OSN");
        setCode("SVC_M_DATA");
        setProvisioning(true);
        setDescription("Services Data");
        addCharge(new SVC_M_SMS_MOMT("SVC_M_SMS_MOMT", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_GPRS("SVC_M_GPRS", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_3G("SVC_M_3G", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_VPN("SVC_M_VPN", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_MAIL("SVC_M_MAIL", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_BLBE("SVC_M_BLBE", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_BEW("SVC_M_BEW", TYPE_CHARGE.RECURRENT, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new SVC_M_IPHONE("SVC_M_IPHONE", TYPE_CHARGE.DISCOUNT, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new CM_IPHONE_REMISE_GEN("CM_IPHONE_REMISE_GEN", TYPE_CHARGE.RECURRENT, TYPE_EVENT.SOUSCRIPTION, false));
    }
}
