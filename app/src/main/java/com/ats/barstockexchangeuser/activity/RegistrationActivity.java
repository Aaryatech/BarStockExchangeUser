package com.ats.barstockexchangeuser.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.barstockexchangeuser.R;
import com.ats.barstockexchangeuser.bean.ErrorMessage;
import com.ats.barstockexchangeuser.bean.UserBean;
import com.ats.barstockexchangeuser.util.CheckNetwork;
import com.ats.barstockexchangeuser.util.InterfaceApi;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edName, edMobile, edPassword, edConfirmPass, edEmail, edDOB, edDOB_DD, edDOB_MM, edDOB_YY, edOtp;
    private TextView tvRegister, tvDOB, tvVerify;
    private LinearLayout llForm, llOTP;
    private String randomNumber;

    int yyyy, mm, dd;
    long dobMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edName = findViewById(R.id.edRegister_PersonName);
        edMobile = findViewById(R.id.edRegister_MobileNumber);
        edPassword = findViewById(R.id.edRegister_Password);
        edConfirmPass = findViewById(R.id.edRegister_ConfirmPassword);
        edOtp = findViewById(R.id.edRegister_OTP);
        tvRegister = findViewById(R.id.tvRegister_RegisterButton);
        tvVerify = findViewById(R.id.tvRegister_VerifyOTP);
        edEmail = findViewById(R.id.edRegister_Email);
        edDOB = findViewById(R.id.edRegister_DOB);
        tvDOB = findViewById(R.id.tvRegister_DOB);
        edDOB_DD = findViewById(R.id.edRegister_DOB_DD);
        edDOB_MM = findViewById(R.id.edRegister_DOB_MM);
        edDOB_YY = findViewById(R.id.edRegister_DOB_YY);

        llForm = findViewById(R.id.llRegister_Form);
        llOTP = findViewById(R.id.llRegister_OTP);

        tvRegister.setOnClickListener(this);
        tvVerify.setOnClickListener(this);
        edDOB.setOnClickListener(this);

        edName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edName.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edName.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });

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

        edOtp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edOtp.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edOtp.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });

        edDOB.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edDOB.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edDOB.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });

        edEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edEmail.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edEmail.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });

        edDOB_DD.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edDOB_DD.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edDOB_DD.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });

        edDOB_MM.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edDOB_MM.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edDOB_MM.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });

        edDOB_YY.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    edDOB_YY.setBackgroundResource(R.drawable.edittext_border);
                } else {
                    edDOB_YY.setBackgroundResource(R.drawable.edittext_border_layout);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvRegister_RegisterButton) {

            try {

                final String name = edName.getText().toString();
                final String mobile = edMobile.getText().toString();
                final String pass = edPassword.getText().toString();
                String confirmPass = edConfirmPass.getText().toString();
                String dob = edDOB.getText().toString();
                //final String saveDob = tvDOB.getText().toString();
                final String email = edEmail.getText().toString();
                String dd = edDOB_DD.getText().toString();
                String mm = edDOB_MM.getText().toString();
                String yy = edDOB_YY.getText().toString();

                final String saveDob = yy + "-" + mm + "-" + dd;


                Calendar todayCal = Calendar.getInstance();
                long millis = todayCal.getTimeInMillis();
                int currentYear = todayCal.get(Calendar.YEAR);


                if (name.isEmpty()) {
                    Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
                    edName.requestFocus();
                } else if (mobile.isEmpty()) {
                    Toast.makeText(this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                    edMobile.requestFocus();
                } else if (mobile.length() != 10) {
                    Toast.makeText(this, "Please Enter Valid Mobile Number", Toast.LENGTH_SHORT).show();
                    edMobile.requestFocus();
                } else if (dd.isEmpty()) {
                    Toast.makeText(this, "Please Select Day", Toast.LENGTH_SHORT).show();
                    edDOB_DD.requestFocus();
                } else if (Integer.parseInt(dd) > 31) {
                    Toast.makeText(this, "Invalid Day", Toast.LENGTH_SHORT).show();
                    edDOB_DD.requestFocus();
                } else if (mm.isEmpty()) {
                    Toast.makeText(this, "Please Select Day", Toast.LENGTH_SHORT).show();
                    edDOB_MM.requestFocus();
                } else if (Integer.parseInt(mm) <= 0 || Integer.parseInt(mm) > 12) {
                    Toast.makeText(this, "Invalid Month", Toast.LENGTH_SHORT).show();
                    edDOB_MM.requestFocus();
                } else if (yy.isEmpty()) {
                    Toast.makeText(this, "Please Select Year", Toast.LENGTH_SHORT).show();
                    edDOB_YY.requestFocus();
                } else if (Integer.parseInt(yy) <= 0 || Integer.parseInt(yy) > currentYear) {
                    Toast.makeText(this, "Invalid Year", Toast.LENGTH_SHORT).show();
                    edDOB_YY.requestFocus();
                } else if (!isDateValid(saveDob)) {
                    //Log.e("Save DOB", "-------------------" + saveDob);
                    Toast.makeText(this, "Invalid Date Of Birth", Toast.LENGTH_SHORT).show();
                    tvDOB.requestFocus();

                }
                /*else if (dob.isEmpty()) {
                    Toast.makeText(this, "Please Select Date Of Birth", Toast.LENGTH_SHORT).show();
                    edDOB.requestFocus();
                } else if (dobMillis > millis) {
                    Toast.makeText(this, "Date Of Birth Should Not Exceed From Today's Date", Toast.LENGTH_SHORT).show();
                    edDOB.requestFocus();
                }*/
                else if (pass.isEmpty()) {
                    Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                    edPassword.requestFocus();
                } else if (confirmPass.isEmpty()) {
                    Toast.makeText(this, "Please Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    edConfirmPass.requestFocus();
                } else if (!pass.equals(confirmPass)) {
                    Toast.makeText(this, "Password Not Matched!", Toast.LENGTH_SHORT).show();
                    edConfirmPass.requestFocus();
                } else {
                    randomNumber = random(6);
                    ////Log.e("OTP", "----------------------" + randomNumber);


                    Calendar cal = Calendar.getInstance();
                    //cal.setTimeInMillis(dobMillis);
                    cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dd));
                    cal.set(Calendar.MONTH, Integer.parseInt(mm));
                    cal.set(Calendar.YEAR, Integer.parseInt(yy));

                    if (!email.isEmpty()) {
                        if (isValidEmailAddress(email)) {
                            if (getAge(cal) < 18) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this, R.style.WarningAlertDialogTheme);
                                LayoutInflater inflater = this.getLayoutInflater();
                                final View dialogView = inflater.inflate(R.layout.custom_warning_dialog, null);
                                builder.setView(dialogView);

                                builder.setCancelable(false);
                                builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
//                                        UserBean bean = new UserBean(name, mobile, pass, 0, 0, "token", email, saveDob);
//                                        RegisterNewUser(bean);
                                        send(mobile, "Thanks for Register at SHAIL app Your OTP Number is " + randomNumber + " Please do not share this OTP.");
                                        //Log.e("Random Number OTP : ", "--------------" + random(6));

                                    }
                                });
                                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.show();
                            } else {
                                send(mobile, "Thanks for Register at SHAIL app Your OTP Number is " + randomNumber + " Please do not share this OTP.");
                                //Log.e("Random Number OTP : ", "--------------" + random(6));

//                                UserBean bean = new UserBean(name, mobile, pass, 0, 0, "token", email, saveDob);
//                                RegisterNewUser(bean);
                            }

                        } else {
                            Toast.makeText(this, "Invalid Email Id", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (getAge(cal) < 18) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this, R.style.WarningAlertDialogTheme);
                            LayoutInflater inflater = this.getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.custom_warning_dialog, null);
                            builder.setView(dialogView);

                            builder.setCancelable(false);
                            builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    send(mobile, "Thanks for Register at SHAIL app Your OTP Number is " + randomNumber + " Please do not share this OTP.");
                                    //Log.e("Random Number OTP : ", "--------------" + random(6));

