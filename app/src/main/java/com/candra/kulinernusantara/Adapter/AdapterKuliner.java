package com.candra.kulinernusantara.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.candra.kulinernusantara.API.APIRequestData;
import com.candra.kulinernusantara.API.RetroServer;
import com.candra.kulinernusantara.MainActivity;
import com.candra.kulinernusantara.Model.ModelKuliner;
import com.candra.kulinernusantara.Model.ModelResponse;
import com.candra.kulinernusantara.R;
import com.candra.kulinernusantara.UpdateActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterKuliner extends RecyclerView.Adapter<AdapterKuliner.HolderData> {
    private Context ctx;
    private List<ModelKuliner> listKuliner;

    public AdapterKuliner(Context ctx, List<ModelKuliner> listKuliner) {
        this.ctx = ctx;
        this.listKuliner = listKuliner;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_kuliner, parent,false);

        HolderData holderData = new HolderData(view);
        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        ModelKuliner mk = listKuliner.get(position);

        holder.tvId.setText(String.valueOf(mk.getId()));
        holder.tvNama.setText(mk.getNama());
        holder.tvAsal.setText(mk.getAsal());
        holder.tvDeskripsi.setText(mk.getDeskripsi_singkat());

    }

    @Override
    public int getItemCount() {
        if(listKuliner == null)
        {
            return 0;
        }
        return listKuliner.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {

        TextView tvId, tvNama, tvAsal, tvDeskripsi;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvAsal = itemView.findViewById(R.id.tv_asal);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsi);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int idKuliner = Integer.parseInt(tvId.getText().toString());

                    AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
                    dialog.setTitle("Perhatian");
                    dialog.setMessage("Anda memilih Kuliner " + tvNama.getText().toString());
                    dialog.setCancelable(true);
                    dialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteData(idKuliner);
                            dialogInterface.dismiss();
                        }
                    });

                    dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent varIntent = new Intent(ctx, UpdateActivity.class);
                            varIntent.putExtra("varId", tvId.getText().toString());
                            varIntent.putExtra("varNama", tvNama.getText().toString());
                            varIntent.putExtra("varAsal", tvAsal.getText().toString());
                            varIntent.putExtra("varDeskripsi", tvDeskripsi.getText().toString());

                            ctx.startActivity(varIntent);
                        }
                    });

                    dialog.show();
                    return false;
                }

            });
        }
    }


    // Method Hapus Data
    private void deleteData(int id)
    {
        APIRequestData api = RetroServer.koneksiRetrofit().create(APIRequestData.class);

        // Memanggil Model Response
        Call<ModelResponse> proses = api.ardDelete(id);

        proses.enqueue(new Callback<ModelResponse>() {
            @Override
            public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                int kode = response.body().getKode();
                String pesan  = response.body().getPesan();

                Toast.makeText(ctx, "Kode : " + kode + " | Pesan : " + pesan, Toast.LENGTH_SHORT).show();
                ((MainActivity) ctx).retrieveKuliner();
            }

            @Override
            public void onFailure(Call<ModelResponse> call, Throwable t) {
                Toast.makeText(ctx, "Gagal menghubungi server !" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
