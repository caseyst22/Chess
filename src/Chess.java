import java.util.Scanner;

public class Chess {
    public static void main(String[] args) {
        //int turnCount = 1; //Number of turns completed, where each move made is a turn
        Color turnColor = Color.WHITE; //Which players turn it is
        String stringColor = "";
        boolean check = false;  //True if player whose turn it is in check
        boolean checkmate = false; //True if a checkmate position is reached
        
        //Instantiate game
        Board board = new Board();
        PieceLogic logic = new PieceLogic();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Chess!\n");
        //Calculate all possible first moves
        for (Piece piece : board.getPieces()) logic.calculatePossibleMoves(piece, board);

        while (!checkmate) {
            //Print board
            board.printPosition(null);
            System.out.println();

            //Get piece to check
            stringColor = (turnColor == Color.WHITE) ? "White" : "Black";
            System.out.println("It's " + stringColor + "'s Turn!");
            //If player is in check, print warning message
            if (check) System.out.println("You're in check");
            System.out.print("Select a piece to move (ex. b2): ");
            Position selectedPosition = getInputPosition(scanner);
            if (selectedPosition == null) {
                System.out.println("Invalid selection");
                continue;
            }

            //Get piece at input position
            Piece selectedPiece = board.getPieceAt(selectedPosition.rank, selectedPosition.file);
            //If no piece at position or piece is wrong color, print error message
            if (selectedPiece == null || selectedPiece.color != turnColor) {
                System.out.println("Invalid piece selected");
                continue;
            }

            //If piece cannot move, print error message
            if (selectedPiece.getMoves().isEmpty()) {
                System.out.println("This piece has no moves!");
                continue;
            }
            //If piece is pinned, print error message
            else if (logic.isPinned(selectedPiece, board)) {
                System.out.println("Selected piece is pinned! Choose a different piece to move.\n");
                continue;
            }

            //Print possible moves
            System.out.println("Possible Moves:\n");
            board.printPosition(selectedPiece);
            
            //Get move
            System.out.print("Select where to move this piece (ex. b3): ");
            selectedPosition = getInputPosition(scanner);
            System.out.println();
            
            //Check if input move is valid
            if (selectedPosition == null || !isValidMove(selectedPiece, selectedPosition)) {
                System.out.println("Selected piece can't move there!");
                continue;
            }
            //If input was valid, move piece
            board.updateBoard(selectedPiece.getPosition(), selectedPosition);
            //If player could move, they are no longer in check
            check = false;

            //Check if piece is a pawn and can promote
            if (selectedPiece.pieceType == PieceType.PAWN && (selectedPiece.getRank() == 0 || selectedPiece.getRank() == 7)) {
                pawnPromotion(scanner, selectedPiece);
            }

            //After piece is moved recalculate all possible moves
            for (Piece piece : board.getPieces()) logic.calculatePossibleMoves(piece, board);
            //Remove move into check squares for both kings
            logic.moveIntoCheck(board, board.getColorKing(Color.WHITE));
            logic.moveIntoCheck(board, board.getColorKing(Color.BLACK));

            //Check for checks and checkmate
            Piece otherKing = board.getColorKing(otherColor(turnColor));
            if (logic.inCheck(board.getColorPieces(turnColor), otherKing)) {
                if (logic.inCheckmate(board.getColorPieces(turnColor), board.getColorPieces(otherColor(turnColor)), otherKing)) {
                    checkmate = true;
                }
                else {
                    check = true;
                }
            }

            //If a check was made, remove moves from pieces that don't cover check
            if (check) logic.removeIllegalMoves(board.getColorPieces(turnColor),
                                                board.getColorPieces(otherColor(turnColor)),
                                                board.getColorKing(otherColor(turnColor)));

            //Increment turn count and switch turn color
            //++turnCount;
            turnColor = (turnColor == Color.WHITE) ? Color.BLACK : Color.WHITE;
        }
        System.out.println("Checkmate! " + stringColor + " wins!\n");
        board.printPosition(null);

        scanner.close();
    }

    /**
     * Returns the opposite color to input color
     * @param color input color
     * @return black if color is white, white if color is black
     */
    private static Color otherColor(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    /**
     * Returns true if input move is valid for selected piece, false otherwise
     * @param selectedPiece piece selected by user
     * @param selectedPosition position selected by user
     * @return true if selectedPosition is in selectedPiece's move list, false otherwise
     */
    private static boolean isValidMove(Piece selectedPiece, Position selectedPosition) {
        return selectedPiece.getMoves().stream().anyMatch(selectedPosition::equals);
    }

    /**
     * Reads position selection from user input
     * @param scanner scanner object
     * @return selected position if input is valid, null otherwise
     */
    private static Position getInputPosition(Scanner scanner) {
        String input = scanner.nextLine().trim();
        //If input is not correct length, print error message
        if (input.length() != 2) { return null; }
        //Parse file
        int file = Character.getNumericValue(input.toLowerCase().charAt(0)) - 10;
        //Parse rank
        int rank = Character.getNumericValue(input.toLowerCase().charAt(1)) - 1;
        //If position is invalid, print error message
        if (file < 0 || file > 7 || rank < 0 || rank > 7) { return null; }
        return new Position(rank, file);
    }
    
    /**
     * Handles pawn promotion user interaction
     * @param scanner scanner object
     * @param pawn pawn to promote
     */
    private static void pawnPromotion(Scanner scanner, Piece pawn) {
        System.out.println("Which piece would you like to promote to: (Q)ueen, (B)ishop, K(n)ight, or (R)ook?");
        while (true) {
            String input = scanner.nextLine();
            switch (input.toUpperCase().charAt(0)) {
                case 'Q':
                    System.out.println("Promoted to Queen!");
                    pawn.pieceType = PieceType.QUEEN;
                    return;
                case 'B':
                    System.out.println("Promoted to Bishop!");
                    pawn.pieceType = PieceType.BISHOP;
                    return;
                case 'N':
                    System.out.println("Promoted to Knight!");
                    pawn.pieceType = PieceType.KNIGHT;
                    return;
                case 'R':
                    System.out.println("Promoted to Rook!");
                    pawn.pieceType = PieceType.ROOK;
                    return;
            }
            System.out.println("Invalid input, please enter a valid character (Q, B, N, R)");
        }
    }
}
