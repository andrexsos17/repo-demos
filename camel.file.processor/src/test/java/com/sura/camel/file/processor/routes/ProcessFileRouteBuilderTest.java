package com.sura.camel.file.processor.routes;

import com.sura.camel.file.processor.dto.PilaDto;
import org.apache.camel.builder.DataFormatClause;
import org.apache.camel.builder.ExpressionClause;
import org.apache.camel.builder.ValueBuilder;
import org.apache.camel.component.cxf.CxfEndpoint;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.ProcessDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.SplitDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ProcessFileRouteBuilder.class})
public class ProcessFileRouteBuilderTest {

    ProcessFileRouteBuilder routeBuilder;

    @Before
    public void beforeMethod() {
        routeBuilder = spy(new ProcessFileRouteBuilder());
    }

    @Test
    public void configureMustConfigureCxfEndpointAndStartProcessAndProcessFile() throws Exception {
        doNothing().when(routeBuilder).configureCxfEndpoint();
        doNothing().when(routeBuilder).configureStartProcess();
        doNothing().when(routeBuilder).configureProcessFile();

        routeBuilder.configure();

        verify(routeBuilder, times(1)).configureCxfEndpoint();
        verify(routeBuilder, times(1)).configureStartProcess();
        verify(routeBuilder, times(1)).configureProcessFile();
    }

    @Test
    public void configureCxfEndpointMust() throws Exception {
        CxfEndpoint endpoint = mock(CxfEndpoint.class);
        whenNew(CxfEndpoint.class).withNoArguments().thenReturn(endpoint);
        doNothing().when(routeBuilder).bindToRegistry("processFileEndpoint", endpoint);
        RouteDefinition routeDefinition = mock(RouteDefinition.class);
        doReturn(routeDefinition).when(routeBuilder).from("cxf:bean:processFileEndpoint");
        ExpressionClause expressionClause = mock(ExpressionClause.class);
        doReturn(expressionClause).when(routeDefinition).recipientList();

        routeBuilder.configureCxfEndpoint();

        verify(routeBuilder, times(1)).bindToRegistry("processFileEndpoint", endpoint);
        verify(endpoint, times(1)).setAddress("http://0.0.0.0:8181/cxf/processFile");
        verify(endpoint, times(1)).setServiceClass(com.sura.camel.file.processor.ws.ProcessFileWS.class.getCanonicalName());
        verify(routeBuilder, times(1)).from("cxf:bean:processFileEndpoint");
        verify(routeDefinition, times(1)).recipientList();
        verify(expressionClause, times(1)).simple("direct:${header.operationName}");
    }

    @Test
    public void configureStartProcessMust() throws Exception {
        RouteDefinition routeDefinition = mock(RouteDefinition.class);
        doReturn(routeDefinition).when(routeBuilder).from("direct:startProcess");

        routeBuilder.configureStartProcess();

        verify(routeBuilder, times(1)).from("direct:startProcess");
        verify(routeDefinition, times(1)).inOnly("seda:processFile");
    }

    @Test
    public void configureProcessFileMust() throws Exception {
        BindyCsvDataFormat pilaCsvDataFormat = mock(BindyCsvDataFormat.class);
        whenNew(BindyCsvDataFormat.class).withArguments(PilaDto.class).thenReturn(pilaCsvDataFormat);
        RouteDefinition routeDefinition = mock(RouteDefinition.class);
        doReturn(routeDefinition).when(routeBuilder).from("seda:processFile");
        doReturn(routeDefinition).when(routeDefinition).pollEnrich("file:src/data?fileName=pila.csv");
        doReturn(routeDefinition).when(routeDefinition).unmarshal(pilaCsvDataFormat);
        ValueBuilder body = mock(ValueBuilder.class);
        doReturn(body).when(routeBuilder).body();
        SplitDefinition splitDefinition = mock(SplitDefinition.class);
        doReturn(splitDefinition).when(routeDefinition).split(body);
        doReturn(splitDefinition).when(splitDefinition).parallelProcessing();
        DataFormatClause dataFormatClause = mock(DataFormatClause.class);
        doReturn(dataFormatClause).when(splitDefinition).marshal();
        ProcessDefinition processDefinition = mock(ProcessDefinition.class);
        doReturn(processDefinition).when(dataFormatClause).json();

        routeBuilder.configureProcessFile();

        verify(routeBuilder, times(1)).from("seda:processFile");
        verify(routeDefinition, times(1)).pollEnrich("file:src/data?fileName=pila.csv");
        verify(routeDefinition, times(1)).unmarshal(pilaCsvDataFormat);
        verify(routeBuilder, times(1)).body();
        verify(routeDefinition, times(1)).split(body);
        verify(splitDefinition, times(1)).parallelProcessing();
        verify(splitDefinition, times(1)).marshal();
        verify(dataFormatClause, times(1)).json();
        verify(processDefinition, times(1)).to("rabbitmq:exchange.dojo?hostname=localhost&portNumber=5672&autoDelete=true&username=guest&password=guest&vhost=/&routingKey=rk.dojo&skipQueueDeclare=true");
    }
}