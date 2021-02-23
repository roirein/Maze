package View;
import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;

public class MazeController implements IView, Observer {

    public MenuItem New;
    @FXML
    public MenuItem Save;
    @FXML
    public MenuItem Load;
    @FXML
    public Button maze_solver;
    @FXML
    public MazeDisplayer md = new MazeDisplayer();
    @FXML
    public Pane pane;
    @FXML
    public ScrollPane scrollPane;
    @FXML
    MenuItem help;
    @FXML
    MenuItem about;
    @FXML
    MenuItem exit;
    @FXML
    Label explain_solutuin;
    @FXML
    Label Target;
    @FXML
    Label your_score;
    @FXML
    Label title1;
    @FXML
    Label title2;
    @FXML
    Label instruction;
    @FXML
    MenuItem props;
    @FXML
    BorderPane pane1;

    private MediaPlayer player;
    private MediaPlayer player2;
    private MediaPlayer player3;
    private int[][] maze;
    private MyViewModel mvm;
    private String curr_save_name;
    Stage st;
    BooleanProperty visible = new SimpleBooleanProperty();
    StringProperty update_player_score = new SimpleStringProperty();


    @Override
    public void initialize() {
        md.widthProperty().bind(scrollPane.widthProperty());
        md.heightProperty().bind(scrollPane.heightProperty());
        md.heightProperty().bind(pane1.heightProperty());
        md.widthProperty().bind(pane1.widthProperty());
        pane1.setMinHeight(400);
        pane1.setMinWidth(400);
        scrollPane.setMinHeight(400);
        explain_solutuin.visibleProperty().bind(visible);
        your_score.textProperty().bind(update_player_score);
        Group g =  new Group(pane);
        scrollPane.setContent(g);
        URL mediaUrl = getClass().getClassLoader().getResource("music/Manuel - Gas Gas Gas.mp3");
        player = new MediaPlayer(new Media(mediaUrl.toExternalForm()));
        player.setCycleCount(100);
        URL mediaUrl1 = getClass().getClassLoader().getResource("music/Victory.mp3");
        player2 = new MediaPlayer( new Media(mediaUrl1.toExternalForm()));
        URL mediaUrl2 = getClass().getClassLoader().getResource("music/Lose.mp3");
        player3 = new MediaPlayer( new Media(mediaUrl2.toExternalForm()));
        player.play();
    }

    public void solve_maze(){
        mvm.solveMaze();
        mvm.set_help();
        maze_solver.setText("maze solved!");
        maze_solver.setDisable(true);
        setVisible(true);
    }

    @Override
    public void setMyViewModel(MyViewModel viewModel) {
        this.mvm = viewModel;
    }

