package org.cis120.chess;

public class Board {
    private Piece[][] boardState = new Piece[8][8];
    private String promotion;
    private boolean needPromotion;

    public Board() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardState[i][j] = null;
            }
        }
        promotion = null;
    }

    public Board(Piece[][] boardState) {
        this.boardState = boardState;
        this.promotion = null;
    }

    public Piece[][] copyBoardState() {
        Piece[][] ret = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardState[i][j] != null) {
                    String temp = boardState[i][j].getPieceString();
                    switch (temp) {
                        case "Pw":
                            ret[i][j] = new Pawn(true);
                            ((Pawn) ret[i][j])
                                    .setEnPassant(((Pawn) boardState[i][j]).getEnPassant());
                            break;
                        case "Pb":
                            ret[i][j] = new Pawn(false);
                            ((Pawn) ret[i][j])
                                    .setEnPassant(((Pawn) boardState[i][j]).getEnPassant());
                            break;
                        case "Nw":
                            ret[i][j] = new Knight(true);
                            break;
                        case "Nb":
                            ret[i][j] = new Knight(false);
                            break;
                        case "Bw":
                            ret[i][j] = new Bishop(true);
                            break;
                        case "Bb":
                            ret[i][j] = new Bishop(false);
                            break;
                        case "Rw":
                            ret[i][j] = new Rook(true);
                            break;
                        case "Rb":
                            ret[i][j] = new Rook(false);
                            break;
                        case "Kw":
                            ret[i][j] = new King(true);
                            break;
                        case "Kb":
                            ret[i][j] = new King(false);
                            break;
                        case "Qw":
                            ret[i][j] = new Queen(true);
                            break;
                        case "Qb":
                            ret[i][j] = new Queen(false);
                            break;
                        default:
                            break;
                    }
                    ret[i][j].setHasMoved(boardState[i][j].hasMoved());
                }
            }
        }
        return ret;
    }

    public void initializeBoard() {
        Piece knightB1 = new Knight(true);
        Piece knightG1 = new Knight(true);
        Piece bishopC1 = new Bishop(true);
        Piece bishopF1 = new Bishop(true);
        Piece rookA1 = new Rook(true);
        Piece rookH1 = new Rook(true);
        Piece queenD1 = new Queen(true);
        Piece kingA1 = new King(true);
        boardState[0][1] = knightB1;
        boardState[0][6] = knightG1;
        boardState[0][0] = rookA1;
        boardState[0][7] = rookH1;
        boardState[0][2] = bishopC1;
        boardState[0][5] = bishopF1;
        boardState[0][3] = queenD1;
        boardState[0][4] = kingA1;
        for (int i = 0; i < 8; i++) {
            Piece pawnI2 = new Pawn(true);
            boardState[1][i] = pawnI2;
        }
        Piece knightB8 = new Knight(false);
        Piece knightG8 = new Knight(false);
        Piece bishopC8 = new Bishop(false);
        Piece bishopF8 = new Bishop(false);
        Piece rookA8 = new Rook(false);
        Piece rookH8 = new Rook(false);
        Piece queenD8 = new Queen(false);
        Piece kingE8 = new King(false);
        boardState[7][1] = knightB8;
        boardState[7][6] = knightG8;
        boardState[7][0] = rookA8;
        boardState[7][7] = rookH8;
        boardState[7][2] = bishopC8;
        boardState[7][5] = bishopF8;
        boardState[7][3] = queenD8;
        boardState[7][4] = kingE8;
        for (int i = 0; i < 8; i++) {
            Piece pawnI7 = new Pawn(false);
            boardState[6][i] = pawnI7;
        }
    }

    // Note: I know this method breaks encapsulation. It is only used for testing and is
    // never called in the actual code.
    public Piece[][] getBoardState() {
        return boardState;
    }

    public Piece getPiece(int x, int y) {
        return boardState[x][y];
    }

    public boolean move(int x1, int y1, int x2, int y2, boolean curPlayer, boolean testing) {
        needPromotion = false;
        if (boardState[x1][y1] == null || boardState[x1][y1].isWhite() != curPlayer) {
            if (!testing) {
                System.out.println("Invalid Move; Piece Not Selected");
            }
            return false;
        }
        // Castling
        if (curPlayer) {
            if (!whiteUnderCheck(boardState) && x1 == 0 && y1 == 4 && x2 == 0 && y2 == 7
                    && whiteCanCastleKing()) {
                Piece[][] tempBoardState = copyBoardState();
                if (whiteUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot Castle Out of Check");
                    }
                    return false;
                }
                tempBoardState[0][5] = (tempBoardState[x1][y1]);
                tempBoardState[x1][y1] = (null);
                if (whiteUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot Castle Out of Check");
                    }
                    return false;
                }
                tempBoardState[0][6] = (tempBoardState[0][5]);
                tempBoardState[x1][y1] = (null);
                if (whiteUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot Castle Out of Check");
                    }
                    return false;
                }
                if (!testing) {
                    boardState[x1][y1].setHasMoved(true);
                    boardState[x2][y2].setHasMoved(true);
                }
                boardState[x2][y2 - 1] = (boardState[x1][y1]);
                boardState[x1][y1 + 1] = (boardState[x2][y2]);
                boardState[x1][y1] = (null);
                boardState[x2][y2] = (null);
                return true;
            } else if (!whiteUnderCheck(boardState) && whiteCanCastleQueen() && x1 == 0 && y1 == 4
                    && x2 == 0 && y2 == 0) {
                Piece[][] tempBoardState = copyBoardState();
                if (whiteUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot Castle Out of Check");
                    }
                    return false;
                }
                tempBoardState[0][3] = (tempBoardState[x1][y1]);
                tempBoardState[x1][y1] = (null);
                if (whiteUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot Castle Out of Check");
                    }
                    return false;
                }
                tempBoardState[0][2] = (tempBoardState[0][3]);
                tempBoardState[x1][y1] = (null);
                if (whiteUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot Castle Out of Check");
                    }
                    return false;
                }
                if (!testing) {
                    boardState[x1][y1].setHasMoved(true);
                    boardState[x2][y2].setHasMoved(true);
                }
                boardState[x2][y2 + 2] = (boardState[x1][y1]);
                boardState[x1][y1 - 1] = (boardState[x2][y2]);
                boardState[x1][y1] = (null);
                boardState[x2][y2] = (null);
                return true;
            }
        } else {
            if (!blackUnderCheck(boardState) && blackCanCastleKing() && x1 == 7 && y1 == 4
                    && x2 == 7 && y2 == 7) {
                Piece[][] tempBoardState = copyBoardState();
                if (blackUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot Castle Out of Check");
                    }
                    return false;
                }
                tempBoardState[7][5] = (tempBoardState[x1][y1]);
                tempBoardState[x1][y1] = (null);
                if (blackUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot Castle Out of Check");
                    }
                    return false;
                }
                tempBoardState[7][6] = (tempBoardState[7][5]);
                tempBoardState[x1][y1] = (null);
                if (blackUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot Castle Out of Check");
                    }
                    return false;
                }
                if (!testing) {
                    boardState[x1][y1].setHasMoved(true);
                    boardState[x2][y2].setHasMoved(true);
                }
                boardState[x2][y2 - 1] = (boardState[x1][y1]);
                boardState[x1][y1 + 1] = (boardState[x2][y2]);
                boardState[x1][y1] = (null);
                boardState[x2][y2] = (null);
                return true;
            } else if (!blackUnderCheck(boardState) && blackCanCastleQueen() && x1 == 7 && y1 == 4
                    && x2 == 7 && y2 == 0) {
                Piece[][] tempBoardState = copyBoardState();
                if (blackUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot Castle Out of Check");
                    }
                    return false;
                }
                tempBoardState[7][3] = (tempBoardState[x1][y1]);
                tempBoardState[x1][y1] = (null);
                if (blackUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot Castle Out of Check");
                    }
                    return false;
                }
                tempBoardState[7][2] = (tempBoardState[7][3]);
                tempBoardState[x1][y1] = (null);
                if (blackUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot Castle Out of Check");
                    }
                    return false;
                }
                if (!testing) {
                    boardState[x1][y1].setHasMoved(true);
                    boardState[x2][y2].setHasMoved(true);
                }
                boardState[x2][y2 + 2] = (boardState[x1][y1]);
                boardState[x1][y1 - 1] = (boardState[x2][y2]);
                boardState[x1][y1] = (null);
                boardState[x2][y2] = (null);
                return true;
            }
        }

        // En passant
        if (curPlayer) {
            if (x1 == 4 && Math.abs(y2 - y1) == 1 && x2 == 5 && boardState[x1][y1] instanceof Pawn
                    &&
                    boardState[x1][y2] instanceof Pawn &&
                    ((Pawn) boardState[x1][y2]).getEnPassant()) {
                Piece[][] tempBoardState = copyBoardState();
                tempBoardState[x2][y2] = (tempBoardState[x1][y1]);
                tempBoardState[x1][y2] = (null);
                tempBoardState[x1][y1] = (null);
                if (whiteUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot En Passant Into Check");
                    }
                    return false;
                }
                boardState[x2][y2] = (boardState[x1][y1]);
                boardState[x1][y2] = (null);
                boardState[x1][y1] = (null);
                return true;
            }
        } else {
            if (x1 == 3 && Math.abs(y2 - y1) == 1 && x2 == 2 && boardState[x1][y1] instanceof Pawn
                    &&
                    boardState[x1][y2] instanceof Pawn &&
                    ((Pawn) boardState[x1][y2]).getEnPassant()) {
                Piece[][] tempBoardState = copyBoardState();
                tempBoardState[x2][y2] = (tempBoardState[x1][y1]);
                tempBoardState[x1][y2] = (null);
                tempBoardState[x1][y1] = (null);
                if (blackUnderCheck(tempBoardState)) {
                    if (!testing) {
                        System.out.println("Invalid Move; Cannot En Passant Into Check");
                    }
                    return false;
                }
                boardState[x2][y2] = (boardState[x1][y1]);
                boardState[x1][y2] = (null);
                boardState[x1][y1] = (null);
                return true;
            }
        }

        if (!boardState[x1][y1].canMove(x1, y1, x2, y2, boardState, curPlayer)) {
            if (!testing) {
                System.out.println("Invalid Move; Selected Piece Cannot Move There");
            }
            return false;
        }
        Piece[][] tempBoardState = copyBoardState();
        tempBoardState[x2][y2] = (tempBoardState[x1][y1]);
        tempBoardState[x1][y1] = (null);
        if (curPlayer) {
            if (whiteUnderCheck(tempBoardState)) {
                if (!testing) {
                    System.out.println("Invalid Move; White In Check");
                }
                return false;
            }
        } else {
            if (blackUnderCheck(tempBoardState)) {
                if (!testing) {
                    System.out.println("Invalid Move; Black In Check");
                }
                return false;
            }
        }
        if (!testing) {
            updateEnPassant();
            boardState[x1][y1].setHasMoved(true);
            if (curPlayer && boardState[x1][y1] instanceof Pawn && x1 == 1 && x2 == 3 && y1 == y2) {
                ((Pawn) boardState[x1][y1]).setEnPassant(true);
            } else if (!curPlayer && boardState[x1][y1] instanceof Pawn && x1 == 6 && x2 == 4
                    && y1 == y2) {
                ((Pawn) boardState[x1][y1]).setEnPassant(true);
            }
        }
        boardState[x2][y2] = (boardState[x1][y1]);
        boardState[x1][y1] = (null);

        // Promotion
        if (curPlayer && boardState[x2][y2] instanceof Pawn && x2 == 7) {
            needPromotion = true;
        }
        if (!curPlayer && boardState[x2][y2] instanceof Pawn && x2 == 0) {
            needPromotion = true;
        }
        return true;
    }

    // Castling
    public boolean whiteCanCastleKing() {
        Piece a = boardState[0][4];
        Piece c = boardState[0][5];
        Piece d = boardState[0][6];
        Piece b = boardState[0][7];
        if (a != null && a.isWhite() && a instanceof King && !a.hasMoved() &&
                b != null && b.isWhite() && b instanceof Rook && !b.hasMoved() &&
                c == null && d == null) {
            return true;
        }
        return false;
    }

    public boolean whiteCanCastleQueen() {
        Piece a = boardState[0][4];
        Piece c = boardState[0][1];
        Piece d = boardState[0][2];
        Piece e = boardState[0][3];
        Piece b = boardState[0][0];
        if (a != null && a.isWhite() && a instanceof King && !a.hasMoved() &&
                b != null && b.isWhite() && b instanceof Rook && !b.hasMoved() &&
                c == null && d == null && e == null) {
            return true;
        }
        return false;
    }

    public boolean blackCanCastleKing() {
        Piece a = boardState[7][4];
        Piece c = boardState[7][5];
        Piece d = boardState[7][6];
        Piece b = boardState[7][7];
        if (a != null && !a.isWhite() && a instanceof King && !a.hasMoved() &&
                b != null && !b.isWhite() && b instanceof Rook && !b.hasMoved() &&
                c == null && d == null) {
            return true;
        }
        return false;
    }

    public boolean blackCanCastleQueen() {
        Piece a = boardState[7][4];
        Piece c = boardState[7][1];
        Piece d = boardState[7][2];
        Piece e = boardState[7][3];
        Piece b = boardState[7][0];
        if (a != null && !a.isWhite() && a instanceof King && !a.hasMoved() &&
                b != null && !b.isWhite() && b instanceof Rook && !b.hasMoved() &&
                c == null && d == null && e == null) {
            return true;
        }
        return false;
    }

    // Checking
    public int[] whiteKingLocation(Piece[][] tempBoardState) {
        int[] ret = new int[2];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tempBoardState[i][j] != null && tempBoardState[i][j].isWhite() &&
                        tempBoardState[i][j] instanceof King) {
                    ret[0] = i;
                    ret[1] = j;
                    return ret;
                }
            }
        }
        return null;
    }

    public int[] blackKingLocation(Piece[][] tempBoardState) {
        int[] ret = new int[2];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tempBoardState[i][j] != null && !tempBoardState[i][j].isWhite()
                        && tempBoardState[i][j] instanceof King) {
                    ret[0] = i;
                    ret[1] = j;
                    return ret;
                }
            }
        }
        return null;
    }

    public boolean whiteUnderCheck(Piece[][] tempBoardState) {
        int x = whiteKingLocation(tempBoardState)[0];
        int y = whiteKingLocation(tempBoardState)[1];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tempBoardState[i][j] != null && !tempBoardState[i][j].isWhite() &&
                        tempBoardState[i][j].canMove(i, j, x, y, tempBoardState, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean blackUnderCheck(Piece[][] tempBoardState) {
        int x = blackKingLocation(tempBoardState)[0];
        int y = blackKingLocation(tempBoardState)[1];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tempBoardState[i][j] != null && tempBoardState[i][j].isWhite() &&
                        tempBoardState[i][j].canMove(i, j, x, y, tempBoardState, true)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void printBoard() {
        System.out.println("-----------------------------------------");
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                String temp = "  ";
                if (boardState[i][j] != null && boardState[i][j].getPieceString() != null) {
                    temp = boardState[i][j].getPieceString();
                }
                System.out.print("| " + temp + " ");
            }
            System.out.println("|");
            System.out.println("-----------------------------------------");
        }
    }

    public void changePromotion(String promotion) {
        this.promotion = promotion;
    }

    public boolean needsPromotion() {
        return needPromotion;
    }

    public void setNeedsPromotion(boolean needsPromotion) {
        this.needPromotion = needsPromotion;
    }

    public void promotion() {
        for (int i = 0; i < 8; i++) {
            if (boardState[7][i] instanceof Pawn && promotion != null) {
                switch (promotion) {
                    case "Queen":
                        boardState[7][i] = (new Queen(true));
                        break;
                    case "Rook":
                        boardState[7][i] = (new Rook(true));
                        break;
                    case "Bishop":
                        boardState[7][i] = (new Bishop(true));
                        break;
                    case "Knight":
                        boardState[7][i] = (new Knight(true));
                        break;
                    default:
                        break;
                }
            } else if (boardState[7][i] instanceof Pawn && promotion == null) {
                // Forces promotion to Queen if user doesn't select a promotion
                // (just like lichess.org when you pre-move a promotion it becomes
                // a Queen)
                boardState[7][i] = (new Queen(true));
                break;
            }
        }
        for (int i = 0; i < 8; i++) {
            if (boardState[0][i] instanceof Pawn && promotion != null) {
                switch (promotion) {
                    case "Queen":
                        boardState[0][i] = (new Queen(false));
                        break;
                    case "Rook":
                        boardState[0][i] = (new Rook(false));
                        break;
                    case "Bishop":
                        boardState[0][i] = (new Bishop(false));
                        break;
                    case "Knight":
                        boardState[0][i] = (new Knight(false));
                        break;
                    default:
                        break;
                }
            } else if (boardState[0][i] instanceof Pawn && promotion == null) {
                // Forces promotion to Queen if user doesn't select a promotion
                // (just like lichess.org when you pre-move a promotion it becomes
                // a Queen)
                boardState[0][i] = (new Queen(false));
                break;
            }
        }
    }

    public void updateEnPassant() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardState[i][j] instanceof Pawn) {
                    ((Pawn) boardState[i][j]).setEnPassant(false);
                }
            }
        }
    }

    public boolean noLegalMoves(boolean curPlayer) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    for (int l = 0; l < 8; l++) {
                        Board temp = new Board(this.copyBoardState());
                        if (temp.move(i, j, k, l, curPlayer, true)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

}
