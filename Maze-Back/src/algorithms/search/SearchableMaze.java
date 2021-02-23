package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

public class SearchableMaze implements ISearchable{

    private Maze maze;

    public SearchableMaze(Maze maze) {
        this.maze = maze;
    }

    public ArrayList<AState> getAllPossibleStates(AState state) {
        ArrayList<AState> neighbours = new ArrayList<AState>();
        MazeState m_state = (MazeState) state;
        Position p1 = m_state.getPos();
        int[] rows = {-1,-1,0,1,1,1,0,-1};//helpers arrays, each index belong to specific directions
        int[] cols = {0,1,1,1,0,-1,-1,-1};
        for (int i = 0; i < 8; i++){
            int row = p1.getRowIndex() + rows[i];
            int col = p1.getColumnIndex() + cols[i];
            if (!isValid(maze,row,col)) continue;
            if (!isPassage(maze,row,col)) continue;
            int cost;
            if (i % 2 == 0)
                cost = 10;
            else {
                if (!check_diagonal(maze,(MazeState)state,row,col)) continue;
                cost = 15;
            }
            MazeState s = new MazeState(state.getCost() + cost,new Position(row,col));
            s.setS(state);
            neighbours.add(s);
        }
        return neighbours;
    }

    private boolean isValid(Maze maze,int row,int col){
        return (row >= 0 && row < maze.getMaze().length && col >= 0 && col < maze.getMaze()[0].length);
    }


    private boolean isPassage(Maze maze,int row,int col){
        return maze.getMaze()[row][col] == 0;
    }

    private boolean check_diagonal(Maze maze,MazeState state, int row,int col){
        boolean top = isValid(maze,state.getPos().getRowIndex()-1,state.getPos().getColumnIndex()) && isPassage(maze,state.getPos().getRowIndex()-1,state.getPos().getColumnIndex());
        boolean rigth = isValid(maze,state.getPos().getRowIndex(),state.getPos().getColumnIndex()+1) && isPassage(maze,state.getPos().getRowIndex(),state.getPos().getColumnIndex()+1);
        boolean down = isValid(maze,state.getPos().getRowIndex()+1,col) && isPassage(maze,state.getPos().getRowIndex()+1,state.getPos().getColumnIndex());
        boolean left =  isValid(maze,state.getPos().getRowIndex(),state.getPos().getColumnIndex()-1) && isPassage(maze,state.getPos().getRowIndex(),state.getPos().getColumnIndex()-1);
        if (row - state.getPos().getRowIndex() == -1 && col - state.getPos().getColumnIndex() == 1 && (top || rigth)) return true;
        if (row - state.getPos().getRowIndex() == 1 && col - state.getPos().getColumnIndex() == 1 && (down || rigth)) return true;
        if (row - state.getPos().getRowIndex() == 1 && col - state.getPos().getColumnIndex() == -1 && (down || left)) return true;
        if (row - state.getPos().getRowIndex() == -1 && col - state.getPos().getColumnIndex() == -1 && (left || top)) return true;
        return false;
    }

    public AState getStartState() {
        return new MazeState(0,maze.getStartPosition());
    }

    public AState getGoalState() {
        return new MazeState(0,maze.getGoalPosition());
    }

}