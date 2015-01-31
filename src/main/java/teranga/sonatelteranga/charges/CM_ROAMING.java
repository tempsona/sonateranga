/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.oml.ded.kaabu.orangepro.charges;

import com.oml.ded.kaabu.charge.Charge;
import com.oml.ded.kaabu.client.Contrat;
import com.oml.ded.kaabu.collecte.Pont;
import com.oml.ded.kaabu.collecte.Usages;
import com.oml.ded.kaabu.conso.Conso;
import com.oml.ded.kaabu.jpacontroller.PontJPA;
import com.oml.ded.kaabu.jpacontroller.PoolConnexion;
import com.oml.ded.kaabu.parametre.ChargeParametre;
import com.oml.ded.kaabu.util.Util;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import table.decision.beans.CritereEgalite;
import table.decision.beans.ValeurAttributEntree;
import table.decision.controleurs.ControleurTableDecision;
import table.decision.entites.TableDecision;
import table.decision.entites.TableDecisionInstanceChronologie;
import table.decision.entites.TableDecisionLigne;

/**
 *
 * @author sidibe0091
 */
public class CM_ROAMING extends Charge {

    public CM_ROAMING(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
        setDescription("Usage et promotion de roaming");
        setEtat(Util.ETAT.ACTIF);
        addParametre(new ChargeParametre(code, "TYPE_CONTRAT", "Option Flotte", Util.TYPE_PARAMETRE.STRING, null, aProvisionner, "X", null, "EXTERNE"));
    }

