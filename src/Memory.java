public class Memory {
    int total;
    int free;


    Memory(int total) {
        this.total = total;
        this.free = total;
    }

    public boolean checkMemory(int requiredMemory) {
        return requiredMemory <= this.free;
    }

    public void assignMemory(int assignMemory) {
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