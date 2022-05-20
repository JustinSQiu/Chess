package org.cis120.chess;

public class Bishop extends Piece {
    public Bishop(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(
            int x1, int y1, int x2, int y2, Piece[][] boardState, boolean curPlayer
    ) {
        // general checks
        if (x1 < 0 || x1 > 7 || y1 < 0 || y1 > 7 || x2 < 0 || x2 > 7 || y2 < 0 || y2 > 7) {
            return false;
        } else if (curPlayer != isWhite()) {
            return false;
        }
        int diffX = (x1 - x2);
        int diffY = (y1 - y2);
        if (diffX == 0 && diffY == 0) {
            return false;
        } else if (boardState[x2][y2] != null && boardState[x2][y2].isWhite() == isWhite()) {
            return false;
        }
        // bishop specific checks
        if (Math.abs(diffX) != Math.abs(diffY)) {
            return false;
        }
        // checks up and right
        if (diffX < 0 && diffY < 0) {
            for (int i = x1 + 1, j = y1 + 1; i < x2 && j < y2; i++, j++) {
                if (boardState[i][j] != null) {
                    return false;
                }
            }
        } else if (diffX < 0 && diffY > 0) {
            for (int i = x1 + 1, j = y1 - 1; i < x2 && j > y2; i++, j--) {
                if (boardState[i][j] != null) {
                    return false;
                }
            }
        } else if (diffX > 0 && diffY < 0) {
            for (int i = x1 - 1, j = y1 + 1; i > x2 && j < y2; i--, j++) {
                if (boardState[i][j] != null) {
                    return false;
                }
            }
        } else if (diffX > 0 && diffY > 0) {
            for (int i = x1 - 1, j = y1 - 1; i > x2 && j > y2; i--, j--) {
                if (boardState[i][j] != null) {
                    return false;
                }
            }
        }
        return true;
    }

}
