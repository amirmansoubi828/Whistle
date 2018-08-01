package amirmh.footballnews.DataType;

import java.util.ArrayList;

public class NewsApiOrg {
    private ArrayList<Article> articles;

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public ArrayList<SkySportsNews> getSSNewsArrayList() {
        ArrayList<SkySportsNews> skySportsNews = new ArrayList<>();
        for (Article article :
                articles) {
            skySportsNews.add(article.toSSNews());
        }
        return skySportsNews;
    }
}
