import java.util.Queue;

public class Process implements Comparable<Process> {
    public int id;
    public String job;
    public int burstTime;
    public int requiredTime;
    public Time arriveTime;
    public Time finishedTime = null;

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
        process.job = "job" + (job.id);
        process.id = job.id;
        return process;
    }

    static int FIFOProcessScheduling(Queue<Process> readyQueue, Time currentTime,int givenTime) {
        Process executingProcess;

        if (!readyQueue.isEmpty()) {
            executingProcess = readyQueue.peek();
            executingProcess.burstTime -= givenTime;
            currentTime.clockingBeyondMinute(givenTime);

            // checkIfArrive是作业调度的功能
        }

        if (readyQueue.peek().burstTime == 0) {
            // readyQueue.peek().finishedTime = currentTime;
            return readyQueue.poll().id-1;
        }

        return -1;


    }


    static void processExecute(Process process,int givenTime) {
        process.burstTime -= givenTime;
    }

}
