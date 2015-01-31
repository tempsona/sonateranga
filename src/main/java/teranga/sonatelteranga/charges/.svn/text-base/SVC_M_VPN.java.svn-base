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
public class SVC_M_VPN extends Charge {

    public SVC_M_VPN(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type.SERVICE, declencheur, true);
        setDescription("Service VPN");
        addParametre(new ChargeParametre(this.getCode(), "VPN_NAME", "Nom du VPN", TYPE_PARAMETRE.STRING, null, true, "", null, "EXTERNE", false));
        addParametre(new ChargeParametre(this.getCode(), "APN_VALUE", "Valeur du VPN", TYPE_PARAMETRE.STRING, null, true, "", null, "EXTERNE", true));
        addParametre(new ChargeParametre(this.getCode(), "OLD_APN_VALUE", "Ancienne Valeur Nom du VPN", TYPE_PARAMETRE.STRING, null, true, "", null, "EXTERNE", false));
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
            c.setDateConso(new Date());
            lc.add(c);
        } catch (Exception ex) {
            Logger.getLogger(SVC_M_VPN.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }
}
