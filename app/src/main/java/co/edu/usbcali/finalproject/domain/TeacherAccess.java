package co.edu.usbcali.finalproject.domain;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import co.edu.usbcali.finalproject.model.Teacher;
import co.edu.usbcali.finalproject.model.Type;

/**
 * Created by Marlon.Ramirez on 14/03/2018.
 */

public class TeacherAccess extends Validation {
    private static TeacherAccess instance;

    public static TeacherAccess getInstance() {
        if (instance == null) {
            instance = new TeacherAccess();
        }
        return instance;
    }

    public void create(Type documentType, String document, String password, String password2, Type specialization, String description, String fee, String experience, String email, Bitmap image) throws Exception {
        Teacher teacher = new Teacher();
        teacher.setDocumentType(documentType.getCode());
        teacher.setSpecialization(specialization.getCode());
        super.validate(document, password, password2, email);
        teacher.setDocument(document);
        teacher.setEmail(email);
        if (image == null) {
            throw new Exception("Por favor tome una foto para su perfil");
        }
        if (description == null || description.isEmpty()) {
            throw new Exception("Por favor digite una breve descripción de su persona");
        }
        teacher.setDescription(description);
        if (fee == null || fee.isEmpty()) {
            throw new Exception("Por favor digite el dinero que solicita por hora");
        }
        teacher.setFee(Long.parseLong(fee));
        if (experience == null || experience.isEmpty()) {
            throw new Exception("Por favor digite las horas de experiencia actual");
        }
        teacher.setExperience(Long.parseLong(experience));
        teacher.setLatitude(3.3441);
        teacher.setLongitude(-76.5435);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("teachers");
        databaseReference.child(teacher.getDocument()).setValue(teacher);
        storagePhoto(document, image);
    }

    private void storagePhoto(String document, Bitmap photo) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://final-project-3fa38.appspot.com");
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference referenceSave = storageReference.child("photos/" + document + ".png");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        byte[] bitmapdata = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        UploadTask uploadTask = referenceSave.putStream(bs);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error", "Fallo subir foto " + e.getMessage());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("Info", "Se almaceno con exito");
            }
        });

    }
}
