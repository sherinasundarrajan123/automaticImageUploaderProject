package com.example.senosheri.newproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Bitmap bmp;
    int holder;
    URL url1 = null;
    ImageView uploadImage,downloadImage;
    Button uploadButton,downloadButton;
    EditText uploadText;
    TextView downloadText;
    String name = "";
    String url = "";
    private static final int RESULT_LOAD_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadImage = (ImageView) findViewById(R.id.imgDownload);
        downloadButton = (Button) findViewById(R.id.downloadButton);
        downloadText = (TextView) findViewById(R.id.downloadText);
        new GetDate().execute(new ApiConnector());
//        new GetAllCustomersTask().execute(new ApiConnector());
        downloadButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
                Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
        name = "";
        url = "";
        downloadText.setText(name);
//                new GetAllCustomersTask().execute(new ApiConnector());
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data!=null) {
            Uri selectedImage = data.getData();
            downloadImage.setImageURI(selectedImage);
        }
    }

    public void setTextToTextView (JSONArray jsonArray)
    {
        String s="";
//        String name = "";
//        getMatch newItemObject = null;
        if(jsonArray!=null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = null;
//                Log.d("Heyyy : ",json.getString(i));
                try {
                    json = jsonArray.getJSONObject(i);
                    s = s+ json.getString("flag");
                    Log.d("s:",s);
                    if(s.equals("0")) {

                        name = name + json.getString("name");
                        Log.d("enters: ",name);
                        break;
                    }
                    s="";
//                    s = s + "Name : " + json.getString("name") + "\n" + json.getString("flag") + "\n\n";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(!name.equals(""))
            {
                new GetSpecificCustomer().execute(new ApiConnector2());
            }
            else
            {
                new SetAllFlags().execute(new ApiConnector2());
            }
//            downloadText.setText(name);
        }
        else
        {
            Log.d("jsonArray is null","Error");
        }
//        downloadImage.setImageURI("");
    }

    public void setTextAgain(JSONArray jsonArray) {
        String flag = "";
//        String name = "";
//        getMatch newItemObject = null;
        if (jsonArray != null) {
            for (int j = 1; j < jsonArray.length(); j++) {
                JSONObject json1 = null;
                try {
                    json1 = jsonArray.getJSONObject(j);
                    flag = flag + json1.getString("flag");
                    if (flag.equals("0")) {
                        url = url + json1.getString("url");
                        Log.d("enters: ", url);
                        new GetImage().execute();
                        break;
                    }
                    flag = "";

//                downloadImage.setImageBitmap(bmp);
//                        break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            downloadText.setText((int) System.currentTimeMillis());

            }
        else
        {
            Log.d("jsonArray is null","Error");
        }

    }

    private class GetDate extends AsyncTask<ApiConnector,Long,Integer>
    {
        @Override
        protected Integer doInBackground(ApiConnector... apiConnectors) {
            int flag=0;
            long yourmilliseconds = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date resultdate = new Date(yourmilliseconds);
            String currentDate = sdf.format(resultdate);
            String cYear = "";
            String cMonth = "";
            String cDate = "";
            cYear = currentDate.substring(currentDate.length() - 4);
            cMonth = currentDate.substring(3,5);
            cDate = currentDate.substring(0,2);
            String prevDate = "";
            JSONArray json3 = apiConnectors[0].GetPrevDate(currentDate);
            if (json3 != null) {
                JSONObject json1 = null;
                try {
                    json1 = json3.getJSONObject(0);
                    prevDate = prevDate + json1.getString("dateinfo");
                    url = url + json1.getString("url");
                    name = name +json1.getString("name");
                    String pYear = "";
                    String pMonth = "";
                    String pDate = "";

                    pYear = prevDate.substring(prevDate.length() - 4);
                    pMonth = prevDate.substring(3, 5);
                    pDate = prevDate.substring(0, 2);
//                    int flag = 0;
                    if (pYear.equals(cYear) && pMonth.equals(cMonth) && pDate.equals(cDate)) {
                        holder = 0;
                    } else
                        holder = 1;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//


            return holder;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(holder == 0)
            {
                new GetImage().execute(url);

            }
            else
            {
                url="";
                name="";
                new GetAllCustomersTask().execute(new ApiConnector());
            }


        }
    }

    private class GetImage extends AsyncTask<String,Void,Bitmap>
    {

        @Override
        protected Bitmap doInBackground(String... strings) {

            try {
                url1 = new URL("http://10.0.2.2/face-recognition-opencv/"+url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                bmp = BitmapFactory.decodeStream(url1.openConnection().getInputStream());
                String url3 = "http://10.0.2.2/setDateInfoUrl.php";
                HttpEntity httpEntity = null;
                try
                {
                    DefaultHttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url3);
                    // add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("url", url));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    httpEntity = response.getEntity();

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            downloadImage.setImageBitmap(bitmap);
            downloadText.setText("Name : "+ name+ " URL : " +url);

            url = "";
            name = "";
        }
    }


    private class SetAllFlags extends AsyncTask<ApiConnector2,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector2... apiConnector2s) {
            return apiConnector2s[0].SetAllFlagsToOne();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            setTextToTextView(jsonArray);
        }
    }

    private class GetSpecificCustomer extends AsyncTask<ApiConnector2,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector2... apiConnector2s) {
            return apiConnector2s[0].GetSpecificCustomer(name);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            setTextAgain(jsonArray);
        }
    }

    private class GetAllCustomersTask extends AsyncTask<ApiConnector,Long,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(ApiConnector... apiConnectors) {
            return apiConnectors[0].GetAllCustomers();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            setTextToTextView(jsonArray);

        }
    }
}
