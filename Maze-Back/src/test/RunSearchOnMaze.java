package test;

import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;

import java.util.ArrayList;

/**
 * Created by Aviadjo on 3/22/2017.
 */
public class RunSearchOnMaze {
    public static void main(String[] args) {
        //IMazeGenerator mg = new MyMazeGenerator();
        //Maze maze = mg.generate(111, 111 );
        int[][] arr = new int[4][2];
        Maze maze = new Maze(arr,new Position(0,0), new Position(3,0));
        maze.print();
        SearchableMaze searchableMaze = new SearchableMaze(maze);

        solveProblem(searchableMaze, new BreadthFirstSearch());
        //solveProblem(searchableMaze, new DepthFirstSearch());
        solveProblem(searchableMaze, new BestFirstSearch());
    }

    private static void solveProblem(ISearchable domain, ISearchingAlgorithm searcher) {
        //Solve a searching problem with a searcher
        Solution solution = searcher.solve(domain);
        System.out.println(String.format("'%s' algorithm - nodes evaluated: %s", searcher.getName(), searcher.getNumberOfNodesEvaluated()));
        //Printing Solution Path
        System.out.println("Solution path:");
        ArrayList<AState> solutionPath = solution.getSolutionPath();
        for (int i = 0; i < solutionPath.size(); i++) {
            System.out.println(String.format("%s. %s",i,solutionPath.get(i)));
        }
    }
}