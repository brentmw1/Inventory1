//Brent Wickenheiser
//Project 6
/*Allows user to enter and search items in an inventory list that will save
between runs of the program*/
package inventory;
import javafx.stage.*;
import java.io.*;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;


public class Inventory extends Application{
    public Stage stage;
    public ToolBar toolBar;
    public Button addButton, findButton, deleteButton, listButton, saveButton;
    public VBox mainScreen;
    
    static int numEntries;
    public static Entry[] entryList = new Entry[200];
    
    public void start(Stage stage) {
        this.stage = stage;
        addButton = new Button("Add Entry");
        findButton = new Button("Find Entry");
        deleteButton = new Button("Delete Entry");
        listButton = new Button("List Entries");
        saveButton = new Button("Save Entries");
        mainScreen = new VBox();
        toolBar = new ToolBar(addButton, findButton, deleteButton, listButton, saveButton);
        //toolBar.setMinHeight(50);
        this.stage.setTitle("Inventory");
        mainScreen.getChildren().addAll(toolBar);
        this.stage.setScene(new Scene(mainScreen, 500, 650));
        this.stage.show();
        this.stage.setMinWidth(520);
        
        addButton.setOnAction(event -> {
            addEntry();
        });
        findButton.setOnAction(event -> {
            findEntry();
        });
        deleteButton.setOnAction(event -> {
            deleteEntry();
        });
        saveButton.setOnAction(event -> {
            try {
                storeInventory(numEntries);
            }catch (Exception e) {
                
            }
        });
        listButton.setOnAction(event -> {
            listEntries();
        });
    }
    
    public void addEntry() {
        Stage addStage;
        GridPane G;
        Button ok;
        Text nameText, numText, notesText, instructions;
        TextField name, quantity, notes;
        VBox v;
        
        addStage = new Stage();
        G = new GridPane();
        ok = new Button("OK");
        nameText = new Text("Item Name: ");
        numText = new Text("Quantity: ");
        notesText = new Text("Notes: ");
        instructions = new Text("Enter the name, quantity, and notes for the entry.");
        v = new VBox();  
        name = new TextField("Name");
        quantity = new TextField("#");
        notes = new TextField("Notes");

        
        G.add(nameText, 1, 1);
        G.add(numText, 1, 2);
        G.add(notesText, 1, 3);
        G.add(name, 2, 1);
        G.add(quantity, 2, 2);
        G.add(notes, 2, 3);
        G.setVgap(10);
        G.setHgap(10);
        G.setAlignment(Pos.CENTER);
        G.setHalignment(numText, HPos.RIGHT);
        G.setHalignment(notesText, HPos.RIGHT);
  
        v.getChildren().addAll(instructions, G, ok);
        v.setPadding(new Insets(20, 20, 20, 20));
        v.setSpacing(10);
        v.setAlignment(Pos.CENTER);
        
        addStage.setScene(new Scene(v));
        addStage.setTitle("Add Entry");
        
        ok.setOnAction(event -> {
            String ID, note;
            int num;
            
            ID = name.getText();
            try {
                num = Integer.parseInt(quantity.getText());
                note = notes.getText();
                String nameError, numError;
                
                nameError = "";
                numError = "";
                if (ID.length() > 8 || num < 0) {
                   if (ID.length() > 8) 
                       nameError = "Item name bust be 8 or fewer characters.";
                   if (num < 0)
                       numError = "Amount must be an integer value greater than or equal to 0.";
                   v.getChildren().clear();
                   v.getChildren().addAll(instructions, new Text(nameError), new Text(numError), G, ok);
                   addStage.setMinWidth(430);
                   addStage.setMinHeight(350);
                } else {
                addEntry(ID, num, note);
                sortEntries();
                genericOK("Entry Added.", true);
                addStage.close();
                }
            } catch (Exception e) {
                v.getChildren().clear();
                v.getChildren().addAll(instructions, new Text("Amount must be an integer value greater than or equal to 0\n"), G, ok);
                addStage.setMinWidth(430);
                addStage.setMinHeight(350);//addStage.setScene(new Scene(v));
            }   
        });
        addStage.showAndWait();
    }
    
    public void addEntry(String name, int number, String notes) {
        entryList[numEntries] = new Entry();
        entryList[numEntries].name = name;
        entryList[numEntries].number = number;
        entryList[numEntries].notes = notes;
        numEntries++;
    }
    
    public void sortEntries() {
       boolean isSorted;
       int i;
       
       i = numEntries - 1;
       isSorted = false;
       while (!isSorted && i > 0) {
           if (entryList[i].name.compareTo(entryList[i - 1].name) < 0) {
               String temp;
               
               temp = entryList[i - 1].name;
               entryList[i - 1].name = entryList[i].name;
               entryList[i].name = temp;
               i--;
           } else isSorted = true;
       }
    }
    
    public void findEntry() {
        Stage findStage;
        StackPane pane;
        Button ok;
        Text instructions;
        TextField field;
        
        findStage = new Stage();
        pane = new StackPane();
        ok = new Button("OK");
        instructions = new Text("Enter the name or part of the name of the item you wish to find.");
        instructions.setFont(Font.font(20));
        field = new TextField("Item");
        
        pane.getChildren().addAll(instructions, field, ok);
        pane.setAlignment(instructions, Pos.TOP_CENTER);
        pane.setAlignment(ok, Pos.BOTTOM_CENTER);
        findStage.setScene(new Scene(pane));
        findStage.setTitle("Find Entry");
        findStage.setMinHeight(150);
        ok.setOnAction(event -> {
            String search;
            
            search = field.getText();
            if (search != null) {
                int sLength;
                boolean isFound;
                    
                sLength = search.length();
                isFound = false;
                
                for (int i = 0; i < numEntries; i++) {
                    if (search.equalsIgnoreCase(entryList[i].name.substring(0, sLength))) {
                        System.out.println("-- " + entryList[i].name);
                        System.out.println("-- " + entryList[i].number);
                        System.out.println("-- " + entryList[i].notes + "\n");
                        isFound = true;
                    }
                }
                if (!isFound) {
                    genericOK("We couldn't find the item you're looking for.\n", false);
                    findStage.close();
                } else {
                    genericOK("Item(s) Found.\n", true);
                    findStage.close();
                }
            }
        });
        findStage.showAndWait();
        
    }
    
