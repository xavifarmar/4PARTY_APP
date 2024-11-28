package com.example.a4party;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    String urlLogin = "http://10.0.2.2/4PARTY/login.php"; // La URL del servidor

    private int loginAttempts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        loginBtn = findViewById(R.id.login_btn); // Botón de login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Llamar al método de login cuando se hace clic en el botón
                loginUsers();
                loginAttempts = 0;
            }
        });
    }

    public void loginUsers() {
        EditText emailTxt = findViewById(R.id.email_txt); // Obtener el campo de email
        EditText passwordTxt = findViewById(R.id.password_txt); // Obtener el campo de contraseña

        final String email = emailTxt.getText().toString(); // Obtener el email del campo de texto
        final String password = passwordTxt.getText().toString(); // Obtener la contraseña del campo de texto

        // Crear la solicitud HTTP POST para hacer login
        StringRequest request = new StringRequest(Request.Method.POST, urlLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsear la respuesta JSON
                            JSONObject jsonResponse = new JSONObject(response);
                            String message = jsonResponse.getString("message");

                            if (message.equals("Inicio de sesión exitoso")) {
                                // Si el login es correcto, redirigir a HomeActivity
                                Toast.makeText(LoginActivity.this, "Inicio de sesion correcto", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            } else {
                                // Si el login es incorrecto, mostrar un mensaje de error
                                Toast.makeText(LoginActivity.this, "Inicio de sesion incorrecto", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Si la petición falla, mostrar mensaje de error
                        Toast.makeText(LoginActivity.this, "Error de conexión.", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);  // Agregar el email a los parámetros
                params.put("password", password); // Agregar la contraseña a los parámetros
                return params;
            }
        };

        // Crear y añadir la solicitud a la cola de peticiones
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(request);
    }

    // Navegar a la actividad de registro
    public void navigateToRegister(View v) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    public void navigateToResetPassword(View v) {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

}
