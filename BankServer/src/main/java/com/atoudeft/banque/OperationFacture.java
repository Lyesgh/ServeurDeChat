package com.atoudeft.banque;

import java.util.Date;

public class OperationFacture extends Operation{

    private double montant;
    private Date date;
    public OperationFacture(double montant, Object operation) {
        super(operation);
        this.montant = montant;
        this.date= new Date(System.currentTimeMillis());
    }

    public String toString(){
        return this.date+"\t" +montant+ "$\t" + typeOperation.toString()+"\n";
    }
}
