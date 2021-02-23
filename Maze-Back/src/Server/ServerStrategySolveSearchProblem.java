package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.SimpleMazeGenerator;
import algorithms.search.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ServerStrategySolveSearchProblem implements IServerStrategy {

    private ISearchingAlgorithm searchingAlgorithm ;
    private static int counter = 0;

    public ServerStrategySolveSearchProblem(){
        searchingAlgorithm = new BestFirstSearch();
       /* String search =Configurations.getSolve();
        if (search.equals("bst")) {
            searchingAlgorithm = new BestFirstSearch();
        }
        else if(search.equals("bfs")){
            searchingAlgorithm = new BreadthFirstSearch();
        }
        else if(search.equals("dfs")){
            searchingAlgorithm = new DepthFirstSearch();
        }*/
    }
    @Override
    public synchronized void handleClient(InputStream inputStream, OutputStream outputStream) {
        try{
            ObjectInputStream in = new ObjectInputStream(inputStream);
            ObjectOutputStream out = new ObjectOutputStream(outputStream);
            Maze m = (Maze) in.readObject();
            String tempDir=System.getProperty("java.io.tmpdir");
            File f = new File(tempDir);
            FilenameFilter textFilter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".maze");
                }
            };
            File[] files = f.listFiles(textFilter);
            File f1 = null;
            Solution sol = null;
            for (int i = 0; i < counter; i++){
                String path_name = tempDir+files[i].getName();
                Path p = Paths.get(tempDir+files[i].getName());
                byte[] data = Files.readAllBytes(p);
                if (Arrays.equals(m.toByteArray(),data)){
                    f1 = files[i];
                    break;
                }
            }
            if (f1 != null){
                String name = f1.getName();
                String maze_number = name.substring(4,name.indexOf('.'));
                File sol_file = new File("solution" + maze_number + ".solution");
                FileInputStream fis = new FileInputStream(sol_file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                sol = (Solution) ois.readObject();
                out.writeObject(sol);
            }
            else {
                FileOutputStream fos = new FileOutputStream(tempDir + "maze" + counter + ".maze");
                fos.write(m.toByteArray());
                SearchableMaze sm = new SearchableMaze(m);
                sol = searchingAlgorithm.solve(sm);
                FileOutputStream fos2 = new FileOutputStream("solution" + Integer.toString(counter) + ".solution");
                ObjectOutputStream ois = new ObjectOutputStream(fos2);
                ois.writeObject(sol);
                out.writeObject(sol);
                counter++;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getCounter() {
        return counter;
    }
}