    public void getMeneItemClick(ActionEvent e) throws IOException {
        if (e.getSource() == New) {
            show_alert_start_new_game();
        }
        if(e.getSource() == Save){
            save_alert();
        }
        if(e.getSource() == Load){
            show_alert_load_game();
        }
        if(e.getSource() == help){
            open_image("src/Resources/images/help.jpg");
        }
        if(e.getSource() == about){
            open_image("src/Resources/images/about.jpeg");
        }
        if(e.getSource() == exit){
            Platform.exit();
            System.exit(0);
        }
        if(e.getSource() == props){
            show("Properties.fxml");
        }
    }
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof MyViewController) {
            switch ((String) arg){
                case "Generate":
                    set_all_labels(mvm.getMode(),mvm.getLevel());
                    this.maze = mvm.getMaze();
                    md.drawMaze(maze,mvm.get_start(),mvm.get_end());
                    Target.textProperty().bind(mvm.limitProperty());
                    setUpdate_player_score("0");
                    break;
                case "Load":
                    setUpdate_player_score(Integer.toString(mvm.getScore()));
                    Target.textProperty().bind(mvm.limitProperty());
                    set_all_labels(mvm.getMode(),mvm.getLevel());
                    this.maze = mvm.getMaze();
                    md.drawMaze(maze,mvm.get_start(),mvm.get_end());
                    md.set_position(new int[]{mvm.getRow(),mvm.getCol()});
                    break;

            }
        }
        if(o instanceof MyViewModel){
            switch ((String) arg) {
                case "Load":
                    set_all_labels(mvm.getMode(),mvm.getLevel());
                    setVisible(false);
                    player2.stop();
                    player3.stop();
                    this.maze = mvm.getMaze();
                    maze_solver.setText("Solve");
                    maze_solver.setDisable(false);
                    setUpdate_player_score(Integer.toString(mvm.getScore()));
                    md.set_position(new int[]{mvm.getRow(),mvm.getCol()});
                    Target.textProperty().bind(mvm.limitProperty());
                    break;
                case "Generate":
                    set_all_labels(mvm.getMode(),mvm.getLevel());
                    player2.stop();
                    player3.stop();
                    player.play();
                    this.maze = mvm.getMaze();
                    maze_solver.setText("Solve");
                    maze_solver.setDisable(false);
                    setVisible(false);
                    setUpdate_player_score("0");
                    md.drawMaze(maze, mvm.get_start(), mvm.get_end());
                    Target.textProperty().bind(mvm.limitProperty());
                    break;
                case "Solution":
                    md.drawMaze(mvm.getSolution());
                    break;
                case "move":
                    md.set_position(new int[]{mvm.getRow(),mvm.getCol()});
                    setUpdate_player_score(Integer.toString(mvm.getScore()));
                    break;
                case "game over":
                    boolean winner = mvm.is_winner();
                    player.stop();
                    if(winner)
                        player2.play();
                    else player3.play();
                    setUpdate_player_score(Integer.toString(mvm.getScore()));
                    md.set_position(new int[]{mvm.getRow(),mvm.getCol()});
                    try {
                        show_game_over_Alert(winner);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }
    }

    public void key_pressed(KeyEvent event){
        if (event.getCode() == KeyCode.CONTROL){
            double zoomFactor = 1.1;
            pane.setScaleX(pane.getScaleX() * zoomFactor);
            pane.setScaleY(pane.getScaleY() * zoomFactor);
        }
        mvm.moveCharacther(event);
        event.consume();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
       md.requestFocus();
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


    private void show_game_over_Alert(boolean winner) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        DialogPane pane = alert.getDialogPane();
        String im_path;
        String message;
        alert.setHeaderText("GAME OVER!!!");
        if (winner){
            im_path = "/Resources/images/victory_image.png";
            message = "Congratulations!! you won!!!";
        }
        else{
            im_path = "/Resources/images/Donkey.jpg";
            message = "You sir, are a loser!";
        }
        ImageView iv = new ImageView(this.getClass().getResource(im_path).toString());
        iv.setFitHeight(100);
        iv.setFitWidth(100);
        pane.setGraphic(iv);
        alert.setContentText(message);
        Optional<ButtonType> reslut = alert.showAndWait();
        if (reslut.get() == ButtonType.OK){
            try {
                show("Generator.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(reslut.get() == ButtonType.CANCEL){
            mvm.set_start();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
            Parent root = fxmlLoader.load();
            MyViewController mvc = fxmlLoader.getController();
            mvc.setMyViewModel(mvm);
            mvc.setStage(st);
            mvm.addObserver(mvc);
            st.setTitle("Hello World");
            st.setScene(new Scene(root, 900, 900));
            st.show();
        }
    }

    public void setStage(Stage st){
        this.st = st;
    }


    public void show_alert_load_game(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("You are about to leave the current game, would like to save the game before you leave?");
        ButtonType buttonTypeOne = new ButtonType("Save and Load");
        ButtonType buttonTypeTwo = new ButtonType("Load without saving");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo,buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTypeOne){
            save_alert();
            Load();
        }
        if(result.get() == buttonTypeCancel){
            alert.close();
        }
        if(result.get() == buttonTypeTwo){
            Load();
        }


    }

    public void save_alert(){
        String time = java.time.LocalTime.now().toString();
        time = time.replace(":","");
        TextInputDialog dialog = new TextInputDialog(time);
        dialog.setContentText("enter your maze name");
        Optional<String> result = dialog.showAndWait();
        File folder = new File(System.getProperty("user.dir") + "/src/Resources/savedMazed");
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length;i++){
            if (result.get().equals(files[i].getName())){
                show_warning_alert(result.get());
                break;
            }
        }
        curr_save_name = result.get();
        mvm.save(result.get());
    }


    public void Load(){
        File folder = new File(System.getProperty("user.dir") + "/src/Resources/savedMazed");
        File[] files = folder.listFiles();
        String[] files_name = new String[files.length];
        for(int i = 0; i < files_name.length;i++){
            files_name[i] = files[i].getName();
        }
        ChoiceDialog<String> saved_mazes = new ChoiceDialog<String>(curr_save_name,files_name);
        Optional<String> result = saved_mazes.showAndWait();
        if(curr_save_name == null){
            Alert alert = new Alert((Alert.AlertType.ERROR));
            alert.setContentText("Sorry, There are no saved mazed right now");
            alert.show();
            return;
        }
        mvm.load_maze(result.get());
    }

    public void show_alert_start_new_game() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("You are about to leave the current game, would like to save the game before you leave?");
        ButtonType buttonTypeOne = new ButtonType("Save and Start new game");
        ButtonType buttonTypeTwo = new ButtonType("New game Without saving");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo,buttonTypeCancel);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == buttonTypeOne){
            save_alert();
            show("Generator.fxml");
        }
        if(result.get() == buttonTypeCancel){
            alert.close();
        }
        if(result.get() == buttonTypeTwo){
            show("Generator.fxml");
        }
    }


    public void show_warning_alert(String s){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("There is already a maze with this name, are you sure you want to overwite it?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK)
            mvm.save(s);
        if (result.get() == ButtonType.CANCEL){
            save_alert();
        }
    }

    public void handle_scroll_pane(ScrollEvent event){
        double zoomFactor = 1.1;
        if(event.getDeltaY() < 0)
            zoomFactor = 1/1.1;
        pane.setScaleX(pane.getScaleX() * zoomFactor);
        pane.setScaleY(pane.getScaleY() * zoomFactor);
        event.consume();

    }
    public void open_image(String path) throws FileNotFoundException {
        Stage help_stage = new Stage();
        Image image = new Image(new FileInputStream(path));
        ImageView imageView = new ImageView(image);
        Group root = new Group(imageView);
        help_stage.setTitle("HELP");
        Scene scene = new Scene(root);
        help_stage.setScene(scene);
        help_stage.show();
    }

    public void setVisible(boolean visible) {
        this.visible.set(visible);
    }


    public void setUpdate_player_score(String update_player_score) {
        this.update_player_score.set(update_player_score);
    }


    public void set_all_labels(int mode,int level){
        if(mode == -1){
            instruction.setVisible(false);
            title1.setVisible(false);
            title2.setVisible(false);
            Target.setVisible(false);
            your_score.setVisible(false);
        }
        else{
            if(level == 0){
                instruction.setVisible(false);
                title2.setVisible(false);
                title1.setVisible(true);
                Target.setVisible(false);
                your_score.setVisible(true);
            }
            else{
                instruction.setVisible(true);
                title1.setVisible(true);
                title2.setVisible(true);
                Target.setVisible(true);
                your_score.setVisible(true);
            }
        }
    }


}
