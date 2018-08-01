package amirmh.footballnews.Model;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    public enum Source {FourFourTwo, SkySports, BleacherReport}

    ;
    private static String SkySports = "https://skysportsapi.herokuapp.com/";
    private static String newsApi = "https://newsapi.org/";
    public static String[] urls = {newsApi, SkySports, newsApi};

    public static SportClient createService(Source source) {


        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(urls[source.ordinal()])
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        return retrofit.create(SportClient.class);
    }
}
