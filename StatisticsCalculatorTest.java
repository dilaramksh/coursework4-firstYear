

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;

/**
 * The test class StatisticsCalculatorTest.
 *
 * @author  Dilara Mukasheva
 * @version 25/03/2024
 */
public class StatisticsCalculatorTest
{
    
    
    private StatisticsCalculator statisticsCalculator;
    private ArrayList<CovidData> testCovidRecords;

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     * 
     */
    @BeforeEach
    public void setUp()
    {
        statisticsCalculator = new StatisticsCalculator();
        
        testCovidRecords = new ArrayList<CovidData>(Arrays.asList(
            new CovidData("2022-06-30", "Redbridge", -15, -3, 4, -32, -37, 5, 79, 102321, 0, 933),
            new CovidData("2022-06-30", "Greenwich", -10, 8, -1, -18, -38, 6, 104, 92920, 0, 582),
            new CovidData("2022-06-29", "Camden", -31, -14, 15, -9, -37, 7, 92, 74692, 0, 363),
            new CovidData("2022-06-29", "Redbridge", -18, 0, 5, -28, -39, 5, 95, 102242, 0, 933),
            new CovidData("2022-06-29", "Enfield", -20, -6, 14, -38, -30, 5, 94, 98644, 0, 758),
            new CovidData("2022-06-28", "Richmond Upon Thames", -22, 6, 14, -32, -48, 7, 97, 68537, 0, 375),
            new CovidData("2022-06-28", "Waltham Forest", -12, 0, 8, -33, -36, 5, 76, 91943, 0, 634),
            new CovidData("2022-06-27", "Harrow", -5, 17, 83, -16, -22, 7, 106, 83586, 1, 648),
            new CovidData("2022-06-27", "Hounslow", -7, 9, 26, -19, -27, 5, 89, 96811, 0, 710),
            new CovidData("2022-06-27", "Haringey", -21, -8, -3, -31, -40, 6, 92, 85131, 0, 507)
        ));
    }
    

    @Test
    public void testFilterDataByDate() {
        LocalDate startDate = LocalDate.of(2022, 6, 28);
        LocalDate endDate = LocalDate.of(2022, 6, 30);
        ArrayList<CovidData> filteredList = statisticsCalculator.filterDataByDate(testCovidRecords, startDate, endDate);
        
        assertEquals(7, filteredList.size()); 
    }

    @Test
    public void testCalcTotalDeaths() {
        int totalDeaths = statisticsCalculator.calcTotalDeaths(testCovidRecords);
        
        assertEquals(6443, totalDeaths); 
    }

    @Test
    public void testCalcAvrgTotalCases() {
        double averageCases = statisticsCalculator.calcAvrgTotalCases(testCovidRecords);
        
        assertEquals(89682.7, averageCases, 0.001); 
    }
    
    @Test 
    public void testCalcAverageParksAndWorkplacesGMR(){
        double averageMobility = statisticsCalculator.calcAverageParksAndWorkplacesGMR(testCovidRecords);
        
        assertEquals(-9.45, averageMobility, 0.001); 
    }
    
    @Test 
    public void testCalcAverageResidentialAndTransitGMR(){
        double averageMobility = statisticsCalculator.calcAverageResidentialAndTransitGMR(testCovidRecords);
        
        assertEquals(-9.9, averageMobility, 0.001); 
    }

}

