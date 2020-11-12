package com.mrswapdrobe.swapdrobe;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<String> imageUrls;

    ViewPagerAdapter(Context context, ArrayList<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Glide.with(context).load(imageUrls.get(position)).centerCrop().into(imageView);
        container.addView(imageView);

/*
        Picasso.get()
                .load(imageUrls.get(position))
                .fit()
                //.centerCrop()
                .into(imageView);
        container.addView(imageView);
*/
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}