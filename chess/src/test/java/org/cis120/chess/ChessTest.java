package org.cis120.chess;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class ChessTest {

    @Test
    public void testSetup() {
        Board chessBoard = new Board();
        chessBoard.initializeBoard();
        Piece[][] boardState = chessBoard.getBoardState();
        assertTrue(boardState[0][0] instanceof Rook && boardState[0][0].isWhite());
        assertTrue(boardState[0][1] instanceof Knight && boardState[0][1].isWhite());
        assertTrue(boardState[0][2] instanceof Bishop && boardState[0][2].isWhite());
        assertTrue(boardState[0][3] instanceof Queen && boardState[0][3].isWhite());
        assertTrue(boardState[0][4] instanceof King && boardState[0][4].isWhite());
        assertTrue(boardState[0][5] instanceof Bishop && boardState[0][5].isWhite());
        assertTrue(boardState[0][6] instanceof Knight && boardState[0][6].isWhite());
        assertTrue(boardState[0][7] instanceof Rook && boardState[0][7].isWhite());
        for (int i = 0; i < 8; i++) {
            assertTrue(boardState[1][i] instanceof Pawn && boardState[1][i].isWhite());
        }
        assertTrue(boardState[7][0] instanceof Rook && !boardState[7][0].isWhite());
        assertTrue(boardState[7][1] instanceof Knight && !boardState[7][1].isWhite());
        assertTrue(boardState[7][2] instanceof Bishop && !boardState[7][2].isWhite());
        assertTrue(boardState[7][3] instanceof Queen && !boardState[7][3].isWhite());
        assertTrue(boardState[7][4] instanceof King && !boardState[7][4].isWhite());
        assertTrue(boardState[7][5] instanceof Bishop && !boardState[7][5].isWhite());
        assertTrue(boardState[7][6] instanceof Knight && !boardState[7][6].isWhite());
        assertTrue(boardState[7][7] instanceof Rook && !boardState[7][7].isWhite());
        for (int i = 0; i < 8; i++) {
            assertTrue(boardState[6][i] instanceof Pawn && !boardState[6][i].isWhite());
        }
    }

    @Test
    public void testAlternatePlayers() {
        Chess a = new Chess();
        a.getBoard().initializeBoard();
        // starts as white
        assertTrue(a.getCurrentPlayer());
        // invalid moves
        a.playTurn(100, 100);
        assertTrue(a.getCurrentPlayer());
        a.playTurn(4, 4);
        assertTrue(a.getCurrentPlayer());
        a.playTurn(0, 6);
        a.playTurn(100, 100);
        // valid move
        a.playTurn(0, 6);
        a.playTurn(0, 4);
        a.printGameState();
        assertFalse(a.getCurrentPlayer());
    }

    @Test
    public void testWrongPlayerMove() {
        Chess a = new Chess();
        a.getBoard().initializeBoard();
        assertTrue(a.getCurrentPlayer());
        assertFalse(a.playTurn(7, 0));
        assertTrue(a.getCurrentPlayer());
    }

    @Test
    public void testMoveOutsideBoard() {
        Chess a = new Chess();
        a.getBoard().initializeBoard();
        assertTrue(a.playTurn(0, 6));
        assertFalse(a.playTurn(100, 100));
        assertTrue(a.getCurrentPlayer());
        assertTrue(a.getBoard().getBoardState()[0][6] instanceof Knight);
    }

    @Test
    public void testCastleSuccess() {
        Board chessBoard = new Board();
        chessBoard.initializeBoard();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[0][5] = null;
        boardState[0][6] = null;
        assertTrue(chessBoard.whiteCanCastleKing());
        assertFalse(chessBoard.blackCanCastleKing());
        assertFalse(chessBoard.whiteCanCastleQueen());
        assertFalse(chessBoard.blackCanCastleQueen());
        assertFalse(boardState[0][4].hasMoved());
        assertFalse(boardState[0][7].hasMoved());
        assertTrue(chessBoard.move(0, 4, 0, 7, true, false));
        assertTrue(boardState[0][5] instanceof Rook && boardState[0][6] instanceof King);
        assertTrue(boardState[0][5].hasMoved());
        assertTrue(boardState[0][6].hasMoved());
    }

    @Test
    public void testCastleFail() {
        Board chessBoard = new Board();
        chessBoard.initializeBoard();
        Piece[][] boardState = chessBoard.getBoardState();
        // can't castle through pieces
        boardState[0][5] = null;
        assertFalse(chessBoard.whiteCanCastleKing());
        assertFalse(boardState[0][4].hasMoved());
        assertFalse(boardState[0][7].hasMoved());
        assertFalse(chessBoard.move(0, 4, 0, 7, true, false));
        assertFalse(boardState[0][5] instanceof Rook && boardState[0][6] instanceof King);
        assertFalse(boardState[0][4].hasMoved());
        assertFalse(boardState[0][7].hasMoved());
        // can't castle after having moved
        boardState[0][6] = null;
        boardState[0][4].setHasMoved(true);
        assertFalse(chessBoard.move(0, 4, 0, 7, true, false));
        assertTrue(boardState[0][4].hasMoved());
        assertFalse(boardState[0][7].hasMoved());
        // can't castle out of check
        boardState[0][4].setHasMoved(false);
        boardState[1][4] = new Queen(false);
        assertFalse(chessBoard.move(0, 4, 0, 7, true, false));
        // can't castle through check
        boardState[1][4] = new Bishop(false);
        assertFalse(chessBoard.move(0, 4, 0, 7, true, false));
    }

    @Test
    public void testEnPassantSuccess() {
        Board chessBoard = new Board();
        chessBoard.initializeBoard();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[3][4] = new Pawn(false);
        assertTrue(chessBoard.move(1, 3, 3, 3, true, false));
        assertTrue(chessBoard.move(3, 4, 2, 3, false, false));
        assertTrue(
                boardState[3][4] == null && boardState[3][3] == null
                        && boardState[2][3] instanceof Pawn
        );
    }

    @Test
    public void testEnPassantFail() {
        Board chessBoard = new Board();
        chessBoard.initializeBoard();
        Piece[][] boardState = chessBoard.getBoardState();
        // can't en passant after move
        boardState[3][4] = new Pawn(false);
        assertTrue(chessBoard.move(1, 3, 2, 3, true, false));
        assertTrue(chessBoard.move(2, 3, 3, 3, true, false));
        assertFalse(chessBoard.move(3, 4, 2, 3, false, false));
        assertFalse(
                boardState[3][4] == null && boardState[3][3] == null
                        && boardState[2][3] instanceof Pawn
        );
    }

    @Test
    public void testRegularCaptureDifferentColor() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[5][5] = new King(true);
        boardState[3][3] = new King(false);
        boardState[0][0] = new Rook(true);
        boardState[0][7] = new Rook(false);
        assertTrue(chessBoard.move(0, 0, 0, 7, true, false));
        assertTrue(
                boardState[0][0] == null && boardState[0][7] instanceof Rook &&
                        boardState[0][7].isWhite()
        );
    }

    @Test
    public void testRegularCaptureSameColor() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[5][5] = new King(true);
        boardState[3][3] = new King(false);
        boardState[0][0] = new Rook(true);
        boardState[0][7] = new Rook(true);
        assertFalse(chessBoard.move(0, 0, 0, 7, true, false));
        assertFalse(
                boardState[0][0] == null && boardState[0][7] instanceof Rook &&
                        boardState[0][7].isWhite()
        );
    }

    @Test
    public void testMoveNullPiece() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        chessBoard.initializeBoard();
        assertFalse(chessBoard.move(3, 3, 4, 4, true, false));
    }

    @Test
    public void testMoveKnight() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[4][4] = new Knight(true);
        List<int[]> test = (boardState[4][4].availableMoves(4, 4, boardState, true));
        List<int[]> comp = new ArrayList<>();
        int[] a = { 2, 3 };
        int[] b = { 2, 5 };
        int[] c = { 3, 2 };
        int[] d = { 3, 6 };
        int[] e = { 5, 2 };
        int[] f = { 5, 6 };
        int[] g = { 6, 3 };
        int[] h = { 6, 5 };
        comp.add(a);
        comp.add(b);
        comp.add(c);
        comp.add(d);
        comp.add(e);
        comp.add(f);
        comp.add(g);
        comp.add(h);
        Iterator<int[]> compIter = comp.iterator();
        for (int[] z : test) {
            int[] tempNext = compIter.next();
            assertEquals(z[0], tempNext[0]);
            assertEquals(z[1], tempNext[1]);
        }
    }

    @Test
    public void testMoveRook() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[4][4] = new Rook(true);
        List<int[]> test = (boardState[4][4].availableMoves(4, 4, boardState, true));
        List<int[]> comp = new ArrayList<>();
        int[] a = { 0, 4 };
        int[] b = { 1, 4 };
        int[] c = { 2, 4 };
        int[] d = { 3, 4 };
        int[] e = { 4, 0 };
        int[] f = { 4, 1 };
        int[] g = { 4, 2 };
        int[] h = { 4, 3 };
        int[] i = { 4, 5 };
        int[] j = { 4, 6 };
        int[] k = { 4, 7 };
        int[] l = { 5, 4 };
        int[] m = { 6, 4 };
        int[] n = { 7, 4 };
        comp.add(a);
        comp.add(b);
        comp.add(c);
        comp.add(d);
        comp.add(e);
        comp.add(f);
        comp.add(g);
        comp.add(h);
        comp.add(i);
        comp.add(j);
        comp.add(k);
        comp.add(l);
        comp.add(m);
        comp.add(n);
        Iterator<int[]> compIter = comp.iterator();
        for (int[] z : test) {
            int[] tempNext = compIter.next();
            assertEquals(z[0], tempNext[0]);
            assertEquals(z[1], tempNext[1]);
        }
    }

    @Test
    public void testMoveBishop() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[4][4] = new Bishop(true);
        List<int[]> test = (boardState[4][4].availableMoves(4, 4, boardState, true));
        List<int[]> comp = new ArrayList<>();
        for (int[] tester : test) {
            System.out.println(tester[0] + " " + tester[1]);
        }
        int[] a = { 0, 0 };
        int[] b = { 1, 1 };
        int[] c = { 1, 7 };
        int[] d = { 2, 2 };
        int[] e = { 2, 6 };
        int[] f = { 3, 3 };
        int[] g = { 3, 5 };
        int[] h = { 5, 3 };
        int[] i = { 5, 5 };
        int[] j = { 6, 2 };
        int[] k = { 6, 6 };
        int[] l = { 7, 1 };
        int[] m = { 7, 7 };
        comp.add(a);
        comp.add(b);
        comp.add(c);
        comp.add(d);
        comp.add(e);
        comp.add(f);
        comp.add(g);
        comp.add(h);
        comp.add(i);
        comp.add(j);
        comp.add(k);
        comp.add(l);
        comp.add(m);
        Iterator<int[]> compIter = comp.iterator();
        for (int[] z : test) {
            int[] tempNext = compIter.next();
            assertEquals(z[0], tempNext[0]);
            assertEquals(z[1], tempNext[1]);
        }
    }

    @Test
    public void testMoveQueen() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[4][4] = new Queen(true);
        List<int[]> test = (boardState[4][4].availableMoves(4, 4, boardState, true));
        List<int[]> comp = new ArrayList<>();
        for (int[] tester : test) {
            System.out.println(tester[0] + " " + tester[1]);
        }
        int[] a = { 0, 0 };
        int[] b = { 0, 4 };
        int[] c = { 1, 1 };
        int[] d = { 1, 4 };
        int[] e = { 1, 7 };
        int[] f = { 2, 2 };
        int[] g = { 2, 4 };
        int[] h = { 2, 6 };
        int[] i = { 3, 3 };
        int[] j = { 3, 4 };
        int[] k = { 3, 5 };
        int[] l = { 4, 0 };
        int[] m = { 4, 1 };
        int[] n = { 4, 2 };
        int[] o = { 4, 3 };
        int[] p = { 4, 5 };
        int[] q = { 4, 6 };
        int[] r = { 4, 7 };
        int[] s = { 5, 3 };
        int[] t = { 5, 4 };
        int[] u = { 5, 5 };
        int[] v = { 6, 2 };
        int[] w = { 6, 4 };
        int[] x = { 6, 6 };
        int[] y = { 7, 1 };
        int[] z = { 7, 4 };
        int[] aa = { 7, 7 };
        comp.add(a);
        comp.add(b);
        comp.add(c);
        comp.add(d);
        comp.add(e);
        comp.add(f);
        comp.add(g);
        comp.add(h);
        comp.add(i);
        comp.add(j);
        comp.add(k);
        comp.add(l);
        comp.add(m);
        comp.add(n);
        comp.add(o);
        comp.add(p);
        comp.add(q);
        comp.add(r);
        comp.add(s);
        comp.add(t);
        comp.add(u);
        comp.add(v);
        comp.add(w);
        comp.add(x);
        comp.add(y);
        comp.add(z);
        comp.add(aa);
        Iterator<int[]> compIter = comp.iterator();
        for (int[] zz : test) {
            int[] tempNext = compIter.next();
            assertEquals(zz[0], tempNext[0]);
            assertEquals(zz[1], tempNext[1]);
        }
    }

    @Test
    public void testMoveKing() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[4][4] = new King(true);
        List<int[]> test = (boardState[4][4].availableMoves(4, 4, boardState, true));
        List<int[]> comp = new ArrayList<>();
        for (int[] tester : test) {
            System.out.println(tester[0] + " " + tester[1]);
        }
        int[] a = { 3, 3 };
        int[] b = { 3, 4 };
        int[] c = { 3, 5 };
        int[] d = { 4, 3 };
        int[] e = { 4, 5 };
        int[] f = { 5, 3 };
        int[] g = { 5, 4 };
        int[] h = { 5, 5 };
        comp.add(a);
        comp.add(b);
        comp.add(c);
        comp.add(d);
        comp.add(e);
        comp.add(f);
        comp.add(g);
        comp.add(h);
        Iterator<int[]> compIter = comp.iterator();
        for (int[] zz : test) {
            int[] tempNext = compIter.next();
            assertEquals(zz[0], tempNext[0]);
            assertEquals(zz[1], tempNext[1]);
        }
    }

    @Test
    public void testMovePawn() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[1][4] = new Pawn(true);
        List<int[]> test = (boardState[1][4].availableMoves(1, 4, boardState, true));
        List<int[]> comp = new ArrayList<>();
        for (int[] tester : test) {
            System.out.println(tester[0] + " " + tester[1]);
        }
        int[] a = { 2, 4 };
        int[] b = { 3, 4 };
        comp.add(a);
        comp.add(b);
        Iterator<int[]> compIter = comp.iterator();
        for (int[] zz : test) {
            int[] tempNext = compIter.next();
            assertEquals(zz[0], tempNext[0]);
            assertEquals(zz[1], tempNext[1]);
        }
    }

    @Test
    public void testCheckmate() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        chessBoard.initializeBoard();
        assertTrue(chessBoard.move(1, 5, 2, 5, true, false));
        assertTrue(chessBoard.move(6, 4, 5, 4, false, false));
        assertTrue(chessBoard.move(1, 6, 3, 6, true, false));
        assertTrue(chessBoard.move(7, 3, 3, 7, false, false));
        assertTrue(chessBoard.noLegalMoves(true) && chessBoard.whiteUnderCheck(boardState));
        assertFalse(chessBoard.noLegalMoves(false) && chessBoard.blackUnderCheck(boardState));
    }

    @Test
    public void testStalemate() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[0][0] = new King(true);
        boardState[7][7] = new King(false);
        boardState[1][7] = new Rook(false);
        boardState[7][1] = new Rook(false);
        assertTrue(chessBoard.noLegalMoves(true) && !chessBoard.whiteUnderCheck(boardState));
        assertFalse(chessBoard.noLegalMoves(false) && !chessBoard.blackUnderCheck(boardState));
    }

    @Test
    public void testCheckWithoutCheckmate() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[0][0] = new King(true);
        boardState[7][7] = new King(false);
        boardState[0][7] = new Rook(false);
        assertTrue(chessBoard.whiteUnderCheck(boardState));
        assertFalse(chessBoard.noLegalMoves(true));
    }

    @Test
    public void testCannotMoveIntoCheck() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[0][0] = new King(true);
        boardState[7][7] = new King(false);
        boardState[1][7] = new Rook(false);
        assertFalse(chessBoard.move(0, 0, 1, 0, true, false));
    }

    @Test
    public void testPromotionSuccess() {
        Board chessBoard = new Board();
        Piece[][] boardState = chessBoard.getBoardState();
        boardState[0][0] = new King(true);
        boardState[0][7] = new King(false);
        boardState[6][4] = new Pawn(true);
        assertTrue(chessBoard.move(6, 4, 7, 4, true, false));
        assertTrue(chessBoard.needsPromotion());
    }

}
