package JavaCore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Map extends JPanel {
    public static final Random RANDOM = new Random();
    public static final int DOT_PADDING = 5;


    private int gameOverType;
    public static final int STATE_DRAW = 0;
    public static final int STATE_WIN_HUMAN = 1;
    public static final int STATE_WIN_AI = 2;

    public static final String MSG_DRAW = "Ничья!";
    public static final String MSG_WIN_AI = "Победил компьютер!";
    public static final String MSG_WIN_HUMAN = "Победил игрок!";
    private final int HUMAN_DOT = 1;
    private final int AI_DOT = 2;
    private final int EMPTY_DOT = 0;
    private int fieldSizeY;
    private int fieldSizeX;
    private char[][] field;

    private int panelWidth;
    private int panelHeight;
    private int cellHeight;
    private int cellWidth;

    private boolean isGameOver;
    private boolean isInitialized;
    //    private String mode;
    private int winLength;

    public Map() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                update(e);
            }
        });
        isInitialized = false;
    }


    private void update(MouseEvent e) {
        if (isGameOver || !isInitialized) return;
        int cellX = e.getX() / cellWidth;
        int cellY = e.getY() / cellHeight;
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) return;
        field[cellY][cellX] = HUMAN_DOT;

        repaint();
        if (checkEndGame(HUMAN_DOT, STATE_WIN_HUMAN)) return;
        aiTurn();
        repaint();
        if (checkEndGame(AI_DOT, STATE_WIN_AI)) return;
    }

    private boolean checkEndGame(int dot, int gameOverType) {
        if (checkWin(dot)) {
            this.gameOverType = gameOverType;
            isGameOver = true;
            repaint();
            return true;
        }
        if (isMapFull()) {
            this.gameOverType = STATE_DRAW;
            isGameOver = true;
            repaint();
            return true;
        }
        return false;
    }

    void steartNewGame(String mode, int fSzX, int fSzY, int wLen) {
//        this.mode = mode;
        this.fieldSizeX = fSzX;
        this.fieldSizeY = fSzY;
        this.winLength = wLen;
        System.out.printf("Mode:" + mode + ";\nSize: x = %d, y=%d;\n Win Length: %d",
                fSzX, fSzY, wLen);

        initMap();
        isGameOver = false;
        isInitialized = true;

        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    private void render(Graphics g) {
        if (!isInitialized) return;
        panelWidth = getWidth();
        panelHeight = getHeight();
        cellHeight = panelHeight / fieldSizeY;
        cellWidth = panelWidth / fieldSizeX;


        g.setColor(Color.BLACK);
        for (int h = 0; h < fieldSizeY; h++) {
            int y = h * cellHeight;
            g.drawLine(0, y, panelWidth, y);
        }

        for (int w = 0; w < fieldSizeX; w++) {
            int x = w * cellWidth;
            g.drawLine(x, 0, x, panelHeight);
        }

        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == EMPTY_DOT) continue;
                if (field[y][x] == HUMAN_DOT) {
                    g.setColor(Color.BLUE);
                    g.fillOval(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);

                } else if (field[y][x] == AI_DOT) {
                    g.setColor(new Color(0xff0000));
                    g.fillOval(x * cellWidth + DOT_PADDING,
                            y * cellHeight + DOT_PADDING,
                            cellWidth - DOT_PADDING * 2,
                            cellHeight - DOT_PADDING * 2);

                } else {
                    throw new RuntimeException("Unexpected value " + field[y][x] +
                            "in cell: x=" + x + "y=" + y);
                }
            }
        }
        if (isGameOver) showMessageGameOver(g);
    }

    private void showMessageGameOver(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 200, getWidth(), 70);
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Times new roman", Font.BOLD, 48));
        switch (gameOverType) {
            case STATE_DRAW:
                g.drawString(MSG_DRAW, 180, getHeight() / 2);
                break;
            case STATE_WIN_AI:
                g.drawString(MSG_WIN_AI, 20, getHeight() / 2);
                break;
            case STATE_WIN_HUMAN:
                g.drawString(MSG_WIN_HUMAN, 70, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Unexepted gameOver state: " + gameOverType);

        }
    }

    private void initMap() {
//        mode();
        field = new char[fieldSizeY][fieldSizeX];
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                field[i][j] = EMPTY_DOT;
            }
        }
    }


    private boolean isValidCell(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    private boolean isEmptyCell(int x, int y) {
        return field[y][x] == EMPTY_DOT;
    }

    //TODO: Переработать логику ходов компьютера
    private void aiTurn() {
        int x, y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = AI_DOT;
    }

    //TODO: Перерабоатать проверку на победу
    private boolean checkWin(int dot) {


        // Проверкак по горизонтали
        //TODO: Переработать проверку по горизонтали.
        // Не считает проверку если начинать игру с крайней правой стороны
        for (int i = 0; i < field.length; i++) {
            int count = 0;
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j] == dot) {
                    count++;
                }else{
                    count=0;
                }
                if (count == winLength) {
                    return true;
                }
            }
        }
        // Проверка вертикалям
        for (int i = 0; i < field.length; i++) {
            int count = 0;
            for (int j = 0; j < field[i].length; j++) {
                if (field[j][i] == dot) {
                    count++;
                }else {
                    count=0;
                }
                if (count == winLength) {
                    return true;
                }
            }
        }
        int count=0;
        // Проверка диагоналям
        for (int i = 0; i < fieldSizeY; i++) {
            if (field[i][i] == dot){
                count++;
            }if (count==winLength){
                return true;
            }
        }
        count=0;
        for (int i = 0; i < fieldSizeX; i++) {
            if (field[i][fieldSizeX - 1 - i] == dot) {
                count++;
            }if(count==winLength){
                return true;
            }
        }
        return false;
    }

    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == EMPTY_DOT) return false;
            }
        }
        return true;
    }
}
