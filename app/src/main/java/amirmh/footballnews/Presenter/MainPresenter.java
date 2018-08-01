package amirmh.footballnews.Presenter;


import com.squareup.otto.Bus;

import java.util.ArrayList;

import amirmh.footballnews.DataType.NewsApiOrg;
import amirmh.footballnews.DataType.SkySportsNews;
import amirmh.footballnews.Model.RetrofitManager;
import amirmh.footballnews.Model.SportClient;
import amirmh.footballnews.View.MainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {
    private amirmh.footballnews.View.MainActivity view;
    private SportClient sportClient;
    private Bus bus;
    private ConnectionListener connectionListener ;

    private static String apiKey = "003ca25abdcc46d086b1fa46f7a67240";
    private static String fourFourTwo = "four-four-two";
    private static String bleacherReport = "bleacher-report";

    public MainPresenter() {
        bus = new Bus();
        bus.register(this);
    }

    public void attachView(MainActivity view) {
        this.view = view;
        bus.register(this.view);
    }

    public void detachView() {
        bus.unregister(view);
        this.view = null;
    }

    public void getSkySports() {
        sportClient = RetrofitManager.createService(RetrofitManager.Source.SkySports);
        Call<ArrayList<SkySportsNews>> skySportNews = sportClient.getSkySportNews();
        skySportNews.enqueue(new Callback<ArrayList<SkySportsNews>>() {
            @Override
            public void onResponse(Call<ArrayList<SkySportsNews>> call, Response<ArrayList<SkySportsNews>> response) {
                bus.post(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<SkySportsNews>> call, Throwable throwable) {
                bus.post(throwable);
            }
        });

    }

    public void getFourFourTwo() {
        sportClient = RetrofitManager.createService(RetrofitManager.Source.FourFourTwo);
        Call<NewsApiOrg> newsApiOrgNews = sportClient.getNewsApiOrgNews(fourFourTwo, 20, apiKey);
        newsApiOrgNews.enqueue(new Callback<NewsApiOrg>() {
            @Override
            public void onResponse(Call<NewsApiOrg> call, Response<NewsApiOrg> response) {
                bus.post(response.body().getSSNewsArrayList());
            }

            @Override
            public void onFailure(Call<NewsApiOrg> call, Throwable throwable) {
                bus.post(throwable);
            }
        });


    }

    public void getBleacherReport() {
        sportClient = RetrofitManager.createService(RetrofitManager.Source.BleacherReport);
        Call<NewsApiOrg> newsApiOrgNews = sportClient.getEveryThingNewsApiOrgNews(bleacherReport, 20, apiKey);
        newsApiOrgNews.enqueue(new Callback<NewsApiOrg>() {
            @Override
            public void onResponse(Call<NewsApiOrg> call, Response<NewsApiOrg> response) {
                bus.post(response.body().getSSNewsArrayList());
            }

            @Override
            public void onFailure(Call<NewsApiOrg> call, Throwable throwable) {
                bus.post(throwable);
            }
        });

    }

}
