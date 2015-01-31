/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.sousoffres;

import com.oml.ded.kaabu.offre.Offre;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import teranga.sonatelteranga.charges.CM_AVOIR;
import teranga.sonatelteranga.charges.CM_FRAIS_EXCEPTIONNEL;
import teranga.sonatelteranga.charges.CM_GESTE_COMMERCIAL;
import teranga.sonatelteranga.charges.CM_PRODUIT_LIBRE;
import teranga.sonatelteranga.charges.CM_PRODUIT_LIBRE_RECURRENT;

/**
 *
 * @author Administrateur
 */
public class SOS_XOCC extends Offre {

    public SOS_XOCC() {
        setOperateur("OSN");
        setCode("SOS_XOCC");
        setDescription("Autres charges et crédits");
        addCharge(new CM_GESTE_COMMERCIAL("CM_GESTE_COMMERCIAL", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, false));
        addCharge(new CM_FRAIS_EXCEPTIONNEL("CM_FRAIS_EXCEPTIONNEL", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, false));
        addCharge(new CM_AVOIR("CM_AVOIR", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, false));
        addCharge(new CM_PRODUIT_LIBRE("CM_PRODUIT_LIBRE", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, false));
        addCharge(new CM_PRODUIT_LIBRE_RECURRENT("CM_PRODUIT_LIBRE_RECURRENT", TYPE_CHARGE.RECURRENT, TYPE_EVENT.SOUSCRIPTION, false));
    }
}
