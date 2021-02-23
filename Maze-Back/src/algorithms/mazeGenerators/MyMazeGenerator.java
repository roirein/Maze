package algorithms.mazeGenerators;

import java.util.*;

public class MyMazeGenerator extends AMazeGenerator {

    public Maze generate(int row, int col) {

        if (row <= 3 || col <= 3){
            SimpleMazeGenerator simpleMazeGenerator =new SimpleMazeGenerator();
            return simpleMazeGenerator.generate(row,col);
        }

        int[][] maze = new int[row][col];
        //init the maze
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                maze[i][j] = 1;
            }
        }

        Random rand = new Random();
        int start_row = rand.nextInt(row);
        int start_col = rand.nextInt(col);
        Position start = new Position(start_row,start_col);// the start of maze
        maze[start.getRowIndex()][start.getColumnIndex()] = 0;

        dfs_generation(maze, start.getRowIndex(), start.getColumnIndex());

        int end_row = rand.nextInt(row);
        int end_col = rand.nextInt(col);
        //Checks that the start and end are not on each other
        while (maze[end_row][end_col] != 0 || (end_row == start_row && start_col == end_col)){
            end_row = rand.nextInt(row);
            end_col = rand.nextInt(col);
        }
        Position end = new Position(end_row,end_col);
        maze[end.getRowIndex()][end.getColumnIndex()] = 0;
        return new Maze(maze, start, end);
    }


    private void dfs_generation(int[][] maze, int row, int col) {

        Stack<Position> positions = new Stack<Position>();
        positions.push(new Position(row, col));

        while (!positions.empty()) {
            Position pos = positions.pop();
            ArrayList<Position> neighbours = getNeighbours(maze, pos);
            if (neighbours.size() > 0) {
                positions.push(pos);
                int index = new Random().nextInt(neighbours.size());
                Position neighbour = neighbours.get(index);

                // recognize the neighbours
                if (pos.getRowIndex() - neighbour.getRowIndex() == 2) {
                    maze[pos.getRowIndex() - 1][pos.getColumnIndex()] = 0;
                }
                if (pos.getRowIndex() - neighbour.getRowIndex() == -2) {
                    maze[pos.getRowIndex() + 1][pos.getColumnIndex()] = 0;
                }
                if (pos.getColumnIndex() - neighbour.getColumnIndex() == 2) {
                    maze[pos.getRowIndex()][pos.getColumnIndex() - 1] = 0;
                }
                if (pos.getColumnIndex() - neighbour.getColumnIndex() == -2) {
                    maze[pos.getRowIndex()][pos.getColumnIndex() + 1] = 0;
                }
                maze[neighbour.getRowIndex()][neighbour.getColumnIndex()] = 0;
                positions.push(neighbour);
            }

        }
    }

    private ArrayList<Position> getNeighbours(int[][] maze, Position pos) {
        ArrayList<Position> neighbours = new ArrayList<Position>();
        if (pos.getRowIndex() - 2 >= 0 && maze[pos.getRowIndex() - 2][pos.getColumnIndex()] != 0)
            neighbours.add(new Position(pos.getRowIndex() - 2, pos.getColumnIndex()));
        if (pos.getRowIndex() + 2 < maze.length && maze[pos.getRowIndex() + 2][pos.getColumnIndex()] != 0)
            neighbours.add(new Position(pos.getRowIndex() + 2, pos.getColumnIndex()));
        if (pos.getColumnIndex() + 2 < maze[0].length && maze[pos.getRowIndex()][pos.getColumnIndex() + 2] != 0)
            neighbours.add(new Position(pos.getRowIndex(), pos.getColumnIndex() + 2));
        if (pos.getColumnIndex() - 2 >= 0 && maze[pos.getRowIndex()][pos.getColumnIndex() - 2] != 0)
            neighbours.add(new Position(pos.getRowIndex(), pos.getColumnIndex() - 2));
        return neighbours;
    }

}