    @Override
    public List<Conso> getConso(Usages cdr) {
        String msisdn = Util.findMsisdn((Pont.trouverPont(cdr.getUtilisateur(), cdr.getDateConsommation())).getContrat().getCompte());
        if (msisdn != null) {
            cdr.setUtilisateur("0" + msisdn);
        }
        
        List<Conso> lc = new ArrayList<Conso>();
        PontJPA pontJPA = PoolConnexion.getInstance().getPontJPAKaabu();
        ControleurTableDecision controleurTableDecision = new ControleurTableDecision();
        Map zoneMap = getZoneDestinationUsageFlotte(pontJPA, controleurTableDecision, cdr);
        if (cdr.getService().equals("roaming")) {
            if (cdr.getType().equals("data")) {
                double total_vol_byte = cdr.getVolumeInKo() + cdr.getVolumeOuKo();
                double total_volume = Util.arrondiSuperieur(Util.floor(total_vol_byte / 1024, 0));
                double cout_brut = Util.arrondiStandard(Util.floor(cdr.getCout() / Math.pow(10, cdr.getNombreDecimal()), 6));
                Conso c = getNewConso();
                c.setUsage_id(cdr.getId());
                c.setMontantHT(cout_brut);
                c.setMontantTTC(c.getMontantHT() * getTVA() + c.getMontantHT());
                c.setAgregat("GRO");
                c.setLegend("13");
                c.setAppelant(cdr.getUtilisateur());
                c.setCorrespondant(cdr.getCorrespondant());
                c.setDuree(cdr.getDuree());
                c.setZone(zoneMap.get("zone") + "");
                c.setCode_reseau(cdr.getTapeCode());
                c.setDateConso(cdr.getDateConsommation());
                c.setUrl(cdr.getUrl());
                c.setType_usage(cdr.getType());
                c.setService_usage(cdr.getService());
                c.setVolume_ko(cdr.getVolumeKo());
                lc.add(c);
            } else if (cdr.getType().equals("mms")) {
                double total_vol_byte = cdr.getVolumeInKo() + cdr.getVolumeOuKo();
                double total_volume = Util.arrondiSuperieur(Util.floor(total_vol_byte / 1024, 0));
                double cout_brut = Util.arrondiStandard(Util.floor(cdr.getCout() / Math.pow(10, cdr.getNombreDecimal()), 6));
                Conso c = getNewConso();
                c.setUsage_id(cdr.getId());
                c.setMontantHT(cout_brut);
                c.setMontantTTC(c.getMontantHT() * getTVA() + c.getMontantHT());
                c.setAgregat("GRO");
                c.setLegend("13");
                c.setAppelant(cdr.getUtilisateur());
                c.setCorrespondant(cdr.getCorrespondant());
                c.setDuree(cdr.getDuree());
                c.setZone(zoneMap.get("zone") + "");
                c.setCode_reseau(cdr.getTapeCode());
                c.setDateConso(cdr.getDateConsommation());
                c.setUrl(cdr.getUrl());
                c.setType_usage(cdr.getType());
                c.setService_usage(cdr.getService());
                c.setVolume_ko(cdr.getVolumeKo());
                lc.add(c);
            } else if (cdr.getType().equals("sms")) {
                double cout_brut = Util.arrondiStandard(Util.floor(cdr.getCout() / Math.pow(10, cdr.getNombreDecimal()), 6));
                Conso c = getNewConso();
                c.setUsage_id(cdr.getId());
                c.setMontantHT(cout_brut);
                c.setMontantTTC(c.getMontantHT() * getTVA() + c.getMontantHT());
                if (cdr.getCallType().equals("MT")) {
                    c.setAgregat("SRI");
                    c.setLegend("11");
                } else {
                    c.setAgregat("SRO");
                    c.setLegend("11");
                }
                c.setAppelant(cdr.getUtilisateur());
                c.setCorrespondant(cdr.getCorrespondant());
                c.setDuree(cdr.getDuree());
                c.setZone(zoneMap.get("zone") + "");
                c.setCode_reseau(cdr.getTapeCode());
                c.setDateConso(cdr.getDateConsommation());
                c.setUrl(cdr.getUrl());
                c.setType_usage(cdr.getType());
                c.setService_usage(cdr.getService());
                lc.add(c);
            } else if (cdr.getType().equals("voix")) { // Roaming Voix
                String agregat = "ORP";
                String legend = "5";
                if (cdr.getCallType().equals("MT")) {
                    agregat = "ROP";
                    legend = "4";
                }

                if (cdr.getCallType().equals("MT")) { // appel reçu                                   
                    if (getValeurParametre("TYPE_CONTRAT").toString().startsWith("GFU") || getValeurParametre("TYPE_CONTRAT").toString().startsWith("FLOTTE")) { // si option GFU
                        if (isOrangeZone(pontJPA, controleurTableDecision, cdr).equals("Y")) { // si Orange Zone                           
                            if (isAppelIntraflotte(pontJPA, controleurTableDecision, cdr).equals("Y")) { // si intra Flotte
                                double cout_brut_int = getTarifIntraFlotteMT(pontJPA, controleurTableDecision, cdr);
                                int cout_add_GINGS = 0;
                                if ("tape_code".equals("GINGS")) {
                                    cout_add_GINGS = 60;
                                }
                                double cout_brut = cout_brut_int + cout_add_GINGS;
                                double cout_final = getPromoRemise(pontJPA, controleurTableDecision, cdr, cout_brut);
                                Conso c = getNewConso();
                                c.setUsage_id(cdr.getId());
                                c.setMontantHT(cout_final);
                                c.setMontantTTC(c.getMontantHT() * getTVA() + c.getMontantHT());
                                c.setAgregat(agregat);
                                c.setLegend(legend);
                                c.setAppelant(cdr.getUtilisateur());
                                c.setCorrespondant(cdr.getCorrespondant());
                                c.setDuree(cdr.getDuree());
                                c.setZone(zoneMap.get("zone") + "");
                                c.setCode_reseau(cdr.getTapeCode());
                                c.setDateConso(cdr.getDateConsommation());
                                c.setUrl(cdr.getUrl());
                                c.setType_usage(cdr.getType());
                                c.setService_usage(cdr.getService());
                                lc.add(c);
                            } else { // si non intraFlotte
                                double cout_brut = Util.arrondiStandard(Util.floor(cdr.getCout() / Math.pow(10, cdr.getNombreDecimal()), 6));
                                double cout_final = getPromoRemise(pontJPA, controleurTableDecision, cdr, cout_brut);
                                Conso c = getNewConso();
                                c.setUsage_id(cdr.getId());
                                c.setMontantHT(cout_final);
                                c.setMontantTTC(c.getMontantHT() * getTVA() + c.getMontantHT());
                                c.setAgregat(agregat);
                                c.setLegend(legend);
                                c.setAppelant(cdr.getUtilisateur());
                                c.setCorrespondant(cdr.getCorrespondant());
                                c.setDuree(cdr.getDuree());
                                c.setZone(zoneMap.get("zone") + "");
                                c.setCode_reseau(cdr.getTapeCode());
                                c.setDateConso(cdr.getDateConsommation());
                                c.setUrl(cdr.getUrl());
                                c.setType_usage(cdr.getType());
                                c.setService_usage(cdr.getService());
                                lc.add(c);
                            }
                        } else { // si non OrangeZone
                            double cout_brut = Util.arrondiStandard(Util.floor(cdr.getCout() / Math.pow(10, cdr.getNombreDecimal()), 6));
                            double cout_final = getPromoRemise(pontJPA, controleurTableDecision, cdr, cout_brut);
                            Conso c = getNewConso();
                            c.setUsage_id(cdr.getId());
                            c.setMontantHT(cout_final);
                            c.setMontantTTC(c.getMontantHT() * getTVA() + c.getMontantHT());
                            c.setAgregat(agregat);
                            c.setLegend(legend);
                            c.setAppelant(cdr.getUtilisateur());
                            c.setCorrespondant(cdr.getCorrespondant());
                            c.setDuree(cdr.getDuree());
                            c.setZone(zoneMap.get("zone") + "");
                            c.setCode_reseau(cdr.getTapeCode());
                            c.setDateConso(cdr.getDateConsommation());
                            c.setUrl(cdr.getUrl());
                            c.setType_usage(cdr.getType());
                            c.setService_usage(cdr.getService());
                            lc.add(c);
                        }
                    } else {  // si non Flotte
                        double cout_brut = Util.arrondiStandard(Util.floor(cdr.getCout() / Math.pow(10, cdr.getNombreDecimal()), 6));
                        double cout_final = getPromoRemise(pontJPA, controleurTableDecision, cdr, cout_brut);
                        Conso c = getNewConso();
                        c.setUsage_id(cdr.getId());
                        c.setMontantHT(cout_final);
                        c.setMontantTTC(c.getMontantHT() * getTVA() + c.getMontantHT());
                        c.setAgregat(agregat);
                        c.setLegend(legend);
                        c.setAppelant(cdr.getUtilisateur());
                        c.setCorrespondant(cdr.getCorrespondant());
                        c.setDuree(cdr.getDuree());
                        c.setZone(zoneMap.get("zone") + "");
                        c.setCode_reseau(cdr.getTapeCode());
                        c.setDateConso(cdr.getDateConsommation());
                        c.setUrl(cdr.getUrl());
                        c.setType_usage(cdr.getType());
                        c.setService_usage(cdr.getService());
                        lc.add(c);
                    }
                } else { // si non MT                 
                    double cout_brut = Util.arrondiStandard(Util.floor(cdr.getCout() / Math.pow(10, cdr.getNombreDecimal()), 6));
                    double cout_final = getPromoRemise(pontJPA, controleurTableDecision, cdr, cout_brut);
                    Conso c = getNewConso();
                    c.setUsage_id(cdr.getId());
                    c.setMontantHT(cout_final);
                    c.setMontantTTC(c.getMontantHT() * getTVA() + c.getMontantHT());
                    c.setAgregat(agregat);
                    c.setLegend(legend);
                    c.setAppelant(cdr.getUtilisateur());
                    c.setCorrespondant(cdr.getCorrespondant());
                    c.setDuree(cdr.getDuree());
                    c.setZone(zoneMap.get("zone") + "");
                    c.setCode_reseau(cdr.getTapeCode());
                    c.setDateConso(cdr.getDateConsommation());
                    c.setUrl(cdr.getUrl());
                    c.setType_usage(cdr.getType());
                    c.setService_usage(cdr.getService());
                    lc.add(c);
                }
            } else {
                Util.InterdictionUsage(this, cdr, "");
            }
        }
        PoolConnexion.getInstance().putPontJPA(pontJPA);
        return lc;
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
        return java.util.Collections.EMPTY_LIST;
    }

