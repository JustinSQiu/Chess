package org.cis120.chess;

import java.util.ArrayList;

public class Chess {

    private Board board;
    private boolean curPlayer;
    private boolean selecting;
    private int[] selectedPiece = new int[2];

    public Chess() {
        reset();
    }

    public boolean playTurnAgainstBot(int c, int r) {
        RandomBot randBot = new RandomBot();
        if (!selecting) {
            if (c > 8 || c < 0 || r > 8 || r < 0 ||
                    board.getPiece(7 - r, c) == null ||
                    board.getPiece(7 - r, c).isWhite() != curPlayer) {
                return false;
            }
            selectedPiece[0] = 7 - r;
            selectedPiece[1] = c;
            selecting = true;
        } else {
            if (board.move(selectedPiece[0], selectedPiece[1], 7 - r, c, curPlayer, false)) {
                curPlayer = !curPlayer;
            } else {
                return false;
            }
            selecting = false;
            randBot.randomMove(this);
            curPlayer = !curPlayer;
        }
        return true;
    }

    public boolean playTurn(int c, int r) {
        if (!selecting) {
            if (c > 8 || c < 0 || r > 8 || r < 0 ||
                    board.getPiece(7 - r, c) == null ||
                    board.getPiece(7 - r, c).isWhite() != curPlayer) {
                return false;
            }
            selectedPiece[0] = 7 - r;
            selectedPiece[1] = c;
            selecting = true;
            return true;
        } else {
            boolean temp = false;
            if (board.move(selectedPiece[0], selectedPiece[1], 7 - r, c, curPlayer, false)) {
                curPlayer = !curPlayer;
                temp = true;
            }
            selecting = false;
            return temp;
        }
    }

    public boolean isSelecting() {
        return selecting;
    }

    public ArrayList<int[]> getAvailableMovesSelectedPiece() {
        if (!isSelecting()) {
            return null;
        }
        int x = selectedPiece[0];
        int y = selectedPiece[1];
        ArrayList<int[]> ret = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Board temp = new Board(board.copyBoardState());
                if (temp.move(x, y, i, j, curPlayer, true)) {
                    int[] e = new int[2];
                    e[0] = i;
                    e[1] = j;
                    ret.add(e);
                }
            }
        }
        return ret;
    }

    public ArrayList<int[]> getAvailableMovesAnyPiece(int x, int y) {
        ArrayList<int[]> ret = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Board temp = new Board(board.copyBoardState());
                if (temp.move(x, y, i, j, curPlayer, true)) {
                    int[] e = new int[2];
                    e[0] = i;
                    e[1] = j;
                    ret.add(e);
                }
            }
        }
        return ret;
    }

    public boolean checkWinner() {
        return board.noLegalMoves(curPlayer);
    }

    public void printGameState() {
        board.printBoard();
    }

    public void reset() {
        board = new Board();
        board.initializeBoard();
        curPlayer = true;
        selecting = false;
    }

    public boolean getCurrentPlayer() {
        return curPlayer;
    }

    public Piece[][] getCBoardState() {
        return board.copyBoardState();
    }

    public Board getBoard() {
        return board;
    }

    public static void main(String[] args) {
        Board gameBoard = new Board();
        gameBoard.initializeBoard();
        gameBoard.printBoard();
        gameBoard.move(0, 1, 1, 3, true, false);
        gameBoard.move(0, 0, 5, 0, true, false);
        gameBoard.printBoard();
    }

}
