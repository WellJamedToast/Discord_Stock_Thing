import javax.swing.JFrame;

/**
 * This also contains my main thread, it runs the main loop that tells the Panel to update itself and go in the frame
 * Overall this project needs some cleaning up, a lot of public stuff that can be private, possibly some wierd
 * inheritance decisions and a lot of dead unused code that I haven't cleaned yet
 * it won't work without the keys, but if you set up your own have fun
 */
public class GUI extends JFrame{
    private GUIPanel panel;
    public GUI() {
        panel = new GUIPanel();
        add(panel);
        setTitle("Stock Price Viewer");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void start() {
        while (true) {
                panel.updateStocksFromThreads();
                panel.netWorthEct();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            panel.repaint();
        }
    }

    public static void main(String[] args) {
        GUI gui = new GUI();
        gui.setVisible(true);
        gui.start();


    }

}
