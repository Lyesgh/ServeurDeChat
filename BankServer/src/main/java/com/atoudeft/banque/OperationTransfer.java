package com.atoudeft.banque;

import java.util.Date;

public class OperationTransfer extends Operation{

    private double montant;
    private Date date;
    public OperationTransfer(double montant, Object typeOperation) {
        super(typeOperation);
        this.montant = montant;
        this.date= new Date(System.currentTimeMillis());
    }

    public String toString(){
        return this.date+"\t" +montant+ "$\t" + typeOperation.toString()+"\n";
    }
}
