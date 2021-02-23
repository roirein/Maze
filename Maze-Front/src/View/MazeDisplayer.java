package View;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int start_row;
    private int start_col;
    private int end_row;
    private int end_col;
    private boolean isSolved = false;
    private int[] last_cell;
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty wallFileNamePlayer = new SimpleStringProperty();
    StringProperty exitFileName = new SimpleStringProperty();
    StringProperty solFileName = new SimpleStringProperty();
    private ArrayList<Integer[]> sol;


    public MazeDisplayer(){
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
       // setImageFileNamePlayer(getClass().getClassLoader().getResource("images/character.png").toExternalForm());
       // setWallFileNamePlayer(getClass().getClassLoader().getResource("images/wall.jpg").toExternalForm());
       // setExitFileName(getClass().getClassLoader().getResource("images/SuperBanana.png").toExternalForm());
       // setSolFileName(getClass().getClassLoader().getResource("images/sol.png").toExternalForm());
    }

    public StringProperty solFileNameProperty() {
        return solFileName;
    }

    public void setSolFileName(String solFileName) {
        this.solFileName.set(solFileName);
    }
    public void drawMaze(int[][] maze,int[] start, int[] end)
    {
        this.maze = maze;
        set_start(start);
        sol = null;
        set_end(end);
        draw();
    }

    public void drawMaze(List<Integer[]> sol)
    {
        this.sol = (ArrayList<Integer[]>) sol;
        isSolved = true;
        draw();
    }
    public void draw()
    {
        if( maze!=null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = maze.length;
            int col = maze[0].length;
            double cellHeight = canvasHeight / row;
            double cellWidth = canvasWidth / col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
            graphicsContext.setFill(Color.BLACK);
            double w, h;
            //Draw Maze
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    h = i * cellHeight;
                    w = j * cellWidth;
                    if (maze[i][j] == 1) // Wall
                    {
                        Image wall_image = null;
                        wall_image = new Image(getClass().getClassLoader().getResource("images/wall.jpg").toExternalForm());
                        graphicsContext.drawImage(wall_image,w, h, cellWidth, cellHeight);
                    } else if (i == last_cell[0] && j == last_cell[1]) {
                        double h_player = last_cell[0] * cellHeight;
                        double w_player = last_cell[1] * cellWidth;
                        Image playerImage = new Image(getClass().getClassLoader().getResource("images/character.png").toExternalForm());
                        graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);

                        /*GraphicsContext graphicsContext1 = getGraphicsContext2D();
                        graphicsContext1.setFill(Color.BLUE);
                        graphicsContext.fillRect(w, h, cellWidth, cellHeight);
                        graphicsContext1.setFill(Color.BLACK);*/
                    }
                    else if (i == end_row && j == end_col) {
                        Image exitImage = new Image(getClass().getClassLoader().getResource("images/SuperBanana.png").toExternalForm());
                        graphicsContext.drawImage(exitImage,w, h, cellWidth, cellHeight);
                    }
                }
            }
            if (isSolved) {
                draw_Solution();
            }

        }
    }
    private void set_end(int[] end_pos){
        end_row = end_pos[0];
        end_col = end_pos[1];
    }

    private void set_start(int[] start_pos){
        start_row = start_pos[0];
        start_col = start_pos[1];
        last_cell = start_pos;
    }

    public void set_position(int[] pos){
        last_cell = pos;
        draw();
    }


    public void draw_Solution() {
        if (sol != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = maze.length;
            int col = maze[0].length;
            double cellHeight = canvasHeight / row;
            double cellWidth = canvasWidth / col;
            Image solImage = new Image(getClass().getClassLoader().getResource("images/sol.png").toExternalForm());
            GraphicsContext graphicsContext1 = getGraphicsContext2D();
            graphicsContext1.setFill(Color.BROWN);
            double w, h;
            for (int i = 1; i < sol.size() - 1; i++) {
                if (last_cell[0] != sol.get(i)[0] || last_cell[1] != sol.get(i)[1]) {
                    h = sol.get(i)[0] * cellHeight;
                    w = sol.get(i)[1] * cellWidth;
                    graphicsContext1.drawImage(solImage,w, h, cellWidth, cellHeight);
                }
            }
        }
    }


    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }

    public String getWallFileNamePlayer() {
        return wallFileNamePlayer.get();
    }

    public StringProperty wallFileNamePlayerProperty() {
        return wallFileNamePlayer;
    }

    public void setWallFileNamePlayer(String wallFileNamePlayer) {
        this.wallFileNamePlayer.set(wallFileNamePlayer);
    }

    public String getExitFileName() {
        return exitFileName.get();
    }

    public StringProperty exitFileNameProperty() {
        return exitFileName;
    }

    public void setExitFileName(String exitFileName) {
        this.exitFileName.set(exitFileName);
    }

    public String getSolFileName() {
        return solFileName.get();
    }


    @Override
    public boolean isResizable() {
        return true;
    }

}
