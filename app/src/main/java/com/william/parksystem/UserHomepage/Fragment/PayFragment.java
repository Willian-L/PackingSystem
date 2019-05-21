package com.william.parksystem.UserHomepage.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.william.parksystem.Information.Park;
import com.william.parksystem.Information.User;
import com.william.parksystem.R;
import com.william.parksystem.SQLite.DBServerForPark;
import com.william.parksystem.SQLite.DBServerForU;

import java.util.Calendar;

public class PayFragment extends Fragment {

    User user = new User();
    Park park = new Park();

    LinearLayout ll_yes, ll_no;
    TextView txt_Time, txt_price, txt_ToTime;
    Button btn_pay;

    private String startTime = null;
    private double price = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay, null);

        init(view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user.setUsername(bundle.getString("username"));
        }

        DBServerForU dbServerForU = new DBServerForU(getContext());
        dbServerForU.open();
        Cursor cursor = dbServerForU.selectLicense(user.getUsername());
        while (cursor.moveToNext()) {
            park.setLicenseNumber(cursor.getString(cursor.getColumnIndex("licenseNumber")));
            Log.i("up", park.getLicenseNumber());
        }
        dbServerForU.close();

        final DBServerForPark dbServerForPark = new DBServerForPark(getContext());
        dbServerForPark.open();
        int place = dbServerForPark.selectPlaceByLN(park.getLicenseNumber());
        if (place > 0) {
            startTime = dbServerForPark.selectTime(place);
        }

        if (startTime != null) {
            ll_no.setVisibility(View.GONE);
            ll_yes.setVisibility(View.VISIBLE);
            txt_Time.setText(startTime);
            int tap = startTime.indexOf(":");
            int hour = Integer.parseInt(startTime.substring(0, tap));
            Calendar calendar = Calendar.getInstance();
            int hour_now = calendar.get(Calendar.HOUR_OF_DAY);
            int total_time = hour_now - hour;
            txt_ToTime.setText(total_time + "H");
            price = total_time * 2;
            park.setPrice(price);
            txt_price.setText("¥" + price);
        }

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LN", park.getLicenseNumber()+park.getPrice());
                dbServerForPark.open();
                if (dbServerForPark.pay(park.getLicenseNumber(), park.getPrice())) {
                    ll_yes.setVisibility(View.GONE);
                    txt_price.setText(null);
                    txt_Time.setText(null);
                    txt_ToTime.setText(null);
                    ll_no.setVisibility(View.VISIBLE);
                }
                dbServerForPark.close();
            }
        });

        dbServerForPark.close();

        return view;
    }

    private void init(View view) {
        ll_yes = view.findViewById(R.id.linlay_yes);
        ll_no = view.findViewById(R.id.linlay_no);
        txt_Time = view.findViewById(R.id.txt_entryTime);
        txt_price = view.findViewById(R.id.txt_price);
        txt_ToTime = view.findViewById(R.id.txt_TotalTime);
        btn_pay = view.findViewById(R.id.btn_pay);
    }
}
