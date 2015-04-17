package com.sugoilabs.fitko;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sugoilabs.helperClasses.Network;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class Schedule extends ActionBarActivity {

    private Toolbar toolbar;
    private String[] navigation_items;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private Intent workout;
    Context context;
    NavigationDrawerFragment navigationDrawerFragment;
    private ListView excerciseListView;
    private ListView InstructionListView;
    String[] excercises;
    private static String Prefs_WorkoutDetails = "Prefs_WorkoutDetails";
    private static SharedPreferences WorkoutDetails;
    private static String Prefs_PersonalDetails = "Prefs_PersonalDetails";
    private static SharedPreferences personalDetailsSharedPreference;
    private ProgressDialog progress;
    private TextView DateTime;
    private Button attendanceButton;
    private static boolean attendanceMade;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.schedule);
        //Setup action bar
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Setup navigation drawer
        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        navigationDrawerFragment.setup(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawer_layout), toolbar);

        //Setup up TimeStamp TextView
        DateTime = (TextView) findViewById(R.id.todayDayDate);
        Calendar calendar = new GregorianCalendar();
        String currentTimestamp = getDay(calendar.get(calendar.DAY_OF_WEEK),calendar.get(calendar.MONTH), calendar.get(calendar.DATE));
        DateTime.setText(currentTimestamp);


        //Setup Shared Preference
        WorkoutDetails = getSharedPreferences(Prefs_WorkoutDetails,0);
        WorkoutDetails.edit().clear().commit();

        //Set Attendance Button and it's on Click Listener
        attendanceButton = (Button) findViewById(R.id.atttendanceButton);
        attendanceButton.setOnClickListener(attendanceButtonOnClickListener);

        if (attendanceMade){
            try {
                getWorkoutDetailsfromWeb();
                attendanceButton.setVisibility(View.INVISIBLE);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    public View.OnClickListener attendanceButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(new Network(getApplicationContext()).isOnline()){
                attendanceMade = true;
                Log.d("Online" , "true");
                if(isWorkoutDetailsEmpty()){
                    Log.d("workout","empty");
                    try {
                        //getWorkoutDetailsFromWeb()
                        getWorkoutDetailsfromWeb();
                        attendanceButton.setVisibility(View.INVISIBLE);

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{

                }
            }

        }
    };


    private String getDay(int d,int m, int dt) {
        String day = null;
        String month = null;
        String date = String.valueOf(dt);
        switch (d){
            case 1: day = "Sunday";
                    break;
            case 2: day = "Monday";
                    break;
            case 3: day = "Tuesday";
                    break;
            case 4: day = "Wednesday";
                    break;
            case 5: day = "Thursday";
                    break;
            case 6: day = "Friday";
                    break;
            case 7: day = "Saturday";
                    break;

        }

        switch (m){
            case 0: month ="January";
                    break;
            case 1: month ="February";
                    break;
            case 2: month ="March";
                    break;
            case 3: month="April";
                    break;
            case 4: month="May";
                    break;
            case 5: month="June";
                    break;
            case 6: month="July";
                    break;
            case 7: month="August";
                    break;
            case 8: month="September";
                    break;
            case 9: month="October";
                    break;
            case 10: month="November";
                    break;
            case 11: month="December";
                    break;
        }
        return day +", " + month + " " + date;

    }



    private void setDetailstoListView() {
        //Get From Shared Preference
        Set<String> abc = null;
        Set<String> excercisesSet = WorkoutDetails.getStringSet("Excercises", abc);
        Set<String> instructionsSet = WorkoutDetails.getStringSet("Instructions",abc);
        ArrayList<String> excerciseList = new ArrayList<String>();
        ArrayList<String> instructionList = new ArrayList<String>();
        for (String str : excercisesSet)
            excerciseList.add(str);
        for (String str : instructionsSet)
            instructionList.add(str);

        for(String str : excerciseList)
            Log.d("excercise from Shared",str);

        for(String str : instructionList)
            Log.d("instruction from Shared",str);

        //Set to List View
        excerciseListView = (ListView) findViewById(R.id.excerciseListView);
        //String[] stringArray = list.toArray(new String[list.size()]);
        final String[] excercises = (String[]) excerciseList.toArray(new String[excerciseList.size()]);
        //Integer[] iconId = {R.drawable.ic_action_accept, R.drawable.ic_action_accept,R.drawable.ic_action_accept, R.drawable.ic_action_accept, R.drawable.ic_action_accept,R.drawable.ic_action_accept, R.drawable.ic_action_accept, R.drawable.ic_action_accept,R.drawable.ic_action_accept};
        ArrayList<Integer> iconIdList = new ArrayList<Integer>();
        for(int i =0 ; i< excercises.length ; i++){
            iconIdList.add(R.drawable.ic_action_accept);
        }
        final Integer[] iconId = (Integer[]) iconIdList.toArray(new Integer[iconIdList.size()]);
        ExcerciseListAdapter excerciseAdapter = new ExcerciseListAdapter(Schedule.this,excercises , iconId);
        excerciseListView.setAdapter(excerciseAdapter);


        InstructionListView = (ListView) findViewById(R.id.instructionListView);
        String[] Instruction = (String[]) instructionList.toArray(new String[instructionList.size()]);
        InstructionListAdapter instructionListAdapter = new InstructionListAdapter(Schedule.this, Instruction);
        InstructionListView.setAdapter(instructionListAdapter);


    }

    private void getWorkoutDetailsfromWeb() throws ExecutionException, InterruptedException {
        personalDetailsSharedPreference = getSharedPreferences(Prefs_PersonalDetails,0);
        if( personalDetailsSharedPreference.contains("PhoneNumber") && personalDetailsSharedPreference.contains("Pin")){
            String PhoneNumber= personalDetailsSharedPreference.getString("PhoneNumber","nothing");
            String Pin = personalDetailsSharedPreference.getString("Pin","nothing");
            Log.d("Pers details", PhoneNumber + Pin);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("phn","PhoneNumber" ));
            nameValuePairs.add(new BasicNameValuePair("msg","Pin"));
            //AsyncTask<List<NameValuePair>, Void, String> Response = new sendLoginData().execute(nameValuePairs);
            AsyncTask<List<NameValuePair>, Void, String> httpResponse = new receiveWorkoutDataAndroid().execute(nameValuePairs);


        }
    }

    private boolean isWorkoutDetailsEmpty() {
        boolean status=false;
        if(WorkoutDetails.getAll().size() == 0){
            status = true;
        }
        return status;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
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
        if( id == R.id.action_update){
            Toast.makeText(this,"yohhoo updating" , Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data.hasExtra("Attendance")) {
                Toast.makeText(this, data.getExtras().getString("Attendance"),Toast.LENGTH_SHORT).show();
                Log.d("Attendance", data.getExtras().getString("Attendance"));
            }
        }
    }


    @Override
    public void onBackPressed() {
        // do nothing

    }

    private static void parseAndSaveWorkoutDetailsfromJson(String jsonResponse) throws  JSONException{
        //Parse the string
        JSONObject jsonObject = new JSONObject(jsonResponse);
        String ReceivedDayNo = jsonObject.getString("DayNo");
        String ReceivedDate = jsonObject.getString("Date");
        ArrayList<String> excercises = new ArrayList<String>();
        JSONArray ReceivedExcercisesJsonArray = jsonObject.getJSONArray("Excercise");
        for(int i =0 ; i< ReceivedExcercisesJsonArray.length(); i++){
            excercises.add((String) ReceivedExcercisesJsonArray.get(i));
        }
        ArrayList<String> instructions = new ArrayList<String>();
        JSONArray ReceivedInstructionJsonArray = jsonObject.getJSONArray("Instruction");
        for(int i =0 ; i< ReceivedInstructionJsonArray.length(); i++){
            instructions.add((String) ReceivedInstructionJsonArray.get(i));
        }
        Log.d("Parsed JSON" , ReceivedDayNo + ReceivedDate + excercises.toString() + instructions.toString());


        //Save to SharedPreference
        SharedPreferences.Editor editor = WorkoutDetails.edit();
        editor.putString("DayNo", ReceivedDayNo);
        editor.putString("Date", ReceivedDate);
        Set<String> setEx = new HashSet<>(excercises);
        editor.putStringSet("Excercises",setEx);
        Set<String> setIn = new HashSet<>(instructions);
        editor.putStringSet("Instructions",setIn);
        editor.commit();


    }

    private void saveDetailsInSharedPreference(HashMap<String, String> personalDetails) {
        personalDetailsSharedPreference = getSharedPreferences(Prefs_PersonalDetails , 0);
        SharedPreferences.Editor editor = personalDetailsSharedPreference.edit();
        Set<String> keys = personalDetails.keySet();
        Object[] keysString = keys.toArray();
        for(int i =0 ; i <personalDetails.size() ; i++) {
            editor.putString(keysString[i].toString() , personalDetails.get(keysString[i]));
        }
        editor.commit();
    }

    private static String createJSONObject(int status) throws JSONException {

        JSONObject json = new JSONObject();
        JSONArray excercises = new JSONArray();
        JSONArray instruction = new JSONArray();
        if( status ==1 ) {
            json.put("DayNo", "21");
            json.put("Date", "23012015");
            json.put("Attendance", "No");
            excercises.put("Running");
            excercises.put("Bench Press");
            excercises.put("Squating");
            excercises.put("Stretching");
            excercises.put("Crunches");
            excercises.put("Sit ups");
            json.put("Excercise",excercises);
            instruction.put("Running for 15mins");
            instruction.put("Each Reps 10 times");
            json.put("Instruction",instruction);
        }
        else if (status == 2){
            json.put("response","Fail");
            json.put("reason", "PhoneNumber does not exist");
        }
        else if (status == 3){
            json.put("response" ,"Fail");
            json.put("reason" , "Incorrect Pin");
        }
        return json.toString();
    }

    private class receiveWorkoutDataAndroid extends AsyncTask<List<NameValuePair>, Void , String> {

        private AndroidHttpClient client;
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Schedule.this, "Loading","Loading Workout Details", true);
        }
        @Override
        protected String doInBackground(List<NameValuePair>... loginDetail) {
            client = AndroidHttpClient.newInstance("AndroidAgent");
            HttpPost httpPost = new HttpPost("http://trideeprath.0fees.net/response.php");
            HttpConnectionParams.setConnectionTimeout(this.client.getParams(), 30000);
            HttpConnectionParams.setSoTimeout(this.client.getParams(), 50000);
            //HttpPost httpPost = new HttpPost("http://planourmeet.herokuapp.com/register");
            Log.d("background","inBackground start");
            HttpResponse response;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(loginDetail[0]));
                response = client.execute(httpPost);
                return EntityUtils.toString(response.getEntity());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                client.close();
                Log.d("background","inBackground end");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("result", result);
            progress.dismiss();
            if(result!=null){
                String result_dummy=null;
                //this is just for debugging, after webservice is created pass the result variable to parseAndSave() function
                try {
                    result_dummy = createJSONObject(1);
                    parseAndSaveWorkoutDetailsfromJson(result_dummy);
                    setDetailstoListView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
                //Set Error list View
                Toast.makeText(context, "Unable to connnect to internet, please retry", Toast.LENGTH_SHORT).show();

            }
        }
    }


}

