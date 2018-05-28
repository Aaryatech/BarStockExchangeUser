package com.ats.barstockexchangeuser.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.barstockexchangeuser.R;
import com.ats.barstockexchangeuser.bean.LoginData;
import com.ats.barstockexchangeuser.util.CheckNetwork;
import com.ats.barstockexchangeuser.util.InterfaceApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edUsername, edPassword;
    private TextView tvLogin, tvCreateAcc, tvForgetPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edUsername = findViewById(R.id.edUsername);
        edPassword = findViewById(R.id.edPassword);
        tvLogin = findViewById(R.id.tvLoginButton);
        tvCreateAcc = findViewById(R.id.tvLogin_CreateAcc);
        tvForgetPass = findViewById(R.id.tvLogin_ForgetPass);

        tvLogin.setOnClickListener(this);
        tvCreateAcc.setOnClickListener(this);
        tvForgetPass.setOnClickListener(this);

        edUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edUsername.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edUsername.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });
        edPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edPassword.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edPassword.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvLoginButton) {
            // progressDialog();
            // startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            // finish();

            String mobile = edUsername.getText().toString();
            String pass = edPassword.getText().toString();
            if (mobile.isEmpty()) {
                Toast.makeText(this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                edUsername.requestFocus();
            } else if (pass.isEmpty()) {
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                edPassword.requestFocus();
            } else {
                loginUser(mobile, pass);
            }

        } else if (view.getId() == R.id.tvLogin_CreateAcc) {
            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
        } else if (view.getId() == R.id.tvLogin_ForgetPass) {
            startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void progressDialog() {

        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_progress_layout);
        dialog.show();
    }


    public void loginUser(String mobile, String password) {
        if (CheckNetwork.isInternetAvailable(LoginActivity.this)) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(InterfaceApi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            InterfaceApi api = retrofit.create(InterfaceApi.class);

            Call<LoginData> loginDataCall = api.doLogin(mobile, password);

            final Dialog progressDialog = new Dialog(LoginActivity.this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.loading_progress_layout);
            progressDialog.show();

            loginDataCall.enqueue(new Callback<LoginData>() {
                @Override
                public void onResponse(Call<LoginData> call, retrofit2.Response<LoginData> response) {
                    try {
                        if (response.body() != null) {
                            LoginData data = response.body();
                            if (data.getErrorMessage().getError()) {
                                progressDialog.dismiss();
                                //Log.e("ON RESPONSE : ", "ERROR : " + data.getErrorMessage().getMessage());
                                Toast.makeText(LoginActivity.this, "Sorry, Unable to Login", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                SharedPreferences pref = getApplicationContext().getSharedPreferences(InterfaceApi.MY_PREF, MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putInt("userId", data.getUserId());
                                editor.putString("userName", data.getUserName());
                                editor.apply();

                                //  Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            }

                        } else {
                            progressDialog.dismiss();
                            //Log.e("ON RESPONSE : ", "NO DATA");
                            Toast.makeText(LoginActivity.this, "Sorry, Unable to Login", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        //Log.e("Exception : ", "" + e.getMessage());
                        Toast.makeText(LoginActivity.this, "Sorry, Unable to Login", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginData> call, Throwable t) {
                    progressDialog.dismiss();
                    //Log.e("ON FAILURE : ", "ERROR : " + t.getMessage());
                    Toast.makeText(LoginActivity.this, "Sorry, Unable to Login", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AlertDialogTheme);
            builder.setTitle("Check Connectivity");
            builder.setCancelable(false);
            builder.setMessage("Please Connect To Internet");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    //String token = SharedPrefManager.getmInstance(this).getDeviceToken();

}
