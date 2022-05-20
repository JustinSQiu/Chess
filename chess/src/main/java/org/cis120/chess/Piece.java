package org.cis120.chess;

import java.util.ArrayList;

public class Piece {
    private boolean white;

    public Piece(boolean white) {
        this.white = white;
    }

    public boolean canMove(
            int x1, int y1, int x2, int y2, Piece[][] boardState, boolean curPlayer
    ) {
        return false;
    }

    public boolean isWhite() {
        return white;
    }

    public ArrayList<int[]> availableMoves(int x, int y, Piece[][] boardState, boolean curPlayer) {
        ArrayList<int[]> ret = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (canMove(x, y, i, j, boardState, curPlayer)) {
                    int[] cur = new int[2];
                    cur[0] = i;
                    cur[1] = j;
                    ret.add(cur);
                }
            }
        }
        return ret;
    }

    public boolean hasMoved() {
        return false;
    }

    public void setHasMoved(boolean hasMoved) {
    }

    public String getPieceString() {
        try {
            if (this instanceof Knight) {
                if (this.isWhite()) {
                    return "Nw";
                } else {
                    return "Nb";
                }
            } else if (this instanceof Rook) {
                if (this.isWhite()) {
                    return "Rw";
                } else {
                    return "Rb";
                }
            } else if (this instanceof Bishop) {
                if (this.isWhite()) {
                    return "Bw";
                } else {
                    return "Bb";
                }
            } else if (this instanceof Queen) {
                if (this.isWhite()) {
                    return "Qw";
                } else {
                    return "Qb";
                }
            } else if (this instanceof King) {
                if (this.isWhite()) {
                    return "Kw";
                } else {
                    return "Kb";
                }
            } else if (this instanceof Pawn) {
                if (this.isWhite()) {
                    return "Pw";
                } else {
                    return "Pb";
                }
            }
        } catch (NullPointerException e) {
            return null;
        }
        return null;
    }

}
