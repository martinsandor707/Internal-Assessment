/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_internalassessment;


import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * FXML Controller class
 *
 * @author Martin
 */
public class LesseeInputController implements Initializable {

    @FXML
    private TextField Name;
    @FXML
    private TextField Address;
    @FXML
    private TextField Phone_number;
    @FXML
    private TextField Email;
    @FXML
    private TextArea Comment;
    @FXML
    private Button BackButton;
    @FXML
    private Label Label;

    /**
     * This class is responsible for letting the user add more elements into the list of Lessees
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void AddButtonAction(ActionEvent event) {
        Lessee input=new Lessee(Name.getText(), Address.getText(), Phone_number.getText(), Email.getText(), Comment.getText());
        Main.LesseeList.add(input);
        
        JSONArray array=new JSONArray();
        
        Label.setText("New row successfully added!");
        Name.setText("");
        Address.setText("");
        Phone_number.setText("");
        Email.setText("");
        Comment.setText("");
        
        Main.LesseeList.forEach((p) -> {
            JSONObject obj1= new JSONObject();
            obj1.put("Name", p.getName());
            obj1.put("Address", p.getAddress());
            obj1.put("Phone_number", p.getPhone_number());
            obj1.put("Email", p.getEmail());
            obj1.put("Comments", p.getComments());

            JSONObject o1= new JSONObject();
            o1.put("Lessee", obj1);

            array.add(o1);
        });
        
        //copies database into temporary file
        try(FileWriter file=new FileWriter("LesseeListTemporary.json")){
             file.write(array.toJSONString());
             file.flush();
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
        //Keeps track of previous version for the sake of rewinding
        ArrayList<Lessee> newNode=new ArrayList<>();
        Main.LesseeList.forEach(p ->{
            newNode.add(p);
        });
        Main.currentLesseeNode.setNext(newNode);
        Main.currentLesseeNode=Main.currentLesseeNode.getNext();
        if (Main.currentLesseeNode.getListSize()>5) Main.currentLesseeNode.removeFirst();
    }

    @FXML
    private void BackButtonAction(ActionEvent event) {
        
        try{
            Parent newPersonParent=FXMLLoader.load(getClass().getResource("LesseeList.fxml"));
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
