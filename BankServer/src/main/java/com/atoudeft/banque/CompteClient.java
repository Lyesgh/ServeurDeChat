package com.atoudeft.banque;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompteClient implements Serializable {
    private String numero;
    private String nip;
    private List<CompteBancaire> comptes;

    /**
     * Crée un compte-client avec un numéro et un nip.
     *
     * @param numero le numéro du compte-client
     * @param nip le nip
     */
    public CompteClient(String numero, String nip) {
        this.numero = numero;
        this.nip = nip;
        this.comptes = new ArrayList<>();
    }

    /**
     * Ajoute un compte bancaire au compte-client.
     *
     * @param compte le compte bancaire
     * @return true si l'ajout est réussi
     */
    public boolean ajouter(CompteBancaire compte) {
        return this.comptes.add(compte);
    }

    public List<CompteBancaire> getCompteBancaire(){
        return this.comptes;
    }
    public String getNip(){
        return this.nip;
    }
    public String getNumero(){
        return this.numero;
    }
}