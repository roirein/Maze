package algorithms.search;

        import algorithms.mazeGenerators.Maze;
        import algorithms.mazeGenerators.Position;


public class MazeState extends AState {

    private Position pos;

    public MazeState(int cost,Position pos) {
        super(cost);
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MazeState ms = (MazeState) o;
        return (this.getPos().equals(ms.getPos()));
    }

    public String toString() {
        return pos.toString();
    }
}
