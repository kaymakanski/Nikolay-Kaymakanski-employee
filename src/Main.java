import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        String csvFile = System.getProperty("user.dir") + "\\src\\data\\timesheet.csv";
        String line = "";
        String cvsSplitBy = ",";

        List<Record> recordList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                Record record = new Record();

                if (!(recordList.isEmpty())) {
                    for (Record e : recordList) {
                        if (e.getEmployeeID().equals(data[0])) {
                            record = e;
                        } else {
                            record.setEmployeeID(data[0]);
                            record.setAssignedProject(data[1]);
                            record.setDateFrom(parseDate(data[2]));
                            record.setDateTo(data.length == 3 || data[3].equals("NULL") ? LocalDate.now() : parseDate(data[3]));
                        }
                    }
                } else {
                    record.setEmployeeID(data[0]);
                    record.setAssignedProject(data[1]);
                    record.setDateFrom(parseDate(data[2]));
                    record.setDateTo(data.length == 3 || data[3].equals("NULL") ? LocalDate.now() : parseDate(data[3]));
                }
                /*System.out.println("Employee ID is: " + record.getEmployeeID());
                System.out.println("Project ID is: " + record.getAssignedProject());*/

                // Calculate the duration of the project for this employee
                long duration = ChronoUnit.DAYS.between(record.getDateFrom(), record.getDateTo());
               // System.out.println("Duration is: " + duration);
                recordList.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Pair> pairsWhoWorkedTogether = findAllPairsWhoWorkedTogether(recordList);
       /* for (Pair p : pairsWhoWorkedTogether){
            System.out.println("Employee " + p.getFirstEmployeeId() + " and Employee " + p.getSecondEmployeeId() + " have worked " + p.getTotalDuration() + " days together!");
        }*/
        pairsWhoWorkedTogether.sort(Comparator.comparingLong(Pair::getTotalDuration));
       /* for (Pair p : pairsWhoWorkedTogether){
            System.out.println("Employee " + p.getFirstEmployeeId() + " and Employee " + p.getSecondEmployeeId() + " have worked " + p.getTotalDuration() + " days together!");
        }*/
        Pair longestPair = pairsWhoWorkedTogether.get(pairsWhoWorkedTogether.size() - 1);
        System.out.println("Employee " +
                longestPair.getFirstEmployeeId() + " and Employee " +
                longestPair.getSecondEmployeeId() + " have worked the longest together for " +
                longestPair.getTotalDuration() + " days!");
    }

    private static List<Pair> findAllPairsWhoWorkedTogether(List<Record> allRecords){
        List<Pair> pairs = new ArrayList<>();

        for (int i = 0; i < allRecords.size() - 1; i++){
            for (int j = i + 1; j < allRecords.size(); j++){
                Record firstEmployee = allRecords.get(i);
                Record secondEmployee = allRecords.get(j);

                if(firstEmployee.getAssignedProject().equals(secondEmployee.getAssignedProject())
                        && haveWorkedTogether(firstEmployee, secondEmployee)) {
                    long daysWorkedTogether = calculateDaysWorkedTogether(firstEmployee, secondEmployee);

                    if(daysWorkedTogether > 0){
                        updatePairs(pairs, firstEmployee, secondEmployee, daysWorkedTogether);
                    }
                }
            }
        }
        return pairs;
    }

    private static void updatePairs(List<Pair> pairs, Record firstEmpl, Record secondEmpl, long daysWorkedTogether){
        boolean isPairPresent = false;

        for (Pair p : pairs){
            if (isPairPresent(p, firstEmpl.getEmployeeID(), secondEmpl.getEmployeeID())){
                p.addMutualDuration(daysWorkedTogether);
                isPairPresent = true;
            }
        }

        if (!isPairPresent){
            Pair newPair = new Pair(firstEmpl.getEmployeeID(), secondEmpl.getEmployeeID(), daysWorkedTogether);
            pairs.add(newPair);
        }
    }

    private static long calculateDaysWorkedTogether(Record firstEmpl, Record secondEmpl){
        LocalDate mutualWorkStartDate =
                firstEmpl.getDateFrom().isBefore(secondEmpl.getDateFrom()) ?
                        secondEmpl.getDateFrom() : firstEmpl.getDateFrom();

        LocalDate mutualWorkEndDate =
                firstEmpl.getDateTo().isBefore(secondEmpl.getDateTo()) ?
                        firstEmpl.getDateTo() : secondEmpl.getDateTo();

        return ChronoUnit.DAYS.between(mutualWorkStartDate, mutualWorkEndDate);
    }

    // Check if the current pair is already in the collection
    private static boolean isPairPresent(Pair pair, String firstEmplId, String secondEmplId) {
        return ( pair.getFirstEmployeeId().equals(firstEmplId)
                && pair.getSecondEmployeeId().equals(secondEmplId))
                || ( pair.getFirstEmployeeId().equals(secondEmplId)
                && pair.getSecondEmployeeId().equals(firstEmplId));
    }

    // check if two employees have worked together
    private static boolean haveWorkedTogether(Record firstEmpl, Record secondEmpl) {
        //firstStartDate <= secondEndDate and firstEndDate >= secondStartDate
        return (firstEmpl.getDateFrom().isBefore(secondEmpl.getDateTo())
                || firstEmpl.getDateFrom().isEqual(secondEmpl.getDateTo()))
                && (firstEmpl.getDateTo().isAfter(secondEmpl.getDateFrom())
                || firstEmpl.getDateTo().isEqual(secondEmpl.getDateFrom()));
    }

    private static LocalDate parseDate(String dateString) {
        if (dateString == null) {
            return LocalDate.now();
        }

        DateTimeFormatter formatter;
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