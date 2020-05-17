package com.example.gldali.siratakip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAut = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");
    DatabaseReference oku= FirebaseDatabase.getInstance().getReference("message");
    Button giris, kayit;
    EditText ad,tc;
    TextView sonuc;

    private void writeNewUser( String name,String soyAd, String tc,String poliklinik,String doktorAdi,String zaman,String sira) {
        Users user = new Users(name,soyAd, tc,poliklinik,doktorAdi,zaman,sira);
        myRef.child("users").push().setValue(user);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //myRef.setValue("Hello, World!");
        //writeNewUser("Celil","Özsoy","28222097994","KBB3","Mehmet","11:30","1");
       // writeNewUser("Melike","Kazak","17555986427","KBB3","Mehmet","11:45","2");
       // writeNewUser("Taner","Kazım","45669230158","KBB3","Mehmet","13:30","3");
       // writeNewUser("Güldali","Özsoy","28222087944","KBB3","Mehmet","14:00","4");
        ad=findViewById(R.id.editTextAd);
        tc=findViewById(R.id.editTextTc);
        sonuc=findViewById(R.id.textView);
        giris=findViewById(R.id.button);
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oku.addValueEventListener(new ValueEventListener()  {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String kullaniciAdi=ad.getText().toString();
                            String tcsi=tc.getText().toString();
                            if(kullaniciAdi.trim().equals("") )
                            {
                                sonuc.setText("Lütfen adınızı  giriniz");
                            }

                            else  if(tcsi.length()==11){
                                Intent intent = new Intent(getApplicationContext(),BilgilerActivity.class);
                                intent.putExtra("bilgiler",tc.getText().toString());
                                startActivity(intent);
                            }
                            else {
                              sonuc.setText("bilgileriniz eksik veya hatalı");
                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("Okuma Başarısız: " + databaseError.getMessage());

                    }
                });

            }

        });

    }
}
