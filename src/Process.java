import java.util.*;

public class Process implements Comparable<Process> {
    public int id;
    public String job;
    public Time startTime;
    public int burstTime;
    public int requiredTime;
    public Time arriveTime;


    @Override
    public int compareTo(Process other) {
        return this.burstTime - other.burstTime;
    }

    @Override
    public String toString() {
        return "进程" + this.id +", burstTime:" + this.burstTime +", 到达时间:" + this.arriveTime ;
    }

    static Process processify(Job job) {
        Process process = new Process();
        process.requiredTime = process.burstTime = job.processTime;
        process.arriveTime = job.arriveTime;
        process.job = "job" + (job.id);
        process.id = job.id;
        return process;
    }

    Process() {

    }

    Process(int burstTime) {
        this.burstTime = burstTime;
    }

    static Queue<Process> sortQueue(Queue<Process> queue) {
        PriorityQueue<Process> priorityQueue = new PriorityQueue<>();
        
        for (Process process : queue) {
            priorityQueue.add(process);
        }

        Queue<Process> sortedQueue = new LinkedList<>();
        while (!priorityQueue.isEmpty()) {
            sortedQueue.offer(priorityQueue.poll());
        }
        
        return sortedQueue;
    }

    static int FIFOProcessScheduling(Queue<Process> readyQueue, Time currentTime,int givenTime) {
        Process executingProcess;

        if (readyQueue.peek().burstTime == readyQueue.peek().requiredTime) {
            readyQueue.peek().startTime = new Time(currentTime.allMinutes);
        }

        if (!readyQueue.isEmpty()) {

            executingProcess = readyQueue.peek();
            executingProcess.burstTime -= givenTime;
            currentTime.clockingBeyondMinute(givenTime);

            // checkIfArrive是作业调度的功能
        }

        if (readyQueue.peek().burstTime == 0) {

            return readyQueue.peek().id;
        }
        return -1;
    }

    static int SPFProcessScheduling(Queue<Process> readyQueue,Time currentTime, int givenTime) {
        Process executingProcess;

        if (readyQueue.peek().burstTime == readyQueue.peek().requiredTime) {
            readyQueue.peek().startTime = new Time(currentTime.allMinutes);
        }

        if (readyQueue.size() == 1) {
            executingProcess = readyQueue.peek();
            executingProcess.burstTime -= givenTime;
            currentTime.clockingBeyondMinute(givenTime);

            // checkIfArrive是作业调度的功能
        } else if (readyQueue.size() > 1) {
            //executingProcess = readyQueue
            readyQueue = sortQueue(readyQueue);

            if (readyQueue.peek().burstTime == readyQueue.peek().requiredTime) {
                readyQueue.peek().startTime = new Time(currentTime.allMinutes);
            }

            executingProcess = readyQueue.peek();
            executingProcess.burstTime -= givenTime;
            currentTime.clockingBeyondMinute(givenTime);


        }

        if (readyQueue.peek().burstTime == 0) {

            return readyQueue.peek().id-1;
        }
        return -1;
    }

    static void processExecute(Process process,int givenTime) {
        process.burstTime -= givenTime;
    }


    public static void main(String[] args) {
        Job job1 = new Job(1, "10:00", 25, 15, 2);
        Job job2 = new Job(2, "10:20", 30, 60, 1);
        Job job3 = new Job(3, "10:30", 10, 50, 3);
        Job job4 = new Job(4, "10:35", 20, 10, 2);
        Job job5 = new Job(5, "10:40", 15, 30, 2);

        Process process1 = Process.processify(job1);
        Process process2 = Process.processify(job2);
        Process process3 = Process.processify(job3);
        Process process4 = Process.processify(job4);
        Process process5 = Process.processify(job5);



        Queue<Process> processQueue = new LinkedList<>();
        processQueue.offer(process1);
        processQueue.offer(process2);
        processQueue.offer(process3);
        processQueue.offer(process4);
        processQueue.offer(process5);

        processQueue = sortQueue(processQueue);

        System.out.println(processQueue);


    }


}

