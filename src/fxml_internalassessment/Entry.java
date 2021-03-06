/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_internalassessment;

/**
 *
 * @author nando
 */
public class Entry {
    
    String date, type, paid_by, comment;
    int amount;

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getPaid_by() {
        return paid_by;
    }

    public String getComment() {
        return comment;
    }

    public int getAmount() {
        return amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPaid_by(String paid_by) {
        this.paid_by = paid_by;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Entry(String date, String type, String paid_by, String comment, int amount) {
        this.date = date;
        this.type = type;
        this.paid_by = paid_by;
        this.comment = comment;
        this.amount = amount;
    }



    
    public Entry(Entry entry) {
        this.date = entry.date;
        this.type = entry.type;
        this.paid_by = entry.paid_by;
        this.comment = entry.comment;
        this.amount = entry.amount;
    }
    public Entry() {
        this.date = null;
        this.type = null;
        this.paid_by = null;
        this.comment = null;
        this.amount = 0;
    }
    @Override
    public String toString(){
        return "Date: "+date+" Amount: "+amount+" Type: "+type+" Paid by: "+paid_by+" Comment: "+comment;
    }
    
    
    
}