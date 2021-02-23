package Model;

import algorithms.mazeGenerators.Maze;

import java.util.List;
import java.util.Observer;

public interface IModel {
    void generate_Maze(int row,int col,int generator,int diff,int mode);
    int[][] getMaze();
    void Solve_maze();
    List<Integer[]> getSolution();
    int get_curr_row();
    int get_curr_col();
    void assignObserver(Observer o);
    int[] get_start();
    int[] get_end();
    void set_position(int direction);
    void set_start(boolean val);
    void save(String name);
    int getTarget();
    void load_maze(String s);
    int getMode();
    int getScore();
    int getDifficulty();
    boolean usedhelp();
    boolean is_winner();
    void sethelp();
}
