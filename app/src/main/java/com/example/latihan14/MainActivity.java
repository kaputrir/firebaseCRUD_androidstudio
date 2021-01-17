package com.example.latihan14;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.latihan14.paketku.AdapterDataku;
import com.example.latihan14.paketku.Dataku;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button tbltambah;
    RecyclerView recyclerView;
    EditText txtNim, txtNama;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Mahasiswa");
    List<Dataku> list = new ArrayList<>();
    AdapterDataku adapterDataku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tbltambah = findViewById(R.id.tmblTambah);
        recyclerView = findViewById(R.id.recycler_view);
        txtNim = findViewById(R.id.txt_nim);
        txtNama = findViewById(R.id.txt_nama);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        tbltambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtNim.getText().toString().length() == 0){
                    txtNim.setError("Isi NIM mahasiswa");
                } else if (txtNama.getText().toString().length() == 0) {
                    txtNama.setError("Isi Nama mahasiswa");
                } else {
                    simpanData(txtNim.getText().toString(), txtNama.getText().toString());
                }
            }
        });

        bacaData();
    }

    private void bacaData() {
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Dataku value = snapshot.getValue(Dataku.class);
                    list.add(value);
                }
                adapterDataku = new AdapterDataku(MainActivity.this, list);
                recyclerView.setAdapter(adapterDataku);

                setClick();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    private void setClick() {
        adapterDataku.setOnCallBack(new AdapterDataku.OnCallBack() {
            @Override
            public void onTblDelete(Dataku dataku) {
                hapusData(dataku);
            }

            @Override
            public void onTblEdit(Dataku dataku) {
                showDialogEditData(dataku);
            }
        });
    }

    private void showDialogEditData(Dataku dataku) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_data_layout);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(lp);

        ImageButton tblKeluar = dialog.findViewById(R.id.tbl_keluar);
        tblKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        EditText txtEditNim = dialog.findViewById(R.id.txt_edit_nim);
        EditText txtEditNama = dialog.findViewById(R.id.txt_edit_nama);
        Button tblEdit = dialog.findViewById(R.id.tbl_edit);

        txtEditNim.setText(dataku.getIsiNim());
        txtEditNama.setText(dataku.getIsiNama());

        tblEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txtEditNim.getText()) || TextUtils.isEmpty(txtEditNama.getText())){
                    tblEdit.setError("Data Tidak Boleh Kosong");
                } else {
                    editData(dataku, txtEditNim.getText().toString(), txtEditNama.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void editData(Dataku dataku, String nimbaru, String namabaru) {
        String kunci = dataku.getKunci();
        dataku = new Dataku(kunci, nimbaru, namabaru);

        myRef.child(kunci).setValue(dataku).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Berhasil Update Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hapusData(Dataku dataku) {
        myRef.child(dataku.getKunci()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(MainActivity.this, "Berhasil Menghapus Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void simpanData(String xnim, String xnama) {
        String kunci = myRef.push().getKey();
        Dataku dataku = new Dataku(kunci, xnim, xnama);

        myRef.child(kunci).setValue(dataku).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MainActivity.this, "Berhasil Menambah Data", Toast.LENGTH_SHORT).show();
                txtNim.setText("");
                txtNama.setText("");
            }
        });
    }
}