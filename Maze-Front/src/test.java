import Model.IModel;
import Model.MyModel;
import View.IView;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class test extends Application {
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("View/MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = (MyViewController) fxmlLoader.getController();
        view.setMyViewModel(viewModel);
        viewModel.addObserver(view);
        view.setStage(primaryStage);
        System.out.println(System.getProperty("user.dir"));

    }


    public static void main(String[] args) {
        launch(args);
    }

}
