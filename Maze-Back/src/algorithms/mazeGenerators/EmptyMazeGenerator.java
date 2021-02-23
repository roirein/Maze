package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator{

    @Override
    public Maze generate(int row, int col) {
        int[][] maze = new int[row][col];
        Position start = new Position(0,0);
        Position goal = new Position(row-1,col-1);
        return new Maze(maze,start,goal);

    }

}
