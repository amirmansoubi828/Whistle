package amirmh.footballnews.DataType;

import org.joda.time.DateTime;

public class SkySportsNews {
    private String title , link , imgsrc , shortdesc  ;
    private DateTime dateTime ;

    public SkySportsNews(String title, String link, String imgsrc, String shortdesc) {
        this.title = title;
        this.link = link;
        this.imgsrc = imgsrc;
        this.shortdesc = shortdesc;
        this.dateTime = null ;
    }
    public SkySportsNews(String title, String link, String imgsrc, String shortdesc , DateTime dateTime) {
        this.title = title;
        this.link = link;
        this.imgsrc = imgsrc;
        this.shortdesc = shortdesc;
        this.dateTime = dateTime ;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
