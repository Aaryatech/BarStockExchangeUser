package com.ats.barstockexchangeuser.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.barstockexchangeuser.R;
import com.ats.barstockexchangeuser.bean.CallWaiter;
import com.ats.barstockexchangeuser.bean.ErrorMessage;
import com.ats.barstockexchangeuser.bean.Settings;
import com.ats.barstockexchangeuser.bean.Table;
import com.ats.barstockexchangeuser.bean.TableData;
import com.ats.barstockexchangeuser.fragment.AboutDevFragment;
import com.ats.barstockexchangeuser.fragment.AboutUsFragment;
import com.ats.barstockexchangeuser.fragment.MyOrdersFragment;
import com.ats.barstockexchangeuser.fragment.NewsFragment;
import com.ats.barstockexchangeuser.fragment.TermsFragment;
import com.ats.barstockexchangeuser.fragment.UserHomeFragment;
import com.ats.barstockexchangeuser.util.CheckNetwork;
import com.ats.barstockexchangeuser.util.GPSTracker;
import com.ats.barstockexchangeuser.util.InterfaceApi;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener {

    public static TextView tvTitle;
    private TextView tvUserMenu_Home, tvUserMenu_News, tvUserMenu_RateApp, tvUserMenu_AboutUs, tvUserMenu_AboutDev, tvUserMenu_Terms, tvUserSignOut, tvUserMenuCallWaiter, tvUserMenuFacebook, tvUserMenuInstagram;
    int userId;
    String userName;

    double centerLatitude, centerlongitude;
    int radius;

    private ArrayList<Table> tableArrayList = new ArrayList<>();
    private ArrayAdapter<Integer> arrayIntAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTitle = findViewById(R.id.tvTitle);

        tvUserMenu_Home = findViewById(R.id.tvUserMenuHome);
        tvUserMenu_News = findViewById(R.id.tvUserMenuNews);
        tvUserMenu_RateApp = findViewById(R.id.tvUserMenuRateApp);
        tvUserMenu_AboutUs = findViewById(R.id.tvUserMenuAboutUs);
        tvUserMenu_AboutDev = findViewById(R.id.tvUserMenuAboutDev);
        tvUserMenu_Terms = findViewById(R.id.tvUserMenuTerms);
        tvUserSignOut = findViewById(R.id.tvUserSignOut);
        tvUserMenuCallWaiter = findViewById(R.id.tvUserMenuCallWaiter);
        tvUserMenuFacebook = findViewById(R.id.tvUserMenuFacebook);
        tvUserMenuInstagram = findViewById(R.id.tvUserMenuInstagram);

        tvUserMenu_Home.setOnClickListener(this);
        tvUserMenu_News.setOnClickListener(this);
        tvUserMenu_RateApp.setOnClickListener(this);
        tvUserMenu_AboutUs.setOnClickListener(this);
        tvUserMenu_AboutDev.setOnClickListener(this);
        tvUserMenu_Terms.setOnClickListener(this);
        tvUserSignOut.setOnClickListener(this);
        tvUserMenuCallWaiter.setOnClickListener(this);
        tvUserMenuFacebook.setOnClickListener(this);
        tvUserMenuInstagram.setOnClickListener(this);

        tvUserMenu_AboutDev.setVisibility(View.GONE);

        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(InterfaceApi.MY_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            userId = pref.getInt("userId", 0);
            userName = pref.getString("userName", "");
        } catch (Exception e) {
        }

        //Log.e("User Id : -----------", "" + userId);

        if (userId <= 0) {
            startActivity(new Intent(HomeActivity.this, SignInOptionActivity.class));
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.mipmap.menu_icon);
        toggle.syncState();

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        TextView tvHead = navigationView.findViewById(R.id.tvNavHeader);
        tvHead.setText("Welcome " + userName);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new UserHomeFragment(), "Home");
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment home = getSupportFragmentManager().findFragmentByTag("Home");
        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag("HomeFragment");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (home instanceof UserHomeFragment && home.isVisible()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
            builder.setTitle("Confirm Action");
            builder.setMessage("Do You Really Want To Exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (homeFragment instanceof MyOrdersFragment && homeFragment.isVisible() ||
                homeFragment instanceof NewsFragment && homeFragment.isVisible() ||
                homeFragment instanceof AboutUsFragment && homeFragment.isVisible() ||
                homeFragment instanceof AboutDevFragment && homeFragment.isVisible() ||
                homeFragment instanceof TermsFragment && homeFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new UserHomeFragment(), "HomeFragment");
            ft.commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvUserMenuHome) {
            Fragment fragment = new UserHomeFragment();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment, "Home");
                ft.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (view.getId() == R.id.tvUserMenuCallWaiter) {

            try {
                SharedPreferences pref = getSharedPreferences(InterfaceApi.MY_PREF, MODE_PRIVATE);
                Gson gson = new Gson();
                String json2 = pref.getString("Settings", "");
                Settings settings = gson.fromJson(json2, Settings.class);
                //Log.e("Settings Bean : ", "---------------" + settings);
                if (settings != null) {
                    centerLatitude = settings.getLatitude();
                    centerlongitude = settings.getLongitude();
                    radius = settings.getRadius();
                }
            } catch (Exception e) {
            }

            getTableList();


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (view.getId() == R.id.tvUserMenuFacebook) {
            Uri uri = Uri.parse("https://www.facebook.com/ShailSportsBar/?ref=bookmarks");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.facebook.katana");

            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/ShailSportsBar/?ref=bookmarks")));
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (view.getId() == R.id.tvUserMenuInstagram) {
            Uri uri = Uri.parse("https://www.instagram.com/shail_sports_bar");
            Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

            likeIng.setPackage("com.instagram.android");

            try {
                startActivity(likeIng);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.instagram.com/shail_sports_bar")));
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (view.getId() == R.id.tvUserMenuNews) {
            Fragment fragment = new NewsFragment();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment, "HomeFragment");
                ft.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (view.getId() == R.id.tvUserMenuAboutUs) {
            Fragment fragment = new AboutUsFragment();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment, "HomeFragment");
                ft.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (view.getId() == R.id.tvUserMenuAboutDev) {
            Fragment fragment = new AboutDevFragment();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment, "HomeFragment");
                ft.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (view.getId() == R.id.tvUserMenuTerms) {
            Fragment fragment = new TermsFragment();
            if (fragment != null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment, "HomeFragment");
                ft.commit();
            }
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else if (view.getId() == R.id.tvUserMenuRateApp) {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }

        } else if (view.getId() == R.id.tvUserSignOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
            builder.setTitle("Sign Out");
            builder.setMessage("Are You Sure You Want To Sign Out?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(InterfaceApi.MY_PREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(HomeActivity.this, SignInOptionActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    public void getTableList() {
        if (CheckNetwork.isInternetAvailable(HomeActivity.this)) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(InterfaceApi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            InterfaceApi api = retrofit.create(InterfaceApi.class);

            Call<TableData> tableDataCall = api.getAllTables();

            final Dialog progressDialog = new Dialog(HomeActivity.this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.loading_progress_layout);
            progressDialog.show();

            tableDataCall.enqueue(new Callback<TableData>() {
                @Override
                public void onResponse(Call<TableData> call, retrofit2.Response<TableData> response) {
                    try {
                        if (response.body() != null) {
                            TableData data = response.body();
                            if (data.getErrorMessage().getError()) {
                                progressDialog.dismiss();
                                //Log.e("ON RESPONSE : ", "ERROR : " + data.getErrorMessage().getMessage());
                                Toast.makeText(HomeActivity.this, "Sorry, Unable To Process", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();

                                tableArrayList.clear();
                                for (int i = 0; i < data.getTable().size(); i++) {
                                    tableArrayList.add(data.getTable().get(i));
                                }

                                final Dialog dialog = new Dialog(HomeActivity.this);
                                //dialog.setTitle("Call Waiter");
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.custom_quantity_dialog);

                                final ListView lvQty = (ListView) dialog.findViewById(R.id.lvQtyDialog);
                                TextView tvTitle = dialog.findViewById(R.id.tvQtyTitle);
                                tvTitle.setText("Select Table");

                                final ArrayList<Integer> intArray = new ArrayList<>();
                                final ArrayList<String> tblArray = new ArrayList<>();
                                for (int i = 0; i < tableArrayList.size(); i++) {
                                    intArray.add(tableArrayList.get(i).getTableNo());
                                    tblArray.add(tableArrayList.get(i).getTableName());
                                }


                                arrayIntAdapter = new ArrayAdapter<Integer>(HomeActivity.this, android.R.layout.simple_expandable_list_item_1, intArray) {
                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent) {
                                        LayoutInflater inflater1 = HomeActivity.this.getLayoutInflater();
                                        View view = inflater1.inflate(R.layout.custom_quantity_item_layout, parent, false);
                                        TextView tvQty = view.findViewById(R.id.tvQtyItem);
                                        tvQty.setText("" + tblArray.get(position));
                                        return view;
                                    }
                                };
                                lvQty.setAdapter(arrayIntAdapter);

                                lvQty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                                        //  Toast.makeText(AddMixerActivity.this, "" + (i + 1), Toast.LENGTH_SHORT).show();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
                                        builder.setTitle("Call Waiter");
                                        builder.setMessage("Do You Want To Call Waiter on Table No - " + intArray.get(i) + " ?");
                                        builder.setPositiveButton("CALL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which) {

                                                GPSTracker gps = new GPSTracker(HomeActivity.this);
                                                if (gps.canGetLocation()) {

                                                    double latitude = gps.getLatitude();
                                                    double longitude = gps.getLongitude();


                                                    float[] results = new float[1];
                                                    Location.distanceBetween(centerLatitude, centerlongitude, latitude, longitude, results);
                                                    float distanceInMeters = results[0];
                                                    //Log.e("Center Lat : ", "--- " + centerLatitude);
                                                    //Log.e("Center Long : ", "--- " + centerlongitude);
                                                    //Log.e("Lat : ", "--- " + latitude);
                                                    //Log.e("Long : ", "--- " + longitude);
                                                    //Log.e("Radius : ", "--- " + radius);
                                                    //Log.e("Distance : ", "--- " + distanceInMeters);

                                                    if (distanceInMeters <= radius) {
                                                        // placeOrder(entry);
                                                        int user = userId;
                                                        int table = intArray.get(i);
                                                        CallWaiter waiter = new CallWaiter(table,user, table, "", 0);
                                                        //Log.e("bean:","-----------------------------------"+waiter);
                                                        callWaiterService(waiter);

                                                    } else {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
                                                        builder.setCancelable(false);
                                                        builder.setMessage("You Need To Be At Our Bar Location To Call Waiter");
                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                        //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                                                    }

                                                } else {
                                                    gps.showSettingsAlert();
                                                }


                                                dialogInterface.dismiss();
                                            }
                                        });
                                        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int which) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();


                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();

                                //Toast.makeText(OrderReviewActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            progressDialog.dismiss();
                            //Log.e("ON RESPONSE : ", "NO DATA");
                            Toast.makeText(HomeActivity.this, "Sorry, Unable To Process", Toast.LENGTH_SHORT).show(); // Toast.makeText(OrderReviewActivity.this, "Sorry, Unable to Place Order", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        //Log.e("Exception : ", "" + e.getMessage());
                        Toast.makeText(HomeActivity.this, "Sorry, Unable To Process", Toast.LENGTH_SHORT).show(); //Toast.makeText(OrderReviewActivity.this, "Sorry, Unable to Place Order", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TableData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(HomeActivity.this, "Sorry, Unable To Process", Toast.LENGTH_SHORT).show();
                    //Log.e("ON FAILURE : ", "ERROR : " + t.getMessage());
                    //Toast.makeText(OrderReviewActivity.this, "Sorry, Unable to Place Order", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
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


    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/<id_here>"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/<user_name_here>"));
        }
    }


    public void callWaiterService(CallWaiter callWaiter) {
        if (CheckNetwork.isInternetAvailable(HomeActivity.this)) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(InterfaceApi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            InterfaceApi api = retrofit.create(InterfaceApi.class);

            Call<ErrorMessage> errorMessageCall = api.callWaiter(callWaiter);

            final Dialog progressDialog = new Dialog(HomeActivity.this);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.loading_progress_layout);
            progressDialog.show();

            errorMessageCall.enqueue(new Callback<ErrorMessage>() {
                @Override
                public void onResponse(Call<ErrorMessage> call, Response<ErrorMessage> response) {
                    try {
                        if (response.body() != null) {
                            if (!response.body().getError()) {
                                Toast.makeText(HomeActivity.this, "Please Wait! Waiter Is Comming", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(HomeActivity.this, "Sorry, Please Try Again!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "Sorry, Please Try Again!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        Toast.makeText(HomeActivity.this, "Sorry, Please Try Again!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Sorry, Please Try Again!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });


        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

}
