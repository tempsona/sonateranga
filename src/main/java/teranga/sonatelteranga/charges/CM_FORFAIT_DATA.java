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
import com.oml.ded.kaabu.parametre.ChargeParametre;
import com.oml.ded.kaabu.util.Util;
import com.oml.ded.kaabu.util.Util.TAXE_MODE;

import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import com.oml.ded.kaabu.util.Util.TYPE_PARAMETRE;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import table.decision.beans.CritereEgalite;
import table.decision.beans.ValeurAttributEntree;
import table.decision.controleurs.ControleurTableDecision;
import table.decision.entites.TableDecisionLigne;

/**
 *
 * @author sidibe0091
 */
public class CM_FORFAIT_DATA extends Charge {

    public CM_FORFAIT_DATA(String code, TYPE_CHARGE type) {
        super(code, type, TYPE_EVENT.SOUSCRIPTION, false);
        setDescription("Forfait Data");
        setTaxeMode(TAXE_MODE.TVA);
        setType(TYPE_CHARGE.RECURRENT);
        setPeriodique(true);
        ChargeParametre cp = new ChargeParametre(this.getCode(), "FORFAIT", "LIST=100M-100Mo;300M-300Mo;500M-500Mo;1Go-1Go;2Go-2Go", TYPE_PARAMETRE.STRING, null, false, "100M", null, "EXTERNE");
        cp.setLibelle("Forfait Data");
        addParametre(cp);
        /**
         * Compteurs
         */
        addCompteur(new Compteur("forfait_data", "Forfait Data ", 0));
    }

    /**
     * Methode dï¿½clenchï¿½e par le collecteur pour gï¿½nï¿½rer les conso
     * d'usage
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
        List<Conso> lc = new ArrayList<Conso>();
        Conso c = getNewConso();
        Map mapTarif = getTarifExceptionnelForfait(getValeurParametre("FORFAIT") + "");
        double tarif = (Double) mapTarif.get("tarif");
        c.setMontantHT(Util.arrondiStandard(tarif));
        c.setMontantTTC(Util.arrondiStandard(tarif * (getTVA() + 1)));
        Compteur cp = getCompteur("forfait_data");
        cp.setValeurCourante(cp.getValeurCourante() + Util.floor(Util.arrondiSuperieur(Double.parseDouble(mapTarif.get("abondance").toString())), 6));
        c.setAgregat(mapTarif.get("agregat").toString());
        c.setLegend(mapTarif.get("libelle").toString());
        c.getDetails().removeAll(c.getDetails());
        c.addDetail(new DetailConso("libelle", "libelle facture", TYPE_PARAMETRE.STRING, null, true, mapTarif.get("libelle").toString(), null));
        c.setPayeur(contrat.getCompte().getCode());
        lc.add(c);

        return lc;
    }

    private Map getTarifExceptionnelForfait(String option) {
        Map<String, Object> sortie = new HashMap<String, Object>();
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("CODE_SERVICE", getCode(), CritereEgalite.EGAL));

        attributEntrees.add(new ValeurAttributEntree("TYPE_FORFAIT", option, CritereEgalite.EGAL));
        TableDecisionLigne tableDecisionLigne = ControleurTableDecision.getInstance().getTableDecisionLigne("TD_FORFAIT_TARIF_EXCEPTIONNELS", attributEntrees, new Date());
        sortie.put("description", tableDecisionLigne.getValeurSortie("DESCRIPTION"));
        sortie.put("tarif", tableDecisionLigne.getValeurSortie("TARIF"));
        sortie.put("agregat", tableDecisionLigne.getValeurSortie("AGREGAT"));
        sortie.put("libelle", tableDecisionLigne.getValeurSortie("LIBELLE"));
        sortie.put("abondance", tableDecisionLigne.getValeurSortie("ABONDANCE"));
        return sortie;
    }
}
