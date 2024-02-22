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

    private void aiTurn() {
        // Проверка на возможность завершить свою выигрышную комбинацию
        if (tryToWin(AI_DOT)) {
            return;
        }
        // Проверка на необходимость блокировать выигрышную комбинацию игрока
        if (tryToWin(HUMAN_DOT)) {
            return;
        }

        // Если не найдено выигрышных комбинаций, сделать случайный ход
        int x, y;
        do {
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = AI_DOT;
    }

    private boolean tryToWin(int dot) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                // Проверка по горизонтали
                if (j <= field[i].length - winLength && tryLine(dot, i, j, 0, 1)) {
                    return true;
                }
                // Проверка по вертикали
                if (i <= field.length - winLength && tryLine(dot, i, j, 1, 0)) {
                    return true;
                }
                // Проверка по основной диагонали
                if (i <= field.length - winLength && j <= field[i].length - winLength && tryLine(dot, i, j, 1, 1)) {
                    return true;
                }
                // Проверка по побочной диагонали
                if (i >= winLength - 1 && j <= field[i].length - winLength && tryLine(dot, i, j, -1, 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean tryLine(int dot, int startX, int startY, int dx, int dy) {
        int count = 0;
        int emptyCells = 0;
        for (int i = 0; i < winLength; i++) {
            if (field[startX + i * dx][startY + i * dy] == dot) {
                count++;
            } else if (field[startX + i * dx][startY + i * dy] == EMPTY_DOT) {
                emptyCells++;
            }
        }
        // Если есть winLength - 1 занятых ячеек и одна свободная, ставим в свободную
        if (count == winLength - 1 && emptyCells == 1) {
            for (int i = 0; i < winLength; i++) {
                if (field[startX + i * dx][startY + i * dy] == EMPTY_DOT) {
                    field[startX + i * dx][startY + i * dy] = AI_DOT;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkWin(int dot) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                // Проверка по горизонтали
                if (j <= field[i].length - winLength && checkLine(dot, i, j, 0, 1)) {
                    return true;
                }
                // Проверка по вертикали
                if (i <= field.length - winLength && checkLine(dot, i, j, 1, 0)) {
                    return true;
                }
                // Проверка по основной диагонали
                if (i <= field.length - winLength && j <= field[i].length - winLength && checkLine(dot, i, j, 1, 1)) {
                    return true;
                }
                // Проверка по побочной диагонали
                if (i >= winLength - 1 && j <= field[i].length - winLength && checkLine(dot, i, j, -1, 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkLine(int dot, int startX, int startY, int dx, int dy) {
        for (int i = 0; i < winLength; i++) {
            if (field[startX + i * dx][startY + i * dy] != dot) {
                return false;
            }
        }
        return true;
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
