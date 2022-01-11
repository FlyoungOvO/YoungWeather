package com.example.coolweather;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectedProvince;
    private City selectedCity;
    private int currentLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_button);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);

        //requireActivity() 返回的是宿主activity
        requireActivity().getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull @NotNull LifecycleOwner source,
                                       @NonNull @NotNull Lifecycle.Event event) {
                if (event.getTargetState() == Lifecycle.State.CREATED){
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent,View view,int position,
                                                long id) {
                            if(currentLevel == LEVEL_PROVINCE){
                                selectedProvince = provinceList.get(position);
                                queryCities();
                            }else if(currentLevel == LEVEL_CITY){
                                selectedCity = cityList.get(position);
                                queryCounties();
                            }
                        }
                    });
                    backButton.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View v){
                            if(currentLevel == LEVEL_COUNTY){
                                queryCities();
                            }else if(currentLevel == LEVEL_CITY){
                                queryCounties();
                            }
                        }
                    });
                    queryProvinces();
                    getLifecycle().removeObserver(this);  //这里是删除观察者
                }
            }
        });
    }

    private void queryProvinces(){
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = LitePal.findAll(Province.class);
        if(provinceList.size() > 0){
            dataList.clear();
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryCities(){
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.GONE);
    }

    private void queryCounties(){

    }

    private void queryFromServer(){

    }
}
