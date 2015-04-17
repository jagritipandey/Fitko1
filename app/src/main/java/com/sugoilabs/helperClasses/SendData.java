package com.sugoilabs.helperClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sugoilabs.fitko.LoginActivity;
import com.sugoilabs.fitko.R;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.List;

import static android.view.View.*;

/**
 * Created by trath on 1/7/2015.
 */
public class SendData extends AsyncTask <List<NameValuePair>, Void, String>{

    public Context appContext = null;
    public Activity activity = null;
    public ProgressDialog progDailog =null;
    public ProgressBar progressBar=null;


    public SendData(Context c , Activity act){
        appContext = c ;
        activity = act ;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();
        //progDailog = new ProgressDialog(appContext);
        progDailog = new ProgressDialog(activity, AlertDialog.THEME_HOLO_DARK);
        progDailog.setMessage("Sending Login Details");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);

        //progDailog.setBackgroundDrawable(getResources().getDrawable(android.R.drawable.progress_horizontal));


        progDailog.show();

    }

    @Override
    protected String doInBackground(List<NameValuePair>... params) {
        Log.d("params", params[0].toString());

        final HttpParams httpParams = new BasicHttpParams();
        //set connection timeout to 10 seconds
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
        //set data transfer timeout to 30 seconds
        HttpConnectionParams.setSoTimeout(httpParams, 30000);
        HttpClient httpclient = new DefaultHttpClient(httpParams);
        HttpPost httppost = new HttpPost("http://trideeprath.0fees.net/response.php");
        //HttpPost httppost = new HttpPost("http://planourmeet.herokuapp.com/register");
        //HttpPost httppost = new HttpPost("http://planourmeet.com/cgi-bin/register.py");
        try {
            // Add your data
            httppost.setEntity(new UrlEncodedFormEntity(params[0]));
            ResponseHandler<String> responseHandler=new BasicResponseHandler();
            String responseBody = httpclient.execute(httppost, responseHandler);
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
        progDailog.dismiss();
        if(unused.equals("Exception while sending")){
            Toast.makeText(appContext, "Unable to connnect to internet , please retry", Toast.LENGTH_SHORT).show();
        }
        else{

        }

    }

}
