package com.rangel.innovation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.rangel.innovation.databinding.ActivityLoginBinding;
import com.rangel.innovation.databinding.ActivityMainBinding;
import com.rangel.innovation.db.ConexionSQLLiteHelper;
import com.rangel.innovation.ui.login.Login;

import java.io.ByteArrayInputStream;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.rangel.innovation.constantes.Utils.DB_NOMBRE;

public class MainActivity extends AppCompatActivity {
private String TAG ="MainActivity";
    private AppBarConfiguration mAppBarConfiguration;

    ActivityMainBinding binding;
    public static Context contextOfApplication;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contextOfApplication = getApplicationContext();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("usuario", MODE_PRIVATE);
        final String id_usuario = userDetails.getString("id", "");
        final String namesuario = userDetails.getString("user", "");
        Log.d(TAG, "onCreate : "+namesuario);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.navUsername);
        navUsername.setText(namesuario);

        Bitmap buscarImagen = buscarImagen(Integer.parseInt(id_usuario));
        if(buscarImagen!= null){
            Log.d(TAG, "onCreate: aqui entra si es diferente de nulo");
            ImageView img = headerView.findViewById(R.id.imageViewProfile);
            img.setImageBitmap(buscarImagen);
        }
        
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            SharedPreferences userDetails = getApplicationContext().getSharedPreferences("usuario", MODE_PRIVATE);
            SharedPreferences.Editor edit = userDetails.edit();
            edit.remove("id");
            edit.remove("user");
            edit.apply();
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    public Bitmap buscarImagen(long id){
        ConexionSQLLiteHelper conn = new ConexionSQLLiteHelper(contextOfApplication, DB_NOMBRE, null, 1);

        SQLiteDatabase db = conn.getReadableDatabase();

        String sql = String.format("SELECT * FROM imagen WHERE id = %d", id);
        Cursor cursor = db.rawQuery(sql, new String[] {});
        Bitmap bitmap = null;
        if(cursor.moveToFirst()){
            byte[] blob = cursor.getBlob(1);
            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            bitmap = BitmapFactory.decodeStream(bais);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        return bitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (
                        grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA:{
                // If request is cancelled, the result arrays are empty.
                if (
                        grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
                return;
            }
        }
    }
}