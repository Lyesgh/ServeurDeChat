package com.atoudeft.banque;

public class PileChainee {
    private Noeud tete = null;
    private int nbOperations = 0;

    // Constructeur par défaut
    public PileChainee() {

    }

    // accesseurs
    public boolean estVide()    { return nbOperations == 0; }
    public int getTaille()      { return nbOperations; }

    /**
     * Ajoute un élément à la tête de la liste.
     * @param obj l'élément à ajouter.
     */
    public void ajouterDebut(Object obj ) {
        // encapsuler l'objet dans un nouveau noeud
        Noeud nouveau = new Noeud(obj);

        // gérer le cas où la liste est vide
        if (tete == null) {
            tete = nouveau;
        } else {
            // tous les autres cas
            nouveau.setSuivant(tete);
            tete = nouveau;
        }
        // mettre à jour la taille
        nbOperations++;
    }

    public String toString() {
        if (nbOperations == 0) {
            return "HISTORIQUE NO\n";
        }
        StringBuilder chaineListe = new StringBuilder();

        Noeud courant = tete;
        //chaineListe.append(tete.toString());
        //int position = 0;
        while (courant.getSuivant() != null) {
            chaineListe.append( courant.toString());
            courant = courant.getSuivant();
            //position++;
        }
        chaineListe.append(courant.toString());

        return chaineListe.toString();
    }
}