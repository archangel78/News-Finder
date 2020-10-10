package com.example.newsreport;

public class NewsReports {
    //Stores the title of the News Report
    private String title;
    //Stores Section Name
    private String sectionName;
    //Stores date
    private String datePublished;
    //Stores url of the article
    private String url;
    //Stores author name
    private String author;

    //Public Constructor that initializes the five states
    public NewsReports(String vTitle, String vSectionName, String vDatePublished, String vUrl, String vauthor) {
        title = vTitle;
        sectionName = vSectionName;
        datePublished = vDatePublished;
        url = vUrl;
        if(vauthor.contains("author")) {
            author = vauthor;
        }else author = "author/Not Available";
    }

    //Public Constructor that initializes the four states
    public NewsReports(String vTitle, String vSectionName, String vDatePublished, String vUrl) {
        title = vTitle;
        sectionName = vSectionName;
        datePublished = vDatePublished;
        url = vUrl;
        author = "author/Not Available";
    }

    public String getTitle() {
        return title;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }
}
