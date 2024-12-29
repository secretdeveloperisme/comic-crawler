package vn.hoanglinh.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class Crawler {
    private static final String COMIC_URL = "https://%s/truyen-tranh/toan-chuc-phap-su-4140-chap-%d.html";
    private static final String CHAPTER_IMAGE_SELECTOR = ".chapter_content .page-chapter > img";
    private static final int delay = 700;
    private static final String TITLE_SELECTOR = "h1.detail-title";
    public static final String CHAPTER_FILE_NAME_TEMPLATE = "chapter_%d-%d.html";
    private String outputFileName;
    private int crawledCounter = 0;

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(int startChapter, int endChapter) {
        this.outputFileName = CHAPTER_FILE_NAME_TEMPLATE.formatted(startChapter, endChapter);
    }

    public String readAllString(InputStream inputStream) throws IOException {
        int bufferSize = 1024;
        char[] buffer = new char[bufferSize];
        StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        for (int numRead; (numRead = in.read(buffer, 0, buffer.length)) > 0; ) {
            out.append(buffer, 0, numRead);
        }
        return out.toString();
    }
    public Document connectToPage(final String page) throws IOException {
        Document document;
        try {
            document = Jsoup.connect(page).get();
        } catch (IOException e) {
            System.out.printf("Retry to connect to page: %s again%n", page);
            try {
                Thread.sleep(Duration.ofMillis(2000));
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            document = Jsoup.connect(page).get();
        }
        return document;
    }
    public ChapterContent extractContents(Document document) throws IOException {
        String detailTitle = document.select(TITLE_SELECTOR).text();
        System.out.println("Crawling page: " + detailTitle );

        ChapterContent chapterContent = new ChapterContent(detailTitle);

        Elements chapterImages = document.select(CHAPTER_IMAGE_SELECTOR);
        for(Element chapterImage: chapterImages){
            String imgSrc = chapterImage.attr("src");
            chapterContent.addChapterImageUrl(imgSrc);
        }
        return chapterContent;
    }
    public void createOutputFile(int startChapter, int endChapter, Document outDocument) throws IOException {
        setOutputFileName(startChapter, endChapter);

        try (FileOutputStream fileOutputStream = new FileOutputStream(this.outputFileName)) {
            fileOutputStream.write(outDocument.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

    public Document newOutputDocument() throws IOException{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        try (InputStream in = classLoader.getResourceAsStream("template_output.html")) {
            if(in == null){
                System.err.println("Could not open template output file");
                return null;
            }

            String outputContent = readAllString(in);
            return Jsoup.parse(outputContent);
        }
    }
    public void writeData(Document outputDocument, ChapterContent chapterContent) throws IOException {
        Element containerElement = outputDocument.selectFirst("#container");
        if (containerElement != null)
            containerElement.append(chapterContent.toHtml());
    }
    public boolean craw(int startChapter, int endChapter){
        Document newOutputDocument;
        try {
            newOutputDocument = newOutputDocument();
        } catch (IOException e) {
           System.err.println("Create new output document failed!");
           return false;
        }
        if (newOutputDocument == null){
            System.err.println("Empty output document");
           return false;
        }
        for (int i = startChapter; i <= endChapter; i++){
            String page = COMIC_URL.formatted(i);
            try {
                Document document = connectToPage(page);
                ChapterContent chapterContent = extractContents(document);
                writeData(newOutputDocument, chapterContent);
            } catch (IOException e) {
                System.err.printf(e + "%n", "Cannot connection to page: %s");
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                System.err.println("Failed to sleep");
                throw new RuntimeException(e);
            }
            crawledCounter++;
        }
        try {
            createOutputFile(startChapter, endChapter, newOutputDocument);
        } catch (IOException e) {
            System.err.println("Could not create Output File");
        }
        return true;
    }

    public int getCrawledCounter() {
        return crawledCounter;
    }
}
