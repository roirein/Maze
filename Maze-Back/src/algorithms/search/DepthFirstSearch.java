package algorithms.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Stack;

public class DepthFirstSearch extends ASearchingAlgorithm {


    public Solution solve(ISearchable problem) {
        ArrayList<AState> sol = DFS(problem);
        return new Solution(sol);
    }


    private ArrayList<AState> DFS(ISearchable problem) {
        HashSet<AState> visited = new HashSet<AState>();
        Stack<AState> solution = new Stack<AState>();//Stack
        boolean flag =false;
        AState start= problem.getStartState();//The State of Start
        AState goal = problem.getGoalState();//The State Goal

        visited.add(start);
        solution.push(start);

        while (solution.empty()==false && !flag) {//if Solution is empty So There is no Solution
            AState front = solution.pop();
            this.nodesvisited++;
            ArrayList<AState> neig = problem.getAllPossibleStates(front);
            int i=0;
            if(neig.size()>0){
                while(neig.size()>0 && visited.contains(neig.get(0))){
                    neig.remove(0);
                }
            }
            if (neig.size()>0){
                AState tempState = neig.get(0);
                solution.push(front);
                solution.push(tempState);
                visited.add(tempState);
                if (tempState.equals(goal)){
                    this.nodesvisited++;
                    flag=true;
                    break;
                }
            }

        }
        return convertToArray(solution);
    }

    // convert stack to array
    private ArrayList<AState> convertToArray(Stack<AState> stack){
        ArrayList<AState> newArray = new ArrayList<>();
        if (stack.size()>0)
        {
            for (int i =0; i<stack.size();i++){
                newArray.add(stack.get(i));
            }
            return newArray;
        }
        return newArray;
    }


}