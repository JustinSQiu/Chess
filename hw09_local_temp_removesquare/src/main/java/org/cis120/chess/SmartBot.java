package org.cis120.chess;

import java.util.ArrayList;
import java.util.Random;

public class SmartBot {
    public SmartBot() {
    }
    public int currentEvaluation(Chess chess) {
        int curEval = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (chess.getCBoardState()[i][j].getPieceString()) {
                    case "Pw":
                        curEval --;
                        break;
                    case "Pb":
                        curEval ++;
                        break;
                    case "Nw":
                    case "Bw":
                        curEval -= 3;
                        break;
                    case "Nb":
                    case "Bb":
                        curEval += 3;
                        break;
                    case "Rw":
                        curEval -= 5;
                        break;
                    case "Rb":
                        curEval += 5;
                        break;
                    case "Kw":
                        curEval -= 100;
                        break;
                    case "Kb":
                        curEval += 100;
                        break;
                    case "Qw":
                        curEval -= 9;
                        break;
                    case "Qb":
                        curEval += 9;
                        break;
                    default:
                        break;
                }
            }
        }
        return curEval;
    }
    public void smartMove(Chess chess) {
        ArrayList<int[]> allMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chess.getCBoardState()[i][j] != null && chess.getCBoardState()[i][j].isWhite() == chess.getCurrentPlayer()) {
                    for (int[] a: chess.getAvailableMovesAnyPiece(i, j)) {
                        int[] b = {i, j, a[0], a[1]};
                        allMoves.add(b);
                    }
                }
            }
        }
        Random rand = new Random();
        int[] randomMove = allMoves.get(rand.nextInt(allMoves.size()));
        chess.getBoard().move(randomMove[0], randomMove[1], randomMove[2], randomMove[3], chess.getCurrentPlayer(), false);
    }
}
