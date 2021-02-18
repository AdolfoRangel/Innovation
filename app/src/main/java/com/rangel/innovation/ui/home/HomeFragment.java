package com.rangel.innovation.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rangel.innovation.R;
import com.rangel.innovation.ui.home.res.UsuarioRestAdapter;
import com.rangel.innovation.ui.home.res.UsuarioRestListener;
import com.rangel.innovation.ui.model.Usuario;
import com.rangel.innovation.ui.model.UsuariosList;

import java.util.List;

public class HomeFragment extends Fragment implements UsuarioRestListener {
    UsuarioRestAdapter usuarioRestAdapter = new UsuarioRestAdapter(this);
    private String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

/*
        final TextView textView = root.findViewById(R.id.text_home);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

         recyclerView = root.findViewById(R.id.rv);
        usuarioRestAdapter.ideas();
        Log.d(TAG, "onCreateView: Paso aqui 2");


        return root;
    }

    @Override
    public void onSucces(UsuariosList result) {
        setIdeasRestAdapter(result.getData());
        Log.d(TAG, "onSucces: PAso aqui?");
    }

    @Override
    public void onError(Throwable result) {
        Toast.makeText(getContext(), "No se pudo obtener la lista de usuarios", Toast.LENGTH_SHORT).show();
    }

    public void setIdeasRestAdapter(List<Usuario> Lista){
        AdapterUsuario adpt = new AdapterUsuario(getContext(),Lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adpt);
    }
}