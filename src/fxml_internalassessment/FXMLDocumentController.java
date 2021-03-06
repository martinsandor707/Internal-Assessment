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
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author nando
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TableView<Entry> table;
    @FXML
    private TableColumn<Entry, String> Date;
    @FXML
    private TableColumn<Entry, String> Type;
    @FXML
    private TableColumn<Entry, String> Paid_by;
    @FXML
    private TableColumn<Entry, String> Comment;
    @FXML
    private TableColumn<Entry, Integer> Amount;
    @FXML
    private Button DeleteButton;
    @FXML
    private Button DetailedButton;
    @FXML
    private Button SaveButton;
    @FXML
    private Label MessageLabel;
    @FXML
    private Button CalculationsButton;
    @FXML
    private Label MenuLabel;
    @FXML
    private Label SpreadsheetLabel;
    @FXML
    private Button ChangeSceneButton;
    //Required by Adress to convert the user's input from String to Integer
    IntegerStringConverter converter=new IntegerStringConverter();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //All columns are initialized
        Date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        Type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        Paid_by.setCellValueFactory(new PropertyValueFactory<>("Paid_by"));
        Comment.setCellValueFactory(new PropertyValueFactory<>("Comment"));
        Amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        table.setEditable(true);
        
        //Makes the given column editable
        Date.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Date.setOnEditCommit((CellEditEvent<Entry, String> t) ->{
        ((Entry)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setDate(t.getNewValue());
                update();
        });
        
        Type.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Type.setOnEditCommit((CellEditEvent<Entry, String> t) ->{
        ((Entry)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setType(t.getNewValue());
                update();
        });
        
        Paid_by.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Paid_by.setOnEditCommit((CellEditEvent<Entry, String> t) ->{
        ((Entry)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setPaid_by(t.getNewValue());
                update();
        });
        
        Comment.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Comment.setOnEditCommit((CellEditEvent<Entry, String> t) ->{
        ((Entry)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setComment(t.getNewValue());
                update();
        });
        
        Amount.setCellFactory(TextFieldTableCell.forTableColumn(converter)); 
        Amount.setOnEditCommit((CellEditEvent<Entry, Integer> t) ->{
        ((Entry)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setAmount(t.getNewValue());
                update();
        });
        
        //Loads the elements of Entries into the table
        Main.Entries.forEach((p) ->{
            table.getItems().add(p);
        });
        
        //Delete button functionality
        DeleteButton.setOnAction(e -> {
            Entry selectedItem=table.getSelectionModel().getSelectedItem();
            table.getItems().remove(selectedItem);
            update();
            MessageLabel.setText("Sor sikeresen törölve!");
        });
    }    

    //Changes scene to InputScene.fxml
    @FXML
    private void HandleChangeScene(ActionEvent event) {
        try{
            Parent newPersonParent=FXMLLoader.load(getClass().getResource("InputScene.fxml"));
            Scene newPersonScene=new Scene(newPersonParent);
            Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(newPersonScene);
            window.show();
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
        
    }

    //Opens Detailed window, gives data of selected row to the other Scene
    @FXML
    private void HandleChangeScene2(ActionEvent event) {
        Entry selectedItem=table.getSelectionModel().getSelectedItem();
         try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("LesseeList.fxml"));
            Parent TableViewParent=loader.load();
            Scene newPersonScene=new Scene(TableViewParent);
                    
            Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(newPersonScene);
            window.show();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
  
   //Creates a file called OutputTemporary.json where any changes to the original Output.json are stored.
   //This method is called every time the contents of the table are manipulated:
   //At adding a new element, at deleting or modifying an already existing element 
    public void update(){
//        MessageLabel.setText("Update method used");
        Main app=new Main();
        JSONArray array=new JSONArray();
        ArrayList<Entry> entries2=new ArrayList<>();
        //Collects the contents of the table into a list
        table.getItems().forEach((p) -> {
            entries2.add(p);
        });
        //updates database to have the elements corresponding with the table
        app.Entries=entries2;
        app.Entries.forEach((p) -> {
            JSONObject obj1= new JSONObject();
            obj1.put("Date", p.getDate());
            obj1.put("Type", p.getType());
            obj1.put("Paid_by", p.getPaid_by());
            obj1.put("Comment", p.getComment());
            obj1.put("Amount", p.getAmount());

            JSONObject o1= new JSONObject();
            o1.put("Entry", obj1);

            array.add(o1);
        });
        
        //copies database into temporary file
        try(FileWriter file=new FileWriter("OutputTemporary.json")){
             file.write(array.toJSONString());
             file.flush();
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
        //Keeps track of the state of the table, in case the user wants to rewind
        ArrayList<Entry> newNode=new ArrayList<>();
        table.getItems().forEach(p ->{
            newNode.add(p);
        });
        Main.currentNode.setNext(newNode);
        Main.currentNode=Main.currentNode.getNext();
        if (Main.currentNode.getListSize()>5) Main.currentNode.removeFirst();
    }

    @FXML
    public void HandleSaveData(ActionEvent event) {
        FileInputStream ins;
        FileOutputStream outs;
        try{
            File outputTemp = new File("OutputTemporary.json");
            File outputReal = new File("Output.json");
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

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }

    @FXML
    /**
    *Method is called when the "Rewind" button is pressed. It reverts the table to it's previous state, just before the latest modification
    * */
    private void HandleRewindAction(ActionEvent event) {
        JSONArray array=new JSONArray();
        
        if (Main.currentNode.getPrev()!=null) Main.currentNode=Main.currentNode.getPrev();
        else return;
        Main.Entries=Main.currentNode.getValueDeep();
        
        Main.Entries.forEach((p) -> {
            JSONObject obj1= new JSONObject();
            obj1.put("Date", p.getDate());
            obj1.put("Type", p.getType());
            obj1.put("Paid_by", p.getPaid_by());
            obj1.put("Comment", p.getComment());
            obj1.put("Amount", p.getAmount());

            JSONObject o1= new JSONObject();
            o1.put("Entry", obj1);

            array.add(o1);
            
        });
        
        
        try(FileWriter file=new FileWriter("OutputTemporary.json")){
             file.write(array.toJSONString());
             file.flush();
             
            table.getItems().removeAll(table.getItems());
            Main.Entries.forEach((p) ->{
                table.getItems().add(p);
            });
            table.refresh();
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
        
        if (Main.currentNode.getNext()!=null) Main.currentNode=Main.currentNode.getNext();
        else return;
        Main.Entries=Main.currentNode.getValueDeep();
        
        Main.Entries.forEach((p) -> {
            JSONObject obj1= new JSONObject();
            obj1.put("Date", p.getDate());
            obj1.put("Type", p.getType());
            obj1.put("Paid_by", p.getPaid_by());
            obj1.put("Comment", p.getComment());
            obj1.put("Amount", p.getAmount());

            JSONObject o1= new JSONObject();
            o1.put("Entry", obj1);

            array.add(o1);
            
        });
        
        
        try(FileWriter file=new FileWriter("OutputTemporary.json")){
             file.write(array.toJSONString());
             file.flush();
             
            table.getItems().removeAll(table.getItems());
            Main.Entries.forEach((p) ->{
                table.getItems().add(p);
            });
            table.refresh();
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }        
    }
    
}
