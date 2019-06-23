package amirmh.footballnews.View;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;


import java.util.ArrayList;

import amirmh.footballnews.DataType.SkySportsNews;
import amirmh.footballnews.Logger;
import amirmh.footballnews.Model.RetrofitManager;
import amirmh.footballnews.Notification.NotificationService;
import amirmh.footballnews.Presenter.MainPresenter;
import amirmh.footballnews.R;
import amirmh.footballnews.View.Adapter.LVAdapter;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rListView_main)
    ListView listView;
    private LVAdapter lvAdapter;
    @BindView(R.id.swipe_main)
    SwipeRefreshLayout swipeRefreshLayout;
    private RetrofitManager.Source source;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    private MainPresenter presenter;
    @BindString(R.string.check_connection)
    String checkConnection;
    @BindString(R.string.disconnected)
    String disconnected;

    ///BroadcastReceiver bcr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUpNotificationService();

        if (savedInstanceState != null) {
            Logger.i("source : " + String.valueOf(savedInstanceState.getInt("source")));
            source = RetrofitManager.Source.values()[savedInstanceState.getInt("source")];
            tabLayout.getTabAt(savedInstanceState.getInt("source")).select();
        } else {
            Logger.i("savedInstanceState == null");
            source = RetrofitManager.Source.values()[tabLayout.getSelectedTabPosition()];
        }
        presenter = new MainPresenter();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                source = RetrofitManager.Source.values()[tab.getPosition()];
                getNews();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        if (savedInstanceState != null) {
            updateListView((ArrayList<SkySportsNews>) savedInstanceState.getSerializable("array"));
        } else {
            getNews();
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNews();
            }
        });
    }

    private void getNews() {
        swipeRefreshLayout.setRefreshing(true);
        if (source.equals(RetrofitManager.Source.SkySports)) {
            presenter.getSkySports();
        } else if (source.equals(RetrofitManager.Source.FourFourTwo)) {
            presenter.getFourFourTwo();
        } else if (source.equals(RetrofitManager.Source.BleacherReport)) {
            presenter.getBleacherReport();
        }
    }

    @Subscribe
    public void updateListView(final ArrayList<SkySportsNews> newsArrayList) {
        lvAdapter = new LVAdapter(getApplicationContext(), newsArrayList);
        listView.setAdapter(lvAdapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe
    public void showErr(Throwable throwable) {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, checkConnection, Toast.LENGTH_SHORT).show();
        throwable.printStackTrace();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ///bcr = new BCR();
        ///registerReceiver(bcr, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        presenter.attachView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///unregisterReceiver(bcr);
        presenter.detachView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Logger.i("");
        outState.putInt("source", source.ordinal());
        try {
            outState.putSerializable("array", lvAdapter.getSkySportsNewsArrayList());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setUpNotificationService() {
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }

}
