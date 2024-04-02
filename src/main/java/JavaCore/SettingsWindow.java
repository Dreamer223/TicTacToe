package JavaCore;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JFrame {
    public static final int WINDOW_HEIGHT = 350;
    public static final int WINDOW_WIDTH = 350;
    int size=3;
    int sizeWin=3;
    int choice=0;


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
                gameWindow.startNewGame("9x9",9,9,5);
                setVisible(false);
            }
        });


        JPanel modePanel = new JPanel(new GridLayout(3, 1));
        modePanel.add(createModeComponent());
        modePanel.add(createSizeComponent());
        modePanel.add(createWinLengthComponent());

        add(btnStart,"South");
        add(modePanel, "North");
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameWindow.startNewGame(size+"x"+size, size, size, sizeWin);
                setVisible(false);
            }
        });
    }

    private JPanel createModeComponent(){
        JPanel modePanel = new JPanel(new GridLayout(3, 1));

        JLabel message = new JLabel("Выбирете режим игры:");

        JRadioButton choice1 = new JRadioButton("Челоловек против человека");
        JRadioButton choice2 = new JRadioButton("Челоловек против Ai");
        choice2.setSelected(true);

        choice1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choice = 0;
            }
        });

        choice2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choice = 1;
            }
        });

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(choice1);
        buttonGroup.add(choice2);

        modePanel.add(message);
        modePanel.add(choice1);
        modePanel.add(choice2);

        return modePanel;
    }

    private JPanel createSizeComponent(){
        JPanel modePanel = new JPanel(new GridLayout(3, 1));

        JLabel message1 = new JLabel("Выберите размеры поля:");
        JLabel message2 = new JLabel("Установленный размер поля: " + size);

        JSlider sizer = new JSlider(3, 10, size);

        sizer.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                size = sizer.getValue();
                message2.setText("Установленный размер поля: " + size);
            }
        });

        modePanel.add(message1);
        modePanel.add(message2);
        modePanel.add(sizer);

        return modePanel;
    }

    private JPanel createWinLengthComponent(){
        JPanel modePanel = new JPanel(new GridLayout(3, 1));

        JLabel message1 = new JLabel("Выберите длину для победы:");
        JLabel message2 = new JLabel("Установленный длина: " + sizeWin);

        JSlider win = new JSlider(3, 10, sizeWin);

        win.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(win.getValue() > size){
                    win.setValue(size);
                }
                sizeWin = win.getValue();
                message2.setText("Установленный длина: " + sizeWin);
            }
        });

        modePanel.add(message1);
        modePanel.add(message2);
        modePanel.add(win);

        return modePanel;
    }

}
