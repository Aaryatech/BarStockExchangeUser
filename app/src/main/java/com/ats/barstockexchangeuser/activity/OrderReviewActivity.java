package com.ats.barstockexchangeuser.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.barstockexchangeuser.R;
import com.ats.barstockexchangeuser.bean.ErrorMessage;
import com.ats.barstockexchangeuser.bean.OrderDetailsList;
import com.ats.barstockexchangeuser.bean.OrderEntry;
import com.ats.barstockexchangeuser.bean.Settings;
import com.ats.barstockexchangeuser.bean.Table;
import com.ats.barstockexchangeuser.bean.TableData;
import com.ats.barstockexchangeuser.bean.TempDataBean;
import com.ats.barstockexchangeuser.util.CheckNetwork;
import com.ats.barstockexchangeuser.util.GPSTracker;
import com.ats.barstockexchangeuser.util.InterfaceApi;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTable, btnConfirm;
    private ArrayAdapter<Integer> arrayIntAdapter;
    private ListView lvOrders;
    private TextView tvTotalPrice, tvTableNo;

    double centerLatitude, centerlongitude;

    int userId, radius;

    ArrayList<TempDataBean> tempList = new ArrayList<>();
    private ArrayList<Table> tableArrayList = new ArrayList<>();

    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnTable = findViewById(R.id.btnOrderReview_table);
        btnConfirm = findViewById(R.id.btnOrderReview_confirm);
        lvOrders = findViewById(R.id.lvOrderReview);
        tvTotalPrice = findViewById(R.id.tvOrderReviewActivity_TotalPrice);
        tvTableNo = findViewById(R.id.tvOrderReview_TableNo);

        btnTable.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(InterfaceApi.MY_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            userId = pref.getInt("userId", 0);
        } catch (Exception e) {
        }

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


        try {
            Bundle bundle = getIntent().getExtras();
            String tempString = bundle.getString("TempDataArray");

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<TempDataBean>>() {
            }.getType();
            tempList = gson.fromJson(tempString, type);

            OrderReviewAdapter orderAdapter = new OrderReviewAdapter(OrderReviewActivity.this, tempList);
            lvOrders.setAdapter(orderAdapter);

            float total = 0;
            for (TempDataBean data : tempList) {
                //Log.e("Temp Data", "-----------" + data);

//                float totalTax = data.getSgst() + data.getCgst();
//                float percent = (data.getPrice() * totalTax) / 100;
//                float result = data.getPrice() + percent;

                float result = data.getPrice() * data.getQty();

                total = total + result;
            }

            tvTotalPrice.setText("\u20B9 " + total);


        } catch (Exception e) {
        }

        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                // execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        getTableList();

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnOrderReview_table) {

            final Dialog dialog = new Dialog(OrderReviewActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_quantity_dialog);

            final ListView lvQty = (ListView) dialog.findViewById(R.id.lvQtyDialog);
            TextView tvTitle = dialog.findViewById(R.id.tvQtyTitle);
            tvTitle.setText("Select Table");

            final ArrayList<Integer> intArray = new ArrayList<>();
            final ArrayList<String> tblArray = new ArrayList<>();
//            for (int i = 1; i <= 100; i++) {
//                intArray.add(i);
//            }

            for (int i = 0; i < tableArrayList.size(); i++) {
                intArray.add(tableArrayList.get(i).getTableNo());
                tblArray.add(tableArrayList.get(i).getTableName());
            }


           /* arrayIntAdapter = new ArrayAdapter<Integer>(OrderReviewActivity.this, android.R.layout.simple_expandable_list_item_1, intArray) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    LayoutInflater inflater1 = OrderReviewActivity.this.getLayoutInflater();
                    View view = inflater1.inflate(R.layout.custom_quantity_item_layout, parent, false);
                    TextView tvQty = view.findViewById(R.id.tvQtyItem);
                    tvQty.setText("" + intArray.get(position));
                    return view;
                }
            };*/

            ArrayAdapter<String> arrayStrAdapter = new ArrayAdapter<String>(OrderReviewActivity.this, android.R.layout.simple_expandable_list_item_1, tblArray) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    LayoutInflater inflater1 = OrderReviewActivity.this.getLayoutInflater();
                    View view = inflater1.inflate(R.layout.custom_quantity_item_layout, parent, false);
                    TextView tvQty = view.findViewById(R.id.tvQtyItem);
                    tvQty.setText("" + tblArray.get(position));
                    return view;
                }
            };

            lvQty.setAdapter(arrayStrAdapter);

            lvQty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //  Toast.makeText(AddMixerActivity.this, "" + (i + 1), Toast.LENGTH_SHORT).show();
                    btnTable.setText("Table : " + tblArray.get(i));
                    tvTableNo.setText("" + intArray.get(i));
                    dialog.dismiss();
                }
            });

            dialog.show();

        } else if (view.getId() == R.id.btnOrderReview_confirm) {

            try {
                if (tempList.size() > 0) {

                    ArrayList<OrderDetailsList> detailsListsArray = new ArrayList<>();
                    for (int i = 0; i < tempList.size(); i++) {
                        OrderDetailsList orderDetailsList = new OrderDetailsList(0, 0, tempList.get(i).getId(), tempList.get(i).getQty(), tempList.get(i).getPrice(), 0, 0);
                        detailsListsArray.add(orderDetailsList);
                    }


                    if (tvTableNo.getText().toString().isEmpty()) {
                        Toast.makeText(this, "Please Select Table", Toast.LENGTH_SHORT).show();
                        btnTable.requestFocus();
                    } else {
                        int tableNo = Integer.parseInt(tvTableNo.getText().toString());
                        OrderEntry entry = new OrderEntry(0, userId, tableNo, 1, "0", 0, detailsListsArray);
                        ////Log.e("Order Entry : ", "--------------------------------------\n" + entry);


                        GPSTracker gps = new GPSTracker(OrderReviewActivity.this);
                        if (gps.canGetLocation()) {

                            double latitude = gps.getLatitude();
                            double longitude = gps.getLongitude();

//                            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                            //                                   + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

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
                                placeOrder(entry);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderReviewActivity.this, R.style.AlertDialogTheme);
                                builder.setCancelable(false);
                                builder.setMessage("You Need To Be At Our Bar Location To Be Able To Place An Order");
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


                    }

                } else {
                    Toast.makeText(this, "Please Select Dirnks First!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }


        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    public class OrderReviewAdapter extends BaseAdapter {

        private ArrayList<TempDataBean> originalValues;
        private ArrayList<TempDataBean> displayedValues;
        LayoutInflater inflater;

        public OrderReviewAdapter(Context context, ArrayList<TempDataBean> stringArrayList) {
            this.originalValues = stringArrayList;
            this.displayedValues = stringArrayList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return displayedValues.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View v, ViewGroup parent) {
            v = inflater.inflate(R.layout.custom_order_review_layout, null);
            TextView tvName = v.findViewById(R.id.tvOrderReview_ItemName);
            TextView tvQty = v.findViewById(R.id.tvOrderReview_ItemQty);
            TextView tvPrice = v.findViewById(R.id.tvOrderReview_ItemPrice);
            ImageView ivCancel = v.findViewById(R.id.ivCancelItem);

            tvName.setText("" + displayedValues.get(position).getName());
            tvQty.setText("" + displayedValues.get(position).getQty());
            tvPrice.setText("\u20B9 " + displayedValues.get(position).getPrice());

            LinearLayout linearLayout1 = (LinearLayout) v.findViewById(R.id.llOrderReview_ImageView);
            for (int x = 0; x < displayedValues.get(position).getQty(); x++) {
                ImageView image = new ImageView(OrderReviewActivity.this);
                image.setBackgroundResource(R.mipmap.glass_icon);
                linearLayout1.addView(image);
            }

            ivCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < tempList.size(); i++) {
                        if (displayedValues.get(position).getId() == tempList.get(i).getId()) {
                            tempList.remove(tempList.get(i));
                            notifyDataSetChanged();
                        }
                    }

                    float total = 0;
                    for (int i = 0; i < tempList.size(); i++) {
                        float result = tempList.get(i).getPrice() * tempList.get(i).getQty();
                        total = total + result;
                    }

                    tvTotalPrice.setText("\u20B9 " + total);

                }
            });


            return v;
        }
    }


    public void placeOrder(OrderEntry orderEntry) {
        if (CheckNetwork.isInternetAvailable(OrderReviewActivity.this)) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(InterfaceApi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            InterfaceApi api = retrofit.create(InterfaceApi.class);

            Call<ErrorMessage> errorMessageCall = api.placeUserOrder(orderEntry);

            final Dialog progressDialog = new Dialog(OrderReviewActivity.this);
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
                                Toast.makeText(OrderReviewActivity.this, "Sorry, Unable To Place Order", Toast.LENGTH_SHORT).show();
                                if (data.getMessage().equalsIgnoreCase("stock")) {
                                    Toast.makeText(OrderReviewActivity.this, "Stock Is Not Available", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(OrderReviewActivity.this, "Sorry, Unable To Place Order", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(OrderReviewActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(OrderReviewActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            progressDialog.dismiss();
                            //Log.e("ON RESPONSE : ", "NO DATA");
                            Toast.makeText(OrderReviewActivity.this, "Sorry, Unable to Place Order", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        //Log.e("Exception : ", "" + e.getMessage());
                        Toast.makeText(OrderReviewActivity.this, "Sorry, Unable to Place Order", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ErrorMessage> call, Throwable t) {
                    progressDialog.dismiss();
                    //Log.e("ON FAILURE : ", "ERROR : " + t.getMessage());
                    Toast.makeText(OrderReviewActivity.this, "Sorry, Unable to Place Order", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderReviewActivity.this, R.style.AlertDialogTheme);
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


    public void getTableList() {
        if (CheckNetwork.isInternetAvailable(OrderReviewActivity.this)) {

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

            final Dialog progressDialog = new Dialog(OrderReviewActivity.this);
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
                                //Toast.makeText(OrderReviewActivity.this, "Sorry, Unable To Place Order", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();

                                tableArrayList.clear();
                                for (int i = 0; i < data.getTable().size(); i++) {
                                    tableArrayList.add(data.getTable().get(i));
                                }


                                //Toast.makeText(OrderReviewActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            progressDialog.dismiss();
                            //Log.e("ON RESPONSE : ", "NO DATA");
                            // Toast.makeText(OrderReviewActivity.this, "Sorry, Unable to Place Order", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        //Log.e("Exception : ", "" + e.getMessage());
                        //Toast.makeText(OrderReviewActivity.this, "Sorry, Unable to Place Order", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TableData> call, Throwable t) {
                    progressDialog.dismiss();
                    //Log.e("ON FAILURE : ", "ERROR : " + t.getMessage());
                    //Toast.makeText(OrderReviewActivity.this, "Sorry, Unable to Place Order", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderReviewActivity.this, R.style.AlertDialogTheme);
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


}
