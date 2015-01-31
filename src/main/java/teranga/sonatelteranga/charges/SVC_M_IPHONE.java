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
import com.oml.ded.kaabu.jpacontroller.PontJPA;
import com.oml.ded.kaabu.jpacontroller.PoolConnexion;
import com.oml.ded.kaabu.parametre.ChargeParametre;
import com.oml.ded.kaabu.parametre.Parametre;
import com.oml.ded.kaabu.util.OffreUtil;
import com.oml.ded.kaabu.util.Util;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import com.oml.ded.kaabu.util.Util.TYPE_PARAMETRE;
import com.oml.ded.kaabu.util.Utilitaire;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.TemporalType;
import table.decision.beans.CritereEgalite;
import table.decision.beans.ValeurAttributEntree;
import table.decision.controleurs.ControleurTableDecision;
import table.decision.entites.TableDecisionLigne;

/**
 *
 * @author Administrateur
 */
public class SVC_M_IPHONE extends Charge {

    public SVC_M_IPHONE(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, TYPE_CHARGE.SERVICE, declencheur, false);
        setPeriodique(true);
        setDescription("Options Smartphone");
        setEtat(Util.ETAT.INACTIF);
        addParametre(new ChargeParametre(code, "compteur_initialise", "N", TYPE_PARAMETRE.STRING, null, false, "N", null, "EXTERNE"));
        ChargeParametre cp1 = new ChargeParametre(code, "VOIX", "LIST=N-Non;Y-Oui", TYPE_PARAMETRE.STRING, null, false, "N", null, "EXTERNE");
        cp1.setLibelle("Forfait Voix");
        addParametre(cp1);
        ChargeParametre cp2 = new ChargeParametre(code, "DATA", "LIST=N-Non;Y-Oui", TYPE_PARAMETRE.STRING, null, false, "N", null, "EXTERNE");
        cp2.setLibelle("Forfait Data");
        addParametre(cp2);
        ChargeParametre cp3 = new ChargeParametre(code, "SMS", "LIST=N-Non;Y-Oui", TYPE_PARAMETRE.STRING, null, false, "N", null, "EXTERNE");
        cp3.setLibelle("Forfait SMS");
        addParametre(cp3);
        ChargeParametre cp4 = new ChargeParametre(code, "TYPE", "LIST=IPH-Iphone;GS3_1-Galaxy S3 300Mo;GS3_2-Galaxy S3 1Go;GS3_3-Galaxy S3 1Go avec Voix", TYPE_PARAMETRE.STRING, null, false, "IPH", null, "EXTERNE");
        cp4.setLibelle("Type Smartphone");
        addParametre(cp4);
        ChargeParametre chargeParametre5 = new ChargeParametre(code, "APN", "Genere APN", TYPE_PARAMETRE.STRING, null, false, "1", null, "EXTERNE", false);
        chargeParametre5.setAffichable(false);
        addParametre(chargeParametre5);
    }

    @Override
    public List<Conso> getConso(Usages arg0) {
        return java.util.Collections.EMPTY_LIST;
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
//        System.out.println("******************************** getConso SVC_M_IPHONE ********************************");
        List<Conso> consos = new ArrayList<Conso>();
        ChargeParametre cp = (ChargeParametre) getParametre("compteur_initialise");
//        System.out.println("compteur_initialise = " + cp.getValeurChaine());
        TableDecisionLigne tableDecisionLigne = getTableDecisionCompteurSmartPhone();
        PontJPA pontJPA = PoolConnexion.getInstance().getPontJPAKaabu();
        try {
            if ("N".equals(cp.getValeurChaine())) {
                initialiserCompteur(pontJPA, contrat, tableDecisionLigne, "VOIX", "remise_iphone", "Forfait Remise smartphone");
                initialiserCompteur(pontJPA, contrat, tableDecisionLigne, "DATA", "forfait_iphone", "Forfait data smartphone");
                initialiserCompteur(pontJPA, contrat, tableDecisionLigne, "SMS", "bonus_sms_iphone", "Forfait sms smartphone");

                Conso conso = getNewConso();
                consos.add(conso);

//                if (getValeurParametre("TYPE").contains("Z10")) {
//                    TableDecisionLigne tableDecisionPaiementAvance = getTableDecisionPaiementAvance("AVANCE_Z10");
//                    if (tableDecisionPaiementAvance != null && tableDecisionPaiementAvance.getValeurSortie("TARIF") != null) {
//                        Conso c = getNewConso();
//                        c.setMontantHT(Double.parseDouble(tableDecisionPaiementAvance.getValeurSortie("TARIF").toString()));
//                        c.setMontantTTC(c.getMontantHT() * getTVA() + c.getMontantHT());
//                        consos.add(c);
//                    }
//                }

                cp.setValeur("Y");
            } else {
                if ("Y".equals(getValeurParametre("VOIX"))) {
                    Conso conso = getNewConso();
                    conso.setAgregat("IPH");
                    conso.setCompte(contrat.getCompte());

                    int valeurInitiale = Integer.valueOf(tableDecisionLigne.getValeurSortie("VOIX").toString());
                    Compteur compteur = getCompteur(pontJPA, contrat, "remise_iphone", "Forfait Remise smartphone", valeurInitiale, false);
                    double forfaitNew = compteur.getValeurCourante() + compteur.getValeurInitiale();
                    double report = 0;
                    double montantConso = getMontantTTCConsos(pontJPA, contrat);
                    if (forfaitNew > montantConso) { // les conso sont inferieur a la valeur courante du forfait, il y a report. Le montant de la remise = au montant des consos
                        report = forfaitNew - montantConso;
                        conso.setMontantHT(montantConso);
                        conso.setTVA(0);
                        conso.setMontantTTC(montantConso); // Pas de TVA sur cette charge
                    } else { // pas de report, le montant de la remise = au forfait
                        conso.setMontantHT(forfaitNew);
                        conso.setTVA(0);
                        conso.setMontantTTC(forfaitNew);
                    }
                    conso.addDetail(new DetailConso("remise_iphone", "valeur remise smartphone", TYPE_PARAMETRE.NUMBER, -conso.getMontantHT()));
                    Util.initCompteur(compteur, (int) report);
                    consos.add(conso);
                }
            }
        } catch (Throwable e) {
            System.out.println(Utilitaire.getStackTrace(e));
        } finally {
            PoolConnexion.getInstance().putPontJPA(pontJPA);
        }
        return consos;
    }

    private TableDecisionLigne getTableDecisionCompteurSmartPhone() {
        ChargeParametre chargeParametre = (ChargeParametre) getParametre("TYPE");
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("CODE_OFFRE", getOffre().getOffreParent().getCode(), CritereEgalite.EGAL));
        attributEntrees.add(new ValeurAttributEntree("CODE_SERVICE", getCode(), CritereEgalite.EGAL));
        attributEntrees.add(new ValeurAttributEntree("TYPE", chargeParametre.getValeurChaine(), CritereEgalite.EGAL));
        TableDecisionLigne tableDecisionLigne = ControleurTableDecision.getInstance().getTableDecisionLigne("TD_COMPTEUR_SMART_PHONE", attributEntrees, new Date());
        return tableDecisionLigne;
    }

    private void initialiserCompteur(PontJPA pontJPA, Contrat contrat, TableDecisionLigne tableDecisionLigne, String type, String nomCompteur, String descriptionCompteur) {
        try {
            pontJPA.begin();
            Parametre chargeParametre = getParametre(type);
//            System.out.println("******************************** initialiserCompteur " + chargeParametre.getValeurChaine() + " ********************************");
            if ("Y".equals(chargeParametre.getValeurChaine())) {
                int valeurInitiale = Integer.parseInt(tableDecisionLigne.getValeurSortie(type).toString());
                List<Compteur> compteurs = pontJPA.getListFromNativeQuery("SELECT c.* FROM COMPTEUR c WHERE c.NOM = '" + nomCompteur + "' AND c.ID_CONTRAT = " + contrat.getId(), Compteur.class);
                if (compteurs.isEmpty()) {
                    Compteur compteur = new Compteur(nomCompteur, descriptionCompteur, valeurInitiale);
                    compteur.setContrat(contrat);
                    pontJPA.persist(compteur);
//                    System.out.println("Compteur : " + compteur.getNom() + ", Valeur Courant : " + compteur.getValeurCourante() + ", Valeur initiale : " + compteur.getValeurInitiale());
                } else {
                    Compteur compteur = compteurs.get(0);
                    compteur.setValeurCourante(0);
                    compteur.setValeurInitiale(valeurInitiale);
                    pontJPA.merge(compteur);
//                    System.out.println("Compteur : " + compteur.getNom() + ", Valeur Courant : " + compteur.getValeurCourante() + ", Valeur initiale : " + compteur.getValeurInitiale());
                }
            }
            pontJPA.commit();
        } catch (Throwable e) {
            e.printStackTrace();
            pontJPA.closeEm();
        }
    }

    private double getMontantTTCConsos(PontJPA pontJPA, Contrat contrat) {
        double montant = 0;
        List<Conso> listConso = pontJPA.createQuery("SELECT c FROM Conso c WHERE c.compte = :compte AND c.typeConso ='USAGE' "
                + "AND (  (c.agregat='SOG' and c.zone not like 'I%') or c.agregat not in ('ABOTG','SOG') ) AND c.dateConso BETWEEN :d1 AND :d2").setParameter("compte", contrat.getCompte()).setParameter("d1", contrat.getCompte().getDateAncienBill(), TemporalType.DATE).setParameter("d2", contrat.getCompte().getDateProchainBill(), TemporalType.DATE).getResultList();
        for (Conso c : listConso) {
            montant += c.getMontantTTC();
        }
        return montant;
    }

    private TableDecisionLigne getTableDecisionPaiementAvance(String type) {
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("CODE_OFFRE", getOffre().getOffreParent().getCode(), CritereEgalite.EGAL));
        attributEntrees.add(new ValeurAttributEntree("CODE_SERVICE", getCode(), CritereEgalite.EGAL));
        attributEntrees.add(new ValeurAttributEntree("TYPE", type, CritereEgalite.EGAL));
        TableDecisionLigne tableDecisionLigne = ControleurTableDecision.getInstance().getTableDecisionLigne("TD_SERVICES", attributEntrees, new Date());
        return tableDecisionLigne;
    }
}
