package com.ats.barstockexchangeuser.fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ats.barstockexchangeuser.R;
import com.ats.barstockexchangeuser.bean.News;
import com.ats.barstockexchangeuser.bean.NewsData;
import com.ats.barstockexchangeuser.util.CheckNetwork;
import com.ats.barstockexchangeuser.util.InterfaceApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ats.barstockexchangeuser.activity.HomeActivity.tvTitle;

public class NewsFragment extends Fragment {

    private ListView lvNews;
    private ArrayList<News> newsArrayList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        tvTitle.setText("News");

        lvNews = view.findViewById(R.id.lvNews);
        getNewsData();
        return view;
    }



    public void getNewsData() {
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
            Call<NewsData> newsDataCall = api.getAllNews();


            final Dialog progressDialog = new Dialog(getContext());
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setContentView(R.layout.loading_progress_layout);
            progressDialog.show();


            newsDataCall.enqueue(new Callback<NewsData>() {
                @Override
                public void onResponse(Call<NewsData> call, Response<NewsData> response) {
                    try {
                        if (response.body() != null) {
                            NewsData data = response.body();
                            if (data.getErrorMessage().getError()) {
                                progressDialog.dismiss();
                                //Log.e("ON RESPONSE : ", " ERROR : " + data.getErrorMessage().getMessage());
                                Toast.makeText(getContext(), "No News Available", Toast.LENGTH_SHORT).show();
                            } else {
                                newsArrayList.clear();
                                for (int i = 0; i < data.getNews().size(); i++) {
                                    newsArrayList.add(i, data.getNews().get(i));
                                }
                                //Log.e("RESPONSE : ", " DATA : " + newsArrayList);
                                //setAdapterData();
                                NewsDisplayAdapter adapter = new NewsDisplayAdapter(getContext(), newsArrayList);
                                lvNews.setAdapter(adapter);
                                progressDialog.dismiss();

                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "No News Available", Toast.LENGTH_SHORT).show();
                            //Log.e("RESPONSE : ", " NO DATA");
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "No News Available", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        //Log.e("Exception : ", "" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<NewsData> call, Throwable t) {
                    Toast.makeText(getContext(), "No News Available", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //Log.e("ON FAILURE : ", " ERROR : " + t.getMessage());
                }
            });


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
            builder.setTitle("Check Connectivity");
            builder.setCancelable(false);
            builder.setMessage("Please Connect to Internet");
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



    class NewsDisplayAdapter extends BaseAdapter{

        Context context;
        private ArrayList<News> originalValues;
        private ArrayList<News> displayedValues;
        LayoutInflater inflater;

        public NewsDisplayAdapter(Context context, ArrayList<News> newsArray) {
            this.context = context;
            this.originalValues = newsArray;
            this.displayedValues = newsArray;
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
            v = inflater.inflate(R.layout.custom_news_master_layout, null);

            TextView tvDate = v.findViewById(R.id.tvNewsMaster_date);
            TextView tvName = v.findViewById(R.id.tvNewsMaster_title);
            TextView tvDesc = v.findViewById(R.id.tvNewsMaster_desc);
            ImageView ivImage = v.findViewById(R.id.ivNewsMaster_photo);
            ImageView ivpopup = v.findViewById(R.id.ivNewsMaster_popup);


            String yyyy = displayedValues.get(position).getNewsDate().substring(0, 4);
            String mm = displayedValues.get(position).getNewsDate().substring(5, 7);
            String dd = displayedValues.get(position).getNewsDate().substring(8, 10);

            tvDate.setText(" " + dd + "-" + mm + "-" + yyyy+" ");

            //tvDate.setText(" " + displayedValues.get(position).getNewsDate() + " ");
            tvName.setText("" + displayedValues.get(position).getNewsTitle());
            tvDesc.setText("" + displayedValues.get(position).getNewsDesc());

            try {
                Picasso.with(getContext())
                        .load(InterfaceApi.IMAGE_PATH+""+displayedValues.get(position).getNewsImage())
                        .placeholder(R.mipmap.alpha_logo)
                        .error(R.mipmap.alpha_logo)
                        .into(ivImage);
            } catch (Exception e) {
            }

            return v;
        }
    }


}
