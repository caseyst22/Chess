import java.util.ArrayList;
import java.util.List;

public class PieceLogic {
    //PIECE MOVEMENT LOGIC
    /**
     * Updates moves list with all possible moves for this piece
     */
    public void calculatePossibleMoves(Piece piece, Board board) {
        piece.getMoves().clear();
        switch (piece.pieceType) {
            case PAWN:
                pawnMoves(piece, board);
                break;
            case ROOK:
                rookMoves(piece, board);
                break;
            case KNIGHT:
                knightMoves(piece, board);
                break;
            case BISHOP:
                bishopMoves(piece, board);
                break;
            case QUEEN:
                queenMoves(piece, board);
                break;
            case KING:
                kingMoves(piece, board);
                break;
        }
    }

    /**
     * Updates moves list if piece is a pawn
     * NOTE: We don't need to check for the end of the board because pawn
     * will become a different piece if it reaches the last rank
     * @param piece pawn to check moves of
     * @param board instance of board object to check moves on
     */
    private void pawnMoves(Piece piece, Board board) {
        //Get 1 space forward position
        if (board.getPieceAt(piece.getRank() + piece.colorMult, piece.getFile()) == null) {
            piece.getMoves().add(new Position(piece.getRank() + piece.colorMult, piece.getFile()));
        }
        //Get 2 space forward position
        if (piece.moveCount == 0 && board.getPieceAt(piece.getRank() + piece.colorMult * 2, piece.getFile()) == null) {
            piece.getMoves().add(new Position(piece.getRank() + piece.colorMult * 2, piece.getFile()));
        }
        //Diagonal capture position
        Piece leftDiagonal = board.getPieceAt(piece.getRank() + piece.colorMult, piece.getFile() - 1);
        Piece rightDiagonal = board.getPieceAt(piece.getRank() + piece.colorMult, piece.getFile() + 1);
        if (leftDiagonal != null && piece.color != leftDiagonal.color) { piece.getMoves().add(leftDiagonal.getPosition()); }
        if (rightDiagonal != null && piece.color != rightDiagonal.color) { piece.getMoves().add(rightDiagonal.getPosition()); }
        //En passant position
        //If piece is white, then piece must be on the 5th rank with a black pawn directly left or right of it, whose moveCount is 1
        //If piece is black, then piece must be on the 4th rank with a white pawn directly left or right of it, whose moveCount is 1
        if ((piece.colorMult > 0 && piece.getRank() == 4) || (piece.colorMult < 0 && piece.getRank() == 3)) {
            Piece left = board.getPieceAt(piece.getRank(), piece.getFile() - 1);
            if (left != null && left.pieceType == PieceType.PAWN && left.color != piece.color && left.moveCount == 1) {
                piece.getMoves().add(new Position(piece.getRank() + piece.colorMult, piece.getFile() - 1));
            }
            Piece right = board.getPieceAt(piece.getRank(), piece.getFile() + 1);
            if (right != null && right.pieceType == PieceType.PAWN && right.color != piece.color && right.moveCount == 1) {
                piece.getMoves().add(new Position(piece.getRank() + piece.colorMult, piece.getFile() + 1));
            }
        }
    }

    /**
     * Updates moves list if piece is a knight
     * @param piece knight to check moves of
     * @param board instance of board object to check moves on
     */
    private void knightMoves(Piece piece, Board board) {
        //Up-left
        addMove(piece, board, piece.getRank() + 2, piece.getFile() - 1);
        //Up-right
        addMove(piece, board, piece.getRank() + 2, piece.getFile() + 1);
        //Left-up
        addMove(piece, board, piece.getRank() + 1, piece.getFile() - 2);
        //Left-down
        addMove(piece, board, piece.getRank() - 1, piece.getFile() - 2);
        //Right-up
        addMove(piece, board, piece.getRank() + 1, piece.getFile() + 2);
        //Right-down
        addMove(piece, board, piece.getRank() - 1, piece.getFile() + 2);
        //Down-left
        addMove(piece, board, piece.getRank() - 2, piece.getFile() - 1);
        //Down-right
        addMove(piece, board, piece.getRank() - 2, piece.getFile() + 1);
    }

