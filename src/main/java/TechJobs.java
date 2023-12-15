import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TechJobs {
    static Scanner in = new Scanner(System.in);
    public static void main(String[] args) {

        //--initializes field map with key name pairs--//
        HashMap<String, String> columnChoices = new HashMap<>();
        columnChoices.put("core competency", "Skill");
        columnChoices.put("employer", "Employer");
        columnChoices.put("location", "Location");
        columnChoices.put("position type", "Position Type");
        columnChoices.put("all", "All");

        //--top-level menu options--//
        HashMap<String, String> actionChoices = new HashMap<>();
        actionChoices.put("search", "Search");
        actionChoices.put("list", "List");

        System.out.println("Welcome to LaunchCode's TechJobs App!");

        //--enables user to search until manually quitting--//
        while (true) {
            //--gets user's choice for viewing jobs--//
            String actionChoice = getUserSelection("View jobs by (type 'x' to quit):", actionChoices);
            if (actionChoice == null) {
                break;

            } else if (actionChoice.equals("list")) {
                //--gets user's choice for listing job data--//
                String columnChoice = getUserSelection("List", columnChoices);

                //--displays all values of user's choice--//
                if (columnChoice.equals("all")) {
                    printJobs(JobData.findAll());
                } else {
                    ArrayList<String> results = JobData.findAll(columnChoice);

                    System.out.println("\n*** All " + columnChoices.get(columnChoice) + " Values ***");

                    //--prints list of skills, employers, etc.--//
                    for (String item : results) {
                        System.out.println(item);
                    }
                }
            }
            //--user choice is "search"--//
            else {

                //--asks user how to search (ex:skill/employer)--//
                String searchField = getUserSelection("Search by:", columnChoices);

                //--prompt's user to enter search term--//
                System.out.println("\nSearch term:");
                String searchTerm = in.nextLine();

                //--performs search & prints results--//
                if (searchField.equals("all")) {
                    printJobs(JobData.findByValue(searchTerm));
                } else {
                    printJobs(JobData.findByColumnAndValue(searchField, searchTerm));
                }
            }
        }
    }

    //--returns the key of selected item from choices Dictionary--//
    private static String getUserSelection(String menuHeader, HashMap<String, String> choices) {

        int choiceIdx = -1;
        Boolean validChoice = false;
        String[] choiceKeys = new String[choices.size()];

        //--puts choices in ordered structure to associate an int with each one--//
        int i = 0;
        for (String choiceKey : choices.keySet()) {
            choiceKeys[i] = choiceKey;
            i++;
        }

        do {
            System.out.println("\n" + menuHeader);

            //--prints available choices--//
            for (int j = 0; j < choiceKeys.length; j++) {
                System.out.println("" + j + " - " + choices.get(choiceKeys[j]));
            }

            //--checks if user entered and int option--//
            if (in.hasNextInt()) {
                choiceIdx = in.nextInt();
                in.nextLine();
            } else {
                String line = in.nextLine();
                boolean shouldQuit = line.equals("x");
                if (shouldQuit) {
                    return null;
                }
            }

            //--validates user input--//
            if (choiceIdx < 0 || choiceIdx >= choiceKeys.length) {
                System.out.println("Invalid choice. Try again.");
            } else {
                validChoice = true;
            }

        } while (!validChoice);

        return choiceKeys[choiceIdx];
    }

    //--prints list of jobs--//
    private static void printJobs(ArrayList<HashMap<String, String>> someJobs) {
        if (someJobs.isEmpty()) {
            System.out.print("No Results");
        } else {
            for (HashMap<String, String> job : someJobs) {
                System.out.println("\n*****");
                for (Map.Entry<String, String> entry : job.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                System.out.println("*****");
            }
        }
    } }
