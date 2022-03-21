package com.sap.copy.mdui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sap.cloud.android.odata.eam_ntf_create_entities.MyNotification;
import com.sap.copy.R;
import com.sap.copy.mdui.mynotificationset.MyNotificationSetActivity;

public class Test2 extends MyNotificationSetActivity implements InterfacedFragment.InterfacedFragmentListener<MyNotification> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
    }
}