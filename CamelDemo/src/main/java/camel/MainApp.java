package camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import java.io.File;

public class MainApp {
    public static void main (String [] args) throws Exception {
        //String homeDir = System.getProperty("home.dir");
        File file = new File("/home/andressossa/test1.txt");

        System.out.println(file.canRead()+"_"+file.exists());


        CamelContext ctx = new DefaultCamelContext();
        RouteBuilder route = new MoveFile();
        ctx.addRoutes(route);
        ctx.start();
        // Maybe sleep a little here
        Thread.sleep(2000);
        ctx.stop();

        System.out.println("hola mundo");
    }

}
