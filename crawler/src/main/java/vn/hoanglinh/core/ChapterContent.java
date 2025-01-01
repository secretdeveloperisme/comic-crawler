package vn.hoanglinh.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChapterContent {
    private String title;
    private List<String> chapterImagesUrls;

    public ChapterContent(String title) {
        this.title = title;
        this.chapterImagesUrls = new ArrayList<>();
    }

    public ChapterContent(String title, List<String> chapterImages) {
        this.title = title;
        this.chapterImagesUrls = chapterImages;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getChapterImagesUrls() {
        return chapterImagesUrls;
    }

    public void setChapterImagesUrls(List<String> chapterImagesUrls) {
        this.chapterImagesUrls = chapterImagesUrls;
    }

    public void addChapterImageUrl(String url){
        this.chapterImagesUrls.add(url);
    }
    public String toHtml(){
        StringBuilder outputString = new StringBuilder();
        outputString.append("<div class='chapter-container'>").append("\n");
        outputString.append("<h1>").append(title).append("</h1>");
        outputString.append("<ul class='chapter-images'>").append("\n");
        for(String chapterImagesUrl: chapterImagesUrls){
            String imgTag = "<img loading=\"lazy\" src=\"%s\"/>".formatted(chapterImagesUrl);
            outputString.append("<li>");
            outputString.append(imgTag).append("\n");
            outputString.append("</li>");
        }
        outputString.append("</ul>").append("\n");
        outputString.append("</div>");

        return outputString.toString();
    }

    public String toJson() {
        String json = """
                    {
                        "title": "%s",
                        "chapterURLs" : [%s]
                    }
                """;
        String chapterURlsStr = chapterImagesUrls.stream().map(s -> "\"" + s + "\"").collect(Collectors.joining(","));
        return json.formatted(this.title, chapterURlsStr);
    }
}
