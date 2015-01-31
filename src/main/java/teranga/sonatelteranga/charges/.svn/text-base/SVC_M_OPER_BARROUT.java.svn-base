/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.charges;

import com.oml.ded.kaabu.charge.Charge;
import com.oml.ded.kaabu.client.Contrat;
import com.oml.ded.kaabu.collecte.Usages;
import com.oml.ded.kaabu.conso.Conso;
import com.oml.ded.kaabu.parametre.ChargeParametre;
import com.oml.ded.kaabu.util.Util.ETAT;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import com.oml.ded.kaabu.util.Util.TYPE_PARAMETRE;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sidibe0091
 */
public class SVC_M_OPER_BARROUT extends Charge {

    public SVC_M_OPER_BARROUT(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
        setDescription("Restriction opérateur appels sortants");
        ChargeParametre cp = new ChargeParametre(this.getCode(), "OPER_OCALLS", "LIST=ODBOIC-Suspendre les appels internationaux sortants;ODBOC-Suspendre les appels sortants;DEL-Activer International", TYPE_PARAMETRE.STRING, null, true, "ODBOIC", null, "EXTERNE");
        cp.setLibelle("Restriction");
        addParametre(cp);
        setEtat(ETAT.ACTIF);
        setObligatoire(true);
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
            for (ChargeParametre cp : getParametres()) {
                if (cp.getNom().equals("OPER_OCALLS") && cp.getNewValue().toString().startsWith("DEL")) {
                    c.setTVA(0); // pas de TVA
                    c.setMontantTTC(c.getMontantHT());
                    lc.add(c);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(SVC_M_OPER_BARROUT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }
}
