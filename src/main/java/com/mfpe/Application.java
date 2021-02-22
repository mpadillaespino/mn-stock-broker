package com.mfpe;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);

//        final ApplicationContext context = Micronaut.run(Application.class, args);
//        final HelloService service = context.getBean(HelloService.class);
//        System.out.println(service.greeting());
//        context.close();
    }
}
