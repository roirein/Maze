package algorithms.search;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearch extends ASearchingAlgorithm {

    protected Queue<AState> states;

    public BreadthFirstSearch() {
        this.states = new LinkedList<AState>();
    }

    public Solution solve(ISearchable Searchable) {
        AState goalState = BFS(Searchable,Searchable.getStartState(),Searchable.getGoalState());
        Solution sol = new Solution(this.path(goalState));
        return sol;
    }

    private AState BFS(ISearchable Searchable,AState start, AState goal) {
        HashSet<AState> visited = new HashSet<AState>();
        visited.add(start);
        states.add(start);
        while (!states.isEmpty()){
            AState curr = states.peek();
            if (curr.equals(goal))
                return curr;
            states.remove();
            ArrayList<AState> neighbours = Searchable.getAllPossibleStates(curr);
            nodesvisited++;
            for (int i = 0; i < neighbours.size(); i++){
                if (!visited.contains(neighbours.get(i))){
                    states.add(neighbours.get(i));
                    visited.add(neighbours.get(i));
                }
            }

        }
        return null;

    }

    /**
     *return the solution path
     */

    private ArrayList<AState> path(AState state){
        ArrayList<AState> path = new ArrayList<AState>();
        if (state == null)
            return path;
        while (state.getS() != null){
            path.add(0,state);
            state = state.getS();
        }
        path.add(0,state);
        return path;
    }

}