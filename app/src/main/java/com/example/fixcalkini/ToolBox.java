package com.example.fixcalkini;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ToolBox {
    private static final String sharePreferenceSesion = "fixCalkiniSesion";
    private static final String testSesion = "login";

    // Verifica si el usuario ha iniciado sesión
    public static boolean testEstadoSesion(Context context) {
        SharedPreferences pref = context.getSharedPreferences(sharePreferenceSesion, Context.MODE_PRIVATE);
        boolean sessionStatus = pref.getBoolean(testSesion, false);
        Log.d("ToolBox", "Session status: " + sessionStatus);
        return sessionStatus;
    }

    // Modifica el estado de la sesión
    public static void setEstadoSesion(Context context, boolean newStatus) {
        SharedPreferences pref = context.getSharedPreferences(sharePreferenceSesion, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(testSesion, newStatus);
        boolean isApplied = editor.commit();  // Usar commit() para obtener confirmación de éxito
        Log.d("ToolBox", "Session status set to: " + newStatus + ", commit result: " + isApplied);
    }

    public static void guardarCorreo(Context context, String correo) {
        SharedPreferences pref = context.getSharedPreferences("fixCalkiniSesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("usuarioCorreo", correo);  // Guardamos el correo con la clave "usuarioCorreo"
        editor.apply();  // Usamos apply() para guardar de manera asincrónica
        Log.d("ToolBox", "Correo guardado: " + correo);  // Opcional: Para verificar en los logs
    }
    public static String obtenerCorreo(Context context) {
        SharedPreferences pref = context.getSharedPreferences("fixCalkiniSesion", Context.MODE_PRIVATE);
        return pref.getString("usuarioCorreo", "");  // Devolvemos un valor por defecto vacío si no se encuentra el correo
    }


}
