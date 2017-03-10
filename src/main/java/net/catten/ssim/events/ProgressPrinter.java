package net.catten.ssim.events;

import net.catten.ssim.common.events.TickEventReceiver;

import java.io.Console;

/**
 * Created by catten on 11/14/16.
 */
public class ProgressPrinter implements TickEventReceiver {

    public static int FIELD_INT_TOTAL = 0;
    public static int FIELD_INT_VALUE = 1;
    public static int FIELD_STRING_MESSAGE = 2;

    private String pattern[] = new String[]{"|","/","-","\\"};
    private int patternCurrent;

    private int total;
    private int value;
    private int size;

    public ProgressPrinter(int size){
        patternCurrent = 0;
        total = 1;
        value = 1;
        this.size = size;
    }

    @Override
    public void tick(Object... message) {
        Console console = System.console();
        if(console!=null){
            total = (int) message[FIELD_INT_TOTAL];
            value = (int) message[FIELD_INT_VALUE];
            tickPattern();
            console.printf("\r[%6s/%6s][%-"+ (size + 1) +"s][%s]Current : %-10s",value,total,getProgressBar(),pattern[patternCurrent],message[FIELD_STRING_MESSAGE]);
        }
    }

    public void tickPattern(){
        if(++patternCurrent > 3) patternCurrent = 0;
    }

    public String getProgressBar(){
        String progressBar = "";
        for (int i = 0;i <= (size * value / total); i++){
            progressBar += "#";
        }
        return progressBar;
    }

    public void setSize(int size){
        this.size = size;
    }
}
