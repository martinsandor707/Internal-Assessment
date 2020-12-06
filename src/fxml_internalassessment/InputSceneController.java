/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_internalassessment;

import java.io.FileWriter;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author nando
 */
public class InputSceneController implements Initializable {

    @FXML
    private Button addButton;
    @FXML
    private Button backButton;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField phoneTextField;
    @FXML
    private TextField commentTextField;
    @FXML
    private Label Label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void addButtonAction(ActionEvent event) {
        Person input=new Person(nameTextField.getText(), phoneTextField.getText(), commentTextField.getText());
        FXML_InternalAssessment.Persons.add(input);
        
        JSONArray array=new JSONArray();
        
        Label.setText("Input successful");
        nameTextField.setText("");
        phoneTextField.setText("");
        commentTextField.setText("");
        
        FXML_InternalAssessment.Persons.forEach((p) -> {
            JSONObject obj1= new JSONObject();
            obj1.put("Name", p.getName());
            obj1.put("Phone_adress", p.getPhone_adress());
            obj1.put("Comment", p.getComment());

            JSONObject o1= new JSONObject();
            o1.put("Person", obj1);

            array.add(o1);
        });
        
        
        try(FileWriter file=new FileWriter("OutputTemporary.json")){
             file.write(array.toJSONString());
             file.flush();
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
        
        FXML_InternalAssessment.currentNode.setNext(FXML_InternalAssessment.Persons);
        FXML_InternalAssessment.currentNode=FXML_InternalAssessment.currentNode.getNext();
        if (FXML_InternalAssessment.currentNode.getListSize()>5) FXML_InternalAssessment.currentNode.removeFirst();
    }

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
