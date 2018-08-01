package amirmh.footballnews.View.Adapter;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;

import amirmh.footballnews.DataType.SkySportsNews;
import amirmh.footballnews.R;

public class LVAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<SkySportsNews> skySportsNewsArrayList;
    private DateTimeFormatter dateTimeFormatter;

    public LVAdapter(Context context, ArrayList<SkySportsNews> skySportsNewsArrayList) {
        this.context = context;
        this.skySportsNewsArrayList = skySportsNewsArrayList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dateTimeFormatter = DateTimeFormat.forPattern("M/d HH:mm");

    }

    @Override
    public int getCount() {
        return skySportsNewsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return skySportsNewsArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_cell, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) view.getTag();

        }
        viewHolder.title.setText(skySportsNewsArrayList.get(i).getTitle());
        viewHolder.desc.setText(skySportsNewsArrayList.get(i).getShortdesc());

        if (skySportsNewsArrayList.get(i).getDateTime() != null) {
            viewHolder.date.setText(dateTimeFormatter.print(skySportsNewsArrayList.get(i).getDateTime()));
        }

        viewHolder.goToWebPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWebPage(skySportsNewsArrayList.get(i).getLink());
            }
        });

        int maxW = Resources.getSystem().getDisplayMetrics().widthPixels;
        int maxH = maxW * 216 / 384;
        Picasso.get().load(skySportsNewsArrayList.get(i).getImgsrc())
                .resize(maxW, maxH)
                .centerCrop(Gravity.TOP)
                .into(viewHolder.imageView);

        return view;
    }
    private void openWebPage(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }
}