//                                    UserBean bean = new UserBean(name, mobile, pass, 0, 0, "token", email, saveDob);
//                                    RegisterNewUser(bean);
                                }
                            });
                            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.show();
                        } else {

                            send(mobile, "Thanks for Register at SHAIL app Your OTP Number is " + randomNumber + " Please do not share this OTP.");
                            //Log.e("Random Number OTP : ", "--------------" + random(6));

//                            UserBean bean = new UserBean(name, mobile, pass, 0, 0, "token", email, saveDob);
//                            RegisterNewUser(bean);
                        }

                    }
//                    send(mobile, "Thanks for Register at SHAIL app Your OTP Number is " + randomNumber + " Please do not share this OTP.");
//                    //Log.e("Random Number OTP : ", "--------------" + random(6));
                    // startActivity(new Intent(RegistrationActivity.this, SignInOptionActivity.class));
                    //finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (view.getId() == R.id.tvRegister_VerifyOTP) {
            String name = edName.getText().toString();
            String mobile = edMobile.getText().toString();
            String pass = edPassword.getText().toString();
            String otp = edOtp.getText().toString();
            final String email = edEmail.getText().toString();

            String dd = edDOB_DD.getText().toString();
            String mm = edDOB_MM.getText().toString();
            String yy = edDOB_YY.getText().toString();

            final String saveDob = yy + "-" + mm + "-" + dd;

            if (randomNumber.equals(otp)) {
                UserBean bean = new UserBean(name, mobile, pass, 0, 0, "token", email, saveDob);
                RegisterNewUser(bean);
            } else {
                Toast.makeText(this, "Please Enter Correct OTP Number", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.edRegister_DOB) {

            int yr, mn, dy;
            if (dobMillis > 0) {
                Calendar purchaseCal = Calendar.getInstance();
                purchaseCal.setTimeInMillis(dobMillis);
                yr = purchaseCal.get(Calendar.YEAR);
                mn = purchaseCal.get(Calendar.MONTH);
                dy = purchaseCal.get(Calendar.DAY_OF_MONTH);
            } else {
                Calendar purchaseCal = Calendar.getInstance();
                yr = 1975;
                mn = 0;
                dy = 1;
            }

            DatePickerDialog.OnDateSetListener toDtListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    yyyy = year;
                    mm = month + 1;
                    dd = dayOfMonth;
                    edDOB.setText(dd + "-" + mm + "-" + yyyy);
                    tvDOB.setText(yyyy + "-" + mm + "-" + dd);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(yyyy, mm - 1, dd);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.HOUR, 0);
                    dobMillis = calendar.getTimeInMillis();
                }
            };

            DatePickerDialog dialog = new DatePickerDialog(this, R.style.DialogTheme, toDtListener, yr, mn, dy);
            dialog.show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
        if (CheckNetwork.isInternetAvailable(RegistrationActivity.this)) {
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

            //    Call<JsonObject> call = myInterface.sendSMS("140742AbB1cy8zZt589c06d5", mobile, message, "RCONNT", "4", "91", "json");

            Call<JsonObject> call = myInterface.sendSMSAPI("lavazza", "lavazza", mobile, "BARSHL", message, 0, 2);

            final Dialog progressDialog = new Dialog(RegistrationActivity.this);
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
                    llForm.setVisibility(View.GONE);
                    llOTP.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    //Log.e("call failed ", " .. " + t.getMessage());
                    Toast.makeText(RegistrationActivity.this, "Oops Something Went Wrong!\nPlease Try Again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }


    public void RegisterNewUser(UserBean userBean) {
        if (CheckNetwork.isInternetAvailable(RegistrationActivity.this)) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(InterfaceApi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            InterfaceApi api = retrofit.create(InterfaceApi.class);

            Call<ErrorMessage> errorMessageCall = api.registerUser(userBean);

            final Dialog progressDialog = new Dialog(RegistrationActivity.this);
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
                                    Toast.makeText(RegistrationActivity.this, "Sorry, Unable to Register", Toast.LENGTH_SHORT).show();
                                } else if (data.getMessage().equalsIgnoreCase("Mobile Already Registered")) {
                                    Toast.makeText(RegistrationActivity.this, "Mobile Already Registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RegistrationActivity.this, "Sorry, Unable to Register", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(RegistrationActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                finish();

                            }

                        } else {
                            progressDialog.dismiss();
                            //Log.e("ON RESPONSE : ", "NO DATA");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        //Log.e("Exception : ", "" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    progressDialog.dismiss();
                    //Log.e("ON FAILURE : ", "ERROR : " + t.getMessage());
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this, R.style.AlertDialogTheme);
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

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static int getAge(Calendar dob) throws Exception {
        Calendar today = Calendar.getInstance();
        int curYear = today.get(Calendar.YEAR);
        int dobYear = dob.get(Calendar.YEAR);
        int age = curYear - dobYear;

        // if dob is month or day is behind today's month or day
        // reduce age by 1
        int curMonth = today.get(Calendar.MONTH);
        int dobMonth = dob.get(Calendar.MONTH);
        if (dobMonth > curMonth) { // this year can't be counted!
            age--;
        } else if (dobMonth == curMonth) { // same month? check for day
            int curDay = today.get(Calendar.DAY_OF_MONTH);
            int dobDay = dob.get(Calendar.DAY_OF_MONTH);
            if (dobDay > curDay) { // this year can't be counted!
                age--;
            }
        }

        return age;
    }


    private static boolean isDateValid(String date) {

        //Log.e("DOB","---------------------"+date);

        boolean result = false;

        if (isValidDateFormat(date)) {

            //Log.e("DOB","---------------------Valid Format");
            /*
             * d -> Day of month
			 * M -> Month of year
			 * y -> Year
			 */
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            /*
			 * By default setLenient() is true. We should make it false for
			 * strict date validations.
			 *
			 * If setLenient() is true - It accepts all dates. If setLenient()
			 * is false - It accepts only valid dates.
			 */
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(date);
                result = true;
            } catch (ParseException e) {
                result = false;
            } catch (Exception e) {
                result = false;
            }
        }
        return result;
    }

    private static boolean isValidDateFormat(String date) {



		/*
		 * Regular Expression that matches String with format dd/MM/yyyy.
		 * dd -> 01-31
		 * MM -> 01-12
		 * yyyy -> 4 digit number
		 */
        String pattern = "([0-9]{4}\\-(0?[1-9]|1[0-2])\\-(0?[1-9]|[12][0-9]|3[01]))";
        boolean result = false;
        if (date.matches(pattern)) {
            result = true;
        }
        return result;

    }
}
