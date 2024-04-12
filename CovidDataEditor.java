import java.util.List;
import java.io.FileWriter;
import com.opencsv.CSVWriter;
import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;


/**
 * The class responsible for adding new entries into the CovidData spreadsheet
 *
 * @author Cora Bowers
 * @version 25/03/2024
 */
public class CovidDataEditor
{
    private List<String[]> rows; // The rows of CovidData entries
    
    public CovidDataEditor(){
        CovidDataLoader loader = new CovidDataLoader();
        rows = loader.loadAsStringList();
    }
    
    /**
     * Saves all known rows of covid data to the csv.
     * 
     * @return True if the save was succesful.
     */
    public boolean saveChanges(){
        try{
            CSVWriter writer = new CSVWriter(new FileWriter("covid_london.csv"));
            writer.writeAll(rows);
            writer.close();
            return true;
        }
        catch(IOException e){
            System.out.println("Something Went Wrong?!");
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Adds a new covid data entry for today's date to the rows list.
     * 
     * @param borough The borough associated with the new entry.
     * @param newCases The number of new covid cases.
     * @param newDeaths The number of new covid deaths.
     * @throws NumberFormatException
     */
    public void addNewRow(String stringDate, String borough, String newCases, String newDeaths) throws NumberFormatException{
        
        int newTotalCases = getCurrentTotalCases(borough) + convertInt(newCases);
        int newTotalDeaths = getCurrentTotalDeaths(borough) + convertInt(newDeaths);
        
        String[] newRow = {stringDate, borough, "", "", "", "", "", "", ""+newCases, ""+newTotalCases, ""+newDeaths, ""+newTotalDeaths};
        rows.add(newRow);
    }
    
    /**
     * Calculate and return the number of total cases to date for a given borough.
     * 
     * @param borough The borough we want to get total cases for.
     * @return The number of total cases for the given borough.
     */
    public int getCurrentTotalCases(String borough){
        int totalCases = 0;
        
        for(String[] row : rows){
            if(row[1].equals(borough)){
                if(convertInt(row[9]) > totalCases){
                    totalCases = convertInt(row[9]);
                }
            }
        }
        
        return totalCases;
    }
    
    /**
     * Calculate and return the number of total deaths to date for a given borough.
     * 
     * @param borough The borough we want to get total deaths for.
     * @return The number of total deaths for the given borough.
     */
    public int getCurrentTotalDeaths(String borough){
        int totalDeaths = 0;
        
        for(String[] row : rows){
            if(row[1].equals(borough)){
                if(convertInt(row[11]) > totalDeaths){
                    totalDeaths = convertInt(row[11]);
                }
            }
        }
        
        return totalDeaths;
    }
    
    /**
     *
     * @param intString the string to be converted to Integer type
     * @return the Integer value of the string, or -1 if the string is 
     * either empty or just whitespace
     */
    private Integer convertInt(String intString) throws NumberFormatException{
        if(intString != null && !intString.trim().equals("")){
            return Integer.parseInt(intString);
        }
        return 0;
    }
}
