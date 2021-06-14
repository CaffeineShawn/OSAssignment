import java.util.Queue;

public class Process implements Comparable<Process> {
    public String name;
    public int burstTime;
    public int requiredTime;
    public Time arriveTime;
    public int finishedTime = -1;

    @Override
    public int compareTo(Process other) {
        if (this.arriveTime.hour != other.arriveTime.hour) {
            return this.arriveTime.hour - other.arriveTime.hour;
        } else {
            return this.arriveTime.minute - other.arriveTime.minute;
        }

    }

    static Process processify(Job job) {
        Process process = new Process();
        process.requiredTime = process.burstTime = job.processTime;
        process.arriveTime = job.arriveTime;
        return process;
    }

    static void FIFOProcessScheduling(Queue<Process> readyQueue, Time currentTime) {
        Process executingProcess;
        if (!readyQueue.isEmpty()) {
            executingProcess = readyQueue.poll();
            currentTime.clockingBeyondMinute(executingProcess.requiredTime);
            executingProcess.burstTime = 0;

            // checkIfArrive是作业调度的功能
        }


    }


    static void processExecute(Process process,int givenTime) {
        process.burstTime -= givenTime;
    }

}
