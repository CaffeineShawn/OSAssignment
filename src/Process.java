public class Process implements Comparable<Process> {
    public String name;
    public int burstTime;
    public int requiredTime;
    public String arriveTime;
    public int finishedTime = -1;

    @Override
    public int compareTo(Process other) {
        String[] thisArriveTime = this.arriveTime.split(":");
        String[] otherArriveTime = other.arriveTime.split(":");
        return Integer.parseInt(thisArriveTime[0]) - Integer.parseInt(otherArriveTime[0]) == 0 ? Integer.parseInt(thisArriveTime[1]) - Integer.parseInt(otherArriveTime[1]) : Integer.parseInt(thisArriveTime[0]) - Integer.parseInt(otherArriveTime[0]);


    }

    static void processExecute(Process process,int givenTime) {
        process.burstTime -= givenTime;
    }

}
