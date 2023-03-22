import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PairService {
    RecordService recordService = new RecordService();
    public Pair getLongestPair(String csvFilePath){
        List<Pair> pairsWhoWorkedTogether = findAllPairsWhoWorkedTogether(recordService.getRecordList(csvFilePath));
       /* for (Pair p : pairsWhoWorkedTogether){
            System.out.println("Employee " + p.getFirstEmployeeId() + " and Employee " + p.getSecondEmployeeId() + " have worked " + p.getTotalDuration() + " days together!");
        }*/
        pairsWhoWorkedTogether.sort(Comparator.comparingLong(Pair::getTotalDuration));
    /* for (Pair p : pairsWhoWorkedTogether){
         System.out.println("Employee " + p.getFirstEmployeeId() + " and Employee " + p.getSecondEmployeeId() + " have worked " + p.getTotalDuration() + " days together!");
     }*/
        Pair longestPair = pairsWhoWorkedTogether.get(pairsWhoWorkedTogether.size() - 1);
        /*System.out.println("Employee "+
                longestPair.getFirstEmployeeId()+" and Employee "+
                longestPair.getSecondEmployeeId()+" have worked the longest together for "+
                longestPair.getTotalDuration()+" days!");*/

        return longestPair;
    }
    public List<Pair> findAllPairsWhoWorkedTogether(List<Record> allRecords){
        List<Pair> pairs = new ArrayList<>();

        for (int i = 0; i < allRecords.size() - 1; i++){
            for (int j = i + 1; j < allRecords.size(); j++){
                Record firstEmployee = allRecords.get(i);
                Record secondEmployee = allRecords.get(j);

                if(firstEmployee.getAssignedProject().equals(secondEmployee.getAssignedProject())
                        //don't count entries of the same employee
                        && !(firstEmployee.getEmployeeID().equals(secondEmployee.getEmployeeID()))
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
}
