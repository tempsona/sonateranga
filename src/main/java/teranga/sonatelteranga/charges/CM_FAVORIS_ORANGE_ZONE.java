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
import com.oml.ded.kaabu.util.Util.TAXE_MODE;
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
public class CM_FAVORIS_ORANGE_ZONE extends Charge {

    public CM_FAVORIS_ORANGE_ZONE(String code, TYPE_CHARGE type) {
        super(code, type, TYPE_EVENT.SOUSCRIPTION, false);
        setDescription("Numéro Préféré National");
        setTaxeMode(TAXE_MODE.TVA);
        addParametre(new ChargeParametre(code, "NUMERO_PREFERE_1", "Numéro préféré 1", TYPE_PARAMETRE.STRING, null, false, "X", null, "EXTERNE"));
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
            lc.add(c);
        } catch (Exception ex) {
            Logger.getLogger(CM_FAVORIS_ORANGE_ZONE.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }
}
