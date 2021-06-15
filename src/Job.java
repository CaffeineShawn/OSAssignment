import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Job implements Comparable<Job>{
    int id;
    int processTime;
    int requiredMemory;
    int requiredTape;
    int turnAroundTime;
    Time finishedTime;
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

    @Override
    public int compareTo(Job other) {
        return this.arriveTime.allMinutes - other.arriveTime.allMinutes;
    }

    @Override
    public String toString() {
        return "作业" + this.id +", 到达时间:" + this.arriveTime + ", 结束时间:" + this.finishedTime;
    }

    public static void main(String[] args) {
        Time currentTime = new Time("10:00");
        Job job1 = new Job(1, "10:00", 25, 15, 2);
        Job job2 = new Job(2, "10:20", 30, 60, 1);
        Job job3 = new Job(3, "10:30", 10, 50, 3);
        Job job4 = new Job(4, "10:35", 20, 10, 2);
        Job job5 = new Job(5, "10:40", 15, 30, 2);

        Job[] jobs = new Job[5];
        jobs[0] = job1;
        jobs[1] = job2;
        jobs[2] = job3;
        jobs[3] = job4;
        jobs[4] = job5;

        Arrays.sort(jobs);

        LinkedList<Job> waitQueue = new LinkedList<>();
        for (Job job : jobs) {
            waitQueue.offer(job);
        }
        FIFOJobScheduling(waitQueue,currentTime,jobs);


    }

    //  作业调动采用先来先服务算法和最小作业优先算法
    //  作业调度是高级调度，它的主要功能是根据一定的算法，从输入井中选中若干个作业，分配必要的资源，如主存、外设等，为它们建立初始状态为就绪的作业进程。

     static boolean resourceMeetsDemand(Job job) {
        // request Memory and Tape
        boolean memoryFlag = false;
        boolean tapeFlag = false;

        // checkMemory()
        if (memory.checkMemory(job.requiredMemory)) {

            System.out.println("作业" + job.id + "满足内存需求");
            memoryFlag = true;
        } else {
            System.out.println("作业" + job.id + "不满足内存需求");
        }

        // checkTape()
        if (tape.checkTape(job.requiredTape)) {

            System.out.println("作业" + job.id + "满足磁带机需求");
            tapeFlag = true;
        } else {
            System.out.println("作业" + job.id + "不满足磁带机需求");
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
        if (waitQueue.peek().arriveTime.allMinutes == currentTime.allMinutes) {
            return true;
        } else {
            return false;
        }
    }

    static Time nextCriticalTime(Queue<Job> waitQueue,Time currentTime) {
        int allMinutes = Integer.MAX_VALUE;

        for (Job job : waitQueue) {

            if (job.arriveTime.allMinutes < allMinutes && job.arriveTime.allMinutes > currentTime.allMinutes) {
                allMinutes = job.arriveTime.allMinutes;
            }
        }
        Time nextCriticalTime = new Time(allMinutes);
        return nextCriticalTime;
    }

    static boolean checkArrival(Queue<Job> waitQueue,Time currentTime, int timeOffset) {
        // 当前时间段有作业到达，可以若等待队列队首的任务满足资源需求则可调入内存中（就绪队列）转化为进程执行
        if ((waitQueue.peek().arriveTime.minute < currentTime.minute + timeOffset) || (currentTime.minute + timeOffset % 60 == 1)) {

            return true;
        } else {
            return false;
        }
    }



    static void FIFOJobScheduling(LinkedList<Job> waitQueue, Time currentTime,Job[] jobs) {

        // 调入内存

        LinkedList<Process> readyQueue = new LinkedList<>();
        while (!waitQueue.isEmpty() || !readyQueue.isEmpty()){

            System.out.println("-------当前时间为" + currentTime+"-------");
            System.out.print("当前内存中作业: ");
            for (Process process : readyQueue) {
                System.out.print(process.job + "\t");
            }
            System.out.print("\n");

            if (!readyQueue.isEmpty()) {
                Time nextCriticalTime = nextCriticalTime(waitQueue, currentTime);
                int givenTime = Integer.min(nextCriticalTime.allMinutes - currentTime.allMinutes, readyQueue.peek().burstTime);
                int releaseJobIndex = Process.FIFOProcessScheduling(readyQueue, currentTime, givenTime);
                if (releaseJobIndex == -1) {
                    ;
                } else {
                    System.out.println("作业" + (releaseJobIndex + 1) + "执行完毕，释放占用资源");
                    jobs[releaseJobIndex].finishedTime = new Time(currentTime.allMinutes);
                    releaseResource(jobs[releaseJobIndex]);
                    releaseJobIndex = -1;
                }

                for (Job job : waitQueue) {
                    if (resourceMeetsDemand(job) && job.arriveTime.allMinutes <= currentTime.allMinutes) {
                        assignResource(job);
                        System.out.println("作业" + job.id + "已进入就绪队列");
                        readyQueue.offer(Process.processify(job));
                        waitQueue.removeFirstOccurrence(job);
                        break;
                    } else if (job.arriveTime.allMinutes > currentTime.allMinutes) {
                        break;
                    }

                }

            } else {
                for (Job job : waitQueue) {
                    if (resourceMeetsDemand(job) && job.arriveTime.allMinutes <= currentTime.allMinutes) {
                        assignResource(job);
                        System.out.println("作业" + job.id + "已进入就绪队列");
                        readyQueue.offer(Process.processify(job));
                        waitQueue.removeFirstOccurrence(job);
                        break;
                    } else if (job.arriveTime.allMinutes > currentTime.allMinutes) {
                        break;
                    }

                }
                Time nextCriticalTime = nextCriticalTime(waitQueue, currentTime);
                int givenTime = Integer.min(nextCriticalTime.allMinutes - currentTime.allMinutes, readyQueue.peek().burstTime);
                Process.FIFOProcessScheduling(readyQueue, currentTime, givenTime);
            }








            System.out.println("-------当前时间为" + currentTime+"-------\n\n");

        }
        // 开始FIFO？？

        // 开始执行

        for (Job job : jobs) {
            System.out.println(job);
        }

    }

    // hello
}



