public class Time {
    int hour;
    int minute;

    Time(String time) {
        String[] timeString = time.split(":");
        hour = Integer.parseInt(timeString[0]);
        minute = Integer.parseInt(timeString[1]);
    }

    @Override
    public String toString() {
        return this.hour + ":" + this.minute;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Time) {
            if (obj == this) {
                return true;
            } else {
                Time timeObj = (Time) obj;
                return this.minute == timeObj.minute && this.hour == timeObj.hour;
            }
        } else {
            return false;
        }
    }

    public void clockingByMinute() {
        if (this.minute == 59) {
            if (this.hour != 23) {
                this.hour += 1;
            } else {
                this.hour = 0;
            }
            this.minute = 0;
        } else {
            this.minute++;
        }
    }

    public void clockingBeyondMinute(int minutes) {
        if (this.minute + minutes < 60) {
            this.minute += minutes;
        } else {
            if (this.hour == 23) {
                this.hour = 0;

            } else {
                this.hour++;
            }
            this.minute = (this.minute + minutes) % 60;
        }
    }
}
