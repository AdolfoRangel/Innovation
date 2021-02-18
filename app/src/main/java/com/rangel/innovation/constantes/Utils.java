package com.rangel.innovation.constantes;

public class Utils {
    public static final String DB_NOMBRE = "db_imagenes";
    public static final String TABLA_IMAGEN= "imagen";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_IMG = "img";


    public static final String CREAR_TABLA_IMG =
            "CREATE TABLE "+TABLA_IMAGEN+" ("+CAMPO_ID+" INTEGER, "+ CAMPO_IMG +" BLOB)";

}
