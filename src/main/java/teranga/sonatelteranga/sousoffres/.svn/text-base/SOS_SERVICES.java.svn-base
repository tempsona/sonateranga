/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.sousoffres;


import com.oml.ded.kaabu.offre.Offre;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import teranga.sonatelteranga.charges.CM_RECONDUCTION;
import teranga.sonatelteranga.charges.CM_SIM_DUO;
import teranga.sonatelteranga.charges.CM_SUSPENSION;

/**
 *
 * @author sidibe0091
 */
public class SOS_SERVICES extends Offre {

    public SOS_SERVICES() {
        setOperateur("OSN");
        setCode("SOS_SERVICES");
        setDescription("Services à la demande");
        addCharge(new CM_SIM_DUO("CM_SIM_DUO", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, false));
        addCharge(new CM_RECONDUCTION("CM_RECONDUCTION", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, false));
        addCharge(new CM_SUSPENSION("CM_SUSPENSION", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, false));
    }
}
