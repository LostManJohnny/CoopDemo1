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

        binding.btnFetchHardCode.setOnClickListener(vie -> new FetchHardCode().start());
    }

    private void initializeUserList() {
        userList = new ArrayList<>();
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,userList);
        binding.lstvListData.setAdapter(listAdapter);
    }

    class FetchURLData extends Thread{

        String data = "";

        @Override
        public void run() {

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Fetching URL Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                URL url = new URL("https://api.npoint.io/d72da44c665fcfd752f5");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while((line = bufferedReader.readLine()) != null){
                    data += line;
                }

                if(!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray users = jsonObject.getJSONArray("Users");
                    userList.clear();
                    for (int i = 0; i <users.length(); i++) {
                        JSONObject names = users.getJSONObject(i);
                        String name = names.getString("name");
                        Integer age = names.getInt("age");
                        userList.add(name + "\n" + age);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    class FetchFileData extends Thread{
        String data = "";

        @Override
        public void run() {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Fetching File Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            try {
                String fileName = "JsonNames.json";

                InputStream fis =
                        MainActivity.this.getAssets().open(fileName);
                InputStreamReader inputStreamReader =
                        new InputStreamReader(fis, StandardCharsets.UTF_8);
                BufferedReader reader =
                        new BufferedReader(inputStreamReader);

                String line;

                while ((line = reader.readLine()) != null) {
                    data += line;
                }

                if(!data.isEmpty()){
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray users = jsonObject.getJSONArray("Users");
                    userList.clear();
                    for (int i = 0; i <users.length(); i++) {
                        JSONObject names = users.getJSONObject(i);
                        String name = names.getString("name");
                        Integer age = names.getInt("age");
                        userList.add(name + "\n" + age);
                    }
                }
            } catch (IOException | JSONException e) {
                userList.add(e.toString());
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    class FetchHardCode extends Thread{

        String data = "";

        @Override
        public void run() {
            String JSON_STRING = "{\"Users\":[{\"name\":\"Reynard\",\"age\":25}]}";

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Fetching File Data...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });

            data = JSON_STRING;

            try {
                if (!data.isEmpty()) {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray users = jsonObject.getJSONArray("Users");
                    userList.clear();
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject names = users.getJSONObject(i);
                        String name = names.getString("name");
                        Integer age = names.getInt("age");
                        userList.add(name + "\n" + age);
                    }
                }
            }
            catch (JSONException e) {
                userList.add(e.toString());
            }

            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    listAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}