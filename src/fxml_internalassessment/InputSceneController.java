/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_internalassessment;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author Martin
 */
public class InputSceneController implements Initializable {

    @FXML
    private Button addButton;
    @FXML
    private Button backButton;

    @FXML
    private DatePicker dateTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private TextField paidByTextField1;
    @FXML
    private TextField commentTextField;
    @FXML
    private TextField amountTextField;
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
    //Add the contents of the TextFields to the database
    private void addButtonAction(ActionEvent event) {
        try{
            LocalDate ld=dateTextField.getValue();
            Entry input=new Entry(ld.toString(), typeTextField.getText(), paidByTextField1.getText(), commentTextField.getText(), Integer.parseInt(amountTextField.getText()), (Main.Entries.size()+1));
            Main.Entries.add(input);
            Label.setText("New element successfully added!");
        }
        catch(NumberFormatException e){
            Label.setText("Please only input numbers into the amount row!");
            System.err.println("Number format exception at amountTextField!");
        }
        catch(NullPointerException e){
            Label.setText("Please write something in all of the rows!");
            System.err.println("Null pointer exception at one of the TextFieds!");
        }
        
        JSONArray array=new JSONArray();
        
        
        dateTextField.setValue(LocalDate.now());
        typeTextField.setText("");
        paidByTextField1.setText("");
        commentTextField.setText("");
        amountTextField.setText("");
        
        Main.Entries.forEach((p) -> {
            JSONObject obj1= new JSONObject();
            obj1.put("Date", p.getDate());
            obj1.put("Type", p.getType());
            obj1.put("Paid_by", p.getPaid_by());
            obj1.put("Comment", p.getComment());
            obj1.put("Amount", p.getAmount());
            obj1.put("Row", p.getRow());

            JSONObject o1= new JSONObject();
            o1.put("Entry", obj1);

            array.add(o1);
        });  
        
        try(FileWriter file=new FileWriter("OutputTemporary.json")){
             file.write(array.toJSONString());
             file.flush();
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
        //Keeps track of previous version for the sake of rewinding
        ArrayList<Entry> newNode=new ArrayList<>();
        Main.Entries.forEach(p ->{
            newNode.add(p);
        });
        Main.currentNode.setNext(newNode);
        Main.currentNode=Main.currentNode.getNext();
        if (Main.currentNode.getListSize()>5) Main.currentNode.removeFirst();
    }

    @FXML
    //Change the scene to LesseeList.fxml
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
