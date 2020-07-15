package com.example.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener{
  private TextView createtext;
  private ImageView createimage;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        createtext=(TextView)findViewById(R.id.createte);
        createimage=(ImageView) findViewById(R.id.createim);
       createtext.setOnClickListener(this);
        createimage.setOnClickListener(this);
        }

    @Override
    public void onClick(View v) {
      createtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, Form_entry.class));
            }
        });



       createimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, Form_entry.class));
            }
        });

    }
}
