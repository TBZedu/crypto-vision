package Model;

/**
 * Time is a class that can save a timeperiod
 */
public class Time {
    public int years; // 1 year = 365
    public int months; // 1 month = 30 days
    public int days;
    public int hours;
    public int min;

    public Time(int min){
        this.min = min;
    }

    /**
     * calculats the minutes from the saved values (min, hours, days, months and years)
     * @return the time in minutes
     */
    public int getMinute(){
        int min = this.min;
        min += this.hours * 60;
        min += this.days * 60 * 24;
        min += this.months * 60 * 24 * 30;
        min += this.years * 60 * 24 * 365;
        return min;
    }

    //TODO implement all methods/vars
}
