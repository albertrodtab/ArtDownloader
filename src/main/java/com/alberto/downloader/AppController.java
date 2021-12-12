package com.alberto.downloader;

import com.alberto.downloader.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppController {

    public TextField tfUrl;
    public Button btDownload;
    public TabPane tpDownloads;
    //esto me servirá para ir guardando todas las descargas.
    private Map<String, DownloadController> allDownloads;


    public ExecutorService executor = Executors.newFixedThreadPool(2);

    @FXML
    private ScrollPane scrollPane;
    public File file;


    /*Este método me permite cambiar el directorio por defecto*/
    @FXML
    private void changeDirectory (ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) scrollPane.getScene().getWindow();
        file = directoryChooser.showDialog(stage);
    }


    public AppController(){
        //inicializo la lista para ir guardando las descargas
        allDownloads = new HashMap<>();
    }

    @FXML
    public void launchDownload(ActionEvent event) {
        String urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();

        launchDownload(urlText);
    }

    private void launchDownload(String url) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUi("download.fxml"));
            DownloadController downloadController = new DownloadController(url, file, executor);

            loader.setController(downloadController);
            VBox downloadBox = loader.load();

            String filename = url.substring(url.lastIndexOf("/") + 1);
            tpDownloads.getTabs().add(new Tab(filename, downloadBox));

            //cada vez que lance una descarga, la voy a guardar en la lista, así los tengo controlados
            allDownloads.put(url, downloadController);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    @FXML
    private void stopAllDownloads () {
        for (DownloadController downloadController : allDownloads.values())
            downloadController.stop();
    }




}
