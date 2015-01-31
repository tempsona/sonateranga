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
import com.oml.ded.kaabu.util.Util;
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
public class CM_TERANGA_VOIX extends Charge {

    public CM_TERANGA_VOIX(String code, TYPE_CHARGE type) {
        super(code, type, TYPE_EVENT.USAGE, false);
        setDescription("Usage Voix");
        setEtat(Util.ETAT.ACTIF);
//        addCompteur(new Compteur("conso_voix_mensuelle", "Suivi conso", 0));
    }

    @Override
    public List<Conso> getConso(Usages cdr) {
        List<Conso> lc = new ArrayList<Conso>();
        if (cdr.getService().equals("voix")/* || cdr.getService().equals("visio")*/) {
            PontJPA pontJPA = PoolConnexion.getInstance().getPontJPAKaabu();
            ControleurTableDecision controleurTableDecision = new ControleurTableDecision();
            Map zoneMap = getZoneDestinationUsage(pontJPA, controleurTableDecision, cdr);
            Map tarifMap = getTarifZoneUsage(pontJPA, controleurTableDecision, zoneMap.get("zone").toString(), cdr);
            PoolConnexion.getInstance().putPontJPA(pontJPA);

            System.out.println("Zone : " + zoneMap.get("zone") + " cadence: " + tarifMap.get("cadencement"));
            
            if (Double.parseDouble(tarifMap.get("prix_CT_HP").toString()) > -1) {
                if (zoneMap.get("type").toString().endsWith("_ORA")) { // Mobile Orange(NAT_ORA) ou fixe Orange(FIX_ORA)
                    Map tarif = getCoutAppelNormalAvecCrenau(tarifMap, cdr);
                    if (Double.parseDouble(tarif.get("prix").toString()) > -1) { // le tarif existe
                        Conso c = getNewConso();
                        c.setUsage_id(cdr.getId());
                        double montant = Double.parseDouble(tarif.get("prix").toString());
                        c.setMontantHT(montant);
                        c.setMontantTTC((c.getMontantHT() * getTVA()) + c.getMontantHT());
                        c.setAgregat(zoneMap.get("agregat").toString());
                        c.setLegend(zoneMap.get("legend").toString());
                        c.setAppelant(cdr.getUtilisateur());
                        c.setCorrespondant(cdr.getCorrespondant());
                        c.setDuree(cdr.getDuree());
                        c.setZone(zoneMap.get("zone").toString());
                        c.setDateConso(cdr.getDateConsommation());
                        c.setUrl(cdr.getUrl());
                        c.setType_usage(cdr.getType());
                        c.setService_usage(cdr.getService());
                        c.addDetail(new DetailConso("duree_cad", "", TYPE_PARAMETRE.NUMBER, Double.parseDouble(tarif.get("dur_cad").toString()), true, cdr.getUtilisateur(), null));
                        c.addDetail(new DetailConso("crenau", "", TYPE_PARAMETRE.STRING, null, true, tarif.get("crenau").toString(), null));
                        lc.add(c);
                    }
                } else { // type d'appel ne commence pas par aucun des prefiixes NAT_ORA ou FIX_ORA
                    Map tarif = getCoutAppelNormalSansCrenau(tarifMap, cdr);
                    if (Double.parseDouble(tarif.get("prix").toString()) > -1) { // le tarif existe
                        Conso c = getNewConso();
                        c.setUsage_id(cdr.getId());
                        double montant = Double.parseDouble(tarif.get("prix").toString());
                        c.setMontantHT(montant);
                        c.setMontantTTC(c.getMontantHT() * getTVA() + c.getMontantHT());
                        c.setAgregat(zoneMap.get("agregat").toString());
                        c.setLegend(zoneMap.get("legend").toString());
                        c.setAppelant(cdr.getUtilisateur());
                        c.setCorrespondant(cdr.getCorrespondant());
                        c.setDuree(cdr.getDuree());
                        c.setZone(zoneMap.get("zone").toString());
                        c.setDateConso(cdr.getDateConsommation());
                        c.setUrl(cdr.getUrl());
                        c.setType_usage(cdr.getType());
                        c.setService_usage(cdr.getService());
                        c.addDetail(new DetailConso("duree_cad", "", TYPE_PARAMETRE.NUMBER, Double.parseDouble(tarif.get("dur_cad").toString()), true, cdr.getUtilisateur(), null));
                        c.addDetail(new DetailConso("crenau", "", TYPE_PARAMETRE.STRING, null, true, tarif.get("crenau").toString(), null));
                        lc.add(c);
                    }
                }
            } else {
                lc.add(Util.InterdictionUsage(this, cdr, "Le prix n'existe pas"));
            } 
        }
        return lc;
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
        return new ArrayList<Conso>();
    }

