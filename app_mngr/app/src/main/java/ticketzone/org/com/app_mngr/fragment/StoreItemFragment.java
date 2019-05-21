package ticketzone.org.com.app_mngr.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import ticketzone.org.com.app_mngr.ActivityManager;
import ticketzone.org.com.app_mngr.R;

public class StoreItemFragment extends Fragment {
    private ActivityManager am = ActivityManager.getInstance();
    private ImageButton store_img;
    private TextView store_name;

    public StoreItemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String storeList;
        JSONObject jobj;
        View view = null;
//        am.addActivity(getActivity());
//        try {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_store_item, container, false);
            store_img = view.findViewById(R.id.store_img);
            store_name = view.findViewById(R.id.store_name);

            storeList = getArguments().getString("storeList");
//            jobj = new JSONObject(storeList);
            store_name.setText(storeList);

//            return view;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return view;
    }

}
