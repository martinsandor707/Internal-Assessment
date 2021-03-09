/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml_internalassessment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author nando
 */
public class Main extends Application {
    //The current state of the database
  public static ArrayList<Entry> Entries;
  public static ArrayList<Lessee> LesseeList;
  //Used for the "rewind" and "fast forward" functions
  public static RewindLinkedList currentNode;
  public static LesseeRewindLinkedList currentLesseeNode;
    
    @Override
    public void start(Stage stage) throws Exception {
        Entries=new ArrayList<>();
        LesseeList=new ArrayList<>();
        JSONParser parser=new JSONParser();
        JSONParser parser2=new JSONParser();
        
        try{
            //Reads all elements from the json database and parses it into Entries for later use
            Object obj= parser.parse(new FileReader("Output.json"));
            JSONArray jsonArray=(JSONArray)obj;            
            jsonArray.forEach(p ->parseEntryObj((JSONObject)p));
            //Immediately create a temporary copy of the database
            FileWriter file=new FileWriter("OutputTemporary.json");
            file.write(jsonArray.toJSONString());
            file.flush();        
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(Exception e){
            
        }
        
        try{
            //Do the same for the list of lessees
            Object obj2= parser2.parse(new FileReader("LesseeList.json"));
            JSONArray jsonArray2=(JSONArray)obj2;            
            jsonArray2.forEach(p ->parseLesseeObj((JSONObject)p));

            FileWriter file2=new FileWriter("LesseeListTemporary.json");
            file2.write(jsonArray2.toJSONString());
            file2.flush();
        
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        catch(Exception e){
            
        }
        
        currentNode=new RewindLinkedList(Entries, null, null);
        currentLesseeNode=new LesseeRewindLinkedList(LesseeList, null, null);
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.setTitle("My First Spreadsheet App");
        
        //before the program is closed, this is called
        stage.setOnCloseRequest(confirmCloseRequestHandler);
        
        stage.show();
        
    }
    
    //Deletes backup database when closing the program
    @Override
    public void stop(){
        
        
        try{
            File temp=new File("OutputTemporary.json");
            File temp2=new File("LesseeListTemporary.json");
            Path path= Paths.get(temp.getAbsolutePath());
            Path path2= Paths.get(temp2.getAbsolutePath());
            Files.deleteIfExists(path);
            Files.deleteIfExists(path2);
        }

        catch(DirectoryNotEmptyException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    /**
    *Displays pop-up window  asking whether the user wants to save before exiting the application
    */
    private EventHandler<WindowEvent> confirmCloseRequestHandler= event ->{
        if (!isSameContent())
        {
        //creates popup window
        Stage popupwindow=new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Figyelmeztetés");

        //Warning message
        Label label1= new Label("Nem mentetted el a változtatásaidat! \nAkarsz menteni mielőtt kilépnél?");
        label1.setAlignment(Pos.CENTER);

        //Yes, No and Cancel Buttons
        Button buttonNo= new Button("Nem");   
        buttonNo.setOnAction(e -> popupwindow.close());
        
        //Invokes FXMLDocumentController's HandleSaveData method before closing the entire application
        Button buttonYes=new Button("Igen");
        buttonYes.setOnAction(e ->{
            
            FXMLDocumentController app=new FXMLDocumentController();
            LesseeListController app2=new LesseeListController();
            app.HandleSaveData(e);
            app2.SaveButtonAction(e);
            popupwindow.close();
        });
        //Consumes the event, therefore the application will not close
        Button buttonCancel=new Button("Mégse");
        buttonCancel.setOnAction(e -> {
            event.consume();
            popupwindow.close();
        });
        
        //position of all elements in window is determined
        VBox Vlayout= new VBox(10);
        HBox Hlayout=new HBox(10);
        
        Hlayout.getChildren().addAll(buttonYes, buttonNo, buttonCancel);
        Hlayout.setAlignment(Pos.CENTER);
        //elements added
        Vlayout.getChildren().addAll(label1, Hlayout);

        Vlayout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(Vlayout, 300, 250);

        popupwindow.setOnCloseRequest(e ->{
            event.consume();
        });
        popupwindow.setScene(scene1);

        
        
        popupwindow.showAndWait();
        }
            };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    //Used after the elements of Output.json are contained in a JSONArray
    private void parseEntryObj(JSONObject p){
        JSONObject userObj = (JSONObject)p.get("Entry");
        Entry p1=new Entry();
        
        p1.setDate((String)userObj.get("Date"));
        
        p1.setType((String)userObj.get("Type"));
        p1.setPaid_by((String)userObj.get("Paid_by"));
        p1.setComment((String)userObj.get("Comment"));
        //NUMBERS IN JSON ARE STORED AS LONG BY DEFAULT
        p1.setAmount((int) ((long)userObj.get("Amount")));
        p1.setRow((int) ((long)userObj.get("Row")));
        Entries.add(p1);
    }
    //Used after the elements of LesseeList.json are contained in a JSONArray
    private void parseLesseeObj(JSONObject p){
        JSONObject userObj = (JSONObject)p.get("Lessee");
        Lessee p1=new Lessee();
        
        p1.setName((String)userObj.get("Name"));
        
        p1.setAddress((String)userObj.get("Address"));
        p1.setPhone_number((String)userObj.get("Phone_number"));
        p1.setEmail((String)userObj.get("Email"));
        p1.setComments((String)userObj.get("Comments"));
        LesseeList.add(p1);
    }
    
    
    /**
    *Checks the contents of Output.json and OutputTemporary.json and returns true if their content is identical, false otherwise
    * */
    private boolean isSameContent(){
        try{
            //initializing variables referring to the two permanent and temporary databases
            File file1=new File("Output.json");
            File file2=new File("OutputTemporary.json");
            File file3=new File("LesseeList.json");
            File file4=new File("LesseeListTemporary.json");
            //Reading their contents byte-by-byte into an array of bytes
            byte[] f1=Files.readAllBytes(file1.toPath());
            byte[] f2=Files.readAllBytes(file2.toPath());
            byte[] f3=Files.readAllBytes(file3.toPath());
            byte[] f4=Files.readAllBytes(file4.toPath());
            //Checks whether their contents are exactly the same
            boolean isArrayEqual=Arrays.equals(f1, f2);
            boolean isLesseeListEqual=Arrays.equals(f3, f4);
            
            if (isArrayEqual==true && isLesseeListEqual==true) return true;
        }
        catch (IOException e){
            e.printStackTrace();
            return true;
        }
        

        return false;
    }   
    
}
