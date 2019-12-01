package camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

import java.io.File;

public class MoveFile extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:/home/andressossa/a/?fileName=test1.txt&noop=true")
                .process(new Processor() {
                    public void process(Exchange msg) {
                        File file = msg.getIn().getBody(File.class);
                        System.out.println("Processing file: " + file);
                    }
                })

                .to("file:/home/andressossa/b/");
    }
}
