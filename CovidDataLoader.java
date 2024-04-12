import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import com.opencsv.CSVReader;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CovidDataLoader {
    
    private ArrayList<String> covid_dates = new ArrayList<String>();
    
    public CovidDataLoader(){
        load(); //gets the covid data to extract the dates from
        for (CovidData data: load()){
            String date = data.getDate(); //gets date from the list
            if (!(covid_dates.contains(date))){ //ensures that the dates don't repeat in the list. so that they are unique 
                covid_dates.add(date);
            }
        }
        Collections.sort(covid_dates);
    }
    
    /** 
     * @return an ArrayList containing the rows in the Covid London data set csv file.
     */
    public ArrayList<CovidData> load() {
        //System.out.println("Begin loading Covid London dataset...");
        ArrayList<CovidData> records = new ArrayList<CovidData>();
        try{
            URL url = getClass().getResource("covid_london.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            //skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                
                String date    = line[0];
                String borough    = line[1];    
                int retailRecreationGMR    = convertInt(line[2]);    
                int groceryPharmacyGMR    = convertInt(line[3]);    
                int parksGMR    = convertInt(line[4]);    
                int transitGMR    = convertInt(line[5]);    
                int workplacesGMR    = convertInt(line[6]);    
                int residentialGMR    = convertInt(line[7]);    
                int newCases    = convertInt(line[8]);    
                int totalCases    = convertInt(line[9]);    
                int newDeaths    = convertInt(line[10]);    
                int totalDeaths    = convertInt(line[11]);                

                CovidData record = new CovidData(date,borough,retailRecreationGMR,
                    groceryPharmacyGMR,parksGMR,transitGMR,workplacesGMR,
                    residentialGMR,newCases,totalCases,newDeaths,totalDeaths);
                records.add(record);
            }
        } catch(IOException | URISyntaxException e){
            System.out.println("Something Went Wrong?!");
            e.printStackTrace();
        }
        //System.out.println("Number of Loaded Records: " + records.size());
        return records;
    }
    
    /**
     * Return a List containing the rows in the Covid London Data csv file, represented as arrays of
     * Strings.
     * 
     * @return A list of string arrays containing the data from each row of the Covid London Data csv
     * file.
     */
    public List<String[]> loadAsStringList(){
        List<String[]> covidData = null;
        
        try{
            URL url = getClass().getResource("covid_london.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            
            covidData = reader.readAll();
            reader.close();
        }
        catch(IOException | URISyntaxException e){
            System.out.println("Something Went Wrong?!");
            e.printStackTrace();
        }
        
        return covidData;
    }

    /**
     *
     * @param doubleString the string to be converted to Double type
     * @return the Double value of the string, or -1.0 if the string is 
     * either empty or just whitespace
     */
    private Double convertDouble(String doubleString){
        if(doubleString != null && !doubleString.trim().equals("")){
            return Double.parseDouble(doubleString);
        }
        return 0.0;
    }

    /**
     *
     * @param intString the string to be converted to Integer type
     * @return the Integer value of the string, or -1 if the string is 
     * either empty or just whitespace
     */
    private Integer convertInt(String intString){
        if(intString != null && !intString.trim().equals("")){
            return Integer.parseInt(intString);
        }
        return 0;
    }
    
    /**
     * @returns the list of all unique dates in the CSV file
     */
    public ArrayList<String> getDates(){
        return covid_dates;
    }
    

    /**
     * Creates a list of all covid data for the borough and dates from the CovidDataLoader.
     * 
     * @return An ArrayList of all CovidData objects for the borough within the date range.
     */
    public ArrayList<CovidData> loadBoroughData(String borough, Date startDate, Date endDate) throws java.text.ParseException{
        ArrayList<CovidData> boroughData = new ArrayList<CovidData>();
        
        ArrayList<CovidData> covidData = load();
        
        for(int i = 0; i < covidData.size(); i++){
            if(covidData.get(i).getBorough().equals(borough) && dateInRange(startDate, convertStringToDate(covidData.get(i).getDate()), endDate)){
                boroughData.add(covidData.get(i));
            }
        }
        
        return boroughData;
    }
    
    /**
     * Creates a list of all covid data for dates from the CovidDataLoader 
     * 
     * @return An ArrayList of all CovidData objects within the date range.
     */
    public ArrayList<CovidData> loadPeriodData(Date startDate, Date endDate) throws java.text.ParseException{
        ArrayList<CovidData> data = new ArrayList<CovidData>();
        
        ArrayList<CovidData> covidData = load();
        
        for(int i = 0; i < covidData.size(); i++){
            if(dateInRange(startDate, convertStringToDate(covidData.get(i).getDate()), endDate)){
                data.add(covidData.get(i));
            }
        }
        
        return data;
    }
        
    /**
     * Check if a date is within a given date range.
     * 
     * @param firstDate The date at the beginning of the range.
     * @param betweenDate The date to be looked for within this range.
     * @param lastDate The date at the end of the range.
     * 
     * @return True if the date is within the given range.
     */
    private boolean dateInRange(Date firstDate, Date betweenDate, Date lastDate){
        if(betweenDate.equals(firstDate)){
            return true;
        }
        else if(betweenDate.equals(lastDate)){
            return true;
        }
        else if(betweenDate.after(firstDate) && betweenDate.before(lastDate)){
            return true;
        }
        return false;
    }
    
    /**
     * this method converts a string date into the Date data type
     * @return Date
     * @param String 
     */
    private Date convertStringToDate(String dateString) throws java.text.ParseException {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
        Date date = (Date)formatter.parse(dateString);
        return date;
    }
    
    /**
     * @return an int, added number of deaths of a given Arraylist of CovidData
     */
    public int addedDeaths(ArrayList<CovidData> covidData) {
        int total = 0;
        
        for(int i = 0; i < covidData.size(); i++){
            total += covidData.get(i).getNewDeaths();
        }
        
        return total;
    }
}