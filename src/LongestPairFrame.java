import javax.swing.*;
import java.util.List;

public class LongestPairFrame {
    private final String csvFilePath = System.getProperty("user.dir") + "\\src\\data\\timesheet.csv";
    private final PairService pairService;
    private final JFrame frame = new JFrame();
    private JLabel label;
    private Pair longestPair;

    LongestPairFrame(){
        pairService = new PairService();
        longestPair = pairService.getLongestPair(csvFilePath);
        label = new JLabel(getLabelText(longestPair));

        label.setBounds(0,0,400,200);
        frame.add(label);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    private static String getLabelText(Pair longestPair){
        return ("Employee "+
                longestPair.getFirstEmployeeId()+" and Employee "+
                longestPair.getSecondEmployeeId()+" have worked together for "+
                longestPair.getTotalDuration()+" days!");
    }
}
