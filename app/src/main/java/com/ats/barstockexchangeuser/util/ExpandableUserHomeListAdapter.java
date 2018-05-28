package com.ats.barstockexchangeuser.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ats.barstockexchangeuser.R;
import com.ats.barstockexchangeuser.activity.AddMixerActivity;
import com.ats.barstockexchangeuser.activity.OrderReviewActivity;
import com.ats.barstockexchangeuser.bean.Item;
import com.ats.barstockexchangeuser.bean.Settings;
import com.ats.barstockexchangeuser.bean.TempDataBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by maxadmin on 2/11/17.
 */

public class ExpandableUserHomeListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Activity activity;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Item>> listDataChild;
    private ArrayAdapter<Integer> arrayAdapter;


    public ExpandableUserHomeListAdapter(Activity activity, Context context, List<String> listDataHeader,
                                         HashMap<String, List<Item>> listChildData) {
        this.activity = activity;
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }


    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.listDataChild.get(this.listDataHeader.get(i))
                .size();
    }

    @Override
    public Object getGroup(int i) {
        return this.listDataHeader.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return this.listDataChild.get(this.listDataHeader.get(i))
                .get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        view = null;
        String headerTitle = (String) getGroup(i);
        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.expandable_user_home_header, null);
            ExpandableListView mExpandableListView = (ExpandableListView) viewGroup;
            mExpandableListView.expandGroup(i);
        }

        TextView lblListHeader = (TextView) view
                .findViewById(R.id.tvExpUserHeader_title);
        lblListHeader.setText(headerTitle);

        TextView tvUpdate = (TextView) view
                .findViewById(R.id.tvExpUserHeader_Count);
        tvUpdate.setText("" + getChildrenCount(i) + " items");


        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Log.e("i : ", "--------------" + i);
        Log.e("i1 : ", "--------------" + i1);

        Log.e("child : --------", "" + listDataChild);
        Log.e("getChild()--------", "" + getChild(i, i1));

        final Item item = (Item) getChild(i, i1);

        try {
            final String childText = (String) getChild(i, i1);

        } catch (Exception e) {
        }

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.expandable_user_home_items, null);
        }

        LinearLayout llItem = (LinearLayout) view.findViewById(R.id.llExpUserList);

        TextView txtListChild = (TextView) view
                .findViewById(R.id.tvExpUserItems_title);

        TextView txtListChildHigh = (TextView) view
                .findViewById(R.id.tvExpUserItems_high);

        TextView txtListChildLow = (TextView) view
                .findViewById(R.id.tvExpUserItems_low);

        TextView txtListChildPrice = (TextView) view
                .findViewById(R.id.tvExpUserItems_price);

        ImageView ivArrow = (ImageView) view
                .findViewById(R.id.ivExpUserItems_arrow);

        txtListChild.setText("" + item.getItemName());

        SharedPreferences pref = context.getSharedPreferences(InterfaceApi.MY_PREF, MODE_PRIVATE);
        Gson gson = new Gson();
        String json2 = pref.getString("Settings", "");
        Settings settings = gson.fromJson(json2, Settings.class);
        Log.e("Settings Bean : ", "---------------" + settings);
        if (settings != null) {
            if (settings.getAppMode().equalsIgnoreCase("Game")) {
                txtListChildHigh.setText("" + String.format("%.0f", item.getMaxRate()));
                txtListChildLow.setText("" + String.format("%.0f", item.getMinRate()));
                txtListChildPrice.setText("" + String.format("%.0f", item.getOpeningRate()));

                float mean = (item.getMaxRate() + item.getMinRate()) / 2;
                if (item.getOpeningRate() > mean) {
                    ivArrow.setImageResource(R.mipmap.up_icon);
                } else {
                    ivArrow.setImageResource(R.mipmap.down_icon);
                }

            } else if (settings.getAppMode().equalsIgnoreCase("Special")) {
                txtListChildPrice.setText("" + String.format("%.0f", item.getMrpSpecial()));
            } else if (settings.getAppMode().equalsIgnoreCase("Regular")) {
                txtListChildPrice.setText("" + String.format("%.0f", item.getMrpRegular()));
            }
        } else {
            txtListChildPrice.setText("" + String.format("%.0f", item.getMrpRegular()));
        }


        llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_quantity_dialog);

                final ListView lvQty = (ListView) dialog.findViewById(R.id.lvQtyDialog);

                final ArrayList<Integer> intArray = new ArrayList<>();
                for (int i = 1; i <= 100; i++) {
                    intArray.add(i);
                }

                arrayAdapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_expandable_list_item_1, intArray) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        LayoutInflater inflater1 = activity.getLayoutInflater();
                        View view = inflater1.inflate(R.layout.custom_quantity_item_layout, parent, false);
                        TextView tvQty = view.findViewById(R.id.tvQtyItem);
                        tvQty.setText("" + intArray.get(position));
                        return view;
                    }
                };
                lvQty.setAdapter(arrayAdapter);

                lvQty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //Toast.makeText(context, "" + (i + 1), Toast.LENGTH_SHORT).show();


                        SharedPreferences pref = context.getSharedPreferences(InterfaceApi.MY_PREF, MODE_PRIVATE);
                        Gson gson = new Gson();
                        String json2 = pref.getString("Settings", "");
                        Settings settings = gson.fromJson(json2, Settings.class);
                        Log.e("Settings Bean : ", "---------------" + settings);

                        if (settings != null) {
                            if (settings.getAppMode().equalsIgnoreCase("Game")) {

                                if (item.getIsMixerApplicable() == 1) {
                                    Intent intent = new Intent(context, AddMixerActivity.class);
                                    intent.putExtra("ItemId", item.getItemId());
                                    intent.putExtra("ItemName", item.getItemName());
                                    intent.putExtra("ItemPrice", item.getOpeningRate());
                                    intent.putExtra("ItemQuantity", intArray.get(i));
                                    intent.putExtra("ItemSgst", item.getSgst());
                                    intent.putExtra("ItemCgst", item.getCgst());
                                    context.startActivity(intent);
                                } else {
                                    ArrayList<TempDataBean> tempDataBeanArrayList = new ArrayList<>();
                                    TempDataBean data = new TempDataBean(item.getItemId(), item.getItemName(), item.getOpeningRate(), intArray.get(i), item.getSgst(), item.getCgst());
                                    tempDataBeanArrayList.add(data);

                                    Gson gson1 = new Gson();
                                    String jsonTempArray = gson.toJson(tempDataBeanArrayList);
                                    Intent intent = new Intent(context, OrderReviewActivity.class);
                                    intent.putExtra("TempDataArray", jsonTempArray);
                                    context.startActivity(intent);

                                }
                            } else if (settings.getAppMode().equalsIgnoreCase("Special")) {
                                if (item.getIsMixerApplicable() == 1) {
                                    Intent intent = new Intent(context, AddMixerActivity.class);
                                    intent.putExtra("ItemId", item.getItemId());
                                    intent.putExtra("ItemName", item.getItemName());
                                    intent.putExtra("ItemPrice", item.getMrpSpecial());
                                    intent.putExtra("ItemQuantity", intArray.get(i));
                                    intent.putExtra("ItemSgst", item.getSgst());
                                    intent.putExtra("ItemCgst", item.getCgst());
                                    context.startActivity(intent);
                                } else {
                                    ArrayList<TempDataBean> tempDataBeanArrayList = new ArrayList<>();
                                    TempDataBean data = new TempDataBean(item.getItemId(), item.getItemName(), item.getMrpSpecial(), intArray.get(i), item.getSgst(), item.getCgst());
                                    tempDataBeanArrayList.add(data);

                                    Gson gson1 = new Gson();
                                    String jsonTempArray = gson.toJson(tempDataBeanArrayList);
                                    Intent intent = new Intent(context, OrderReviewActivity.class);
                                    intent.putExtra("TempDataArray", jsonTempArray);
                                    context.startActivity(intent);
                                }

                            } else if (settings.getAppMode().equalsIgnoreCase("Regular")) {

                                if (item.getIsMixerApplicable() == 1) {
                                    Intent intent = new Intent(context, AddMixerActivity.class);
                                    intent.putExtra("ItemId", item.getItemId());
                                    intent.putExtra("ItemName", item.getItemName());
                                    intent.putExtra("ItemPrice", item.getMrpRegular());
                                    intent.putExtra("ItemQuantity", intArray.get(i));
                                    intent.putExtra("ItemSgst", item.getSgst());
                                    intent.putExtra("ItemCgst", item.getCgst());
                                    context.startActivity(intent);
                                } else {
                                    ArrayList<TempDataBean> tempDataBeanArrayList = new ArrayList<>();
                                    TempDataBean data = new TempDataBean(item.getItemId(), item.getItemName(), item.getMrpRegular(), intArray.get(i), item.getSgst(), item.getCgst());
                                    tempDataBeanArrayList.add(data);

                                    Gson gson1 = new Gson();
                                    String jsonTempArray = gson.toJson(tempDataBeanArrayList);
                                    Intent intent = new Intent(context, OrderReviewActivity.class);
                                    intent.putExtra("TempDataArray", jsonTempArray);
                                    context.startActivity(intent);
                                }

                            }
                        } else {

                            if (item.getIsMixerApplicable() == 1) {
                                Intent intent = new Intent(context, AddMixerActivity.class);
                                intent.putExtra("ItemId", item.getItemId());
                                intent.putExtra("ItemName", item.getItemName());
                                intent.putExtra("ItemPrice", item.getMrpRegular());
                                intent.putExtra("ItemQuantity", intArray.get(i));
                                intent.putExtra("ItemSgst", item.getSgst());
                                intent.putExtra("ItemCgst", item.getCgst());
                                context.startActivity(intent);
                            } else {
                                ArrayList<TempDataBean> tempDataBeanArrayList = new ArrayList<>();
                                TempDataBean data = new TempDataBean(item.getItemId(), item.getItemName(), item.getMrpRegular(), intArray.get(i), item.getSgst(), item.getCgst());
                                tempDataBeanArrayList.add(data);

                                Gson gson1 = new Gson();
                                String jsonTempArray = gson.toJson(tempDataBeanArrayList);
                                Intent intent = new Intent(context, OrderReviewActivity.class);
                                intent.putExtra("TempDataArray", jsonTempArray);
                                context.startActivity(intent);
                            }
                        }


                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


}
