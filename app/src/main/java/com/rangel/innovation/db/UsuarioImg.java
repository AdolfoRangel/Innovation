package com.rangel.innovation.db;

import android.graphics.Bitmap;

public class UsuarioImg {
    private Integer Id;
    private Byte[] img;

    public UsuarioImg() {
    }

    public UsuarioImg(Integer id, Byte[] img) {
        Id = id;
        this.img = img;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Byte[] getImg() {
        return img;
    }

    public void setImg(Byte[] img) {
        this.img = img;
    }
}
