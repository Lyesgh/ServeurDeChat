package com.atoudeft.banque;

public class CompteCheque extends CompteBancaire {
    private double solde;
    public CompteCheque(String numero, TypeCompte type){
        super(numero, type);
        solde = super.getSolde();
    }
    @Override
    public boolean crediter(double montant) {
        if(this.solde>=0){
            this.solde = solde + montant;
            return true;
        }
        return false;
    }

    public double getSolde(){
        return this.solde;
    }

    @Override
    public boolean debiter(double montant) {
        if(this.solde>0 && this.solde>montant){
            this.solde-=montant;
            return true;
        }
        return false;
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
