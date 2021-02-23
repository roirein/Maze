package algorithms.mazeGenerators;

import java.io.Serializable;
import java.util.ArrayList;



public class Maze implements Serializable {

    private int[][] maze;
    private Position start;
    private Position goal;

    public Maze(int[][] maze, Position start, Position goal) {
        this.maze = maze;
        this.start = start;
        this.goal = goal;
    }
    public Maze(byte[] maze){
        byte[] size = getBytes(maze,0,4);
        byte[] s_row = getBytes(maze,4,3);
        byte[] s_col = getBytes(maze,7,3);
        byte[] g_row = getBytes(maze,10,3);
        byte[] g_col = getBytes(maze,13,3);
        String size_as_string = new String(size);
        String s_row_as_string = new String(s_row);
        String s_col_as_string = new String(s_col);
        String g_row_as_string = new String(g_row);
        String g_col_as_string = new String(g_col);
        this.start = new Position(Integer.parseInt(s_row_as_string),Integer.parseInt(s_col_as_string));
        this.goal = new Position(Integer.parseInt(g_row_as_string),Integer.parseInt(g_col_as_string));
        int[][] new_maze = new int[Integer.parseInt(size_as_string)][(maze.length-16)/Integer.parseInt(size_as_string)];
        int x = 16;
        for (int i = 0; i < new_maze.length;i++){
            for (int j = 0; j < new_maze[0].length;j++){
                new_maze[i][j] = maze[x];
                x++;
            }
        }
        this.maze=new_maze;
    }
    private byte[] getBytes(byte[] array, int i, int j){
        ArrayList<Byte> helper = new ArrayList<Byte>();
        for (int ind = 0; ind < j; ind++){
            if (array[i] != 0)
                helper.add(array[i]);
            i++;
        }
        byte[] for_return = new byte[helper.size()];
        for (int ind = 0; ind < for_return.length;ind++){
            for_return[ind] = helper.get(ind);
        }
        return for_return;
    }

    public Position getStartPosition() { // start of maze
        return start;
    }

    public Position getGoalPosition() { // The Goal of maze
        return goal;
    }

    public int[][] getMaze() { //return array
        return maze;
    }

    public byte[] toByteArray() {

        byte[] maze_byte_array = new byte[maze.length*maze[0].length+16];
        copy_array(4,Integer.toString(maze.length).getBytes(),maze_byte_array);
        copy_array(7,Integer.toString(start.getRowIndex()).getBytes(),maze_byte_array);
        copy_array(10,Integer.toString(start.getColumnIndex()).getBytes(),maze_byte_array);
        copy_array(13,Integer.toString(goal.getRowIndex()).getBytes(),maze_byte_array);
        copy_array(16,Integer.toString(goal.getColumnIndex()).getBytes(),maze_byte_array);
        int ind = 16;
        for (int i = 0; i < maze.length; i++){
            for (int j = 0; j < maze[0].length; j++){
                maze_byte_array[ind] = (byte) maze[i][j];
                ind++;
            }
        }
        return maze_byte_array;
    }

    private void copy_array(int i,byte[] source,byte[] target){
        int j = 0;
        for (int ind = i-source.length; ind < i;ind++){
            target[ind] = source[j];
            j++;
        }
    }



    public void print(){

        for (int i = 0; i < maze.length;i++){
            for (int j = 0; j < maze[0].length;j++){
                if (i == start.getRowIndex() && j == start.getColumnIndex())
                    System.out.print("S");
                else if(i == goal.getRowIndex() && j == goal.getColumnIndex())
                    System.out.print("E");
                else System.out.print(maze[i][j]);
            }
            System.out.print("\n");
        }
    }




}