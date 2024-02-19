package JavaCore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameWindow extends JFrame {
    public static final int WINDOW_HEIGHT = 555;
    public static final int WINDOW_WIDTH = 507;
    public static final int WINDOW_POSX = 800;
    public static final int WINDOW_POSY = 300;

    JButton btnStart = new JButton("New game");
    JButton btnExit = new JButton("Exit");
    Map map;
    SettingsWindow settings;

    public GameWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(WINDOW_POSX, WINDOW_POSY);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("TicTacToe");
        map = new Map();
        settings = new SettingsWindow(this);

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settings.setVisible(true);
            }
        });

        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(btnStart);
        panel.add(btnExit);
        add(panel, BorderLayout.SOUTH);
        add(map);


        setVisible(true);

    }

    public void startNewGame(String mode, int fSzX, int fSzY, int wLen) {
        map.steartNewGame(mode, fSzX, fSzY, wLen);
    }
}
