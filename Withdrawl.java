package P_2;

import java.io.IOException;
import java.util.Random;

public class Withdrawal implements Runnable {

    private static int MAX_WITHDRAWAL = 99;
    private static Random generator = new Random();
    private static Random sleeptime = new Random();
    private Account sharedLocation;

    String threadname;

    public Withdrawal(Account shared, String name) {
        sharedLocation = shared;
        threadname = name;
    }

    public void run() {
        while (true) {
            try {
                sharedLocation.withdraw(generator.nextInt(MAX_WITHDRAWAL - 1 + 1) + 1, threadname);
                Thread.sleep(generator.nextInt(100));
                Thread.sleep(sleeptime.nextInt(100 - 1 + 1) + 1);
                Thread.sleep(500);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public static class Account {
        // Account implementation goes here
    }
}
