/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_internalassessment;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author nando
 */
public class DetailedSceneController implements Initializable {

    @FXML
    private Button BackButton;
    @FXML
    private Label nameLabel;
    
    private Person selectedPerson;

    
    //Loads data from FXMLDOcument.fxml from the selected row
    public void initData(Person person)
    {
        selectedPerson=person;
        nameLabel.setText(selectedPerson.getName());
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    //returns to main Scene
     @FXML
    private void HandleChangeScene(ActionEvent event) {
        try{
            Parent newPersonParent=FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Scene newPersonScene=new Scene(newPersonParent);
            Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(newPersonScene);
            window.show();
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }
    
}
