package org.cis120.chess;

public class Main {
    public static void main(String[] args) {
        Board gameBoard = new Board();
        gameBoard.initializeBoard();
        gameBoard.printBoard();
        gameBoard.move(0, 1, 1, 3, true, false);
        gameBoard.move(0, 0, 5, 0, true, false);
        gameBoard.printBoard();
    }
}
