package com.example.gldali.siratakip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class BilgilerActivity extends AppCompatActivity {
    DatabaseReference oku= FirebaseDatabase.getInstance().getReference("message");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    TextView sonuc,mevcutSira;
    ListView mListView;
    ArrayList<String> mMeetings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilgiler);
        sonuc = findViewById(R.id.sonuc);
        mListView=findViewById(R.id.liste);
        mevcutSira=findViewById(R.id.mevcutSira);


        Intent intent= getIntent();
        String bilgiler= intent.getStringExtra("bilgiler");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mMeetings);
        mListView.setAdapter(arrayAdapter);

        Query query = FirebaseDatabase.getInstance().getReference("message").child("users").orderByChild("tc").equalTo(bilgiler);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapShot : dataSnapshot.getChildren()) {

                    String sira = (String) childSnapShot.child("sira").getValue();
                    String datetime = (String) childSnapShot.child("zaman").getValue();
                    String doktor = (String) childSnapShot.child("doktorAdi").getValue();
                    String surname = (String) childSnapShot.child("soyAd").getValue();
                    String name = (String) childSnapShot.child("ad").getValue();
                    String poli = (String) childSnapShot.child("poliklinik").getValue();
                    String tc = (String) childSnapShot.child("tc").getValue();

                    mMeetings.add("Ad="+name);
                    mMeetings.add("Soyad="+surname);
                    mMeetings.add("T.C ="+tc);
                    mMeetings.add("Poliklinik="+poli);
                    mMeetings.add("Doktor Adı="+doktor);
                    mMeetings.add("Sıranız="+sira);
                    mMeetings.add("zaman="+datetime);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Okuma Başarısız: " + databaseError.getMessage());

            }
        });
        oku.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    long numOfLikes = postSnapshot.getChildrenCount();
                    if(numOfLikes>0) {
                        System.out.println("Toplam Hasta Sayısı=" + numOfLikes);
                        sonuc.setText((String.valueOf(numOfLikes)));
                    }

                    else{
                        sonuc.setText("Hasta Bulunmamaktadır");
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Okuma Başarısız: " + databaseError.getMessage());

            }
        });

        Query query1 = FirebaseDatabase.getInstance().getReference("message").child("users").limitToFirst(1);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String sira1 = (String) postSnapshot.child("sira").getValue();
                        System.out.println("mevcut sıramız"+sira1);
                        mevcutSira.setText(sira1);

                    }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
