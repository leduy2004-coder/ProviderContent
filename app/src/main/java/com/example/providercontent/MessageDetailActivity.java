package com.example.providercontent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MessageDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        TextView tvFrom = findViewById(R.id.tvFrom);
        TextView tvMessage = findViewById(R.id.tvMessage);
        Button btnBack = findViewById(R.id.btnBack); // Lấy ID của nút quay về

        String from = getIntent().getStringExtra("from");
        String message = getIntent().getStringExtra("message");

        tvFrom.setText("From: " + from);
        tvMessage.setText("Message: " + message);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
