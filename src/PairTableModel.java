import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PairTableModel extends AbstractTableModel {
    private final List<Pair> data;
    private final String[] columnNames = {"Employee ID #1", "Employee ID #2", "Days worked together"};

    public PairTableModel(List<Pair> data) {
        this.data = data;
    }
    public void setData(List<Pair> newData){
        data.clear();
        data.addAll(newData);
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    @Override
    public String getColumnName(int column){
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pair pair = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> pair.getFirstEmployeeId();
            case 1 -> pair.getSecondEmployeeId();
            case 2 -> pair.getTotalDuration();
            default -> throw new IndexOutOfBoundsException();
        };
    }
}
