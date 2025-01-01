package vn.hoanglinh.imageproxy;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.InputStream;


@Controller
@RequestMapping("/proxy")
public class ProxyController {

    private static final Logger log = LoggerFactory.getLogger(ProxyController.class);
    HttpClient httpClient;

    @Autowired
    public ProxyController(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @GetMapping
    public ResponseEntity<InputStreamResource> get(@RequestParam String url){
        log.info("Forward request: {}", url);
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if(statusCode == HttpStatus.SC_OK){
                InputStream inputStream = httpResponse.getEntity().getContent();
                Header contentType = httpResponse.getFirstHeader(HttpHeaders.CONTENT_TYPE);
                ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.ok();
                if(contentType != null){
                    return bodyBuilder.contentType(MediaType.parseMediaType(contentType.getValue())).body(new InputStreamResource(inputStream));
                }else{
                    return bodyBuilder.body(new InputStreamResource(inputStream));
                }

            }
        } catch (IOException e) {
            log.error("Cannot request to server: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
        return ResponseEntity.internalServerError().body(null);

    }
}
