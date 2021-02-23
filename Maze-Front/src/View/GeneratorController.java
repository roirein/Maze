package View;


import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class GeneratorController implements IView{
//    public ComboBox fxCollections;

    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeColumns;
    @FXML
    ComboBox<String> fxCollections;
    @FXML
    ComboBox<String> Difficulty;
    @FXML
    ComboBox<String> Mode;

    @FXML
    Button go;

    @FXML
    Button close;

    private MyViewModel mvm;

    Stage stage;


    @Override

    public void initialize(){

    }


    public void setMyViewModel(MyViewModel myViewModel){
        mvm = myViewModel;
    }

    public void handle_button(ActionEvent e) throws IOException {
        int diff, mode;
        if (e.getSource() == go){
            try{
                int rows = Integer.valueOf(textField_mazeRows.getText());
                int cols = Integer.valueOf(textField_mazeColumns.getText());
                String generator = fxCollections.getValue();
                if (fxCollections.getSelectionModel().isSelected(2)){
                    diff = Difficulty.getSelectionModel().getSelectedIndex();
                    if(diff == -1){
                        diff = 0;
                    }
                    mode = Mode.getSelectionModel().getSelectedIndex();
                    if(mode == -1)
                        mode = 0;
                }
                else {
                    diff = -1;
                    mode = -1;
                }
                if(rows < 2 || cols < 2){
                    textField_mazeRows.setText("");
                    textField_mazeColumns.setText("");
                    showAlert();
                    return;
                }
                int chosen;
                if(generator == null){
                    chosen = 0;
                }
                else chosen = fxCollections.getSelectionModel().getSelectedIndex();
                mvm.generateMaze(rows,cols,chosen,diff,mode);
                close(go);
            } catch (NumberFormatException ex) {
                textField_mazeRows.setText("");
                textField_mazeColumns.setText("");
                showAlert();
            }
        }
        if(e.getSource() == close){
            close(close);
        }
    }

    public void close(Button b){
        Stage st = (Stage)b.getScene().getWindow();
        st.close();
    }
    public void showAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        DialogPane pane = alert.getDialogPane();
        pane.getStylesheets().add(
                getClass().getResource("myDialog.css").toExternalForm());
        pane.getStyleClass().add("myDialog");
        URL url = getClass().getClassLoader().getResource("images/Donkey.jpg");
        ImageView iv = new ImageView(url.toExternalForm());
        pane.setGraphic(iv);
        //alert.setContentText(message);
        alert.setContentText("invalid input");;
        alert.show();
    }

    public void setStage(Stage st){
        this.stage = st;
    }
    public void mouse_click(ActionEvent e){
        if(e.getSource() == fxCollections){
            if (fxCollections.getSelectionModel().getSelectedItem().equals(fxCollections.getItems().get(2))){
                Difficulty.setDisable(false);
                Mode.setDisable(false);
            }
            else {
                Mode.setDisable(true);
                Difficulty.setDisable(true);
            }
        }
    }
}
