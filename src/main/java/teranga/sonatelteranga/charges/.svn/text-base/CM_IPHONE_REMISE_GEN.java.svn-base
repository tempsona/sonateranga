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
import com.oml.ded.kaabu.jpacontroller.CompteurJpaController;
import com.oml.ded.kaabu.jpacontroller.exceptions.NonexistentEntityException;
import com.oml.ded.kaabu.parametre.ChargeParametre;
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

/**
 *
 * @author Administrateur
 */
public class CM_IPHONE_REMISE_GEN extends Charge {

    public CM_IPHONE_REMISE_GEN(String code, TYPE_CHARGE type, TYPE_EVENT declencheur, boolean aProvisionner) {
        super(code, type, declencheur, true);
        setDescription("Remise iPhone sur consommations mensuelles");

      //  addParametre(new ChargeParametre(this.getCode(), "CODE_FO", "Famille d'offres", TYPE_PARAMETRE.STRING, null, true, "GD", null, "INTERNE"));
        addCompteur(new Compteur("report_forfait_iphone", "Montant du report de forfait des mois précédents", 0));
        addCompteur(new Compteur("mois_courant_iphone", "Mois courant", 1));
        addCompteur(new Compteur("conso_voix_mensuelle", "Consommation voix mensuelle", 0));
    }

    @Override
    public List<Conso> getConso(Usages arg0) {
        return java.util.Collections.EMPTY_LIST;
    }

