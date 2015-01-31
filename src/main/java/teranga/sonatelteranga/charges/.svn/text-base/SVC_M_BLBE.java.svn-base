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
import com.oml.ded.kaabu.parametre.ChargeParametre;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import com.oml.ded.kaabu.util.Util.TYPE_PARAMETRE;
import com.oml.ded.kaabu.util.Utilitaire;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sidibe0091
 */
public class SVC_M_BLBE extends Charge {

    public SVC_M_BLBE(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, false);
        setDescription("Forfait Blackberry");
        setPeriodique(true);
        ChargeParametre cp = new ChargeParametre(this.getCode(), "LISTFORFAITS", "LIST=100000-100Mo;300000-200Mo;1000000-1Go", TYPE_PARAMETRE.STRING, null, true, "10000", null, "EXTERNE");
        cp.setLibelle("Forfait");
        addParametre(cp);
        ChargeParametre chargeParametre = new ChargeParametre(code, "TYPE", "Type", TYPE_PARAMETRE.STRING, null, false, "BLBE", null, "EXTERNE", false);
        chargeParametre.setAffichable(false);
        addParametre(chargeParametre);
        ChargeParametre chargeParametre5 = new ChargeParametre(code, "APN", "Genere APN", TYPE_PARAMETRE.STRING, null, false, "1", null, "EXTERNE", false);
        chargeParametre5.setAffichable(false);
        addParametre(chargeParametre5);
        
        addCompteur(new Compteur("forfait_bb", "Forfait blackberry", 1024000));
    }

    @Override
    public List<Conso> getConso(Usages cdr) {
        return java.util.Collections.EMPTY_LIST;
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
        List<Conso> lc = new ArrayList<Conso>();
        Compteur cp = contrat.getCompteur("forfait_bb");
        Conso conso = getNewConso();
        conso.setCompte(contrat.getCompte());
        lc.add(conso);
        Object object = getValeurParametre("LISTFORFAITS");
        double valeur = 0;
        if (object != null) {
            try {
                valeur = Double.parseDouble(object.toString());
            } catch (Throwable e) {
//                System.out.println(Utilitaire.getStackTrace(e));
            }
        }
        cp.setValeurInitiale(valeur);
        cp.setValeurCourante(valeur);
        return lc;
    }
}
