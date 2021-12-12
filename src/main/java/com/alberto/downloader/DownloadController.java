package com.alberto.downloader;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

public class DownloadController implements Initializable {

    public TextField tfUrl;
    public TextField delay;
    public Label lbStatus;
    public Label lbDelay;
    public ProgressBar pbProgress;
    private String urlText;
    private DownloadTask downloadTask;
    private File defaultFile;
    private File file;
    private AppController controler;
    private ExecutorService exec;

    private static final Logger logger = LogManager.getLogger(DownloadController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfUrl.setText(urlText);
    }

    public DownloadController(String urlText, File defaultFile, ExecutorService exec) {
        //recojo el texto del enlace y lo muestro en la caja de texto de la pantalla de descarga.
        logger.info("Creado: " + urlText);
        this.urlText = urlText;
        this.defaultFile = defaultFile;
        this.exec = exec;

    }

    @FXML
    public void start(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory((defaultFile));
            file = fileChooser.showSaveDialog((tfUrl.getScene().getWindow()));
            if (file == null)
                return;
            try {
                long delayTime = delay();
                Thread.sleep(delayTime * 1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            downloadTask = new DownloadTask(urlText, file);

            pbProgress.progressProperty().bind(downloadTask.progressProperty());

            downloadTask.stateProperty().addListener((observableValue, oldState, newState) -> {
                System.out.println(observableValue.toString());
                if (newState == Worker.State.SUCCEEDED) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("La descarga ha terminado");
                    alert.show();
                }
            });
            downloadTask.messageProperty().addListener((observableValue, oldValue, newValue) -> lbStatus.setText(newValue));
            exec.execute(downloadTask);


        }catch (MalformedURLException murle) {
            murle.printStackTrace();
        }
    }

    @FXML
    public void stop(ActionEvent event) {
        stop();
    }

    public void stop() {
        if (downloadTask != null) {
            pbProgress.progressProperty().unbind();
            pbProgress.setProgress(0);
            downloadTask.cancel();
        }
    }

    public void delete() {
        if (file != null) {
            file.delete();
        }
    }


    private long delay() {
        if (delay.getText().equals("")) {
            return 0;
        } else {
            try {
                if (Integer.parseInt(delay.getText()) <= 0) {
                    return 0;
                }
                return Integer.parseInt(delay.getText());
            } catch (NumberFormatException nfe) {
                return 0;
            }
        }
    }
}
