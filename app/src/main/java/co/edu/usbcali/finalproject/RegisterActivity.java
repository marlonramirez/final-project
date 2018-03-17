package co.edu.usbcali.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.usbcali.finalproject.domain.StudentAccess;
import co.edu.usbcali.finalproject.domain.TeacherAccess;
import co.edu.usbcali.finalproject.model.Type;
import co.edu.usbcali.finalproject.util.MessageDialog;
import in.gauriinfotech.commons.Commons;

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
    private EditText txtDocument;
    private EditText txtPassword;
    private EditText txtRepPassword;
    private EditText txtPhone;
    private EditText txtDescription;
    private EditText txtEmail;
    private EditText txtFee;
    private EditText txtExperience;
    private Type selectedDocumentType;
    private Type selectedPaymentType;
    private Type selectedSpecialization;
    private String selectedFilePath;
    private Bitmap photo;
    private static final int REQUEST_CODE;
    private static final int PICK_FILE_REQUEST;
    static {
        REQUEST_CODE = 1888;
        PICK_FILE_REQUEST = 1999;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        spnDocumentType = findViewById(R.id.spn_document_type);
        spnSpecialization = findViewById(R.id.spn_specialization);
        spnPaymentType = findViewById(R.id.spn_payment);
        layoutTeacher = findViewById(R.id.layout_teacher);
        layoutStudent = findViewById(R.id.layout_student);
        txtDocument = findViewById(R.id.txt_document);
        txtPassword = findViewById(R.id.txt_password);
        txtRepPassword = findViewById(R.id.txt_rep_password);
        txtPhone = findViewById(R.id.txt_phone);
        txtDescription = findViewById(R.id.txt_description);
        txtFee = findViewById(R.id.txt_fee);
        txtExperience = findViewById(R.id.txt_experience);
        txtEmail = findViewById(R.id.txt_email);
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

    public void loadCurriculum(View view) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Choose File to Upload.."),PICK_FILE_REQUEST);
    }

    public void register(View view) {
        String email = txtEmail.getText().toString();
        String document = txtDocument.getText().toString();
        String password = txtPassword.getText().toString();
        String password2 = txtRepPassword.getText().toString();
        try {
            if (swType.isChecked()) {
                TeacherAccess.getInstance().create(
                        selectedDocumentType,
                        document,
                        password,
                        password2,
                        selectedSpecialization,
                        txtDescription.getText().toString(),
                        txtFee.getText().toString(),
                        txtExperience.getText().toString(),
                        email,
                        photo,
                        selectedFilePath
                );
            } else {
                StudentAccess.getInstance().create(
                        selectedDocumentType,
                        document,
                        password,
                        password2,
                        txtPhone.getText().toString(),
                        selectedPaymentType,
                        email
                );
            }
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser != null) {
                showMessage("Usuario ya creado");
            }
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        new MessageDialog("Se ha creado el usuario exitosamente", LoginActivity.class, RegisterActivity.this).show(getFragmentManager(), "Alerta");
                    } else {
                        showMessage("No se ha registrado con exito, " + task.getException().getMessage());
                    }
                }
            });
        } catch(Exception ex) {
            showMessage(ex.getMessage());
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

    public void accessCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
            photo = (Bitmap) data.getExtras().get("data");
        } else if (requestCode == PICK_FILE_REQUEST && data != null) {
            Uri fileUri = data.getData();
            selectedFilePath = Commons.getPath(fileUri, this);
        }
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

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
