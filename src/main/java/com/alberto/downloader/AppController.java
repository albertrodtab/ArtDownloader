package com.alberto.downloader;

import com.alberto.downloader.util.R;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
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

        launchDownload(urlText, file);
    }

    private void launchDownload(String url, File file) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(R.getUi("download.fxml"));
            DownloadController downloadController = new DownloadController(url, this.file, executor);

            loader.setController(downloadController);
            VBox downloadBox = loader.load();

            String filename = url.substring(url.lastIndexOf("/") + 1);
            tpDownloads.getTabs().add(new Tab(filename, downloadBox));
            //Esto me permite añadir un botón de cierre a cada tapPane, sino lo configuro en el SceneBuilder
            //puedo elegir que se muestre en todas las pestañas, en ninguna o en la selecionada solo.
            //tpDownloads.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

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

    @FXML
    public void readDLC() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile == null)
            return;

        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            String line;
            while ((line = reader.readLine()) != null)
                launchDownload(line, file);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void viewLog(ActionEvent event) throws IOException {
        Desktop desktop = Desktop.getDesktop();
        File log = new File("C:/Users/alber/Downloads/ArtDownloader/ArtDownloader.log");
        desktop.open(log);
    }


}
