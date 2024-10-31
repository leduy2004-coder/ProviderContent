package com.example.providercontent;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private ListView listView;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvInfo = findViewById(R.id.tvInfo);
        listView = findViewById(R.id.listView);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, 1);
        } else {
            showAllMessages(); // Gọi phương thức của bạn sau khi có quyền
        }
    }

    private void showAllMessages() {
        ArrayList<String> smsList = new ArrayList<>();
        ArrayList<String> addresses = new ArrayList<>();
        ArrayList<String> bodies = new ArrayList<>();

        Uri uri = Uri.parse("content://sms");
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                smsList.add("From: " + address + "\nMessage: " + body);
                addresses.add(address);
                bodies.add(body);
            } while (cursor.moveToNext());
            cursor.close();
        }


        if (smsList.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy tin nhắn SMS", Toast.LENGTH_SHORT).show();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, smsList);
            listView.setAdapter(adapter);

            // Thiết lập sự kiện click cho từng item
            listView.setOnItemClickListener((parent, view, position, id) -> {
                String selectedAddress = addresses.get(position);
                String selectedBody = bodies.get(position);

                // Chuyển đến Activity chi tiết tin nhắn
                Intent intent = new Intent(MainActivity.this, MessageDetailActivity.class);
                intent.putExtra("from", selectedAddress);
                intent.putExtra("message", selectedBody);
                startActivity(intent);
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showAllMessages(); // Gọi lại phương thức nếu quyền được cấp
            } else {
                Toast.makeText(this, "Cần cấp quyền để truy cập tin nhắn SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
