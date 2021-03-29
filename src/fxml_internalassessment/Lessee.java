/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_internalassessment;

/**
 *
 * Object type used in the LesseeList.json database. Contents of the database are shown in LesseeList.fxml, and are organized into such objects
 * Aside from the variables themselves, this class only has the basic getters and setters, plus a constructor.
 */
public class Lessee {
    private String name, address, phone_number, email, comments;

    public Lessee(String name, String adress, String phone_number, String email, String comments) {
        this.name = name;
        this.address = adress;
        this.phone_number = phone_number;
        this.email = email;
        this.comments = comments;
    }
    public Lessee(Lessee lessee) {
        this.name = lessee.name;
        this.address = lessee.address;
        this.phone_number = lessee.phone_number;
        this.email = lessee.email;
        this.comments = lessee.comments;
    }
    public Lessee() {
        this.name = null;
        this.address = null;
        this.phone_number = null;
        this.email = null;
        this.comments = null;
    }
    
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getComments() {
        return comments;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
 
}
