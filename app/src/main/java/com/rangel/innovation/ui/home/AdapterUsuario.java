package com.rangel.innovation.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rangel.innovation.R;
import com.rangel.innovation.databinding.ItemUsuarioBinding;
import com.rangel.innovation.ui.model.Usuario;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import androidx.recyclerview.widget.RecyclerView;

public class AdapterUsuario extends RecyclerView.Adapter<AdapterUsuario.ViewHolder> {
    private String TAG = "AdapterUsuario";
    private Context iContext;
    private List<Usuario> listItems;
    private LayoutInflater iInflater;

    public AdapterUsuario(Context iContext, List<Usuario> listItems) {
        this.iContext = iContext;
        this.listItems = listItems;
        this.iInflater = LayoutInflater.from(iContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ItemUsuarioBinding binding = ItemUsuarioBinding.inflate(iInflater,parent,false);
        return new ViewHolder(binding);
        //return new ViewHolder(iInflater.inflate(R.layout.item_usuario, parent, false));
    }

    @Override
    public void onBindViewHolder(AdapterUsuario.ViewHolder holder, int position) {
        final Usuario usuario = listItems.get(position);
        holder.setData(usuario);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(iContext, DetalleUsuario.class);
                intent.putExtra(DetalleUsuario.ID, usuario.getId());
                intent.putExtra(DetalleUsuario.NOMBRE, usuario.getEmployeeName());
                intent.putExtra(DetalleUsuario.SALARIO, usuario.getEmployeeSalary());
                intent.putExtra(DetalleUsuario.EDAD, usuario.getEmployeeAge());
                intent.putExtra(DetalleUsuario.IMAGE, usuario.getProfileImage());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemUsuarioBinding binding;

        public ViewHolder(ItemUsuarioBinding binding) {
            super(binding.getRoot());
            this.binding =binding;

        }

        public void setData(Usuario usuario) {
            int numberEdad =  Integer.parseInt(usuario.getEmployeeAge());
            binding.tvEdad.setText(iContext.getString(R.string.edad) + usuario.getEmployeeAge());
            binding.tvId.setText(iContext.getString(R.string.id_)+usuario.getId());
            binding.tvNambre.setText(usuario.getEmployeeName());
            int numberSalary =  Integer.parseInt(usuario.getEmployeeSalary());
            binding.tvSalario.setText(iContext.getString(R.string.salario)+getFormatedAmount(numberSalary));
            if(numberEdad > 25 && numberEdad < 35 ){
                binding.tvEdad.setTextColor(Color.parseColor("#008F39"));
            }else{
                binding.tvEdad.setTextColor(Color.parseColor("#FF0000"));
            }
        }

    }
    private String getFormatedAmount(int amount){
        return NumberFormat.getNumberInstance(Locale.US).format(amount);
    }
}
