package com.sap.copy.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.sap.copy.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sap.copy.mdui.EntitySetListActivity;
import com.sap.copy.mdui.HomePage;

public class MainBusinessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_business);
    }

    private void startHomePage() {
        SAPWizardApplication application = (SAPWizardApplication) getApplication();
        application.getSAPServiceManager().openODataStore(() -> {
            Intent intent = new Intent(this, HomePage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startHomePage();
    }

}
