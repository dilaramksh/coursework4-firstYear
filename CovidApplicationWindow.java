import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
 
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The CovidApplicationWindow class is the JavaFX class that makes the application visible as well as interactive
 * @author Dilara Mukasheva, Cora Bowers, Lucia Garces, Martin Berthoud
 * @version 20/03/2024
 */
 
public class CovidApplicationWindow extends Application {
    
    private ComboBox fromComboBox;
    private ComboBox toComboBox;

    private static CovidDataLoader CDL = new CovidDataLoader();
    private StatisticsWindow statisticsWindow = new StatisticsWindow();
    private ChallengeWindow challengeWindow = new ChallengeWindow();

    private int WIDTH = 700;
    private int HEIGHT = 550;
 
    private BorderPane rootPane;
    private VBox pane1 = new VBox(); //Welcome pane
    private AnchorPane pane2 = new AnchorPane(); //Map pane
    private HBox pane3 = new HBox(); //Statistics pane
    private HBox pane4 = new HBox(); //challenge pane

    private ArrayList<Pane> paneCarousel = new ArrayList<Pane>(Arrays.asList(pane1, pane2, pane3, pane4)); 
    private int paneCounter = 0; //this allows to loop through the list of panes using the forward, backward button
    
    public static void main(String[] args) {
        launch(args);
    }
 
    /**
     * This creates the application.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
       
        
        Group root = new Group();
 
        rootPane = new BorderPane();
        //rootPane.getStylesheets().add("style.css");
        //Date Panel where the user can select dates from and to
        HBox datePane = new HBox();
        datePane.setBorder(new Border(new BorderStroke(null, null, Color.BLACK, null, null, null, BorderStrokeStyle.SOLID, null, CornerRadii.EMPTY, BorderWidths.DEFAULT,null )));
        datePane.setPrefSize(WIDTH, 15);
 
        Label fromText = new Label("From:");
        fromComboBox = new ComboBox<>(FXCollections.observableArrayList(CDL.getDates()));
        fromComboBox.setPrefSize(150,15);
        Label toText = new Label("To:");
        toComboBox = new ComboBox<>(FXCollections.observableArrayList(CDL.getDates()));
        toComboBox.setPrefSize(150,15);
 
        datePane.getChildren().addAll(fromText, fromComboBox, toText, toComboBox);
        datePane.setSpacing(10.0);
 
        //initialising the panels using the methods below
        createWelcomePane();
        createMapPane();
        createStatsPane();
        createChallengePane();
 
        //Change panel for going back and forwards between the panels above
        HBox changePane = new HBox();
        changePane.setBorder(new Border(new BorderStroke(Color.BLACK, Color.WHITE, Color.WHITE, Color.WHITE, BorderStrokeStyle.SOLID, null, null, null, CornerRadii.EMPTY, BorderWidths.DEFAULT,null )));
        changePane.setPrefSize(WIDTH,15);
 
        Button backwardButton = new Button("<<");
        backwardButton.setDisable(true);
        backwardButton.setPrefSize(60,20);
        backwardButton.setOnAction(actionEvent -> clickBackwards());
 
        Button forwardButton = new Button(">>");
        forwardButton.setDisable(true);
        forwardButton.setPrefSize(60,20);
        forwardButton.setOnAction(actionEvent -> clickForwards());
 
        changePane.getChildren().addAll(backwardButton, forwardButton);
        changePane.setSpacing(WIDTH - 100.0);
             
            
        toComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                toComboBoxAction();
                if(toComboBox.getSelectionModel().getSelectedItem() != null){
                    backwardButton.setDisable(false);
                    forwardButton.setDisable(false);
                    try {
                        updateMapPane();
                        }
                    catch (java.text.ParseException pe) {
                        pe.printStackTrace();
                        }
                } else{
                    backwardButton.setDisable(true);
                    forwardButton.setDisable(true);
                }}});
               
        
        fromComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                fromComboBoxAction();
                if(fromComboBox.getSelectionModel().getSelectedItem() != null){
                    backwardButton.setDisable(false);
                    forwardButton.setDisable(false);
                    try {
                        updateMapPane();
                        }
                    catch (java.text.ParseException pe) {
                        pe.printStackTrace();
                        }
                } else{
                    backwardButton.setDisable(true);
                    forwardButton.setDisable(true);
                }
            }});
 
        //order where the panes should be on the main window
        BorderPane.setAlignment(datePane, Pos.TOP_RIGHT);
        rootPane.setTop(datePane);
        BorderPane.setAlignment(pane1, Pos.CENTER);
        rootPane.setCenter(pane1);
        BorderPane.setAlignment(changePane, Pos.BOTTOM_LEFT);
        rootPane.setBottom(changePane);
 
        
        datePane.getStyleClass().add("datePane");
        fromText.getStyleClass().add("dateLabel");
        fromComboBox.getStyleClass().add("dateComboBox");
        toText.getStyleClass().add("dateLabel");
        toComboBox.getStyleClass().add("dateComboBox");
        changePane.getStyleClass().add("changePane");
        backwardButton.getStyleClass().add("navigationButton");
        forwardButton.getStyleClass().add("navigationButton");
        
        
        root.getChildren().add(rootPane);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setTitle("Covid Application Window");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }
 
    /**
     * This method allows the user to go back to the previous pane they were in
     */
    private void clickBackwards(){
        if (paneCounter <= 0){
            paneCounter = paneCarousel.size() - 1;
        } else{
            paneCounter --;
        }
        switchPanes(paneCarousel.get((paneCounter)));
    }
 
