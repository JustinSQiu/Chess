package org.cis120.chess;

public class Knight extends Piece {

    public Knight(boolean white) {
        super(white);
    }

    @Override
    public boolean canMove(
            int x1, int y1, int x2, int y2, Piece[][] boardState, boolean curPlayer
    ) {
        return (curPlayer == this.isWhite()) && (0 <= x2 && 8 > x2 && 0 <= y2 && 8 > y2) &&
                (boardState[x2][y2] == null || boardState[x2][y2].isWhite() != this.isWhite())
                && ((Math.abs(x2 - x1) == 2) && (Math.abs(y2 - y1) == 1)
                        || ((Math.abs(x2 - x1) == 1) && (Math.abs(y2 - y1) == 2)));
    }

}
