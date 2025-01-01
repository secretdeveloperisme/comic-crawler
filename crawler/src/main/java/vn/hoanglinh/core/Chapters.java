package vn.hoanglinh.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Chapters {
    private int startChapter;
    private int endChapter;
    private List<ChapterContent> chapters;

    public Chapters(int startChapter, int endChapter) {
        this.startChapter = startChapter;
        this.endChapter = endChapter;
        this.chapters = new ArrayList<>();
    }

    public int getStartChapter() {
        return startChapter;
    }

    public void setStartChapter(int startChapter) {
        this.startChapter = startChapter;
    }

    public int getEndChapter() {
        return endChapter;
    }

    public void setEndChapter(int endChapter) {
        this.endChapter = endChapter;
    }

    public List<ChapterContent> getChapters() {
        return chapters;
    }

    public void setChapters(List<ChapterContent> chapters) {
        this.chapters = chapters;
    }

    public void addChapter(ChapterContent chapterContent){
        this.chapters.add(chapterContent);
    }

    public String toJson() {
        String json = """
                    {
                        "startChapter" : %d,
                        "endChapter" : %d,
                        "chapterContents": [%s]
                    }
                """;
        String chapterContents = chapters.stream().map(ChapterContent::toJson).collect(Collectors.joining(",\n"));

        return json.formatted(startChapter, endChapter, chapterContents);
    }
}
