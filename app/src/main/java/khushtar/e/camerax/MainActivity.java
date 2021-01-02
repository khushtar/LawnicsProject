package khushtar.e.camerax;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab_camera;
    FloatingActionButton fab_store;
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    ArrayList<PhotoHandler> postArrayList;

    FirebaseDatabase database;
    DatabaseReference myRef;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab_camera=findViewById(R.id.camera);
        fab_store=findViewById(R.id.store);
        recyclerView=findViewById(R.id.recycler_view_main);
        pd=new ProgressDialog(this);

        postArrayList=new ArrayList<>();

        myAdapter=new MyAdapter(postArrayList,this);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("images");
        pd.setMessage("Data being Fetch...");
        pd.show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postArrayList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    PhotoHandler ph = snapshot.getValue(PhotoHandler.class);
//                    Log.d("url",ph.getUrl());
//                    Log.d("name",ph.getImageName());
                    postArrayList.add(ph);
                }
                pd.dismiss();
                recyclerView.setAdapter(myAdapter);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Fetching Failed", Toast.LENGTH_SHORT).show();

            }
        });


        fab_camera.setOnClickListener(v->{
            startActivity(new Intent(MainActivity.this,Shoot.class));
        });

    }

}