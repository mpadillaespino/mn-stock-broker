package com.mfpe.auth.jwt;

import io.micronaut.http.HttpRequest;

import io.micronaut.security.authentication.*;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.ArrayList;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    private final static Logger LOG = LoggerFactory.getLogger(AuthenticationProviderUserPassword.class);

    @Override
    public Publisher<AuthenticationResponse> authenticate(
            @Nullable HttpRequest<?> httpRequest,
            AuthenticationRequest<?, ?> authenticationRequest) {
        return Flowable.create(emiiter -> {
            Object identity = authenticationRequest.getIdentity();
            Object secret = authenticationRequest.getSecret();
            LOG.debug("User {} tries to login...", identity);
            if(identity.equals("my-user") && secret.equals("secret")){
                emiiter.onNext(new UserDetails((String) identity, new ArrayList<>()));
                emiiter.onComplete();
                return;
            }
            emiiter.onError(new AuthenticationException(new AuthenticationFailed("wrong username or password")));
        }, BackpressureStrategy.ERROR);
    }
}