    /**
     * Methode de calcul d'appel voix avec deux crénaux HN (23h à 8h) et HP (8h à 23h)
     * @param map
     * @param cdr
     */
    private Map getCoutAppelNormalAvecCrenau(Map tarif, Usages cdr) {
        Map<String, Object> sortie = new HashMap<String, Object>();
        double item3 = 0;

        if (Double.parseDouble(tarif.get("cadencement").toString()) > 0) {        
            // calculer item3 ( diviser l'appel en partie HP et HN )
            if (cdr.getDuree() > Double.parseDouble(tarif.get("duree_CT").toString())) { // test sur la durée de l'appel
                double duree_a_cadencer = Util.floor((cdr.getDuree() - Double.parseDouble(tarif.get("duree_CT").toString())), 6);
                double nb_tranches = Util.floor(Util.arrondiSuperieur(duree_a_cadencer / Double.parseDouble(tarif.get("cadencement").toString())), 0);
                double duree_cad = Util.floor((Double.parseDouble(tarif.get("cadencement").toString()) * nb_tranches), 6);
                double dur_cad = Util.floor(duree_cad + Double.parseDouble(tarif.get("duree_CT").toString()), 6);
                double prix_plein = 0;
                double prix_nuit = 0;
                Date date_debut_decalee_CT = Util.decalerDateDuree(cdr.getDateConsommation(), Double.parseDouble(tarif.get("duree_CT").toString()));
                if(Util.isHeurePleine(date_debut_decalee_CT)){
                    double prix_sec_plein = Util.floor((Double.parseDouble(tarif.get("prix_appel_HP").toString()) / 60), 6);
                     prix_plein = prix_sec_plein * duree_cad;
                } else {
                    double prix_sec_nuit = Util.floor((Double.parseDouble(tarif.get("prix_appel_HN").toString()) / 60), 6);
                     prix_nuit = prix_sec_nuit * duree_cad;
                }
                
                item3 = prix_nuit + prix_plein;
                sortie.put("dur_cad", dur_cad);
            } else { // durée <= duree_CT (gratuit)
                sortie.put("dur_cad", 0);
            }
        } else {// Interdit
            // throw new IllegalArgumentException("Cadencement non Valide");
        }
        sortie.put("prix", item3);
        return sortie;
    }
    private Map getCoutAppelNormalAvecCrenauCopie(Map tarif, Usages cdr) {
        Map<String, Object> sortie = new HashMap<String, Object>();
        double item1 = 0;
        double item2 = 0;
        double item3 = 0;

        if (Double.parseDouble(tarif.get("cadencement").toString()) > 0) {
            // calculer item1
            if (Util.isHeurePleine(cdr.getDateConsommation())) {
                item1 = Util.floor(Double.parseDouble(tarif.get("prix_EA_HP").toString()), 6);
                sortie.put("crenau", "P");
            } else {
                item1 = Util.floor(Double.parseDouble(tarif.get("prix_EA_HN").toString()), 6);
                sortie.put("crenau", "N");
            }
            // calculer item2
            if (Util.isHeurePleine(cdr.getDateConsommation())) {
                item2 = Util.floor(Double.parseDouble(tarif.get("prix_CT_HP").toString()), 6);

            } else {
                item2 = Util.floor(Double.parseDouble(tarif.get("prix_CT_HN").toString()), 6);

            }

            // calculer item3 ( diviser l'appel en partie HP et HN )
            if (cdr.getDuree() > Double.parseDouble(tarif.get("duree_CT").toString())) { // test sur la durée de l'appel
                double duree_a_cadencer = Util.floor((cdr.getDuree() - Double.parseDouble(tarif.get("duree_CT").toString())), 6);
                double nb_tranches = Util.floor(Util.arrondiSuperieur(duree_a_cadencer / Double.parseDouble(tarif.get("cadencement").toString())), 0);
                double duree_cad = Util.floor((Double.parseDouble(tarif.get("cadencement").toString()) * nb_tranches), 6);
                double dur_cad = Util.floor(duree_cad + Double.parseDouble(tarif.get("duree_CT").toString()), 6);
                Date date_debut_decalee_CT = Util.decalerDateDuree(cdr.getDateConsommation(), Double.parseDouble(tarif.get("duree_CT").toString()));
                Date date_fin_decalee = Util.decalerDateDuree(date_debut_decalee_CT, duree_cad);
                //Split de la date d'appel en HP et HN
                Map duree = Util.dureeHeurePleineHeureCreuse(date_debut_decalee_CT, date_fin_decalee);
                //partie nuit
                double prix_sec_nuit = Util.floor((Double.parseDouble(tarif.get("prix_appel_HN").toString()) / 60), 6);
                double prix_nuit = Util.floor((prix_sec_nuit * Double.parseDouble(duree.get("dureeNuit").toString())), 6);
                //partie pleine
                double prix_sec_plein = Util.floor((Double.parseDouble(tarif.get("prix_appel_HP").toString()) / 60), 6);
                double prix_plein = Util.floor((prix_sec_plein * Double.parseDouble(duree.get("dureePleine").toString())), 6);
                item3 = prix_nuit + prix_plein;
                sortie.put("dur_cad", dur_cad);
            } else { // durée <= duree_CT (gratuit)
                sortie.put("dur_cad", 0);
            }
        } else {// Interdit
            // throw new IllegalArgumentException("Cadencement non Valide");
        }
        sortie.put("prix", item1 + item2 + item3);
        return sortie;
    }

