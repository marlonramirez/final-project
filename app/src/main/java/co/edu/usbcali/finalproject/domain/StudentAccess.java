package co.edu.usbcali.finalproject.domain;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.edu.usbcali.finalproject.model.Student;
import co.edu.usbcali.finalproject.model.Type;

/**
 * Created by Marlon.Ramirez on 14/03/2018.
 */

public class StudentAccess extends Validation {
    private static StudentAccess instance;

    public static StudentAccess getInstance() {
        if (instance == null) {
            instance = new StudentAccess();
        }
        return instance;
    }

    public void create(Type documentType, String document, String password, String password2, String phone, Type payment, String email) throws Exception {
        Student student = new Student();
        student.setDocumentType(documentType.getCode());
        student.setPayment(payment.getCode());
        super.validate(document, password, password2, email);
        student.setDocument(document);
        student.setEmail(email);
        if (phone == null || phone.isEmpty()) {
            throw new Exception("Por favor digite su número de télefono");
        }
        student.setPhone(phone);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("students");
        databaseReference.child(student.getDocument()).setValue(student);
    }
}
