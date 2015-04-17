package com.sugoilabs.fitko;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sugoilabs.helperClasses.LoginType;
import com.sugoilabs.helperClasses.Network;
import com.sugoilabs.helperClasses.TrainerDb;
import com.sugoilabs.helperClasses.UserDb;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import static org.apache.http.params.HttpConnectionParams.setConnectionTimeout;
import static org.apache.http.params.HttpConnectionParams.setSoTimeout;


public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private static String phoneNumber = "phoneNumber";
    private static String password = "password";
    private static SharedPreferences loginDetails ;
    private static SharedPreferences personalDetailsSharedPreference;
    private static String Prefs_LoginDetails = "Prefs_LoginDetails";
    private static String Prefs_PersonalDetails = "Prefs_PersonalDetails";
    private String invalidLoginText="";
    private EditText phoneEditText;
    private EditText passwordEditText;
    private Button buttonLogin;
    private LinearLayout invalidDialogLayout;
    private TextView invalidTextView;
    private ProgressBar progressBar;
    public static LoginType loginType;
    //Two things during debugging has been done i.e shared preference to save login details is erased every time , Response is the actual response rather than response_dummy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // returns a boolean after checking the password and phone number exist or not
        loginDetails = getSharedPreferences(Prefs_LoginDetails,0);

        //Next line just during debugging to clear the shared preferences
        loginDetails.edit().clear().commit();

        boolean isLoggedIn = checkLoginStatus();
        Log.d("isLoggedIn", String.valueOf(isLoggedIn));
        if(isLoggedIn){
            //Call Intent for Workout Activity
            Log.d("Call workout Intent" ,"Call workout Activity");
        }

        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setIndeterminate(true);

        // Get handlers to the actionable views
        phoneEditText = (EditText) findViewById(R.id.phoneText);
        passwordEditText = (EditText) findViewById(R.id.passwordText);
        buttonLogin = (Button) findViewById(R.id.Button);

        //get Handler for the invalidate dialog
        invalidDialogLayout = (LinearLayout) findViewById(R.id.invalidLoginDetails);
        invalidTextView = (TextView) findViewById(R.id.invalidText);

        // Set OnClickListeners to each of them
        phoneEditText.setOnClickListener(this);
        passwordEditText.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);

    }

    public  boolean checkLoginStatus() {
        boolean alreadyLoggedIn = false;
        boolean passwordExists = loginDetails.contains(password);
        boolean phoneNumberExists = loginDetails.contains(phoneNumber);
        Log.d("passwordExists",String.valueOf(passwordExists));
        Log.d("userExists", String.valueOf(phoneNumberExists));
        if(!(passwordExists && phoneNumberExists)){
            alreadyLoggedIn = false;
        }
        else {
            alreadyLoggedIn = true;
        }
        return alreadyLoggedIn;
    }

    private void setLoginSharedPreference(String phone, String pass){
            SharedPreferences.Editor editor = loginDetails.edit();
            editor.putString(phoneNumber, phone);
            editor.putString(password, pass);
            editor.apply();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    @Override
    public void onClick(View v) {
        if( v == phoneEditText){
            Log.d("EditText", "phone");
        }
        if(v == passwordEditText){
            Log.d("EditText", "password");
            //passwordEditText.setHint("");
        }

        if(v==buttonLogin){
            Log.d("LoginButton", "loginButton");
            String phone = phoneEditText.getText().toString();
            String pass = passwordEditText.getText().toString();
            boolean isValid = validateLoginDetails(phone , pass);
            Log.d("isValid",String.valueOf(isValid));
            if(isValid){
                //Send the details to the webserver
                phone = phone.substring(phone.length() -10);
                Network networkAvailable = new Network(getApplicationContext());
                if(networkAvailable.isOnline()){
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("phn", phone));
                    nameValuePairs.add(new BasicNameValuePair("msg",pass));
                    //AsyncTask<List<NameValuePair>, Void, String> Response = new sendLoginData().execute(nameValuePairs);

                    AsyncTask<List<NameValuePair>, Void, String> Response = new sendLoginDataAndroid().execute(nameValuePairs);
                    String response_dummy = null;
                    try {
                        String response = Response.get();
                        Log.d("Async task response", String.valueOf(Response.get()));
                        // response dummy is just for debugging
                        try {
                           response_dummy = createJSONObject(1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("response",response_dummy);
                        Log.d("Login", String.valueOf(isLoginSucess(response_dummy)));
                        if(isLoginSucess(response_dummy)){
                            Log.d("goto Workout activity","Yes");
                            HashMap<String,String> personalDetails = new HashMap<String, String>();
                            //get All personal detail like height , weight if exists
                            personalDetails = getAllPersonalDetails(response_dummy);
                            // save all personal details in shared preference
                            saveDetailsInSharedPreference(personalDetails);
                            Log.d("personalDetails", personalDetails.toString());
                            Log.d("Shared Preference personal details", personalDetailsSharedPreference.getAll().toString());

                            if(loginType == LoginType.User)
                            {
                                //Call intent to schedule Activity
                                Intent scheduleIntent = new Intent(this, Schedule.class);
                                startActivity(scheduleIntent);
                            }
                            else if(loginType == LoginType.Trainer)
                            {
                                Intent scheduleIntent = new Intent(this, TrainerView.class);
                                startActivity(scheduleIntent);
                            }
                        }
                        else{
                            Log.d("wrong login","No");
                            String reason = (String) new JSONObject(response_dummy).get("reason");
                            invalidDialogLayout.setVisibility(View.VISIBLE);
                            invalidTextView.setText(reason);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    invalidDialogLayout.setVisibility(View.VISIBLE);
                    invalidTextView.setText("Unable to connect to Internet");
                }
                Log.d("PhoneNumber", phone);
                Log.d("Password", pass);
            }
            else{
                Log.d("invalid Login Text" , invalidLoginText);
                invalidDialogLayout.setVisibility(View.VISIBLE);
                invalidTextView.setText(invalidLoginText);
            }
        }
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

    private HashMap<String, String> getAllPersonalDetails(String jsonString) throws JSONException {
        HashMap<String, String> personalDetails = new HashMap<String,String>();
        JSONObject jsonObject = new JSONObject(jsonString);
        int length = jsonObject.length();
        for(int i =0 ; i < length-1 ; i++){
            JSONArray keys = jsonObject.names();
            String value = (String) jsonObject.get((String) keys.get(i+1));
            String key = (String) keys.get(i+1);
            personalDetails.put(key , value);
        }
        return personalDetails;
    }


    private boolean isLoginSucess(String response_dummy) throws JSONException {
        boolean loginStatus = false;
        JSONObject json = new JSONObject(response_dummy);
        String response = (String) json.get("response");
        if(response.equalsIgnoreCase("pass")){
            return true;
        }
        else if ( response.equalsIgnoreCase("fail")){
            return false;
        }
        return false;
    }

    private static String createJSONObject(int status) throws JSONException {

        JSONObject json = new JSONObject();
        if( status ==1 ) {
            json.put("response", "Pass");
            json.put("name", "Trideep");
            json.put("age", "21");
            json.put("weight", "83");
            json.put("height", "6'11");
            json.put("GymName", "Spirit");
            json.put("PhoneNumber","9964218671");
            json.put("Pin","1234");
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


    private boolean validateLoginDetails(String phnNo, String pass) {
        boolean isLoginDetailsValid = false;
        boolean isPhoneNumberValid = false;
        boolean isPasswordValid = false;
        String phoneNumber=phnNo;
        String password=pass;

        // Get UserDB
        UserDb userDb = UserDb.getInstance();
        userDb.Init();
        // get TrainerDb
        TrainerDb trainerDb = TrainerDb.getInstance();
        trainerDb.Init();

        String invalidText;
        //PhoneNumber entered and is valid
        if(userDb.CheckCredentials(phnNo, pass) ){
            Log.d("PhoneNumber",phoneNumber);
            isPhoneNumberValid = true;
            isPasswordValid = true;
            loginType = LoginType.User;
        }
        else if (trainerDb.CheckCredentials(phnNo, pass))
        {
            Log.d("PhoneNumber",phoneNumber);
            isPhoneNumberValid = true;
            isPasswordValid = true;
            loginType = LoginType.Trainer;
        }
        if(phoneNumber == null || phoneNumber.isEmpty() || phoneNumber.length() < 10){
            invalidLoginText = "Phone Number field should contain 10 characters";
        }
        else if(password == null || password.isEmpty()){
            invalidLoginText = "Password field is empty";
        }
        else {
            invalidLoginText = userDb.getInvalidText();
        }

        Log.d("isPhoneNumberValid" , String.valueOf(isPhoneNumberValid));
        Log.d("isPasswordValid", String.valueOf(isPasswordValid));
        isLoginDetailsValid = isPhoneNumberValid && isPasswordValid;
        return isLoginDetailsValid;
    }

    public void setVisibility(int visibility){
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        if(visibility ==1){
            progressBar.setVisibility(View.VISIBLE);
            int visibility1 = progressBar.getVisibility();
            Log.d("visbility should be visible ",String.valueOf(visibility1)+ " " + timestamp + " " + String.valueOf(progressBar.isShown()));

        }
        else{
            progressBar.setVisibility(View.INVISIBLE);
            int visibility1 = progressBar.getVisibility();
            Log.d("visbility should be invisible",String.valueOf(visibility1) + " " + timestamp + " " + String.valueOf(progressBar.isShown()));
        }

    }

    private class sendLoginData extends AsyncTask<List<NameValuePair>, Void, String>{

        private ProgressDialog progDailog;
        HttpClient httpClient;


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            setVisibility(1);
            //progDailog = new ProgressDialog(appContext);
            /*
            progDailog = new ProgressDialog(LoginActivity.this, AlertDialog.THEME_HOLO_DARK);
            progDailog.setMessage("Sending Login Details");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progDailog.setCancelable(true);
            progDailog.show();
            */
        }

        @Override
        protected String doInBackground(List<NameValuePair>... params) {


            System.out.println(params[0].toString());
            Log.d("params",params[0].toString());

            final HttpParams httpParams = new BasicHttpParams();
            //set connection timeout to 10 seconds
            setConnectionTimeout(httpParams, 10000);
            //set data transfer timeout to 30 seconds
            setSoTimeout(httpParams, 30000);
            httpClient = new DefaultHttpClient(httpParams);
            //HttpPost httppost = new HttpPost("http://trideeprath.0fees.net/response.php");
            HttpConnectionParams.setConnectionTimeout(this.httpClient.getParams(), 30000);
            HttpConnectionParams.setSoTimeout(this.httpClient.getParams(), 50000);
            HttpPost httppost = new HttpPost("http://planourmeet.herokuapp.com/register");
            //HttpPost httppost = new HttpPost("http://planourmeet.com/cgi-bin/register.py");
            try {
                // Add your data
                httppost.setEntity(new UrlEncodedFormEntity(params[0]));
                ResponseHandler<String> responseHandler=new BasicResponseHandler();
                String responseBody = httpClient.execute(httppost, responseHandler);

                Log.d("response", responseBody);
                return responseBody;

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                return "Exception while sending";
            } catch(ConnectTimeoutException cte ){
                Log.d("connection timeout" , "Network problem" );
                return "Exception while sending";

            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.d("connection timeout 2" , "Network problem" );
                return "Exception while sending";

            }


        }

        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);
            Log.d("result", unused);
            //progDailog.dismiss();
            setVisibility(0);
            if(unused.equals("Exception while sending")){
                Toast.makeText(getApplicationContext(), "Unable to connnect to internet , please retry", Toast.LENGTH_SHORT).show();
            }
            else{
            }

        }
    }

    private class sendLoginDataAndroid extends AsyncTask<List<NameValuePair>, Void , String> {

        @Override
        protected void onPreExecute() {
            setVisibility(1);


        }
        @Override
        protected String doInBackground(List<NameValuePair>... loginDetail) {
            AndroidHttpClient client = AndroidHttpClient.newInstance("AndroidAgent");
            HttpPost httpPost = new HttpPost("http://trideeprath.0fees.net/response.php");
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
            setVisibility(0);
            if(result.equals("Exception while sending")){
                Toast.makeText(getApplicationContext(), "Unable to connnect to internet , please retry", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
