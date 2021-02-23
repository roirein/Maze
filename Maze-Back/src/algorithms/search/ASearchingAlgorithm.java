package algorithms.search;

import algorithms.mazeGenerators.Maze;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm{

    protected int nodesvisited;

    public ASearchingAlgorithm(){
        this.nodesvisited = 0;
    }

    @Override
    public abstract Solution solve(ISearchable s);

    @Override
    public String getName(){
        return this.getClass().getSimpleName();
    }

    @Override
    public int getNumberOfNodesEvaluated() {
        return nodesvisited;
    }

    public long measureAlgorithmTimeMillis(ISearchable s) {
        long time1 = System.currentTimeMillis();
        this.solve(s);
        long res = System.currentTimeMillis()-time1;
        return res;
    }
}
