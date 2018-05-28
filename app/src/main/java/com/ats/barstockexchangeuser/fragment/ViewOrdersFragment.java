package com.ats.barstockexchangeuser.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.barstockexchangeuser.R;
import com.ats.barstockexchangeuser.activity.HomeActivity;
import com.ats.barstockexchangeuser.bean.CallWaiter;
import com.ats.barstockexchangeuser.bean.OrderDisplay;
import com.ats.barstockexchangeuser.bean.OrderItem;
import com.ats.barstockexchangeuser.bean.Table;
import com.ats.barstockexchangeuser.bean.TableData;
import com.ats.barstockexchangeuser.bean.UserOrderHistory;
import com.ats.barstockexchangeuser.util.CheckNetwork;
import com.ats.barstockexchangeuser.util.GPSTracker;
import com.ats.barstockexchangeuser.util.InterfaceApi;
import com.ats.barstockexchangeuser.util.ViewOrderInterface;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ViewOrdersFragment extends Fragment implements ViewOrderInterface {

    private ArrayList<OrderDisplay> displayArrayList = new ArrayList<>();
    private ListView lvOrders;
    ViewOrderAdapter orderAdapter;
    int userId;

    private ArrayList<Table> tableArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_orders, container, false);

        lvOrders = view.findViewById(R.id.lvViewOrder);

        try {
            SharedPreferences pref = getContext().getSharedPreferences(InterfaceApi.MY_PREF, MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            userId = pref.getInt("userId", 0);
        } catch (Exception e) {
        }
//        getUserOrders(userId);
        getTableList();

        return view;
    }

    public void getTableList() {
        if (CheckNetwork.isInternetAvailable(getContext())) {

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

            final Dialog progressDialog = new Dialog(getContext());
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
                                Toast.makeText(getContext(), "Sorry, Unable To Process", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();

                                tableArrayList.clear();
                                for (int i = 0; i < data.getTable().size(); i++) {
                                    tableArrayList.add(data.getTable().get(i));
                                }
                                getUserOrders(userId);

                            }

                        } else {
                            progressDialog.dismiss();
                            //Log.e("ON RESPONSE : ", "NO DATA");
                            Toast.makeText(getContext(), "Sorry, Unable To Process", Toast.LENGTH_SHORT).show(); // Toast.makeText(OrderReviewActivity.this, "Sorry, Unable to Place Order", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        //Log.e("Exception : ", "" + e.getMessage());
                        Toast.makeText(getContext(), "Sorry, Unable To Process", Toast.LENGTH_SHORT).show(); //Toast.makeText(OrderReviewActivity.this, "Sorry, Unable to Place Order", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<TableData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Sorry, Unable To Process", Toast.LENGTH_SHORT).show();
                    //Log.e("ON FAILURE : ", "ERROR : " + t.getMessage());
                    //Toast.makeText(OrderReviewActivity.this, "Sorry, Unable to Place Order", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
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

    public void getUserOrders(int userId) {
        if (CheckNetwork.isInternetAvailable(getContext())) {

            //Log.e("Parameter-----", "" + userId);

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(InterfaceApi.URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

            InterfaceApi api = retrofit.create(InterfaceApi.class);
            Call<UserOrderHistory> orderHistoryCall = api.orderHistory(userId);


            final Dialog progressDialog = new Dialog(getContext());
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.loading_progress_layout);
            progressDialog.show();


            orderHistoryCall.enqueue(new Callback<UserOrderHistory>() {
                @Override
                public void onResponse(Call<UserOrderHistory> call, Response<UserOrderHistory> response) {
                    try {
                        if (response.body() != null) {
                            UserOrderHistory data = response.body();
                            if (data.getErrorMessage().getError()) {
                                progressDialog.dismiss();
                                //Log.e("ON RESPONSE : ", " ERROR : " + data.getErrorMessage().getMessage());
                            } else {

                                displayArrayList.clear();

                                //Log.e("Order : ", "-----------" + response.body());

                                for (int i = 0; i < data.getOrdersByUser().get(0).getOrderDisplay().size(); i++) {
                                    displayArrayList.add(data.getOrdersByUser().get(0).getOrderDisplay().get(i));
                                }
                                //Log.e("Data : ", "" + displayArrayList);

                                orderAdapter = new ViewOrderAdapter(getContext(), displayArrayList);
                                lvOrders.setAdapter(orderAdapter);

                                progressDialog.dismiss();

                            }
                        } else {
                            progressDialog.dismiss();
                            //Log.e("RESPONSE : ", " NO DATA");
                        }
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        //Log.e("Exception : ", "" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UserOrderHistory> call, Throwable t) {
                    progressDialog.dismiss();
                    //Log.e("ON FAILURE : ", " ERROR : " + t.getMessage());
                }
            });


        } else {
            //Log.e("No Connection", "-----");
        }
    }

    @Override
    public void fragmentBecameVisible() {
        getUserOrders(userId);
    }


    public class ViewOrderAdapter extends BaseAdapter {

        private ArrayList<OrderDisplay> originalValues;
        private ArrayList<OrderDisplay> displayedValues;
        LayoutInflater inflater;

        public ViewOrderAdapter(Context context, ArrayList<OrderDisplay> stringArrayList) {
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
            v = inflater.inflate(R.layout.custom_view_order_layout, null);
            TextView tvTableNo = v.findViewById(R.id.tvCustomViewOrder_TableNo);
            TextView tvBillStatus = v.findViewById(R.id.tvCustomViewOrder_BillStatus);
            TextView tvOrderDate = v.findViewById(R.id.tvCustomViewOrder_OrderDate);
            ListView lvItems = v.findViewById(R.id.lvCustomViewOrder);

            if (tableArrayList.size() > 0) {
                for (int i = 0; i < tableArrayList.size(); i++) {
                    if (displayedValues.get(position).getOrder().getTableNo() == tableArrayList.get(i).getTableNo()) {
                        tvTableNo.setText("Table No : " + tableArrayList.get(i).getTableName());
                    }
                }
            }

            if (displayedValues.get(position).getOrder().getBillStatus() == 1) {
                tvBillStatus.setText("Pending");
            } else if (displayedValues.get(position).getOrder().getBillStatus() == 2) {
                tvBillStatus.setText("Approved By Captain");
            } else if (displayedValues.get(position).getOrder().getBillStatus() == 3) {
                tvBillStatus.setText("Order Rejected");
            } else if (displayedValues.get(position).getOrder().getBillStatus() == 4) {
                tvBillStatus.setText("Order Served");
            } else if (displayedValues.get(position).getOrder().getBillStatus() == 5) {
                tvBillStatus.setText("Order Rejected");
            }

            tvOrderDate.setText("Order Date : " + displayedValues.get(position).getOrder().getOrderDate());

            ArrayList<OrderItem> itemArray = new ArrayList<>();
            for (int i = 0; i < displayedValues.get(position).getOrderItems().size(); i++) {
                itemArray.add(displayedValues.get(position).getOrderItems().get(i));
            }

            ViewOrderItemsAdapter adapter = new ViewOrderItemsAdapter(getContext(), itemArray);
            lvItems.setAdapter(adapter);

            setListViewHeightBasedOnChildren(lvItems);


            return v;
        }
    }


    public class ViewOrderItemsAdapter extends BaseAdapter {

        private ArrayList<OrderItem> originalValues;
        private ArrayList<OrderItem> displayedValues;
        LayoutInflater inflater;

        public ViewOrderItemsAdapter(Context context, ArrayList<OrderItem> stringArrayList) {
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
            v = inflater.inflate(R.layout.custom_view_order_items_layout, null);
            TextView tvItem = v.findViewById(R.id.tvCustomViewOrderItem_Item);
            TextView tvQty = v.findViewById(R.id.tvCustomViewOrderItem_Qty);
            TextView tvRate = v.findViewById(R.id.tvCustomViewOrderItem_Rate);

            tvItem.setText("" + displayedValues.get(position).getItemName());
            tvQty.setText("" + displayedValues.get(position).getQuantity());
            tvRate.setText("" + displayedValues.get(position).getRate());

            return v;
        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static void getTotalHeightofRecyclerView(RecyclerView recyclerView) {

        RecyclerView.Adapter mAdapter = recyclerView.getAdapter();

        int totalHeight = 0;

        for (int i = 0; i < mAdapter.getItemCount(); i++) {
            View mView = recyclerView.findViewHolderForAdapterPosition(i).itemView;

            mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
        }

        if (totalHeight > 100) {
            ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
            params.height = 100;
            recyclerView.setLayoutParams(params);
        }
    }

}
