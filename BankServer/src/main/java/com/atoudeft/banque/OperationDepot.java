package com.atoudeft.banque;

import java.util.Date;

public class OperationDepot extends Operation{
    private double montant;
    private Date date;
    public OperationDepot(double montant, Object operation) {
        super(operation);
        this.montant = montant;
        this.date = new Date(System.currentTimeMillis());
    }

    public String toString(){
        return this.date + " \t" + montant+ "$\t" + typeOperation.toString()+"\n";
    }
}
