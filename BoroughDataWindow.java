import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * The class responsible for the design on the window displaying covid data for a specific borough
 * within a specific date range.
 *
 * @author Cora Bowers
 * @version 15/03/2024
 */
public class BoroughDataWindow extends Application
{
    // The list of options for the drop down menu
    private final ObservableList<String> COLUMNS = FXCollections.observableArrayList ("Date", "Retail Recreation GMR", "Grocery/Pharamacy GMR", "Parks GMR", "Transit GMR", "Workplaces GMR", "Residential GMR", "New Cases", "Total Cases", "New Deaths");
    
    private ComboBox<String> dropDown;
    
    private String borough;
    //private BoroughDataLoader boroughDataLoader;
    private CovidDataLoader covidDataLoader;
    private ArrayList<CovidData> boroughData;
    
    public BoroughDataWindow(String borough, Date startDate, Date endDate) throws java.text.ParseException {
        super();
        dropDown = createDropDown();
        this.borough = borough;
        //this.boroughDataLoader = new BoroughDataLoader(borough, startDate, endDate);
        //this.boroughData = boroughDataLoader.load();
        this.covidDataLoader = new CovidDataLoader();
        this.boroughData = covidDataLoader.loadBoroughData(borough, startDate, endDate);
    }
    
    /**
     * Add all components to the stage and display the stage onscreen.
     *
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage)
    {
        Scene scene = new Scene(createRoot());
        stage.setTitle(borough + " Data"); //change to specific borough name
        stage.setScene(scene);

        stage.show();
    }
    
    /**
     * Create the root of the GUI and add the components
     * 
     * @return a BorderPane to act as the root.
     */
    private BorderPane createRoot(){
        BorderPane root = new BorderPane();
        root.setTop(dropDown);
        root.setCenter(createStats());
        
        return root;
    }
    
    /**
     * Create the drop down menu to provide the user with the option sort the data by a specific
     * category
     * 
     * @return a ComboBox to act as a drop down menu.
     */
    private ComboBox<String> createDropDown(){
        ComboBox<String> dropDown = new ComboBox<>(COLUMNS);
        
        dropDown.setValue("Sort by");
        
        return dropDown;
    }
    
    /**
     * Create the table containing all the covid statistics for the borough (providing the functionality
     * to sort the statistics), add the statisitcs to it and place it in a VBox.
     * 
     * @return a VBox containing the table of covid date for the borough
     */
    private VBox createStats(){
        TableView<CovidData> tableView = new TableView<>();
        
        TableColumn<CovidData, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<CovidData, String>("date"));
        
        TableColumn<CovidData, Integer> retailRecreationGMRColumn = new TableColumn<>("Retail Recreation GMR");
        retailRecreationGMRColumn.setCellValueFactory(new PropertyValueFactory<CovidData, Integer>("retailRecreationGMR"));
        
        TableColumn<CovidData, Integer> groceryPharmacyGMRColumn = new TableColumn<>("Grocery/Pharmacy GMR");
        groceryPharmacyGMRColumn.setCellValueFactory(new PropertyValueFactory<CovidData, Integer>("groceryPharmacyGMR"));
        
        TableColumn<CovidData, Integer> parksGMRColumn = new TableColumn<>("Parks GMR");
        parksGMRColumn.setCellValueFactory(new PropertyValueFactory<CovidData, Integer>("parksGMR"));
        
        TableColumn<CovidData, Integer> transitGMRColumn = new TableColumn<>("Transit GMR");
        transitGMRColumn.setCellValueFactory(new PropertyValueFactory<CovidData, Integer>("transitGMR"));
        
        TableColumn<CovidData, Integer> workplacesGMRColumn = new TableColumn<>("Workplaces GMR");
        workplacesGMRColumn.setCellValueFactory(new PropertyValueFactory<CovidData, Integer>("workplacesGMR"));
        
        TableColumn<CovidData, Integer> residentialGMRColumn = new TableColumn<>("Residential GMR");
        residentialGMRColumn.setCellValueFactory(new PropertyValueFactory<CovidData, Integer>("residentialGMR"));
        
        TableColumn<CovidData, Integer> newCasesColumn = new TableColumn<>("New Cases");
        newCasesColumn.setCellValueFactory(new PropertyValueFactory<CovidData, Integer>("newCases"));
        
        TableColumn<CovidData, Integer> totalCasesColumn = new TableColumn<>("Total Cases");
        totalCasesColumn.setCellValueFactory(new PropertyValueFactory<CovidData, Integer>("totalCases"));
        
        TableColumn<CovidData, Integer> newDeathsColumn = new TableColumn<>("New Deaths");
        newDeathsColumn.setCellValueFactory(new PropertyValueFactory<CovidData, Integer>("newDeaths"));
        
        ObservableList<CovidData> observableBoroughData = FXCollections.observableArrayList(boroughData);
        tableView.setItems(observableBoroughData);
        tableView.getColumns().addAll(dateColumn, retailRecreationGMRColumn, groceryPharmacyGMRColumn, parksGMRColumn, transitGMRColumn, workplacesGMRColumn, residentialGMRColumn, newCasesColumn, totalCasesColumn, newDeathsColumn);
        
        dropDown.setOnAction(e -> {
            String selectedOption = dropDown.getValue();
            if(selectedOption.equals(COLUMNS.get(0))){
                tableView.getSortOrder().setAll(dateColumn);
            }
            else if(selectedOption.equals(COLUMNS.get(1))){
                tableView.getSortOrder().setAll(retailRecreationGMRColumn);
            }
            else if(selectedOption.equals(COLUMNS.get(2))){
                tableView.getSortOrder().setAll(groceryPharmacyGMRColumn);
            }
            else if(selectedOption.equals(COLUMNS.get(3))){
                tableView.getSortOrder().setAll(parksGMRColumn);
            }
            else if(selectedOption.equals(COLUMNS.get(4))){
                tableView.getSortOrder().setAll(transitGMRColumn);
            }
            else if(selectedOption.equals(COLUMNS.get(5))){
                tableView.getSortOrder().setAll(workplacesGMRColumn);
            }
            else if(selectedOption.equals(COLUMNS.get(6))){
                tableView.getSortOrder().setAll(residentialGMRColumn);
            }
            else if(selectedOption.equals(COLUMNS.get(7))){
                tableView.getSortOrder().setAll(newCasesColumn);
            }
            else if(selectedOption.equals(COLUMNS.get(8))){
                tableView.getSortOrder().setAll(totalCasesColumn);
            }
            else if(selectedOption.equals(COLUMNS.get(9))){
                tableView.getSortOrder().setAll(newDeathsColumn);
            }
        });
        
        VBox container = new VBox(tableView);
        return container;
    }
}
