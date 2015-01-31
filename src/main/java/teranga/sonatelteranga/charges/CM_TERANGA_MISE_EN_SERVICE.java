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
public class CM_TERANGA_MISE_EN_SERVICE extends Charge {

    public CM_TERANGA_MISE_EN_SERVICE(String code, TYPE_CHARGE type) {
        super(code, type, TYPE_EVENT.SOUSCRIPTION, false);
        setDescription("Frais Mise en Service");
        setTaxeMode(TAXE_MODE.TVA);
        setType(TYPE_CHARGE.EXCEPTIONNEL);

        addParametre(new ChargeParametre(this.getCode(), "NATURE_SOUSCRIPTION", "LIST=SOUSCRIPTION;MIGRATION", TYPE_PARAMETRE.STRING, "SOUSCRIPTION"));        
//        addParametre(new ChargeParametre(this.getCode(), "TARIFICATION_SECONDE", "LIST=OUI;NON", TYPE_PARAMETRE.STRING, "NON"));

        /**
         * Ajout des charges dépendantes de la charge
         */
        addChargeDependant(new CM_TERANGA_ABONNEMENT("CM_TERANGA_ABONNEMENT", TYPE_CHARGE.EXCEPTIONNEL));
//        addChargeDependant(new CM_GESTE_COMMERCIAL("CM_GESTE_COMMERCIAL", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, false));
        addChargeDependant(new CM_TERANGA_CAUTION("CM_TERANGA_CAUTION", TYPE_CHARGE.EXCEPTIONNEL));
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
//            CM_MIGRATION charge = new CM_MIGRATION("CM_MIGRATION", TYPE_CHARGE.EXCEPTIONNEL, TYPE_EVENT.SOUSCRIPTION, false);
//            charge.setOffre(this.getOffre());
//            l.addAll(charge.getConso(contrat));
        } else { // Frais de souscription
            Conso c = getNewConso();
            c.setDateConso(new Date());
            c.setPayeur(contrat.getCompte().getCode());
            c.setOffre(contrat.getCodeOffre());
            int nombreContratActif = contrat.getCompte().getClient().getNombreContratActif(contrat.getCodeOffre());
            nombreContratActif++;
            TableDecisionLigne tarifRemiseFMSParLigne = getTarifRemiseFMSParLigne(nombreContratActif);
            c.setMontantHT(Double.parseDouble(tarifRemiseFMSParLigne.getValeurSortie("TARIF").toString()));
            c.setMontantTTC(c.getMontantHT() * (1 + getTVA()));
            c.setAgregat(tarifRemiseFMSParLigne.getValeurSortie("AGREGAT").toString());

            l.add(c);

            /**
             * Traitement des charges dépendantes
             */
            for (Charge ch : getDependants()) {                                
                ch.setOffre(getOffre());
                l.addAll(ch.getConso(contrat));
            }


        }

        /**
         * Renvoie de la liste comme retour
         */
        return l;
    }

    private TableDecisionLigne getTarifRemiseFMSParLigne(int nbComptes) {
        System.out.println("************************ getTarifAbonnementCautionParLigne ");
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("CODE_SERVICE", getCode(), CritereEgalite.EGAL));
        attributEntrees.add(new ValeurAttributEntree("CODE_OFFRE", getOffre().getOffreParent().getCode(), CritereEgalite.EGAL));
        List<TableDecisionLigne> lignes = ControleurTableDecision.getInstance().getTableDecisionLignes("TD_REMISE_FMS_PROMO", attributEntrees, new Date());
        if (!lignes.isEmpty()) {          
            for (TableDecisionLigne ligne : lignes) {
                double min = Double.valueOf(ligne.getValeurSortieString("MIN"));//MIN
                double max = Double.valueOf(ligne.getValeurSortieString("MAX"));//MAX
                Date valeurSortieDD = (Date) ligne.getValeurSortie("DATEDEBUT");
                Date valeurSortieDF = (Date) ligne.getValeurSortie("DATEFIN");
                Date dateJourDate = new Date();
                if ((valeurSortieDD.before(dateJourDate) || valeurSortieDD.equals(dateJourDate))
                        && (valeurSortieDF.after(dateJourDate) || valeurSortieDF.equals(dateJourDate))
                        && min <= nbComptes && nbComptes <= max) {
                    return ligne;
                }
            }

            lignes = ControleurTableDecision.getInstance().getTableDecisionLignes("TD_REMISE_FMS", attributEntrees, new Date());
            System.out.println("************************ Nombre TDLignes " + lignes.size() + ", Nombre Comptes : " + nbComptes);
            for (TableDecisionLigne ligne : lignes) {
                double min = Double.valueOf(ligne.getValeurSortieString("MIN"));//MIN
                double max = Double.valueOf(ligne.getValeurSortieString("MAX"));//MAX
                System.out.println("************************ Min : " + min + ", Max : " + max + ", ID : " + ligne.getId());
                if (min <= nbComptes && nbComptes <= max) {
                    System.out.println("************************ OK " + ligne.getId());
                    return ligne;
                }
            }
        } else {
            lignes = ControleurTableDecision.getInstance().getTableDecisionLignes("TD_REMISE_FMS", attributEntrees, new Date());
            System.out.println("************************ Nombre TDLignes " + lignes.size() + ", Nombre Comptes : " + nbComptes);
            for (TableDecisionLigne ligne : lignes) {
                double min = Double.valueOf(ligne.getValeurSortieString("MIN"));//MIN
                double max = Double.valueOf(ligne.getValeurSortieString("MAX"));//MAX
                System.out.println("************************ Min : " + min + ", Max : " + max + ", ID : " + ligne.getId());
                if (min <= nbComptes && nbComptes <= max) {
                    System.out.println("************************ OK " + ligne.getId());
                    return ligne;
                }
            }
        }
        System.out.println("************************ KO");
        return null;
    }
}