    /**
     * Updates moves list if piece is a rook
     * @param piece rook to check moves of
     * @param board instance of board object to check moves on
     */
    private void rookMoves(Piece piece, Board board) {
        //Regular movement
        lateralMoves(piece, board);
    }

    /**
     * Updates moves list if piece is a bishop
     * @param piece bishop to check moves of
     * @param board instance of board object to check moves on
     */
    private void bishopMoves(Piece piece, Board board) {
        diagonalMoves(piece, board);
    }

    /**
     * Updates moves list if piece is a queen
     * @param piece queen to check moves of
     * @param board instance of board object to check moves on
     */
    private void queenMoves(Piece piece, Board board) {
        //Lateral movement
        lateralMoves(piece, board);
        //Diagonal movement
        diagonalMoves(piece, board);
    }

    /**
     * Updates moves list if piece is a king
     * @param piece king to check moves of
     * @param board instance of board object to check moves on
     */
    private void kingMoves(Piece piece, Board board) {
        //Up
        addMove(piece, board, piece.getRank() + 1, piece.getFile());
        //Down
        addMove(piece, board, piece.getRank() - 1, piece.getFile());
        //Left
        addMove(piece, board, piece.getRank(), piece.getFile() - 1);
        //Right
        addMove(piece, board, piece.getRank(), piece.getFile() + 1);
        //Up right
        addMove(piece, board, piece.getRank() + 1, piece.getFile() + 1);
        //Up left
        addMove(piece, board, piece.getRank() + 1, piece.getFile() - 1);
        //Down right
        addMove(piece, board, piece.getRank() - 1, piece.getFile() + 1);
        //Down left
        addMove(piece, board, piece.getRank() - 1, piece.getFile() - 1);
        //Check for castling
        if (piece.moveCount == 0) {
            //Get left rook
            Piece corner = board.getPieceAt(piece.getRank(), 0);
            //Must check every square between king and rook, pieceType, moveCount, and color
            if (board.getPieceAt(piece.getRank(), 1) == null && board.getPieceAt(piece.getRank(), 2) == null && 
                board.getPieceAt(piece.getRank(), 3) == null && corner.moveCount == 0 && corner.pieceType == PieceType.ROOK && 
                corner.color == piece.color) {
                piece.getMoves().add(new Position(piece.getRank(), 2));
            }
            //Get right rook
            corner = board.getPieceAt(piece.getRank(), 7);
            //Same check in opposite direction
            if (board.getPieceAt(piece.getRank(), 5) == null && board.getPieceAt(piece.getRank(), 6) == null &&
                corner.moveCount == 0 && corner.pieceType == PieceType.ROOK && corner.color == piece.color) {
                piece.getMoves().add(new Position(piece.getRank(), 6));
            }
        }
    }

    /**
     * Add all lateral moves to moves list, stopping at the edges of the board, the first same color piece,
     * or the first capture, whichever comes first
     * @param piece rook/queen to check moves of
     * @param board instance of board object to check moves on
     */
    private void lateralMoves(Piece piece, Board board) {
        //Horizontal right
        for (int i = piece.getFile(); i < 7; i++) if (!addMove(piece, board, piece.getRank(), i + 1)) break;
        //Horizontal left
        for (int i = piece.getFile(); i > 0; i--) if (!addMove(piece, board, piece.getRank(), i - 1)) break;
        //Vertical up
        for (int i = piece.getRank(); i < 7; i++) if (!addMove(piece, board, i + 1, piece.getFile())) break;
        //Vertical down
        for (int i = piece.getRank(); i > 0; i--) if (!addMove(piece, board, i - 1, piece.getFile())) break;
    }

    /**
     * Adds all diagonal moves to the moves list, stopping at the edges of the board, the first same color piece,
     * or the first capture, whichever comes first
     * @param piece bishop/queen to check moves of
     * @param board instance of board object to check moves on
     */
    private void diagonalMoves(Piece piece, Board board) {
        //Up left
        for (int i = piece.getRank(), j = piece.getFile(); i < 7 && j > 0; i++, j--) if (!addMove(piece, board, i + 1, j - 1)) break;
        //Up right
        for (int i = piece.getRank(), j = piece.getFile(); i < 7 && j < 7; i++, j++) if (!addMove(piece, board, i + 1, j + 1)) break;
        //Down left
        for (int i = piece.getRank(), j = piece.getFile(); i > 0 && j > 0; i--, j--) if (!addMove(piece, board, i - 1, j - 1)) break;
        //Down right
        for (int i = piece.getRank(), j = piece.getFile(); i > 0 && j < 7; i--, j++) if (!addMove(piece, board, i - 1, j  +1)) break;
    }

