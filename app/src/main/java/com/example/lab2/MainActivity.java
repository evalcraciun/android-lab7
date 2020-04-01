package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    String[] names = {"Item1", "Item2", "Item3"};
    ListView listView;
    TextView textView;
    Button btn;
    List<String> listOfItems = Arrays.asList(names);
    String txt;
    public static final String EXTRA_MESSAGE = "EXTRA MESSAGE";
    AppDatabase db;
    SharedPreferences sharedPreferences;
    boolean test;
    String savedName;
    String savedUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_item);
        textView = findViewById(R.id.textView2);

        ArrayAdapter<String> arrAdapted = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listOfItems);
        listView.setAdapter(arrAdapted);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textView.setText(listOfItems.get(position));
            }
        });

        if (savedInstanceState != null) {
            txt = (String) textView.getText();
        }

        //get an instance of Rom Db created
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "User").build();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        test = sharedPreferences.getBoolean("test", false);
        savedName = sharedPreferences.getString("edit_text_preference_1", "Craciun Stefan");
        savedUserEmail = sharedPreferences.getString("edit_text_preference_2", "test@gmail.com");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_example, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.send_msg:
                sendMessage(item.getTitle().toString());
                break;
            case R.id.goTo:
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setData(Uri.parse("mailto:")); // only email apps should handle this
                email.putExtra(Intent.EXTRA_SUBJECT, "Hello Email");
                email.putExtra(Intent.EXTRA_TEXT, "Test message");
                //need this to prompts email client only
                email.setType("text/*");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
                break;
            case R.id.login:
                //create intent here
                addModal();
            case R.id.settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            case R.id.save_settings:
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        User user = new User(savedName, savedUserEmail);
                        String name = user.getName();
                        String email = user.getEmail();
                        System.out.println("######################---------########### TRUE \n" + name + " " +email);
                        db.userDao().insert(new User(name, email));
                    }
                });
                break;
            case R.id.sensor_map:
                Intent intent = new Intent(this, SensorActivity.class);
                startActivity(intent);
                break;
            case R.id.camera:
                Intent surface = new Intent(this, SurfaceActivity.class);
                startActivity(surface);
                break;
            default:
                System.out.println("Default option");
                break;
        }

        return true;
    }

    /** Called when the user taps the send message */
    public void sendMessage(String title) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, title);
        startActivity(intent);
    }

    public void addModal() {
        // Build an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Set a title for alert dialog
        builder.setTitle("Login Dialog");
        // Ask the final question
        builder.setMessage("We need to add some input fields?");

        // Set click listener for alert dialog buttons
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //yes
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        // No
                        break;
                }
            }
        };

        // Set the alert dialog yes button click listener
        builder.setPositiveButton("Yes", dialogClickListener);

        // Set the alert dialog no button click listener
        builder.setNegativeButton("No",dialogClickListener);

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();
    }



    // II
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("lastItem", (String) textView.getText());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String myString = savedInstanceState.getString("lastItem");
        textView.setText(myString);
    }


    //I
    @Override
    protected void onStart() {
        super.onStart();
        //Log.d("MAIN:", "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.d("MAIN:", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d("MAIN:", "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.d("MAIN:", "onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
       // Log.d("MAIN:", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MAIN:", "onRestart");
    }
}
