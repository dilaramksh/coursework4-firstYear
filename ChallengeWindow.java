import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
 
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
 
/**
* This class generates a pane that is used in the CovidApplicationWindow to generate the interface of the challenge pane
*
* @author Dilara Mukasheva and Cora Bowers
* @version 21/03/2024
*/
public class ChallengeWindow {
   
    private ArrayList<String> boroughNames = new ArrayList<String>(Arrays.asList("Barking And Dagenham", "Barnet", "Bexley", "Brent", "Bromley", "Camden", "City Of London", "Westminster", "Croydon", "Ealing", "Enfield", "Greenwich", "Hackney", "Hammersmith And Fulham", "Haringey", "Harrow", "Havering", "Hillingdon", "Hounslow", "Islington", "Kensington And Chelsea", "Kingston Upon Thames", "Lambeth", "Lewisham", "Merton", "Newham", "Redbridge", "Richmond Upon Thames", "Southwark", "Sutton", "Tower Hamlets", "Waltham Forest", "Wandsworth"));
   
    CovidDataEditor editor = new CovidDataEditor();
    CovidDataLoader loader = new CovidDataLoader();
 
   
    
    
    public Pane createChallengeComponent() {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("center-box");
       
        VBox vbox = new VBox(3);
        HBox HBOXborough = new HBox(2);
        HBox HBOXnewcases = new HBox(2);
        HBox HBOXnewdeaths = new HBox(2);
 
        Label header = new Label("ADD NEW DATA LOGGER");
        header.getStyleClass().add("statistics-title");
       
        Label boroughLabel = new Label("Borough");
        boroughLabel.setPrefSize(350, 110);
        ComboBox<String> boroughComboBox = new ComboBox<>(FXCollections.observableArrayList(boroughNames)); // Insert the borough data
        boroughComboBox.setPrefWidth(350);
        //boroughComboBox.setAlignment(Pos. CENTER);
        HBOXborough.getChildren().addAll(boroughLabel, boroughComboBox);
 
        Label newcasesLabel = new Label("Total New Cases: ");
        newcasesLabel.setPrefSize(350, 110);
        TextField newcasesTextField = new TextField();
        newcasesTextField.setPrefWidth(350);
        HBOXnewcases.getChildren().addAll(newcasesLabel, newcasesTextField);
 
        Label newdeathsLabel = new Label("Total New Death: ");
        newdeathsLabel.setPrefSize(350, 110);
        TextField newdeathsTextField = new TextField();
        newdeathsTextField.setPrefWidth(350);
        HBOXnewdeaths.getChildren().addAll(newdeathsLabel, newdeathsTextField);
       
        Button submitButton = new Button("Submit");
        submitButton.getStyleClass().add("navigationButton");
 
        submitButton.setOnAction(e->{
            if(boroughComboBox.getValue() == null || newcasesTextField.getText().trim().equals("") || newdeathsTextField.getText().trim().equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Data not present");
                alert.setHeaderText("Partial data entered");
                alert.setContentText("Enter data for all the fields");
                alert.showAndWait();
            }
            else{
                try{
                    LocalDate todaysDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String todayDate = todaysDate.format(formatter);
 
                    editor.addNewRow(todayDate, boroughComboBox.getValue(), newcasesTextField.getText(), newdeathsTextField.getText());
                    editor.saveChanges();
                    newcasesTextField.clear();
                    newdeathsTextField.clear();
                    boroughComboBox.getSelectionModel().clearSelection();
                   
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setHeaderText("Data added");
                    alert.showAndWait();
                }
                catch(NumberFormatException exception){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Something went wrong");
                    alert.setHeaderText("Data of the wrong format was entered");
                    alert.setContentText("Enter data of the correct format");
                    alert.showAndWait();
                }
            }
        }
        );
       
        vbox.getChildren().addAll(HBOXborough, HBOXnewcases, HBOXnewdeaths);
 
        root.add(header, 0, 0);
        root.add(boroughLabel, 0, 1);
        root.add(boroughComboBox, 1, 1);
        root.add(newcasesLabel, 0, 2);
        root.add(newcasesTextField, 1, 2);
        root.add(newdeathsLabel, 0, 3);
        root.add(newdeathsTextField, 1, 3);
        root.add(submitButton, 1, 4);
        submitButton.setAlignment(Pos.CENTER_RIGHT);
 
        return root;
    }
   
    
    
}