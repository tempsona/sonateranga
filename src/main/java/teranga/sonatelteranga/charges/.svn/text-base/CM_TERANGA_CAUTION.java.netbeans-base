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
import com.oml.ded.kaabu.util.OffreUtil;
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
 * Charge de la caution d'accès national
 * @author sidibe0091
 */
public class CM_TERANGA_CAUTION extends Charge {

    public CM_TERANGA_CAUTION(String code, TYPE_CHARGE type) {
        super(code, type, TYPE_EVENT.SOUSCRIPTION, false);
        setDescription("Caution d'accès national");
        setTaxeMode(TAXE_MODE.TVA);
        setEtat(ETAT.INACTIF);
        ChargeParametre cp = new ChargeParametre(this.getCode(), "ANNUL_CAUTION", "LIST=NON-NON;OUI-OUI", TYPE_PARAMETRE.STRING, null, true, "NON", null, "EXTERNE");
        cp.setLibelle("Annulation Caution");
        addParametre(new ChargeParametre(this.getCode(), "NATURE_SOUSCRIPTION", "LIST=SOUSCRIPTION;MIGRATION", TYPE_PARAMETRE.STRING, null, true, "SOUSCRIPTION", null, "INTERNE"));
    }

    @Override
    public List<Conso> getConso(Usages cdr) {
        return new ArrayList<Conso>();
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
        List<Conso> l = new ArrayList<Conso>();
        if (getValeurParametre("NATURE_SOUSCRIPTION").equals("MIGRATION")) {
            //gratuit
        } else { // Frais de souscription
            try {
                // pour les parametres de la charges (A remplacer par une methode getParametre(nom)
                Conso c = getNewConso();
                c.setDateConso(new Date());
                c.setFacture(null);
                c.setPayeur(contrat.getCompte().getCode());
                c.setTVA(0);// Pas de TVA
                c.setMontantTTC(c.getMontantHT());
                l.add(c);
            } catch (Exception ex) {
                Logger.getLogger(CM_TERANGA_CAUTION.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return l;
    }
}
