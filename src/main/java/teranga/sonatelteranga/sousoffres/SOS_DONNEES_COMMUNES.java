/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.sousoffres;

import com.oml.ded.kaabu.offre.Offre;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import teranga.sonatelteranga.charges.CM_CARTE_SIM;
import teranga.sonatelteranga.charges.CM_NUMERO_TELEPHONE;
import teranga.sonatelteranga.charges.CM_TELEPHONE_MOBILE;

/**
 *
 * @author sidibe0091
 */
public class SOS_DONNEES_COMMUNES extends Offre {

    public SOS_DONNEES_COMMUNES() {        
        setOperateur("OSN");
        setCode("SOS_DONNEES_COMMUNES");
        setProvisioning(true);
        setDescription("Données Communes");
        addCharge(new CM_NUMERO_TELEPHONE("CM_NUMERO_TELEPHONE", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, true));
        addCharge(new CM_CARTE_SIM("CM_CARTE_SIM", TYPE_CHARGE.SERVICE));
        addCharge(new CM_TELEPHONE_MOBILE("CM_TELEPHONE_MOBILE", TYPE_CHARGE.SERVICE, TYPE_EVENT.SOUSCRIPTION, false));


    }
}
