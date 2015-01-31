/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.sousoffres;


import com.oml.ded.kaabu.offre.Offre;
import com.oml.ded.kaabu.orangepro.charges.CM_ROAMING;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import teranga.sonatelteranga.charges.CM_TERANGA_GPRS;
import teranga.sonatelteranga.charges.CM_TERANGA_MMS;
import teranga.sonatelteranga.charges.CM_TERANGA_SMS;
import teranga.sonatelteranga.charges.CM_TERANGA_VOIX;

/**
 *
 * @author sidibe0091
 */
public class SOS_TERANGA_USAGE extends Offre {

    public SOS_TERANGA_USAGE() {
        setCode("SOS_TERANGA_USAGE");
        setDescription("La sous offre usage");
        setOperateur("OSN");
        addCharge(new CM_TERANGA_VOIX("CM_TERANGA_VOIX", TYPE_CHARGE.USAGE));
        addCharge(new CM_TERANGA_SMS("CM_TERANGA_SMS", TYPE_CHARGE.USAGE, TYPE_EVENT.USAGE, false));
        addCharge(new CM_TERANGA_GPRS("CM_TERANGA_GPRS", TYPE_CHARGE.USAGE, TYPE_EVENT.USAGE, false));
        addCharge(new CM_TERANGA_MMS("CM_TERANGA_MMS", TYPE_CHARGE.USAGE, TYPE_EVENT.USAGE, false));
        addCharge(new CM_ROAMING("CM_ROAMING", TYPE_CHARGE.USAGE, TYPE_EVENT.USAGE, false));
    }
}
