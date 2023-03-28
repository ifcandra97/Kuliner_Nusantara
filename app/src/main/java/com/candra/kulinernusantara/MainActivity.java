package com.candra.kulinernusantara;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.candra.kulinernusantara.API.APIRequestData;
import com.candra.kulinernusantara.API.RetroServer;
import com.candra.kulinernusantara.Adapter.AdapterKuliner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    private RecyclerView rvKuliner;
    private ProgressBar pbLoading;
    private RecyclerView.Adapter rvAdapterKuliner;
    private RecyclerView.LayoutManager rvLayoutKuliner;
    private List<ModelKuliner> listKuliner = new ArrayList<>();
    private FloatingActionButton fabTambahKuliner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvKuliner = findViewById(R.id.rv_kuliner);
        pbLoading = findViewById(R.id.pb_loading);
        fabTambahKuliner = findViewById(R.id.fab_tambahkuliner);
        fabTambahKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateActivity.class));
            }
        });

        rvKuliner.setHasFixedSize(true);
        /*Performance*/
        rvKuliner.setItemViewCacheSize(24);
        rvKuliner.setDrawingCacheEnabled(true);
        rvKuliner.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

//        recyclerView.setLayoutManager(new GridLayoutManager(CookingRecipes.this, 2));?
        rvLayoutKuliner = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvKuliner.setLayoutManager(rvLayoutKuliner);

    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveKuliner();
    }

    public void retrieveKuliner()
    {
        // Membuat Loading Progress muncul
        pbLoading.setVisibility(View.VISIBLE);

        // Membuka Koneksi ke Retrofit (API)
        APIRequestData api = RetroServer.koneksiRetrofit().create(APIRequestData.class);

        // Call Respon yang terjadi ketika Retrieve Data
        Call<ModelResponse> proses = api.ardRetrieve();

        // Enqueue Data
        proses.enqueue(new Callback<ModelResponse>()
        {
            // Ketika berhasil melakukan response
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                listKuliner = response.body().getDataKuliner();

                rvAdapterKuliner = new AdapterKuliner(MainActivity.this, listKuliner);

                rvKuliner.setAdapter(rvAdapterKuliner);
                rvAdapterKuliner.notifyDataSetChanged();

                Toast.makeText(MainActivity.this, "Pesan : " + response.body().getPesan(), Toast.LENGTH_SHORT).show();
                pbLoading.setVisibility(View.GONE);

            }

            // Ketika Gagal melakukan response
            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal menghubungi server " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}