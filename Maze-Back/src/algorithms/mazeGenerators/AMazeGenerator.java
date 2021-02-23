package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator{

    public abstract Maze generate(int row,int col);

    @Override
    public long measureAlgorithmTimeMillis(int row, int col) {
        long time1 = System.currentTimeMillis();
        this.generate(row,col);
        long res = System.currentTimeMillis()-time1;
        return res;
    }
}
