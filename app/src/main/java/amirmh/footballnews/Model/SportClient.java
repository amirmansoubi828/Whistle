package amirmh.footballnews.Model;

import java.util.ArrayList;

import amirmh.footballnews.DataType.NewsApiOrg;
import amirmh.footballnews.DataType.SkySportsNews;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SportClient {
    @GET("/sky/getnews/football/v1.0/")
    Call<ArrayList<SkySportsNews>> getSkySportNews();

    @GET("/v2/top-headlines")
    Call<NewsApiOrg> getNewsApiOrgNews(
            @Query("sources") String source
            , @Query("pageSize") int pageSize
            , @Query("apiKey") String apiKey);

    @GET("/v2/everything")
    Call<NewsApiOrg> getEveryThingNewsApiOrgNews(
            @Query("sources") String source
            , @Query("pageSize") int pageSize
            , @Query("apiKey") String apiKey);

}
