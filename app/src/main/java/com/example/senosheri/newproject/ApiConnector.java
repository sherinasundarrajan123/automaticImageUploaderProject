package com.example.senosheri.newproject;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import static android.R.attr.name;

/**
 * Created by Senorita Sundarrajan on 25/02/2019.
 */

public class ApiConnector {
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
