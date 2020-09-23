package com.example.profile;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment1 extends Fragment {

    public static final String Server_URL="https://jsonplaceholder.typicode.com/posts";
    ArrayAdapter<String> adapter;
    ArrayList<UserData> userList;
    ListView listView1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment1_layout,container,false);

         listView1= view.findViewById(R.id.listview1);
        final Dialog myDialog = new Dialog(getActivity());
         userList = new ArrayList<>();
        extractProduct();




        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView txtclose,textView,textid,textBody,textuserid;
                UserData user = userList.get(i);

                myDialog.setContentView(R.layout.customepopup);
                txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
                textView=(TextView)myDialog.findViewById(R.id.title);
                textid=(TextView)myDialog.findViewById(R.id.id);
                textuserid=myDialog.findViewById(R.id.userid);
                textBody =(TextView) myDialog.findViewById(R.id.bodytext);
                txtclose.setText("X");
                textView.setText(user.getTitle());
                textuserid.setText(String.valueOf(user.getUserId()));
                textid.setText(String.valueOf(user.getId()));
                textBody.setText(user.getBody());
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });



        return view;
    }


    private void extractProduct() {
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Server_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray ProductArray=new JSONArray(response);
                    String[] name=new String[ProductArray.length()];
                    for(int i=0;i<ProductArray.length();i++){
                        JSONObject productObject=ProductArray.getJSONObject(i);

                        int Uid = productObject.getInt("userId");
                        int id = productObject.getInt("id");
                        String title_Name=productObject.getString("title");
                        String body_text = productObject.getString("body");

                        name[i]=title_Name;
                        UserData userData=new UserData(Uid,id,title_Name,body_text);
                        userList.add(userData);
                    }
                    adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,name);

                    listView1.setAdapter(adapter);


                } catch (JSONException e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }
}
