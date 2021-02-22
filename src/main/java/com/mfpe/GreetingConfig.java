package com.mfpe;

import io.micronaut.context.annotation.ConfigurationInject;
import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@ConfigurationProperties("hello.config.greeting")
public class GreetingConfig {

    @Getter
    private final String en;
    @Getter
    private final String de;

    @ConfigurationInject
    public GreetingConfig(@NotBlank String en, @NotBlank String de) {
        this.en = en;
        this.de = de;
    }

}
