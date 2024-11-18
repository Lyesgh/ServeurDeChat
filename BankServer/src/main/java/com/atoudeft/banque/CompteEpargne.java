package com.atoudeft.banque;

public class CompteEpargne extends CompteBancaire{
    private double tauxInteret = 5;
    private double solde;
    private final double FRAIS = 2, LIMITE=1000, CENT = 100;
    /**
     * Crée un compte bancaire.
     *
     * @param numero numéro du compte
     * @param type   type du compte
     */
    public CompteEpargne(String numero, TypeCompte type) {
        super(numero, type);
        this.solde= super.getSolde();
    }

    /**
     * Methode qui incremente le solde selon le taux d'interet donne.
     */
    public void ajouterInterets(){
        this.solde +=this.solde*tauxInteret/CENT;
    }

    /**
     *
     * @return retourne le taux d'interet du compte epagne.
     */
    public double getTauxInterets(){
        return tauxInteret;
    }

    public double getSolde(){
        return this.solde;
    }

    /**
     * Methode qui definit le taux d'interet du compte epargne.
     * @param taux la valeur du taux d'interet.
     */
    public void setTauxInterets(double taux){
        this.tauxInteret = taux;
    }

    /**
     * Methode qui permet de crediter au compte epargne.
     * @param montant le montant a crediter au compte epargne
     * @return retourne faux si le solde du compte est de 0.
     */
    @Override
    public boolean crediter(double montant) {
        if(this.solde>=0){
            this.solde = this.solde + montant;
            return true;
        }
        return false;
    }

    @Override
    public boolean debiter(double montant) {
        if(this.solde>0 && this.solde>montant){
            this.solde -= montant;
            if(this.solde<LIMITE){
                this.solde -= FRAIS;
            }
            return true;
        }
        else return false;
    }

    @Override
    public boolean payerFacture(String numeroFacture, double montant, String description) {
        return false;
    }

    @Override
    public boolean transferer(double montant, String numeroCompteDestinataire) {
        return false;
    }
}
