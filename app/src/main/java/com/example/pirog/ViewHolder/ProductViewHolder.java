package com.example.pirog.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.pirog.Interface.itemClickListener;
import com.example.pirog.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;
    public itemClickListener listner;


    public ProductViewHolder(View itemView, itemClickListener listener)
    {
        super(itemView);

        imageView = itemView.findViewById(R.id.product_image);
        txtProductName = itemView.findViewById(R.id.product_name);
        txtProductDescription = itemView.findViewById(R.id.product_description);
        txtProductPrice = itemView.findViewById(R.id.product_price);

        this.listner = listener;
        imageView.setOnClickListener(this);
    }

    public void setItemClickListener(itemClickListener listener)
    {
        this.listner = listener;
    }

    @Override
    public void onClick(View view)
    {
        if (view == imageView)
        {
            listner.onItemClick(getAdapterPosition());
        }
    }
}