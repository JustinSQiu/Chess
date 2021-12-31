package org.cis120.chess;

public class Pawn extends Piece {

    private boolean enPassant;

    public Pawn(boolean white) {
        super(white);
        enPassant = false;
    }

    @Override
    public boolean canMove(
            int x1, int y1, int x2, int y2, Piece[][] boardState, boolean curPlayer
    ) {
        // general checks
        if (x1 < 0 || x1 > 7 || y1 < 0 || y1 > 7 || x2 < 0 || x2 > 7 || y2 < 0 || y2 > 7) {
            return false;
        } else if (isWhite() != curPlayer) {
            return false;
        }
        // checks for white
        if (this.isWhite()) {
            if (x1 == 1 && y1 == y2 && x2 == 3 && boardState[x2][y2] == null
                    && boardState[2][y1] == null) {
                return true;
            } else if (y1 == y2 && (x2 - x1) == 1 && boardState[x2][y2] == null) {
                return true;
            } else if (Math.abs(y1 - y2) == 1 && x2 - x1 == 1 &&
                    (boardState[x2][y2] != null &&
                            boardState[x2][y2].isWhite() != this.isWhite())) {
                return true;
            }
        } else {
            if (x1 == 6 && y1 == y2 && x2 == 4 && boardState[x2][y2] == null
                    && boardState[5][y1] == null) {
                return true;
            } else if (y1 == y2 && (-x2 + x1) == 1 && boardState[x2][y2] == null) {
                return true;
            } else if (Math.abs(y1 - y2) == 1 && -x2 + x1 == 1 &&
                    (boardState[x2][y2] != null &&
                            boardState[x2][y2].isWhite() != this.isWhite())) {
                return true;
            }
        }
        return false;
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    public boolean getEnPassant() {
        return enPassant;
    }
}
