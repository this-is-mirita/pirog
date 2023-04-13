package com.example.pirog.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pirog.R;

public class AdminMainActivity extends AppCompatActivity {

    private Button add_productBtn, delete_productBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        add_productBtn = findViewById(R.id.admin_add_product);
        delete_productBtn = findViewById(R.id.admin_delete_product);

        add_productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddProduct = new Intent(AdminMainActivity.this,AdminCategoryActivity.class );
                startActivity(intentAddProduct);
            }
        });

        delete_productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentDeleteProduct = new Intent(AdminMainActivity.this, AdminDeleteProductActivity.class);
                startActivity(intentDeleteProduct);
            }
        });
    }
}