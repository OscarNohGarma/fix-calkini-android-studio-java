package com.example.fixcalkini;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fixcalkini.admin.AdminMainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    boolean testGoogle = false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private ImageView viewImg;
    private LinearLayout layoutLogin, layoutRegistro;
    private Button btnLogin, btnRegistro;
    private ImageButton btnGoogle;
    private EditText edit_nombre, edit_correo2,edit_correo1, edit_contra2, edit_contra1, edit_confirmar2;
    private TextView btnRegis, btnVolverLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        testGoogle = false;

        viewImg = findViewById(R.id.idcarrite);
        layoutLogin = findViewById(R.id.layout_login);
        layoutRegistro = findViewById(R.id.layout_registro);
        btnLogin = findViewById(R.id.btn_inicio);

        btnRegistro = findViewById(R.id.btn_registro);
        btnRegis = findViewById(R.id.btn_regis);
        btnVolverLogin = findViewById(R.id.btn_volver_login);
        btnGoogle = findViewById(R.id.btn_google);


        edit_nombre = findViewById(R.id.edit_nombre);
        edit_correo1 = findViewById(R.id.edit_correo1);
        edit_correo2 = findViewById(R.id.edit_correo2);
        edit_contra2 = findViewById(R.id.edit_contra2);
        edit_contra1 = findViewById(R.id.edit_contra1);
        edit_confirmar2 = findViewById(R.id.edit_confirmaContra);

        // Animaciones
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        Animation slideInLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
        Animation slideOutRight = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);

        Animation fromTop = AnimationUtils.loadAnimation(this, R.anim.anim_from_top);
        viewImg.startAnimation(fromTop);


        // Configurar Google Sign-In
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Asegúrate de tener el ID de cliente correcto en `google-services.json`
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        //Registro con google
        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
        //VENTANA REGISTRO
        btnRegistro.setOnClickListener(v -> {

            if(interfazRegistro()){
                // Verificar si el correo ya está registrado en Firestore
                db.collection("users").document(edit_correo2.getText().toString()).get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        //alert();
                        Toast.makeText(getApplicationContext(), "Esta cuenta ya está registrada", Toast.LENGTH_LONG).show();
                    } else {
                        long timestamp = System.currentTimeMillis();
                        String fechaLegible = convertirTimestamp(timestamp);
                        if(testGoogle == false){
                            // Crear el usuario en Firebase Authentication
                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(edit_correo2.getText().toString(), edit_contra2.getText().toString()).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {

                                    // Metodo Hash para guardar los datos
                                    HashMap<String, String> usuarioMash = new HashMap<>();
                                    usuarioMash.put("nombre", edit_nombre.getText().toString());
                                    usuarioMash.put("email", edit_correo2.getText().toString());
                                    usuarioMash.put("password", edit_contra2.getText().toString());
                                    usuarioMash.put("tipo", "usuario");
                                    usuarioMash.put("fecha_registro", fechaLegible);

                                    // Guardar el usuario en la base de datos de Firestore
                                    db.collection("users").document(edit_correo2.getText().toString()).set(usuarioMash).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {

                                            Toast.makeText(getApplicationContext(), "Registro exitoso:)", Toast.LENGTH_SHORT).show();
                                            layoutLogin.setVisibility(View.VISIBLE); // Hacemos visible el login antes de iniciar animación
                                            layoutLogin.startAnimation(slideInLeft); // Se desliza dentro

                                            slideOutRight.setAnimationListener(new Animation.AnimationListener() {
                                                @Override
                                                public void onAnimationStart(Animation animation) {}

                                                @Override
                                                public void onAnimationEnd(Animation animation) {
                                                    layoutRegistro.setVisibility(View.GONE); // Ocultamos hasta que termine la animación
                                                }

                                                @Override
                                                public void onAnimationRepeat(Animation animation) {}
                                            });

                                            layoutRegistro.startAnimation(slideOutRight); // Se desliza fuera
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error al guardar:(", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    alert(); // Llamada a tu método de alerta en caso de error (si lo tienes)
                                }
                            });
                        }else{
                            // Metodo Hash para guardar los datos
                            HashMap<String, String> usuarioMash = new HashMap<>();
                            usuarioMash.put("nombre", edit_nombre.getText().toString());
                            usuarioMash.put("email", edit_correo2.getText().toString());
                            usuarioMash.put("password", "");
                            usuarioMash.put("tipo", "usuario");
                            usuarioMash.put("fecha_registro", fechaLegible);

                            // Guardar el usuario en la base de datos de Firestore
                            db.collection("users").document(edit_correo2.getText().toString()).set(usuarioMash).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    cargar(edit_correo2.getText().toString());
                                    Toast.makeText(getApplicationContext(), "Registro exitoso:)", Toast.LENGTH_SHORT).show();
                                    layoutLogin.setVisibility(View.VISIBLE); // Hacemos visible el login antes de iniciar animación
                                    layoutLogin.startAnimation(slideInLeft); // Se desliza dentro

                                    slideOutRight.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {}

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            layoutRegistro.setVisibility(View.GONE); // Ocultamos hasta que termine la animación
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {}
                                    });

                                    layoutRegistro.startAnimation(slideOutRight); // Se desliza fuera
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error al guardar:(", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });

            }
        });


        btnRegis.setOnClickListener(v -> {
            layoutRegistro.setVisibility(View.VISIBLE); // Hacemos visible el registro antes de iniciar animación
            layoutRegistro.startAnimation(slideIn); // Se desliza dentro

            slideOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    layoutLogin.setVisibility(View.GONE); // Ocultamos hasta que termine la animación
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            layoutLogin.startAnimation(slideOut); // Se desliza fuera
        });

        btnVolverLogin.setOnClickListener(v -> {
            layoutLogin.setVisibility(View.VISIBLE); // Hacemos visible el login antes de iniciar animación
            layoutLogin.startAnimation(slideInLeft); // Se desliza dentro

            slideOutRight.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    layoutRegistro.setVisibility(View.GONE); // Ocultamos hasta que termine la animación
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            layoutRegistro.startAnimation(slideOutRight); // Se desliza fuera
        });



        btnLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(iniciarsesion()){
                    db.collection("users").document(edit_correo1.getText().toString()).get()
                            .addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String pwd = documentSnapshot.getString("password");
                                    if (pwd != null && pwd.equals(edit_contra1.getText().toString())) {
                                        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                                                edit_correo1.getText().toString(),
                                                edit_contra1.getText().toString()
                                        ).addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                cargar(task.getResult().getUser().getEmail());
                                            }
                                        });

                                    } else {
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "Contraseña incorrecta",
                                                Toast.LENGTH_LONG
                                        ).show();
                                    }
                                } else {
                                    error();
                                }
                            });


                }

            }
        });

    }
    private String convertirTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(timestamp);
        return sdf.format(date);
    }

    private boolean iniciarsesion() {
        boolean sucesfull = true;
        if(!(Reglas.validarCorreo(edit_correo1.getText().toString()) == 1)){
            edit_correo1.setError("Correo no valido");
            sucesfull = false;
        }
        if(!(Reglas.validarContrasena(edit_contra1.getText().toString()) == 1)){
            edit_contra1.setError("Password invalido");
            sucesfull = false;
        }
        return sucesfull;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // El inicio de sesión con Google falló, maneja el error
                // Captura el código de error
                Log.e("LoginActivity", "Google Sign-In failed: " + e.getStatusCode());
                Toast.makeText(this, "Google sign-in failed: " + e.getStatusCode(), Toast.LENGTH_SHORT).show();            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // El inicio de sesión con Firebase fue exitoso
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String email = user.getEmail();

                        db.collection("users").document(email).get().addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                cargar(email);
                                //alert();
                                Toast.makeText(getApplicationContext(), "Esta cuenta ya está registrada", Toast.LENGTH_LONG).show();
                            } else {
                                testGoogle = true;

                                edit_correo2.setText(email);
                                edit_correo2.setEnabled(false);
                                edit_contra2.setVisibility(View.GONE);
                                edit_confirmar2.setVisibility(View.GONE);
                                Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
                                Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);


                                layoutRegistro.setVisibility(View.VISIBLE); // Hacemos visible el registro antes de iniciar animación
                                layoutRegistro.startAnimation(slideIn); // Se desliza dentro

                                slideOut.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {}

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        layoutLogin.setVisibility(View.GONE); // Ocultamos hasta que termine la animación
                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {}
                                });

                                layoutLogin.startAnimation(slideOut); // Se desliza fuera
                                // Crear el usuario en Firebase Authentication

                            }
                        });

                    } else {
                        // Si el inicio de sesión falla, muestra un mensaje al usuario
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private boolean interfazRegistro() {

        boolean sucesfull = true;

        if(!(Reglas.validarNombre(edit_nombre.getText().toString()) == 1)){
            edit_nombre.setError("Campo vacio/ min 3 caracteres");
            sucesfull = false;
        }
        if(!(Reglas.validarCorreo(edit_correo2.getText().toString()) == 1) ){
            edit_correo2.setError("Correo no valido");
            sucesfull = false;
        }
        if(!(Reglas.validarContrasena(edit_contra2.getText().toString()) == 1) && testGoogle == false){
            edit_contra2.setError("Password invalido");
            sucesfull = false;
        }
        if(!(Reglas.validarConfirmarContrasena(edit_contra2.getText().toString(),edit_confirmar2.getText().toString()) == 1) && testGoogle == false){
            edit_confirmar2.setError("Password incompatibles");
            sucesfull = false;
        }
        return sucesfull;
    }
    private void error() {
        AlertDialog.Builder feik = new AlertDialog.Builder(this);
        feik.setTitle("ERROR");
        feik.setMessage("Usuario no registrado / Sin conexión");
        feik.setPositiveButton("Aceptar", null);

        AlertDialog nuevo = feik.create();
        nuevo.show();
    }


    private void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Fallo de registro / Sin conexión");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void cargar(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog, null));
        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        db.collection("users").document(email).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String tipoUsuario = documentSnapshot.getString("tipo");
                new Handler().postDelayed(() -> {
                    Intent intent;
                    if ("admin".equals(tipoUsuario)) {
                        intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                    } else {
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                    }
                    intent.putExtra("email", email);
                    ToolBox.setEstadoSesion(getApplicationContext(), true);
                    ToolBox.guardarCorreo(getApplicationContext(), email);

                    if ("admin".equals(tipoUsuario)) {
                        ToolBox.guardarTipoUsuario(getApplicationContext(), "admin");
                    } else {
                        ToolBox.guardarTipoUsuario(getApplicationContext(), "usuario");
                    }

                    startActivity(intent);
                    finish();
                }, 2000);
            } else {
                Toast.makeText(getApplicationContext(), "No se encontró el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getApplicationContext(), "Error al obtener el tipo de usuario", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }


}

