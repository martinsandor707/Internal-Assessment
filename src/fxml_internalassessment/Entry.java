/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_internalassessment;

/**
 * Class used by the main Output.json database. Elements in the main table are shown according to this object.
 * Aside from the variables themselves, this class only has the basic getters and setters, plus a constructor.
 */
public class Entry {
    
    private String date, type, paid_by, comment;
    private int amount, row;

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
    
    public int getRow() {
        return row;
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
    
    public void setRow(int row) {
        this.row = row;
    }

    public Entry(String date, String type, String paid_by, String comment, int amount, int row) {
        this.date = date;
        this.type = type;
        this.paid_by = paid_by;
        this.comment = comment;
        this.amount = amount;
        this.row = row;
    }



    
    public Entry(Entry entry) {
        this.date = entry.date;
        this.type = entry.type;
        this.paid_by = entry.paid_by;
        this.comment = entry.comment;
        this.amount = entry.amount;
        this.row = entry.row;
    }
    public Entry() {
        this.date = null;
        this.type = null;
        this.paid_by = null;
        this.comment = null;
        this.amount = 0;
        this.row = 0;
    }
    @Override
    public String toString(){
        return "Row: "+row+" Date: "+date+" Amount: "+amount+" Type: "+type+" Paid by: "+paid_by+" Comment: "+comment;
    }
    
    
    
}
