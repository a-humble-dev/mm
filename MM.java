import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.*;

public class MM {
    private static final long IDLE_TIME_MS = 3 * 60 * 1000;
    private static final long MONITOR_INTERVAL_MS = 500;

    public static void main(String[] args) {
        try {
            Robot robot = new Robot();
            Random random = new Random();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;

            Point lastUserPosition = MouseInfo.getPointerInfo().getLocation();
            long lastActivityTime = System.currentTimeMillis();

            boolean idleMode = false;

            while (true) {
                Point currentPosition = MouseInfo.getPointerInfo().getLocation();

                if (!currentPosition.equals(lastUserPosition)) {
                    lastActivityTime = System.currentTimeMillis();
                    lastUserPosition = currentPosition;

                    if (idleMode) {
                        idleMode = false;
                        System.out.println("User came back");
                    }
                }

                long inactiveTime =
                    System.currentTimeMillis() - lastActivityTime;

                if (!idleMode && inactiveTime >= IDLE_TIME_MS) {
                    idleMode = true;
                    System.out.println("Inactivity detected. Starting auto move");
                }

                if (idleMode) {

                    int x = random.nextInt(screenWidth);
                    int y = random.nextInt(screenHeight);

                    robot.mouseMove(x, y);

                    Point expectedPosition = new Point(x, y);

                    long delay = 500 + random.nextInt(2501);

                    Thread.sleep(delay);

                    Point actualPosition =
                        MouseInfo.getPointerInfo().getLocation();

                    if (!actualPosition.equals(expectedPosition)) {
                        idleMode = false;
                        lastActivityTime = System.currentTimeMillis();
                        lastUserPosition = actualPosition;
                    }
                } else {
                    Thread.sleep(MONITOR_INTERVAL_MS);
                }
            }

        } catch (AWTException e) {
            System.err.println("Robot error: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Program interrupted");
        }
    }
}
