package amirmh.footballnews.View;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;

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
    ArrayList<String> names;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
        ButterKnife.bind(this);
        names = new ArrayList<String>();
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, names);
        notificationList.setAdapter(arrayAdapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!names.contains(editText.getText().toString())) {
                    arrayAdapter.add(editText.getText().toString());
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

    protected void read() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput("notification_name");
        } catch (FileNotFoundException e) {
            try {
                write();
                return;
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object readObject = objectInputStream.readObject();
        names = (ArrayList<String>) readObject;
        objectInputStream.close();
    }
}