    @Override
    public List<Conso> getConso(Contrat arg0) {
        List<Conso> lc = new ArrayList<Conso>();

        try {
            Compteur compteur_report_forfait_iphone = getCompteur("report_forfait_iphone");
            Compteur compteur_mois_courant_iphone = getCompteur("mois_courant_iphone");
            Compteur compteur_conso_voix_mensuelle = getCompteur("conso_voix_mensuelle");

            if (getType().equals(TYPE_CHARGE.RECURRENT)) {
                Conso c = getNewConso();
                c.setCompte(arg0.getCompte());
                c.setOffre(arg0.getCodeOffre());
                c.setDateConso(new Date());

//                Map compte_comptable = getCodeComptable("code_remise");
//                c.addDetail(new DetailConso("CODE_COMPTABLE", "", TYPE_PARAMETRE.STRING, null, true, compte_comptable.get("CODE_COMPTABLE").toString(), null));
                c.addDetail(new DetailConso("agregat", "", TYPE_PARAMETRE.STRING, null, true, "FF25", null));
                c.addDetail(new DetailConso("remise", "", TYPE_PARAMETRE.STRING, null, true, "iphone", null));

                CompteurJpaController ct = new CompteurJpaController();

                if (compteur_mois_courant_iphone.getValeurCourante() <= 12) {
                    double montant_forfait = Double.parseDouble(getValeurParametre("montant_forfait").toString());
//                    if (compteur_conso_voix_mensuelle.getValeurCourante() < Double.parseDouble(getValeurParametre("montant_forfait").toString())) {
                    if (compteur_conso_voix_mensuelle.getValeurCourante() < montant_forfait) {
//                        double report = Double.parseDouble(getValeurParametre("montant_forfait").toString()) - compteur_conso_voix_mensuelle.getValeurCourante();
                        double report = montant_forfait - compteur_conso_voix_mensuelle.getValeurCourante();
                        c.setMontantHT(-1 * compteur_conso_voix_mensuelle.getValeurCourante() + 0);
                        compteur_report_forfait_iphone.setValeurCourante((int) (compteur_report_forfait_iphone.getValeurCourante() + report));
                        compteur_mois_courant_iphone.setValeurCourante(compteur_mois_courant_iphone.getValeurCourante() + 1);

                        compteur_report_forfait_iphone.setDateChangement(new Date());
                        compteur_mois_courant_iphone.setDateChangement(new Date());

                        ct.edit(compteur_report_forfait_iphone);
                        ct.edit(compteur_mois_courant_iphone);
                    } else {
                        if (compteur_report_forfait_iphone.getValeurCourante() >= 0) {
//                            double forfait_plus_report = Double.parseDouble(getValeurParametre("montant_forfait").toString()) + compteur_report_forfait_iphone.getValeurCourante();
                            double forfait_plus_report = montant_forfait + compteur_report_forfait_iphone.getValeurCourante();
                            if (compteur_conso_voix_mensuelle.getValeurCourante() > forfait_plus_report) {
                                c.setMontantHT(forfait_plus_report * (-1) + 0);

                                compteur_report_forfait_iphone.setValeurCourante(0);
                                compteur_mois_courant_iphone.setValeurCourante(compteur_mois_courant_iphone.getValeurCourante() + 1);

                                compteur_report_forfait_iphone.setDateChangement(new Date());
                                compteur_mois_courant_iphone.setDateChangement(new Date());

                                ct.edit(compteur_report_forfait_iphone);
                                ct.edit(compteur_mois_courant_iphone);
                            } else {
                                double report = forfait_plus_report- compteur_conso_voix_mensuelle.getValeurCourante();
                                c.setMontantHT(compteur_conso_voix_mensuelle.getValeurCourante() * (-1) + 0);

                                compteur_report_forfait_iphone.setValeurCourante((int) report);
                                compteur_mois_courant_iphone.setValeurCourante(compteur_mois_courant_iphone.getValeurCourante() + 1);

                                compteur_report_forfait_iphone.setDateChangement(new Date());
                                compteur_mois_courant_iphone.setDateChangement(new Date());

                                ct.edit(compteur_report_forfait_iphone);
                                ct.edit(compteur_mois_courant_iphone);
                            }
                        } else {
                            //Interdiction
                        }
                    }
                } else {
                    if (compteur_mois_courant_iphone.getValeurCourante() == 13) {
                        if (compteur_conso_voix_mensuelle.getValeurCourante() >= compteur_report_forfait_iphone.getValeurCourante()) {
                            c.setMontantHT(compteur_report_forfait_iphone.getValeurCourante() * (-1) + 0);

                            compteur_report_forfait_iphone.setValeurCourante(0);
                            compteur_mois_courant_iphone.setValeurCourante(compteur_mois_courant_iphone.getValeurCourante() + 1);

                            compteur_report_forfait_iphone.setDateChangement(new Date());
                            compteur_mois_courant_iphone.setDateChangement(new Date());

                            ct.edit(compteur_report_forfait_iphone);
                            ct.edit(compteur_mois_courant_iphone);
                        } else {
                            double report = compteur_report_forfait_iphone.getValeurCourante() - compteur_conso_voix_mensuelle.getValeurCourante();
                            c.setMontantHT(compteur_conso_voix_mensuelle.getValeurCourante() * (-1) + 0);

                            compteur_report_forfait_iphone.setValeurCourante((int) report);
                            compteur_mois_courant_iphone.setValeurCourante(compteur_mois_courant_iphone.getValeurCourante() + 1);

                            compteur_report_forfait_iphone.setDateChangement(new Date());
                            compteur_mois_courant_iphone.setDateChangement(new Date());

                            ct.edit(compteur_report_forfait_iphone);
                            ct.edit(compteur_mois_courant_iphone);
                        }
                    } else {
                        if (compteur_mois_courant_iphone.getValeurCourante() == 14) {
                            if (compteur_conso_voix_mensuelle.getValeurCourante() >= compteur_report_forfait_iphone.getValeurCourante()) {
                                c.setMontantHT(compteur_report_forfait_iphone.getValeurCourante() * (-1) + 0);

                                compteur_report_forfait_iphone.setValeurCourante(0);
                                compteur_mois_courant_iphone.setValeurCourante(compteur_mois_courant_iphone.getValeurCourante() + 1);

                                compteur_report_forfait_iphone.setDateChangement(new Date());
                                compteur_mois_courant_iphone.setDateChangement(new Date());

                                ct.edit(compteur_report_forfait_iphone);
                                ct.edit(compteur_mois_courant_iphone);
                            } else {
                                c.setMontantHT(compteur_conso_voix_mensuelle.getValeurCourante() * (-1) + 0);

                                compteur_report_forfait_iphone.setValeurCourante(0);
                                compteur_mois_courant_iphone.setValeurCourante(compteur_mois_courant_iphone.getValeurCourante() + 1);

                                compteur_report_forfait_iphone.setDateChangement(new Date());
                                compteur_mois_courant_iphone.setDateChangement(new Date());

                                ct.edit(compteur_report_forfait_iphone);
                                ct.edit(compteur_mois_courant_iphone);
                            }
                        } else if (compteur_mois_courant_iphone.getValeurCourante() != 14) {
                            // gratuit
                        }
                    }
                }
                lc.add(c);
            }
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CM_IPHONE_REMISE_GEN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CM_IPHONE_REMISE_GEN.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lc;
    }

    private Map getCodeComptable(String code_resil) {
        Map<String, Object> sortie = new HashMap<String, Object>();
        sortie.put("CODE_COMPTABLE", getValeurParametre(code_resil));
        return sortie;
    }
}
