package com.sura.camel.file.processor;

import com.sura.camel.file.processor.routes.ProcessFileRouteBuilder;
import org.apache.camel.main.Main;

public class MainApp {

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.init();
        main.getCamelContext().addRoutes(new ProcessFileRouteBuilder());
        main.run(args);
    }
}