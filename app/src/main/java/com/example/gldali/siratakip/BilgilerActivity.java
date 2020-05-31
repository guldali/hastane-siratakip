package com.example.gldali.siratakip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
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
    TextView sonuc,mevcutSira,bilgilendirme;
    ListView mListView;
    ArrayList<String> mMeetings = new ArrayList<>();
    Context context = this;
    NotificationManager notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bilgiler);
        sonuc = findViewById(R.id.sonuc);
        mListView=findViewById(R.id.liste);
        mevcutSira=findViewById(R.id.mevcutSira);
        bilgilendirme=findViewById(R.id.textView2);
        ActionBar actionBar =getSupportActionBar();
        actionBar.hide();

        Intent intent= getIntent();
        String bilgiler= intent.getStringExtra("bilgiler"); //1. sayfadan gönderilen tc bilgisi alındı

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mMeetings);
        mListView.setAdapter(arrayAdapter);

        Query query = FirebaseDatabase.getInstance().getReference("message").child("users").orderByChild("tc").equalTo(bilgiler);//Veritabanından tc sorgusu yapıldı
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapShot : dataSnapshot.getChildren()) {
                    //girilen tc ye ait kişinin tüm sıra bilgileri alındı
                    String sira = (String) childSnapShot.child("sira").getValue();
                    String datetime = (String) childSnapShot.child("zaman").getValue();
                    String doktor = (String) childSnapShot.child("doktorAdi").getValue();
                    String surname = (String) childSnapShot.child("soyAd").getValue();
                    String name = (String) childSnapShot.child("ad").getValue();
                    String poli = (String) childSnapShot.child("poliklinik").getValue();
                    String tc = (String) childSnapShot.child("tc").getValue();

                    mMeetings.add("Ad = "+name);
                    mMeetings.add("Soyad = "+surname);
                    mMeetings.add("T.C = "+tc);
                    mMeetings.add("Poliklinik = "+poli);
                    mMeetings.add("Doktor Adı = "+doktor);
                    mMeetings.add("Sıranız = "+sira);
                    mMeetings.add("zaman = "+datetime);

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

                    long numOfLikes = postSnapshot.getChildrenCount();//veritabanındaki tüm veri sayısı numOfLikes değişkenine atandı.
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
        //ilk sıradaki veri
        Query query1 = FirebaseDatabase.getInstance().getReference("message").child("users").limitToFirst(1);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 //ilk verinin sıra bilgisi alındı.
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
        Query query2 = FirebaseDatabase.getInstance().getReference("message").child("users").orderByChild("tc").equalTo(bilgiler);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot childSnapShot : dataSnapshot.getChildren()) {

                    String sira = (String) childSnapShot.child("sira").getValue();
                    query1.addListenerForSingleValueEvent(new ValueEventListener() {//ilk sıradaki veri
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                String sira1 = (String) postSnapshot.child("sira").getValue();//en baştaki sıra değeri alındı.
                                System.out.println("mevcut sıramız"+sira1);
                                mevcutSira.setText(sira1);
                                Integer fark =(Integer.parseInt(sira)) - (Integer.parseInt(sira1));
                                System.out.println("fark"+fark);

                                if(fark<4){
                                bilgilendirme.setText("Sıranız yaklaşıyor kalan kişi"+":"+fark);
                                /*
                                Intent intent1 = new Intent(Intent.ACTION_VIEW,Uri.parse(myArayuz));
                                PendingIntent pendingIntent=PendingIntent.getActivity(context,0,intent1,0);
                                myNotification = new Notification.Builder(context)
                                        .setContentTitle("Bildirim")
                                        .setContentText("Sıranız Yaklaşıyor")
                                        .setWhen(System.currentTimeMillis())
                                        .setContentIntent(pendingIntent)
                                        .setDefaults(Notification.DEFAULT_SOUND)
                                        .setAutoCancel(true).build();
                                notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(NotificationId,myNotification);
*/
/*
                             //  Uri sesDosyası = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                                NotificationManager manager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                Notification.Builder builder = new Notification.Builder(getApplicationContext());
                                builder.setContentTitle("SIRAMATİK");
                                builder.setContentText("Sıranız Yaklaşıyor");
                                builder.setAutoCancel(true);
                                builder.setTicker("yaklaşıyor");
                                //builder.setDefaults(Notification.DEFAULT_VIBRATE);
                               // builder.setSound(sesDosyası);

                                Intent intent1= new Intent(getApplicationContext(),BilgilerActivity.class);
                                PendingIntent pending = PendingIntent.getActivity(getApplicationContext(),1,intent1,0);
                                builder.setContentIntent(pending);
                                Notification notification=builder.getNotification();
                                manager.notify(1,notification);
*/

                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Okuma Başarısız: " + databaseError.getMessage());

            }
        });

    }
}
