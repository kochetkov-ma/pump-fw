package ru.mk.pump.commons.utils;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.function.Consumer;
import javax.net.ssl.SSLContext;
import lombok.experimental.UtilityClass;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;

@UtilityClass
public class HttpClients {

    public HttpClient newHttpClientNoSSL() {
        return newHttpClientNoSSL(null);
    }

    public HttpClient newHttpClientNoSSL(Consumer<HttpClientBuilder> builderConsumer) {
        try {
            final SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, (chain, authType) -> true).build();
            final SSLConnectionSocketFactory ssl = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            if (Objects.nonNull(builderConsumer)) {
                builderConsumer.accept(httpClientBuilder);
            }
            return httpClientBuilder.setSSLSocketFactory(ssl).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            throw new RuntimeException("Error HTTP client creating", e);
        }
    }
}
