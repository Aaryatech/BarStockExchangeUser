package com.ats.barstockexchangeuser.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.barstockexchangeuser.R;
import com.ats.barstockexchangeuser.bean.ErrorMessage;
import com.ats.barstockexchangeuser.util.CheckNetwork;
import com.ats.barstockexchangeuser.util.InterfaceApi;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edMobile, edOTP, edPass, edConfirmPass;
    private TextView tvSubmitOTP, tvSubmit, tvSubmitMobile;
    private LinearLayout llMobile, llOTP, llPassword;

    private String randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edMobile = findViewById(R.id.edForgetPass_Mobile);
        edOTP = findViewById(R.id.edForgetPass_OTP);
        edPass = findViewById(R.id.edForgetPass_Password);
        edConfirmPass = findViewById(R.id.edForgetPass_ConfirmPassword);

        tvSubmit = findViewById(R.id.tvForgetPass_Submit);
        tvSubmitOTP = findViewById(R.id.tvForgetPass_SubmitOTP);
        tvSubmitMobile = findViewById(R.id.tvForgetPass_SubmitMobile);

        tvSubmit.setOnClickListener(this);
        tvSubmitOTP.setOnClickListener(this);
        tvSubmitMobile.setOnClickListener(this);

        llMobile = findViewById(R.id.llForgetPass_Mobile);
        llOTP = findViewById(R.id.llForgetPass_OTP);
        llPassword = findViewById(R.id.llForgetPass_Password);

        edMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edMobile.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edMobile.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });

        edOTP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edOTP.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edOTP.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });

        edPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edPass.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edPass.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });

        edConfirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edConfirmPass.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edConfirmPass.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvForgetPass_SubmitMobile) {

            if (edMobile.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile.requestFocus();
            } else if (edMobile.getText().toString().length() != 10) {
                Toast.makeText(this, "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile.requestFocus();
            } else {
                checkUser(edMobile.getText().toString());
            }


        } else if (view.getId() == R.id.tvForgetPass_SubmitOTP) {
            String otp = edOTP.getText().toString();
            if (randomNumber.equals(otp)) {
                llOTP.setVisibility(View.GONE);
                llPassword.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "OTP Not Matched", Toast.LENGTH_SHORT).show();
                edOTP.requestFocus();
            }
        } else if (view.getId() == R.id.tvForgetPass_Submit) {

            if (edPass.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                edPass.requestFocus();
            } else if (edConfirmPass.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                edConfirmPass.requestFocus();
            }
            if (!edPass.getText().toString().equals(edConfirmPass.getText().toString())) {
                Toast.makeText(this, "Password Not Matched", Toast.LENGTH_SHORT).show();
                edConfirmPass.requestFocus();
            } else {
                changePass(edMobile.getText().toString(), edPass.getText().toString());
            }

        }
    }


    public void checkUser(final String mobile) {
        if (CheckNetwork.isInternetAvailable(ForgetPasswordActivity.this)) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(InterfaceApi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            InterfaceApi api = retrofit.create(InterfaceApi.class);

            Call<ErrorMessage> errorMessageCall = api.checkUserExist(mobile);

            final Dialog progressDialog = new Dialog(ForgetPasswordActivity.this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.loading_progress_layout);
            progressDialog.show();

            errorMessageCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, retrofit2.Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {
                            ErrorMessage data = response.body();
                            if (data.getError()) {
                                progressDialog.dismiss();
                                //Log.e("ON RESPONSE : ", "ERROR : " + data.getMessage());

                                if (data.getMessage().equalsIgnoreCase("Failed")) {
                                    randomNumber = random(6);
                                    send(mobile, "Change password for SHAIL app Your OTP Number is " + randomNumber + " Please do not share this OTP.");
                                    llMobile.setVisibility(View.GONE);
                                    llOTP.setVisibility(View.VISIBLE);
                                }


                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(ForgetPasswordActivity.this, "User Is Not Registered", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                            //Log.e("ON RESPONSE : ", "NO DATA");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ForgetPasswordActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                        //Log.e("Exception : ", "" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ForgetPasswordActivity.this, "Unable To Process", Toast.LENGTH_SHORT).show();
                    //Log.e("ON FAILURE : ", "ERROR : " + t.getMessage());
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this, R.style.AlertDialogTheme);
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

    public void changePass(String mobile, String pass) {
        if (CheckNetwork.isInternetAvailable(ForgetPasswordActivity.this)) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(InterfaceApi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            InterfaceApi api = retrofit.create(InterfaceApi.class);

            Call<ErrorMessage> errorMessageCall = api.changePassword(mobile, pass);

            final Dialog progressDialog = new Dialog(ForgetPasswordActivity.this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.loading_progress_layout);
            progressDialog.show();

            errorMessageCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, retrofit2.Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {
                            ErrorMessage data = response.body();
                            if (data.getError()) {
                                progressDialog.dismiss();
                                //Log.e("ON RESPONSE : ", "ERROR : " + data.getMessage());

                                Toast.makeText(ForgetPasswordActivity.this, "Unable To Change Password", Toast.LENGTH_SHORT).show();


                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(ForgetPasswordActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                                finish();
                            }

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, "Unable To Change Password", Toast.LENGTH_SHORT).show();
                            //Log.e("ON RESPONSE : ", "NO DATA");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ForgetPasswordActivity.this, "Unable To Change Password", Toast.LENGTH_SHORT).show();
                        //Log.e("Exception : ", "" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(ForgetPasswordActivity.this, "Unable To Change Password", Toast.LENGTH_SHORT).show();
                    //Log.e("ON FAILURE : ", "ERROR : " + t.getMessage());
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this, R.style.AlertDialogTheme);
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

    public static String random(int size) {

        StringBuilder generatedToken = new StringBuilder();
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            // Generate 20 integers 0..20
            for (int i = 0; i < size; i++) {
                generatedToken.append(number.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return generatedToken.toString();
    }

    public void send(String mobile, String message) {
        if (CheckNetwork.isInternetAvailable(ForgetPasswordActivity.this)) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .header("Accept", "application/json")
                                    .method(original.method(), original.body())

                                    .build();

                            Response response = chain.proceed(request);
                            return response;
                        }
                    })
                    .readTimeout(20000, TimeUnit.SECONDS)
                    .connectTimeout(20000, TimeUnit.SECONDS)
                    .writeTimeout(20000, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://sms.sminfomedia.in/vendorsms/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build();

            InterfaceApi myInterface = retrofit.create(InterfaceApi.class);

           // Call<JsonObject> call = myInterface.sendSMS("140742AbB1cy8zZt589c06d5", mobile, message, "RCONNT", "4", "91", "json");
            Call<JsonObject> call = myInterface.sendSMSAPI("lavazza","lavazza",mobile,"BARSHL",message,0,2);
            final Dialog progressDialog = new Dialog(ForgetPasswordActivity.this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.loading_progress_layout);
            progressDialog.show();

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    progressDialog.dismiss();
                    //Log.e("Json Object ", " is " + response.body().toString());
                    //Toast.makeText(RegistrationActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    llMobile.setVisibility(View.GONE);
                    llOTP.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    //Log.e("call failed ", " .. " + t.getMessage());
                    Toast.makeText(ForgetPasswordActivity.this, "Oops Something Went Wrong!\nPlease Try Again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
