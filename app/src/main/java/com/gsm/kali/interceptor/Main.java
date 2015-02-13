package com.gsm.kali.interceptor;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class Main extends ActionBarActivity {
    static EditText edit_debug;
    Button btngo,btnclear,btndump;
    Boolean isrunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_debug = (EditText)findViewById(R.id.edit_debug);
        btngo = (Button)findViewById(R.id.btngo);
        btnclear = (Button)findViewById(R.id.btnclear);
        btndump = (Button)findViewById(R.id.btndump);




        btndump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    writeToFile(edit_debug.getText().toString());
                    Toast.makeText(getBaseContext(),"File saved to"+getApplicationContext().getFilesDir().getAbsolutePath(),
                            Toast.LENGTH_SHORT).show();
                }catch (Exception ee){
                    Toast.makeText(getBaseContext(), "File not saved error=" + ee,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //writeToFile(edit_out1.getText().toString());
                edit_debug.setText("");
            }
        });

        btngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isrunning) {
                    isrunning = true;
                    btngo.setText("X");
                    LowLevelSmsInterceptor.setRUNNING(true);
                    new LowLevelSmsInterceptor().execute("");

                }else
                {
                    //myclass.stop();
                    LowLevelSmsInterceptor.setRUNNING(false);
                    isrunning = false;
                    btngo.setText("Go");
                }
            }
        });
    }

    private void writeToFile(String data) {
        String path = getApplicationContext().getFilesDir().getAbsolutePath();
        File file = new File(path + "/Intercetpor_debug_data.txt");
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(file.getName(), Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