    private double calculeRemise(double pourcentage, double montant) {
        double percent = Util.arrondiStandard(Util.floor(pourcentage / 100, 6));
        double montant_de_remise = percent * montant;
        double montant_apres_remise = montant - montant_de_remise;
        return montant_apres_remise;
    }

    private String isAppelIntraflotte(PontJPA pontJPA, ControleurTableDecision controleurTableDecision, Usages cdr) {
        String flag = "N";
        TableDecisionLigne tableDecisionLigne;
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("NUMERO", cdr.getUtilisateur().trim(), CritereEgalite.EGAL));

        TableDecision tableDecision = cdr.getSessionBigTableDecision().getTableDecision(pontJPA, controleurTableDecision, "TD_FLOTTE");
        TableDecisionInstanceChronologie tdic = tableDecision.getTableDecisionInstanceChronologie(cdr.getDateConsommation());
        tableDecisionLigne = tdic.getTableDecisionLigneVAL(attributEntrees);

        TableDecisionLigne tableDecisionLigne1;
        List<ValeurAttributEntree> attributEntrees1 = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("NUMERO", cdr.getCorrespondant().trim(), CritereEgalite.EGAL));
        tableDecisionLigne1 = tdic.getTableDecisionLigneVAL(attributEntrees1);

        if (tableDecisionLigne.getValeurSortie("CUG_ID").toString().equals(tableDecisionLigne1.getValeurSortie("CUG_ID"))) {
            flag = "Y";
        }

        return flag;
    }

    /**
     * Renvoie la Zone pour un usage voix(spécial ou normal ou Flotte)
     *
     * @param cdr
     * @return (zone, detail, description, type, prefix, agregat, legend)
     */
    private Map getZoneDestinationUsageFlotte(PontJPA pontJPA, ControleurTableDecision controleurTableDecision, Usages cdr) {
        TableDecisionLigne tableDecisionLigne;
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        Map<String, Object> sortie = new HashMap<String, Object>();
        attributEntrees.add(new ValeurAttributEntree("PREFIXE", cdr.getCorrespondant().trim(), CritereEgalite.EGAL));
        if (cdr.getService().equals("voix")) {
//            tableDecisionLigne = controleurTableDecision.getTableDecisionLigne(pontJPA, "TD_ZONE_DESTINATION", attributEntrees, cdr.getDateConsommation());
            tableDecisionLigne = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_ZONE_DESTINATION", cdr.getDateConsommation(), attributEntrees);
            if (!((String) tableDecisionLigne.getValeurSortie("ZONE")).equals("null")) { // contient une ligne (appel special ou reversement)
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
//                tableDecisionLigne = controleurTableDecision.getTableDecisionLigne(pontJPA, "TD_ZONE_DESTINATION", attributEntrees, cdr.getDateConsommation());
                tableDecisionLigne = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_ZONE_DESTINATION", cdr.getDateConsommation(), attributEntrees);
                sortie.put("zone", tableDecisionLigne.getValeurSortie("ZONE"));
                sortie.put("detail", tableDecisionLigne.getValeurSortie("DETAIL"));
                sortie.put("description", tableDecisionLigne.getValeurSortie("DESCRIPTION"));
                sortie.put("type", tableDecisionLigne.getValeurSortie("TYPE"));
                sortie.put("prefix", tableDecisionLigne.getValeurSortie("PREFIX"));
                sortie.put("agregat", tableDecisionLigne.getValeurSortie("AGREGAT"));
                sortie.put("legend", tableDecisionLigne.getValeurSortie("LEGEND"));
            }

            attributEntrees = new ArrayList<ValeurAttributEntree>();
            attributEntrees.add(new ValeurAttributEntree("NUMERO", cdr.getCorrespondant().trim(), CritereEgalite.EGAL));

            TableDecision tableDecision = cdr.getSessionBigTableDecision().getTableDecision(pontJPA, controleurTableDecision, "TD_FLOTTE");
            TableDecisionInstanceChronologie tdic = tableDecision.getTableDecisionInstanceChronologie(cdr.getDateConsommation());
            tableDecisionLigne = tdic.getTableDecisionLigneVAL(attributEntrees);

            String CUG_id_appele = tableDecisionLigne.getValeurSortie("CUG_ID").toString();

            attributEntrees = new ArrayList<ValeurAttributEntree>();
            attributEntrees.add(new ValeurAttributEntree("NUMERO", cdr.getUtilisateur().trim(), CritereEgalite.EGAL));
            tableDecisionLigne = tdic.getTableDecisionLigneVAL(attributEntrees);
            String CUG_id_appelant = tableDecisionLigne.getValeurSortie("CUG_ID").toString();

            if (CUG_id_appelant.equals(CUG_id_appele) && !CUG_id_appele.equals("INC")) { // Test si c'est appel Flotte
                if (CUG_id_appele.equals("IKA")) {
                    sortie.put("zone", "FLOTTE_ORANGE");
                } else {
                    sortie.put("zone", "FLOTTE");
                }
                sortie.put("detail", tableDecisionLigne.getValeurSortie("ENTREPRISE_ID"));
                sortie.put("description", "");
                sortie.put("type", "FLOTTE");
                sortie.put("prefix", tableDecisionLigne.getValeurSortie("PREFIX"));
                sortie.put("agregat", "IKP");
                sortie.put("legend", "3");
            }

        }
        return sortie;
    }
    /*
     * Retourne Y ou N si l'appel Roaming est dans Orange Zone
     */

    private String isOrangeZone(PontJPA pontJPA, ControleurTableDecision controleurTableDecision, Usages cdr) {
        TableDecisionLigne tableDecisionLigne = new TableDecisionLigne();
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("TAP_CODE", cdr.getTapeCode(), CritereEgalite.EGAL));
//        tableDecisionLigne = controleurTableDecision.getTableDecisionLigne(pontJPA, "TD_ORANGE_ZONE", attributEntrees, cdr.getDateConsommation());
        tableDecisionLigne = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_ORANGE_ZONE", cdr.getDateConsommation(), attributEntrees);
        return tableDecisionLigne.getValeurSortie("FLAG").toString();
    }

    private double getTarifIntraFlotteMT(PontJPA pontJPA, ControleurTableDecision controleurTableDecision, Usages cdr) throws NumberFormatException {
        TableDecisionLigne tableDecisionLigne = new TableDecisionLigne();
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("TYPE_OPTION", getValeurParametre("TYPE_CONTRAT").toString(), CritereEgalite.EGAL));
//        tableDecisionLigne = controleurTableDecision.getTableDecisionLigne(pontJPA, "TD_TARIF_ROAMING_FLOTTE", attributEntrees, cdr.getDateConsommation());
        tableDecisionLigne = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_TARIF_ROAMING_FLOTTE", cdr.getDateConsommation(), attributEntrees);
        double cout_brut_int = Double.parseDouble(tableDecisionLigne.getValeurSortie("PRIX_MTC").toString()) * Util.floor(Util.arrondiSuperieur(cdr.getDuree() / Double.parseDouble(tableDecisionLigne.getValeurSortie("CADENCE").toString())), 0);
        return cout_brut_int;
    }

    private double getPromoRemise(PontJPA pontJPA, ControleurTableDecision controleurTableDecision, Usages cdr, double cout_brut) throws NumberFormatException {
        double cout_final = cout_brut;
        TableDecisionLigne tableDecisionLigne = new TableDecisionLigne();
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("CALL_TYPE", cdr.getCallType(), CritereEgalite.EGAL));
        attributEntrees.add(new ValeurAttributEntree("TAP_CODE", cdr.getTapeCode(), CritereEgalite.EGAL));

//        tableDecisionLigne = controleurTableDecision.getTableDecisionLigne(pontJPA, "TD_PROMO_ROAMING", attributEntrees, cdr.getDateConsommation());
        tableDecisionLigne = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_PROMO_ROAMING", cdr.getDateConsommation(), attributEntrees);
        if (!tableDecisionLigne.getValeursSorties().isEmpty() && Util.estDedans((Date) tableDecisionLigne.getValeurSortie("DATE_DEBUT"), (Date) tableDecisionLigne.getValeurSortie("DATE_FIN"), cdr.getDateConsommation())) {
            cout_final = calculeRemise(Double.parseDouble(tableDecisionLigne.getValeurSortie("REMISE").toString()), cout_brut);
        }

        return cout_final;
    }
}
