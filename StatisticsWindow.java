import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;

 
import java.time.LocalDate;
import java.util.ArrayList;
/**
 * The StatisticsWindow class is a part of a graphical user interface that presents 
 * various COVID-19 statistics to the user. It manages the display and updating of 
 * statistical data within the application window based on user-selected date ranges.
 * This class allows cycling through different types of statistics and updating the
 * view to reflect the latest data.
 * @author Lucia Garces
 * @version 20/03/2024
 */
public class StatisticsWindow {
    private Label statisticNameLabel = new Label();
    private Label statisticValueLabel = new Label();
    private Label statisticsLabel = new Label();
    
    private StatisticsCalculator statisticsCalculator = new StatisticsCalculator();
    private ArrayList<CovidData> filteredData;
    private StatisticsType currentStatistic = StatisticsType.TOTAL_DEATHS;
    
    private enum StatisticsType {
        TOTAL_DEATHS, AVERAGE_CASES, PARK_WORKPLACE, RESIDENTIAL_TRANSIT
    }
    
    /**
     * Creates the main statistics component for the application window.
     * It organizes the UI elements in a vertical box layout and initializes
     * the statistic labels and navigation buttons.
     * @return A Pane object containing the constructed statistics UI.
     */
    public Pane createStatisticsComponent() {
        VBox root = new VBox(10);
        root.setFillWidth(true); 
        root.setAlignment(Pos.CENTER);
        VBox.setVgrow(root, Priority.ALWAYS);
        
        statisticsLabel = new Label("STATISTICS");
        statisticsLabel.getStyleClass().add("statistics-title"); 
        statisticsLabel.setAlignment(Pos.CENTER); 
        
        HBox centerBox = createStatisticDisplayContainer();
        VBox.setVgrow(centerBox, Priority.ALWAYS); 
        centerBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(statisticsLabel, centerBox);
        root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); 

        return root;
    }
    
     /**
     * Creates a container for displaying the statistic name and value with navigation buttons.
     * This horizontal box contains buttons for cycling through statistics and labels for displaying them.
     * @return An HBox object containing the statistics display components.
     */
    private HBox createStatisticDisplayContainer() {
        Button leftButton = new Button("<");
        Button rightButton = new Button(">");
        leftButton.getStyleClass().add("nav-button");
        rightButton.getStyleClass().add("nav-button");
        leftButton.setOnAction(event -> cycleStatistics(false));
        rightButton.setOnAction(event -> cycleStatistics(true));
        
        statisticNameLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        statisticValueLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        statisticNameLabel.getStyleClass().add("statistic-label");
        statisticValueLabel.getStyleClass().add("statistic-value");

        HBox centerBox = new HBox(10, leftButton, statisticNameLabel, statisticValueLabel, rightButton);
        centerBox.setFillHeight(true);
        centerBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(statisticNameLabel, Priority.ALWAYS); 
        HBox.setHgrow(statisticValueLabel, Priority.ALWAYS);
        centerBox.setAlignment(Pos.CENTER);
        
        centerBox.getStyleClass().add("center-box");
        


        return centerBox;
    }
    
    /**
     * Cycles through the available statistics, updating the display forward or backward.
     * This method changes the current displayed statistic to the next or previous one in the enumeration.
     * @param forward A boolean value determining the direction of cycling through statistics.
     */
    private void cycleStatistics(boolean forward) {
        if (filteredData == null || filteredData.isEmpty()) {
            statisticNameLabel.setText("No data available.");
            statisticValueLabel.setText("");
            return;
        }
        currentStatistic = StatisticsType.values()[(currentStatistic.ordinal() + (forward ? 1 : StatisticsType.values().length - 1)) % StatisticsType.values().length];
        updateStatistics();
    }
    
    /**
     * Updates the displayed data based on the start and end dates provided.
     * It loads the relevant CovidData using a data loader and filters it using the statistics calculator.
     * Then it updates the display with the new data.
     * @param startDate The start date of the date range as a String.
     * @param endDate The end date of the date range as a String.
     */
     public void updateData(String startDate, String endDate) {
        // Logic to update the statistics based on the new dates
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        CovidDataLoader dataLoader = new CovidDataLoader(); 
        filteredData = statisticsCalculator.filterDataByDate(dataLoader.load(), start, end);
        updateStatistics();
    }
    
    /**
     * Updates the statistical data displayed in the UI based on the currentStatistic member.
     * It chooses which statistic to display and formats the data appropriately for presentation.
     */
    private void updateStatistics() {
        if (filteredData == null || filteredData.isEmpty()) {
            statisticNameLabel.setText("No data available.");
            statisticValueLabel.setText("");
            return;
        }
 
        switch (currentStatistic) {
            case TOTAL_DEATHS:
                statisticNameLabel.setText("Total Deaths");
                statisticValueLabel.setText(String.valueOf(statisticsCalculator.calcTotalDeaths(filteredData)));
                break;
            case AVERAGE_CASES:
                statisticNameLabel.setText("Average Cases");
                statisticValueLabel.setText(String.format("%.2f", statisticsCalculator.calcAvrgTotalCases(filteredData)));
                break;
            case PARK_WORKPLACE:
                statisticNameLabel.setText("Avg. Parks/Workplaces GMR");
                statisticValueLabel.setText(String.format("%.2f", statisticsCalculator.calcAverageParksAndWorkplacesGMR(filteredData)));
                break;
            case RESIDENTIAL_TRANSIT:
                statisticNameLabel.setText("Avg. Residential/Transit GMR");
                statisticValueLabel.setText(String.format("%.2f", statisticsCalculator.calcAverageResidentialAndTransitGMR(filteredData)));
                break;
        }
    }
}
 