    /**
     * This method allows the user to go to the next pane
     */
    private void clickForwards(){
        if (paneCounter >= (paneCarousel.size() - 1)){
            paneCounter = 0;
        } else{
            paneCounter ++;
        }
        switchPanes(paneCarousel.get(paneCounter));
    }
 
    /**
     * This method initialises the nodes within the WELCOME PANE
     * @return HBox
    */
    private VBox createWelcomePane() {
        pane1.setId("welcomePane");
        pane1.setPrefSize(WIDTH, 460);

        Label welcomeLabel = new Label("Welcome to the Covid Data Platform");
        welcomeLabel.setId("welcomeLabel");

        Label description = new Label("Explore interactive maps, view detailed statistics, and navigate through a comprehensive dataset tailored for Greater London.");
        description.setId("descriptionLabel");
        
        VBox welcomeInstructions = new VBox(5); 
        welcomeInstructions.getStyleClass().add("welcome-instructions");
        welcomeInstructions.setAlignment(Pos.CENTER);

        Label instructionTitle = new Label("How to Use the Platform (Click '<<' and '>>' to navigate to the following panels):");

        Label instructionStepOne = new Label("Panel 1. Choose a date range to view COVID-19 statistics.");
        Label instructionStepTwo = new Label("Panel 2. Interact with the map for a visual representation of data.");
        Label instructionStepThree = new Label("Panel 3. Navigate through different statistics.");
        Label instructionStepFour = new Label("Panel 4. Add new data logs for different boroughs.");

        welcomeInstructions.getChildren().addAll(instructionTitle, instructionStepOne, instructionStepTwo, instructionStepThree, instructionStepFour);

        pane1.getChildren().addAll(welcomeLabel, description, welcomeInstructions);
        pane1.setAlignment(Pos.CENTER);
        pane1.setSpacing(10);

        return pane1;
    }

 
    /**
     * This method initialises the nodes within the MAP PANE
     * @return AnchorPane
     */
    private AnchorPane createMapPane(){
        pane2.setId("mapPane");
        Label text = new Label("MAP");
        pane2.getChildren().addAll(text);
        return pane2;
    }

    /**
     * This method updates the map pane.
     * @return AnchorPane
     */
    private AnchorPane updateMapPane() throws java.text.ParseException {
        pane2.getChildren().clear();
        if (fromComboBox.getSelectionModel().getSelectedItem()!= null && toComboBox.getSelectionModel().getSelectedItem() != null){
            MapBuilder creator = new MapBuilder(pane2, convertStringToDate(fromComboBox.getSelectionModel().getSelectedItem().toString()), convertStringToDate(toComboBox.getSelectionModel().getSelectedItem().toString()));
            
            creator.addLegend();
            
            creator.createButton("ENFI", "Enfield", 7, 0);
            
            creator.createButton("BARN", "Barnet", 4, 1);
            creator.createButton("HRGY", "Haringey", 6, 1);
            creator.createButton("WALT", "Waltham Forest", 8, 1);
            
            creator.createButton("HRRW", "Harrow", 1, 2);
            creator.createButton("BREN", "Brent", 3, 2);
            creator.createButton("CAMD", "Camden", 5, 2);
            creator.createButton("ISLI", "Islington", 7, 2);
            creator.createButton("HACK", "Hackney", 9, 2);
            creator.createButton("REDB", "Redbridge", 11, 2);
            creator.createButton("HAVE", "Havering", 13, 2);
            
            creator.createButton("HILL", "Hillingdon", 0, 3);
            creator.createButton("EALI", "Ealing", 2, 3);
            creator.createButton("KENS", "Kensington And Chelsea", 4, 3);
            creator.createButton("WSTM", "Westminster", 6, 3);
            creator.createButton("TOWH", "Tower Hamlets", 8, 3);
            creator.createButton("NEWH", "Newham", 10, 3);
            creator.createButton("BARK", "Barking And Dagenham", 12, 3);
            
            creator.createButton("HOUN", "Hounslow", 1, 4);
            creator.createButton("HAMM","Hammersmith And Fulham", 3, 4);
            creator.createButton("WAND", "Wandsworth", 5, 4);
            creator.createButton("CITY", "City Of London", 7, 4);
            creator.createButton("GWCH", "Greenwich", 9, 4);
            creator.createButton("BEXL", "Bexley", 11, 4);
            
            creator.createButton("RICH", "Richmond Upon Thames", 2, 5);
            creator.createButton("MERT", "Merton", 4, 5);
            creator.createButton("LAMB", "Lambeth", 6, 5);
            creator.createButton("STHW", "Southwark", 8, 5);
            creator.createButton("LEWS", "Lewisham", 10, 5);
            
            creator.createButton("KING", "Kingston Upon Thames", 3, 6);
            creator.createButton("SUTT", "Sutton", 5, 6);
            creator.createButton("CROY", "Croydon", 7, 6);
            creator.createButton("BROM", "Bromley", 9, 6);
        }
        return pane2;
    }

