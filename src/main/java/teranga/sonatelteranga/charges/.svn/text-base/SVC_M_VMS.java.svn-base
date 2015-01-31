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
public class SVC_M_VMS extends Charge {

    public SVC_M_VMS(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
        setEtat(ETAT.INACTIF);
        setDescription("Messagerie vocale");
        setObligatoire(true);
        ChargeParametre cp = new ChargeParametre(code, "VMSID", "LIST=1-VMSC 1;2-VMSC 2", TYPE_PARAMETRE.NUMBER, 1, aProvisionner, "1", null, "EXTERNE");
        cp.setLibelle("VMSC");
        addParametre(cp);
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
            Logger.getLogger(SVC_M_VMS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }
}
