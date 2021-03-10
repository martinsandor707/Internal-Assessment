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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 * FXML Controller class
 *
 * @author Martin
 * This class is responsible for helping the user view the list of lessees, which is stored in a file called LesseeList.json.
 * Much like the main table, this table saves any changes into a temporary file, and changes are only permanent if the Save button is pressed.
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
    private TextField FilterTextField;
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
    
    String dummy;
    
    ObservableList list=FXCollections.observableArrayList(Main.LesseeList);
    
    
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
                dummy=FilterTextField.getText();
                FilterTextField.setText("");
                update();
                FilterTextField.setText(dummy);
        });
        
        Address.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Address.setOnEditCommit((TableColumn.CellEditEvent<Lessee, String> t) ->{
        ((Lessee)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setAddress(t.getNewValue());
                dummy=FilterTextField.getText();
                FilterTextField.setText("");
                update();
                FilterTextField.setText(dummy);
        });
        
        Phone_number.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Phone_number.setOnEditCommit((TableColumn.CellEditEvent<Lessee, String> t) ->{
        ((Lessee)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setPhone_number(t.getNewValue());
                dummy=FilterTextField.getText();
                FilterTextField.setText("");
                update();
                FilterTextField.setText(dummy);
        });
        
        Email.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Email.setOnEditCommit((TableColumn.CellEditEvent<Lessee, String> t) ->{
        ((Lessee)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setEmail(t.getNewValue());
                dummy=FilterTextField.getText();
                FilterTextField.setText("");
                update();
                FilterTextField.setText(dummy);
        });
        
        Comments.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Comments.setOnEditCommit((TableColumn.CellEditEvent<Lessee, String> t) ->{
        ((Lessee)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setComments(t.getNewValue());
                dummy=FilterTextField.getText();
                FilterTextField.setText("");
                update();
                FilterTextField.setText(dummy);
        });
        
        //Loads the elements of Entries into the table
        Table.setItems(list);
        
        //Delete button functionality
        DeleteButton.setOnAction(e -> {
            Lessee selectedItem=Table.getSelectionModel().getSelectedItem();
            list.remove(selectedItem);
            dummy=FilterTextField.getText();
            FilterTextField.setText("");
            update();
            FilterTextField.setText(dummy);
        });
        
        connectTableToFilter();        
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
        list.forEach((p) -> {
            LesseeList2.add((Lessee)p);
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
        //Set filter to track the current version of the table
        connectTableToFilter();
        
        //Keeps track of the state of the table, in case the user wants to rewind
        ArrayList<Lessee> newNode=new ArrayList<>();
        list.forEach(p ->{
            newNode.add((Lessee)p);
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
             
            list.setAll(Main.LesseeList);
            connectTableToFilter();
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
             
            list.setAll(Main.LesseeList);
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
            
            MessageLabel.setText("Changes successfully saved !");
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
    
    private void connectTableToFilter(){
        //Wrap the ObservableList in a FilteredList (initially display all data)
        FilteredList<Lessee> filteredData=new FilteredList<>(list, b-> true);
        //2. Set the filter Predicate whenever the filter changes
        FilterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(lessee -> {
                
                //If filter text is empty, display all entries
                if (newValue == null || newValue.isEmpty()) return true;
                
                String lowerCaseFilter=newValue.toLowerCase();
                
                //Compare the contents of the date, type, paid_by and comment columns to the filter text
                if (lessee.getName().toLowerCase().contains(lowerCaseFilter)) return true;   //Filter matches date
                else if (lessee.getAddress().toLowerCase().contains(lowerCaseFilter)) return true;
                else if (lessee.getPhone_number().toLowerCase().contains(lowerCaseFilter)) return true;
                else if (lessee.getEmail().toLowerCase().contains(lowerCaseFilter)) return true;
                else if (lessee.getComments().toLowerCase().contains(lowerCaseFilter)) return true;
                return false; //Does not match
            });
        });
        
        //3. Wrap the FilteredList in a SortedList
        SortedList<Lessee> sortedData=new SortedList<>(filteredData);
        
        //4. Bind the SortedList comparator to the TableView comparator. Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(Table.comparatorProperty());
        
        //5. Add sorted and filtered data to the table
        Table.setItems(sortedData);
    }
    
}
