package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field0
     *
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);
           if (!values.contains(aValue)) {
                    values.add(aValue);
                }
        }
        Collections.sort(values, String.CASE_INSENSITIVE_ORDER);

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        //an empty ArrayList that will contain HashMap objects of jobs
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        //from the Class Static variable i.e an ArrayList of HashMap Objects.
        //HashMap objects contain Job details.
        //Get each HashMap job object from an ArrayList of allJobs objects
        for (HashMap<String, String> row : allJobs) {

            //From Row HashMap (job) Object find the column like
            //Position Type or core competency. First get the column requested
            //then check if the column has the value requested
            String aValue = row.get(column);

            //if (aValue.contains(value)) {
            if(caseInsensitiveSearch(aValue,value)){
                jobs.add(row);
            }
        }

        return jobs;
    }

    public static ArrayList<HashMap<String,String>> findByValue(String value){
        loadData();
        //an empty ArrayList that will contain HashMap objects of jobs
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        //from the Class Static variable i.e an ArrayList of HashMap Objects.
        //HashMap objects contain Job details.
        //Get each HashMap job object from an ArrayList of allJobs objects
        for (HashMap<String, String> row : allJobs) {
            Iterator<Map.Entry<String, String>> iterator = row.entrySet().iterator();
            boolean found = false;
            while (iterator.hasNext() && !found) {
                Map.Entry<String, String> mapEntry = iterator.next();
                String keyValue = mapEntry.getValue();
                //System.out.println("keyValue"+keyValue);
                if (caseInsensitiveSearch(keyValue,value)) {
                    jobs.add(row);
                    found = true;
                }
            }
        }
            return jobs;
    }

    /**
     * Case Insensitive Search.
     * @param findIn Value of the field to search for
     * @param query Search term entered by the User.
     * @return true or false after performing case Insensitive Search
     */
    public static boolean caseInsensitiveSearch (String findIn, String query){

        String query_lower = query.toLowerCase();
        String[] findInArray = findIn.split(" ");
        boolean found = false;
        int i = 0;
        while (!found && findInArray.length > i) {
            String ar = findInArray[i];
            String ar_lower = ar.toLowerCase();
            if (query_lower.equals(ar_lower)) {
                found = true;
            }
            i++;
        }
        return found;
    }




    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}
