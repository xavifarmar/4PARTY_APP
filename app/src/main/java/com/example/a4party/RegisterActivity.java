package com.example.a4party;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
        Button registerBtn;
        String urlRegister =  "http://10.0.2.2/4PARTY/register.php";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_register);

            registerBtn = findViewById(R.id.login_btn);
            registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    registerUser();
                }
            });

        }
        public void registerUser(){

            EditText usernameTxt = findViewById(R.id.username_txt);
            EditText passwordTxt = findViewById(R.id.password_txt);
            EditText confirmPasswdTxt = findViewById(R.id.confirm_password_txt);
            EditText emailTxt = findViewById(R.id.email_txt);

            final String username = usernameTxt.getText().toString();
            final String password =  passwordTxt.getText().toString();
            final String confirmPasswd =  confirmPasswdTxt.getText().toString();
            final String email = emailTxt.getText().toString();

           StringRequest request = new StringRequest(Request.Method.POST, urlRegister,
                   new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           if (response.equalsIgnoreCase("Usuario registrado correctamente")) {
                               Toast.makeText(RegisterActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();

                               Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                               startActivity(intent);
                           } else {
                               Toast.makeText(RegisterActivity.this, "No se pudo crear el usuario", Toast.LENGTH_SHORT).show();
                           }
                       }
                   }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   Toast.makeText(RegisterActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
               }
           }) {
               @Nullable
               @Override
               protected Map<String, String> getParams() throws AuthFailureError {
                   Map<String, String> params = new HashMap<>();
                   params.put("username", username);
                   params.put("email", email);
                   params.put("password", password);
                   params.put("confirm_passwd", confirmPasswd);

                   return params;
               }
           };
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            requestQueue.add(request);


        }

        //IR DE LA PANTALLA DE INICIO DE SESION A LA DE REGISTRARSE
        public void navigateToLogin(View v){
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        }
}
