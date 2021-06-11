package com.example.testreadcalllog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.example.testreadcalllog.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ContentResolver resolver;
    ArrayList<String>al;
    ArrayAdapter<String>adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        getReadCallLogPermission();
        resolver = getContentResolver();

        Uri callLogUri = CallLog.Calls.CONTENT_URI;
        String col[] = {
                CallLog.Calls.CACHED_NAME,
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE,
                CallLog.Calls.TYPE,
                CallLog.Calls._ID
        };
        Cursor c = resolver.query(callLogUri,col,null,null,null);
        al = new ArrayList<>();
        while(c.moveToNext()){
            String name = "";
            name = c.getString(0);
            if(name == null)
                name = "Unknown";
            String number = c.getString(1);
            String date = c.getString(2);
            SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            String fromattedDate = sd.format(new Date(Long.parseLong(date)));
            int type = c.getInt(3);
            String callType = "";
            if(type == CallLog.Calls.MISSED_TYPE)
                callType = "Missed call";
            else if(type == CallLog.Calls.INCOMING_TYPE)
                callType ="Incoming call";
            else if(type == CallLog.Calls.OUTGOING_TYPE)
                callType = "Outgoing call";
            int id = c.getInt(4);
            String info = id+"\nName : "+name+
                    "\nNumber : "+number+
                    "\nCall : "+callType+
                    "\nDate : "+fromattedDate;
           al.add(info);
        }
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1,al);
        binding.lv.setAdapter(adapter);

    }
    private void getReadCallLogPermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)!= PermissionChecker.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CALL_LOG},111);
        }
    }
}