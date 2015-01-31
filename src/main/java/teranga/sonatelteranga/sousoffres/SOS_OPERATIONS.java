/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.sousoffres;

import com.oml.ded.kaabu.offre.Offre;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import teranga.sonatelteranga.charges.CM_CESSION;
import teranga.sonatelteranga.charges.CM_CHANGEMENT_NUMERO;
import teranga.sonatelteranga.charges.CM_CHANGEMENT_SIM;
import teranga.sonatelteranga.charges.CM_CHANGEMENT_SIM_MICRO;
import teranga.sonatelteranga.charges.CM_CHANGEMENT_SYMP;
import teranga.sonatelteranga.charges.CM_FAVORIS_INT_CHANGEMENT;
import teranga.sonatelteranga.charges.CM_FAVORIS_NAT_CHANGEMENT;
import teranga.sonatelteranga.charges.CM_FAVORIS_ORANGE_ZONE_CHANGEMENT;

/**
 *
 * @author sidibe0091
 */
public class SOS_OPERATIONS extends Offre {

    public SOS_OPERATIONS() {
        setOperateur("OSN");
        setCode("SOS_OPERATIONS");
        setDescription("Opérations de changement");      
        addCharge(new CM_CHANGEMENT_NUMERO("CM_CHANGEMENT_NUMERO", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new CM_FAVORIS_NAT_CHANGEMENT("CM_FAVORIS_NAT_CHANGEMENT", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new CM_FAVORIS_INT_CHANGEMENT("CM_FAVORIS_INT_CHANGEMENT", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new CM_FAVORIS_ORANGE_ZONE_CHANGEMENT("CM_FAVORIS_ORANGE_ZONE_CHANGEMENT", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new CM_CHANGEMENT_SYMP("CM_CHANGEMENT_SYMP", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new CM_CHANGEMENT_SIM("CM_CHANGEMENT_SIM", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new CM_CHANGEMENT_SIM_MICRO("CM_CHANGEMENT_SIM_MICRO", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new CM_CESSION("CM_CESSION", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, true));
    }
}
