package JavaCore;

import java.util.Random;
import java.util.Scanner;

public class Main {

    public static final char DOT_HUMAN = 'X';
    public static final char DOT_AI = 'O';
    public static final char DOT_EMPTY = '*';
    public static final Scanner scanner = new Scanner(System.in);
    public static final Random random = new Random();
    public static char[][] field;
    public static int fieldSizeX;
    public static int fieldSizeY;

    /**
     * Инициализация обьктов игры
     */
    static void initialize(){
        fieldSizeX = 3;
        fieldSizeY = 3;

        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Печать текущего состояния игрового поля
     */

    static void printField(){
        System.out.print("+");
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.println("-"+(i+1));
        }
        System.out.println("-");
        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print(x+1+"|");
            for (int y = 0; y < fieldSizeY; y++) {
                System.out.print(field[x][y]+ "|");
            }
            System.out.println();
        }

    }
    public static void main(String[] args) {
        new GameWindow();
    }
}