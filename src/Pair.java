public class Pair {
    private String firstEmployeeId;
    private String secondEmployeeId;
    private long totalDuration;

    public Pair(String firstEmployeeId, String secondEmployeeId, long totalDuration) {
        this.setFirstEmployeeId(firstEmployeeId);
        this.setSecondEmployeeId(secondEmployeeId);
        this.setTotalDuration(totalDuration);
    }

    public String getFirstEmployeeId() {
        return this.firstEmployeeId;
    }

    private void setFirstEmployeeId(String firstEmployeeId) {
        this.firstEmployeeId = firstEmployeeId;
    }

    public String getSecondEmployeeId() {
        return this.secondEmployeeId;
    }

    private void setSecondEmployeeId(String secondEmployeeId) {
        this.secondEmployeeId = secondEmployeeId;
    }

    public long getTotalDuration() {
        return this.totalDuration;
    }

    private void setTotalDuration(long totalDuration) {
        this.totalDuration = totalDuration;
    }

    public void addMutualDuration(long duration) {
        this.totalDuration += duration;
    }
}
