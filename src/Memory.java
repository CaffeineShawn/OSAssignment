import java.util.LinkedList;

public class Memory {
    int total;
    int free;
    LinkedList<Partition> partitionLinkedList = new LinkedList<>();
            
    


    Memory(int total) {
        this.total = total;
        this.free = total;
        this.partitionLinkedList = new LinkedList<>();
        partitionLinkedList.offer(new Partition(total, 0));
    }

    public boolean checkMemory(int requiredMemory) {
        if (requiredMemory < this.free) {
            return false;
        }
        for (Partition partition : partitionLinkedList) {
            if (partition.partitionSize >= requiredMemory) {
                return true;
            }
        }
        return false;
        
    }
    
    public void assignMemory(int assignMemory) {

        for (Partition partition : partitionLinkedList) {
            
        }
        this.free -= assignMemory;
        
        
    }

    public void releaseMemory(int releaseMemory) {
        this.free += releaseMemory;
    }

    @Override
    public String toString() {
        return "- 共有" + total + "KB内存, 当前空闲" + free + "KB";
    }


}