    /**
     * If move is legal, adds move to move list. If piece can continue returns true, if a capture returns false
     * @param board instance of board object to check moves on
     * @param rank  rank of move
     * @param file  file of move
     * @return true if move is inbounds and square if empty, false otherwise
     */
    private boolean addMove(Piece piece, Board board, int rank, int file) {
        //If move is out of bounds, return false
        if (rank > 7 || rank < 0 || file > 7 || file < 0) return false;
        //If move is in bounds and square is empty, add move and return true
        else if (board.getPieceAt(rank, file) == null) {
            piece.getMoves().add(new Position(rank, file));
            return true;
        }
        //If move is in bounds and a capture, add move and return false
        else if (board.getPieceAt(rank, file).color != piece.color) {
            piece.getMoves().add(new Position(rank, file));
            return false;
        }
        //If move is in bounds and a friendly piece, return false
        else return false;
    }
    
    //IS PINNED LOGIC
    /**
     * Check if piece is pinned
     * @param piece piece to check if pinned
     * @param board instance of board to check on
     * @return true if pinned, false otherwise
     */
    public boolean isPinned(Piece piece, Board board) {
        //Find all opposing Queens, Bishops, and Rooks that see this piece
        Color attackerColor = (piece.color == Color.WHITE) ? Color.BLACK : Color.WHITE;
        List<Piece> possiblePins = board.getColorPieces(attackerColor).stream()
                .filter(e -> e.pieceType != PieceType.KING)
                .filter(e -> e.pieceType != PieceType.KNIGHT)
                .filter(e -> e.pieceType != PieceType.PAWN)
                .filter(e -> e.getMoves().contains(piece.getPosition()))
                .toList();

        //For each possible pin, check spaces opposite this piece for king
        for (Piece attacker : possiblePins) {
            //Rook or queen on same rank
            if (attacker.getRank() == piece.getRank() && horizontalPin(board, piece, attacker)) return true;
            //Rook or queen on same file
            else if (attacker.getFile() == piece.getFile() && verticalPin(board, piece, attacker)) return true;
            //Queen or bishop at a diagonal
            else if (diagonalPin(board, piece, attacker)) return true;
        }
        return false;
    }

    /**
     * Checks for pin when attacker is a queen or rook on same rank as piece
     * @param board instance of board to check on
     * @param piece piece to check if pinned
     * @param attacker piece attacking piece
     * @return true if attacker pins piece to its king, false otherwise
     */
    private boolean horizontalPin(Board board, Piece piece, Piece attacker) {
        //Check for pin left of current piece
        if (attacker.getFile() > piece.getFile()) {
            for (int i = piece.getFile() - 1; i > -1; i--)
                if (board.getPieceAt(piece.getRank(), i) != null) return isColorKing(piece.color, board, piece.getRank(), i);
        }
        //Check for pin right of current piece
        else {
            for (int i = piece.getFile() + 1; i < 8; i++)
                if (board.getPieceAt(piece.getRank(), i) !=  null) return isColorKing(piece.color, board, piece.getRank(), i);
        }
        return false;
    }

    /**
     * Checks for pin when attacker is a queen or rook on same file as piece
     * @param board instance of board to check pin on
     * @param piece piece to check if pinned
     * @param attacker piece attacking piece
     * @return true if piece is pinned by attacker, false otherwise
     */
    private boolean verticalPin(Board board, Piece piece, Piece attacker) {
        //Check for pin below current piece
        if (attacker.getRank() > piece.getRank()) {
            for (int i = piece.getRank() - 1; i > -1; i--)
                if (board.getPieceAt(i, piece.getFile()) != null) return isColorKing(piece.color, board, i, piece.getFile());
        }
        //Check for pin above current piece
        else {
            for (int i = piece.getRank() + 1; i < 8; i++)
                if (board.getPieceAt(i, piece.getFile()) != null) return isColorKing(piece.color, board, i, piece.getFile());
        }
        return false;
    }

