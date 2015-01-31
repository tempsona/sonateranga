/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.charges;

import com.oml.ded.kaabu.charge.Charge;
import com.oml.ded.kaabu.client.Contrat;
import com.oml.ded.kaabu.collecte.Usages;
import com.oml.ded.kaabu.compteur.Compteur;
import com.oml.ded.kaabu.conso.Conso;
import com.oml.ded.kaabu.conso.DetailConso;
import com.oml.ded.kaabu.util.Util;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import com.oml.ded.kaabu.util.Util.TYPE_PARAMETRE;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author sidibe0091
 */
public class SVC_M_BEW extends Charge {

    public SVC_M_BEW(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
        setDescription("Redevance Business everywhere");
        addCompteur(new Compteur("nbr_bew", "Nombre de fois qu'est tombé l'abonnement depuis la dernière résiliation", 0));
    }

    @Override
    public List<Conso> getConso(Usages cdr) {
        return java.util.Collections.EMPTY_LIST;
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
        List<Conso> lc = new ArrayList<Conso>();
        Conso c = getNewConso();
        c.setCompte(contrat.getCompte());
        c.setOffre(contrat.getCodeOffre());
        c.setDateConso(new Date());
        c.addDetail(new DetailConso("agregat", "", TYPE_PARAMETRE.STRING, null, true, "FF25", null));
        c.addDetail(new DetailConso("fact_isol", "", TYPE_PARAMETRE.STRING, null, true, "Y", null));
        lc.add(c);
        Util.incrementCompteur(getCompteur("nbr_bew"), 1);

        return lc;
    }
}
