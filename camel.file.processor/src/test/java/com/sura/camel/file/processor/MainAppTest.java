package com.sura.camel.file.processor;

import com.sura.camel.file.processor.routes.ProcessFileRouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MainApp.class})
public class MainAppTest {

    @Test
    public void testMain() throws Exception {
        String[] args = new String[0];
        Main main = mock(Main.class);
        whenNew(Main.class).withAnyArguments().thenReturn(main);
        ProcessFileRouteBuilder processFileRouteBuilder = mock(ProcessFileRouteBuilder.class);
        whenNew(ProcessFileRouteBuilder.class).withNoArguments().thenReturn(processFileRouteBuilder);
        DefaultCamelContext camelContext = mock(DefaultCamelContext.class);
        when(main.getCamelContext()).thenReturn(camelContext);

        MainApp.main(args);

        verify(main, times(1)).init();
        verify(camelContext, times(1)).addRoutes(processFileRouteBuilder);
        verify(main, times(1)).run(args);
    }
}