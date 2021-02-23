package View;

import Model.Configurations;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Properties {

    @FXML
    Label label1;
    @FXML
    Label label2;
    @FXML
    Label label3;
    @FXML
    Label label4;

    public void initialize(){
        String[] props = Configurations.getallProperties("src/Resources/Config.Properties");
        //label1.setText("Maze Generator : " + props[0]);
        label2.setText("Maze Solver : " + props[0]);
        //label3.setText("User Rival Algorithm : " + props[2]);
        label4.setText("Maximum Threads Running: " + props[1]);
        
    }
}
