package com.example.senosheri.newproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ApiConnector2 {


    public JSONArray SetAllFlagsToOne()
    {
        String url = "http://10.0.2.2/setAllFlagsToOne.php";
        HttpEntity resEntity = null;
        try
        {
            DefaultHttpClient httpClient=new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            resEntity = response.getEntity();


        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray5 = null;
        if(resEntity!=null)
        {
            try{
                String entityResponse = EntityUtils.toString(resEntity);
                Log.e("Entity Response : ",entityResponse);
                jsonArray5 = new JSONArray(entityResponse);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray5;
    }






    public JSONArray GetSpecificCustomer(String name) {
        Log.d("Name : ",name);

        String postReceiverUrl = "http://10.0.2.2/getSpecificUserDetails.php";
        HttpEntity resEntity = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            // post header
            HttpPost httpPost = new HttpPost(postReceiverUrl);
            // add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("name", name));
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpClient.execute(httpPost);
            resEntity = response.getEntity();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONArray jsonArray1 = null;
        if(resEntity!= null)
        {
            try{

                String entityResponse = EntityUtils.toString(resEntity);
                Log.e("Entity Response : ",entityResponse);
                jsonArray1 = new JSONArray(entityResponse);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray1;

    }
}
