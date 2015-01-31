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
public class SVC_M_BDATA extends Charge {

    public SVC_M_BDATA(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
        setDescription("Basic Data");
   //        addParametre(new ChargeParametre(this.getCode(), "MSISDN_D", "Numéro secondaire pour service BasicDATA, uniquement si le service BasicDATA utilise un uméro différent du numéro principal", TYPE_PARAMETRE.STRING, null, true, "X", null, "EXTERNE"));
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
            lc.add(c);
        } catch (Exception ex) {
            Logger.getLogger(SVC_M_BDATA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }
}
