public class Memory {
    int total;
    int free;

    Memory(int total) {
        this.total = total;
    }

    public boolean checkMemory(int requiredMemory) {
        return requiredMemory >= this.free;
    }

    public void assignMemory(int assignMemory) {
        this.free -= assignMemory;
    }

    public void releaseMemory(int releaseMemory) {
        this.free += releaseMemory;
    }
}