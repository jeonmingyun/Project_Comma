package com.org.ticketzone.app_mem.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.org.ticketzone.app_mem.R;
import com.org.ticketzone.app_mem.task.JsonArrayTask;
import com.org.ticketzone.app_mem.task.NetworkTask;
import com.org.ticketzone.app_mem.task.SendDataSet;
import com.org.ticketzone.app_mem.db.DBOpenHelper;
import com.org.ticketzone.app_mem.listViewAdapter.CustomAdapter;
import com.org.ticketzone.app_mem.vo.StoreVO;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;
    private ListView listview = null, storeListView;
    private DBOpenHelper mDBHelper;
    private ArrayList<StoreVO> storeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBHelper = new DBOpenHelper(this);

        toolbar(); // menu toolbar
        tabHost(); // LinearLayout 페이지 바꿔끼우기
        selectAllStore(); // storeList = store table data select
        storeList(); // store tab에서 store list를 보여줌
    }

    // store list 생성
    protected void storeList() {
        storeListView = (ListView)findViewById(R.id.store_list_view);
        CustomAdapter<StoreVO> storeAdapter;

        storeAdapter = new CustomAdapter<StoreVO>(storeList) {
            @Override
            public View getView(final int idx, View view, ViewGroup parent) {
                view = getLayoutInflater().inflate(R.layout.store_list_item, null);
                String license_number = storeList.get(idx).getLicense_number();
                Cursor cursor = mDBHelper.countTeam(license_number);
                String count = "";

                while(cursor.moveToNext()){
                    count = cursor.getString(0);
                }
//                ImageView storeImg = (ImageView)view.findViewById(R.id.store_img);
                TextView storeName = (TextView)view.findViewById(R.id.store_name);
                TextView store_address = (TextView)view.findViewById(R.id.store_address);
                TextView waiting = (TextView)view.findViewById(R.id.waiting);
                TextView bluetooth = (TextView)view.findViewById(R.id.bluetooth);
                final Button TAGBTN = (Button)view.findViewById(R.id.tag_btn);

//                storeImg.setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_launcher_background));
                storeName.setText(storeList.get(idx).getStore_name());
                store_address.setText(storeList.get(idx).getAddress_name());
                waiting.setText(count + "팀");
                bluetooth.setText("bluetooth status");
                view.setTag(idx);   // 인덱스 저장
                TAGBTN.setTag(idx);
                TAGBTN.setText("발급 가능");

                // 발급 버튼 클릭
                TAGBTN.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        v.setTag(TAGBTN.getTag());
                        int btnIndex = (Integer)TAGBTN.getTag();  //인덱스 변수 선언
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
            Log.e("store_name", cursor.getString(8));
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
            storeVO.setAddress_name(cursor.getString(10));

            storeList.add(storeVO);
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
        TabHost host=(TabHost)findViewById(R.id.host);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("store list");
        spec.setIndicator("store list");
        spec.setContent(R.id.store_list);
        host.addTab(spec);

        spec = host.newTabSpec("categorie");
        spec.setIndicator("categorie");
        spec.setContent(R.id.categorie);
        host.addTab(spec);
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
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if(id == R.id.logout){
//            Intent logoutIntent = new Intent(this, LoginActivity.class);
//            startActivity(logoutIntent);
//        }
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
