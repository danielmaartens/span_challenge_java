package utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This utils.Print class serves as a DRY way
 * of delaying output to the console.
 *
 * In the beginning we want to simulate the console talking to you
 * and giving you enough time to read the messages.
 *
 * This class serves to make the process of accomplishing that
 * in a more flowing and succinct manner when a situation arises
 * where you may have multiple outputs to the console in sequence
 * which you do not want to display all at once.
 *
 * The class is instantiated with an initialDelay.
 * Once the print.delayed() function is invoked the initialDelay will be added to the current runningDelay.
 *
 * Every time we print.delayed() is invoked it uses the runningDelay property to figure out how long it should sleep for.
 *
 * So every subsequent call to the function will always result in an even withDelay between subsequent outputs.
 */

public class Print {

    private int initialDelay;
    private int runningDelay;

    public Print(int initialDelay) {
        this.initialDelay = initialDelay;
        this.runningDelay = initialDelay;
    }

    public void withDelay(String text) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(text);
            }
        }, this.runningDelay);

        this.runningDelay += this.initialDelay;
    }

    public void withDelay(String text, int delay) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(text);
            }
        }, delay);

        this.runningDelay += this.initialDelay;
    }

    public void ln(String text) {
        System.out.println(text);
    }

    public int getRunningDelay(Integer n) {
        return this.runningDelay + n;
    }

    public int getRunningDelay() {
        return this.runningDelay;
    }

    public Print reset() {
        this.runningDelay = this.initialDelay;
        return this;
    }

    public Print setRunningDelay(int n) {
        this.runningDelay = n;
        return this;
    }


}
