import java.util.LinkedList;
import java.util.Queue;

public class Job {
    int id;
    int processTime;
    int requiredMemory;
    int requiredTape;
    int turnAroundTime;
    Time arriveTime;

    Job(int id, String  arriveTime, int estimatedTime, int requiredMemory, int requiredTape) {
        this.id = id;
        this.arriveTime = new Time(arriveTime);
        this.processTime = estimatedTime;
        this.requiredMemory = requiredMemory;
        this.requiredTape = requiredTape;
    }

    public static void main(String[] args) {
        Job job1 = new Job(1, "10:20", 25, 15, 2);
        Job job2 = new Job(2, "10:25", 30, 60, 1);
        Job job3 = new Job(3, "10:30", 10, 50, 3);
        Job job4 = new Job(4, "10:35", 20, 10, 2);
        Job job5 = new Job(5, "10:40", 15, 30, 2);
        Memory memory = new Memory(100);
        Tape tape = new Tape(4);
        Queue<Job> waitQueue = new LinkedList<>();
        waitQueue.offer(job1);
        waitQueue.offer(job2);
        waitQueue.offer(job3);
        waitQueue.offer(job4);
        waitQueue.offer(job5);



    }

    //  作业调动采用先来先服务算法和最小作业优先算法
    //  作业调度是高级调度，它的主要功能是根据一定的算法，从输入井中选中若干个作业，分配必要的资源，如主存、外设等，为它们建立初始状态为就绪的作业进程。

    public boolean executeJob(Job job, Memory memory, Tape tape) {
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

    static void checkArrival(Queue<Job> waitQueue,String currentTime) {
//        currentTime.equals()
    }

    // hello
}