    private Map getCoutAppelNormalSansCrenau(Map tarif, Usages cdr) {
        Map<String, Object> sortie = new HashMap<String, Object>();
        double item1 = 0;
        double item2 = 0;
        double item3 = 0;
        // System.out.println("cadencement : " + Double.parseDouble(tarif.get("cadencement").toString()));
        if (Double.parseDouble(tarif.get("cadencement").toString()) > 0) {
            // calculer item1
            item1 = Util.floor(Double.parseDouble(tarif.get("prix_EA_HP").toString()), 6);
            // calculer item2
            item2 = Util.floor(Double.parseDouble(tarif.get("prix_CT_HP").toString()), 6);
            // calculer item3 ( diviser l'appel en parrtie HP et HN )

            if (cdr.getDuree() > Double.parseDouble(tarif.get("duree_CT").toString())) { // test sur la durée de l'appel
                double duree_a_cadencer = Util.floor((cdr.getDuree() - Double.parseDouble(tarif.get("duree_CT").toString())), 6);
                double nb_tranches = Util.floor(Util.arrondiSuperieur(duree_a_cadencer / Double.parseDouble(tarif.get("cadencement").toString())), 0);
                double duree_cad = Util.floor((Double.parseDouble(tarif.get("cadencement").toString()) * nb_tranches), 6);
                double dur_cad = Util.floor(duree_cad + Double.parseDouble(tarif.get("duree_CT").toString()), 6);
                double prix_sec = Util.floor((Double.parseDouble(tarif.get("prix_appel_HP").toString()) / 60), 6);
                double prix_cadencement = Util.floor((prix_sec * Double.parseDouble(tarif.get("cadencement").toString())), 6);
                double cout_cad = Util.floor((prix_cadencement * nb_tranches), 6);
                item3 = cout_cad;
                sortie.put("dur_cad", dur_cad);
//                  System.out.println("1- " + duree_a_cadencer + "2-tranche " + nb_tranches + "3-prix séc: " + prix_sec + " cout : " + cout_cad);
            } else { // durée <= duree_CT (gratuit)
                sortie.put("dur_cad", 0);
            }
        } else {// Interdit
            // System.out.println("Interdit");
            //  throw new IllegalArgumentException("Cadencement non Valide");
            sortie.put("dur_cad", 0);
        }
//System.out.println(" sans creanux --- item 1: "+item1+" item 2: "+item2+" item 3: "+item3);
        sortie.put("prix", item1 + item2 + item3);
        sortie.put("crenau", "P");
        return sortie;
    }

