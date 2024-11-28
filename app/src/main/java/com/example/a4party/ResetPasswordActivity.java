package com.example.a4party;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {

    // URLs de las APIs
    String urlSendMail = "http://10.0.2.2/4PARTY/sendMail.php";
    String urlVerifyCode = "http://10.0.2.2/4PARTY/verifyCode.php";
    String urlResetPassword = "http://10.0.2.2/4PARTY/resetpasswd.php";

    // Campos del layout
    EditText emailTxt, codeTxt, newPasswdTxt;
    Button sendCodeBtn, verifyCodeBtn, resetPassBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passwd); // Reutilizamos un único layout

        // Vincular elementos
        emailTxt = findViewById(R.id.email_txt);
        codeTxt = findViewById(R.id.codeTxt);
        newPasswdTxt = findViewById(R.id.newPasswordTxt);

        sendCodeBtn = findViewById(R.id.sendCodeBtn);
        verifyCodeBtn = findViewById(R.id.verifyCodeBtn);
        resetPassBtn = findViewById(R.id.resetPassBtn);

        // Configurar visibilidad inicial
        codeTxt.setVisibility(View.GONE);
        verifyCodeBtn.setVisibility(View.GONE);
        newPasswdTxt.setVisibility(View.GONE);
        resetPassBtn.setVisibility(View.GONE);

        // Configurar eventos
        sendCodeBtn.setOnClickListener(view -> sendCodeEmail());
        verifyCodeBtn.setOnClickListener(view -> verifyCode());
        resetPassBtn.setOnClickListener(view -> resetPasswd());
    }

    // Enviar correo con el código OTP
    public void sendCodeEmail() {
        String email = emailTxt.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(this, "Debe ingresar un correo", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, urlSendMail,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");

                        if (message.equals("Mensaje enviado con éxito")) {
                            Toast.makeText(this, "Correo enviado", Toast.LENGTH_LONG).show();
                            // Mostrar el siguiente paso
                            codeTxt.setVisibility(View.VISIBLE);
                            verifyCodeBtn.setVisibility(View.VISIBLE);
                            sendCodeBtn.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar la respuesta", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión.", Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    // Verificar código OTP
    public void verifyCode() {
        String code = codeTxt.getText().toString();

        if (code.isEmpty()) {
            Toast.makeText(this, "Debe ingresar el código", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, urlVerifyCode,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");

                        if (message.equals("OTP válido")) {
                            Toast.makeText(this, "Código verificado", Toast.LENGTH_LONG).show();
                            // Mostrar el siguiente paso
                            newPasswdTxt.setVisibility(View.VISIBLE);
                            resetPassBtn.setVisibility(View.VISIBLE);
                            codeTxt.setVisibility(View.GONE);
                            verifyCodeBtn.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(this, "Código incorrecto", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar la respuesta", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión.", Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("otp", code);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    // Cambiar la contraseña del usuario
    public void resetPasswd() {
        String newPasswd = newPasswdTxt.getText().toString();

        if (newPasswd.isEmpty()) {
            Toast.makeText(this, "Debe ingresar una nueva contraseña", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, urlResetPassword,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String message = jsonResponse.getString("message");

                        if (message.equals("Contraseña actualizada con éxito")) {
                            Toast.makeText(this, "Contraseña cambiada", Toast.LENGTH_LONG).show();
                            finish(); // Cierra la actividad
                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error al procesar la respuesta", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error de conexión.", Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("newPasswd", newPasswd);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}
