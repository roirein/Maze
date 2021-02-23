package algorithms.search;

import java.util.ArrayList;

public interface ISearchable {
    public ArrayList<AState> getAllPossibleStates(AState state);
    public AState getStartState();
    public AState getGoalState();
}