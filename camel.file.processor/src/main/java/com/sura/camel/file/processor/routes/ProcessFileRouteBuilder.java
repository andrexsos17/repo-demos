package com.sura.camel.file.processor.routes;

import com.sura.camel.file.processor.dto.PilaDto;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;

public class ProcessFileRouteBuilder extends RouteBuilder {

    public void configure() throws Exception {
        configureCxfEndpoint();
        configureStartProcess();
        configureProcessFile();
    }

    public void configureCxfEndpoint() throws ClassNotFoundException {
        CxfEndpoint endpoint = new CxfEndpoint();
        endpoint.setAddress("http://0.0.0.0:8181/cxf/processFile");
        endpoint.setServiceClass(com.sura.camel.file.processor.ws.ProcessFileWS.class.getCanonicalName());
        bindToRegistry("processFileEndpoint", endpoint);

        from("cxf:bean:processFileEndpoint")
                .recipientList()
                .simple("direct:${header.operationName}")
        ;
    }

    public void configureStartProcess() {
        from("direct:startProcess")
                .inOnly("seda:processFile")
        ;
    }

    public void configureProcessFile() {
        BindyCsvDataFormat pilaCsvDataFormat = new BindyCsvDataFormat(PilaDto.class);

        from("seda:processFile")
                .pollEnrich("file:src/data?fileName=pila.csv")
                .unmarshal(pilaCsvDataFormat)
                .split(body()).parallelProcessing()
                .marshal().json()
                .to("rabbitmq:exchange.dojo?hostname=localhost&portNumber=5672&autoDelete=true&username=guest&password=guest&vhost=/&routingKey=rk.dojo&skipQueueDeclare=true")
        ;
    }
}