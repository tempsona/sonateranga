/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.charges;

import com.oml.ded.kaabu.charge.Charge;
import com.oml.ded.kaabu.client.Contrat;
import com.oml.ded.kaabu.collecte.Usages;
import com.oml.ded.kaabu.conso.Conso;
import com.oml.ded.kaabu.conso.DetailConso;
import com.oml.ded.kaabu.parametre.ChargeParametre;
import com.oml.ded.kaabu.util.Util.ETAT;
import com.oml.ded.kaabu.util.Util.TAXE_MODE;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import com.oml.ded.kaabu.util.Util.TYPE_PARAMETRE;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sidibe0091
 */
public class CM_CARTE_SIM extends Charge {

    public CM_CARTE_SIM(String code, TYPE_CHARGE type) {
        super(code, type, TYPE_EVENT.SOUSCRIPTION, true, "Carte SIM");
        
        addParametre(new ChargeParametre(this.getCode(), "IMSI", "Numero imsi", TYPE_PARAMETRE.STRING, null, true, "X", null, "EXTERNE"));
        addParametre(new ChargeParametre(this.getCode(), "IMSI_KEY", "clé d'authentification", TYPE_PARAMETRE.STRING, null, true, "X", null, "EXTERNE"));
        addParametre(new ChargeParametre(this.getCode(), "PUK", "code puk", TYPE_PARAMETRE.STRING, null, true, "X", null, "EXTERNE"));
        addParametre(new ChargeParametre(this.getCode(), "PUK2", "code puk 2", TYPE_PARAMETRE.STRING, null, true, "X", null, "EXTERNE"));
        setEtat(ETAT.ACTIF);

        setTaxeMode(TAXE_MODE.TVA);
    }
    
    @Override
    public List<Conso> getConso(Usages cdr) {
        return java.util.Collections.EMPTY_LIST;
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
        List<Conso> lc = new ArrayList<Conso>();
        Conso c = getNewConso();
        try {
            c.setDateConso(new Date());
            c.setCompte(contrat.getCompte());
            c.setOffre(contrat.getCodeOffre());
            c.addDetail(new DetailConso("carte.sim", "Charge carte sim", TYPE_PARAMETRE.STRING, null, true, contrat.getInstanceOffre().getValeurParametre(getParametres(), "IMSI").toString(), null));
            lc.add(c);
        } catch (InstantiationException ex) {
            Logger.getLogger(CM_CARTE_SIM.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CM_CARTE_SIM.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }
}
