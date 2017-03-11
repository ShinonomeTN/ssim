package cn.lncsa.ssim.web.services.util;

import cn.lncsa.ssim.common.events.TickEventReceiver;

/**
 * Created by catten on 3/4/17.
 */
public class TickModel implements TickEventReceiver {
    private Integer taskTotal = 0;
    private Integer taskCurrent = 0;
    private String taskStatus;

    @Override
    public void tick(Object... message) {

        Integer _total = (Integer) message[0];
        Integer _current = (Integer) message[1];

        if (!taskTotal.equals(_total)) taskTotal = _total;
        setTaskCurrent(_current);
        setTaskStatus((String) message[2]);
    }

    /*
    *
    * Getter setter
    *
    * */

    public synchronized Integer getTaskTotal() {
        return taskTotal;
    }

    public synchronized void setTaskTotal(Integer taskTotal) {
        this.taskTotal = taskTotal;
    }

    public synchronized void setTaskCurrent(Integer taskCurrent) {
        this.taskCurrent = taskCurrent;
    }

    public synchronized String getTaskStatus() {
        return taskStatus;
    }

    public synchronized void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Integer getTaskCurrent() {
        return taskCurrent;
    }
}
