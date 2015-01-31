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
import com.oml.ded.kaabu.jpacontroller.PontJPA;
import com.oml.ded.kaabu.jpacontroller.PoolConnexion;
import com.oml.ded.kaabu.parametre.ChargeParametre;
import com.oml.ded.kaabu.util.ClientUtil;
import com.oml.ded.kaabu.util.Util.ETAT;
import com.oml.ded.kaabu.util.Util.TAXE_MODE;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import com.oml.ded.kaabu.util.Util.TYPE_PARAMETRE;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import table.decision.beans.CritereEgalite;
import table.decision.beans.ValeurAttributEntree;
import table.decision.controleurs.ControleurTableDecision;
import table.decision.entites.TableDecisionLigne;

/**
 *
 * @author sidibe0091
 */
public class CM_TERANGA_RESILIATION extends Charge {

    public CM_TERANGA_RESILIATION(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
        setDescription("Frais de Résiliation");
        setTaxeMode(TAXE_MODE.TVA);
        setType(TYPE_CHARGE.EXCEPTIONNEL);
        setDeclencheur(TYPE_EVENT.RESILIATION);
        setEtat(ETAT.INACTIF);
        addParametre(new ChargeParametre(code, "NATURE_RESILIATION", "LIST=MIGRATION;SUSPENSION", TYPE_PARAMETRE.STRING, null, aProvisionner, "X", null, "EXTERNE"));
    }

    @Override
    public List<Conso> getConso(Usages cdr) {
        return java.util.Collections.EMPTY_LIST;
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
        List<Conso> lc = new ArrayList<Conso>();
        if (getValeurParametre("NATURE_RESILIATION").equals("MIGRATION")) {
        } else {
            Conso conso = getNewConso();
            conso.setOffre(contrat.getCodeOffre());
            conso.addDetail(new DetailConso("frais_resiliation", "Frais de résiliation", TYPE_PARAMETRE.NUMBER, conso.getMontantHT()));
            lc.add(conso);
            PontJPA pontJPA = PoolConnexion.getInstance().getPontJPAKaabu();
            List<Conso> consos = pontJPA.createQuery("SELECT c FROM Conso c WHERE c.compte = :compte and c.facture is null").setParameter("compte", contrat.getCompte()).getResultList();
            lc.addAll(consos);
            PoolConnexion.getInstance().putPontJPA(pontJPA);
            Date dateJour = new Date();
            ChargeParametre chargeParametre = ClientUtil.getChargeParametre(contrat, "CM_TERANGA_ABONNEMENT", "DUREE_ENGAGEMENT");
            int dureeEngagement = Integer.parseInt(chargeParametre.getValeurChaine());
            Date dateEffet = contrat.getDateEffet();            
            TableDecisionLigne tarifRemiseEngagementParLigne = getTarifRemiseEngagementParLigne(dureeEngagement);            
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(dateEffet);
            calendar.set(Calendar.MONTH, dureeEngagement);
            int nbMois=0;
            while(calendar.getTime().after(dateJour)){
                calendar.add(GregorianCalendar.MONTH, 1);
               if(calendar.getTime().after(dateJour) || calendar.getTime().equals(dateJour)){
            //Calculer le nombre d'abonnement qu'il doit payer
               nbMois++;
            }  
        }
            if (nbMois>=2){
            conso.setMontantHT(Double.parseDouble(tarifRemiseEngagementParLigne.getValeurSortieString("TARIF"))*nbMois);
            conso.setMontantTTC((conso.getMontantHT() * (1 + getTVA())) * nbMois);
            lc.add(conso);
            }
            else {
            conso.setMontantHT(Double.parseDouble(tarifRemiseEngagementParLigne.getValeurSortieString("TARIF")));
            conso.setMontantTTC(conso.getMontantHT() * (1 + getTVA()));
            lc.add(conso);
            }
            
    }
         return lc;
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
