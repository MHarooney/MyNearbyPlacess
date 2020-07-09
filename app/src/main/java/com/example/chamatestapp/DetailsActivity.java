package com.example.chamatestapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.chamatestapp.Model.PlaceDetail;
import com.example.chamatestapp.Remote.IGoogleAPIService;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    ImageView photo;
    RatingBar ratingBar;
    TextView openining_hours, place_address, place_name;
    Button btnViewOnMap, btnViewDirections;

    IGoogleAPIService mService;

    PlaceDetail mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mService = Common.getGoogleAPIService();

        photo = (ImageView)findViewById(R.id.photo);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        place_address = (TextView)findViewById(R.id.place_addess);
        place_name = (TextView)findViewById(R.id.place_name);
        openining_hours = (TextView)findViewById(R.id.place_open_hour);
        btnViewOnMap = (Button)findViewById(R.id.btn_show_map);
        btnViewDirections = (Button)findViewById(R.id.btn_view_directions);

        //Empty all view
        place_name.setText("");
        place_address.setText("");
        openining_hours.setText("");

        btnViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getResult().getUrl()));
                startActivity(mapIntent);
            }
        });

        btnViewDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(DetailsActivity.this, ViewDirections.class);
                startActivity(mapIntent);
            }
        });

        //Photo
        if(Common.currentResult.getPhotos() != null && Common.currentResult.getPhotos().length > 0)
        {
            Picasso.with(this)
                    .load(getPhotoOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(), 1000))
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .error(R.drawable.ic_baseline_error_24)
                    .into(photo);
        }
        //Rating
        if (Common.currentResult.getRating() != null && !TextUtils.isEmpty(Common.currentResult.getRating()))
        {
            ratingBar.setRating(Float.parseFloat(Common.currentResult.getRating()));
        }
        else
        {
            ratingBar.setVisibility(View.GONE);
        }
        //Opening hours
        if (Common.currentResult.getOpening_hours() != null)
        {
            openining_hours.setText("Open now: " + Common.currentResult.getOpening_hours().getOpen_now());
        }
        else
        {
            openining_hours.setVisibility(View.GONE);
        }
        //User Service to fetch Address and Name
        mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                        mPlace = response.body();

                        place_address.setText(mPlace.getResult().getFormatted_address());
                        place_name.setText(mPlace.getResult().getName());
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {

                    }
                });
    }

    private String getPlaceDetailUrl(String place_id) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?place_id="+place_id);
        url.append("&key="+getResources().getString(R.string.browser_key));
        return url.toString();
    }

    private String getPhotoOfPlace(String photo_reference, int maxWidth) {
        StringBuilder url = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth="+maxWidth);
        url.append("&photoreference="+photo_reference);
        url.append("&key="+getResources().getString(R.string.browser_key));
        return url.toString();
    }
}