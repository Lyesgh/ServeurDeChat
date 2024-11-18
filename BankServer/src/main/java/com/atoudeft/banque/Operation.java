package com.atoudeft.banque;

import java.io.Serializable;
import java.util.Date;

public abstract class Operation implements Serializable {
    Object typeOperation;
    //Date date;
    public Operation(Object typeOperation){
        this.typeOperation = typeOperation;
        //this.date = new Date(System.currentTimeMillis());
    }

    /*public String getDate(){
        return this.date.toString();
    }*/
    public String toString(){
        return typeOperation.toString();
    }
}
