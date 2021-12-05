package com.c.retrofit_post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.c.retrofit_post.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    EditText et_Name, et_Trip;
    Button bt_Submit;
    RecyclerView recycler_view;

    String BASE_URL = "https://api.instantwebtools.net/v1/";
    String sName, sTrips;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_Name = findViewById(R.id.et_Name);
        et_Trip = findViewById(R.id.et_Trip);
        bt_Submit = findViewById(R.id.bt_Submit);
        recycler_view = findViewById(R.id.recycler_view);

        getPassenger();

        bt_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sName = et_Name.getText().toString().trim();
                sTrips = et_Trip.getText().toString().trim();

                if (!sName.isEmpty() && !sTrips.isEmpty()) {
                    addPassenger();
                }
            }
        });

    }

    //Api InterFace
    private interface getInter {
        @GET("passenger")
        Call<String> STRING_CALL(
                @Query("page") String page,
                @Query("size") String size
        );
    }

    //Api Method
    private void getPassenger() {
        ProgressDialog dialog = ProgressDialog.show(
                this, "", "Please Wait...", true
        );

        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        getInter inter = retrofit.create(getInter.class);
        Call<String> call = inter.STRING_CALL("756", "25");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                //Always Check This Condition
                if (response.isSuccessful() && response.body() != null) {
                    //When Response is successful and body id not empty
                    dialog.dismiss();
                    //JsonObject
                    try {
                        JSONObject object = new JSONObject(response.body());
                        JSONArray jsonArray = object.getJSONArray("data");
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                                MainActivity.this, 2
                        );
                        recycler_view.setLayoutManager(gridLayoutManager);
                        recycler_view.setAdapter(new MainAdapter(jsonArray));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });
    }

    //Api InterFace
    private interface postInter {
        //Post
        @FormUrlEncoded
        @POST("passenger")
        Call<String> STRING_CALL(
                @Field("name") String name,
                @Field("trips") String trips,
                @Field("airline") String airLine
        );
    }

    //Api Method
    private void addPassenger() {
        //Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        postInter inter = retrofit.create(postInter.class);
        Call<String> call = inter.STRING_CALL(sName, sTrips, "1");
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //Always Check This Condition
                if (response.isSuccessful() && response.body() != null) {
                    //When Response is successful and body id not empty
                    et_Name.getText().clear();
                    et_Trip.getText().clear();
                    getPassenger();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}