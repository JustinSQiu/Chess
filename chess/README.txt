=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: 18754344
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

NOTE: If images don't display, the file paths may have been messed up by the Gradescope submission. Please change
the file paths in GameBoard to make the visuals appear again. Thanks.

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2d Arrays; My chess program uses an 8 by 8 2d array to store the state of the game board. I believe that this
  makes sense because chess is a grid-based game that can be easily represented by an array. Initially, I planned to
  have the board be represented by a 2d array of objects of the square class. The square class would store the
  location of the square and the Piece object inside of the square, and would of course have getter and setter methods
  for its variables. However, a TA mentioned in my proposal grade that I could do away with the Square class and simply
  make the game board a 2d array of Piece objects. I realized that the squares did not need to store anything but
  the pieces inside so I just changed it to a 2d array of Piece objects (the Piece class is extended by each of the
  chess pieces and contains some basic methods common to all pieces. I will discuss my subtyping in the next core
  concept). My justification for using the custom Piece class inside the 2d array is that chess is essentially just
  boards moving on a piece, so storing pieces directly on the board should work. For the 2d array (stored in the
  Board class), I made sure to iterate through it properly by checking each time it is iterated through that the bounds
  I am looking for are not less than 0 or greater than 7 to prevent IndexOutofBounds exceptions. My 2d array is also
  encapsulated properly, as it is a private variable in the Board class and can only be accessed and changed through
  public methods in the Board class.

  2. Inheritance and Subtyping; My Piece class is a concrete class that all of my chess piece classes (Knight,
  Rook, Bishop, Pawn, King, and Queen) extend. The Piece class contains a variable storing whether the piece is
  white or black, a method that gets a String representation for the piece (for convenient matching in other classes)
  as well as a method returning all available moves for the piece aside from special moves (which
  are handled in the board class). This is because having a color and a set of available moves are universal to all
  chess pieces so I can use the same method for each. Furthermore, the Piece class has methods like move, hasMoved,
  and getHasMoved that are there to be overridden by each of the individual classes that extend Piece. In each of the
  individual classes that extended piece, there are additional methods that are unique to each piece as well as
  overriding methods such as move that behave differently for each piece. For instance, the Knight class overrides move
  with it's own move method that checks each of the eight possible squares knights can usually move to and if each is
  valid. I use dynamic dispatch when I create objects representing the chess pieces. For instance, when creating a
  Knight, I write "Piece Nb1 = new Knight(true);" in order to create an object that can be put in the 2d array of Piece
  objects while having access to functions specific to the Knight class.

  3. Complex Game Logic; I implemented chess with all its standard rules (with the exception of the 50-move no captures
  or pawn moves draw rule, the threefold repetition draw rule, the draw by insufficient material rule, and several other
  edge rules that I confirmed with an instructor that I did not have to implement). My chess game has standard movement
  for all chess pieces, as well as proper implementations of queen-side castling, king-side castling, promotion of a
  pawn on the eight or first rank to a queen, rook, bishop, or knight (as per the user's choice), check, checkmate,
  stalemate, inability to move into check, and en passant. To clarify some of the edge cases, my game ensures that
  a king cannot castle out of check, into check, or through check, that a pawn can only en passant another pawn that has
  advanced two squares to the fifth or third rank in the previous turn, that you cannot move a piece causing your king
  to be in check (unless it is stalemate or checkmate), that if in check the player must make a move that leads
  to a board state in which the player is not in check (unless it is stalemate or checkmate), that when a player is in
  check their king's tile turns red as a visual indicator of check, that when checkmated or stalemated a popup window
  appears announcing the result of the game and allowing the user to either restart the game or quit the application,
  that a player cannot castle their king if the king has moved or if the rook on the side that the player intends to
  castle to has moved, and several other edge cases that would be too tedious to list out one by one.

  4. JUnit Testable Component; I wrote many tests to test core game functionality and edge cases to make sure my code
  does not break. My test cases are all exclusive (no two test cases do the exact same thing) and are unit-testable
  (they test the internal state of the game without any reliance on the GUI components). Here is a brief list of
  several of the test cases I implemented:
  - testing that the board sets up correctly
  - testing that one cannot move a piece to an invalid square outside the bounds of the board and that such a move does
  not break the game
  - testing that a valid move will cause the current player to change (turn progression)
  - testing that an invalid move will not cause the current player to change
  - testing that a move made by a player on their opponent's turn will not go through
  - testing a successful use of castling
  - testing that one cannot castle through check
  - testing that one cannot castle if their king or rook has moved
  - testing that a regular capture of an enemy piece by a piece on the appropriate turn works
  - testing that a regular capture of a piece of the same color on the appropriate turn doesn't work
  - testing that a move on a square that contains a null value (no piece) will not go through and that such a move does
  not break the game
  - testing that the knight moves properly
  - testing that the rook moves properly
  - testing that the pawn moves properly
  - testing that the bishop moves properly
  - testing that the queen moves properly
  - testing that the king moves properly
  - testing a successful use of en passant
  - testing an unsuccessful use of en passant because the pawn in question did not advance two squares the previous turn
  - testing checkmate with fool's mate
  - testing stalemate with two rooks and a king
  - testing check without checkmate
  - testing that a player cannot make a move that results in their king being in check with the exception of stalemate
  and checkmate
  - testing a successful instance of promotion

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
Piece: The piece class is a class that represents general chess pieces. It is the superclass to the actual specific
chess pieces (Pawn/Knight/Bishop/Rook/Queen/King). The piece class contains several methods that are universal to
all chess pieces, such as the method to get all available moves (which searches through each move from each possible
square on the board to each other possible square on the board and returns the ones that are valid), as well as several
methods that are meant to be overridden by the classes that extend Piece (such as move, which each class has a different
implementation for because different chess pieces move differently.

Pawn/Knight/Bishop/Rook/Queen/King: These are the classes that extend the Piece superclass. They each have several
methods that are unique to the piece (for example, Pawn uniquely has methods and variables dealing with En Passant,
which other pieces don't need because only pawn can be En Passant-ed, as well as methods that they override (such as
the move function, as discussed above).

Board: The board class stores various variables and methods relating to the internal board state. This includes the
2d array of pieces representing the board state, a method to initialize the board, a method to copy the board, a method
to move pieces on the board (special moves such as castle, en passant, and promotion) are accounted for here, and a
methods dealing with check.

Chess: The chess class stores most variables and methods allowing a complete game to be played (without GUI). It stores
an instance of Board, the current player, a method that allows for the current player to take their turn, a method
that returns the available moves of the selected piece, a method that resets the whole game, and methods that deal with
changing turns after each move, among other things.

GameBoard: The GameBoard class deals with things in the GUI. It extends JPanel and overrides the paintComponent and the
getPreferredSize, allowing it to visually represent the game state to the user. It also deals with aspects of the
controller; with listeners, it modifies the internal game state based on the user's input. Finally, it uses basic File
I/O (which is not one of my four core concepts) to get images of each piece, the board, and several other aspects to
make my user interface look nice.

RunChess: This method is mostly copied from the starting code for Tic Tac Toe. It creates the GUI (as dealt with in
GameBoard) and runs the game.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
Yes, there were several stumbling blocks while I was implementing my code. The first was changing my implementation of
the chess board after reading the TA's suggestion of removing the Square class and simply storing the board as an array
of Pieces. I had to go back through my code and manually refactor a lot of code to ensure that this would not cause
an error. The second was implementing castling (as well as the other special rules, but I did castling first so
it was probably the most difficult). At first, I attempted to implement castling as part of the canMove method in the
King piece. However, I could not figure out how to make it work due to various errors such as not being able to check
if the Rook has moved with a function in King. As such, I decided to implement castling in the move method in board,
which proved to be much easier.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
I would say that my design is quite sound and has a good separation of functionality. Overall, each of the classes
serve their own purpose and there is little overlap between classes (there is some overlap in terms of things like
keeping track of the curPlayer which I would remove if I had a chance to refactor some things, but it does not affect
the functionality). I used the model-view-controller design, in which the user interacts with the controller, which
edits the internal game state/model, which in turn updates what the user sees (the view). The private state is only
accessed through user interactions with the controller; I believe that it is reasonably well encapsulated.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
I used images of chess from wikimedia commons:
https://commons.wikimedia.org/wiki/Category:SVG_chess_pieces

I also used the Java documentation of several classes, most notably:
https://docs.oracle.com/javase/7/docs/api/javax/swing/JOptionPane.html

and several parts of:
https://docs.oracle.com/javase/7/docs/api/javax/swing/package-summary.html