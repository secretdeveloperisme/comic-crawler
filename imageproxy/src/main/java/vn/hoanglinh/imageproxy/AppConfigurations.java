package vn.hoanglinh.imageproxy;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class AppConfigurations {
    @Value("${http.header.referer}")
    String refererServer;
    @Bean
    HttpClient httpClient(){
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader(HttpHeaders.REFERER, refererServer));
        return HttpClientBuilder.create().setDefaultHeaders(headers).build();
    }
}
