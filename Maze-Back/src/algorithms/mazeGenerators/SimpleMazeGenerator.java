package algorithms.mazeGenerators;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator {


    public Maze generate(int row, int column) {

        int[][] maze = new int[row][column];
        // init the maze
        for (int j =0;j<row;j++) {
            for (int i = 0; i < column; i++) {
                maze[j][i] = 1;
            }
        }

        Random random = new Random();
        int colR = random.nextInt(column);
        int rowR = random.nextInt(row);
        Position StartRandom = new Position(rowR,colR);// Start Position
        maze[StartRandom.getRowIndex()][StartRandom.getColumnIndex()] = 0;

        int NumberTotalOfStep = random.nextInt(row*column);// number of step to the exit
        while (NumberTotalOfStep<2){
            NumberTotalOfStep = random.nextInt(row*column);
        }

        Position tmpR=StartRandom;
        Position tmpP;
        //create path
        for (int i =0;i<NumberTotalOfStep;i++){
            tmpP=tmpR;
            ArrayList<Position> neig = getNeighbours(maze,tmpP);
            if(neig.size()>0){
                int index = random.nextInt(neig.size());
                tmpR = neig.get(index);
                maze[tmpR.getRowIndex()][tmpR.getColumnIndex()] = 0;
            }
            else
                break;
        }

        Position EndRandom =tmpR;//The End

        for (int j =0;j<row;j++) {
            for (int i = 0; i < column; i++) {
                if (maze[j][i] == 1) {
                    maze[j][i] = random.nextInt(2);
                }
            }
        }

        return new Maze(maze,StartRandom,EndRandom);
    }


    /**
     *
     * @param maze
     * @param nowState
     * @return all the neighbours  in the maze that different from 0
     */
    private ArrayList<Position> getNeighbours(int[][] maze,Position nowState){
        ArrayList<Position> Neig= new ArrayList<>();
        Position Down = new Position(nowState.getRowIndex()+1,nowState.getColumnIndex());
        if (Down.getRowIndex()<maze.length && maze[Down.getRowIndex()][Down.getColumnIndex()]!=0){
            Neig.add(Down);
        }
        Position Up = new  Position(nowState.getRowIndex()-1,nowState.getColumnIndex());
        if (Up.getRowIndex()>=0 && maze[Up.getRowIndex()][Up.getColumnIndex()]!=0) {
            Neig.add(Up);
        }
        Position Right = new Position(nowState.getRowIndex(),nowState.getColumnIndex()+1);
        if (Right.getColumnIndex()<maze[0].length && maze[Right.getRowIndex()][Right.getColumnIndex()]!=0) {
            Neig.add(Right);
        }
        Position Left = new Position(nowState.getRowIndex(),nowState.getColumnIndex()-1);
        if (Left.getColumnIndex()>=0 && maze[Left.getRowIndex()][Left.getColumnIndex()]!=0) {
            Neig.add(Left);
        }
        return Neig;
    }


}