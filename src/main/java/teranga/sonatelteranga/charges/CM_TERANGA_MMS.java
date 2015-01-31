/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga.charges;

import com.oml.ded.kaabu.charge.Charge;
import com.oml.ded.kaabu.client.Contrat;
import com.oml.ded.kaabu.collecte.Usages;
import com.oml.ded.kaabu.conso.Conso;
import com.oml.ded.kaabu.jpacontroller.PontJPA;
import com.oml.ded.kaabu.jpacontroller.PoolConnexion;
import com.oml.ded.kaabu.util.Util;
import com.oml.ded.kaabu.util.Util.TYPE_CHARGE;
import com.oml.ded.kaabu.util.Util.TYPE_EVENT;
import java.util.ArrayList;
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
public class CM_TERANGA_MMS extends Charge {

    public CM_TERANGA_MMS(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, aProvisionner);
        setDescription("Usage MMS");
        setEtat(Util.ETAT.ACTIF);
    }

    @Override
    public List<Conso> getConso(Usages cdr) {
        List<Conso> lc = new ArrayList<Conso>();
        Usages cdr2 = cdr;
        ControleurTableDecision controleurTableDecision = new ControleurTableDecision();
        PontJPA pontJPA = PoolConnexion.getInstance().getPontJPAKaabu();
        if (cdr.getService().equals("mms")) {
            Map zoneMap = getZoneDestinationUsage(pontJPA, controleurTableDecision, cdr);
            Map tarifMap = getTarifServiceData(pontJPA, controleurTableDecision, zoneMap.get("zone").toString(), cdr);
            if (tarifMap.get("tarif") == null || Double.parseDouble(tarifMap.get("tarif") + "") < 0) { // le tarif n'existe pas
                cdr2.setCout(-1); // filter le CDR
            } else {
                cdr2.setCout(cdr.getVolumeKo() * Double.parseDouble(tarifMap.get("tarif").toString()) + Double.parseDouble(tarifMap.get("tarif_appel").toString()));
            }

            Conso c = getNewConso();
            c.setMontantHT(cdr2.getCout());
            c.setMontantTTC((c.getMontantHT() * getTVA() + c.getMontantHT()));
            setConso(c, cdr);
            lc.add(c);
        }
        PoolConnexion.getInstance().putPontJPA(pontJPA);
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

    private Map getTarifServiceData(PontJPA pontJPA, ControleurTableDecision controleurTableDecision, String zone, Usages cdr) {
        Map<String, Object> sortie = new HashMap<String, Object>();
        List<ValeurAttributEntree> attributEntrees = new ArrayList<ValeurAttributEntree>();
        attributEntrees.add(new ValeurAttributEntree("ZONE", zone, CritereEgalite.EGAL));
        TableDecisionLigne tableDecisionLigne = cdr.getSessionBigTableDecision().getTableDecisionLigne(pontJPA, controleurTableDecision, "TD_TARIF_MMS_ZONE", cdr.getDateConsommation(), attributEntrees);
        sortie.put("tarif_appel", tableDecisionLigne.getValeurSortie("TARIF_APPEL"));
        sortie.put("tarif", tableDecisionLigne.getValeurSortie("TARIF"));
        return sortie;
    }

    /**
     * Renvoie la Zone pour un usage mms
     *
     * @param cdr
     * @return (zone, detail, description, type, prefix, agregat, legend)
     */
    private Map getZoneDestinationUsage(PontJPA pontJPA, ControleurTableDecision controleurTableDecision, Usages cdr) {
        TableDecisionLigne tableDecisionLigne = null;
        List<ValeurAttributEntree> attributEntrees = null;
        Map<String, Object> sortie = new HashMap<String, Object>();
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
        return sortie;
    }
}