    /**
     * This method initialises the nodes within the STATISTICS PANE
     * @return HBox
     */
    private HBox createStatsPane() {
        pane3.setId("statsPane");
        pane3.setMaxWidth(Double.MAX_VALUE);
        pane3.setMaxHeight(Double.MAX_VALUE);
        
        Pane statsPane = statisticsWindow.createStatisticsComponent();
        
        HBox.setHgrow(statsPane, Priority.ALWAYS); 
        
        pane3.getChildren().clear();
        pane3.getChildren().add(statsPane);
        return pane3;
    }
 
    /**
     * This method initialises the nodes within the CHALLENGE PANE
     * @return HBox
     */
    private HBox createChallengePane(){
        Pane challengePane = challengeWindow.createChallengeComponent();
        pane4.getChildren().clear();
        pane4.getChildren().addAll(challengePane);
        return pane4;
    }
 
    /**
     * This method switches the center pane to other panes
     * @param pane
     */
    private void switchPanes(Pane pane) {
        pane.setPrefSize(WIDTH, 460);
        rootPane.setCenter(pane);
    }
   
    /**
     * this method converts a string date into the Date data type
     * @return Date
     * @param String
     */
    private Date convertStringToDate(String dateString) throws java.text.ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return java.sql.Date.valueOf(localDate);
    }
   
    /**
     * This method evaluates if the date TO is after the date FROM and outputs a warning message if not
     */
    private void toComboBoxAction(){
        try{
            Date dateTo = convertStringToDate(toComboBox.getSelectionModel().getSelectedItem().toString());
            if (fromComboBox.getSelectionModel().getSelectedItem() != null){
                Date dateFrom = convertStringToDate(fromComboBox.getSelectionModel().getSelectedItem().toString());
                if (dateTo.before(dateFrom)){
                    toComboBox.getSelectionModel().clearSelection();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Date TO can't be before the date FROM");
                    alert.setContentText("Choose date TO after the date FROM");
                    alert.showAndWait();
                }
                if (fromComboBox.getSelectionModel().getSelectedItem() != null && toComboBox.getSelectionModel().getSelectedItem() != null) {
                    statisticsWindow.updateData(fromComboBox.getSelectionModel().getSelectedItem().toString(), toComboBox.getSelectionModel().getSelectedItem().toString());
                }
            }
        }
        catch (java.text.ParseException pe){
            pe.printStackTrace();
        }
    }
   
    /**
     * This method evaluates if the date FROM is before the date TO and outputs a warning message if not
     */
    private void fromComboBoxAction(){
        try{
            Date dateFrom = convertStringToDate(fromComboBox.getSelectionModel().getSelectedItem().toString());
            if (toComboBox.getSelectionModel().getSelectedItem() != null){
                Date dateTo = convertStringToDate(toComboBox.getSelectionModel().getSelectedItem().toString());
                if (dateFrom.after(dateTo)){
                    fromComboBox.getSelectionModel().clearSelection();
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Date FROM can't be after the date TO");
                    alert.setContentText("Choose date to FROM before the date TO");
                    alert.showAndWait();
                }
                if (fromComboBox.getSelectionModel().getSelectedItem() != null && toComboBox.getSelectionModel().getSelectedItem() != null) {
                    statisticsWindow.updateData(fromComboBox.getSelectionModel().getSelectedItem().toString(), toComboBox.getSelectionModel().getSelectedItem().toString());
                }
            }
        }
        catch (java.text.ParseException pe){
            pe.printStackTrace();
        }
    }
}