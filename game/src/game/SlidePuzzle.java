package game;

import java.util.Scanner;
import java.util.Random;

public class SlidePuzzle extends Game {
    private static final int MIN_WIDTH = 3;
    private static final int MAX_WIDTH = 10;
    private int boardWidth;
    private int boardSize;
    private int[][] board;

    private static final int SPACE_POS_VALUE = 0;
    private int spacePosRow;
    private int spacePosCol;

    private static final int SHUFFLE_ITERATION = 100000;

    public SlidePuzzle(String name) {
        super(name);
        this.boardWidth = selectBoardWidth();
        this.boardSize = this.boardWidth * this.boardWidth;
        this.board = initBoard(this.boardWidth);
        shuffleBoard(this.board, this.boardWidth);
    }

    // boardWidthをユーザーが選択する
    private int selectBoardWidth() {
        Scanner scanner = new Scanner(System.in);
        int boardWidth = MIN_WIDTH;
        while (true) {
            System.out.printf("盤面幅を入力してください。\n");
            System.out.printf("※%dから%dの整数値\n", MIN_WIDTH, MAX_WIDTH);
            System.out.printf("盤面幅：");
            boardWidth = scanner.nextInt();

            // 不正な値ではないか確認
            if (boardWidth >= MIN_WIDTH && boardWidth <= MAX_WIDTH) {
                break;
            } else {
                printManyLineBreak();
                System.out.printf("%dは不正な値です。\n", boardWidth);
            }
        }
        scanner.close();
        return boardWidth;
    }

    // boardの初期化
    private int[][] initBoard(int boardWidth) {
        int[][] board = new int[boardWidth][boardWidth];
        int boardIdx = 0;
        for (int ri = 0; ri < boardWidth; ri++) {
            for (int ci = 0; ci < boardWidth; ci++) {
                if (ri == boardWidth - 1 && ci == boardWidth - 1) {
                    board[ri][ci] = SPACE_POS_VALUE;
                    this.spacePosRow = ri;
                    this.spacePosCol = ci;
                } else {
                    board[ri][ci] = ++boardIdx;
                }
            }
        }
        return board;
    }

    // boardをシャッフルする
    private void shuffleBoard(int[][] board, int boardWidth) {
        Random random = new Random();

        // 空白位置を適当に動かすことでboardをシャッフルする
        for (int ite = 0; ite < SHUFFLE_ITERATION; ite++) {
            int nextSpacePosRow = this.spacePosRow;
            int nextSpacePosCol = this.spacePosCol;

            // 乱数によって空白位置を動かす方向を決める
            int randomInt = random.nextInt(4);
            switch (randomInt) {
                case 0:
                    nextSpacePosRow--;
                    break;
                case 1:
                    nextSpacePosRow++;
                    break;
                case 2:
                    nextSpacePosCol--;
                    break;
                case 3:
                    nextSpacePosCol++;
                    break;
            }
            swapSpacePos(board, boardWidth, nextSpacePosRow, nextSpacePosCol);
        }
    }

    private Boolean swapSpacePos(
            int[][] board,
            int boardWidth,
            int nextSpacePosRow,
            int nextSpacePosCol) {
        // 動かした先が不正な位置ではないかを確認
        Boolean isNormalPos = (nextSpacePosRow >= 0 &&
                nextSpacePosRow < boardWidth &&
                nextSpacePosCol >= 0 &&
                nextSpacePosCol < boardWidth);

        if (isNormalPos) {
            // 空白位置の入れ替え
            int temp = board[nextSpacePosRow][nextSpacePosCol];
            board[nextSpacePosRow][nextSpacePosCol] = board[this.spacePosRow][this.spacePosCol];
            board[this.spacePosRow][this.spacePosCol] = temp;
            this.spacePosRow = nextSpacePosRow;
            this.spacePosCol = nextSpacePosCol;
        }
        return isNormalPos;
    }

    private void printBoard() {
        printManyLineBreak();
        for (int ri = 0; ri < boardWidth; ri++) {
            for (int ci = 0; ci < boardWidth; ci++) {
                if (board[ri][ci] == SPACE_POS_VALUE) {
                    System.out.printf("    ", board[ri][ci]);
                } else {
                    System.out.printf("%4d", board[ri][ci]);
                }
            }
            System.out.printf("\n");
        }
    }

    public void printManyLineBreak() {
        System.out.printf("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public void play() {
        printBoard();
    }
}