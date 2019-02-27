package com.example.senosheri.newproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Bitmap bmp;
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

        uploadImage = (ImageView) findViewById(R.id.imgUpload);
        uploadButton = (Button) findViewById(R.id.uploadButton);
        uploadText = (EditText) findViewById(R.id.uploadText);
        downloadImage = (ImageView) findViewById(R.id.imgDownload);
        downloadButton = (Button) findViewById(R.id.downloadButton);
//        downloadText = (TextView) findViewById(R.id.downloadText);
        downloadText = (TextView) findViewById(R.id.downloadText);
        uploadImage.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        downloadButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.imgUpload:
                Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
                break;
            case R.id.uploadButton:


                break;
            case R.id.downloadButton:
                new GetAllCustomersTask().execute(new ApiConnector());


                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data!=null) {
            Uri selectedImage = data.getData();
            uploadImage.setImageURI(selectedImage);
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
            downloadText.setText("Name : "+ name+ " URL : " +url);
            }
        else
        {
            Log.d("jsonArray is null","Error");
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
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            downloadImage.setImageBitmap(bitmap);
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
