/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.charges;

import com.oml.ded.kaabu.charge.Charge;
import com.oml.ded.kaabu.client.Contrat;
import com.oml.ded.kaabu.collecte.Usages;
import com.oml.ded.kaabu.conso.Conso;
import com.oml.ded.kaabu.util.Util.ETAT;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sidibe0091
 */
public class SVC_M_CWS extends Charge {

    public SVC_M_CWS(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
         setDescription("Signal d'appel");
         setObligatoire(true);
         setEtat(ETAT.ACTIF);
       // addParametre(new ChargeParametre(this.getCode(), "CODE_FO", "Famille d'offre", TYPE_PARAMETRE.STRING, null, true, "PRO", null,"INTERNE"));
    }

    @Override
    public List<Conso> getConso(Usages cdr) {
        return java.util.Collections.EMPTY_LIST;
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
        List<Conso> lc = new ArrayList<Conso>();
        try {
            Conso c = getNewConso();
            c.setCompte(contrat.getCompte());
            c.setOffre(contrat.getCodeOffre());
            lc.add(c);
        } catch (Exception ex) {
            Logger.getLogger(SVC_M_CWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }
}
