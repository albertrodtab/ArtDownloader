package com.alberto.downloader;

import com.alberto.downloader.util.R;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        AppController controller = new AppController();

        /*
        le tenemos que decir en donde está el fichero FXML, para que sepa el controller donde está
        Lo sacamos a una Clase R como en android y creamos un método al que le pasamos el nombre de fichero
        para que los vincule
         */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(R.getUi("multidownload.fxml"));

        //le digo la clase donde estan los métodos que va a querer usar
        loader.setController(controller);
        //como dentro del diseño tengo un ScrollPane le indico que lo tiene que cargar y con el metodo load la cargamos
        ScrollPane scrollPane = loader.load();

        //inicializamos la escena que es como inicializar la ventana, le pasamos el scrollPane donde está el diseño
        Scene scene = new Scene(scrollPane);
        //Esto es lo que tienes que inicializar y por último lo pintamos.
        stage.setScene(scene);
        stage.setTitle("ArtDownloader");
        stage.show();

    }

    public static void main(String[] args) {
        //este método lanza el método start y lanza la app
        launch();

    }


}
