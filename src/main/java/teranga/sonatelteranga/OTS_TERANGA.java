/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teranga.sonatelteranga;
//teranga.sonatelteranga.OTS_TERANGA
import com.oml.ded.kaabu.offre.Offre;
import com.oml.ded.kaabu.parametre.OffreParametre;
import com.oml.ded.kaabu.util.Util.TYPE_OFFRE;
import com.oml.ded.kaabu.util.Util.TYPE_PARAMETRE;
import teranga.sonatelteranga.sousoffres.SOS_DONNEES_COMMUNES;
import teranga.sonatelteranga.sousoffres.SOS_OPERATIONS;
import teranga.sonatelteranga.sousoffres.SOS_OPTION_GPRS;
import teranga.sonatelteranga.sousoffres.SOS_SERVICES;
import teranga.sonatelteranga.sousoffres.SOS_TERANGA_ABONNEMENT;
import teranga.sonatelteranga.sousoffres.SOS_TERANGA_USAGE;
import teranga.sonatelteranga.sousoffres.SOS_XOCC;
import teranga.sonatelteranga.sousoffres.SVC_M_DATA;
import teranga.sonatelteranga.sousoffres.SVC_M_VOICE;

/**
 *
 * @author sidibe0091
 */
public class OTS_TERANGA extends Offre {

    /**
     * Création de l'offre orange pro
     */
    public OTS_TERANGA() {
        setCode("OTS_TERANGA");
        setDescription("Teranga");
        setOperateur("OSN");
        setProvisioning(true);
        setType(TYPE_OFFRE.POSTPAID);
        
        /**
         * Ajout des sous offres
         */
        
        addSousOffre(new SOS_TERANGA_ABONNEMENT());
        addSousOffre(new SOS_TERANGA_USAGE());        
        
        addSousOffre(new SOS_DONNEES_COMMUNES());
        addSousOffre(new SOS_OPERATIONS());
        addSousOffre(new SOS_OPTION_GPRS());
        addSousOffre(new SOS_SERVICES());        
        addSousOffre(new SOS_XOCC());
        addSousOffre(new SVC_M_VOICE());
        addSousOffre(new SVC_M_DATA());

        /**
         * Ajout des parametres
         */
        addParametre(new OffreParametre(getCode(), "MSISDN", "numéro d'abonné", TYPE_PARAMETRE.STRING, null, true, "", null));
//        addParametre(new OffreParametre(getCode(), "ENGAGEMENT", "Durée d'engement", TYPE_PARAMETRE.NUMBER, 12, true, null, null));
        addParametre(new OffreParametre(getCode(), "ID_OPERATEUR", "opérateur", TYPE_PARAMETRE.STRING, null, true, "ORANGE_SENEGAL", null));
        addParametre(new OffreParametre(getCode(), "OFFER_TYPE", "Type d'offre", TYPE_PARAMETRE.STRING, null, true, "MOB_POSTPAID", null));
//        addParametre(new OffreParametre(getCode(), "INITIALISATEUR", "Initialisateur Offre", TYPE_PARAMETRE.STRING, null, true, InitialisateurOffreTeranga.class.getName(), null, false));
    }
}
