package com.william.parksystem.UserHomepage.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.william.parksystem.Information.Park;
import com.william.parksystem.Information.User;
import com.william.parksystem.R;
import com.william.parksystem.SQLite.DBServerForPark;
import com.william.parksystem.SQLite.DBServerForU;

import java.util.Calendar;

public class PlaceFragment extends Fragment {

    TextView txtPInfo;
    Button btnLooking;

    User user = new User();
    Park park = new Park();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place, null);

        txtPInfo = view.findViewById(R.id.txtPlace_PInfo);
        btnLooking = view.findViewById(R.id.btn_looking);

        Bundle bundle = this.getArguments();
        String username = bundle.getString("username");
        Log.i("getUser",username);

            user.setUsername(username);
            DBServerForU dbServerForU = new DBServerForU(getContext());
            dbServerForU.open();
            Cursor cursor = dbServerForU.selectLicense(user.getUsername());
            while (cursor.moveToNext()){
                park.setLicenseNumber(cursor.getString(cursor.getColumnIndex("licenseNumber")));
                Log.i("up",park.getLicenseNumber());
            }
            dbServerForU.close();

        btnLooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBServerForPark dbServerForPark = new DBServerForPark(getContext());
                dbServerForPark.open();

                int place = 0;

                for (int i = 0; i < 100; i++) {
                    ++place;
                    if (dbServerForPark.selectPlace(place)) {
                        if (dbServerForPark.insert(park.getLicenseNumber())) {
                            park.setPlace(place);
                            Log.i("place", "insert License Number");
                            break;
                        }
                    }
                }

                if (place != 0) {
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    String time = hour + ":" + minute;

                    park.setTime(time);
                    dbServerForPark.updataPackPlace(park.getPlace(), park.getLicenseNumber(), park.getTime());

                    btnLooking.setVisibility(View.GONE);
                    txtPInfo.setVisibility(View.VISIBLE);
                    txtPInfo.setText("Your parking space is\n" + park.getPlace());
                } else {
                    Toast.makeText(getActivity(), "Parking is full!\nPlease wait for a moment", Toast.LENGTH_SHORT).show();
                }

                dbServerForPark.close();
            }
        });

        return view;
    }
}
