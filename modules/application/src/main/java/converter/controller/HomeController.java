package converter.controller;

import convert.Convertable;
import convert.ConverterFactory;
import convert.impl.ConverterFactoryImpl;
import converter.ConverterApplication;
import converter.config.BootInitializable;
import converter.convert.Converter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.docx4j.services.client.Format;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

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

            Convertable convert = Converter.builder()
                    .inFormat(Format.DOCX)
                    .outFormat(Format.PDF)
                    .and(lbPath.getText())
                    .build();

            convert.convert(converterFactory.createStrategy(convert))
                    .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                    .observeOn(io.reactivex.schedulers.Schedulers.single())
                    .subscribe(() -> {
                        Platform.runLater(() -> lbPath.setText("Konwertowanie zakończono pomyślnie!"));
                        btnFileOpen.setDisable(false);
                        btnConvert.setDisable(false);
                        progressBar.setVisible(false);
                    }, throwable -> {
                        Platform.runLater(() -> lbPath.setText("Coś poszło nie tak!"));
                        throwable.printStackTrace();
                        btnFileOpen.setDisable(false);
                        btnConvert.setDisable(false);
                        progressBar.setVisible(false);
                    });
        });
    }







}
