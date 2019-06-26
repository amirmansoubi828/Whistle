package amirmh.footballnews.DataType;

import org.joda.time.DateTime;

import java.io.Serializable;

import amirmh.footballnews.Logger;

public class SkySportsNews implements Serializable {
    private String title , link , imgsrc , shortdesc  ;
    private DateTime dateTime ;

    public SkySportsNews(String title, String link, String imgsrc, String shortdesc) {
        this.title = title;
        this.link = link;
        this.imgsrc = imgsrc;
        this.shortdesc = shortdesc;
        this.dateTime = null ;
        if(shortdesc == null){
            this.shortdesc = title;
        }
    }
    public SkySportsNews(String title, String link, String imgsrc, String shortdesc , DateTime dateTime) {
        this.title = title;
        this.link = link;
        this.imgsrc = imgsrc;
        this.shortdesc = shortdesc;
        this.dateTime = dateTime ;
        if(shortdesc == null){
            this.shortdesc = title;
        }
    }

    public SkySportsNews(){
        this.title = "";
        this.link = "";
        this.imgsrc = "";
        this.shortdesc = "";
        this.dateTime = null ;
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
        if(shortdesc == null){
            this.shortdesc = title;
            return;
        }
        this.shortdesc = shortdesc;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
