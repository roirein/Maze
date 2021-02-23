package algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;

public class  Solution implements Serializable {

    private ArrayList<AState> path;

    public Solution(ArrayList<AState> path) {
        this.path = path;
    }

     public ArrayList<AState> getSolutionPath(){
        return path;
    }
}