    /**
     * Checks for pin when attacker is a queen or bishop on same diagonal as piece
     * @param board instance of board to check pin on
     * @param piece piece to check if pinned
     * @param attacker piece attacking piece
     * @return true if piece is pinned by attacker, false otherwise
     */
    private boolean diagonalPin(Board board, Piece piece, Piece attacker) {
        //Check for pin on Down-left diagonal (if attacking piece is on Up-right diagonal)
        if (attacker.getRank() > piece.getRank() && attacker.getFile() > piece.getFile()) {
            for (int i = piece.getRank() - 1, j = piece.getFile() - 1; i > -1 && j > -1; i--, j--)
                if (board.getPieceAt(i, j) != null) return isColorKing(piece.color, board, i, j);
        }
        //Check for pin on Down-right diagonal (if attacking piece is on Up-left diagonal)
        else if (attacker.getPosition().rank > piece.getRank() && attacker.getPosition().file < piece.getFile()) {
            for (int i = piece.getRank() - 1, j = piece.getFile() + 1; i > -1 && j < 8; i--, j++)
                if (board.getPieceAt(i, j) != null) return isColorKing(piece.color, board, i, j);
        }
        //Check for pin on Up-left diagonal (if attacking piece is on Down-right diagonal)
        else if (attacker.getPosition().rank < piece.getRank() && attacker.getPosition().file > piece.getFile()) {
            for (int i = piece.getRank() + 1, j = piece.getFile() - 1; i < 8 && j > -1; i++, j--)
                if (board.getPieceAt(i, j) != null) return isColorKing(piece.color, board, i, j);
        }
        //Check for pin on Up-right diagonal (if attacking piece is on Down-left diagonal)
        else {
            for (int i = piece.getRank() + 1, j = piece.getFile() + 1; i < 8 && j < 8; i++, j++)
                if (board.getPieceAt(i, j) != null) return isColorKing(piece.color, board, i, j);
        }
        return false;
    }

    /**
     * Returns true if there is a same color king on the given square, false otherwise
     * @param color color of king to check
     * @param board instance of board to check if king
     * @param rank rank of position to check
     * @param file file of position to check
     * @return true if board[rank][file] contains a king of same color as color, false otherwise
     */
    private boolean isColorKing(Color color, Board board, int rank, int file) {
        return (board.getPieceAt(rank, file) != null && board.getPieceAt(rank, file).pieceType == PieceType.KING &&
                board.getPieceAt(rank, file).color == color);
    }

    //MOVE IN TO CHECK LOGIC
    /**
     * Iterates through pieces on the board, removing moves from king's move list 
     * if an enemy piece sees that square
     * @param board instance of board to check
     * @param king king to check if any moves in move list move into check
     */
    public void moveIntoCheck(Board board, Piece king) {
        List<Piece> attackers = board.getColorPieces((king.color == Color.WHITE) ? Color.BLACK : Color.WHITE);
        for (Piece attacker : attackers) {
            removeConflicts(attacker, king);
        }
    }

    /**
     * Removes all moves from king's move list that are also in piece's move list. 
     * Also checks positions in between king and rook if king can castle.
     * @param piece checks if king moves into any squares seen by this piece
     * @param king king to check moves of
     */
    private void removeConflicts(Piece piece, Piece king) {
        Position[] leftCastlePositions = null;
        Position[] rightCastlePositions = null;
        if (king.moveCount == 0) {
            leftCastlePositions = getCastlePositions(true, king);
            rightCastlePositions = getCastlePositions(false, king);
        }
        //For each position in piece's move list, if it interrupts a move in king's move list, remove that move from king's move list
        for (Position piecePosition : piece.getMoves()) {
            king.getMoves().remove(piecePosition);
            //Check for castle interrupt
            if (leftCastlePositions != null) {
                for (Position castlePosition : leftCastlePositions) {
                    //Remove castle from move list if interrupt exists
                    if (piecePosition.equals(castlePosition)) {
                        king.getMoves().remove(new Position(king.getPosition().rank, 2));
                    }
                }
            }
            if (rightCastlePositions != null) {
                for (Position castlePosition : rightCastlePositions) {
                    //Remove castle from move list if interrupt exists
                    if (piecePosition.equals(castlePosition)) {
                        king.getMoves().remove(new Position(king.getPosition().rank, 6));
                    }
                }
            }
        }
    }

