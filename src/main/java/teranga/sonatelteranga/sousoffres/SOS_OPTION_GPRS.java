/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.sousoffres;

import com.oml.ded.kaabu.offre.Offre;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import teranga.sonatelteranga.charges.CM_FORFAIT_DATA;

/**
 *
 * @author sidibe0091
 */
public class SOS_OPTION_GPRS extends Offre {

    public SOS_OPTION_GPRS() {
        setOperateur("OSN");
        setCode("SOS_OPTION_GPRS");
        setDescription("Options GPRS");
        addCharge(new CM_FORFAIT_DATA("CM_FORFAIT_DATA", TYPE_CHARGE.RECURRENT));
    }
}
