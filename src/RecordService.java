import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RecordService {
    String line;
    String cvsSplitBy = ",";

    public List<Record> getRecordList(String csvFilePath) {
        List<Record> recordList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
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
                recordList.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recordList;
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

