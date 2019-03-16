package com.example.senosheri.newproject;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.R.attr.name;
import static android.R.id.input;
public class ApiConnector {

    public JSONArray GetPrevDate(String currentdate)
    {
        String url = "http://10.0.2.2/getPrevDate.php";
        HttpEntity httpEntity = null;
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpPost httpPost = new HttpPost(url);
            // add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("dateinfo", currentdate));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            httpEntity = response.getEntity();

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = null;
        if(httpEntity!=null)
        {
            try{
                String entityResponse = EntityUtils.toString(httpEntity);
                Log.e("Entity Response : ",entityResponse);
                jsonArray = new JSONArray(entityResponse);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return jsonArray;
    }



    public JSONArray GetAllCustomers(){
//        String url = "http://localhost/getAllUsers.php";
        String url="http://10.0.2.2/getAllUsers.php";
        HttpEntity httpEntity = null;
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            httpEntity = httpResponse.getEntity();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray = null;
        if(httpEntity!=null)
        {
            try{
                String entityResponse = EntityUtils.toString(httpEntity);
                Log.e("Entity Response : ",entityResponse);
                jsonArray = new JSONArray(entityResponse);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }
}
