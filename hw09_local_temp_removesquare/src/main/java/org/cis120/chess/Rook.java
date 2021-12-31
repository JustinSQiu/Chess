package org.cis120.chess;

public class Rook extends Piece {

    private boolean hasMoved;

    public Rook(boolean white) {
        super(white);
        hasMoved = false;
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
        }
        if (boardState[x2][y2] != null && boardState[x2][y2].isWhite() == isWhite()) {
            return false;
        }

        // rook specific checks
        if (diffX != 0 && diffY != 0) {
            return false;
        } else if (diffX == 0) {
            if (diffY > 0) {
                for (int i = y1 - 1; i > y2; i--) {
                    if (boardState[x2][i] != null) {
                        return false;
                    }
                }
            } else if (diffY < 0) {
                for (int i = y1 + 1; i < y2; i++) {
                    if (boardState[x2][i] != null) {
                        return false;
                    }
                }
            }
        } else if (diffY == 0) {
            if (diffX > 0) {
                for (int i = x1 - 1; i > x2; i--) {
                    if (boardState[i][y2] != null) {
                        return false;
                    }
                }
            } else if (diffX < 0) {
                for (int i = x1 + 1; i < x2; i++) {
                    if (boardState[i][y2] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

}
