package JavaCore;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JFrame {
    public static final int WINDOW_HEIGHT = 230;
    public static final int WINDOW_WIDTH = 350;

    JButton btnStart = new JButton("Start new game");
    JButton mode1 = new JButton("3x3");
    JButton mode2 = new JButton("5x5");
    JButton mode3 = new JButton("9x9");

    SettingsWindow(GameWindow gameWindow){
        setLocationRelativeTo(gameWindow);
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);

        JPanel panel = new JPanel(new GridLayout(1,3));
        panel.add(mode1);
        panel.add(mode2);
        panel.add(mode3);
        mode1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.startNewGame("3x3",3,3,3);
                setVisible(false);
            }
        });
        mode2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.startNewGame("5x5",5,5,4);
                setVisible(false);

            }
        });
        mode3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.startNewGame("9x9",9,9,4);
                setVisible(false);
            }
        });




        add(btnStart);
        add(panel);

    }
}
