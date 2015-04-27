package com.sugoilabs.fitko;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;


public class Diet extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        RadioButton rb1 = (RadioButton)findViewById(R.id.radioButton1);
        RadioButton rb2 = (RadioButton)findViewById(R.id.radioButton2);
        RadioButton rb3 = (RadioButton)findViewById(R.id.radioButton3);

        final Button nbutton = (Button)findViewById(R.id.button);
        final Button mbutton = (Button)findViewById(R.id.button2);
        final Button pbutton = (Button)findViewById(R.id.button3);
        final TextView txt = (TextView)findViewById(R.id.textview6);

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nbutton.setText("40");
                mbutton.setText("30");
                pbutton.setText("20");
                txt.setText("90");
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nbutton.setText("60");
                mbutton.setText("40");
                pbutton.setText("30");
                txt.setText("130");
            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nbutton.setText("90");
                mbutton.setText("80");
                pbutton.setText("40");
                txt.setText("210");

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diet, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