    /**
     * Returns positions in between king and rook if king can castle, null otherwise
     * @param side true if left side, false if right side
     * @param king king to check moves of
     * @return an array of positions that must not be seen by another piece if king can castle
     */
    private Position[] getCastlePositions(boolean side, Piece king) {
        if (side && king.getMoves().contains(new Position(king.getPosition().rank, 2))) {
            return new Position[]{ new Position(king.getPosition().rank, 4),
                                   new Position(king.getPosition().rank, 3),
                                   new Position(king.getPosition().rank, 2),
                                   new Position(king.getPosition().rank, 1), 
                                   new Position(king.getPosition().rank, 0)};
        }
        else if (!side && king.getMoves().contains(new Position(king.getPosition().rank, 6))) {
            return new Position[]{ new Position(king.getPosition().rank, 4), 
                                   new Position(king.getPosition().rank, 5),
                                   new Position(king.getPosition().rank, 6),
                                   new Position(king.getPosition().rank, 7)};
        }
        else return null;
    }

    //CHECK AND CHECKMATE LOGIC
    /**
     * Returns true if king is seen by any opposite color pieces, false otherwise
     * @param attackers potential attackers to check
     * @param king  king to check
     * @return true if king is in check, false otherwise
     */
    public boolean inCheck(List<Piece> attackers, Piece king) { return !getCheckingPieces(attackers, king).isEmpty(); }
    
    /**
     * Returns true if king is in checkmate, false otherwise
     * @param attackers list of potential attackers
     * @param defenders list of potential defenders
     * @param king king to check
     * @return true if king is in checkmate, false otherwise
     */
    public boolean inCheckmate(List<Piece> attackers, List<Piece> defenders, Piece king) {
        //If king has a possible move, return false
        if (!king.getMoves().isEmpty()) return false;
        //Get pieces which check the king
        List<Piece> checkingPieces = getCheckingPieces(attackers, king);
        //If there is more than one checking piece and the king has no moves, it is checkmate
        if (checkingPieces.size() > 1) return true;
        //If there is only one checking piece, get attack vector that sees king
        //If check can be blocked, return false, otherwise return true
        return !canBlock(getAttackVector(king, checkingPieces.get(0)), defenders);
    }

    /**
     * Returns true if attack vector can be blocked by defenders in list, false otherwise
     * @param vector attack vector to check
     * @param defenders potential pieces that can block attack vector
     * @return true if one of defenders can block attack vector, false otherwise
     */
    private boolean canBlock(List<Position> vector, List<Piece> defenders) {
        for (Position position : vector) {
            for (Piece defender : defenders) {
                if (defender.getMoves().contains(position)) return true;
            }
        }
        return false;
    }

    /**
     * Returns a list of opposite color pieces that see input position
     * @param attackers list of pieces that may be attacking the king
     * @param king king being attacker
     * @return list of pieces that do attack the king
     */
    private List<Piece> getCheckingPieces(List<Piece> attackers, Piece king) {
        return attackers.stream()
                .filter(e -> e.getMoves().contains(king.getPosition()))
                .toList();
    }

    /**
     * Returns containing the position of the checking piece and all positions it sees between it and the king
     * @param king king to check
     * @param attacker attacker to check
     * @return the list of all positions in between attacker and king, including attacker's position
     */
    private List<Position> getAttackVector(Piece king, Piece attacker) {
        //If piece is a pawn or knight, it only attacks from the square it is on
        if (attacker.pieceType == PieceType.PAWN || attacker.pieceType == PieceType.KNIGHT) {
            List<Position> vector = new ArrayList<>(1);
            vector.add(attacker.position);
            return vector;
        }
        else if (attacker.getRank() == king.getRank() || attacker.getFile() == king.getFile()) return getHorizontalAttackVector(king, attacker);
        else return getDiagonalAttackVector(king, attacker);
    }

