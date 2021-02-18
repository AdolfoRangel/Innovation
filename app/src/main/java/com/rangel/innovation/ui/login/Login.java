package com.rangel.innovation.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rangel.innovation.MainActivity;
import com.rangel.innovation.R;
import com.rangel.innovation.databinding.ActivityLoginBinding;
import com.rangel.innovation.databinding.ActivitySplashBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

public class Login extends AppCompatActivity {
    String TAG = "Login";

    private String UPLOAD_URL = "http://dummy.restapiexample.com/api/v1/employee/";

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());


        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(binding.getRoot());


        binding.login.setOnClickListener(v -> {
            Log.d(TAG, "onCreate: "+binding.idUser.getText().toString());
          if(binding.idUser.getText().toString().equals("")){
              Toast.makeText(getApplicationContext(),"Porfavor pon tu ID",Toast.LENGTH_SHORT).show();
          }else {
              final ProgressDialog loading = ProgressDialog.show(Login.this, "", "Espere por favor...", false, false);

              StringRequest stringRequest = new StringRequest(Request.Method.GET, UPLOAD_URL+binding.idUser.getText().toString(), new Response.Listener<String>() {
                  @Override
                  public void onResponse(String s) {
                      // guardar datos y cerrar
                      Log.d(TAG, "onResponse: "+s);

                      SharedPreferences userDetails = getApplicationContext().getSharedPreferences("usuario", MODE_PRIVATE);
                      SharedPreferences.Editor edit = userDetails.edit();
                      Log.d("OPE", s + "___");
                      if (s.trim().equals("")) {
                          Toast.makeText(getApplicationContext(), "Ocurrió un error, intente de nuevo.", Toast.LENGTH_SHORT).show();
                          loading.dismiss();
                      } else if (s.trim().contains("\"data\":null")) {
                          Toast.makeText(getApplicationContext(), "Tu id es incorrecto, intenta nuevamente", Toast.LENGTH_SHORT).show();
                          loading.dismiss();
                      } else {
                          try {
                              JSONObject jsonObject = new JSONObject(s);
                              JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                              String  name =  jsonObject1.getString("employee_name");
                              edit.putString("user", name);
                          }catch (JSONException err){
                              Log.d("Error", err.toString());
                          }
                          loading.dismiss();
                          edit.putString("id", binding.idUser.getText().toString());
                          edit.apply();
                          Intent intent = new Intent(Login.this, MainActivity.class);
                          startActivity(intent);
                          finish();
                      }

                  }
              },
                      new Response.ErrorListener() {
                          @Override
                          public void onErrorResponse(VolleyError volleyError) {
                              //Descartar el diálogo de progreso
                              loading.dismiss();
                              //Showing toast
                              Toast.makeText(Login.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                              Log.d(TAG, "onErrorResponse: "+volleyError.getMessage());
                          }
                      }) {
                  @Override
                  protected Map<String, String> getParams() throws AuthFailureError {
                      Map<String, String> params = new Hashtable<String, String>();
                      //params.put("id", binding.idUser.getText().toString());
                      return params;
                  }
              };
              //Creación de una cola de solicitudes
              RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
              //Agregar solicitud a la cola
              requestQueue.add(stringRequest);
          }

        });
    }
}