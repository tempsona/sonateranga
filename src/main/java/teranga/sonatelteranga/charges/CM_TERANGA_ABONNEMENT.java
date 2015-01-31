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
import com.oml.ded.kaabu.util.ClientUtil;
import com.oml.ded.kaabu.util.Util.ETAT;
import com.oml.ded.kaabu.util.Util.TAXE_MODE;

import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import com.oml.ded.kaabu.util.Util.TYPE_PARAMETRE;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import table.decision.beans.CritereEgalite;
import table.decision.beans.ValeurAttributEntree;
import table.decision.controleurs.ControleurTableDecision;
import table.decision.entites.TableDecisionLigne;

/**
 *
 * @author sidibe0091
 */
public class CM_TERANGA_ABONNEMENT extends Charge {

    public CM_TERANGA_ABONNEMENT(String code, TYPE_CHARGE type) {
        super(code, type, TYPE_EVENT.SOUSCRIPTION, false);
        setDescription("Abonnement Teranga");
        setTaxeMode(TAXE_MODE.TVA);
        setType(TYPE_CHARGE.RECURRENT);
        setPeriodique(true);
        setEtat(ETAT.ACTIF);
        
        addParametre(new ChargeParametre(this.getCode(), "DUREE_ENGAGEMENT", "LIST=6;12;18;24", TYPE_PARAMETRE.NUMBER, "12"));
    }

    /**
     * Methode déclenchée par le collecteur pour générer les conso d'usage
     *
     * @param cdr
     * @return
     */
    @Override
    public List<Conso> getConso(Usages cdr) {
        return new ArrayList<Conso>();
    }
    /*
     * Methode appelée par l'engineServer pour générer les conso de type recurrent et exceptionnel
     */

    @Override
    public List<Conso> getConso(Contrat contrat) {
        List<Conso> l = new ArrayList<Conso>();
        if (getValeurParametre("NATURE_SOUSCRIPTION").equals("MIGRATION")) {
            //gratuit
        } else {
            Conso c = getNewConso();
            c.setDateConso(new Date());
            c.setPayeur(contrat.getCompte().getCode());
                        
            int valeurParametre = Integer.parseInt(getValeurParametre("DUREE_ENGAGEMENT").toString());            
            TableDecisionLigne tarifRemiseEngagementParLigne = getTarifRemiseEngagementParLigne(valeurParametre);
            c.setMontantHT(Double.parseDouble(tarifRemiseEngagementParLigne.getValeurSortie("TARIF").toString()));
            c.setMontantTTC(c.getMontantHT() * (1 + getTVA()));
            c.setAgregat(tarifRemiseEngagementParLigne.getValeurSortie("AGREGAT").toString());
            l.add(c);
        }
        return l;
    }

    private TableDecisionLigne getTarifRemiseEngagementParLigne(int duree) {
        System.out.println("************************ getTarifAbonnementCautionParLigne ");
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("CODE_SERVICE", getCode(), CritereEgalite.EGAL));
        attributEntrees.add(new ValeurAttributEntree("CODE_OFFRE", getOffre().getOffreParent().getCode(), CritereEgalite.EGAL));
        attributEntrees.add(new ValeurAttributEntree("DUREE", duree+"", CritereEgalite.EGAL));
        TableDecisionLigne ligne = ControleurTableDecision.getInstance().getTableDecisionLigne("TD_REMISE_ENGAGEMENT_ABO_PROMO", attributEntrees, new Date());
        if (ligne != null) {
            return ligne;
        } else {
            ligne = ControleurTableDecision.getInstance().getTableDecisionLigne("TD_REMISE_ENGAGEMENT_ABO", attributEntrees, new Date());
            return ligne;

        }
    }
}
