package com.rangel.innovation.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.rangel.innovation.MainActivity;
import com.rangel.innovation.R;
import com.rangel.innovation.databinding.ActivitySplashBinding;
import com.rangel.innovation.ui.login.Login;

public class Splash extends AppCompatActivity {
    String TAG = "Splash";
    // Duración en milisegundos que se mostrará el splash
    private final int DURACION_SPLASH = 3000; // 3 segundos

    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());

        // Tenemos una plantilla llamada splash.xml donde mostraremos la información que queramos (logotipo, etc.)

        setContentView(binding.getRoot());

        SharedPreferences userDetails = getApplicationContext().getSharedPreferences("usuario", MODE_PRIVATE);
        final String id_usuario = userDetails.getString("id", "");

        new Handler().postDelayed(new Runnable() {
            public void run() {
                // Cuando pasen los 3 segundos, pasamos a la actividad principal de la aplicación

                if (!id_usuario.equals("")) {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "run:  login ");
                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }

            ;
        }, DURACION_SPLASH);
    }
}