package com.example.pirog.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.pirog.R;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView pirog, tort, ice, ches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        init();

        pirog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "pirognie");
                startActivity(intent);
                finish();
            }
        });

        tort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "torty");
                startActivity(intent);
                finish();
            }
        });

        ice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "ice_cream");
                startActivity(intent);
                finish();
            }
        });

        ches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "cheese");
                startActivity(intent);
                finish();
            }
        });


    }

    private void init() {
        pirog = findViewById(R.id.pirognie);
        tort = findViewById(R.id.torty);
        ice = findViewById(R.id.ice_cream);
        ches = findViewById(R.id.cheese);
    }
}