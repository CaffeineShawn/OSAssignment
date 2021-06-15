public class Tape {
    int total;
    int free;

    Tape(int total) {
        this.total = total;
        this.free = total;
    }


    public boolean checkTape(int requiredTape) {
        return requiredTape <= this.free;
    }

    public void assignTape(int assignTape) {
        this.free -= assignTape;
    }

    public void releaseTape(int releaseTape) {
        this.free += releaseTape;
    }
}