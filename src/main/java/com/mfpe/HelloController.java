package com.mfpe;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;


@Controller()
public class HelloController {

//    @Inject
//    private HelloService helloService;

    private final HelloService helloService;
    private final GreetingConfig config;

    public HelloController(HelloService helloService, GreetingConfig config) {
        this.helloService = helloService;
        this.config = config;
    }

    @Get("${hello.controller.path}")
    public String greeting(){
        return helloService.greeting();
    }

    @Get("/de")
    public String greetingDe(){
        return config.getDe();
    }

    @Get("/en")
    public String greetingEn(){
        return config.getEn();
    }

    @Get("/json")
    public Greeting json(){
        return new Greeting();
    }

}
