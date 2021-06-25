import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class Partition implements Comparable<Partition> {
    public static void main(String[] args) {
        // 直接输入预先测试用例，可写在testExample的开头
        Scanner sc = new Scanner(System.in);

//        // 选择分区置换算法
//        int algorithmSelection = 0;
//        while (algorithmSelection != 1  && algorithmSelection != 2) {
//            System.out.print("1 for first-fit and 2 for best-fit:");
//            algorithmSelection = sc.nextInt();
//        }
//        System.out.println("You have chosen:" + (algorithmSelection - 1 == 0 ? "first-fit" : "best-fit") );



        // 初始化分区起始地址
        System.out.print("请输入分区的起始地址:");
        int startAddress = sc.nextInt();

        // 初始化分区及进程
        LinkedList<Partition> partitionLinkedList = new LinkedList<>();
        LinkedList<Process> processes = new LinkedList<>();
        System.out.print("请输入分区的大小:");
        partitionLinkedList.add(new Partition(sc.nextInt(), startAddress));

        // 展示一下分区
        for (Partition partition : partitionLinkedList) {
            System.out.println(partition.toString());
        }

//        FirstFitPartition(partitionLinkedList,job);
//        // 初始化作业号为1
//        int processId = 1;

//        boolean looping = true;
//        while (looping) {
//            System.out.print("1 for alloc, 2 for release, 3 for exit:");
//            int selection = sc.nextInt();
//            switch (selection) {
//                case 1: {
//                    // 新作业来了
//                    System.out.print("请输入作业" + processId + "需要的空间大小:");
//                    Process newProcess = new Process(sc.nextInt(), processId++);
//                    processes.add(newProcess);
//                    if (algorithmSelection == 1) {
//                        FirstFitPartition(partitionLinkedList, newProcess);
//                    } else {
//                        BestFitPartition(partitionLinkedList,newProcess);
//                    }
//
//
//                    break;
//                }
//                case 2: {
//                    // 释放作业所占内存，可以释放部分
//                    System.out.print("需要释放的作业号为:");
//                    releasePartition(partitionLinkedList, processes.get(sc.nextInt() - 1));
//                    break;
//                }
//                case 3: {
//                    // 退出
//                    looping = false;
//                    break;
//                }
//            }
//            MergeFreePartition(partitionLinkedList);
//            MergeFreePartition(partitionLinkedList);// in case of merging the latter one :)
//            for (Partition partition : partitionLinkedList) {
//                System.out.println(partition.toString());
//            }
//        }
//        System.out.print("输入任意值结束:");
//        String end = sc.next();
    }

    int partitionSize;
    int startAddress=0;
    Job job = null;
    boolean assigned = false;

    // 分区的构造器
    Partition(int partitionSize, int startAddress) {
        this.startAddress = startAddress;
        this.partitionSize = partitionSize;
    }

    @Override
    public String toString() {
        // 简单拼接字符串输出
        String res =
                "当前分区起始地址:" + this.startAddress + ",当前分区大小:" + this.partitionSize + ",当前分区分配情况:" + (this.assigned ? "assigned" : "free");
        if (this.assigned) {
            res = res + ",当前分区作业号:" + this.job.id;
        }
        return res;
    }

    @Override
    public int compareTo(Partition o) {
        return -this.partitionSize + o.partitionSize;
    }

    static boolean checkMemoryPartition(LinkedList<Partition> partitionLinkedList, int requiredMemory) {
        for (Partition partition : partitionLinkedList) {
            if (partition.partitionSize >= requiredMemory) {
                return true;
            }
        }
        return false;
    }

    static void assignMemoryPartition(LinkedList<Partition> partitionLinkedList, Job job) {
        FirstFitPartition(partitionLinkedList, job);
    }


    static void MergeFreePartition(LinkedList<Partition> partitionLinkedList) {
        // 合并两个空闲分区，如果没有就不做任何操作
        Partition formerPart;
        ListIterator<Partition> listIterator = partitionLinkedList.listIterator();

        formerPart = listIterator.next();
        while (listIterator.hasNext()) {

            Partition currentPart = listIterator.next();
            if (!formerPart.assigned && !currentPart.assigned) {
                formerPart.partitionSize += currentPart.partitionSize;

                partitionLinkedList.remove(currentPart);
                break;

            }

            formerPart = currentPart;
        }
    }

    static boolean FirstFitPartition(LinkedList<Partition> partitionLinkedList, Job job) {
        // 首次适应算法



        for (Partition currentPartition : partitionLinkedList) {
            if (currentPartition.partitionSize >= job.requiredMemory && !currentPartition.assigned) {
                // 可分配
                int currentId = partitionLinkedList.lastIndexOf(currentPartition);
                // 新分区，原分区一分为二成一个占用的分区和一个空闲的分区
                Partition newPartition = new Partition(currentPartition.partitionSize - job.requiredMemory, currentPartition.startAddress + job.requiredMemory);
                currentPartition.partitionSize = job.requiredMemory;
                currentPartition.assigned = true;

                currentPartition.job = job;

                partitionLinkedList.add(currentId + 1, newPartition);


                return true;

            }
        }

        System.out.println("Could not assign 作业" + job.id + ", skipping...");
        return false;


    }

    static boolean BestFitPartition(LinkedList<Partition> partitionLinkedList, Job job) {
        Partition bestFitPartition = null;
        int bestFitIndex = -1;
        // 最佳适应算法
        for (Partition currentPartition : partitionLinkedList) {
            if (!currentPartition.assigned){
                if (bestFitPartition == null) {
                    //  开始寻找最佳适应分区
                    if (job.requiredMemory <= currentPartition.partitionSize) {
                        bestFitPartition = currentPartition;
                        bestFitIndex = partitionLinkedList.indexOf(currentPartition);
                    }
                } else if ((currentPartition.partitionSize - job.requiredMemory >= 0)&&(currentPartition.partitionSize - job.requiredMemory <= bestFitPartition.partitionSize - job.requiredMemory)) {
                    bestFitPartition = currentPartition;
                    bestFitIndex = partitionLinkedList.indexOf(currentPartition);
                }
            }



        }


        if (bestFitPartition == null) {
            System.out.println("Could not assign 作业" + job.id + ", skipping...");
            return false;
        } else {
            // 新分区，原分区一分为二成一个占用的分区和一个空闲的分区
            Partition newPartition = new Partition(bestFitPartition.partitionSize - job.requiredMemory, bestFitPartition.startAddress + job.requiredMemory);
            bestFitPartition.partitionSize = job.requiredMemory;
            bestFitPartition.assigned = true;
            bestFitPartition.job = job;

            partitionLinkedList.add(bestFitIndex + 1, newPartition);
            return true;
        }


    }

    // 释放分区
    static void releasePartition(LinkedList<Partition> partitionLinkedList, Job releasingJob) {
        Scanner sc = new Scanner(System.in);
        for (Partition currentPartition : partitionLinkedList) {

            if (currentPartition.job == releasingJob) {

                currentPartition.job = null;
                currentPartition.assigned = false;


                MergeFreePartition(partitionLinkedList);
                MergeFreePartition(partitionLinkedList);

                return;
            }
        }

    }


}




