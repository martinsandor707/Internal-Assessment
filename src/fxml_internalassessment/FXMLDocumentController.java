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
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 *
 * @author nando
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TableView<Person> table;
    @FXML
    private TableColumn<Person, String> Name;
    @FXML
    private TableColumn<Person, String> Phone_adress;
    @FXML
    private TableColumn<Person, String> Comment;
    @FXML
    private Button ChangeSceneButton;
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
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        Phone_adress.setCellValueFactory(new PropertyValueFactory<>("Phone_adress"));
        Comment.setCellValueFactory(new PropertyValueFactory<>("Comment"));
        table.setEditable(true);
        
        //Makes the given column editable
        Name.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Name.setOnEditCommit((CellEditEvent<Person, String> t) ->{
        ((Person)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setName(t.getNewValue());
                update();
        });
        
        FXML_InternalAssessment.getPersons().forEach((p) ->{
            table.getItems().add(p);
        });
        
        
        DeleteButton.setOnAction(e -> {
            Person selectedItem=table.getSelectionModel().getSelectedItem();
            table.getItems().remove(selectedItem);
            update();
            MessageLabel.setText("Row deleted successfully!");
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
        Person selectedItem=table.getSelectionModel().getSelectedItem();
         try{
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("DetailedScene.fxml"));
            Parent TableViewParent=loader.load();
            Scene newPersonScene=new Scene(TableViewParent);
            
            DetailedSceneController controller= loader.getController();
            controller.initData(selectedItem);
                    
            Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(newPersonScene);
            window.show();
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }
  
   //Creates a file called OutputTemporary.json where any changes to the original Output.json are stored.
   //This method is called every time the contents of the table are manipulated:
   //At adding a new element, at deleting or modifying an already existing element 
    public void update(){
//        MessageLabel.setText("Update method used");
        FXML_InternalAssessment app=new FXML_InternalAssessment();
        JSONArray array=new JSONArray();
        List<Person> persons2=new ArrayList<>();
        table.getItems().forEach((p) -> {
            persons2.add(p);
        });
        app.Persons=persons2;
        app.Persons.forEach((p) -> {
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
        app.currentNode.setNext(app.Persons);
        app.currentNode=app.currentNode.getNext();
        if (app.currentNode.getListSize()>5) app.currentNode.removeFirst();
    }

    @FXML
    public void HandleSaveData(ActionEvent event) {
        FileInputStream ins=null;
        FileOutputStream outs=null;
        try{
            File outputTemp = new File("OutputTemporary.json");
            File outputReal = new File("Output.json");
            ins = new FileInputStream(outputTemp);
            outs = new FileOutputStream(outputReal);
            byte[] buffer = new byte[1024];
            int length;
            
            while((length = ins.read(buffer)) > 0){
                outs.write(buffer, 0, length);
            }
            ins.close();
            outs.close();
            
            FXML_InternalAssessment app=new FXML_InternalAssessment();
            MessageLabel.setText("File saved successfully!");
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
    //Method is called when the "Rewind" button is pressed. It reverts the table to it's previous state, just before the latest modification
    //Currently can't revert the addition of rows, neither the modification of existing cells for an unknown reason.
    private void HandleRewindAction(ActionEvent event) {
//        System.out.println("Rewind pressed");
        FXML_InternalAssessment app=new FXML_InternalAssessment();
        JSONArray array=new JSONArray();
        
        if (app.currentNode.getPrev()!=null) app.currentNode=app.currentNode.getPrev();
        else return;
        app.Persons=app.currentNode.getValue();
        
        app.Persons.forEach((p) -> {
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
             
            table.getItems().removeAll(table.getItems());
            app.Persons.forEach((p) ->{
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
        app.currentNode.getListSize();
    }

    @FXML
    private void HandleForwardAction(ActionEvent event) {
//        System.out.println("Forward pressed");
        FXML_InternalAssessment app=new FXML_InternalAssessment();
        JSONArray array=new JSONArray();
        
        if (app.currentNode.getNext()!=null) app.currentNode=app.currentNode.getNext();
        else return;
        app.Persons=app.currentNode.getValue();
        
        app.Persons.forEach((p) -> {
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
             
            table.getItems().removeAll(table.getItems());
            app.Persons.forEach((p) ->{
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
        app.currentNode.getListSize();
        
    }
    
}
