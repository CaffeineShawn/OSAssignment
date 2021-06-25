import java.util.*;

public class Job{
    int id;
    int processTime;
    double weightedTurnAroundTime;
    int requiredMemory;
    int requiredTape;
    Time startTime;
    Time finishedTime;
    Time arriveTime;

    static Memory memory = new Memory(100);
    static Tape tape = new Tape(4);
    static int choice1;
    static int choice2;


    Job(int id, String arriveTime, int estimatedTime, int requiredMemory, int requiredTape) {
        this.id = id;
        this.arriveTime = new Time(arriveTime);
        this.processTime = estimatedTime;
        this.requiredMemory = requiredMemory;
        this.requiredTape = requiredTape;
    }



    @Override
    public String toString() {
        return "作业" + this.id + ", 到达时间:" + this.arriveTime + ", 开始时间:" + this.startTime + ", 结束时间:" + this.finishedTime + ", 周转时间:" + (this.finishedTime.allMinutes - this.arriveTime.allMinutes) +", 带权周转时间:" + (this.weightedTurnAroundTime);
    }

    public static void main(String[] args) {
        Time currentTime = new Time("10:00");
        Job job1 = new Job(1, "10:00", 25, 15, 2);
        Job job2 = new Job(2, "10:20", 30, 60, 1);
        Job job3 = new Job(3, "10:30", 10, 10, 1);
        Job job4 = new Job(4, "10:35", 20, 30, 3);
        Job job5 = new Job(5, "10:40", 15, 30, 2);

        Job[] jobs = new Job[5];
        jobs[0] = job1;
        jobs[1] = job2;
        jobs[2] = job3;
        jobs[3] = job4;
        jobs[4] = job5;




        LinkedList<Job> waitQueue = new LinkedList<>();
        for (Job job : jobs) {
            waitQueue.offer(job);
        }
        Scanner sc = new Scanner(System.in);
        System.out.print("choice1: ");
        choice1 = sc.nextInt();


        System.out.print("choice2: ");
        choice2 = sc.nextInt();

        if (choice1 == 1) {
            Arrays.sort(jobs, Comparator.comparingInt(o -> o.arriveTime.allMinutes));
        }
        else if (choice1 == 2) {
            Arrays.sort(jobs, Comparator.comparingInt(o -> o.processTime));
        }


//        if (choice1 == 1) {
//            UniversalJobScheduling(waitQueue, currentTime, jobs);
//        } else if (choice1 == 2) {
//            UniversalJobScheduling(waitQueue, currentTime, jobs);
//        }
        UniversalJobScheduling(waitQueue, currentTime, jobs);






//        FIFOJobScheduling(waitQueue,currentTime,jobs);
//        SJFJobScheduling(waitQueue,currentTime,jobs);


        double turnAroundTime = 0;
        double weightedTurnAroundTime = 0;

        Arrays.sort(jobs, Comparator.comparingInt(o -> o.id));

        for (Job job : jobs) {
            System.out.println(job);
            turnAroundTime += job.finishedTime.allMinutes - job.arriveTime.allMinutes;
            weightedTurnAroundTime += job.weightedTurnAroundTime;
        }

        System.out.println("平均周转时间为: " + turnAroundTime / (jobs.length));
        System.out.println("平均带权周转时间为: " + weightedTurnAroundTime / (jobs.length));


    }

    //  作业调动采用先来先服务算法和最小作业优先算法
    //  作业调度是高级调度，它的主要功能是根据一定的算法，从输入井中选中若干个作业，分配必要的资源，如主存、外设等，为它们建立初始状态为就绪的作业进程。

    static boolean resourceMeetsDemand(Job job) {
        // request Memory and Tape
        boolean memoryFlag = false;
        boolean tapeFlag = false;

        // checkMemory()
        if (memory.checkMemory(job.requiredMemory)) {

//            System.out.println("- 作业" + job.id + "满足内存需求");
            memoryFlag = true;
        }  //            System.out.println("- 作业" + job.id + "不满足内存需求");


        // checkTape()
        if (tape.checkTape(job.requiredTape)) {

//            System.out.println("- 作业" + job.id + "满足磁带机需求");
            tapeFlag = true;
        }  //            System.out.println("- 作业" + job.id + "不满足磁带机需求");


        return memoryFlag && tapeFlag;
    }

