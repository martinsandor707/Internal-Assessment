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
public class FXML_InternalAssessment extends Application {
    //The current state of the database
  public static ArrayList<Entry> Entries;
  //Used for the "rewind" and "fast forward" functions
  public static RewindLinkedList currentNode;

  FXMLDocumentController app=new FXMLDocumentController();
  

    public static void setEntries(ArrayList<Entry> Entries) {
        FXML_InternalAssessment.Entries = Entries;
    }

    public static List<Entry> getEntries() {
        return Entries;
    }
    
    public static void setCurrentNode(RewindLinkedList currentNode) {
        currentNode = currentNode;
    }

    public static RewindLinkedList getCurrentNode() {
        return currentNode;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        Entries=new ArrayList<>();
        JSONParser parser=new JSONParser();
        
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
        
        currentNode=new RewindLinkedList(Entries, null, null);
        
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
            Path path= Paths.get(temp.getAbsolutePath());
            Files.deleteIfExists(path);
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
        popupwindow.setTitle("This is a pop up window");

        //Warning message
        Label label1= new Label("You haven't saved your changes! \nDo you want to save before exiting?");
        label1.setAlignment(Pos.CENTER);

        //Yes, No and Cancel Buttons
        Button buttonNo= new Button("No");   
        buttonNo.setOnAction(e -> popupwindow.close());
        
        //Invokes FXMLDocumentController's HandleSaveData method before closing the entire application
        Button buttonYes=new Button("Yes");
        buttonYes.setOnAction(e ->{
            
            FXMLDocumentController app=new FXMLDocumentController();
            app.HandleSaveData(e);
            popupwindow.close();
        });
        //Consumes the event, therefore the application will not close
        Button buttonCancel=new Button("Cancel");
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
        System.out.println("Third line of ParseEntry");
        Entries.add(p1);
    }
    /**
    *Checks the contents of Output.json and OutputTemporary.json and returns true if their content is identical, false otherwise
    * */
    private boolean isSameContent(){
        try{
            //initializing variables referring to the two databases
            File file1=new File("Output.json");
            File file2=new File("OutputTemporary.json");
            //Reading their contents byte-by-byte into an array of bytes
            byte[] f1=Files.readAllBytes(file1.toPath());
            byte[] f2=Files.readAllBytes(file2.toPath());
            //Checks whether their contents are exactly the same
            boolean isTwoEqual=Arrays.equals(f1, f2);
            
            return isTwoEqual;
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }   
    
}
