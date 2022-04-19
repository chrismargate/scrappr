package expressoapp;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

public class TestURLImage {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        try {
            String path = "https://scontent.fmnl2-1.fna.fbcdn.net/v/t1.6435-9/197964117_4209584259102372_9057906903961384461_n.jpg?_nc_cat=105&ccb=1-3&_nc_sid=09cbfe&_nc_eui2=AeHFn2a3oiQ6FxHzPOGb8bInVu4FaSTE2TFW7gVpJMTZMWZXU0vEXV_BZOQUqFv13IA6jDSHEyLSaw-H9xMAKw2_&_nc_ohc=oE3CEek-BBMAX_ZQoxf&_nc_ht=scontent.fmnl2-1.fna&oh=c8e49b97d24185c74b339b0ab1c0d7ea&oe=60F2F066";
            System.out.println("Get Image from " + path);
            URL url = new URL(path);
            BufferedImage image = ImageIO.read(url);
            System.out.println("Load image into frame...");
            JLabel label = new JLabel(new ImageIcon(image));
            JFrame f = new JFrame();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.getContentPane().add(label);
            f.pack();
            f.setLocation(200, 200);
            f.setVisible(true);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

}