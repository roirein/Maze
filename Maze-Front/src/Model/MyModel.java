package Model;

import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.*;
import algorithms.search.*;
import Server.*;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MyModel extends Observable implements IModel {

    private Maze maze;
    private Position curr_Position;
    private Solution sol;
    private boolean start = true;
    private int target = 0;
    private int mode = -1;
    private int score = 0;
    private int diff = -1;
    private boolean help;



    public MyModel(){
        this.maze = null;
        this.curr_Position = null;
        this.sol = null;
    }
    public int[][] getMaze() {
        return maze.getMaze();
    }


    @Override
    public void generate_Maze(int row, int col, int generator,int diff,int mode){
        Server mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze(get_generator(generator)), Configurations.getThreads());
        mazeGeneratingServer.start();
        Socket s = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            s = new Socket(InetAddress.getLocalHost(), 5400);
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
            out.flush();
            int[] dimension = {row,col};
            out.writeObject(dimension);
            byte[] compressedMaze = (byte[]) ((byte[])in.readObject());
            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
            byte[] decompressedMaze = new byte[dimension[0] * dimension[1] + 16];
            is.read(decompressedMaze);
            Maze maze = new Maze(decompressedMaze);
            s.close();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.maze = maze;
            this.mode = mode;
            this.diff = diff;
            this.help = false;
            this.score = 0;

            curr_Position = maze.getStartPosition();
            if(mode != -1 && diff != -1){
               setTarget(diff,mode);
            }
            setChanged();
            if (start) {
                start = false;
                notifyObservers("Generate_start");
            }
            else {
                notifyObservers("Generate");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            mazeGeneratingServer.stop();
        }
    }

    @Override
    public void Solve_maze() {
        ArrayList<Integer[]> sol = new ArrayList<Integer[]>();
        ArrayList<AState> mazeSolutionSteps = null;
        Server solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem(getSolver()),Runtime.getRuntime().availableProcessors()*2);
        solveSearchProblemServer.start();
        Socket s = null;
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        try {
            s = new Socket(InetAddress.getLocalHost(), 5401);
            out = new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
            out.writeObject(maze);
            out.flush();
            this.sol = (Solution)in.readObject();
            s.close();
            setChanged();
            notifyObservers("Solution");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            solveSearchProblemServer.stop();
        }
    }

    public List<Integer[]> getSolution(){
        ArrayList<Integer[]> solution = new ArrayList<Integer[]>();
        if(sol != null) {
            for (int i = 0; i < sol.getSolutionPath().size(); i++) {
                MazeState ms = (MazeState) sol.getSolutionPath().get(i);
                Integer[] cell = {ms.getPos().getRowIndex(), ms.getPos().getColumnIndex()};
                solution.add(cell);
            }
        }
        return solution;
    }

    @Override
    public int get_curr_row() {
        return curr_Position.getRowIndex();
    }

    @Override
    public int get_curr_col() {
        return curr_Position.getColumnIndex();
    }


    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }


    public int[] get_end(){
        return new int[]{maze.getGoalPosition().getRowIndex(),maze.getGoalPosition().getColumnIndex()};
    }

    public int[] get_start(){
        return new int[]{maze.getStartPosition().getRowIndex(),maze.getStartPosition().getColumnIndex()};
    }

    public void set_position(int direction){
        int row = curr_Position.getRowIndex();
        int col = curr_Position.getColumnIndex();
        switch (direction){
            case 1:
                if(valid_move(row-1,col)) {
                    if (mode == 0) {
                        score += 1;
                    } else score += 10;
                    row--;
                }
                break;
            case 2:
                if(valid_move(row-1,col+1)){
                    if(mode == 0){
                        score+=1;
                    }
                    else score += 15;
                    row--;
                    col++;
                }
                break;
            case 3:
                if(valid_move(row,col+1)) {
                    if (mode == 0) {
                        score += 1;
                    } else score += 10;
                    col++;
                }
                break;
            case 4:
                if(valid_move(row+1,col+1)){
                    if(mode == 0){
                        score+=1;
                    }
                    else score += 15;
                    row++;
                    col++;
                }
                break;
            case 5:
                if(valid_move(row+1,col)) {
                    if(mode == 0){
                        score+=1;
                    }
                    else score += 10;
                    row++;
                }
                break;
            case 6:
                if(valid_move(row+1,col-1)){
                    if(mode == 0){
                        score+=1;
                    }
                    else score += 15;
                    row++;
                    col--;
                }
                break;
            case 7:
                if(valid_move(row,col-1)) {
                    if (mode == 0) {
                        score += 1;
                    } else score += 10;
                    col--;
                }
                break;
            case 8:
                if(valid_move(row-1,col-1)){
                    if(mode == 0){
                        score+=1;
                    }
                    else score += 15;
                    row--;
                    col--;
                }
                break;
        }
        curr_Position = new Position(row,col);
        setChanged();
        if(is_game_over()){
            notifyObservers("game over");
        }
        else {
            notifyObservers("move");
        }
    }

    @Override
    public void set_start(boolean val) {
        start = val;
    }

    @Override
    public void save(String name) {
        try {
            Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/src/Resources/savedMazed/" + name));
            File maze_file = new File(System.getProperty("user.dir") + "/src/Resources/savedMazed/" + name + "/" + name+"maze.maze" );
            File position_file = new File(System.getProperty("user.dir") + "/src/Resources/savedMazed/" + name + "/" + name+"last_pos.pos" );
            File player_state = new File(System.getProperty("user.dir") + "/src/Resources/savedMazed/" + name + "/" + name+"state.txt" );
            FileOutputStream fos = new FileOutputStream(maze_file);
            fos.write(maze.toByteArray());
            String pos = curr_Position.getRowIndex() + "," + curr_Position.getColumnIndex();
            FileOutputStream fos1 = new FileOutputStream(position_file);
            FileOutputStream fos2 = new FileOutputStream(player_state);
            byte[] pos1 = pos.getBytes();
            fos1.write(pos1);
            String data = target + "," + score + "," + mode + "," + diff;
            fos2.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getTarget() {
        return target;
    }

    @Override
    public void load_maze(String s) {
        try {
            Path pos_path = Paths.get(System.getProperty("user.dir") + "/src/Resources/savedMazed/" + s + "/" + s + "last_pos.pos");
            Path maze_path = Paths.get(System.getProperty("user.dir") + "/src/Resources/savedMazed/" + s + "/" + s + "maze.maze");
            Path player_state = Paths.get(System.getProperty("user.dir") + "/src/Resources/savedMazed/" + s + "/" + s + "state.txt");
            byte[] data = Files.readAllBytes(maze_path);
            this.maze = new Maze(data);
            byte[] pos = Files.readAllBytes(pos_path);
            String text = new String(pos);
            String row = text.substring(0, text.indexOf(','));
            String col = text.substring(text.indexOf(',') + 1);
            byte[] data1 = Files.readAllBytes(player_state);
            String text1 = new String(data1);
            String[] data2 = text1.split(",");
            this.target = Integer.parseInt(data2[0]);
            this.score = Integer.parseInt(data2[1]);
            this.mode = Integer.parseInt(data2[2]);
            this.diff = Integer.parseInt(data2[3]);
            this.maze = new Maze(data);
            this.curr_Position = new Position(Integer.parseInt(row), Integer.parseInt(col));
            set_configurations(-1,this.diff,this.mode);
            setChanged();
            if (start = true)
                notifyObservers("Load Start");
            else {
                notifyObservers("Load");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getMode() {
        return mode;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getDifficulty() {
        return diff;
    }

    @Override
    public boolean usedhelp() {
        return help;
    }

    private boolean valid_move(int row,int col){
        if (row < 0 || row >= maze.getMaze().length)
            return false;
        if (col < 0 || col >= maze.getMaze()[0].length)
            return false;
        if (maze.getMaze()[row][col] == 1)
            return false;
        return true;
    }

    public boolean is_game_over(){
        if (curr_Position.equals(maze.getGoalPosition()))
            return true;
        return false;
    }

    public void setTarget(int diff,int mode){
        if(diff < 1)
            return;
        target = 0;
        ISearchingAlgorithm searchingAlgorithm = null;
        if(diff == 1)
            searchingAlgorithm = new DepthFirstSearch();
        if(diff == 2)
            if(mode == 0)
                searchingAlgorithm = new BreadthFirstSearch();
            if(mode == 1)
                searchingAlgorithm = new BestFirstSearch();
        Solution sol = searchingAlgorithm.solve(new SearchableMaze(maze));
        if (mode == 0){
            target = sol.getSolutionPath().size() - 1;
        }
        else{
            for (int i = 1; i < sol.getSolutionPath().size(); i++) {
                Position p = ((MazeState) sol.getSolutionPath().get(i-1)).getPos();
                Position p1 = ((MazeState) sol.getSolutionPath().get(i)).getPos();
                if(Math.abs(p1.getRowIndex()-p.getRowIndex()) == 1 && Math.abs(p1.getColumnIndex()-p.getColumnIndex()) == 1)
                    target+=15;
                else
                    target+=10;
            }
        }
    }

    public boolean is_winner(){
        if (help)
            return false;
        if(mode != -1 && diff > 0){
            if (score > target)
                return false;
        }
        return true;
    }

    @Override
    public void sethelp() {
        help = true;
    }

    private void set_configurations(int generator, int level,int mode) throws IOException {
        Configurations.setProperty(BestFirstSearch.class.getSimpleName());
    }


    private IMazeGenerator get_generator(int generator){
        if (generator == 0)
            return new EmptyMazeGenerator();
        if (generator == 1)
            return new SimpleMazeGenerator();
        if (generator == 2)
            return new MyMazeGenerator();
        return null;
    }

    private ISearchingAlgorithm getSolver(){
        String res = Configurations.getSolve();
        if (res.equals(DepthFirstSearch.class.getSimpleName()))
            return new DepthFirstSearch();
        if (res.equals(BreadthFirstSearch.class.getSimpleName()))
            return new BreadthFirstSearch();
        if(res.equals(BestFirstSearch.class.getSimpleName()))
            return new BestFirstSearch();
        return null;
    }


}
