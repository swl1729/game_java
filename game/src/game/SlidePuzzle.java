package game;

import java.util.Random;

public class SlidePuzzle extends Game {
    private static final int MIN_WIDTH = 3;
    private static final int MAX_WIDTH = 10;
    private int boardWidth;
    private int boardSize;
    private int[][] board;

    private static final int SPACE_POS_VALUE = 0;
    private Position spacePos;

    private static final int SHUFFLE_ITERATION = 100000;

    // クリア判定
    private Boolean isGameClear;

    public SlidePuzzle(String name) {
        super(name);
        this.boardWidth = selectBoardWidth();
        this.boardSize = this.boardWidth * this.boardWidth;
        this.board = initBoard();
        shuffleBoard();
        this.isGameClear = false;
    }

    // boardWidthをユーザーが選択する
    private int selectBoardWidth() {
        int boardWidth = MIN_WIDTH;
        while (true) {
            System.out.printf("盤面幅を入力してください。\n");
            System.out.printf("※%dから%dの整数値\n", MIN_WIDTH, MAX_WIDTH);
            System.out.printf("盤面幅：");
            boardWidth = this.scanner.nextInt();

            // 不正な値ではないか確認
            if (boardWidth >= MIN_WIDTH && boardWidth <= MAX_WIDTH) {
                break;
            } else {
                printManyLineBreak();
                System.out.printf("%dは不正な値です。\n", boardWidth);
            }
        }
        return boardWidth;
    }

    // boardの初期化
    private int[][] initBoard() {
        int[][] board = new int[this.boardWidth][this.boardWidth];
        int boardIdx = 0;
        for (int ri = 0; ri < this.boardWidth; ri++) {
            for (int ci = 0; ci < this.boardWidth; ci++) {
                if (ri == this.boardWidth - 1 && ci == this.boardWidth - 1) {
                    board[ri][ci] = SPACE_POS_VALUE;
                    this.spacePos = new Position(ri, ci);
                } else {
                    board[ri][ci] = ++boardIdx;
                }
            }
        }
        return board;
    }

    // boardをシャッフルする
    private void shuffleBoard() {
        Random random = new Random();

        // 空白位置を適当に動かすことでboardをシャッフルする
        for (int ite = 0; ite < SHUFFLE_ITERATION; ite++) {
            int nextSpacePosRow = this.spacePos.getRowPos();
            int nextSpacePosCol = this.spacePos.getColPos();

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
            Position nextPos = new Position(nextSpacePosRow, nextSpacePosCol);
            swapSpacePos(nextPos);
        }
    }

    // 空白位置を入れ替える
    private Boolean swapSpacePos(Position nextPos) {
        int currentSpacePosRow = this.spacePos.getRowPos();
        int currentSpacePosCol = this.spacePos.getColPos();
        int nextSpacePosRow = nextPos.getRowPos();
        int nextSpacePosCol = nextPos.getColPos();

        // 動かした先が盤面内かを確認
        Boolean isInsidePos = (nextSpacePosRow >= 0 &&
                nextSpacePosRow < this.boardWidth &&
                nextSpacePosCol >= 0 &&
                nextSpacePosCol < this.boardWidth);

        // 動かした先と今の位置が隣り合っているかを確認
        Boolean isNextToPos = (Math.abs(nextSpacePosRow - currentSpacePosRow)
                + Math.abs(nextSpacePosCol - currentSpacePosCol) == 1);

        Boolean canSwap = isInsidePos && isNextToPos;
        if (canSwap) {
            // 空白位置の入れ替え
            int temp = this.board[nextSpacePosRow][nextSpacePosCol];
            this.board[nextSpacePosRow][nextSpacePosCol] = this.board[currentSpacePosRow][currentSpacePosCol];
            this.board[currentSpacePosRow][currentSpacePosCol] = temp;
            this.spacePos.setRowPos(nextSpacePosRow);
            this.spacePos.setColPos(nextSpacePosCol);
        }
        return canSwap;
    }

    // 動かしたい盤面の数字の座標を取得
    private Position getMovingPos(int movingNum) {
        Position movingPos = new Position(0, 0);
        for (int ri = 0; ri < this.boardWidth; ri++) {
            for (int ci = 0; ci < this.boardWidth; ci++) {
                if (this.board[ri][ci] == movingNum) {
                    movingPos.setRowPos(ri);
                    movingPos.setColPos(ci);
                    break;
                }
            }
        }
        return movingPos;
    }

    // クリア判定
    private Boolean judgeGameClear() {
        int boardIdx = 0;
        for (int ri = 0; ri < this.boardWidth; ri++) {
            for (int ci = 0; ci < this.boardWidth; ci++) {
                //右下の値は無視する（空白想定）
                if (ri == this.boardWidth - 1 && ci == this.boardWidth - 1) {
                    break;
                }
                //一致しなかった時点でfalseを返す
                if (board[ri][ci] != ++boardIdx) {
                    return false;
                }
            }
        }
        return true;
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
        while (true) {
            printBoard();

            System.out.printf("\n動かす数字を入力：");
            int movingNum = this.scanner.nextInt();

            // 盤面に含まれている値なのかを確認
            if (movingNum > 0 && movingNum <= this.boardSize - 1) {
                Position movingPos = getMovingPos(movingNum);
                swapSpacePos(movingPos);
            }

            // Todo:終了判定
            this.isGameClear = judgeGameClear();
            if (this.isGameClear) {
                break;
            }
        }
    }
}