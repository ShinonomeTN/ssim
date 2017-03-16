package cn.lncsa.ssim.web.view;

/**
 * Created by cattenlinger on 2017/3/16.
 */
public class LessonTimePoint {
    private int week;
    private int day;
    private int turn;

    public LessonTimePoint() {
    }

    public LessonTimePoint(int week, int day, int turn) {
        this.week = week;
        this.day = day;
        this.turn = turn;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