    /**
     *Methode qui retourne le tarif par rapport à la zone d'un usage.
     * @param zone
     * @param cdr
     * @return (description, duree_CT, prix_CT_HC ,prix_CT_HP, prix_CT_HN, cadencement, prix_appel_HC, prix_appel_HP, prix_appel_HN, prix_EA_HP, prix_EA_HC, prix_EA_HN)
     */
    private Map getTarifZoneUsage(PontJPA pontJPA, ControleurTableDecision controleurTableDecision, String zone, Usages cdr) {
        // Appel à la table de décision des codes comptables
        Map<String, Object> sortie = new HashMap<String, Object>();
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();

        // rechercher tarif  promotion
        attributEntrees.add(new ValeurAttributEntree("ZONE", zone, CritereEgalite.EGAL));
        if (cdr.getService().equals("voix")) {
            TableDecisionLigne tableDecisionLignep = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_TARIF_ZONE_PROMOTION_VOIX", cdr.getDateConsommation(), attributEntrees);
            if (!tableDecisionLignep.getValeursSorties().isEmpty() && Util.estDedans((Date) tableDecisionLignep.getValeurSortie("DATE_DEBUT"), (Date) tableDecisionLignep.getValeurSortie("DATE_FIN"), cdr.getDateConsommation())) {
                sortie.put("description", tableDecisionLignep.getValeurSortie("DESCRIPTION"));
                sortie.put("duree_CT", tableDecisionLignep.getValeurSortie("DUREE_CT"));
                sortie.put("prix_CT_HC", tableDecisionLignep.getValeurSortie("PRIX_CT_HC"));
                sortie.put("prix_CT_HP", tableDecisionLignep.getValeurSortie("PRIX_CT_HP"));
                sortie.put("prix_CT_HN", tableDecisionLignep.getValeurSortie("PRIX_CT_HN"));
                sortie.put("cadencement", tableDecisionLignep.getValeurSortie("CADENCEMENT"));
                sortie.put("prix_appel_HC", tableDecisionLignep.getValeurSortie("PRIX_APPEL_HC"));
                sortie.put("prix_appel_HP", tableDecisionLignep.getValeurSortie("PRIX_APPEL_HP"));
                sortie.put("prix_appel_HN", tableDecisionLignep.getValeurSortie("PRIX_APPEL_HN"));
                sortie.put("prix_EA_HC", tableDecisionLignep.getValeurSortie("PRIX_EA_HC"));
                sortie.put("prix_EA_HP", tableDecisionLignep.getValeurSortie("PRIX_EA_HP"));
                sortie.put("prix_EA_HN", tableDecisionLignep.getValeurSortie("PRIX_EA_HN"));
                // fin rechercher tarif promotion
            } else {
                // rechercher tarif hors promotion
                attributEntrees = new ArrayList<ValeurAttributEntree>();
                attributEntrees.add(new ValeurAttributEntree("ZONE", zone, CritereEgalite.EGAL));
                TableDecisionLigne tableDecisionLigne = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_TARIF_VOIX_ZONE", cdr.getDateConsommation(), attributEntrees);
                sortie.put("description", tableDecisionLigne.getValeurSortie("DESCRIPTION"));
                sortie.put("duree_CT", tableDecisionLigne.getValeurSortie("DUREE_CT"));
                sortie.put("prix_CT_HC", tableDecisionLigne.getValeurSortie("PRIX_CT_HC"));
                sortie.put("prix_CT_HP", tableDecisionLigne.getValeurSortie("PRIX_CT_HP"));
                sortie.put("prix_CT_HN", tableDecisionLigne.getValeurSortie("PRIX_CT_HN"));
                sortie.put("cadencement", tableDecisionLigne.getValeurSortie("CADENCEMENT"));
                sortie.put("prix_appel_HC", tableDecisionLigne.getValeurSortie("PRIX_APPEL_HC"));
                sortie.put("prix_appel_HP", tableDecisionLigne.getValeurSortie("PRIX_APPEL_HP"));
                sortie.put("prix_appel_HN", tableDecisionLigne.getValeurSortie("PRIX_APPEL_HN"));
                sortie.put("prix_EA_HC", tableDecisionLigne.getValeurSortie("PRIX_EA_HC"));
                sortie.put("prix_EA_HP", tableDecisionLigne.getValeurSortie("PRIX_EA_HP"));
                sortie.put("prix_EA_HN", tableDecisionLigne.getValeurSortie("PRIX_EA_HN"));
                // fin rechercher tarif hors promotion
            }
        }
        return sortie;
    }

