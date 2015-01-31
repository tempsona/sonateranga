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
public class SVC_M_CLIR extends Charge {

    public SVC_M_CLIR(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
        setPeriodique(true);
        setDescription("CLIR - Liste rouge");
        addParametre(new ChargeParametre(code, "ACTION", "LIST=SOUSCRIPTION;BILLING", TYPE_PARAMETRE.STRING, null, aProvisionner, "SOUSCRIPTION", null, "INTERNE"));
    }

    @Override
    public List<Conso> getConso(Usages cdr) {
        return java.util.Collections.EMPTY_LIST;
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
        List<Conso> lc = new ArrayList<Conso>();
        if (getValeurParametre("ACTION")!=null && getValeurParametre("ACTION").equals("SOUSCRIPTION")) {
        } else {
            try {
                Conso c = getNewConso();
                c.setCompte(contrat.getCompte());
                c.setOffre(contrat.getCodeOffre());
                lc.add(c);
            } catch (Exception ex) {
                Logger.getLogger(SVC_M_CLIR.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return lc;
    }
}
