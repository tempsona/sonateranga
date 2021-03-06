/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.sousoffres;


import com.oml.ded.kaabu.offre.Offre;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import teranga.sonatelteranga.charges.CM_TERANGA_ABONNEMENT;
import teranga.sonatelteranga.charges.CM_TERANGA_CAUTION;
import teranga.sonatelteranga.charges.CM_TERANGA_MISE_EN_SERVICE;
import teranga.sonatelteranga.charges.CM_TERANGA_RESILIATION;

/**
 *
 * @author sidibe0091
 */
public class SOS_TERANGA_ABONNEMENT extends Offre {

    public SOS_TERANGA_ABONNEMENT() {        
        setOperateur("OSN");
        setCode("SOS_TERANGA_ABONNEMENT");
        setDescription("Abonnement Principale Teranga");        
        
        addCharge(new CM_TERANGA_MISE_EN_SERVICE("CM_TERANGA_MISE_EN_SERVICE", TYPE_CHARGE.EXCEPTIONNEL));    
        addCharge(new CM_TERANGA_ABONNEMENT("CM_TERANGA_ABONNEMENT", TYPE_CHARGE.RECURRENT));
        addCharge(new CM_TERANGA_RESILIATION("CM_TERANGA_RESILIATION", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.RESILIATION, true));                   
        addCharge(new CM_TERANGA_CAUTION("CM_TERANGA_CAUTION", TYPE_CHARGE.EXCEPTIONNEL));
       
    }
}
