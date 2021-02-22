package com.mfpe;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Value;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class HelloService {

//    @Value("${hello.service.greeting}")
//    private String greeting;

    private static final Logger LOG = LoggerFactory.getLogger(HelloService.class);

    @Property(name = "hello.service.greeting", defaultValue = "default")
    private String greeting;

    @EventListener
    public void onStartUp(StartupEvent startupEvent){
        LOG.info("Startup: {}", HelloService.class.getSimpleName());
    }


    public String greeting(){
        LOG.info("Saludo a mostrar: {}", greeting);
        return greeting;
    }

}
