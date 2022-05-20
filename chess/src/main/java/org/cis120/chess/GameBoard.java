package org.cis120.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Chess chess; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setFocusable(true);

        chess = new Chess(); // initializes model for the game
        status = statusInit; // initializes the status JLabel
//        JOptionPane.showInternalMessageDialog(
//                null,
//                "Chess is a classic game that has been played " +
//                        "for many centuries. Your goal is to checkmate " +
//                        "the opponent's king, meaning that you want to " +
//                        "maneuver your pieces in such a way that \n" +
//                        "makes the enemy king's capture inevitable. " +
//                        "Along the way, you will want to capture enemy " +
//                        "pieces, though you can not capture your own. " +
//                        "Each piece moves differently; the knight \n" +
//                        "moves in an L shape in a 2 by 1 grid , the " +
//                        "bishop moves along diagonals, the rook moves " +
//                        "in a straight line, the queen combines the " +
//                        "movement of the bishop and the rook, the \n" +
//                        "king can move anywhere in a one tile radius " +
//                        "and the pawn can move forward one square or " +
//                        "capture one square diagonally left or right. " +
//                        "There are also special moves in chess such \n" +
//                        "as En Passant, Castling, Pawn Promotion, and " +
//                        "pawns moving up two squares on their first " +
//                        "move, as well as special game rules such as " +
//                        "Stalemate that you may want to know more \n" +
//                        "about. Here is a link that explains it well: " +
//                        "https://en.wikipedia.org/wiki/Rules_of_chess " +
//                        "For this chess application, when you click on " +
//                        "a piece, green bubbles will show the places \n" +
//                        "you are allowed to move it to. Click again to " +
//                        "move the piece. Drag and drop is not supported. \n\n" +
//                        "Click \"OK\" to begin the game!",
//                "Game Rules", JOptionPane.INFORMATION_MESSAGE
//        );
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                chess.playTurn(p.x / 100, p.y / 100);
                if (chess.getBoard().needsPromotion()) {
                    String[] possibleValues = { "Queen", "Rook", "Bishop", "Knight" };

                    Object selectedValue = JOptionPane.showInputDialog(
                            null,
                            "What would you like to promote your pawn to?", "Choices:",
                            JOptionPane.INFORMATION_MESSAGE, null,
                            possibleValues, possibleValues[0]
                    );
                    chess.getBoard().changePromotion((String) selectedValue);
                    chess.getBoard().promotion();
                    chess.getBoard().setNeedsPromotion(false);
                    repaint();
                }
                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board

                if (status.getText().equals("Checkmate! White Wins!")) {
                    String[] options = { "Quit Game", "Reset Game" };
                    Object selectedValue = JOptionPane.showOptionDialog(
                            null,
                            "Checkmate! White Wins!", "Game End",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, options, options[0]
                    );
                    if ((int) selectedValue == 1) {
                        chess.reset();
                    } else if ((int) selectedValue == 0) {
                        System.exit(0);
                    }
                    repaint();
                } else if (status.getText().equals("Checkmate! Black Wins!")) {
                    String[] options = { "Quit Game", "Reset Game" };
                    Object selectedValue = JOptionPane.showOptionDialog(
                            null,
                            "Checkmate! Black Wins!", "Game End",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, options, options[0]
                    );
                    if ((int) selectedValue == 1) {
                        chess.reset();
                    } else if ((int) selectedValue == 0) {
                        System.exit(0);
                    }
                    repaint();
                } else if (status.getText().equals("Stalemate! It's a Tie!")) {
                    String[] options = { "Quit Game", "Reset Game" };
                    Object selectedValue = JOptionPane.showOptionDialog(
                            null,
                            "It's a Tie!", "Game End",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, options, options[0]
                    );
                    if ((int) selectedValue == 1) {
                        chess.reset();
                    } else if ((int) selectedValue == 0) {
                        System.exit(0);
                    }
                    repaint();
                }

            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        chess.reset();
        status.setText("White's turn");
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    private void updateStatus() {
        String text = "";
        if (chess.checkWinner() && !chess.getCurrentPlayer() &&
                chess.getBoard().blackUnderCheck(chess.getCBoardState())) {
            text = "Checkmate! White Wins!";
        } else if (chess.checkWinner() && chess.getCurrentPlayer() &&
                chess.getBoard().whiteUnderCheck(chess.getCBoardState())) {
            text = "Checkmate! Black Wins!";
        } else if (chess.checkWinner() && !chess.getCurrentPlayer() &&
                !chess.getBoard().blackUnderCheck(chess.getCBoardState())) {
            text = "Stalemate! It's a Tie!";
        } else if (chess.checkWinner() && chess.getCurrentPlayer() &&
                !chess.getBoard().whiteUnderCheck(chess.getCBoardState())) {
            text = "Stalemate! It's a Tie!";
        } else if (chess.getCurrentPlayer()) {
            text = "White's turn";
        } else {
            text = "Black's turn";
        }
        status.setText(text);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        for (int i = 0; i < 800; i += 100) {
            g.drawLine(i, 0, i, 800);
        }
        for (int i = 0; i < 800; i += 100) {
            g.drawLine(0, i, 800, i);
        }

        BufferedImage whitePawn = null;
        try {
            whitePawn = ImageIO.read(new File("files/white_pawn.png"));
        } catch (IOException e) {
        }
        BufferedImage blackPawn = null;
        try {
            blackPawn = ImageIO.read(new File("files/black_pawn.png"));
        } catch (IOException e) {
        }
        BufferedImage whiteKnight = null;
        try {
            whiteKnight = ImageIO.read(new File("files/white_knight.png"));
        } catch (IOException e) {
        }
        BufferedImage blackKnight = null;
        try {
            blackKnight = ImageIO.read(new File("files/black_knight.png"));
        } catch (IOException e) {
        }
        BufferedImage whiteBishop = null;
        try {
            whiteBishop = ImageIO.read(new File("files/white_bishop.png"));
        } catch (IOException e) {
        }
        BufferedImage blackBishop = null;
        try {
            blackBishop = ImageIO.read(new File("files/black_bishop.png"));
        } catch (IOException e) {
        }
        BufferedImage whiteRook = null;
        try {
            whiteRook = ImageIO.read(new File("files/white_rook.png"));
        } catch (IOException e) {
        }
        BufferedImage blackRook = null;
        try {
            blackRook = ImageIO.read(new File("files/black_rook.png"));
        } catch (IOException e) {
        }
        BufferedImage whiteQueen = null;
        try {
            whiteQueen = ImageIO.read(new File("files/white_queen.png"));
        } catch (IOException e) {
        }
        BufferedImage blackQueen = null;
        try {
            blackQueen = ImageIO.read(new File("files/black_queen.png"));
        } catch (IOException e) {
        }
        BufferedImage whiteKing = null;
        try {
            whiteKing = ImageIO.read(new File("files/white_king.png"));
        } catch (IOException e) {
        }
        BufferedImage blackKing = null;
        try {
            blackKing = ImageIO.read(new File("files/black_king.png"));
        } catch (IOException e) {
        }
        BufferedImage availableMove = null;
        try {
            availableMove = ImageIO.read(new File("files/available_move.png"));
        } catch (IOException e) {
        }
        BufferedImage chessBoard = null;
        try {
            chessBoard = ImageIO.read(new File("files/chess_board.png"));
        } catch (IOException e) {
        }
        BufferedImage check = null;
        try {
            check = ImageIO.read(new File("files/check.jpg"));
        } catch (IOException e) {
        }
        // Draw gameboard
        try {
            g.drawImage(chessBoard, 0, 0, 800, 800, null);
        } catch (NullPointerException h) {
        }

        // Display check
        if (chess.getBoard().whiteUnderCheck(chess.getCBoardState())) {
            int x = chess.getBoard().whiteKingLocation(chess.getCBoardState())[0];
            int y = chess.getBoard().whiteKingLocation(chess.getCBoardState())[1];
            g.drawImage(check, 100 * y, 100 * (7 - x), 100, 100, null);
        } else if (chess.getBoard().blackUnderCheck(chess.getCBoardState())) {
            int x = chess.getBoard().blackKingLocation(chess.getCBoardState())[0];
            int y = chess.getBoard().blackKingLocation(chess.getCBoardState())[1];
            g.drawImage(check, 100 * y, 100 * (7 - x), 100, 100, null);
        }

        // Draw pieces
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                String temp = "";
                try {
                    if (chess.getCBoardState()[i][j].getPieceString() != null) {
                        temp = chess.getCBoardState()[i][j].getPieceString();
                    }
                } catch (NullPointerException e) {
                }
                switch (temp) {
                    case "Pw":
                        g.drawImage(whitePawn, 100 * j, 100 * (7 - i), 100, 100, null);
                        break;
                    case "Pb":
                        g.drawImage(blackPawn, 100 * j, 100 * (7 - i), 100, 100, null);
                        break;
                    case "Nw":
                        g.drawImage(whiteKnight, 100 * j, 100 * (7 - i), 100, 100, null);
                        break;
                    case "Nb":
                        g.drawImage(blackKnight, 100 * j, 100 * (7 - i), 100, 100, null);
                        break;
                    case "Bw":
                        g.drawImage(whiteBishop, 100 * j, 100 * (7 - i), 100, 100, null);
                        break;
                    case "Bb":
                        g.drawImage(blackBishop, 100 * j, 100 * (7 - i), 100, 100, null);
                        break;
                    case "Rw":
                        g.drawImage(whiteRook, 100 * j, 100 * (7 - i), 100, 100, null);
                        break;
                    case "Rb":
                        g.drawImage(blackRook, 100 * j, 100 * (7 - i), 100, 100, null);
                        break;
                    case "Kw":
                        g.drawImage(whiteKing, 100 * j, 100 * (7 - i), 100, 100, null);
                        break;
                    case "Kb":
                        g.drawImage(blackKing, 100 * j, 100 * (7 - i), 100, 100, null);
                        break;
                    case "Qw":
                        g.drawImage(whiteQueen, 100 * j, 100 * (7 - i), 100, 100, null);
                        break;
                    case "Qb":
                        g.drawImage(blackQueen, 100 * j, 100 * (7 - i), 100, 100, null);
                        break;
                    default:
                        break;
                }
            }
        }

        // Draw available moves
        try {
            ArrayList<int[]> movesToPrint = chess.getAvailableMovesSelectedPiece();
            for (int[] a : movesToPrint) {
                g.drawImage(availableMove, 100 * a[1] + 35, 100 * (7 - a[0]) + 35, 30, 30, null);
            }
        } catch (NullPointerException f) {
        }

    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