    static void assignResource(Job job) {
        memory.assignMemory(job.requiredMemory,job);
        tape.assignTape(job.requiredTape);
    }

    static void releaseResource(Job job) {
        memory.releaseMemory(job.requiredMemory,job);
        tape.releaseTape(job.requiredTape);
    }

//    static boolean checkArrival(Queue<Job> waitQueue, Time currentTime) {
//        // 当前时间有作业到达，可以若等待队列队首的任务满足资源需求则可调入内存中（就绪队列）转化为进程执行
//        if (waitQueue.peek().arriveTime.allMinutes == currentTime.allMinutes) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    static Time nextCriticalTime(Queue<Job> waitQueue, Time currentTime) {
        int allMinutes = Integer.MAX_VALUE;

        for (Job job : waitQueue) {

            if (job.arriveTime.allMinutes < allMinutes && job.arriveTime.allMinutes > currentTime.allMinutes) {
                allMinutes = job.arriveTime.allMinutes;
            }
        }
        return new Time(allMinutes);
    }

//    static boolean checkArrival(Queue<Job> waitQueue, Time currentTime, int timeOffset) {
//        // 当前时间段有作业到达，可以若等待队列队首的任务满足资源需求则可调入内存中（就绪队列）转化为进程执行
//        if ((waitQueue.peek().arriveTime.minute < currentTime.minute + timeOffset) || (currentTime.minute + timeOffset % 60 == 1)) {
//
//            return true;
//        } else {
//            return false;
//        }
//    }


    static void UniversalJobScheduling(LinkedList<Job> waitQueue, Time currentTime, Job[] jobs) {

        // 调入内存

        Queue<Process> readyQueue = new LinkedList<>();
        while (!waitQueue.isEmpty() || !readyQueue.isEmpty()) {

            System.out.println("-------当前时间为" + currentTime + "-------");
            System.out.println(tape);
            System.out.println(memory);
            job2Process(waitQueue, currentTime, readyQueue);
            System.out.print("- 当前内存中作业: ");
            for (Process process : readyQueue) {
                System.out.print(process.job + "\t");
            }
            System.out.print("\n");


            if (!readyQueue.isEmpty()) {
                Time nextCriticalTime = nextCriticalTime(waitQueue, currentTime);
                int givenTime = Integer.min(nextCriticalTime.allMinutes - currentTime.allMinutes, readyQueue.peek().burstTime);
                if (choice2 == 2) {
                    readyQueue = Process.sortQueue(readyQueue);
                }
                processSchedulingWithResourceRelease(waitQueue, currentTime, jobs, readyQueue, givenTime);

            } else {
                readyQueue = processScheduling(waitQueue, currentTime, jobs, readyQueue);
            }

            System.out.println("-------时间段截止" + currentTime + "-------\n\n");

        }
        // 开始FIFO？？

        // 开始执行


    }

    private static Queue<Process> processScheduling(LinkedList<Job> waitQueue, Time currentTime, Job[] jobs, Queue<Process> readyQueue) {
        job2Process(waitQueue, currentTime, readyQueue);
        Time nextCriticalTime = nextCriticalTime(waitQueue, currentTime);
        int givenTime = Integer.min(nextCriticalTime.allMinutes - currentTime.allMinutes, readyQueue.peek().burstTime);
        jobs[readyQueue.peek().id - 1].startTime = currentTime;
        if (choice2 == 2) {
            readyQueue = Process.sortQueue(readyQueue);
        }
        Process.UniversalProcessScheduling(readyQueue, currentTime, givenTime);
        return readyQueue;
    }


    private static Job findJobById(Job[] jobs,int id) {
        for (Job job : jobs) {
            if (job.id == id) {
                return job;
            }
        }
        return null;
    }

    private static void processSchedulingWithResourceRelease(LinkedList<Job> waitQueue, Time currentTime, Job[] jobs, Queue<Process> readyQueue, int givenTime) {
        int releaseJobId = Process.UniversalProcessScheduling(readyQueue, currentTime, givenTime);
        if (releaseJobId != -1)  {
            System.out.println("- 作业" + releaseJobId + "于时间" + currentTime + "执行完毕，释放占用资源");
            Job finishedJob = findJobById(jobs,releaseJobId);
            finishedJob.finishedTime = new Time(currentTime.allMinutes);
            finishedJob.startTime = new Time(readyQueue.poll().startTime.allMinutes);
            finishedJob.weightedTurnAroundTime = (finishedJob.finishedTime.allMinutes -  finishedJob.arriveTime.allMinutes) / (double) finishedJob.processTime;
            releaseResource(finishedJob);

            System.out.println(tape);
            System.out.println(memory);
        }

        job2Process(waitQueue, currentTime, readyQueue);
    }

    private static void job2Process(LinkedList<Job> waitQueue, Time currentTime, Queue<Process> readyQueue) {
        for (Job job : waitQueue) {
            if (resourceMeetsDemand(job) && job.arriveTime.allMinutes <= currentTime.allMinutes) {
                assignResource(job);
                System.out.println("- 作业" + job.id + "于时间" + currentTime + "进入就绪队列");
//                    job.startTime = new Time(currentTime.allMinutes);
                readyQueue.offer(Process.processify(job));
                waitQueue.removeFirstOccurrence(job);
                break;
            } else if (job.arriveTime.allMinutes > currentTime.allMinutes) {
                break;
            }

        }
    }




