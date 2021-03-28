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
import javafx.geometry.Pos;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
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
    static String dummy="";
    //Used to adjust row numbers when a row is deleted
    static int rowNR;
    //The list that appears in the table at first
    ObservableList list=FXCollections.observableArrayList(Main.Entries);
    //Used for calculations on a filtered list
    static ArrayList<Entry> actualList=new ArrayList<>();
    @FXML
    private TextField AdditionalRows;
    
    
    
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
        Main.Entries.forEach(p ->{
            actualList.add(new Entry (p));
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
   /**
   *Creates a file called OutputTemporary.json where any changes to the original Output.json are stored.
   *This method is called every time the contents of the table are manipulated:
   *At adding a new element, at deleting or modifying an already existing element 
   */ 
    private void update(){
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
        //Keeps track of the state of the table, in case the user wants to rewind
        ArrayList<Entry> newNode=new ArrayList<>();
        list.forEach(p ->{
            newNode.add((Entry)p);
        });
        Main.currentNode.setNext(newNode);
        Main.currentNode=Main.currentNode.getNext();
        if (Main.currentNode.getListSize()>5) Main.currentNode.removeFirst();
    }

    /**
     * Copies the contents of the temporary files into Output.json and LesseeList.json.
     */
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
            //resets row numbers
            rowNR=1;
            table.getItems().forEach( entry ->{
               entry.setRow(rowNR);
               rowNR++;
            });
            
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @FXML
    /**
     * Method is called when the "Forward" button is pressed. It only works if rewind has been pressed directly before pressing this button.
     * It does the opposite of rewind: it changes the contents of the table to a newer version, provided one exists.
     */
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
            //resets row numbers
            rowNR=1;
            table.getItems().forEach( entry ->{
               entry.setRow(rowNR);
               rowNR++;
            });
            
        }
        
        catch(IOException e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }        
    }

    @FXML
    /**
     * Sums up the interval specified in the "From" and "To" row numbers, then adds the result to the table
     */
    private void Summation(ActionEvent event) {
        int sum=0, start=Integer.parseInt(FromRow.getText()), end=Integer.parseInt(ToRow.getText());
        Entry cur;
        String concat="";
        try{
            
            for (int i = start-1; i < end; i++) {
                cur=actualList.get(i);
                sum=sum+cur.getAmount();
            }
           }
        
        catch(NumberFormatException e){
            MessageLabel.setText("Please only input numbers into the to/from rows!");
            System.err.println("Number format exception at summation!");
        }
        catch(IndexOutOfBoundsException e){
            MessageLabel.setText("One or more of the rows you entered are incorrect!");
            System.err.println("Index out of bounds exception at summation!");
        }
        try{
            if (AdditionalRows.getText()!=""){
                String[] str=AdditionalRows.getText().split(";");
                for (int i = 0; i < str.length; i++) {
                    cur=actualList.get(Integer.parseInt(str[i])-1);
                    sum=sum+cur.getAmount();
                    concat=concat+str[i]+";";
            }    
            }
            
        }
        catch(NumberFormatException e){
            if (FromRow.getText().matches("[0-9]+")==false) MessageLabel.setText("Please only input numbers into the to/from rows!");
            System.err.println("Number format exception at summation!");
        }
        catch(IndexOutOfBoundsException e){
                if (FromRow.getText()!="0" && ToRow.getText()!="0") MessageLabel.setText("One or more of the rows you entered are incorrect!");
                System.err.println("Index out of bounds exception at summation!");
            }
        Entry newEntry=new Entry(LocalDate.now().toString(),FilterTextField.getText()+" Sum:"+start+"-"+end+", "+concat,"*****","",sum,actualList.size()+1);
            
            Main.Entries.add(newEntry);
            list.add(newEntry);
            actualList=new ArrayList();
            Main.Entries.forEach(p ->{
                actualList.add(new Entry(p));
            });
            update();
        
    }

    @FXML
    /**
     * Calculates the average of the interval specified in the "From" and "To" row numbers,then adds the result to the table
     */
    private void Averaging(ActionEvent event) {
        int avg=0, start=Integer.parseInt(FromRow.getText()), end=Integer.parseInt(ToRow.getText()), count=0;
        Entry cur;
        String concat="";
        try{
            
            for (int i = start-1; i < end; i++) {
                cur=actualList.get(i);
                avg=avg+cur.getAmount();
                count++;
            }
            
        }
        catch(NumberFormatException e){
            if (FromRow.getText().matches("[0-9]+")==false) MessageLabel.setText("Please only input numbers into the to/from rows!");
            System.err.println("Number format exception at averaging!");
        }
        catch(IndexOutOfBoundsException e){
                if (FromRow.getText()!="0" && ToRow.getText()!="0") MessageLabel.setText("One or more of the rows you entered are incorrect!");
                System.err.println("Index out of bounds exception at averaging!");
            }
        try{
                if (AdditionalRows.getText()!=""){
                String[] str=AdditionalRows.getText().split(";");
                for (int i = 0; i < str.length; i++) {
                    cur=actualList.get(Integer.parseInt(str[i])-1);
                    avg=avg+cur.getAmount();
                    count++;
                    concat=concat+str[i]+";";
            }
            }
        }
        catch(NumberFormatException e){
            System.err.println("Number format exception at averaging!");
        }
        catch(IndexOutOfBoundsException e){
                MessageLabel.setText("One or more of the rows you entered are incorrect!");
                System.err.println("Index out of bounds exception at averaging!");
            }
        avg=avg/count;
            Entry newEntry=new Entry(LocalDate.now().toString(),FilterTextField.getText()+" Average:"+start+"-"+end+", "+concat,"*****","",avg,actualList.size()+1);
            Main.Entries.add(newEntry);
            list.add(newEntry);
            actualList.add(newEntry);
            update();
    }
    /**
     * The name is self-explanatory, but this method makes sure that filtering works on the table.
     * It also rewrites the row numberings to fit the new filtered list.
     * Multiple expressions can be searched, if they are divided either by a ";" or a "-"
     * ";" creates a logical OR relationship between the two expressions, while
     * "-" creates a logical AND.
     */
    private void connectTableToFilter(){
        //Wrap the ObservableList in a FilteredList (initially display all data)
        FilteredList<Entry> filteredData=new FilteredList<>(list, b-> true);
        //2. Set the filter Predicate whenever the filter changes
        FilterTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            rowNR=1;
            filteredData.setPredicate(entry -> {
                //If filter text is empty, display all entries
                if (newValue == null || newValue.isEmpty()){
                        entry.setRow(rowNR);
                        rowNR=rowNR+1;
                        return true; //Filter matches date
                    }   
                
                String[] ORconditionsArray=newValue.split(";");
                //The different filter elements are searched for separately. Their existence is marked by a semicolon.
                //Eg.: if the filter is "Electricity;Adam", then the filter will look for rows containing at least one of "Electricity" or "Adam"
                for (int i = 0; i < ORconditionsArray.length; i++) {
                    String[] ANDconditionsArray=ORconditionsArray[i].split("-");
                    boolean[] isTrue=new boolean[ANDconditionsArray.length];
                    boolean isAllTrue=true;
                    
                    
                    for (int j = 0; j < ANDconditionsArray.length; j++) {
                        String lowerCaseFilter=ANDconditionsArray[j].toLowerCase();
                        //Compare the contents of the date, type, paid_by and comment columns to the filter text
                        if (entry.getDate().toLowerCase().contains(lowerCaseFilter)) isTrue[j]=true; //One of the conditions matches
                        else if (entry.getType().toLowerCase().contains(lowerCaseFilter)) isTrue[j]=true;
                        else if (String.valueOf(entry.getAmount()).toLowerCase().contains(lowerCaseFilter)) isTrue[j]=true;
                        else if (entry.getPaid_by().toLowerCase().contains(lowerCaseFilter)) isTrue[j]=true;
                        else if (entry.getComment().toLowerCase().contains(lowerCaseFilter)) isTrue[j]=true;
                    }
                      for (int j = 0; j < isTrue.length; j++) {
                        if (isTrue[j]) isTrue[j]=false;
                        else isAllTrue=false;
                    }
                      if (isAllTrue==true) {
                          entry.setRow(rowNR);
                          rowNR++;
                          return true; //Row matches ALL conditions
                      }
                }
                
                return false; //Does not match
            });
            //Updating the list of elements that can be used for calculations
            actualList=new ArrayList();
            filteredData.forEach(p ->{
               actualList.add(p);
            });
        });
        
        //3. Wrap the FilteredList in a SortedList
        SortedList<Entry> sortedData=new SortedList<>(filteredData);
        
        //4. Bind the SortedList comparator to the TableView comparator. Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        
        //5. Add sorted and filtered data to the table
        table.setItems(sortedData);
    }

    @FXML
    /**
     * Opens popup window with contact info
     */
    private void ContactButtonAction(ActionEvent event) {
         //creates popup window
        Stage popupwindow=new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Hi!");

        //Warning message
        Label label1= new Label("Thank you for trying out my first spreadsheet program! \nIf you have any questions, please contact me at\n myemail@gmail.com");
        label1.setAlignment(Pos.CENTER);
        label1.setTextAlignment(TextAlignment.CENTER);
        
        //position of all elements in window is determined
        VBox Vlayout= new VBox(10);

        //elements added
        Vlayout.getChildren().add(label1);

        Vlayout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(Vlayout, 400, 300);

        popupwindow.setOnCloseRequest(e ->{
            event.consume();
        });
        popupwindow.setScene(scene1);
        popupwindow.show();
    }

    @FXML
    /**
     * Tutorial to be implemented in a future release
     */
    private void TutorialButtonAction(ActionEvent event) {
    }
    
}
