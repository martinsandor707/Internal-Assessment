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
import java.time.LocalDate;
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
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Martin
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private TableView<Entry> table;
    @FXML
    private TextField FilterTextField;
    @FXML
    private TextField FromRow;
    @FXML
    private TextField ToRow;
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
    private TableColumn<Entry, Integer> Row;
    @FXML
    private Button DeleteButton;
    @FXML
    private Label MessageLabel;
    //Required by Amount to convert the user's input from String to Integer
    IntegerStringConverter converter=new IntegerStringConverter();
    //Used to preserve the unfiltered database when the user manipulates the filtered version
    String dummy="";
    //Used to adjust row numbers when a row is deleted
    int rowNR;
    ObservableList list=FXCollections.observableArrayList(Main.Entries);
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //Placeholder message for an empty table is set
        Label lb=new Label();
        lb.setText("The table is either empty or your search didn't find anything");
        table.setPlaceholder(lb);
        //All columns are initialized
        Date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        Type.setCellValueFactory(new PropertyValueFactory<>("Type"));
        Paid_by.setCellValueFactory(new PropertyValueFactory<>("Paid_by"));
        Comment.setCellValueFactory(new PropertyValueFactory<>("Comment"));
        Amount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        Row.setCellValueFactory(new PropertyValueFactory<>("Row"));
        table.setEditable(true);
        
        //Makes the given column editable
        Date.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Date.setOnEditCommit((CellEditEvent<Entry, String> t) ->{
        ((Entry)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setDate(t.getNewValue());
                dummy=FilterTextField.getText();
                FilterTextField.setText("");
                update();
                FilterTextField.setText(dummy);
        });
        
        Type.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Type.setOnEditCommit((CellEditEvent<Entry, String> t) ->{
        ((Entry)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setType(t.getNewValue());
                dummy=FilterTextField.getText();
                FilterTextField.setText("");
                update();
                FilterTextField.setText(dummy);
        });
        
        Paid_by.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Paid_by.setOnEditCommit((CellEditEvent<Entry, String> t) ->{
        ((Entry)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setPaid_by(t.getNewValue());
                dummy=FilterTextField.getText();
                FilterTextField.setText("");
                update();
                FilterTextField.setText(dummy);
        });
        
        Comment.setCellFactory(TextFieldTableCell.forTableColumn()); 
        Comment.setOnEditCommit((CellEditEvent<Entry, String> t) ->{
        ((Entry)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setComment(t.getNewValue());
                dummy=FilterTextField.getText();
                FilterTextField.setText("");
                update();
                FilterTextField.setText(dummy);
        });
        
        Amount.setCellFactory(TextFieldTableCell.forTableColumn(converter)); 
        Amount.setOnEditCommit((CellEditEvent<Entry, Integer> t) ->{
        ((Entry)t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setAmount(t.getNewValue());
                dummy=FilterTextField.getText();
                FilterTextField.setText("");
                update();
                FilterTextField.setText(dummy);
        });
        

        //Delete button functionality
        DeleteButton.setOnAction(e -> {
            Entry selectedItem=table.getSelectionModel().getSelectedItem();
            list.remove(selectedItem);
            dummy=FilterTextField.getText();
            FilterTextField.setText("");
            rowNR=1;
            table.getItems().forEach( entry ->{
               entry.setRow(rowNR);
               rowNR++;
            });
            update();
            FilterTextField.setText(dummy);
        });
        
        connectTableToFilter();
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

    //Opens LesseeList window
    @FXML
    private void HandleChangeScene2(ActionEvent event) {
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
        JSONArray array=new JSONArray();
        ArrayList<Entry> entries2=new ArrayList<>();
        //Collects the contents of the table into a list
        list.forEach(entry -> {
            entries2.add((Entry) entry);
        });
        //updates database to have the elements corresponding with the table
        Main.Entries=entries2;
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
        
        //copies database into temporary file
        try(FileWriter file=new FileWriter("OutputTemporary.json")){
             file.write(array.toJSONString());
             file.flush();
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
        //Set filter to track the current version of the table
        connectTableToFilter();
        
        //Keeps track of the state of the table, in case the user wants to rewind
        ArrayList<Entry> newNode=new ArrayList<>();
        list.forEach(p ->{
            newNode.add((Entry)p);
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
            
            MessageLabel.setText("Changes successfully saved!");
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
            obj1.put("Row", p.getRow());

            JSONObject o1= new JSONObject();
            o1.put("Entry", obj1);

            array.add(o1);
            
        });
        
        
        try(FileWriter file=new FileWriter("OutputTemporary.json")){
            file.write(array.toJSONString());
            file.flush();
            list.setAll(Main.Entries);
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
            obj1.put("Row", p.getRow());

            JSONObject o1= new JSONObject();
            o1.put("Entry", obj1);

            array.add(o1);
            
        });
        
        
        try(FileWriter file=new FileWriter("OutputTemporary.json")){
             file.write(array.toJSONString());
             file.flush();
             
            list.setAll(Main.Entries);
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }        
    }

    @FXML
    private void Summation(ActionEvent event) {
        try{
            int sum=0, start=Integer.parseInt(FromRow.getText()), end=Integer.parseInt(ToRow.getText());
            Entry cur;
            for (int i = start-1; i < end; i++) {
                cur=Main.Entries.get(i);
                sum=sum+cur.getAmount();
            }
            Entry newEntry=new Entry(LocalDate.now().toString(),"Sum:"+start+"-"+end,"-----","",sum,Main.Entries.size()+1);
            Main.Entries.add(newEntry);
            list.setAll(Main.Entries);
            
            update();
            }
            catch(NumberFormatException e){
                MessageLabel.setText("Please only input numbers into the to/from rows!");
                System.err.println("Number format exception at summation!");
            }
            catch(IndexOutOfBoundsException e){
                MessageLabel.setText("One or more of the rows you entered are incorrect!");
                System.err.println("Index out of bounds exception at summation!");
            }
    }

    @FXML
    private void Averaging(ActionEvent event) {
        try{
            int avg=0, start=Integer.parseInt(FromRow.getText()), end=Integer.parseInt(ToRow.getText()), count=0;
            Entry cur;
            for (int i = start-1; i < end; i++) {
                cur=Main.Entries.get(i);
                avg=avg+cur.getAmount();
                count++;
            }
            avg=avg/count;
            Entry newEntry=new Entry(LocalDate.now().toString(),"Average:"+start+"-"+end,"-----","",avg,Main.Entries.size()+1);
            Main.Entries.add(newEntry);
            list.setAll(Main.Entries);
            
            update();
        }
        catch(NumberFormatException e){
            MessageLabel.setText("Please only input numbers into the to/from rows!");
            System.err.println("Number format exception at averaging!");
        }
        catch(IndexOutOfBoundsException e){
                MessageLabel.setText("One or more of the rows you entered are incorrect!");
                System.err.println("Index out of bounds exception at averaging!");
            }
    }
    
    private void connectTableToFilter(){
        //Wrap the ObservableList in a FilteredList (initially display all data)
        FilteredList<Entry> filteredData=new FilteredList<>(list, b-> true);
        //2. Set the filter Predicate whenever the filter changes
        FilterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(entry -> {
                
                //If filter text is empty, display all entries
                if (newValue == null || newValue.isEmpty()) return true;
                
                String lowerCaseFilter=newValue.toLowerCase();
                
                //Compare the contents of the date, type, paid_by and comment columns to the filter text
                if (entry.getDate().toLowerCase().contains(lowerCaseFilter)) return true;   //Filter matches date
                else if (entry.getType().toLowerCase().contains(lowerCaseFilter)) return true;
                else if (String.valueOf(entry.getAmount()).toLowerCase().contains(lowerCaseFilter)) return true;
                else if (entry.getPaid_by().toLowerCase().contains(lowerCaseFilter)) return true;
                else if (entry.getComment().toLowerCase().contains(lowerCaseFilter)) return true;
                return false; //Does not match
            });
        });
        
        //3. Wrap the FilteredList in a SortedList
        SortedList<Entry> sortedData=new SortedList<>(filteredData);
        
        //4. Bind the SortedList comparator to the TableView comparator. Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        
        //5. Add sorted and filtered data to the table
        table.setItems(sortedData);
    }
    
}
