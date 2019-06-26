package amirmh.footballnews.Widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import amirmh.footballnews.DataType.SkySportsNews;
import amirmh.footballnews.R;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 */
public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList<ListItem> listItemList = new ArrayList<ListItem>();
    private Context context = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem() {
        ArrayList<SkySportsNews> news = extractNews(context);
        for (int i = 0; i < news.size(); i++) {
            ListItem listItem = new ListItem();
            listItem.heading = news.get(i).getTitle();
            listItem.content = news.get(i).getShortdesc();
            listItem.image = news.get(i).getImgsrc();
            listItemList.add(listItem);
        }

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.list_row);
        ListItem listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.heading, listItem.heading);
        remoteView.setTextViewText(R.id.content, listItem.content);
        //remoteView.setImageViewResource(R.id.widget_imageView, R.mipmap.whisle);
        /**Picasso.get().load(listItem.image).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                remoteView.setImageViewBitmap(R.id.widget_imageView, bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });***/

        return remoteView;
    }


    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

    private ArrayList<SkySportsNews> extractNews(Context context) {
        ArrayList<SkySportsNews> news = new ArrayList<SkySportsNews>();
        String[] sources = {"skysports", "bleacherreport", "fourfourtwo"};
        for (String source : sources
        ) {
            news.addAll(readNews(context, source));
        }
        return news;
    }

    private ArrayList<SkySportsNews> readNews(Context context, String source) {
        try {
            FileInputStream fileInputStream = context.openFileInput("last_news_" + source);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ArrayList<SkySportsNews> skySportsNews = (ArrayList<SkySportsNews>) objectInputStream.readObject();
            objectInputStream.close();
            return skySportsNews;
        } catch (Exception e) {
            return new ArrayList<SkySportsNews>();
        }
    }

}