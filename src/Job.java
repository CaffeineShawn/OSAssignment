import java.util.LinkedList;
import java.util.Queue;

public class Job {
    int id;
    int processTime;
    int requiredMemory;
    int requiredTape;
    int turnAroundTime;
    Time arriveTime;
    static Memory memory = new Memory(100);
    static Tape tape = new Tape(4);

    Job(int id, String  arriveTime, int estimatedTime, int requiredMemory, int requiredTape) {
        this.id = id;
        this.arriveTime = new Time(arriveTime);
        this.processTime = estimatedTime;
        this.requiredMemory = requiredMemory;
        this.requiredTape = requiredTape;
    }

    public static void main(String[] args) {
        Time currentTime = new Time("10:00");
        Job job1 = new Job(1, "10:00", 25, 15, 2);
        Job job2 = new Job(2, "10:20", 30, 60, 1);
        Job job3 = new Job(3, "10:30", 10, 50, 3);
        Job job4 = new Job(4, "10:35", 20, 10, 2);
        Job job5 = new Job(5, "10:40", 15, 30, 2);

        LinkedList<Job> waitQueue = new LinkedList<>();
        waitQueue.offer(job1);
        waitQueue.offer(job2);
        waitQueue.offer(job3);
        waitQueue.offer(job4);
        waitQueue.offer(job5);
        FIFOJobScheduling(waitQueue,currentTime);


    }

    //  作业调动采用先来先服务算法和最小作业优先算法
    //  作业调度是高级调度，它的主要功能是根据一定的算法，从输入井中选中若干个作业，分配必要的资源，如主存、外设等，为它们建立初始状态为就绪的作业进程。

     static boolean resourceMeetsDemand(Job job) {
        // request Memory and Tape
        boolean memoryFlag = false;
        boolean tapeFlag = false;

        // checkMemory()
        if (memory.checkMemory(job.requiredMemory)) {
            memory.assignMemory(job.requiredMemory);
            System.out.println("作业" + job.id + "的内存分配成功");
            memoryFlag = true;
        } else {
            System.out.println("作业" + job.id + "的内存分配失败");
        }

        // checkTape()
        if (tape.checkTape(job.requiredTape)) {
            tape.assignTape(job.requiredTape);
            System.out.println("作业" + job.id + "的内存分配成功");
            tapeFlag = true;
        } else {
            System.out.println("作业" + job.id + "的磁带机分配失败");
        }

        return memoryFlag && tapeFlag;
    }

    static void assignResource(Job job) {
        memory.assignMemory(job.requiredMemory);
        tape.assignTape(job.requiredTape);
    }

    static void releaseResource(Job job) {
        memory.releaseMemory(job.requiredMemory);
        tape.releaseTape(job.requiredTape);
    }

    static boolean checkArrival(Queue<Job> waitQueue,Time currentTime) {
        // 当前时间有作业到达，可以若等待队列队首的任务满足资源需求则可调入内存中（就绪队列）转化为进程执行
        if (waitQueue.peek().arriveTime.equals(currentTime)) {
            return true;
        } else {
            return false;
        }
    }

    static boolean checkArrival(Queue<Job> waitQueue,Time currentTime, int timeOffset) {
        // 当前时间段有作业到达，可以若等待队列队首的任务满足资源需求则可调入内存中（就绪队列）转化为进程执行
        if ((waitQueue.peek().arriveTime.minute < currentTime.minute + timeOffset) || (currentTime.minute + timeOffset % 60 == 1)) {

            return true;
        } else {
            return false;
        }
    }



    static void FIFOJobScheduling(LinkedList<Job> waitQueue, Time currentTime) {
        Queue<Job> blockQueue = new LinkedList<>();
        // 调入内存
        int givenTime;
        Job currentExecutingJob = null;
        Job backupJob = null;
        Queue<Process> readyQueue = new LinkedList<>();
        while (!waitQueue.isEmpty()){
            if (currentExecutingJob == null && checkArrival(waitQueue, currentTime) && resourceMeetsDemand(waitQueue.peek())) {
//                if (!resourceMeetsDemand(waitQueue.peek()) && checkArrival(waitQueue, currentTime) && waitQueue.size() >= 2) {
//
//                }
                currentExecutingJob = waitQueue.poll();
                // 分配资源
                assignResource(currentExecutingJob);

                readyQueue.offer(Process.processify(currentExecutingJob));

                Process.FIFOProcessScheduling(readyQueue,currentTime,1);

                if (checkArrival(waitQueue, currentTime )) {
                    if (resourceMeetsDemand(waitQueue.peek())) {
                        assignResource(waitQueue.peek());
                        backupJob = waitQueue.peek();
                        readyQueue.offer(Process.processify(waitQueue.peek()));
                    }
                }

            } else if (currentExecutingJob != null) {
                // 继续执行当前进程同时checkArrival
                Process.FIFOProcessScheduling(readyQueue,currentTime,1);
                checkArrival(waitQueue, currentTime);
            }
        }
        // 开始FIFO？？

        // 开始执行

        currentTime.clockingByMinute();

    }

    // hello
}



