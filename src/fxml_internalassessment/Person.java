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
public class Person {
    
    String name, phone_adress, comment;

    public String getName() {
        return name;
    }

    public String getPhone_adress() {
        return phone_adress;
    }

    public String getComment() {
        return comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone_adress(String phone_adress) {
        this.phone_adress = phone_adress;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Person(String name, String phone_adress, String comment) {
        this.name = name;
        this.phone_adress = phone_adress;
        this.comment = comment;
    }
    public Person() {
        this.name = "";
        this.phone_adress = "";
        this.comment = "";
    }
    
    
    
    
}
