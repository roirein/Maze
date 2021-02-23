package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;

import java.io.*;
import java.util.Arrays;

public class ServerStrategyGenerateMaze implements IServerStrategy {

    private IMazeGenerator generator;


    public ServerStrategyGenerateMaze(){
        //Configurations.set();
        String gen =Configurations.getGenerate(System.getProperty("user.dir") + "/src/Resources/Config.Properties");
        if (gen.equals("Original Maze")){
            generator = new MyMazeGenerator();
        }
        else if (gen.equals("Randomized Maze")){
            generator= new SimpleMazeGenerator();
        }
        else if(gen.equals("Empty Maze")){
            generator = new EmptyMazeGenerator();
        }
        //generator = new MyMazeGenerator();
    }
    @Override
    public void handleClient(InputStream inputStream, OutputStream outputStream) throws IOException, ClassNotFoundException{

        ObjectInputStream fromClient = new ObjectInputStream(inputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        int[] arr = (int[])fromClient.readObject();

        Maze maze = generator.generate(arr[0],arr[1]);
        String mazeFileName = "savedMaze.maze";

        MyCompressorOutputStream comp = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
        comp.write(maze.toByteArray());
        comp.flush();
        comp.close();


        FileInputStream fileOutputStream = new FileInputStream(mazeFileName);
        byte contextMaze = (byte) fileOutputStream.read();
        long size_m =fileOutputStream.getChannel().size();
        byte[] byteMaze = new byte[(int) size_m];

        for (int i=0;i<byteMaze.length;i++){
            byteMaze[i] = contextMaze;
            contextMaze = (byte) fileOutputStream.read();
        }
        objectOutputStream.writeObject(byteMaze);


    }
}