    /**
     * Returns horizontal attack vector between king and attacker
     * @param king king to check
     * @param attacker attacker to check
     * @return list of positions on the path from attacker to king, including attacker
     */
    private List<Position> getHorizontalAttackVector(Piece king, Piece attacker) {
        List<Position> vector = new ArrayList<>(8);
        //Attack from above
        if (attacker.getRank() > king.getRank()) {
            for (int i = attacker.getRank(); i > king.getRank(); i--) vector.add(new Position(i, attacker.getFile()));
            return vector;
        }
        //Attack from below
        else if (attacker.getRank() < king.getRank()) {
            for (int i = attacker.getRank(); i < king.getRank(); i++) vector.add(new Position(i, attacker.getFile()));
            return vector;
        }
        //Attack from right
        else if (attacker.getFile() > king.getFile()) {
            for (int i = attacker.getFile(); i > king.getFile(); i--) vector.add(new Position(attacker.getRank(), i));
            return vector;
        }
        //Attack from left
        else {
            for (int i = attacker.getFile(); i < king.getFile(); i++) vector.add(new Position(attacker.getRank(), i));
            return vector;
        }
    }

    /**
     * Returns diagonal attack vector between king and attacker
     * @param king king to check
     * @param attacker attacker to check
     * @return list of positions on the path from attacker to king, including attacker
     */
    private List<Position> getDiagonalAttackVector(Piece king, Piece attacker) {
        List<Position> vector = new ArrayList<>(8);
        //Attack from above left
        if (attacker.getRank() > king.getRank() && attacker.getFile() < king.getFile()) {
            for (int i = attacker.getRank(), j = attacker.getFile(); i > king.getRank() && j < king.getFile(); i--, j++) vector.add(new Position(i, j));
            return vector;
        }
        //Attack from above right
        else if (attacker.getRank() > king.getRank() && attacker.getFile() > king.getFile()) {
            for (int i = attacker.getRank(), j = attacker.getFile(); i > king.getRank() && j > king.getFile(); i--, j--) vector.add(new Position(i, j));
            return vector;
        }
        //Attack from below left
        else if (attacker.getRank() < king.getRank() && attacker.getFile() < king.getFile()) {
            for (int i = attacker.getRank(), j = attacker.getFile(); i < king.getRank() && j < king.getFile(); i++, j++) vector.add(new Position(i, j));
            return vector;
        }
        //Attack from below right
        else {
            for (int i = attacker.getRank(), j = attacker.getFile(); i < king.getRank() && j > king.getFile(); i++, j--) vector.add(new Position(i, j));
            return vector;
        }
    }

    //TODO
    //STALEMATE LOGIC

    //REMOVE ILLEGAL MOVES LOGIC
    /**
     * Removes all illegal moves from input color pieces if input color is in check
     * @param attackers potential attackers
     * @param defenders potential defenders
     * @param king  king being attacked
     */
    public void removeIllegalMoves(List<Piece> attackers, List<Piece> defenders, Piece king) {
        List<Piece> checkingPieces = getCheckingPieces(attackers, king);
        //If there is more than one check but no checkmate, only piece that can move is the king
        if (checkingPieces.size() > 1) {
            for (Piece piece : defenders) {
                if (piece.pieceType != PieceType.KING) piece.getMoves().clear();
            }
            return;
        }
        List<Position> attackVector = getAttackVector(king, checkingPieces.get(0));
        for (Piece piece : defenders) {
            if (piece.pieceType != PieceType.KING) removeNonBlockingMoves(attackVector, piece);
        }
    }

    /**
     * Removes all moves from piece's move list that don't block the attack vector
     * @param attackVector list of moves along path from attacker to king, including attacker
     * @param piece potential defender
     */
    private void removeNonBlockingMoves(List<Position> attackVector, Piece piece) {
        //For each move in move list, only keep it if the attackVector contains a matching position
        piece.getMoves().removeAll(piece.getMoves().stream()
                .filter(e -> !attackVector.contains(e))
                .toList());
    }
}