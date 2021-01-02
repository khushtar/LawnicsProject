package khushtar.e.camerax;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.UUID;

public class Shoot extends AppCompatActivity {

    private int REQUEST_CODE_PERMISSION=101;
    private String[] REQUIRED_PERMISSION=new String[]{"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE"};
    ImageView textureView;
    ImageButton btnCap,btnDone,btnGallery;
    Bitmap image;
    ProgressDialog pd;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot);

        getSupportActionBar().hide();

        textureView=findViewById(R.id.textureView);
        btnCap=findViewById(R.id.image_shoot);
        btnDone=findViewById(R.id.done_shoot);
        btnGallery=findViewById(R.id.gallery_shoot);
        pd=new ProgressDialog(this);

        storageReference= FirebaseStorage.getInstance().getReference();
        Intent camera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(allPermissionGranted()){

            startActivityForResult(camera,REQUEST_CODE_PERMISSION);
        }else{
            ActivityCompat.requestPermissions(this,REQUIRED_PERMISSION,REQUEST_CODE_PERMISSION);
        }

        btnCap.setOnClickListener(view->{
            startActivityForResult(camera,REQUEST_CODE_PERMISSION);
        });

        btnDone.setOnClickListener(view->{
            onUploade();
        });

        btnGallery.setOnClickListener(view->{
            onDonload();
        });
    }

    private void onUploade() {

        pd.setMessage("please Wait");
        pd.show();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,stream);


        final String random= UUID.randomUUID().toString();
        StorageReference imageRef=storageReference.child("images/"+random);

        byte[] b=stream.toByteArray();
        imageRef.putBytes(b).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                taskSnapshot.getMetadata().getReference().getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(Uri uri) {
                                Uri downloadUri=uri;
                                String date=LocalDate.now().toString().substring(0,8);
                                String time=LocalTime.now().toString().substring(0,8);

                                PhotoHandler ph=new PhotoHandler();
                                ph.setTime(time);
                                ph.setDate(date);
                                ph.setImageName("Lawnics"+date+""+time);
                                ph.setType("jpeg");
                                ph.setUrl(downloadUri.toString());

                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("images");

                                myRef.child(ph.getImageName()).setValue(ph).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("status: ","succesfull added to the database");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Log.d("status: "," Failed to add on database");

                                    }
                                });
                            }
                        });
                Toast.makeText(Shoot.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Toast.makeText(Shoot.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void onDonload(){
            startActivity(new Intent(this,MainActivity.class));
            finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_PERMISSION && resultCode==RESULT_OK){
            image=(Bitmap)data.getExtras().get("data");
            textureView.setImageBitmap(image);
        }
    }

    private boolean allPermissionGranted() {

        for(String permission:REQUIRED_PERMISSION){
            if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

}