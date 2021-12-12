package com.alberto.downloader.util;

import java.io.File;
import java.net.URL;

public class R {

    //este método me permite cargar las ventanas con las que va a funcionar mi aplicación.
    public static URL getUi(String name) {
        return Thread.currentThread().getContextClassLoader().getResource("ui" + File.separator + name);
    }
}