    /**
     * Renvoie la Zone pour un usage voix(spécial ou normal)
     * @param cdr
     * @return (zone, detail, description, type, prefix, agregat, legend)
     */
    private Map getZoneDestinationUsage(PontJPA pontJPA, ControleurTableDecision controleurTableDecision, Usages cdr) {
        TableDecisionLigne tableDecisionLigne;
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        Map<String, Object> sortie = new HashMap<String, Object>();
        attributEntrees.add(new ValeurAttributEntree("PREFIXE", cdr.getCorrespondant().trim(), CritereEgalite.EGAL));
        if (cdr.getService().equals("voix")) {
            tableDecisionLigne = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_ZONE_DESTINATION", cdr.getDateConsommation(), attributEntrees);
            if (!((String) tableDecisionLigne.getValeurSortie("ZONE")).equals("INC")) { // contient une ligne (appel special ou reversement)
                sortie.put("zone", tableDecisionLigne.getValeurSortie("ZONE"));
                sortie.put("detail", tableDecisionLigne.getValeurSortie("DETAIL"));
                sortie.put("description", tableDecisionLigne.getValeurSortie("DESCRIPTION"));
                sortie.put("type", tableDecisionLigne.getValeurSortie("TYPE"));
                sortie.put("prefix", tableDecisionLigne.getValeurSortie("PREFIX"));
                sortie.put("agregat", tableDecisionLigne.getValeurSortie("AGREGAT"));
                sortie.put("legend", tableDecisionLigne.getValeurSortie("LEGEND"));

            } else { //Appel voix normal
                attributEntrees = new ArrayList<ValeurAttributEntree>();
                attributEntrees.add(new ValeurAttributEntree("PREFIXE", cdr.getCorrespondant().trim(), CritereEgalite.COMMENCE_PAR));
                tableDecisionLigne = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_ZONE_DESTINATION", cdr.getDateConsommation(), attributEntrees);
                sortie.put("zone", tableDecisionLigne.getValeurSortie("ZONE"));
                sortie.put("detail", tableDecisionLigne.getValeurSortie("DETAIL"));
                sortie.put("description", tableDecisionLigne.getValeurSortie("DESCRIPTION"));
                sortie.put("type", tableDecisionLigne.getValeurSortie("TYPE"));
                sortie.put("prefix", tableDecisionLigne.getValeurSortie("PREFIX"));
                sortie.put("agregat", tableDecisionLigne.getValeurSortie("AGREGAT"));
                sortie.put("legend", tableDecisionLigne.getValeurSortie("LEGEND"));

                /* Table des numéro préférés*/
                attributEntrees = new ArrayList<ValeurAttributEntree>();
                attributEntrees.add(new ValeurAttributEntree("APPELANT", cdr.getUtilisateur().trim(), CritereEgalite.EGAL));
                attributEntrees.add(new ValeurAttributEntree("PREFERE", cdr.getCorrespondant().trim(), CritereEgalite.EGAL));
                tableDecisionLigne = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_NUMERO_PREFERE", cdr.getDateConsommation(), attributEntrees);
                if (!((String) tableDecisionLigne.getValeurSortie("ZONE")).equals("NON")) {// si appelé est préféré
                    sortie.put("zone", tableDecisionLigne.getValeurSortie("ZONE"));
                }
                /*Fin test préféré*/
            }
        }
        return sortie;
    }
}
