import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.*;

/**
 * MouseMover - Move o mouse para posições aleatórias quando o usuário
 * estiver ausente por 5 minutos. O mouse é movido em intervalos aleatórios
 * entre 2 e 30 segundos.
 */
public class MM {

//    static final long INACTIVITY_MS = 5 * 60 * 1000; // 5 minutos
    static final long INACTIVITY_MS = 2000;
    static final int  MIN_SEC = 2, MAX_SEC = 30;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        Random random = new Random();
        AtomicLong lastActivity = new AtomicLong(System.currentTimeMillis());

        // Detecta atividade do usuário (mouse + teclado)
        Toolkit.getDefaultToolkit().addAWTEventListener(
            e -> lastActivity.set(System.currentTimeMillis()),
            AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK
        );

        System.out.println("MouseMover iniciado. Ativará após 5 min de inatividade. Ctrl+C para sair.");

        boolean active = false;

        while (true) {
            long idle = System.currentTimeMillis() - lastActivity.get();

            if (idle >= INACTIVITY_MS) {
                if (!active) {
                    active = true;
                    System.out.println("Usuário ausente. Iniciando movimentos...");
                }

                // Move o mouse para posição aleatória
                Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
                int x = random.nextInt(screen.width);
                int y = random.nextInt(screen.height);
                robot.mouseMove(x, y);
                System.out.printf("Mouse movido para (%d, %d)%n", x, y);

                // Aguarda intervalo aleatório entre 2 e 30 segundos
                int delaySec = MIN_SEC + random.nextInt(MAX_SEC - MIN_SEC + 1);
                Thread.sleep(delaySec * 1000L);

            } else {
                if (active) {
                    active = false;
                    System.out.println("Usuário voltou. Pausando movimentos.");
                }
                Thread.sleep(1000); // verifica inatividade a cada segundo
            }
        }
    }
}
