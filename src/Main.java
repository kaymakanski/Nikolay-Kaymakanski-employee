import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        String csvFile = System.getProperty("user.dir") + "\\src\\data\\timesheet.csv";
        String line = "";
        String cvsSplitBy = ",";

        // Map to store the project durations for each group of employees
        Map<String, Long> projectDurations = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                String[] data = line.split(cvsSplitBy);

                //int employeeId = Integer.parseInt(data[0]);
                int projectId = Integer.parseInt(data[1]);

                LocalDate dateFrom = parseDate(data[2]);
                LocalDate dateTo = data.length == 3 ? LocalDate.now() : parseDate(data[3]);

                // Calculate the duration of the project for this employee
                long duration = ChronoUnit.DAYS.between(dateFrom, dateTo);

                // Add this employee's duration to the total for this project
                String projectKey = String.valueOf(projectId);
                projectDurations.put(projectKey, projectDurations.getOrDefault(projectKey, 0L) + duration);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Find the group of employees with the longest project duration
        long maxDuration = 0;
        String maxDurationKey = "";
        for (String key : projectDurations.keySet()) {
            if (projectDurations.get(key) > maxDuration) {
                maxDuration = projectDurations.get(key);
                maxDurationKey = key;
            }
        }

        // Find the employees who worked on this project and return their IDs as an array
        String[] employees = maxDurationKey.split("-");
        int[] employeeIds = new int[employees.length];
        for (int i = 0; i < employees.length; i++) {
            employeeIds[i] = Integer.parseInt(employees[i]);
        }
        for(int i = 0; i < employeeIds.length; i++){
            System.out.println(employeeIds[i]);
        }

    }

    private static LocalDate parseDate(String dateString) {
        if (dateString == null) {
            return LocalDate.now();
        }

        DateTimeFormatter formatter = null;
        if (dateString.matches("\\d{4}-\\d{2}-\\d{2}")) {
            formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        } else if (dateString.matches("\\d{2}-\\d{2}-\\d{2}")) {
            formatter = DateTimeFormatter.ofPattern("dd-MM-yy");
        } else {
            throw new IllegalArgumentException("Unsupported date format: " + dateString);
        }

        return LocalDate.parse(dateString, formatter);
    }
}