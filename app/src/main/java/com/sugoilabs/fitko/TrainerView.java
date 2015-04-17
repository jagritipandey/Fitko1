package com.sugoilabs.fitko;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sugoilabs.helperClasses.UserDb;
import com.sugoilabs.helperClasses.UserInfo;

import java.util.ArrayList;

/**
 * Created by sujeetk on 15-04-2015.
 */
public class TrainerView extends Activity{

    private UserDb userDb;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainerview);
        ListView listView = (ListView)findViewById(R.id.listView1);
        userDb = UserDb.getInstance();
        userDb.Init();
        ArrayList<String> userPhoneNumber = new ArrayList<String>();
        for (int i = 0; i < userDb.numberOfUsers; i++){
            userPhoneNumber.add(userDb.GetUserInfo(i).userName);
        }
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.trainerview_item, R.id.listitem, userPhoneNumber));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView <? > arg0, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                Intent scheduleIntent = new Intent(TrainerView.this, UserElementModify.class);
                startActivity(scheduleIntent);
            }
        });
    }

}
