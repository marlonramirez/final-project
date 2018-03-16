package co.edu.usbcali.finalproject.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import co.edu.usbcali.finalproject.R;
import co.edu.usbcali.finalproject.model.Teacher;

/**
 * Created by Marlon.Ramirez on 3/02/2018.
 */

public class TeacherAdapter extends BaseAdapter {
    protected Activity activity;
    protected List<Teacher> items;

    public TeacherAdapter(Activity activity, List<Teacher> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(List<Teacher> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(R.layout.adapter_teacher, null);
        }
        Teacher teacher = items.get(position);
        TextView name = convertView.findViewById(R.id.txt_title);
        name.setText(teacher.getEmail());
        TextView description = convertView.findViewById(R.id.txt_description);
        description.setText(teacher.getDescription());
        TextView payment = convertView.findViewById(R.id.txt_payment);
        payment.setText("$15000");
        TextView time = convertView.findViewById(R.id.txt_time);
        time.setText("12000");
        ImageView imageView = convertView.findViewById(R.id.img_perfil);
        queryImage(imageView, teacher.getDocument());
        return convertView;
    }

    private void queryImage(final ImageView imageView, String document) {
        if (!document.isEmpty()) {
            long size = 1024*1024;
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance("gs://final-project-3fa38.appspot.com");
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference referenceDownload = storageReference.child("photos/" + document + ".png");
            referenceDownload.getBytes(size).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap photo = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(photo);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Error", "Fallo al descargar la imagen, " + e.getMessage());
                }
            });
        }
    }
}
