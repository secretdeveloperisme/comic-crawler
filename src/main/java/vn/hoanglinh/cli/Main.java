package vn.hoanglinh.cli;


import vn.hoanglinh.core.Crawler;
import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) {
        Options options = new Options();

        Option startChapterOpt = new Option("s", "start", true, "start chapter number");
        startChapterOpt.setRequired(true);
        startChapterOpt.setType(Integer.class);
        options.addOption(startChapterOpt);

        Option endChapterOpt = new Option("e", "end", true, "end chapter number");
        endChapterOpt.setRequired(true);
        endChapterOpt.setType(Integer.class);
        options.addOption(endChapterOpt);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("ComicCrawler", options);
            System.exit(1);
        }
        int startChapter, endChapter;
        try {
            startChapter = cmd.getParsedOptionValue("start", 1);
            endChapter = cmd.getParsedOptionValue("end", 10);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if(startChapter <= 0){
            System.err.println("start value must be greater than zero");
            return;
        }
        if(endChapter <= 0){
            System.err.println("end value must be greater than zero");
            return;
        }
        if (startChapter > endChapter){
            System.err.println("end value must be greater than start value");
            return;
        }

        if(endChapter - startChapter > 100){
            System.err.println("The number of chapters exceeds 100 chapters");
            return;
        }

        Crawler crawler = new Crawler();
        if (crawler.craw(startChapter, endChapter)){
            System.out.printf("Crawls comic chapters from %d to %d successfully%n", startChapter, endChapter );
            System.out.printf("%d chapters are crawled and output file: %s%n", crawler.getCrawledCounter(), crawler.getOutputFileName());
        }else {
            System.out.println("Crawls comic chapters failed");
        }
    }
}
