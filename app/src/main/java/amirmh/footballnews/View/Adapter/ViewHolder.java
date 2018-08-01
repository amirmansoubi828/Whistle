package amirmh.footballnews.View.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import amirmh.footballnews.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewHolder {
    @BindView(R.id.title_cell)
    TextView title;
    @BindView(R.id.desc_cell)
    TextView desc;
    @BindView(R.id.imageView_cell)
    ImageView imageView;
    @BindView(R.id.date_cell)
    TextView date;
    @BindView(R.id.goToWebPage_cell)
    TextView goToWebPage;

    ViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
