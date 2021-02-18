package com.rangel.innovation.ui.home;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.rangel.innovation.R;
import com.rangel.innovation.databinding.ActivityDetalleUsuarioBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class DetalleUsuario extends AppCompatActivity {

    ActivityDetalleUsuarioBinding binding;

public static final String ID ="id";
public static final String NOMBRE ="employee_name";
public static final String SALARIO ="employee_salary";
public static final String EDAD ="employee_age";
public static final String IMAGE ="profileImage";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetalleUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * funcion para color elizquierdo flecha parte 1
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.empleado_individual);

        Bundle bundle = getIntent().getExtras();


        int numberSalary =  Integer.parseInt(bundle.getString(SALARIO));
        binding.tvNombre1.setText(bundle.getString(NOMBRE));
        binding.tvEdad1.setText(bundle.getString(EDAD));
        binding.tvSalario1.setText(getFormatedAmount(numberSalary));
        binding.tvId1.setText(bundle.getString(ID));

        if(numberSalary>1000){
            binding.tvSalario1.setTextColor(Color.parseColor("#008F39"));
        }else{
            binding.tvSalario1.setTextColor(Color.parseColor("#FF0000"));
        }

    }

    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }




    /**
     * funcion para color el izquierdo flecha parte 2
     */
    @Override
    public boolean onSupportNavigateUp() {
        //code it to launch an intent to the activity you want
        finish();
        return true;
    }

}