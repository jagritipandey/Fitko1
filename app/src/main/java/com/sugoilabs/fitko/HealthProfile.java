package com.sugoilabs.fitko;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class HealthProfile extends ActionBarActivity {

    private Toolbar toolbar;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthprofile);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        CharSequence[] categories = getResources().getStringArray(R.array.occupation_arrays);
        SetSpinner(spinner, categories);

        SetSpinner((Spinner)findViewById(R.id.PhyActivity_spinner), getResources().getStringArray(R.array.PhyActivity_arrays));
        SetSpinner((Spinner)findViewById(R.id.Stress), getResources().getStringArray(R.array.Stress_arrays));
        SetSpinner((Spinner)findViewById(R.id.StressPersonal_spinner), getResources().getStringArray(R.array.StressPersonal_arrays));
    }

    private void SetSpinner(Spinner spinner, CharSequence[] categories) {
        List<String> stringList = new ArrayList<>();
        for (CharSequence cat : categories)
        {
            stringList.add(cat.toString());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
