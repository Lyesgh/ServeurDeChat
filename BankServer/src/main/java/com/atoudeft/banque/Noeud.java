package com.atoudeft.banque;

import java.io.Serializable;
import java.util.Date;

public class Noeud implements Serializable {
    private final Object element;
    private Noeud suivant;
    //Date date;


    public static void main(String[] args){
        Noeud n = new Noeud(20);
        //System.out.println(n);
    }
    // constructeur par paramètre
    public Noeud(Object element) {
        this.element = element;
        //date= new Date(System.currentTimeMillis());
        //System.out.println(date.toString());
    }

    // accesseurs
    public Noeud getSuivant() {
        return suivant;
    }

    public Object getElement() {
        return element;
    }
    /*public String date(){
        return date.toString();
    }*/

    // mutateurs
    public void setSuivant(Noeud suivant) {
        this.suivant = suivant;
    }

    public String toString() {
        return element.toString();
    }
}