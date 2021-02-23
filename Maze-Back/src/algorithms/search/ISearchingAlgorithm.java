package algorithms.search;

public interface ISearchingAlgorithm {
    public Solution solve(ISearchable s);
    public String getName();
    public int getNumberOfNodesEvaluated();
}