    public void deleteEntry() {
        Stage deleteStage;
        VBox v;
        Text instructions;
        TextField f;
        Button ok;
        
        deleteStage = new Stage();
        instructions = new Text("\nEnter the name of the item you wish to delete.\n");
        f = new TextField("Name");
        instructions.setFont(Font.font(20));
        ok = new Button("Delete");
        v = new VBox();
        v.setAlignment(Pos.CENTER);
        v.getChildren().addAll(instructions, f, ok);
        deleteStage.setScene(new Scene(v));
        deleteStage.setTitle("Delete Entry");
        ok.setOnAction(event -> {
            String search;
            
            search = f.getText();
            if (search != null) {
                boolean isFound;
                    
                isFound = false;
                for (int i = 0; i < numEntries; i++) {
                    if (search.equalsIgnoreCase(entryList[i].name)) {
                        delete(i);
                        isFound = true;
                    }
                }
                if (isFound)
                    genericOK("Item Deleted", true);
                else
                    genericOK("We couldn't find the item you're looking for.\n", false);
                deleteStage.close();
            }
        });
        deleteStage.showAndWait();
        
    }
    
    public void delete(int index) {
        for (int i = index; i < numEntries - 1; i++) {
            entryList[i].name = entryList[i + 1].name;
            entryList[i].number = entryList[i + 1].number;
            entryList[i].notes = entryList[i + 1].notes;
        }
        entryList[numEntries - 1].name = null;
        entryList[numEntries - 1].number = 0;
        entryList[numEntries - 1].notes = null;
        numEntries--;
    }
    
    public void listEntries() {
        GridPane G;
        Text nameLabel, numLabel, notesLabel;
        Text[] entryName, entryNum, entryNotes;
        ColumnConstraints column1 = new ColumnConstraints();
    
        G = new GridPane();      
        nameLabel = new Text("Item Name");
        numLabel = new Text("Quantity");
        notesLabel = new Text("Notes");
        column1.setHalignment(HPos.LEFT);
        G.setVgap(5);
        G.setHgap(100);
        G.add(nameLabel, 1, 1);
        G.add(numLabel, 2, 1);
        G.add(notesLabel, 3, 1);
        G.getColumnConstraints().add(column1);
        entryName = new Text[numEntries];
        entryNum = new Text[numEntries];
        entryNotes = new Text[numEntries];
        mainScreen.getChildren().clear();        
        
        for (int i = 0; i < numEntries; i++) {
            entryName[i] = new Text(entryList[i].name);
            entryNum[i] = new Text(entryList[i].number + "");
            entryNotes[i] = new Text(entryList[i].notes);
            if (i % 2 == 1) {
                entryName[i].setFill(Color.GREEN);
                entryNum[i].setFill(Color.GREEN);
                entryNotes[i].setFill(Color.GREEN);
            } else {
                entryName[i].setFill(Color.BLUE);
                entryNum[i].setFill(Color.BLUE);
                entryNotes[i].setFill(Color.BLUE);
            }
            G.add(entryName[i], 1, i + 2);
            G.add(entryNum[i], 2, i + 2);
            G.add(entryNotes[i], 3, i + 2);
        }
        mainScreen.getChildren().addAll(toolBar, G);
    }
    
    public void genericOK(String s, boolean b) {
        Stage okStage = new Stage();
        Text message = new Text(s);
        Button ok = new Button("OK");
        VBox v = new VBox();
        
        v.getChildren().addAll(message, ok);
        v.setAlignment(Pos.CENTER);
        if (b)
            okStage.setTitle("Success");
        else
            okStage.setTitle("Whoops");
        okStage.setScene(new Scene(v));
        okStage.setMinWidth(300);
        okStage.setMinHeight(150);
        ok.setOnAction(event -> {
           okStage.close();
        });
        okStage.showAndWait();
    }
    public static void main(String[] args) throws IOException {
        Application.launch(args);
    }
    
    public static void readInventory () throws IOException {
        BufferedReader in;
        in = new BufferedReader(new FileReader("storage.text"));
        for (int i = 0; i < 200; i++) {
            String input;
            input = in.readLine();
            if (input == null)
                i = 200;
            else {
            input = input.trim();
            entryList[i] = new Entry();
            entryList[i].name = input.substring(0, input.indexOf("\t"));
            input = input.substring(input.indexOf("\t")).trim();
            entryList[i].number = Integer.parseInt(input.substring(0, input.indexOf("\t")));
            input = input.substring(input.indexOf("\t")).trim();
            entryList[i].notes = input;
            numEntries++;
            } 
        }
        in.close();
    }

    public void storeInventory (int numEntries) 
        throws IOException {
            PrintStream P = new PrintStream("storage.text");
            for (int i = 0; i < numEntries; i++) {
                P.println(entryList[i].name + "\t" + entryList[i].number + "\t"
                + entryList[i].notes);
            }
            P.close();
            genericOK("List Saved.", true);
    }
}