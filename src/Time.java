public class Time {
    int hour;
    int minute;
    int allMinutes;

    Time(String time) {
        String[] timeString = time.split(":");
        this.hour = Integer.parseInt(timeString[0]);
        this.minute = Integer.parseInt(timeString[1]);
        this.allMinutes = hour * 60 + minute;
    }

    Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        this.allMinutes = 60 * hour + minute;
    }

    Time(int allMinutes) {
        this.hour = allMinutes / 60;
        this.minute = allMinutes % 60;
        this.allMinutes = allMinutes;
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
        this.allMinutes = hour * 60 + minute;
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
        this.allMinutes = hour * 60 + minute;
    }

     static boolean largerThan(Time one,Time another) {
        if (one.hour != another.hour) {
            return one.hour - another.hour > 0;
        } else {
            return one.minute - another.minute > 0;
        }

    }

    public void add(int minutes) {
        if (this.minute + minutes >= 60) {
            this.hour += 1;
        }
        this.minute = (this.minute + minutes) % 60;
        this.allMinutes = hour * 60 + minute;
    }

    @Override
    public String toString() {
        return this.hour + ":" + (this.minute < 10 ? "0" + this.minute : this.minute);
    }

}
