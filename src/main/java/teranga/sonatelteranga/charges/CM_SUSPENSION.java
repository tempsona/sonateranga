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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sidibe0091
 */
public class CM_SUSPENSION extends Charge {

    public CM_SUSPENSION(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
        setDescription("Frais de Suspension");
        addParametre(new ChargeParametre(this.getCode(), "DUREE_SUSPENSION", "Durée de la suspension", TYPE_PARAMETRE.NUMBER, 3, true, null, null, "EXTERNE"));
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
            c.setDateConso(new Date());
            c.setCompte(contrat.getCompte());
            lc.add(c);
        } catch (Exception ex) {
            Logger.getLogger(CM_SUSPENSION.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }
}
