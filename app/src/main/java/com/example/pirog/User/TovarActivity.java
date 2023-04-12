package com.example.pirog.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pirog.Interface.UserHelper;
import com.example.pirog.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class TovarActivity extends AppCompatActivity {
    private String productId;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference cartRef = database.getReference("Carts")
            .child(UserHelper.UserData.getId());

    DatabaseReference databaseProducts = database.getReference()
            .child("Products");

    TextView nameTextView, priceTextView, descriptionTextView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tovar);

        nameTextView = findViewById(R.id.product_name);
        priceTextView = findViewById(R.id.product_price);
        descriptionTextView = findViewById(R.id.product_description);

        imageView = findViewById(R.id.select_product_image);

        Intent intent = getIntent();
        if (intent != null)
        {
            productId = intent.getStringExtra("product_id");

            DatabaseReference databaseProduct = databaseProducts.child(productId);

            databaseProduct.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String category = snapshot.child("category").getValue(String.class);
                        String date = snapshot.child("date").getValue(String.class);
                        String description = snapshot.child("description").getValue(String.class);
                        String image = snapshot.child("image").getValue(String.class);
                        String pname = snapshot.child("pname").getValue(String.class);
                        String price = snapshot.child("price").getValue(String.class);
                        String time = snapshot.child("time").getValue(String.class);

                        nameTextView.setText(pname);
                        priceTextView.setText(price);
                        descriptionTextView.setText(description);
                        Picasso.get().load(image).into(imageView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

                // load product details from Firebase using productId
            });
        }

        Button btnAddToCart = findViewById(R.id.btn_add_new_product);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference cartProductsRef = cartRef.child("products").push();
                cartProductsRef.child("productId").setValue(productId);
                cartProductsRef.child("price").setValue(priceTextView.getText());
                cartProductsRef.child("quantity").setValue(1);
                Toast.makeText(TovarActivity.this, "Продукт был добавлен в корзину", Toast.LENGTH_SHORT).show();
            }
        });
    }
}