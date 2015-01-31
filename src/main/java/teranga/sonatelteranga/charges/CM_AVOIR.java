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
 * @author Administrateur
 */
public class CM_AVOIR extends Charge {

    public CM_AVOIR(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);

        setDescription("Avoir");

        addParametre(new ChargeParametre(this.getCode(), "CODE_FO", "Famille d'offre", TYPE_PARAMETRE.STRING, null, true, "BA", null, "INTERNE"));        
    }

    @Override
    public List<Conso> getConso(Usages arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Conso> getConso(Contrat arg0) {
        List<Conso> lc = new ArrayList<Conso>();
        try {
            Conso c = getNewConso();
            c.setDateConso(new Date());
            c.setCompte(arg0.getCompte());
            c.setOffre(arg0.getCodeOffre());            
            lc.add(c);
        } catch (Exception ex) {
            Logger.getLogger(CM_AVOIR.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }
}
