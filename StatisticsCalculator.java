import java.util.ArrayList;
import java.time.LocalDate;

/**
 * The StatisticsCalculator class helps understand COVID-19 through numbers. 
 * It can sort data by date, count deaths, find average cases, 
 * and check how much people move around in different areas.
 * This class works with lists of CovidData, making it easier to see what's happening with the virus.
 * @author Lucia Garces
 * @version 20/03/2024
 */
public class StatisticsCalculator
{
    /**
     * This method filters a list of CovidData to include only those within a specified date range.
     * Checks each data point to determine if its date falls between the start and end dates.
     * @param dataList the list of CovidData to filter.
     * @param startDate the start date of the range.
     * @param endDate the end date of the range.
     * @returns filtered list.
     */
    public ArrayList<CovidData> filterDataByDate(ArrayList<CovidData> dataList, LocalDate startDate, LocalDate endDate) {
        ArrayList<CovidData> filteredList = new ArrayList<>();
        for (CovidData data : dataList) {
            LocalDate dataDate = LocalDate.parse(data.getDate());
            if ((dataDate.isAfter(startDate) || dataDate.isEqual(startDate)) && (dataDate.isBefore(endDate) || dataDate.isEqual(endDate))) {
                filteredList.add(data);
            }
        }
        return filteredList;
    }
    
    /**
     * Sums up the total number of COVID-19 related deaths from a list of data points.
     * @returns total death count across all data points.
     */

    public int calcTotalDeaths(ArrayList<CovidData> dataList) {
        int totalDeaths = 0;
        for (CovidData data : dataList) {
            totalDeaths += data.getTotalDeaths();
        }
        return totalDeaths;
    }
    
    
    /**
     * Calculates the average of total COVID-19 cases across a list of data points.
     * @returns average cases per data point or 0 if list is empty.
     */

    public double calcAvrgTotalCases(ArrayList<CovidData> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }
        int totalCases = dataList.stream().mapToInt(CovidData::getTotalCases).sum();
        return(double) totalCases / dataList.size();
    }
    
    /**
     * Calculates the average of Parks GMR and Workplaces GMR across a list of CovidData points.
     * 
     * @param dataList The list of CovidData from which to calculate the averages.
     * @return The average value of Parks GMR and Workplaces GMR across all provided CovidData points.
     */
    public double calcAverageParksAndWorkplacesGMR(ArrayList<CovidData> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }
        double totalParksGMR = 0;
        double totalWorkplacesGMR = 0;
        for (CovidData data : dataList) {
            totalParksGMR += data.getParksGMR();
            totalWorkplacesGMR += data.getWorkplacesGMR();
        }
        double averageParksGMR = totalParksGMR / dataList.size();
        double averageWorkplacesGMR = totalWorkplacesGMR / dataList.size();
        
        // Calculate the average of the averages of Parks GMR and Workplaces GMR
        return (averageParksGMR + averageWorkplacesGMR) / 2;
    }
    
    /**
     * Calculates the average of Residential GMR and Transit GMR across a list of CovidData points.
     * 
     * @param dataList The list of CovidData from which to calculate the averages.
     * @return The average value of Residential GMR and Transit GMR across all provided CovidData points.
     */
    public double calcAverageResidentialAndTransitGMR(ArrayList<CovidData> dataList) {
        if (dataList.isEmpty()) {
            return 0;
        }
        double totalResidentialGMR = 0;
        double totalTransitGMR = 0;
        for (CovidData data : dataList) {
            totalResidentialGMR += data.getResidentialGMR();
            totalTransitGMR += data.getTransitGMR();
        }
        double averageResidentialGMR = totalResidentialGMR / dataList.size();
        double averageTransitGMR = totalTransitGMR / dataList.size();
        
        // Calculate the average of the averages of Residential GMR and Transit GMR
        return (averageResidentialGMR + averageTransitGMR) / 2;
    }
}