//    static LinkedList<Job> SJFwaitQueue(LinkedList<Job> waitQueue, Time currentTime) {
//
//        PriorityQueue<Job> arrivedJobQueue = new PriorityQueue<>(new Comparator<Job>() {
//            @Override
//            public int compare(Job o1, Job o2) {
//               return -o1.processTime + o2.processTime;
//            }
//        });
//
//        for (Job job : waitQueue) {
//            if (job.arriveTime.allMinutes <= currentTime.allMinutes) {
//                waitQueue.removeFirstOccurrence(job);
//                arrivedJobQueue.offer(job);
//            }
//        }
//
//        waitQueue.offerFirst(arrivedJobQueue.poll());
//
//        return waitQueue;
//    }





//    static void UniversalJobScheduling(LinkedList<Job> waitQueue, Time currentTime, Job[] jobs) {
//
//        // 调入内存
//
//        Queue<Process> readyQueue = new LinkedList<>();
//        while (!waitQueue.isEmpty() || !readyQueue.isEmpty()) {
//
//            System.out.println("-------当前时间为" + currentTime + "-------");
//            System.out.println(tape);
//            System.out.println(memory);
//
//            //waitQueue = SJFwaitQueue(waitQueue, currentTime);
//            job2Process((LinkedList<Job>) waitQueue, currentTime, readyQueue);
//
//            System.out.print("- 当前内存中作业: ");
//            for (Process process : readyQueue) {
//                System.out.print(process.job + "\t");
//            }
//            System.out.print("\n");
//
//
//            if (!readyQueue.isEmpty()) {
//                Time nextCriticalTime = nextCriticalTime(waitQueue, currentTime);
//                int givenTime = Integer.min(nextCriticalTime.allMinutes - currentTime.allMinutes, readyQueue.peek().burstTime);
//                if (choice2 == 2) {
//                    readyQueue = Process.sortQueue(readyQueue);
//                }
//
//                processSchedulingWithResourceRelease((LinkedList<Job>) waitQueue, currentTime, jobs, readyQueue, givenTime);
//
//            } else {
//                readyQueue = processScheduling((LinkedList<Job>) waitQueue, currentTime, jobs, readyQueue);
//            }
//
//            System.out.println("-------时间段截止" + currentTime + "-------\n\n");
//
//        }
//        // 开始FIFO？？
//
//        // 开始执行
//
//
//    }

//    public Job findByProcess(Process process) {
//
//    }





    // hello
}



