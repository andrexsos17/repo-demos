package camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

import java.io.File;

public class MainApp {
    private Main main;

    public static void main(String[] args) throws Exception {
        MainApp example = new MainApp();
        example.boot();
    }

    public void boot() throws Exception {
        // create a Main instance
        main = new Main();
        // enable hangup support so you can press ctrl + c to terminate the JVM
        main.enableHangupSupport();
        // add routes
        main.addRoutesBuilder(new MoveFile());

        // run until you terminate the JVM
        System.out.println("Starting Camel. Use ctrl + c to terminate the JVM.\n");
        main.run();
    }

}
