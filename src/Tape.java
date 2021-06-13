public class Tape {
    int total;
    int free;

    Tape(int total) {
        this.total = total;
    }


    public boolean checkTape(int requiredTape) {
        return requiredTape >= this.free;
    }

    public void assignTape(int assignTape) {
        this.free -= assignTape;
    }
}