package co.edu.usbcali.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import co.edu.usbcali.finalproject.adapters.TeacherAdapter;
import co.edu.usbcali.finalproject.model.Teacher;

/**
 * Created by Marlon.Ramirez on 11/03/2018.
 */

public class MainActivity extends FragmentActivity {
    private ListView lstTeachers;
    private ArrayList<Teacher> teachers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstTeachers = findViewById(R.id.lst_teachers);
        loadTeachers();
    }

    public void loadTeachers() {
        FirebaseDatabase fireDataBase = FirebaseDatabase.getInstance();
        DatabaseReference reference = fireDataBase.getReference("teachers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teachers = new ArrayList<>();
                for (DataSnapshot iterated: dataSnapshot.getChildren()) {
                    Teacher teacher = iterated.getValue(Teacher.class);
                    teachers.add(teacher);
                }
                loadListView(teachers);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", "connexion FireBase cancelled: " + databaseError.getMessage());
            }
        });
    }

    public void loadListView(final List<Teacher> teachers) {
        TeacherAdapter arrayAdapter = new TeacherAdapter(this, teachers);
        lstTeachers.setAdapter(arrayAdapter);
        lstTeachers.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
            {
                Teacher selected = teachers.get(position);
                Intent intent = new Intent(view.getContext(), ClassActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("document", selected.getDocument());
                bundle.putDouble("latitude", selected.getLatitude());
                bundle.putDouble("longitude", selected.getLongitude());
                intent.putExtra("teacher", bundle);
                startActivity(intent);
            }
        });
    }
}
