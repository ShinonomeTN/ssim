package cn.lncsa.kingo.jw.cs.caterpillar.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by catten on 2/24/17.
 */
public class KingoRawTimePoint {
    private static Map<String, Integer> weekMap = new HashMap<String, Integer>() {
        {
            put("日", 0);
            put("一", 1);
            put("二", 2);
            put("三", 3);
            put("四", 4);
            put("五", 5);
            put("六", 6);
        }
    };

    private String weekRange;
    private String timePoints;

    public KingoRawTimePoint() {

    }

    public KingoRawTimePoint(String weekRange, String timePoints) {
        this.weekRange = weekRange;
        this.timePoints = timePoints;
    }

    public String getWeekRange() {
        return weekRange;
    }

    public void setWeekRange(String weekRange) {
        this.weekRange = weekRange;
    }

    public String getTimePoints() {
        return timePoints;
    }

    public void setTimePoints(String timePoints) {
        timePoints = timePoints.replace("节","");
        this.timePoints = timePoints;
    }

    private List<Integer> parseWeeks(String parse) {
        String[] list = parse.split(",");
        List<Integer> _weeks = new LinkedList<>();
        for (String s : list) {
            String[] range = s.split("-");
            if (range.length == 1) {
                _weeks.add(Integer.parseInt(range[0]));
            } else {
                int _startWeek = Integer.parseInt(range[0]);
                int _endWeek = Integer.parseInt(range[range.length - 1]);
                for (int i = _startWeek; i <= _endWeek; i++) {
                    _weeks.add(i);
                }
            }
        }
        return _weeks;
    }

    public List<TimePoint> expand() {
        List<TimePoint> timePoints = new LinkedList<>();

        List<Integer> weeks = parseWeeks(weekRange);
        for (Integer week : weeks) {
            String[] strings = this.timePoints.split("\\[|]");
            if (strings.length >= 3) {
                switch (strings[2]) {
                    case "双":
                        if (week % 2 != 0) continue;
                    case "单":
                        if (week % 2 == 0) continue;
                    default:
                        break;
                }
            }

            String[] r = strings[1].split("-");
            if(r.length > 1){
                for (int i = Integer.parseInt(r[0]); i <= Integer.parseInt(r[1]); i++){
                    timePoints.add(new TimePoint(week,weekMap.get(strings[0]),i));
                }
            }else {
                timePoints.add(new TimePoint(week,weekMap.get(strings[0]),Integer.parseInt(r[0])));
            }
        }

        return timePoints;
    }

    @Override
    public String toString() {
        return "KingoRawTimePoint{" +
                "weekRange='" + weekRange + '\'' +
                ", timePoints='" + timePoints + '\'' +
                '}';
    }
}
