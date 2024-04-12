import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import java.util.Date;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.layout.*;

/**
 * Creates a hexagonal button on a given panel
 * 
 *
 * @author Martin Berthoud
 * @version 21/03/2024
 */
public class MapBuilder
{
    static final int HORIZONTAL_BORDER = 150;
    static final int VERTICAL_BORDER = 20;
    
    //The size of the font and hexagon are based on this double
    static final double HEX_SIZE = 35;
    
    static final int GAP_SIZE = 19;
    
    static final int NUMBER_OF_BOROUGHS = 33;
    
    private AnchorPane pane;
    private Date startDate;
    private Date endDate;
    private CovidDataLoader covidDataLoader;
    
    //This death toll over the given period will be used to determine 
    //the hexagons' colors.
    private int totalDeaths;
    
    
    /**
     * Constructor
     * Takes the AnchorPane that is defined as the map as a parameter
     */
    public MapBuilder(AnchorPane pane, Date startDate, Date endDate)
    {
        this.pane = pane;
        this.startDate = startDate;
        this.endDate = endDate;
        covidDataLoader = new CovidDataLoader();
        try{
            totalDeaths = covidDataLoader.addedDeaths(covidDataLoader.loadPeriodData(startDate, endDate));
        }
        catch(java.text.ParseException e){
            System.out.println("Dates entered are invalid.");
        }
    }
    
    /**
     * This method creates a hexagonal button representing a borough
     * on the map, taking simple coordinates to simplify their 
     * creation.
     */
    public void createButton(String borough, String fullName, double xCoordinate, double yCoordinate) {
        //create a StackPane so that the text is centered to the hexagon
        StackPane stack = new StackPane();
        
        //create the hexagon in question
        Polygon hexagon = new Polygon();
        
        hexagon.getPoints().addAll(new Double[]{ 
             0.0, HEX_SIZE, 
             (java.lang.Math.sqrt(3) * HEX_SIZE / 2), (HEX_SIZE / 2), 
             (java.lang.Math.sqrt(3) * HEX_SIZE / 2), (HEX_SIZE / -2), 
             0.0, -HEX_SIZE, 
             (java.lang.Math.sqrt(3) * HEX_SIZE / -2), (HEX_SIZE / -2),
             (java.lang.Math.sqrt(3) * HEX_SIZE / -2), (HEX_SIZE / 2),
             }); 
        
        hexagon.setStroke(Color.BLACK);
        hexagon.setOpacity(0.5);     
             
        //This gives the hexagon its color
        try
        {        
            setColor(hexagon, fullName);
        }
        catch (java.text.ParseException pe)
        {
            pe.printStackTrace();
        }
        

        //This links the hexagon to its pop up window 
        hexagon.setOnMouseClicked((event) -> {
            try{
                BoroughDataWindow window = new BoroughDataWindow(fullName, startDate, endDate);
                Stage stage = new Stage();
                window.start(stage);
            }
            catch(java.text.ParseException e){
                System.out.println("Dates entered are invalid.");
            }
        });
        
        //create the name
        Text name = new Text(borough);
        name.setFont(new Font(HEX_SIZE/2));
        
        //assemble everything
        stack.getChildren().addAll(name, hexagon);
        pane.getChildren().addAll(stack);
        
        //place the hexagon at the right spot on the map
        pane.setTopAnchor(stack, VERTICAL_BORDER + yCoordinate * GAP_SIZE * 3);
        pane.setLeftAnchor(stack, HORIZONTAL_BORDER + xCoordinate * java.lang.Math.sqrt(3) * GAP_SIZE);
    }    
    
