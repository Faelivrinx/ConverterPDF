package converter;

import converter.controller.HomeController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class ConverterApplication extends Application {

    private static String [] arguments;
    private static Stage stage;
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        ConverterApplication.arguments = args;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        Task<Object> task = new Task<Object>() {
            @Override
            protected Object call() throws Exception {
                applicationContext = SpringApplication.run(ConverterApplication.class, arguments);
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            Scene scene = null;
            try {
                Parent load = FXMLLoader.load(getClass().getResource("/scenes/Home.fxml"));
                scene = new Scene(load);
            } catch (IOException e) {
                e.printStackTrace();
                task.cancel();
            }
            primaryStage.setMinHeight(400);
            primaryStage.setMinWidth(400);
            primaryStage.setMaxHeight(400);
            primaryStage.setMaxWidth(400);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Cinewatch");
            primaryStage.show();
        });

        task.setOnFailed(event -> {
            System.exit(0);
            Platform.exit();
        });

        task.run();
    }

    public static Stage getStage(){
        return stage;
    }


}
