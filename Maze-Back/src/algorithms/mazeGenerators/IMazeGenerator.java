package algorithms.mazeGenerators;

public interface IMazeGenerator {
    public Maze generate(int row,int col);
    public long measureAlgorithmTimeMillis(int row,int col);

}