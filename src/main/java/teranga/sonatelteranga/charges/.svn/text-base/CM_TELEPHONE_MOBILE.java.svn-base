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
public class CM_TELEPHONE_MOBILE extends Charge {

    public CM_TELEPHONE_MOBILE(String string, TYPE_CHARGE tYPE_CHARGE, TYPE_EVENT tYPE_EVENT, boolean SERVICE) {
        super(string, tYPE_CHARGE, tYPE_EVENT, SERVICE);
        setDescription("Téléphone Mobile");
        addParametre(new ChargeParametre(this.getCode(), "IMEI", "Numero IMEI", TYPE_PARAMETRE.STRING, null, true, "X", null, "EXTERNE"));
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
            c.addDetail(new DetailConso("telephone.imei", "IMEI du téléphone", TYPE_PARAMETRE.STRING, null, true, contrat.getInstanceOffre().getValeurParametre(getParametres(), "IMEI").toString(), null));
            lc.add(c);
        } catch (InstantiationException ex) {
            Logger.getLogger(CM_TELEPHONE_MOBILE.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CM_TELEPHONE_MOBILE.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }
}
