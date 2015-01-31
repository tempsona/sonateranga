/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.charges;

import com.oml.ded.kaabu.charge.Charge;
import com.oml.ded.kaabu.client.Contrat;
import com.oml.ded.kaabu.collecte.Usages;
import com.oml.ded.kaabu.compteur.Compteur;
import com.oml.ded.kaabu.conso.Conso;
import com.oml.ded.kaabu.parametre.ChargeParametre;
import com.oml.ded.kaabu.util.Util;
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
public class CM_PRODUIT_LIBRE_RECURRENT extends Charge {

    public CM_PRODUIT_LIBRE_RECURRENT(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
        setDescription("Produit libre récurrent");

        addParametre(new ChargeParametre(this.getCode(), "NB_RECURRENCES", "Nombre de mois", TYPE_PARAMETRE.NUMBER, 0, true, "", null, "EXTERNE"));
        addParametre(new ChargeParametre(this.getCode(), "CODE_FO", "Famille d'offre", TYPE_PARAMETRE.STRING, null, true, "BA", null, "INTERNE"));
        addCompteur(new Compteur("cpt_occurences", "Compte pour le nombre d'occurences", 0));
    }

    @Override
    public List<Conso> getConso(Usages arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Conso> getConso(Contrat arg0) {
        List<Conso> lc = new ArrayList<Conso>();

        try {
            Compteur compteur = getCompteur("cpt_occurences");

            if (getDeclencheur().equals(TYPE_EVENT.SOUSCRIPTION)) {
                compteur.setDateChangement(new Date());
                Util.initCompteur(compteur, 0);
            }

            if(getType().equals(TYPE_CHARGE.RECURRENT)){
                if (compteur.getValeurCourante() < Integer.parseInt(getValeurParametre("NB_RECURRENCES").toString())) {
                    Conso c = getNewConso();
                    c.setCompte(arg0.getCompte());
                    c.setOffre(arg0.getCodeOffre());
                    c.setDateConso(new Date());
                    lc.add(c);

                    compteur.setDateChangement(new Date());
                    Util.incrementCompteur(compteur, 1);
                } else if (compteur.getValeurCourante() > Integer.parseInt(getValeurParametre("NB_RECURRENCES").toString())) {
                    //gratuit
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(CM_PRODUIT_LIBRE_RECURRENT.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }
}
