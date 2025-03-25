package com.example.fixcalkini;


import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Reglas {

    // Método para eliminar espacios innecesarios en nombre y correo
    private static String limpiarTexto(String texto) {
        return texto.trim().replaceAll("\\s+", " "); // Reemplaza múltiples espacios por un solo espacio
    }

    // Validar Nombre: mínimo 3 caracteres, sin estar vacío y sin espacios extra
    public static int validarNombre(String nombre) {
        nombre = limpiarTexto(nombre); // Eliminar espacios extra

        if (nombre.isBlank() || nombre.length() < 3) {
            return 2; // Inválido
        }
        return 1; // Válido
    }

    // Validar Correo: debe tener formato válido y sin espacios extra
    public static int validarCorreo(String correo) {
        correo = limpiarTexto(correo); // Eliminar espacios extra
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(correo);

        if (!matcher.matches()) {
            return 2; // Inválido
        }
        return 1; // Válido
    }

    // Validar Contraseña: mínimo 8 caracteres, una mayúscula, una minúscula, un número
    public static int validarContrasena(String pass) {
        pass = pass.trim();

        if (pass.length() < 8) {
            return 2; // Inválida
        }

        // Debe contener al menos un número
        if (!pass.matches(".*[0-9].*")) {
            return 2;
        }

        // Debe contener al menos una letra mayúscula
        if (!pass.matches(".*[A-Z].*")) {
            return 2;
        }

        // Debe contener al menos una letra minúscula
        if (!pass.matches(".*[a-z].*")) {
            return 2;
        }



        return 1; // Válida
    }

    // Validar Confirmación de Contraseña: debe coincidir con la contraseña
    public static int validarConfirmarContrasena(String pass, String confirmPass) {
        if (!pass.equals(confirmPass)) {
            return 2; // No coinciden
        }
        return 1; // Coinciden
    }

    // Validar Login: verifica que el correo y contraseña no estén vacíos
    public static boolean validarLogin(String correo, String pass) {
        correo = limpiarTexto(correo); // Limpiar espacios extra
        return !correo.isBlank() && !pass.isBlank();
    }
}
