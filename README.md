# Comic Website Crawler 

## Motivation

When reading comics on a website, I encounter numerous visible and hidden advertisements. 
Additionally, each chapter has few images. To address these inconveniences, I created a small program that crawls content from multiple chapters and displays the output in a single HTML file.
## How to use the program 
### 1. Starting Image Proxy server
Because the target server restricts requests from untrusted hosts, it uses the `Referer` header to validate the request. However, the browser has a policy preventing modification of the `Referer` header before sending a request.
Therefore, I used an HTTP proxy server to modify and forward the request to the target server.

#### Execute the command to run server 
```bash
java -jar .\imageproxy-1.0.0.jar
```

### 2. Crawling content of chapters  
The server uses a rate-limiting method to prevent large-scale crawling of comic content.
Therefore, in the program, I have to limit the number of requests sent and retry sending a request if it detects too many requests. 
#### Execute the command to start crawler program
```bash
java -jar .\comic-crawler-1.0-jar-with-dependencies.jar --start 1 --end 10
```
**How to use the crawler program**
```bash
Usage: ComicCrawler
 -e,--end <arg>     end chapter number
 -s,--start <arg>   start chapter number
 ```

