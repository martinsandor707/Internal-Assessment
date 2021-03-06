/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_internalassessment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * FXML Controller class
 *
 * @author nando
 */
public class LesseeListController implements Initializable {

    @FXML
    private Button BackButton;
    @FXML
    private Button AddButton;
    @FXML
    private Button DeleteButton;
    @FXML
    private Button RewindButton;
    @FXML
    private Button ForwardButton;
    
    @FXML
    private TableView<Lessee> Table;
    @FXML
    private TableColumn<Lessee, String> Name;
    @FXML
    private TableColumn<Lessee, String> Address;
    @FXML
    private TableColumn<Lessee, String> Phone_number;
    @FXML
    private TableColumn<Lessee, String> Email;
    @FXML
    private TableColumn<Lessee, String> Comments;
    @FXML
    private Label MessageLabel;

    

    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //All columns are initialized
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        Phone_number.setCellValueFactory(new PropertyValueFactory<>("Phone_number"));
        Email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        Comments.setCellValueFactory(new PropertyValueFactory<>("Comments"));
        Table.setEditable(true);
        
        //Makes the given column editable
        Name.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Name.setOnEditCommit((TableColumn.CellEditEvent<Lessee, String> t) ->{
        ((Lessee)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setName(t.getNewValue());
                update();
        });
        
        Address.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Address.setOnEditCommit((TableColumn.CellEditEvent<Lessee, String> t) ->{
        ((Lessee)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setAddress(t.getNewValue());
                update();
        });
        
        Phone_number.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Phone_number.setOnEditCommit((TableColumn.CellEditEvent<Lessee, String> t) ->{
        ((Lessee)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setPhone_number(t.getNewValue());
                update();
        });
        
        Email.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Email.setOnEditCommit((TableColumn.CellEditEvent<Lessee, String> t) ->{
        ((Lessee)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setEmail(t.getNewValue());
                update();
        });
        
        Comments.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Comments.setOnEditCommit((TableColumn.CellEditEvent<Lessee, String> t) ->{
        ((Lessee)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setComments(t.getNewValue());
                update();
        });
        
        //Loads the elements of Entries into the table
        Main.LesseeList.forEach((p) ->{
            Table.getItems().add(p);
        });
        
        //Delete button functionality
        DeleteButton.setOnAction(e -> {
            Lessee selectedItem=Table.getSelectionModel().getSelectedItem();
            Table.getItems().remove(selectedItem);
            update();
        });
        
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
    
    //Changes Scene to LesseeInput.fxml
     @FXML
    private void ChangeToInputScene(ActionEvent event) {
        try{
            Parent newPersonParent=FXMLLoader.load(getClass().getResource("LesseeInput.fxml"));
            Scene newPersonScene=new Scene(newPersonParent);
            Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(newPersonScene);
            window.show();
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }
    
    private void update(){
        JSONArray array=new JSONArray();
        ArrayList<Lessee> LesseeList2=new ArrayList<>();
        //Collects the contents of the table into a list
        Table.getItems().forEach((p) -> {
            LesseeList2.add(p);
        });
        //updates database to have the elements corresponding with the table
        Main.LesseeList=LesseeList2;
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
        
        //Keeps track of the state of the table, in case the user wants to rewind
        ArrayList<Lessee> newNode=new ArrayList<>();
        Table.getItems().forEach(p ->{
            newNode.add(p);
        });
        Main.currentLesseeNode.setNext(newNode);
        Main.currentLesseeNode=Main.currentLesseeNode.getNext();
        if (Main.currentLesseeNode.getListSize()>5) Main.currentLesseeNode.removeFirst();
        
    }

    @FXML
    private void HandleRewindAction(ActionEvent event) {
        
        JSONArray array=new JSONArray();
        
        if (Main.currentLesseeNode.getPrev()!=null) Main.currentLesseeNode=Main.currentLesseeNode.getPrev();
        else return;
        Main.LesseeList=Main.currentLesseeNode.getValueDeep();
        
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
        
        
        try(FileWriter file=new FileWriter("LesseeListTemporary.json")){
             file.write(array.toJSONString());
             file.flush();
             
            Table.getItems().removeAll(Table.getItems());
            Main.LesseeList.forEach((p) ->{
                Table.getItems().add(p);
            });
            Table.refresh();
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }        
        
    }

    @FXML
    private void HandleForwardAction(ActionEvent event) {
        
        JSONArray array=new JSONArray();
        
        if (Main.currentLesseeNode.getNext()!=null) Main.currentLesseeNode=Main.currentLesseeNode.getNext();
        else return;
        Main.LesseeList=Main.currentLesseeNode.getValueDeep();
        
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
        
        
        try(FileWriter file=new FileWriter("LesseeListTemporary.json")){
             file.write(array.toJSONString());
             file.flush();
             
            Table.getItems().removeAll(Table.getItems());
            Main.LesseeList.forEach((p) ->{
                Table.getItems().add(p);
            });
            Table.refresh();
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }        
        
    }

    @FXML
    public void SaveButtonAction(ActionEvent event) {
        FileInputStream ins;
        FileOutputStream outs;
        try{
            File outputTemp = new File("LesseeListTemporary.json");
            File outputReal = new File("LesseeList.json");
            //Create streams
            ins = new FileInputStream(outputTemp);
            outs = new FileOutputStream(outputReal);
            byte[] buffer = new byte[1024];
            int length;
            //Write into Output.json as long as the outputstream is not empty
            while((length = ins.read(buffer)) > 0){
                outs.write(buffer, 0, length);
            }
            //Close streams
            ins.close();
            outs.close();
            
            MessageLabel.setText("Változtatások mentve!");
        }
        
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
        catch(Exception e){
            
    }
    }
    
}
