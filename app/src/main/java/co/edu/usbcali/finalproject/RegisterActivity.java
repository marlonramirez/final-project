package co.edu.usbcali.finalproject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

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

public class RegisterActivity extends FragmentActivity {
    private Spinner spnDocumentType;
    private Spinner spnSpecialization;
    private Spinner spnPaymentType;
    private Switch swType;
    private LinearLayout layoutStudent;
    private LinearLayout layoutTeacher;
    private List<Type> documentTypes;
    private List<Type> paymentTypes;
    private List<Type> specializations;
    private Type selectedDocumentType;
    private Type selectedPaymentType;
    private Type selectedSpecialization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        spnDocumentType = findViewById(R.id.spn_document_type);
        spnSpecialization = findViewById(R.id.spn_specialization);
        spnPaymentType = findViewById(R.id.spn_payment);
        layoutTeacher = findViewById(R.id.layout_teacher);
        layoutStudent = findViewById(R.id.layout_student);
        swType = findViewById(R.id.sw_type);
        loadDocumentTypes();
        loadPaymentTypes();
        loadSpecializations();
    }

    public void changeType(View view) {
        if (swType.isChecked()) {
            layoutStudent.setVisibility(View.GONE);
            layoutTeacher.setVisibility(View.VISIBLE);
        } else {
            layoutStudent.setVisibility(View.VISIBLE);
            layoutTeacher.setVisibility(View.GONE);
        }
    }

    private void loadDocumentTypes() {
        FirebaseDatabase fireDataBase = FirebaseDatabase.getInstance();
        DatabaseReference reference = fireDataBase.getReference("documentTypes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> labels = new ArrayList<>();
                documentTypes = new ArrayList<>();
                for (DataSnapshot iterated: dataSnapshot.getChildren()) {
                    Type documentType = iterated.getValue(Type.class);
                    documentTypes.add(documentType);
                    labels.add(documentType.getDescription());
                }
                loadSpinnerDocuments(labels);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "connexion FireBase cancelled: " + databaseError.getMessage());
            }
        });
    }

    private void loadSpinnerDocuments(List<String> labels) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDocumentType.setAdapter(dataAdapter);
        spnDocumentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDocumentType = documentTypes.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedDocumentType = null;
            }
        });
    }

    private void loadPaymentTypes() {
        FirebaseDatabase fireDataBase = FirebaseDatabase.getInstance();
        DatabaseReference reference = fireDataBase.getReference("paymentTypes");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> labels = new ArrayList<>();
                paymentTypes = new ArrayList<>();
                for (DataSnapshot iterated: dataSnapshot.getChildren()) {
                    Type documentType = iterated.getValue(Type.class);
                    paymentTypes.add(documentType);
                    labels.add(documentType.getDescription());
                }
                loadSpinnerPayments(labels);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "connexion FireBase cancelled: " + databaseError.getMessage());
            }
        });
    }

    private void loadSpinnerPayments(List<String> labels) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPaymentType.setAdapter(dataAdapter);
        spnPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPaymentType = paymentTypes.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedPaymentType = null;
            }
        });
    }

    private void loadSpecializations() {
        FirebaseDatabase fireDataBase = FirebaseDatabase.getInstance();
        DatabaseReference reference = fireDataBase.getReference("especialization");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> labels = new ArrayList<>();
                specializations = new ArrayList<>();
                for (DataSnapshot iterated: dataSnapshot.getChildren()) {
                    Type documentType = iterated.getValue(Type.class);
                    specializations.add(documentType);
                    labels.add(documentType.getDescription());
                }
                loadSpinnerSpecializations(labels);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "connexion FireBase cancelled: " + databaseError.getMessage());
            }
        });
    }

    private void loadSpinnerSpecializations(List<String> labels) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, labels);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSpecialization.setAdapter(dataAdapter);
        spnSpecialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSpecialization = specializations.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedSpecialization = null;
            }
        });
    }
}
