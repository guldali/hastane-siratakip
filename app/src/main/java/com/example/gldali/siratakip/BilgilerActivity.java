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

                    mMeetings.add(name);
                    mMeetings.add(surname);
                    mMeetings.add(tc);
                    mMeetings.add(poli);
                    mMeetings.add(doktor);
                    mMeetings.add(sira);
                    mMeetings.add(datetime);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                   // mevcutSira.setText(dataSnapshot.child("users").child("M744akkFWVvuIg-Kp-q").child("sira").getValue().toString());

                   // Query query1=FirebaseDatabase.getInstance().getReference("message").child("users").child("1").orderByChild("sira");
                   // System.out.println("first"+query1);
                  //  mevcutSira.setText((String.valueOf(query1)));

                   // Iterator iterator = dataSnapshot.getChildren().iterator();
                    //System.out.println("iterator" + iterator.next());

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //Query query1=FirebaseDatabase.getInstance().getReference("message").child("users").child("1").orderByChild("sira");
        //System.out.println("first"+query1);
        /*
        oku.addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {



                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }
}
