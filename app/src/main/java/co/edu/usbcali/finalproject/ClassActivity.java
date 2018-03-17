package co.edu.usbcali.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.usbcali.finalproject.model.Type;

/**
 * Created by Marlon.Ramirez on 11/03/2018.
 */

public class ClassActivity extends FragmentActivity {
    private List<Type> modalities;
    private Spinner spnModality;
    private Type selectedModality;
    private ImageView imgStudent;
    private Bitmap photo;
    private static final int REQUEST_CODE;
    static {
        REQUEST_CODE = 1888;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        spnModality = findViewById(R.id.spn_modality);
        imgStudent = findViewById(R.id.img_student);
        loadModalities();
    }

    public void save(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void watchMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        Bundle extras = getIntent().getExtras().getBundle("teacher");
        intent.putExtra("teacher", extras);
        startActivity(intent);
    }

    public void accessCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
            photo = (Bitmap) data.getExtras().get("data");
            imgStudent.setImageBitmap(photo);
        }
    }

    private void loadModalities() {
        FirebaseDatabase fireDataBase = FirebaseDatabase.getInstance();
        DatabaseReference reference = fireDataBase.getReference("modalities");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> labels = new ArrayList<>();
                modalities = new ArrayList<>();
                for (DataSnapshot iterated: dataSnapshot.getChildren()) {
                    Type documentType = iterated.getValue(Type.class);
                    modalities.add(documentType);
                    labels.add(documentType.getDescription());
                }
                loadSpinnerModalities(labels);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "connexion FireBase cancelled: " + databaseError.getMessage());
            }
        });
    }

    private void loadSpinnerModalities(List<String> labels) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnModality.setAdapter(dataAdapter);
        spnModality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedModality = modalities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedModality = null;
            }
        });
    }
}
