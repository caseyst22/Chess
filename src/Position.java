/**
 * Class that represents an ordered pair (file, rank) for a piece's position on the board
 */
public class Position {
    int rank; //Rank (y) position on the board
    int file; //File (x) position on the board

    /**
     * Constructor for Position class
     * @param rank starting rank
     * @param file starting file
     */
    public Position(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }

    /**
     * Compares two positions
     * @param o position to compare to
     * @return true if rank and file are equivalent, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        Position otherPos = (Position)o;
        return (this.rank == otherPos.rank) && (this.file == otherPos.file);
    }
}