    /**
     * This method will give the button its adapted color.
     * @param the hexagon whose color will be modified
     * @param name of the borough
     */
    private void setColor(Polygon hexagon, String borough) throws java.text.ParseException {
        int boroughDeathToll = covidDataLoader.addedDeaths(covidDataLoader.loadBoroughData(borough, startDate, endDate));
        
        if ((boroughDeathToll == 0) || (boroughDeathToll <= (totalDeaths / NUMBER_OF_BOROUGHS * 0.8))) {
            hexagon.setFill(Color.LIMEGREEN);
            hexagon.setOnMouseEntered((event) -> {
                hexagon.setFill(Color.FORESTGREEN);
            });
            hexagon.setOnMouseExited((event) -> {
                hexagon.setFill(Color.LIMEGREEN);
            });
        }
        else if (boroughDeathToll <= (totalDeaths / NUMBER_OF_BOROUGHS * 1.2)) {
            hexagon.setFill(Color.YELLOW);
            hexagon.setOnMouseEntered((event) -> {
                hexagon.setFill(Color.GOLDENROD);
            });
            hexagon.setOnMouseExited((event) -> {
                hexagon.setFill(Color.YELLOW);
            });
        }
        else {
            hexagon.setFill(Color.TOMATO);
            hexagon.setOnMouseEntered((event) -> {
                hexagon.setFill(Color.FIREBRICK);
            });
            hexagon.setOnMouseExited((event) -> {
                hexagon.setFill(Color.TOMATO);
            });
        }
    }
    
    /**
     * This method creates a coloured box with a description to its right.
     * 
     * @param Color colour of the box
     * @param String description of the legend and colour
     * @return HBox 
     */
    public HBox legend(Color color, String desc) {
        HBox legend = new HBox();
        
        Polygon rectangle = new Polygon();
        
        rectangle.getPoints().addAll(new Double[]{ 
            0.0, 0.0, 
            60.0, 0.0, 
            60.0, 15.0, 
            0.0, 15.0,
            }); 
             
        rectangle.setStroke(Color.BLACK);
        
        rectangle.setFill(color);
        rectangle.setOpacity(0.5);
        
        //create some space between the rectangle and the text
        Text space = new Text("  ");
        space.setFont(new Font(13));
        
        //create the name
        Text text = new Text(desc);
        text.setFont(new Font(13));
        
        legend.getChildren().addAll(rectangle, space, text);
        
        return legend;
    }
    
   
    /**
     * This method places a legend in the pane's right left corner.
     */
    public void addLegend() {
        HBox firstLegend = legend(Color.LIMEGREEN, (int) Math.floor(totalDeaths * 0.8 / NUMBER_OF_BOROUGHS) + " or less deaths over the selected duration");
        pane.getChildren().add(firstLegend);
        pane.setTopAnchor(firstLegend, 8.0);
        pane.setLeftAnchor(firstLegend, 10.0);
        int border = 0;
        
        if (Math.floor(totalDeaths * 0.8 / NUMBER_OF_BOROUGHS) != Math.ceil(totalDeaths * 1.2 / NUMBER_OF_BOROUGHS) && Math.floor(totalDeaths * 0.8 / NUMBER_OF_BOROUGHS) != (Math.ceil(totalDeaths * 1.2 / NUMBER_OF_BOROUGHS) - 1)) {
            HBox secondLegend;
            if ((Math.floor(totalDeaths * 0.8 / NUMBER_OF_BOROUGHS) + 1) == (Math.ceil(totalDeaths * 1.2 / NUMBER_OF_BOROUGHS) - 1)) {
                secondLegend = legend(Color.YELLOW, (int) (Math.floor(totalDeaths * 0.8 / NUMBER_OF_BOROUGHS) + 1) + " death(s) over the selected duration");
            }
            else {
                secondLegend = legend(Color.YELLOW, "Between " + (int) (Math.floor(totalDeaths * 0.8 / NUMBER_OF_BOROUGHS) + 1) + " and " + (int) (Math.ceil(totalDeaths * 1.2 / NUMBER_OF_BOROUGHS) - 1) + " deaths");
            }
            pane.getChildren().add(secondLegend);   
            pane.setTopAnchor(secondLegend, 28.0);
            pane.setLeftAnchor(secondLegend, 10.0);
            border = 20;
        }
        
        if (totalDeaths != 0) {
            HBox thirdLegend = legend(Color.TOMATO, (int) Math.ceil(totalDeaths * 1.2 / NUMBER_OF_BOROUGHS) + " or more deaths over the selected duration");
            pane.getChildren().add(thirdLegend);
            pane.setTopAnchor(thirdLegend, border + 28.0);
            pane.setLeftAnchor(thirdLegend, 10.0);
        }
        
    }
}