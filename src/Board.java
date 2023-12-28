import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    Piece[][] board; //Array of pieces representing board, [0][0] is bottom left corner
    List<Piece> pieces; //List of pieces on the board
    List<Piece> capturedPieces; //List of captured pieces
    /**
     * Constructor for Board class, instantiates array and places pieces in starting positions
     */
    public Board() {
        board = new Piece[8][8];
        pieces = new ArrayList<>(32);
        capturedPieces = new ArrayList<>(32);
        for (int i = 0; i < 8; i++) {
            board[0][i] = new Piece(0, i);
            pieces.add(board[0][i]);
            board[1][i] = new Piece(1, i);
            pieces.add(board[1][i]);
            board[6][i] = new Piece(6, i);
            pieces.add(board[6][i]);
            board[7][i] = new Piece(7, i);
            pieces.add(board[7][i]);
        }
    }

    /**
     * Updates position of a piece on the board
     * @param currPos current position of piece
     * @param newPos new position of piece
     */
    public void updateBoard(Position currPos, Position newPos) {
        //If move is a capture, move captured piece to captured pieces list
        if (board[newPos.rank][newPos.file] != null) {
            pieces.remove(board[newPos.rank][newPos.file]);
            capturedPieces.add(board[newPos.rank][newPos.file]);
        }
        //Update piece position in piece object
        board[currPos.rank][currPos.file].updatePosition(newPos);
        board[newPos.rank][newPos.file] = board[currPos.rank][currPos.file];
        board[currPos.rank][currPos.file] = null;
        //If move was a castle, update rook position
        if (board[newPos.rank][newPos.file].pieceType == PieceType.KING) updateCastlePosition(currPos, newPos);
    }

    /**
     * Updates rook position on board if move was a castle
     * @param currPos current position of king
     * @param newPos newPosition of king
     */
    private void updateCastlePosition(Position currPos, Position newPos) {
        //If right castle, move right rook
        if (newPos.file == currPos.file + 2) {
            board[currPos.rank][7].updatePosition(new Position(currPos.rank, 5));
            board[currPos.rank][5] = board[currPos.rank][7];
            board[currPos.rank][7] = null;    
        }
        //If left castle, move left rook
        if (newPos.file == currPos.file - 2) {
            board[currPos.rank][0].updatePosition(new Position(currPos.rank, 3));
            board[currPos.rank][3] = board[currPos.rank][0];
            board[currPos.rank][0] = null;
        }
    }

    /**
     * Returns piece at given position on board, null if invalid position or empty square
     * @param rank rank to search at
     * @param file file to search at
     * @return piece at board[rank][file]
     */
    public Piece getPieceAt(int rank, int file) { 
        if (rank > 7 || rank < 0 || file > 7 || file < 0) return null;
        return board[rank][file];
    }

    /**
     * Gets the list of pieces on the board
     * @return list of pieces on the board
     */
    public List<Piece> getPieces() { return pieces; }

    /**
     * Gets the list of captured pieces
     * @return list of captured pieces
     */
    public List<Piece> getCapturedPieces() { return capturedPieces; }

    /**
     * Returns the king of the input color
     * @param color color of king to return
     * @return reference to king of input color
     */
    public Piece getColorKing(Color color) {
        return pieces.stream()
                .filter(e -> e.color == color)
                .filter(e -> e.pieceType == PieceType.KING)
                .findFirst()
                .orElse(null); //King should always be in pieces
    }

    /**
     * Returns a list of all pieces of input color
     * @param color color of pieces to return
     * @return list of pieces on the board with matching color
     */
    public List<Piece> getColorPieces(Color color) {
        return pieces.stream()
                .filter(e -> e.color == color)
                .collect(Collectors.toList());
    }

    /**
     * Prints current board position. If currPiece is not null, prints possible moves of currPiece
     * @param currPiece piece to print possible moves of, if null just print current board position
     */
    public void printPosition(Piece currPiece) {
        for (int i = 7; i > -1; i--) {
            System.out.printf("%d  ", i+1);
            for (int j = 0; j < 8; j++) {
                //If selected piece can move to this position, print 1 or C if capture
                if (currPiece != null && currPiece.moves.contains(new Position(i, j))) {
                    String string = (board[i][j] == null) ? "1 " : "C ";
                    System.out.print(string);
                }
                //If no piece at this position, print 0
                else if (board[i][j] == null) {
                    System.out.print("0 ");
                }
                //Otherwise, print letter of piece in this position
                else {
                    switch (board[i][j].pieceType) {
                        case PAWN:
                            System.out.print("P ");
                            break;
                        case ROOK:
                            System.out.print("R ");
                            break;
                        case KNIGHT:
                            System.out.print("N ");
                            break;
                        case BISHOP:
                            System.out.print("B ");
                            break;
                        case QUEEN:
                            System.out.print("Q ");
                            break;
                        case KING:
                            System.out.print("K ");
                            break;
                    }
                }
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("   a b c d e f g h\n");
    }

}