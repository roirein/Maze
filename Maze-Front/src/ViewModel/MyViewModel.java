package ViewModel;

import Model.IModel;
import Model.MyModel;
import View.MazeDisplayer;
import View.MyViewController;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel myModel;
    private int[][] maze;
    private List<Integer[]> sol;
    private int row;
    private int col;
    private int target;
    private int mode = -1;
    private int score = 0;
    private int level = -1;

    StringProperty limit = new SimpleStringProperty();


    public MyViewModel(IModel model){
        this.myModel = model;
        this.myModel.assignObserver(this);
        this.maze = null;
        this.sol = null;
    }

    public int[][] getMaze() {
        return maze;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel && arg instanceof String){
            String order = (String) arg;
            switch (order){
                case "Generate":
                case "Generate_start":
                    this.maze = myModel.getMaze();
                    this.target = myModel.getTarget();
                    this.mode = myModel.getMode();
                    this.level = myModel.getDifficulty();
                    this.score = myModel.getScore();
                    limit.set(Integer.toString(target));
                    break;
                case "Solution":
                    this.sol = myModel.getSolution();
                    break;
                case "game over":
                case "move":
                    this.row = myModel.get_curr_row();
                    this.col = myModel.get_curr_col();
                    this.score = myModel.getScore();
                    break;
                case "Load Start":
                case "Load":
                    this.score = myModel.getScore();
                    this.target = myModel.getTarget();
                    limit.set(Integer.toString(target));
                    this.mode = myModel.getMode();
                    this.level = myModel.getDifficulty();
                    this.maze = myModel.getMaze();
                    this.row = myModel.get_curr_row();
                    this.col = myModel.get_curr_col();
            }
            setChanged();
            notifyObservers(order);
        }

    }

    public void generateMaze(int row,int col,int generator,int diff,int mode){
        this.myModel.generate_Maze(row,col,generator,diff, mode);
    }

    public void solveMaze(){
        myModel.Solve_maze();
    }

    public int[] get_start(){
        return myModel.get_start();
    }

    public int[] get_end(){
        return myModel.get_end();
    }

    public List<Integer[]> getSolution(){
        return myModel.getSolution();
    }

    public void moveCharacther(KeyEvent keyEvent){
        int direction = -1;
        switch (keyEvent.getCode()){
            case NUMPAD6:
                direction = 1;
                break;
            case NUMPAD1:
                direction = 2;
                break;
            case NUMPAD8:
                direction = 3;
                break;
            case NUMPAD7:
                direction = 4;
                break;
            case NUMPAD4:
                direction = 5;
                break;
            case NUMPAD9:
                direction = 6;
                break;
            case NUMPAD2:
                direction = 7;
                break;
            case NUMPAD3:
                direction = 8;
                break;
        }
        myModel.set_position(direction);

    }

    public int getScore() {
        return score;
    }

    public void set_start(){
        this.deleteObservers();
        myModel.set_start(true);
    }

    public void save(String name){
        myModel.save(name);
    }

    public void load_maze(String s) {
        myModel.load_maze(s);
    }

    public int getTarget() {
        return target;
    }

    public int getMode() {
        return mode;
    }

    public String getLimit() {
        return limit.get();
    }

    public StringProperty limitProperty() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit.set(limit);
    }

    public int getLevel() {
        return level;
    }
    public boolean help(){
        return myModel.usedhelp();
    }
    public void set_help(){
        myModel.sethelp();
    }

    public boolean is_winner(){
        return myModel.is_winner();
    }
}
