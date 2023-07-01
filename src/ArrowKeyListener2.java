import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ArrowKeyListener2 implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Registered key released");
//        if (e.getKeyCode() == 38) {
//            moveVertically(true);
//        }
//        if (e.getKeyCode() == 40) {
//            moveVertically(false);
//        }
//        if (e.getKeyCode() == 37) {
//            moveHorizontally(true);
//        }
//        if (e.getKeyCode() == 39) {
//            moveHorizontally(false);
//        }
//
//        checkPosition();

//            System.out.println("Player location = " + playerLocation.toString()); //FIXME

    }
}
