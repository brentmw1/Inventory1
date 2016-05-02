// Brent Wickenheiser
// Joseph Ferrara
// Project 6
/* Allows user to enter and search items in an inventory list that will save
between runs of the program. Uses a GUI and object oriented programming */

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
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class Inventory extends Application{
    public Stage stage;
    public MenuBar menuBar;
    public Menu fileMenu, entryMenu;
    public MenuItem saveItem, loadItem, addItem, findItem, deleteItem,
            listItem;
    public ToolBar toolBar;
    public VBox mainScreen;
    static int numEntries;
    public static Entry[] entryList = new Entry[200];
    
    public void start(Stage stage) {
        this.stage = stage;
        menuBar    = new MenuBar();
        fileMenu   = new Menu("File");
        entryMenu  = new Menu("Entry");
        saveItem   = new MenuItem("Save Entries");
        loadItem   = new MenuItem("Load Entries");
        addItem    = new MenuItem("Add");
        findItem   = new MenuItem("Find");
        deleteItem = new MenuItem("Delete");
        listItem   = new MenuItem("List");
        mainScreen = new VBox();
        fileMenu  .getItems().addAll(saveItem, loadItem);
        entryMenu .getItems().addAll(addItem, findItem, deleteItem, listItem);
        menuBar   .getMenus().addAll(fileMenu, entryMenu);
        this.stage.setTitle("Inventory");
        mainScreen.getChildren().addAll(menuBar);
        mainScreen.setPadding(new Insets(0, 0, 0, 0));
        this.stage.setScene(new Scene(mainScreen));
        this.stage.show();
        this.stage.setMinWidth(450);
        this.stage.setMinHeight(650);
        addItem.setOnAction   (ActionEvent -> {
            addEntry();
        });  
        findItem.setOnAction  (ActionEvent -> {
            findEntry();
        });
        deleteItem.setOnAction(ActionEvent -> {
            deleteEntry();
        });
        saveItem.setOnAction  (ActionEvent -> {
            saveOrLoad("Enter a name for the file.", true);
        });
        loadItem.setOnAction  (ActionEvent -> {
            saveOrLoad("Enter the name of the file you wish to load.", false);
        });
        listItem.setOnAction  (ActionEvent -> {
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
        
        addStage     = new Stage();
        G            = new GridPane();
        ok           = new Button("OK");
        nameText     = new Text("Item Name: ");
        numText      = new Text("Quantity: ");
        notesText    = new Text("Notes: ");
        instructions = new Text("Enter the name, quantity, and notes for"
                + " the entry.");
        v            = new VBox();  
        name         = new TextField("Name");
        quantity     = new TextField("#");
        notes        = new TextField("N/A");
        G.add(nameText, 1, 1);
        G.add(numText, 1, 2);
        G.add(notesText, 1, 3);
        G.add(name, 2, 1);
        G.add(quantity, 2, 2);
        G.add(notes, 2, 3);
        G.setVgap(10);
        G.setHgap(10);
        G.setAlignment (Pos.CENTER);
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
                num  = Integer.parseInt(quantity.getText());
                note = notes.getText().trim();
                if (note.equals(""))
                       note = "N/A";
                String nameError, numError, noteError;
                nameError = "";
                numError  = "";
                if (ID.length() > 8 || num < 0) {
                   if (ID.length() > 8) 
                       nameError = "Item name bust be 8 or fewer characters.";
                   if (num < 0)
                       numError = "Amount must be an integer value greater"
                               + " than or equal to 0.";
                   v.getChildren().clear();
                   v.getChildren().addAll(instructions, new Text(nameError),
                           new Text(numError), G, ok);
                   addStage.setMinWidth(430);
                   addStage.setMinHeight(350);
                } else {
                    addEntry(ID, num, note);
                    sortEntries();
                    genericOK("Entry Added.", true);
                    listEntries();
                    addStage.close();
                }
            } catch (Exception e) {
                v.getChildren().clear();
                v.getChildren().addAll(instructions, new Text("Amount must be"
                        + " an integer value greater than or equal to 0\n"),
                        G, ok);
                addStage.setMinWidth (430);
                addStage.setMinHeight(350);//addStage.setScene(new Scene(v));
            }   
        });
        addStage.showAndWait();
    }
    
    public void addEntry(String name, int number, String notes) {
        entryList[numEntries]        = new Entry();
        entryList[numEntries].name   = name;
        entryList[numEntries].number = number;
        entryList[numEntries].notes  = notes;
        numEntries++;
    }
    
    public void sortEntries() {
       boolean isSorted;
       int i;
       
       i = numEntries - 1;
       isSorted = false;
       while (!isSorted && i > 0) {
           if (entryList[i].name.compareToIgnoreCase(entryList[i - 1].name) 
                   < 0) {
               String tempName, tempNotes;
               int tempNum;
               
               tempName  = entryList[i - 1].name;
               tempNum   = entryList[i - 1].number;
               tempNotes = entryList[i - 1].notes;
               entryList[i - 1].name   = entryList[i].name;
               entryList[i - 1].number = entryList[i].number;
               entryList[i - 1].notes  = entryList[i].notes;
               entryList[i].name   = tempName;
               entryList[i].number = tempNum;
               entryList[i].notes  = tempNotes;
               i--;
           } else isSorted = true;
       }
    }
    
    public void findEntry() {
        Stage findStage;
        VBox v;
        Button ok;
        Text instructions;
        TextField field;
        
        findStage    = new Stage();
        v            = new VBox();
        ok           = new Button("OK");
        instructions = new Text("Enter the name or part of the name of the"
                + " item you wish to find.");
        field        = new TextField("Item");
        v.getChildren().addAll(instructions, field, ok);
        v.setAlignment(Pos.CENTER);
        v.setPadding(new Insets(10, 10, 10, 10));
        v.setSpacing(10);
        findStage.setScene(new Scene(v));
        findStage.setTitle("Find Entry");
        findStage.setMinHeight(150);
        ok.setOnAction(event -> {
            String search;
            
            search = field.getText();
            if (field.getText() != null) {
                GridPane G;
                ScrollPane S;
                Text nameLabel, numLabel, notesLabel;
                Text[] entryName, entryNum, entryNotes;
                ColumnConstraints column1 = new ColumnConstraints();
                ColumnConstraints column2 = new ColumnConstraints();
                int x;
    
                x = 0;
                G = new GridPane();      
                nameLabel  = new Text("Item Name     ");
                numLabel   = new Text("Quantity   ");
                notesLabel = new Text("Notes");
                G.setVgap(5);
                G.add(nameLabel, 1, 1);
                G.add(numLabel, 2, 1);
                G.add(notesLabel, 3, 1);
                G.getColumnConstraints().addAll(column1, column1, column2);
                G.setPadding(new Insets(10, 10, 10, 10));
                entryName  = new Text[numEntries];
                entryNum   = new Text[numEntries];
                entryNotes = new Text[numEntries];
                mainScreen.getChildren().clear();          
                int sLength;
                boolean isFound;
                    
                sLength = search.length();
                isFound = false;
                
                for (int i = 0; i < numEntries; i++) {
                    if (sLength <= entryList[i].name.length())
                        if (search.equalsIgnoreCase(entryList[i].name
                                .substring(0, sLength))) {
                            entryName[x]  = new Text(entryList[i].name);
                            entryNum[x]   = new Text(entryList[i].number 
                                    + "");
                            entryNotes[x] = new Text(entryList[i].notes);
                            if (x % 2 == 1) {
                                entryName[x].setFill (Color.BLUE);
                                entryNum[x].setFill  (Color.BLUE);
                                entryNotes[x].setFill(Color.BLUE);
                            } else {
                                entryName[x].setFill (Color.GREEN);
                                entryNum[x].setFill  (Color.GREEN);
                                entryNotes[x].setFill(Color.GREEN);
                            }
                            G.add( entryName[x], 1, x + 2);
                            G.add(  entryNum[x], 2, x + 2);
                            G.add(entryNotes[x], 3, x + 2);
                            x++;
                            isFound = true;
                        }
                }
                S = new ScrollPane(G);
                mainScreen.getChildren().addAll(menuBar, S);
                mainScreen.setPadding(new Insets(0, 0, 0, 0));
                if (!isFound) {
                    genericOK("We couldn't find the item you're looking for.",
                            false);
                    findStage.close();
                } else {
                    genericOK("Item(s) Found.", true);
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
        
        deleteStage  = new Stage();
        instructions = new Text("Enter the name of the item you wish to"
                + " delete.");
        f = new TextField("Name");
        ok = new Button("Delete");
        v  = new VBox();
        v.setAlignment(Pos.CENTER);
        v.setPadding(new Insets(10, 10, 10, 10));
        v.setSpacing(10);
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
                if (isFound) {
                    genericOK("Item Deleted", true);
                    listEntries();
                } else
                    genericOK("We couldn't find the item you're"
                            + " looking for.", false);
                deleteStage.close();
            }
        });
        deleteStage.showAndWait();
    }
    
    public void delete(int index) {
        for (int i = index; i < numEntries - 1; i++) {
            entryList[i].name   = entryList[i + 1].name;
            entryList[i].number = entryList[i + 1].number;
            entryList[i].notes  = entryList[i + 1].notes;
        }
        entryList[numEntries - 1].name   = null;
        entryList[numEntries - 1].number = 0;
        entryList[numEntries - 1].notes  = null;
        numEntries--;
    }
    
    public void listEntries() {
        GridPane G;
        ScrollPane S;
        Text nameLabel, numLabel, notesLabel;
        Text[] entryName, entryNum, entryNotes;
    
        G = new GridPane();      
        nameLabel  = new Text("Item Name     ");
        numLabel   = new Text("Quantity   ");
        notesLabel = new Text("Notes");
        G.setVgap(5);
        G.add(nameLabel, 1, 1);
        G.add(numLabel, 2, 1);
        G.add(notesLabel, 3, 1);
        G.setPadding(new Insets(10, 10, 10, 10));
        entryName  = new Text[numEntries];
        entryNum   = new Text[numEntries];
        entryNotes = new Text[numEntries];
        mainScreen.getChildren().clear();
        for (int i = 0; i < numEntries; i++) {
            entryName[i]  = new Text(entryList[i].name);
            entryNum[i]   = new Text(entryList[i].number + "");
            entryNotes[i] = new Text(entryList[i].notes);
            if (i % 2 == 1) {
                entryName[i].setFill (Color.BLUE);
                entryNum[i].setFill  (Color.BLUE);
                entryNotes[i].setFill(Color.BLUE);
            } else {
                entryName[i].setFill (Color.GREEN);
                entryNum[i].setFill  (Color.GREEN);
                entryNotes[i].setFill(Color.GREEN);
            }
            G.add(entryName[i],  1, i + 2);
            G.add(entryNum[i],   2, i + 2);
            G.add(entryNotes[i], 3, i + 2);
        }
        S = new ScrollPane(G);
        mainScreen.getChildren().addAll(menuBar, S);
        mainScreen.setPadding(new Insets(0, 0, 0, 0));
    }
    
    public void genericOK(String m, boolean isSuccess) {
        Stage okStage = new Stage();
        Text message  = new Text(m);
        Button ok     = new Button("OK");
        VBox v        = new VBox();
        
        v.getChildren().addAll(message, ok);
        v.setAlignment(Pos.CENTER);
        v.setPadding(new Insets(10, 10, 10, 10));
        v.setSpacing(10);
        if (isSuccess)
            okStage.setTitle("Success");
        else
            okStage.setTitle("Whoops");
        okStage.setScene(new Scene(v));
        ok.setOnAction(ActionEvent -> {
           okStage.close();
        });
        okStage.showAndWait();
    }
    
    public void saveOrLoad(String m, boolean isSave) {
        Stage fieldStage = new Stage();
        Text message     = new Text(m);
        TextField file   = new TextField("File");
        Button ok        = new Button("OK");
        VBox v           = new VBox();
        
        v.getChildren().addAll(message, file, ok);
        v.setAlignment(Pos.CENTER);
        v.setPadding(new Insets(10, 10, 10, 10));
        v.setSpacing(10);
        if (isSave)
            fieldStage.setTitle("Save");
        else
            fieldStage.setTitle("Load");
        fieldStage.setScene(new Scene(v));
        ok.setOnAction(event -> {
            if (isSave) {
                try {
                    storeInventory(numEntries, file.getText());
                    fieldStage.close();
                } catch (Exception e) {
                    v.getChildren().clear();
                    v.getChildren().addAll(new Text("Invalid file name. "
                            + "Try again."), file, ok);
                }
            } else {
                try {
                    readInventory(file.getText());
                    sortEntries();
                    genericOK("File loaded.", true);
                    listEntries();
                    fieldStage.close();
                } catch (Exception e) {
                    v.getChildren().clear();
                    genericOK("File not found.", false);
                    fieldStage.close();
                }
            }
        });
        fieldStage.showAndWait(); 
    }
    public static void main(String[] args) throws IOException {
        Application.launch(args);
    }
    
    public static void readInventory (String file) throws IOException {
        BufferedReader in;
        in = new BufferedReader(new FileReader(file + ".text"));
        numEntries = 0;
        for (int i = 0; i < 200; i++) {
            String input;
            
            input = in.readLine();
            if (input == null)
                i = 200;
            else {
            input = input.trim();
            entryList[i] = new Entry();
            entryList[i].name   = input.substring(0, input.indexOf("\t"));
            input = input.substring(input.indexOf("\t")).trim();
            entryList[i].number = Integer.parseInt(input.substring(0, 
                    input.indexOf("\t")));
            input = input.substring(input.indexOf("\t")).trim();
            entryList[i].notes  = input;
            numEntries++;
            } 
        }
        in.close();
    }

    public void storeInventory (int numEntries, String file) 
        throws IOException {
            PrintStream P = new PrintStream(file + ".text");
            for (int i = 0; i < numEntries; i++) {
                P.println(entryList[i].name + "\t" + entryList[i].number 
                        + "\t" + entryList[i].notes);
            }
            P.close();
            genericOK("List Saved.", true);
    }
}