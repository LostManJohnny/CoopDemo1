package com.example.coopdemo1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.coopdemo1.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<String> userList;
    ArrayAdapter<String> listAdapter;
    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeUserList();

        binding.btnFetchURL.setOnClickListener(view -> new FetchURLData().start());

        binding.btnFetchFile.setOnClickListener(view -> new FetchFileData().start());

        binding.btnReset.setOnClickListener(view -> {
            userList.clear();
            listAdapter.notifyDataSetChanged();
        });

        binding.btnFetchHardCode.setOnClickListener(view -> new FetchHardCode().start());
    }

    private void initializeUserList() {
        userList = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,userList);
        binding.lstvListData.setAdapter(listAdapter);
    }

    //Thread for retrieving JSON data via a URL
    class FetchURLData extends Thread{

        //Data string for all the JSON data
        String data = "";

        @Override
        public void run() {

            //Begins the loading wheel as the thread is processing
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    //Displays the loading wheel
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Fetching URL Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            /*
            ,
            {
              "name": "New",
              "age": 45
            }
            */

            try {
                //Create a URL object with the destination URL
                URL url = new URL("https://api.npoint.io/d72da44c665fcfd752f5");
                //Create a HttpURLConnection and open the url
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //Retrieve the InputStream
                InputStream inputStream = connection.getInputStream();
                //Store in a BufferedReader (makes things smoother)
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                //Instantiate a line string
                String line;

                //Retrieve each line from the buffered reader and store in the data string
                while((line = bufferedReader.readLine()) != null){
                    data += line;
                }

                //Ensure data has content
                if (!data.isEmpty()) {
                    //Convert whole JSON string to JSONObject
                    JSONObject jsonObject = new JSONObject(data);
                    //Retrieve the User JSONArray from the JSONObject
                    JSONArray users = jsonObject.getJSONArray("Users");
                    //Clears the array list
                    userList.clear();

                    //Loops over the JSONArray, storing each name and age into the arraylist
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject names = users.getJSONObject(i);
                        String name = names.getString("name");
                        Integer age = names.getInt("age");
                        userList.add(name + "\n" + age);
                    }
                }
            }
            //Catches any JSON exceptions, places it in the arraylist to be viewed
            catch (JSONException | IOException e) {
                userList.add(e.toString());
            }

            //Closes the loading wheel
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    //Closes the loading wheel if it is currently running
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    //Alerts the listAdapter and binding object that the array list data has changed
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    //Thread for retrieving JSON data via an internal file (in assets)
    class FetchFileData extends Thread{
        //Data string for all the JSON data
        String data = "";

        @Override
        public void run() {

            //Begins the loading wheel as the thread is processing
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    //Displays the loading wheel
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Fetching File Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                //The JSON file stored in the assets folder
                String fileName = "JsonNames.json";

                //Create an InputStream for the JSON file based on the current context assets
                InputStream fis =
                        MainActivity.this.getAssets().open(fileName);
                //A bridge from byte streams to character streams based on a charset
                InputStreamReader inputStreamReader =
                        new InputStreamReader(fis, StandardCharsets.UTF_8);
                //Store in a BufferedReader (makes things smoother)
                BufferedReader reader =
                        new BufferedReader(inputStreamReader);

                //Instantiate a line string
                String line;

                //Retrieve each line from the buffered reader and store in the data string
                while((line = reader.readLine()) != null){
                    data += line;
                }

                //Ensure data has content
                if (!data.isEmpty()) {
                    //Convert whole JSON string to JSONObject
                    JSONObject jsonObject = new JSONObject(data);
                    //Retrieve the User JSONArray from the JSONObject
                    JSONArray users = jsonObject.getJSONArray("Users");
                    //Clears the array list
                    userList.clear();

                    //Loops over the JSONArray, storing each name and age into the arraylist
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject names = users.getJSONObject(i);
                        String name = names.getString("name");
                        Integer age = names.getInt("age");
                        userList.add(name + "\n" + age);
                    }
                }
            }
            //Catches any JSON exceptions, places it in the arraylist to be viewed
            catch (JSONException | IOException e) {
                userList.add(e.toString());
            }

            //Closes the loading wheel if it is currently running
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    //Alerts the listAdapter and binding object that the array list data has changed
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    //Thread for retrieving JSON data via a hardcoded string (please don't do this to yourself)
    class FetchHardCode extends Thread{

        //String of all JSON data
        String data = "";

        @Override
        //Main application of the thread
        public void run() {

            //Hardcoded JSON data
            /*
            * {
            *   "Users":
            *       [
            *           {
            *               "name": "Reynard",
            *               "age": 25
            *           }
            *       ]
            * }
            * */
            String JSON_STRING = "{\"Users\":[{\"name\":\"Reynard\",\"age\":25}]}";

            data = JSON_STRING;

            try {
                //Ensure data has content
                if (!data.isEmpty()) {
                    //Convert whole JSON string to JSONObject
                    JSONObject jsonObject = new JSONObject(data);
                    //Retrieve the User JSONArray from the JSONObject
                    JSONArray users = jsonObject.getJSONArray("Users");
                    //Clears the array list
                    userList.clear();

                    //Loops over the JSONArray, storing each name and age into the arraylist
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject names = users.getJSONObject(i);
                        String name = names.getString("name");
                        Integer age = names.getInt("age");
                        userList.add(name + "\n" + age);
                    }
                }
            }
            //Catches any JSON exceptions, places it in the arraylist to be viewed
            catch (JSONException e) {
                userList.add(e.toString());
            }
//
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    //Alerts the listAdapter and binding object that the array list data has changed
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}