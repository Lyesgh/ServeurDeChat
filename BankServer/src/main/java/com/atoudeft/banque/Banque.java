package com.atoudeft.banque;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Banque implements Serializable {
    private String nom;
    private List<CompteClient> comptes;

    public Banque(String nom) {
        this.nom = nom;
        this.comptes = new ArrayList<>();
    }

    /*
     * Recherche un compte-client à partir de son numéro.
     *
     * @param numeroCompteClient le numéro du compte-client
     * @return le compte-client s'il a été trouvé. Sinon, retourne null
     */
    public CompteClient getCompteClient(String numeroCompteClient) {
        CompteClient cc = null;
        if(comptes.isEmpty()!=true) {
            for (int x = 0; x < comptes.size(); x++) {
                if (comptes.get(x).getNumero().equals(numeroCompteClient) == true) {
                    cc = comptes.get(x);
                    break;
                }

            }
        }
        return cc;
    }

    /**
     * Vérifier qu'un compte-bancaire appartient bien au compte-client.
     *
     * @param numeroCompteBancaire numéro du compte-bancaire
     * @param numeroCompteClient    numéro du compte-client
     * @return  true si le compte-bancaire appartient au compte-client
     */
    public boolean appartientA(String numeroCompteBancaire, String numeroCompteClient) {
        boolean appartenance = false;
        CompteClient cc = null;
        for (int x = 0; x < comptes.size(); x++) {
            if (comptes.get(x).getNumero().equals(numeroCompteClient) == true) {
                cc = comptes.get(x);
                CompteBancaire cb;
                for(int j=0; j<cc.getCompteBancaire().size();j++){
                    cb = cc.getCompteBancaire().get(j);
                    if(cb.getNumero().equals(numeroCompteBancaire)==true){
                        appartenance = true;
                    }
                }
            }

        }
        return appartenance;
    }

    /**
     * Effectue un dépot d'argent dans un compte-bancaire
     *
     * @param montant montant à déposer
     * @param numeroCompte numéro du compte
     * @return true si le dépot s'est effectué correctement
     */
    public boolean deposer(double montant, String numeroCompte) {
        boolean deposage = false;
        CompteClient cc = null;
        for (int x = 0; x < comptes.size(); x++) {
            cc = comptes.get(x);
            CompteBancaire cb;
            for(int j=0; j<cc.getCompteBancaire().size();j++){
                cb = cc.getCompteBancaire().get(j);
                if(cb.getNumero().equals(numeroCompte)==true){
                    cb.crediter(montant);
                    deposage = true;
                }
            }
        }
        return deposage;
    }

    /**
     * Effectue un retrait d'argent d'un compte-bancaire
     *
     * @param montant montant retiré
     * @param numeroCompte numéro du compte
     * @return true si le retrait s'est effectué correctement
     */
    public boolean retirer(double montant, String numeroCompte) {
        boolean retrait = false;
        CompteClient cc = null;
        for (int x = 0; x < comptes.size(); x++) {
            cc = comptes.get(x);
            CompteBancaire cb;
            for(int j=0; j<cc.getCompteBancaire().size();j++){
                cb = cc.getCompteBancaire().get(j);
                if(cb.getNumero().equals(numeroCompte)==true){
                    cb.debiter(montant);
                    retrait = true;
                }
            }
        }
        return retrait;
    }

    /**
     * Effectue un transfert d'argent d'un compte à un autre de la même banque
     * @param montant montant à transférer
     * @param numeroCompteInitial   numéro du compte d'où sera prélevé l'argent
     * @param numeroCompteFinal numéro du compte où sera déposé l'argent
     * @return true si l'opération s'est déroulée correctement
     */
    public boolean transferer(double montant, String numeroCompteInitial, String numeroCompteFinal) {
        boolean transfter = false;
        CompteClient cc, ccf = null;
        for (int x = 0; x < comptes.size(); x++) {
            cc = comptes.get(x);
            CompteBancaire cb;
            for(int j=0; j<cc.getCompteBancaire().size();j++){
                cb = cc.getCompteBancaire().get(j);
                if(cb.getNumero().equals(numeroCompteFinal)==true){
                    this.deposer(montant, numeroCompteFinal);
                    transfter = true;
                }
                if(cb.getNumero().equals(numeroCompteInitial)==true){
                    this.retirer(montant, numeroCompteInitial);
                    //transfter = true;
                }
            }
        }
        return transfter;
    }

    /**
     * Effectue un paiement de facture.
     * @param montant montant de la facture
     * @param numeroCompte numéro du compte bancaire d'où va se faire le paiement
     * @param numeroFacture numéro de la facture
     * @param description texte descriptif de la facture
     * @return true si le paiement s'est bien effectuée
     */
    public boolean payerFacture(double montant, String numeroCompte, String numeroFacture, String description) {
        boolean facturepayee = false;
        CompteClient cc = null;
        for (int x = 0; x < comptes.size(); x++) {
            cc = comptes.get(x);
            CompteBancaire cb;
            for(int j=0; j<cc.getCompteBancaire().size();j++){
                cb = cc.getCompteBancaire().get(j);
                if(cb.getNumero().equals(numeroCompte)==true){
                    //cb.debiter(montant);
                    this.retirer(montant, numeroCompte);
                    facturepayee = true;
                }
            }
        }
        return facturepayee;
    }

    /**
     * Crée un nouveau compte-client avec un numéro et un nip et l'ajoute à la liste des comptes.
     *
     * @param numCompteClient numéro du compte-client à créer
     * @param nip nip du compte-client à créer
     * @return true si le compte a été créé correctement
     */
    public boolean ajouter(String numCompteClient, String nip) {
        boolean ajout = false;
        for(char c: numCompteClient.toCharArray()){
            if(Character.isLowerCase(c)){
                return false;
            }
            if(!Character.isLetterOrDigit(c)){
                return false;
            }
        }

        for(char c: nip.toCharArray()){
            if(!Character.isDigit(c)){
                return false;
            }
        }

        if (numCompteClient.length()>8 || numCompteClient.length()<6){
            return false;
        }

        if (nip.length()>8 || nip.length()<4){
            return false;
        }

        else{
            if(this.getCompteClient(numCompteClient)==null){
                CompteClient client = new CompteClient(numCompteClient, nip);
                List<CompteBancaire> ComptesDispo = client.getCompteBancaire();
                String compteChe = CompteCheque.genereNouveauNumero();

                do{
                    compteChe = CompteBancaire.genereNouveauNumero();
                }while(ComptesDispo.contains(compteChe)==true);

                if(!ComptesDispo.contains(compteChe)){
                    CompteCheque cc = new CompteCheque(compteChe, TypeCompte.CHEQUE);
                    client.ajouter(cc);
                }
                this.comptes.add(client);
                ajout = true;
            }

            else{
                ajout = false;
            }
        }
        return ajout;
    }

    /**
     * Retourne le numéro du compte-chèque d'un client à partir de son numéro de compte-client.
     *
     * @param numCompteClient numéro de compte-client
     * @return numéro du compte-chèque du client ayant le numéro de compte-client
     */
    public String getNumeroCompteParDefaut(String numCompteClient) {
        //À compléter : retourner le numéro du compte-chèque du compte-client.
        if(this.getCompteClient(numCompteClient)!=null){
            List<CompteBancaire> ComptesDispo = this.getCompteClient(numCompteClient).getCompteBancaire();

            if(!ComptesDispo.isEmpty()){
                return ComptesDispo.get(0).getNumero();
            }
        }

       return null;
    }

    public List<CompteClient> getComptes(){
        return this.comptes;
    }
}