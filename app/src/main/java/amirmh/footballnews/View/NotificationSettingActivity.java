package amirmh.footballnews.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import amirmh.footballnews.R;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationSettingActivity extends AppCompatActivity {
    @BindView(R.id.ns_notification_list)
    ListView notificationList;
    @BindView(R.id.ns_edit_name)
    EditText editText;
    @BindView(R.id.ns_btn_add)
    Button add;
    @BindView(R.id.ns_btn_remove)
    Button remove;
    @BindView(R.id.ns_textview_last_notify)
    TextView lastNotify;
    ArrayList<String> names;
    ArrayAdapter<String> arrayAdapter;
    SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
        ButterKnife.bind(this);
        simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        lastNotify.setText("Last Notify : " + readLastNotify());
        names = new ArrayList<String>();
        read();
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, names);
        notificationList.setAdapter(arrayAdapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!names.contains(editText.getText().toString())) {
                    arrayAdapter.insert(editText.getText().toString(), 0);
                    try {
                        write();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayAdapter.remove(editText.getText().toString());
                try {
                    write();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void write() throws IOException {
        FileOutputStream fileOutputStream = openFileOutput("notification_name", MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(names);
        objectOutputStream.close();
    }

    protected void read() {
        try {
            FileInputStream fileInputStream = openFileInput("notification_name");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Object readObject = objectInputStream.readObject();
            names = (ArrayList<String>) readObject;
            objectInputStream.close();
        } catch (Exception e) {
            names = new ArrayList<>();
            names.add("ManUtd");
            names.add("Man Utd");
            names.add("ManchesterUnited");
            names.add("Manchester United");
            names.add("pogba");
            names.add("lukaku");
            try {
                write();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    private String readLastNotify() {
        try {
            FileInputStream fileInputStream = openFileInput("notification_lastdate");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Date lastDate = (Date) objectInputStream.readObject();
            objectInputStream.close();
            return simpleDateFormat.format(lastDate);
        } catch (Exception e) {
            return " ";
        }
    }
}
