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
public class CM_TERANGA_SMS extends Charge {

    public CM_TERANGA_SMS(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
        setDescription("Usage SMS");
        setEtat(Util.ETAT.ACTIF);
//        addCompteur(new Compteur("bonus_sms_iphone", "Compteur de forfait sms iphone", 150));
//        addCompteur(new Compteur("conso_voix_mensuelle", "Suivi conso", 0));
//        
//        ChargeParametre cp3 = new ChargeParametre(code, "SMS", "LIST=N-Non;Y-Oui", TYPE_PARAMETRE.STRING, null, false, "N", null, "EXTERNE");
//        cp3.setLibelle("Forfait SMS");
//        addParametre(cp3);

    }

    @Override
    public List<Conso> getConso(Usages cdr) {
        List<Conso> lc = new ArrayList<Conso>();
        if (cdr.getService().equals("sms")) {
            PontJPA pontJPA = PoolConnexion.getInstance().getPontJPAKaabu();
            ControleurTableDecision controleurTableDecision = new ControleurTableDecision();
            Map zoneMap = getZoneDestinationUsageSMS(pontJPA, controleurTableDecision, cdr);
            Map tarifMap = getTarifZoneSMS(pontJPA, controleurTableDecision, zoneMap.get("zone").toString(), cdr);
            PoolConnexion.getInstance().putPontJPA(pontJPA);
            if (zoneMap.get("type").equals("null")) {
                // InterdictionUsage
                lc.add(Util.InterdictionUsage(this, cdr, "La zone n'existe pas"));
            }

            // SMS Normal
            Map tarif = getCoutSMSNormal(tarifMap, cdr);
            if (Double.parseDouble(tarif.get("prix").toString()) > -1) { // le tarif existe
                // le tarif donné par la table des tarifs
                Conso c = getNewConso();
                c.setUsage_id(cdr.getId());
                double montant = Double.parseDouble(tarif.get("prix").toString());
                c.setMontantHT(montant);
                c.setMontantTTC(c.getMontantHT() * getTVA() + c.getMontantHT());
                c.setAgregat("SOG");
                c.setLegend("9");
                c.setAppelant(cdr.getUtilisateur());
                c.setCorrespondant(cdr.getCorrespondant());
                c.setDuree(cdr.getDuree());
                c.setZone(zoneMap.get("zone").toString());
                c.setDateConso(cdr.getDateConsommation());
                c.setUrl(cdr.getUrl());
                c.setType_usage(cdr.getType());
                c.setService_usage(cdr.getService());
                c.addDetail(new DetailConso("crenau", "", TYPE_PARAMETRE.STRING, null, true, tarif.get("crenau").toString(), null));
                c.addDetail(new DetailConso("bonus_sms_iphone", "Bonus utilisé", TYPE_PARAMETRE.NUMBER, 0));
                lc.add(c);
            } else {  // le tarif n'existe pas
                lc.add(Util.InterdictionUsage(this, cdr, "Le tarif n'existe pas"));
            }
        }
        return lc;
    }

    @Override
    public List<Conso> getConso(Contrat contrat) {
        return new ArrayList<Conso>();
    }

    /**
     * Renvoie la Zone pour un usage sms(spécial ou normal)
     * @param cdr
     * @return (zone, detail, description, type, prefix, agregat, legend)
     */
    private Map getZoneDestinationUsageSMS(PontJPA pontJPA, ControleurTableDecision controleurTableDecision, Usages cdr) {
        TableDecisionLigne tableDecisionLigne;
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        Map<String, Object> sortie = new HashMap<String, Object>();
        if (cdr.getService().equals("sms")) {
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
        return sortie;
    }

    /**
     *Methode qui retourne le tarif par rapport à la zone d'un usage SMS.
     * @param zone
     * @param cdr
     * @return (description,  prix_HC_SMS,  prix_HP_SMS, prix_HN_SMS)
     */
    private Map getTarifZoneSMS(PontJPA pontJPA, ControleurTableDecision controleurTableDecision, String zone, Usages cdr) {
        // Appel à la table de décision des codes comptables
        Map<String, Object> sortie = new HashMap<String, Object>();
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("ZONE", zone, CritereEgalite.EGAL));
        // promotion
//        TableDecisionLigne tableDecisionLignep = controleurTableDecision.getTableDecisionLigne(pontJPA, "TD_TARIF_ZONE_PROMOTION_SMS", attributEntrees, cdr.getDateConsommation());
        TableDecisionLigne tableDecisionLignep = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_TARIF_ZONE_PROMOTION_SMS", cdr.getDateConsommation(), attributEntrees);

        if (tableDecisionLignep != null && !tableDecisionLignep.getValeursSorties().isEmpty() && Util.estDedans((Date) tableDecisionLignep.getValeurSortie("DATE_DEBUT"), (Date) tableDecisionLignep.getValeurSortie("DATE_FIN"), cdr.getDateConsommation())) {
            sortie.put("description", tableDecisionLignep.getValeurSortie("DESCRIPTION"));
            sortie.put("prix_HC_SMS", tableDecisionLignep.getValeurSortie("PRIX_HC_SMS"));
            sortie.put("prix_HP_SMS", tableDecisionLignep.getValeurSortie("PRIX_HP_SMS"));
            sortie.put("prix_HN_SMS", tableDecisionLignep.getValeurSortie("PRIX_HN_SMS"));
        } else { // hors promotion
            attributEntrees = new ArrayList<ValeurAttributEntree>();
            attributEntrees.add(new ValeurAttributEntree("ZONE", zone, CritereEgalite.EGAL));
//            TableDecisionLigne tableDecisionLigne = controleurTableDecision.getTableDecisionLigne(pontJPA, "TD_TARIF_SMS_ZONE", attributEntrees, cdr.getDateConsommation());
            TableDecisionLigne tableDecisionLigne = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_TARIF_SMS_ZONE", cdr.getDateConsommation(), attributEntrees);
            sortie.put("description", tableDecisionLigne.getValeurSortie("DESCRIPTION"));
            sortie.put("prix_HC_SMS", tableDecisionLigne.getValeurSortie("PRIX_HC_SMS"));
            sortie.put("prix_HP_SMS", tableDecisionLigne.getValeurSortie("PRIX_HP_SMS"));
            sortie.put("prix_HN_SMS", tableDecisionLigne.getValeurSortie("PRIX_HN_SMS"));
        }
        return sortie;
    }

    /**
     * Methode qui retourne le cout d'un sms normal
     * @param tarif
     * @param cdr
     * @return
     */
    private Map getCoutSMSNormal(Map tarif, Usages cdr) {
        Map<String, Object> sortie = new HashMap<String, Object>();

        sortie.put("prix", Util.floor(Double.parseDouble(tarif.get("prix_HP_SMS").toString()), 6));
        sortie.put("crenau", "P");

        return sortie;
    }
}
