package cn.lncsa.ssim.common.events;

/**
 * Created by catten on 11/14/16.
 */
public interface TickEventReceiver {
    void tick(Object... message);
}
