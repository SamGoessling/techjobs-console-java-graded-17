import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JobData {
    //--path to CSV data file--//
    private static final String DATA_FILE = "src/main/resources/job_data.csv";
    //--flag to check if data is already loaded--//
    private static boolean isDataLoaded = false;
    //--list to store all job data--//
    private static ArrayList<HashMap<String, String>> allJobs;

    //--fetches all unique values for specific field from job data--//
    public static ArrayList<String> findAll(String field) {
        //--insures data is loaded--//
        loadData();
        ArrayList<String> values = new ArrayList<>();
        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);
            //--avoids adding duplicates--//
            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }
        return values;
    }

    //--fetches all job records, each represented as a HashMap when returned--//
    public static ArrayList<HashMap<String, String>> findAll() {
        loadData();
        //--returns copy of allJobs to prevent modifications--//
        return new ArrayList<>(allJobs);
    }

    //--searches job data for jobs in specified column containing specified value--//
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {
        loadData();
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(column);
            //--case-insensitive search--//
            if (aValue.toLowerCase().contains(value.toLowerCase())) {
                jobs.add(row);
            }
        }
        return jobs;
    }

    //--searches columns for given term--//
    public static ArrayList<HashMap<String, String>> findByValue(String value) {
        loadData();
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        for (HashMap<String, String> row : allJobs) {
            for (String key : row.keySet()) {
                String aValue = row.get(key);
                //--case-insensitive search across all fields--//
                if (aValue.toLowerCase().contains(value.toLowerCase())) {
                    //--avoids dupes--//
                    if (!jobs.contains(row)) {
                        jobs.add(row);
                    }
                    //--stops checking fields when match is found--//
                    break;
                }
            }
        }
        return jobs;
    }

    //--loads data from CSV file into allJobs list, ensures data only loads once--//
    private static void loadData() {
        if (isDataLoaded) {
            return;
        }

        try {
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            //--converts CSV records into HashMaps & adds to allJobs list--//
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            //--flags data as loaded--//
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}

//--this code is responsible for loading & querying job data from a CSV file--//