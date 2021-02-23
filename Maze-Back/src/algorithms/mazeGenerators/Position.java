package algorithms.mazeGenerators;

import java.io.Serializable;

public class Position implements Serializable {

    private int row;
    private int col;

    public Position(int row,int col){
        this.row = row;
        this.col = col;
    }

    public int getRowIndex() {
        return row;
    }

    public int getColumnIndex() {
        return col;
    }

    public String toString(){
        return "{" + Integer.toString(row) + "," + Integer.toString(col)+"}";
    }

    public boolean equals(Position pos){
        if (this.getRowIndex() == pos.getRowIndex() && this.getColumnIndex() == pos.getColumnIndex()) return true;
        return false;
    }

}
