package com.org.ticketzone.app_mem.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import com.org.ticketzone.app_mem.GpsTracker;
import com.org.ticketzone.app_mem.R;
import com.org.ticketzone.app_mem.categorieCardView.RecyclerViewAdapter;
import com.org.ticketzone.app_mem.task.JsonArrayTask;
import com.org.ticketzone.app_mem.beacon.BeaconConnection;
import com.org.ticketzone.app_mem.task.JsonArrayTask;
import com.org.ticketzone.app_mem.task.NetworkTask;
import com.org.ticketzone.app_mem.task.SendDataSet;
import com.org.ticketzone.app_mem.db.DBOpenHelper;
import com.org.ticketzone.app_mem.listViewAdapter.CustomAdapter;
import com.org.ticketzone.app_mem.vo.BeaconVO;
import com.org.ticketzone.app_mem.vo.CategorieVO;
import com.org.ticketzone.app_mem.vo.StoreVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;
    private ListView listview = null, storeListView;
    private DBOpenHelper mDBHelper;
    private ArrayList<StoreVO> storeList;
    private ArrayList<BeaconVO> beaconList;
    // 비콘
    private BeaconManager beaconManager;
    private BeaconManager beaconManager2;
    private BeaconRegion region;
    private BeaconRegion region2;
    private boolean isConnected;

    private List<CategorieVO> cateList;

    //
    private int connect = 1;
    private int connect2=2;
    private String Minor = "";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    // GPS
    private GpsTracker gpsTracker;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private Double my_x;
    private Double my_y;

    // Img
    private List<Address> addresses;
    private String addr;
    private Button addressWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBHelper = new DBOpenHelper(this);

        mSwipeRefreshLayout = findViewById(R.id.swipe_layout);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        
        toolbar(); // menu toolbar
        tabHost(); // LinearLayout 페이지 바꿔끼우기
        selectAllStore(); // storeList = store table data select
        selectAllCate();
        selectAllBeacon(); // beaconList
        beaconConnection();
        storeList(); // store tab에서 store list를 보여줌
        cateList();

    //gps
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        gpsTracker = new GpsTracker(MainActivity.this);
        my_x = gpsTracker.getLatitude();
        my_y = gpsTracker.getLongitude();
        Log.e("xx", my_x.toString());
        String address = getCurrentAddress(my_x, my_y);
        addressWindow = findViewById(R.id.addressWindow);
        addressWindow.setText(addr);
        addressWindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapintent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(mapintent);
            }
        });
    }

    // select categorie table data from SQLite
    private void selectAllCate() {
        Cursor cursor = mDBHelper.selectAllCategorie();
        CategorieVO categorieVO;

        cateList = new ArrayList<>();

        while(cursor.moveToNext()) {
            categorieVO = new CategorieVO();
            categorieVO.setCate_name(cursor.getString(1));

            cateList.add(categorieVO);
        }
    }

    // categorie list 생성
    private void cateList() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        RecyclerViewAdapter recyclerAdapter = new RecyclerViewAdapter(cateList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(recyclerAdapter);
    }

    // store list 생성
    protected void storeList() {
        storeListView = findViewById(R.id.store_list_view);
        CustomAdapter<StoreVO> storeAdapter;

        storeAdapter = new CustomAdapter<StoreVO>(storeList) {
            @Override
            public View getView(final int idx, View view, ViewGroup parent) {
                //notifyDataSetChanged();
                view = getLayoutInflater().inflate(R.layout.store_list_item, null);
                String license_number = storeList.get(idx).getLicense_number();
                Cursor cursor = mDBHelper.countTeam(license_number);
                String count = "";

                while(cursor.moveToNext()){
                    count = cursor.getString(0);
                }

                ImageView storeImg = view.findViewById(R.id.store_img);
                TextView storeName = view.findViewById(R.id.store_name);
                TextView store_address = view.findViewById(R.id.store_address);
                TextView waiting = view.findViewById(R.id.waiting);
                final Button tagBtn = view.findViewById(R.id.tag_btn);
                String imageUrl = "http://15.164.115.73:8080/resources/img/" + storeList.get(idx).getImg_uploadpath() + "/" + storeList.get(idx).getImg_uuid() + "_" + storeList.get(idx).getImg_filename();

                Glide.with(view).load(imageUrl).centerCrop().into(storeImg);
                storeImg.setAlpha(130);

                storeName.setText(storeList.get(idx).getStore_name());
                store_address.setText(storeList.get(idx).getAddress_name());
                waiting.setText(count + "팀");
                view.setTag(idx);   // 인덱스 저장

                tagBtn.setTag(idx);
                tagBtn.setText("발급불가");
                tagBtn.setEnabled(false);

                String B_name[] =  new String[beaconList.size()];
                String B_id[] = new String[beaconList.size()];

                for(int i =0; i<beaconList.size(); i++){
                    B_name[i] = beaconList.get(i).getStore_name();
                    B_id[i] = beaconList.get(i).getB_code().substring(41);
                }

                for(int i =0; i<B_name.length; i++) {
                    if (storeName.getText().equals(B_name[i]) && Minor.equals(B_id[i])) {
                        tagBtn.setEnabled(true);
                        tagBtn.setText("발급가능");
                    }
                }

                // 발급 버튼 클릭
                tagBtn.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        v.setTag(tagBtn.getTag());
                        int btnIndex = (Integer)tagBtn.getTag();  //인덱스 변수 선언
                        final String LICENSE = storeList.get(btnIndex).getLicense_number();
                        final String STORE_NAME = storeList.get(btnIndex).getStore_name(); // 변수 설정 하는 법

                        final EditText ET = new EditText(MainActivity.this);
                        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                        dialog.setTitle("인원 수 설정");
                        dialog.setMessage("인원 수");
                        dialog.setView(ET);

                        // 확인 버튼 이벤트
                        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String inputValue = ET.getText().toString();
                                Cursor cursor = mDBHelper.selectAllMember();
                                TextView storeName = findViewById(R.id.storeName);
                                String member_id = "";

                                while(cursor.moveToNext()){
                                    member_id = cursor.getString(0);
                                }

                                NetworkTask networkTask = new NetworkTask("Mem_issue_ticket") {

                                };
                                SendDataSet sds1 = new SendDataSet("member_id", member_id);
                                SendDataSet sds2 = new SendDataSet("the_number", inputValue);
                                SendDataSet sds3 = new SendDataSet("license_number", LICENSE);
                                Log.e("111", member_id+", "+ inputValue + ", " + LICENSE);
                                networkTask.execute(sds1, sds2, sds3);
                                Toast.makeText(MainActivity.this, inputValue + "명 입력되었습니다.", Toast.LENGTH_SHORT).show();

                                JsonArrayTask jat = new JsonArrayTask("MyTicket"){
                                    @Override
                                    protected void onPostExecute(JSONArray jsonArray) {
                                        super.onPostExecute(jsonArray);
                                        try{
                                            mDBHelper.insertTicket(new JSONArray(jsonArray.get(0).toString()));
                                            Log.e("myTicket", jsonArray.get(0).toString());

                                        } catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                SendDataSet sds5 = new SendDataSet("member_id", member_id);
                                SendDataSet sds6 = new SendDataSet("license_number", LICENSE);
                                jat.execute(sds5,sds6);

                                Intent numInfoIntent = new Intent(MainActivity.this, NumInfoActivity.class);
                                numInfoIntent.putExtra("member_id", member_id);
                                numInfoIntent.putExtra("storename",STORE_NAME);
                                numInfoIntent.putExtra("license", LICENSE);
                                startActivity(numInfoIntent);
                            }
                        });

                        //취소 버튼 이벤트
                        dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();

                    }
                });

                storeItemClicked(idx, view);

                return view;
            }
        };

        storeListView.setAdapter(storeAdapter);
    }

    private void selectAllStore() { //StoreVO 에 값채우기
        Cursor cursor = mDBHelper.selectAllStore();
        storeList = new ArrayList<>();
        StoreVO storeVO;

        if(cursor.getCount() == 0) { // not found data
            Toast.makeText(this, "not found data", Toast.LENGTH_SHORT).show();
        }

        while(cursor.moveToNext()) {
            storeVO = new StoreVO();
            storeVO.setLicense_number(cursor.getString(0));
            storeVO.setR_name(cursor.getString(1));
            storeVO.setMax_number(cursor.getString(2));
            storeVO.setStore_status(cursor.getInt(3));
            storeVO.setCate_code(cursor.getString(4));
            storeVO.setOwner_id(cursor.getString(5));
            storeVO.setStore_tel(cursor.getString(6));
            storeVO.setStore_time(cursor.getString(7));
            storeVO.setStore_name(cursor.getString(8));
            storeVO.setStore_intro(cursor.getString(9));
            storeVO.setImg_uuid(cursor.getString(10));
            storeVO.setImg_uploadpath(cursor.getString(11));
            storeVO.setImg_filename(cursor.getString(12));
            storeVO.setAddress_name(cursor.getString(13));

            storeList.add(storeVO);
        }
    }



    private void selectAllBeacon(){
            Cursor cursor = mDBHelper.selectAllBeacon();
            beaconList = new ArrayList<>();
            BeaconVO beaconVO;

            while (cursor.moveToNext()){
                beaconVO = new BeaconVO();
                beaconVO.setB_code(cursor.getString(0));
                beaconVO.setStore_name(cursor.getString(1));
                beaconVO.setLicense_number(cursor.getString(2));

                beaconList.add(beaconVO);
            }

    }

    public void onClickLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                redirectLoginActivity();
            }
        });
    }

    protected void redirectLoginActivity() {
        final Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // tagHost 화면 Layout 바꿔끼우기
    protected void tabHost() {
        final TabHost host= findViewById(R.id.host);
        host.setup();
        host.getTabWidget().setBackgroundResource(R.drawable.non_selected_border);

        TabHost.TabSpec spec = host.newTabSpec("storeList");
        spec.setIndicator("매장");
        spec.setContent(R.id.store_list);
        host.addTab(spec);

        spec = host.newTabSpec("categorie");
        spec.setIndicator("카테고리");
        spec.setContent(R.id.categorie);
        host.addTab(spec);

        // TabWidet의 background 설정
        for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
            View tabView = host.getTabWidget().getChildAt(i);

            tabView.setBackgroundResource(R.drawable.non_selected_border); // unselected
            tabView.getLayoutParams().height = 150;
        }
        host.getTabWidget().getChildAt(host.getCurrentTab())
                .setBackgroundResource(R.drawable.selected_border); // selected

        // TabWidet tab 클릭시 background 설정
        host.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {
                for (int i = 0; i < host.getTabWidget().getChildCount(); i++) {
                    host.getTabWidget().getChildAt(i)
                            .setBackgroundResource(R.drawable.non_selected_border); // unselected
                }
                host.getTabWidget().getChildAt(host.getCurrentTab())
                        .setBackgroundResource(R.drawable.selected_border); // selected

            }
        });
    }

    // menu toolbar
    protected void toolbar() {
        toolbar = findViewById(R.id.toolbar);
        dlDrawer = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        dtToggle = new ActionBarDrawerToggle(this, dlDrawer, R.string.app_name, R.string.app_name);
        dlDrawer.addDrawerListener(dtToggle);

        final String[] items = {"홈","logout"};
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);

        listview = findViewById(R.id.drawer);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                switch (position){
                    case 0:
                        Intent homeIntent = new Intent(v.getContext(), MainActivity.class);
                        startActivity(homeIntent);
                        break;
                    case 1:
                        onClickLogout();
                        break;
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu){
        getMenuInflater().inflate(R.menu.main_navigation_menu, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);

        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume(){
        super.onResume();

        //블루투스 권환 승낙 및 블루투스 활성화
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });

    }

    @Override
    protected void onPause(){
        //beaconManager.stopRanging(region);
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void beaconConnection() {
        // 비콘
        // 비콘의 수신 범위를 갱신 받음
        beaconManager = new BeaconManager(MainActivity.this);
        beaconManager2 = new BeaconManager(MainActivity.this);
        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> list) {
                if (!list.isEmpty()) {
                    Beacon nearestBeacon = list.get(0);
                    Log.e("Airport", "Nearest places: " + nearestBeacon.getRssi());


                    if (nearestBeacon.getRssi() >  -90) {
                        Toast.makeText(MainActivity.this, "버튼활성화", Toast.LENGTH_LONG);
                        //tagBtn.setEnabled(true);

                        connect = nearestBeacon.getMinor();


                        Minor = String.valueOf(connect);
                        Log.e("test", "dddd");
                        Log.e("mino2", Minor);

                    } else if (nearestBeacon.getRssi() < -90) {
                        Toast.makeText(MainActivity.this, "연결이 끊어졌습니다.", Toast.LENGTH_SHORT);
                        connect = 0;
                        Log.e("test", "ddff");
                    }
                }

            }

        });


        region = new BeaconRegion("ranged region",
                UUID.fromString("74278bda-b644-4520-8f0c-720eaf059935"), 40001, 15383);
        //비콘 //
    }


    private void storeItemClicked( final int idx, View view) {
        LinearLayout storeItem = view.findViewById(R.id.store_list_wrapper);

        storeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String license_number = storeList.get(idx).getLicense_number();

                Intent intent = new Intent(MainActivity.this, StoreDetailActivity.class);
                intent.putExtra("license_number", license_number);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        storeList.removeAll(storeList);

        if(connect == 1){
            connect2 = 1;
        }else if(connect == 2){
            connect2 = 2;
        }
        JsonArrayTask jat = new JsonArrayTask("gpsTest"){
            @Override
            protected void onPostExecute(JSONArray jsonArray) {
                super.onPostExecute(jsonArray);
                try {

                    mDBHelper.insertGpsTest(new JSONArray(jsonArray.get(0).toString()));
                    JSONArray jarr;
                    JSONObject jobj;
                    StoreVO storeVO;
                    jarr = new JSONArray(jsonArray.get(0).toString());
                    for(int i=0; i<jarr.length(); i++){
                        Log.e("test", jarr.get(i).toString());
                        jobj = new JSONObject(jarr.get(i).toString());
                        Cursor cursor = mDBHelper.selectGpsStore(jobj.getString("store_name"));
                        while(cursor.moveToNext()){
                            storeVO = new StoreVO();
                            storeVO.setLicense_number(cursor.getString(0));
                            storeVO.setR_name(cursor.getString(1));
                            storeVO.setMax_number(cursor.getString(2));
                            storeVO.setStore_status(cursor.getInt(3));
                            storeVO.setCate_code(cursor.getString(4));
                            storeVO.setOwner_id(cursor.getString(5));
                            storeVO.setStore_tel(cursor.getString(6));
                            storeVO.setStore_time(cursor.getString(7));
                            storeVO.setStore_name(cursor.getString(8));
                            storeVO.setStore_intro(cursor.getString(9));
                            storeVO.setImg_uuid(cursor.getString(10));
                            storeVO.setImg_uploadpath(cursor.getString(11));
                            storeVO.setImg_filename(cursor.getString(12));
                            storeVO.setAddress_name(cursor.getString(13));

                            storeList.add(storeVO);
                        }
                        storeList();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        SendDataSet sds1 = new SendDataSet("my_x", my_x.toString());
        SendDataSet sds2 = new SendDataSet("my_y", my_y.toString());
        Log.e("loca", my_y.toString() + ", " + my_x.toString());

        jat.execute(sds1, sds2);
        gpsTracker = new GpsTracker(MainActivity.this);
        my_x = gpsTracker.getLatitude();
        my_y = gpsTracker.getLongitude();
        getCurrentAddress(my_x,my_y);
        Toast.makeText(MainActivity.this, "현재위치 \n위도 " + my_x + "\n경도 " + my_y, Toast.LENGTH_LONG).show();
        Log.e("test1",addr);

        addressWindow=findViewById(R.id.addressWindow);
        addressWindow.setText(addr);
        mSwipeRefreshLayout.setRefreshing(false);

    }
    //GPS
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음

            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(MainActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(MainActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(MainActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());



        try {

            latitude = Double.parseDouble(my_x.toString());
            longitude = Double.parseDouble(my_y.toString());

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1);
            Address a = addresses.get(0);

            for(int i=0; i<=a.getMaxAddressLineIndex(); i++){
                Log.e("test1234",a.getAddressLine(i));
                addr = a.getAddressLine(i);
            }
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
