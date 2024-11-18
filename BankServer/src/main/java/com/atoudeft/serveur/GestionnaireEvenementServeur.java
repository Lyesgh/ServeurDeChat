package com.atoudeft.serveur;

import com.atoudeft.banque.*;
import com.atoudeft.banque.serveur.ConnexionBanque;
import com.atoudeft.banque.serveur.ServeurBanque;
import com.atoudeft.commun.evenement.Evenement;
import com.atoudeft.commun.evenement.GestionnaireEvenement;
import com.atoudeft.commun.net.Connexion;

/**
 * Cette classe représente un gestionnaire d'événement d'un serveur. Lorsqu'un serveur reçoit un texte d'un client,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;

    /**
     * Construit un gestionnaire d'événements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un client.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        ServeurBanque serveurBanque = (ServeurBanque)serveur;
        Banque banque = serveurBanque.getBanque();
        ConnexionBanque cnx;
        String msg, typeEvenement, argument, numCompteClient, nip;
        String[] t;

        if (source instanceof Connexion) {
            cnx = (ConnexionBanque) source;

            typeEvenement = evenement.getType();
            cnx.setTempsDerniereOperation(System.currentTimeMillis());
            switch (typeEvenement) {

                /******************* COMMANDES GÉNÉRALES *******************/
                case "EXIT": //Ferme la connexion avec le client qui a envoyé "EXIT":
                    cnx.envoyer("END");
                    serveurBanque.enlever(cnx);
                    cnx.close();
                    break;

                case "LIST": //Envoie la liste des numéros de comptes-clients connectés :
                    cnx.envoyer("LIST " + serveurBanque.list());
                    break;

                /******************* COMMANDES DE GESTION DE COMPTES *******************/
                case "NOUVEAU": //Crée un nouveau compte-client :
                    argument = evenement.getArgument();
                    t = argument.split(":");
                    if (t.length<2) {
                        cnx.envoyer("NOUVEAU NO");
                    }
                    else {
                        numCompteClient = t[0];
                        nip = t[1];

                        if (cnx.getNumeroCompteClient() == null && banque.getCompteClient(numCompteClient)==null) {
                            banque.ajouter(numCompteClient, nip);
                            cnx.setNumeroCompteClient(numCompteClient);
                            cnx.setNumeroCompteActuel(banque.getNumeroCompteParDefaut(numCompteClient));
                            cnx.envoyer("NOUVEAU OK " + t[0] + " cree");
                        }
                        else
                            cnx.envoyer("NOUVEAU NO "+t[0]+" existe");
                    }
                    break;
                case "CONNECT":
                    argument = evenement.getArgument();
                    t = argument.split(":");

                    if(((ConnexionBanque)source).getNumeroCompteClient()!=null){
                        if(((ConnexionBanque)source).getNumeroCompteClient().equals(t[0])==true){

                        }
                        else{
                            cnx.envoyer("CONNECT NO "+((ConnexionBanque)source).getNumeroCompteClient()+" existe");
                        }
                    }

                    if(((ConnexionBanque)source).getNumeroCompteClient()==null){
                        if(banque.getCompteClient(t[0])!=null && banque.getCompteClient(t[0]).getNip().equals(t[1])==true){
                            cnx.setNumeroCompteClient(t[0]);
                            cnx.setNumeroCompteActuel(banque.getNumeroCompteParDefaut(t[0]));
                            ((ConnexionBanque)source).setTempsDerniereOperation(System.currentTimeMillis());
                            cnx.envoyer("CONNECT OK " + t[0] + " reconnecte!");
                        }
                        else{
                            if(((ConnexionBanque) source).getNumeroCompteClient()==t[0]){
                                cnx.envoyer("CONNECT NO " + cnx.getNumeroCompteClient() + " deja connecte!");
                            }
                        }
                    }
                    if(((ConnexionBanque)source).getNumeroCompteClient()==null && banque.getCompteClient(t[0])!=null && banque.getCompteClient(t[0]).getNip().equals(t[1])!=true){
                        cnx.envoyer("CONNECT NO " + t[1] + " NIP Invalide!");
                    }
                    else{
                        if(banque.getCompteClient(t[0])==null){
                            cnx.envoyer("CONNECT NO " + t[0] + " INEXISTANT! Creez avec 'NOUVEAU' !");
                        }

                    }
                    break;

                case "EPARGNE":
                    String compteEpargne="";
                    if(cnx.getNumeroCompteClient() == null){
                        cnx.envoyer("EPARGNE NO ");
                    }

                    if(cnx.getNumeroCompteClient()!=null){
                        for(int i=0; i<(banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().size(); i++){
                            if((banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().get(i).getType() == TypeCompte.EPARGNE){
                                compteEpargne = (banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().get(i).getNumero();
                                break;
                            }
                        }
                        if(compteEpargne == ""){
                            banque.getCompteClient(cnx.getNumeroCompteClient()).ajouter(new CompteEpargne(CompteEpargne.genereNouveauNumero(), TypeCompte.EPARGNE));
                            cnx.envoyer("EPARGNE OK ");
                        }
                        else{
                            cnx.envoyer("EPARGNE NO  Compte epargne existe");
                        }
                    }

                    break;

                case "SELECT":
                    argument = evenement.getArgument();
                    if(cnx.getNumeroCompteActuel()==null){
                        cnx.envoyer("SELECT NO | Pas connecte");
                        break;
                    }

                    if(cnx.getNumeroCompteActuel()!=null && argument.equals("epargne")==true && (banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().size()<=1){
                        cnx.envoyer("SELECT EPARGNE NO | Indisponible");
                        break;
                    }

                    else{
                        CompteBancaire cb;
                        if(argument.equals("epargne")==true){
                            cnx.setNumeroCompteActuel((banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().get(1).getNumero());
                            cnx.envoyer("SELECT EPARGNE OK");
                            break;
                        }

                        if(argument.equals("cheque")==true){
                            cnx.setNumeroCompteActuel((banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().get(0).getNumero());
                            cnx.envoyer("SELECT CHEQUE OK");
                            break;
                        }
                        else{cnx.envoyer("SELECT NO | COMMANDE INVALIDE!");}
                    }

                    break;

                case "DEPOT":
                    argument = evenement.getArgument();
                    if(cnx.getNumeroCompteActuel()==null){
                        cnx.envoyer("DEPOT NO | Pas connecte");
                        break;
                    }

                    if(cnx.getNumeroCompteActuel()!=null){
                        try{
                            double montant;
                            montant = Double.parseDouble(argument);
                            banque.deposer(montant, cnx.getNumeroCompteActuel());
                            banque.appartientA((banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().get(0).getNumero(), cnx.getNumeroCompteClient());

                            (banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().get(0).getHistorique().ajouterDebut(new OperationDepot(montant, typeEvenement));
                            System.out.println("le montant: "+montant);
                            cnx.envoyer("DEPOT OK | Montant Depose");
                            System.out.println("solde du compte:  "+(banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().get(0).getSolde());
                        } catch (NumberFormatException e) {
                            cnx.envoyer("DEPOT NO | Montant Invalide");
                            ///throw new RuntimeException(e);
                        }
                    }
                    break;

                case "RETRAIT":
                    argument = evenement.getArgument();
                    if(cnx.getNumeroCompteActuel()==null){
                        cnx.envoyer("RETRAIT NO | Pas connecte");
                        break;
                    }

                    if(cnx.getNumeroCompteActuel()!=null){
                        try{
                            double montant;
                            montant = Double.parseDouble(argument);
                            banque.retirer(montant, cnx.getNumeroCompteActuel());
                            System.out.println("le montant: "+montant);
                            (banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().get(0).getHistorique().ajouterDebut(new OperationRetrait(montant, typeEvenement));
                            cnx.envoyer("RETRAIT OK | Montant retire");
                        } catch (NumberFormatException e) {
                            cnx.envoyer("RETRAIT NO | Montant Invalide");
                            ///throw new RuntimeException(e);
                        }
                    }
                    break;
                case "TRANSFERER":
                    argument = evenement.getArgument();
                    t = argument.split(" ");

                    if(cnx.getNumeroCompteActuel()==null){
                        cnx.envoyer("TRANSFERER NO | Pas connecte");
                        break;
                    }

                    if(cnx.getNumeroCompteActuel()!=null){
                        try{
                            double montant;
                            montant = Double.parseDouble(t[0]);
                            boolean transferReussi = banque.transferer(montant, String.valueOf(banque.getCompteClient(cnx.getNumeroCompteClient()).getCompteBancaire().get(0)), t[1]);
                            if(transferReussi==true){
                                (banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().get(0).getHistorique().ajouterDebut(new OperationTransfer(montant, typeEvenement));
                                cnx.envoyer("TRANSFERER OK");
                            }
                            else{
                                cnx.envoyer("TRANSFERER NO");
                            }

                        } catch (NumberFormatException e) {
                            cnx.envoyer("TRANSFERER NO | Montant Invalide");
                        }
                    }
                    break;
                case "FACTURE":

                    argument = evenement.getArgument();
                    t = argument.split(" ");

                    if(cnx.getNumeroCompteActuel()==null){
                        cnx.envoyer("FACTURE NO | Pas connecte");
                        break;
                    }

                    if(cnx.getNumeroCompteActuel()!=null){
                        try{
                            double montant;
                            montant = Double.parseDouble(t[0]);
                            boolean facturer = banque.payerFacture(montant, String.valueOf(banque.getCompteClient(cnx.getNumeroCompteClient()).getCompteBancaire().get(0).getNumero()), t[1], String.valueOf(t[2]));
                            if(facturer == true){
                                (banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().get(0).getHistorique().ajouterDebut(new OperationFacture(montant, typeEvenement));
                                cnx.envoyer("FACTURE OK");
                            }
                            else{
                                cnx.envoyer("FACTURE NO");
                            }

                        } catch (NumberFormatException e) {
                            cnx.envoyer("FACTURE NO | FORMAT / Montant Invalide");
                        }
                    }
                    break;

                case "HIST"://afficher l'historique des operations effectuees dans un compte bancaire.
                    (banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().get(0).getHistorique().toString();
                    cnx.envoyer((banque.getCompteClient(cnx.getNumeroCompteClient())).getCompteBancaire().get(0).getHistorique().toString());
                    break;
                /******************* TRAITEMENT PAR DÉFAUT *******************/

                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}