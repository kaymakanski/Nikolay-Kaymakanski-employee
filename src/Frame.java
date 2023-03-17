import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Frame extends JFrame {
    PairService pairService = new PairService();
    RecordService recordService = new RecordService();

    private JButton button1;
    private JButton button2;
    private JTable table;
    private PairTableModel model;

    public Frame(){
        model = new PairTableModel(new ArrayList<>());
        table = new JTable(model);
        button1 = new JButton("Load a .csv file");
        button1.setFocusable(false);
        //button.setBounds(200,100,100,50);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == button1){
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));

                    if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
                        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                        List<Pair> pairsWhoWorkedTogether = pairService.findAllPairsWhoWorkedTogether(recordService.getRecordList(file.getPath()));
                        model.setData(pairsWhoWorkedTogether);
                    }
                }
            }
        });

        button2 = new JButton("Show Longest Pair");
        button2.setFocusable(false);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == button2){

                    new LongestPairFrame();
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(button1, BorderLayout.WEST);
        panel.add(button2, BorderLayout.EAST);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        getContentPane().add(panel);

        setTitle("Employees");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }
}

