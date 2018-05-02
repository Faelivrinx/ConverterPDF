package converter.controller;

import converter.ConverterApplication;
import converter.config.BootInitializable;
import converter.convert.ConvertStrategy;
import converter.convert.Converter;
import converter.convert.ConverterFactory;
import converter.convert.ConverterFactoryImpl;
import io.reactivex.Completable;
import io.reactivex.Scheduler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.docx4j.services.client.Format;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

@Component
public class HomeController implements BootInitializable {


    private ConverterFactory converterFactory;

    @FXML
    AnchorPane main;

    @FXML
    Button btnFileOpen;

    @FXML
    Button btnConvert;

    @FXML
    ProgressIndicator progressBar;

    @FXML
    Label lbPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        converterFactory = new ConverterFactoryImpl();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file to convert");

        btnFileOpen.setOnMouseClicked(event -> {
            String absolutePath = fileChooser.showOpenDialog(ConverterApplication.getStage()).getAbsolutePath();
            lbPath.setText(absolutePath);
            if (!lbPath.getText().isEmpty()) {
                btnConvert.setDisable(false);
            }
        });

        btnConvert.setOnMouseClicked(event -> {
            progressBar.setVisible(true);
            btnFileOpen.setDisable(true);
            btnConvert.setDisable(true);
            Converter convert = Converter.builder()
                    .inFormat(Format.DOCX)
                    .outFormat(Format.PDF)
                    .and(lbPath.getText())
                    .build();

            Completable.fromCallable(() -> {
                convert.convert(converterFactory.createStrategy(convert));
                return true;
            })
                    // TODO: 02.05.2018 Expoler JavaFXSchedulers.gui() 
                    .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .observeOn(io.reactivex.schedulers.Schedulers.single())
                    .subscribe(() -> {
                        Platform.runLater(() -> lbPath.setText("Konwertowanie zakończono pomyślnie!"));
                        btnFileOpen.setDisable(false);
                        btnConvert.setDisable(false);
                        progressBar.setVisible(false);
                    }, throwable -> {
                        progressBar.setVisible(false);
                    });
        });
    }







}
