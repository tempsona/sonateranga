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
import com.oml.ded.kaabu.conso.DetailConso;
import com.oml.ded.kaabu.jpacontroller.CompteurJpaController;
import com.oml.ded.kaabu.jpacontroller.exceptions.NonexistentEntityException;
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
public class SVC_M_MAIL extends Charge {

    public SVC_M_MAIL(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type.SERVICE, declencheur, true);
        setDescription("Service PushMail");
        setType(TYPE_CHARGE.RECURRENT);
        addCompteur(new Compteur("nb_rec", "Nombre de fois qu'est tombé l'abonnement depuis la dernière résiliation", 0));
    }

    @Override
    public List<Conso> getConso(Usages cdr) {
        return java.util.Collections.EMPTY_LIST;
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
        List<Conso> lc = new ArrayList<Conso>();

        CompteurJpaController ct = new CompteurJpaController();
        try {
            Compteur compteur = contrat.getCompteur("nb_rec");
            if (getDeclencheur().equals(TYPE_EVENT.RESILIATION)) {
                compteur.setDateChangement(new Date());
                compteur.setValeurCourante(compteur.getValeurInitiale());
                ct.edit(compteur);
            }
            if (getType().equals(TYPE_CHARGE.RECURRENT)) {
                Conso c = getNewConso();
                c.setCompte(contrat.getCompte());
                c.setOffre(contrat.getCodeOffre());
                c.setDateConso(new Date());
                c.addDetail(new DetailConso("agregat", "", TYPE_PARAMETRE.STRING, null, true, "FF25", null));

                if (compteur.getValeurCourante() == 0) {
                    c.addDetail(new DetailConso("fact_isol", "", TYPE_PARAMETRE.STRING, null, true, "Y", null));
                } else {
                    c.addDetail(new DetailConso("fact_isol", "", TYPE_PARAMETRE.STRING, null, true, "N", null));
                }

                lc.add(c);

                compteur.setDateChangement(new Date());
                compteur.setValeurCourante(compteur.getValeurCourante() + 1);
                ct.edit(compteur);
            }
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(SVC_M_MAIL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SVC_M_MAIL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }
}
