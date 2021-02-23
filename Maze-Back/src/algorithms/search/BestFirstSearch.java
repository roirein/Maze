package algorithms.search;

import java.util.PriorityQueue;

public class BestFirstSearch extends BreadthFirstSearch {

    public BestFirstSearch(){
        this.states = new PriorityQueue<AState>();
    }

    public Solution solve(ISearchable s){
       return super.solve(s);
    }
}
