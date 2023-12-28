import java.util.ArrayList;
import java.util.List;

/**
 * Parent class for all pieces in my chess game
 */
public class Piece{
    PieceType pieceType;
    Position position; //Current position on the board
    Color color; //Color of this piece
    int colorMult; //Multiplier for movement calculations
    int moveCount; //Number of moves this piece has made
    List<Position> moves; //List of possible moves from current position

    /**
     * Constructor for piece class
     * @param rank starting rank of this piece
     * @param file starting file of this piece
     */
    public Piece(int rank, int file) {
        position = new Position(rank, file);
        //If piece is on rank 0 or 1, piece is white, else black
        color = (rank < 4) ? Color.WHITE : Color.BLACK;
        //Set color multiplier for movement calculations
        colorMult = (color == Color.WHITE) ? 1 : -1;
        //Set move counter to 0
        moveCount = 0;
        //Initialize moves list (max # of moves on any given turn is 27 for a queen on an empty board)
        moves = new ArrayList<>(27);
        //If on rank 1 or 6, piece is a pawn
        if (rank == 1 || rank == 6) pieceType = PieceType.PAWN;
        //Else instantiate by file
        else {
            switch(file) {
                case 0: case 7:
                    pieceType = PieceType.ROOK;
                    break;
                case 1: case 6:
                    pieceType = PieceType.KNIGHT;
                    break;
                case 2: case 5:
                    pieceType = PieceType.BISHOP;
                    break;
                case 3:
                    pieceType = PieceType.QUEEN;
                    break;
                case 4:
                    pieceType = PieceType.KING;
                    break;
            }
        }
    }

    /**
     * Get list of possible moves
     * @return list of possible moves for this piece
     */
    public List<Position> getMoves() { return moves; }

    /**
     * Get current position of this piece
     * @return position of this piece
     */
    public Position getPosition() { return position; }

    /**
     * Returns the current rank this piece is on
     * @return rank of this piece
     */
    public int getRank() { return position.rank; }

    /**
     * Returns the current file this piece is on
     * @return file of this piece
     */
    public int getFile() { return position.file; }

    /**
     * Updates the position of this piece on the board
     * @param newPos new position of this piece
     */
    public void updatePosition(Position newPos) {
        this.position = newPos;
        ++moveCount;
    }
}