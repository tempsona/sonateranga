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
import com.oml.ded.kaabu.util.Util;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import com.oml.ded.kaabu.util.Util.TYPE_PARAMETRE;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CM_TERANGA_GPRS extends Charge {

    public CM_TERANGA_GPRS(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
        setDescription("Usage GPRS");
        setEtat(Util.ETAT.ACTIF);
        addCompteur(new Compteur("forfait_bb", "Forfait blackberry", 1024000));
        addCompteur(new Compteur("forfait_iphone", "Forfait iphone", 1024000));
        addCompteur(new Compteur("forfait_data", "Forfait Data", 0));
        addCompteur(new Compteur("suivi_conso_data", "Compteur Data", 0));
        ChargeParametre cp2 = new ChargeParametre(code, "DATA", "LIST=N-Non;Y-Oui", TYPE_PARAMETRE.STRING, null, false, "N", null, "EXTERNE");
        cp2.setLibelle("Forfait Data");
        addParametre(cp2);
    }

    @Override
    public List<Conso> getConso(Usages cdr) {
        List<Conso> lc = new ArrayList<Conso>();
        Usages cdr2 = cdr;
//        System.out.println("cdr volume = " + cdr.getVolumeKo());
        PontJPA pontJPA = null;
        try {
            pontJPA = PoolConnexion.getInstance().getPontJPAKaabu();
            if (cdr.getService().equals("data")) {
                Compteur bb_c = cdr.getPont().getContrat().getCompteur("forfait_bb");
                Compteur data_c = cdr.getPont().getContrat().getCompteur("forfait_data");
                String etat_bb = "INACTIF";
                String etat_data = "INACTIF";

                /**
                 * ***Recupère l'état des charges iphone, blackberry et data
                 */
                try {
                    if (bb_c != null) {
                        ChargeParametre cp = bb_c.getContrat().getChargeParametre("ETAT", "SVC_M_BLBE");
                        if (cp != null) {
                            etat_bb = cp.getValeurChaine();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (data_c != null) {
                        ChargeParametre cp = data_c.getContrat().getChargeParametre("ETAT", "CM_FORFAIT_DATA");
                        if (cp != null) {
                            etat_data = cp.getValeurChaine();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Map tarifMap = getTarifServiceData(pontJPA, cdr);
                if (tarifMap.get("tarif") == null || Double.parseDouble(tarifMap.get("tarif") + "") < 0) { // le tarif n'existe pas
                    cdr2.setCout(-1); // filter le CDR
                } else {
                    cdr2.setCout(cdr.getVolumeKo() * Double.parseDouble(tarifMap.get("tarif").toString()) + Double.parseDouble(tarifMap.get("tarif_appel").toString()));
                }

                if (bb_c != null && Util.valeurCompteur(bb_c) > 0 && etat_bb.equals("ACTIF")) { // si blackberry est activé
                    Map mont = Util.PartageurMontantForfaitData(bb_c, cdr2);
                    Conso c = getNewConso();
                    c.setMontantHT(Double.parseDouble(mont.get("montant_hors_forfait").toString()) * Double.parseDouble(tarifMap.get("tarif").toString()) + Double.parseDouble(tarifMap.get("tarif_appel").toString()));
                    c.setMontantTTC((c.getMontantHT() * getTVA() + c.getMontantHT()));
                    setConso(c, cdr);
                    Util.decrementCompteur(bb_c, Double.parseDouble(mont.get("montant_forfait").toString()));
                    c.addDetail(new DetailConso("forfait_bb", "volume deduit du forfait", TYPE_PARAMETRE.NUMBER, -(int) Double.parseDouble(mont.get("montant_forfait").toString())));
                    lc.add(c);
                } else if (data_c != null && Util.valeurCompteur(data_c) > 0 && etat_data.equals("ACTIF")) { // si data est activé
                    Map mont = Util.PartageurMontantForfaitData(data_c, cdr2);
                    Conso c = getNewConso();
                    c.setMontantHT(Double.parseDouble(mont.get("montant_hors_forfait").toString()) * Double.parseDouble(tarifMap.get("tarif").toString()) + Double.parseDouble(tarifMap.get("tarif_appel").toString()));
                    c.setMontantTTC((c.getMontantHT() * getTVA() + c.getMontantHT()));
                    setConso(c, cdr);
                    Util.decrementCompteur(data_c, Double.parseDouble(mont.get("montant_forfait").toString()));
                    c.addDetail(new DetailConso("forfait_data", "volume deduit du forfait", TYPE_PARAMETRE.NUMBER, -(int) Double.parseDouble(mont.get("montant_forfait").toString())));
                    lc.add(c);
                } else {
                    Conso c = getNewConso();
                    c.setMontantHT(cdr2.getCout());
                    c.setMontantTTC((c.getMontantHT() * getTVA() + c.getMontantHT()));
                    setConso(c, cdr);
                    lc.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(CM_TERANGA_GPRS.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            PoolConnexion.getInstance().putPontJPA(pontJPA);
        }

        return lc;
    }

    private void setConso(Conso c, Usages cdr) {
        c.setUsage_id(cdr.getId());
        c.setAgregat("GPR");
        c.setLegend("12");
        c.setAppelant(cdr.getUtilisateur());
        c.setCorrespondant(cdr.getCorrespondant());
        c.setDuree(cdr.getDuree());
        c.setZone("");
        c.setDateConso(cdr.getDateConsommation());
        c.setUrl(cdr.getUrl());
        c.setType_usage(cdr.getType());
        c.setService_usage(cdr.getService());
        c.setVolume_ko(cdr.getVolumeKo());
        c.setService_gprs(cdr.getCallType());
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
        return new ArrayList<Conso>();
    }

    private Map getTarifServiceData(PontJPA pontJPA, Usages cdr) {
        Map<String, Object> sortie = new HashMap<String, Object>();
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("SERVICE_CLASS", cdr.getCallType(), CritereEgalite.EGAL));
        if (cdr.getUrl() == null || cdr.getUrl().trim().equals("")) {
            cdr.setUrl("http");
        }
        attributEntrees.add(new ValeurAttributEntree("URL", cdr.getUrl(), CritereEgalite.CONTIENT));
        // promotion
        ControleurTableDecision controleurTableDecision = new ControleurTableDecision();
        TableDecision tableDecision = cdr.getSessionBigTableDecision().getTableDecision(pontJPA, controleurTableDecision, "TD_TARIF_SERVICE_PROMOTION_DATA");
        TableDecisionInstanceChronologie tdic = tableDecision.getTableDecisionInstanceChronologie(cdr.getDateConsommation());
        TableDecisionLigne tableDecisionLignep = (tdic == null) ? null : tdic.getTableDecisionLigneVAL(attributEntrees);
        if (tableDecisionLignep != null && !tableDecisionLignep.getValeursSorties().isEmpty() && Util.estDedans((Date) tableDecisionLignep.getValeurSortie("DATE_DEBUT"), (Date) tableDecisionLignep.getValeurSortie("DATE_FIN"), cdr.getDateConsommation())) {
            sortie.put("tarif", tableDecisionLignep.getValeurSortie("TARIF"));
            sortie.put("tarif_appel", tableDecisionLignep.getValeurSortie("TARIF_APPEL"));
        } else { // hors promotion
            attributEntrees = new ArrayList<ValeurAttributEntree>();
            attributEntrees.add(new ValeurAttributEntree("SERVICE_CLASS", cdr.getCallType(), CritereEgalite.EGAL));
            attributEntrees.add(new ValeurAttributEntree("URL", cdr.getUrl(), CritereEgalite.CONTIENT));
            TableDecision tableDecision2 = cdr.getSessionBigTableDecision().getTableDecision(pontJPA, controleurTableDecision, "TD_TARIF_SERVICE_DATA");
            TableDecisionInstanceChronologie tdic2 = tableDecision2.getTableDecisionInstanceChronologie(cdr.getDateConsommation());
            TableDecisionLigne tableDecisionLigne = tdic2.getTableDecisionLigneVAL(attributEntrees);
            sortie.put("tarif", tableDecisionLigne.getValeurSortie("TARIF"));
            sortie.put("tarif_appel", tableDecisionLigne.getValeurSortie("TARIF_APPEL"));
        }
        return sortie;
    }
}
