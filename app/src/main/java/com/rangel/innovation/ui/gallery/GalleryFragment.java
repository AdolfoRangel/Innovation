package com.rangel.innovation.ui.gallery;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.rangel.innovation.MainActivity;
import com.rangel.innovation.R;
import com.rangel.innovation.databinding.FragmentGalleryBinding;
import com.rangel.innovation.db.ConexionSQLLiteHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.rangel.innovation.MainActivity.getContextOfApplication;
import static com.rangel.innovation.constantes.Utils.DB_NOMBRE;

public class GalleryFragment extends Fragment {

    static final int PICK_IMAGE_REQUEST = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;



    private GalleryViewModel galleryViewModel;
    private Bitmap bitmap = null;
    ImageView photoViewer = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);


         photoViewer = root.findViewById(R.id.photoViewer);

        elegir();

        Button otra = root.findViewById(R.id.btn_otra);
        Button aceptar = root.findViewById(R.id.btn_aceptar);

        otra.setOnClickListener(v ->{
            elegir();
        });
        aceptar.setOnClickListener(v->{

            SharedPreferences userDetails = getContextOfApplication().getSharedPreferences("usuario", MODE_PRIVATE);
            final String id_usuario = userDetails.getString("id", "");
            int id = Integer.parseInt(id_usuario);
            guardarImagen(id,bitmap);
        });

        return root;

    }

    public void elegir(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //Setting message manually and performing action on button click
        builder.setMessage("¿De donde quieres tomar la foto?")
                .setCancelable(false)
                .setPositiveButton("Desde el celular", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        showFileChooser();
                    }
                })
                .setNegativeButton("Tomar foto", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        showFileChooserTomar();
                    }
                });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("FOTO");
        alert.show();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Imagen"), PICK_IMAGE_REQUEST);
    }

    private void showFileChooserTomar() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        //}
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
//Cómo obtener el mapa de bits de la Galería
                Context applicationContext = getContextOfApplication();

                bitmap = MediaStore.Images.Media.getBitmap( applicationContext.getContentResolver(), filePath);

                final int maxSize = 800;
                int outWidth;
                int outHeight;
                int inWidth = bitmap.getWidth();
                int inHeight = bitmap.getHeight();
                if (inWidth > inHeight) {
                    outWidth = maxSize;
                    outHeight = (inHeight * maxSize) / inWidth;
                } else {
                    outHeight = maxSize;
                    outWidth = (inWidth * maxSize) / inHeight;
                }

                bitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);

                //Configuración del mapa de bits en ImageView
                photoViewer.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photoViewer.setImageBitmap(imageBitmap);
            //bitmap = imageBitmap;
           /* if (bitmap.getWidth() != 800) {
                double porcentaje = (bitmap.getWidth() * 100) / 800;
                double restadelporcentaje = 100 - porcentaje;
                double ancho = bitmap.getWidth() + (800 * (restadelporcentaje * 0.01));
                double altura = bitmap.getHeight() + (800 * (restadelporcentaje * 0.01));
                bitmap = redimensionarImagenMaximo(bitmap, ancho, altura);
            }*/

            final int maxSize = 800;
            int outWidth;
            int outHeight;
            int inWidth = imageBitmap.getWidth();
            int inHeight = imageBitmap.getHeight();
            if (inWidth > inHeight) {
                outWidth = maxSize;
                outHeight = (inHeight * maxSize) / inWidth;
            } else {
                outHeight = maxSize;
                outWidth = (inWidth * maxSize) / inHeight;
            }

            bitmap = Bitmap.createScaledBitmap(imageBitmap, outWidth, outHeight, true);
        }
    }

    public void guardarImagen(long id, Bitmap bitmap){
        // tamaño del baos depende del tamaño de tus imagenes en promedio
        ByteArrayOutputStream baos = new ByteArrayOutputStream(20480);
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 , baos);
        byte[] blob = baos.toByteArray();
        // aqui tenemos el byte[] con el imagen comprimido, ahora lo guardemos en SQLite
        ConexionSQLLiteHelper conn = new ConexionSQLLiteHelper(getContext(), DB_NOMBRE, null, 1);

        SQLiteDatabase db = conn.getWritableDatabase();

        String sql = "INSERT INTO imagen (id, img) VALUES(?,?)";
        SQLiteStatement insert = db.compileStatement(sql);
        insert.clearBindings();
        insert.bindLong(1, id);
        insert.bindBlob(2, blob);
        insert.executeInsert();
        db.close();


    }
}