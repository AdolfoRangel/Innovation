package com.rangel.innovation.ui.home.res;


import com.rangel.innovation.ui.model.UsuariosList;



public interface UsuarioRestListener {

    void onSucces(UsuariosList result);

    void onError(Throwable result);


}
