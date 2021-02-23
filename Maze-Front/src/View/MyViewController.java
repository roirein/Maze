package View;

import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.*;


public class MyViewController extends Observable implements IView, Observer {

    @FXML
    MenuItem New;
    @FXML
    MenuItem Save;
    @FXML
    MenuItem Load;
    @FXML
    BorderPane home_border_pane;
    @FXML
    MenuItem help;
    @FXML
    MenuItem about;
    @FXML
    MenuItem exit;
    @FXML
    MenuItem props;


    private Stage st;
    private MyViewModel mvm;
    private MediaPlayer player;
    @Override

    public void initialize() {
        URL mediaUrl = getClass().getClassLoader().getResource("music/HomeMusic.mp3");
        player = new MediaPlayer(new Media(mediaUrl.toExternalForm()));
        player.setCycleCount(100);
        player.play();
    }

    @Override
    public void setMyViewModel(MyViewModel viewModel) {
        mvm = viewModel;
    }

    public void getMeneItemClick(ActionEvent e) throws IOException {
        if (e.getSource() == New) {
            show("Generator.fxml");
        }
        if(e.getSource() == Load){
            File folder = new File(System.getProperty("user.dir") + "/src/Resources/savedMazed");
            File[] files = folder.listFiles();
            String[] files_name = new String[files.length];
            for(int i = 0; i < files_name.length;i++){
                files_name[i] = files[i].getName();
            }
            String instruc = "Choose Your Maze";
            ChoiceDialog<String> saved_mazes = new ChoiceDialog<String>(instruc,files_name);
            Optional<String> res = saved_mazes.showAndWait();
            try {
                if (res.get().equals(instruc)) {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setContentText("Please choose a valid load");
                    alert1.show();
                    return;
                }
                mvm.load_maze(res.get());
            }
            catch (NoSuchElementException ex){
                saved_mazes.close();
            }

        }
        if(e.getSource() == help){
            URL url = getClass().getClassLoader().getResource("images/help.jpg");
            open_image(url.toExternalForm());
        }
        if(e.getSource() == about){
            URL url = getClass().getClassLoader().getResource("images/about.jpeg");
            open_image(url.toExternalForm());
        }
        if(e.getSource() == exit){
            Platform.exit();
            System.exit(0);
        }
        if(e.getSource() == props)
            show("Properties.fxml");
    }

    public void show(String className) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(className));
        Parent root = fxmlLoader.load();
        Stage primaryStage = new Stage();
        primaryStage.setTitle("");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        if (className.equals("Generator.fxml")) {
            GeneratorController gc = fxmlLoader.getController();
            gc.setMyViewModel(mvm);
            gc.setStage(st);
        }
    }



    public void change_scene(String path, boolean loaded) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        Parent root = fxmlLoader.load();
        st.setScene(new Scene(root));
        st.show();
        MazeController mc = fxmlLoader.getController();
        mc.setStage(st);
        this.assignObserver(mc);
        mvm.addObserver(mc);
        mc.setMyViewModel(mvm);
        setChanged();
        player.stop();
        if(loaded){
            notifyObservers("Load");
        }
        else {
            notifyObservers("Generate");
        }
    }






    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyViewModel && arg instanceof String) {
            String order = (String) arg;
            switch (order) {
                case "Generate_start":
                    try {
                        change_scene("Maze.fxml",false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "Load Start":
                    try {
                        change_scene("Maze.fxml",true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }
    }


    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    public void setStage(Stage st){
        this.st = st;
    }

    public void open_image(String path) throws FileNotFoundException {
        Stage help_stage = new Stage();
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        Group root = new Group(imageView);
        help_stage.setTitle("HELP");
        Scene scene = new Scene(root);
        help_stage.setScene(scene);
        help_stage.show();
    }

}