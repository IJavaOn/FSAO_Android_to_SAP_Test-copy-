package com.sap.copy.mdui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sap.copy.R;

public class NotificationList extends AppCompatActivity {

    //  implements InterfacedFragment.InterfacedFragmentListener<MyNotification> {

    private Button buttoncreate;
    private ImageView add;
    private Button buttondetails;
    private EditText editTextTextMultiLine;
    private EditText editTextTextMultiLine3;
    private TextView createdby, date;
    private ImageButton navback;


    //Odata Binding components

    private static final String KEY_IS_NAVIGATION_DISABLED = "isNavigationDisabled";
    private static final String KEY_IS_NAVIGATION_FROM_HOME = "isNavigationFromHome";

    /**
     * Flag to indicate whether both master and detail frames should be visible at the same time
     */
    private Boolean isMasterDetailView;

    /**
     * Flag to indicate whether requesting user confirmation before navigation is needed
     */
    protected boolean isNavigationDisabled = false;

    /**
     * Flag to tell whether back action is from home click or or others
     */
    private boolean isConfirmDataLossFromHomeButton = false;

    //odata ends here


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);


    /*   //Odata binding
        isMasterDetailView = getResources().getBoolean(R.bool.two_pane);
        setContentView(R.layout.activity_entityitem);

        if (savedInstanceState != null) {
            this.isNavigationDisabled = savedInstanceState.getBoolean(KEY_IS_NAVIGATION_DISABLED, false);
            this.isConfirmDataLossFromHomeButton = savedInstanceState.getBoolean(KEY_IS_NAVIGATION_FROM_HOME, false);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.masterFrame, new MyNotificationSetListFragment(), UIConstants.LIST_FRAGMENT_TAG)
                    .commit();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        //odata ends here

        buttoncreate = findViewById(R.id.buttoncreate);
        add = findViewById(R.id.add);
        buttondetails = findViewById(R.id.buttondetails);
        navback = findViewById(R.id.navback);

        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);
        editTextTextMultiLine3 = findViewById(R.id.editTextTextMultiLine3);


     /*   String text = getIntent().getStringExtra("createdby");
        String text1 = getIntent().getStringExtra("date");


        createdby.setText(text);
        date.setText(text1);*/


        buttoncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationList.this, CreateNotification.class);
                startActivity(intent);
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationList.this, DisplayNotification.class);
                startActivity(intent);

             /*   String text = editTextTextMultiLine.getText().toString();
                String text1 = editTextTextMultiLine3.getText().toString();

                Intent intent = new Intent(Homepage.this, Homepage.class);
                intent.putExtra("editTextTextMultiLine",text);
                intent.putExtra("editTextTextMultiLine3",text1);
                startActivity(intent);*/


            }
        });


        buttondetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationList.this, DisplayNotification.class);
                startActivity(intent);
            }
        });

        navback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotificationList.this,HomePage.class);
                startActivity(intent);
            }
        });

    }

    //Starts Odata Binding components
    /*
       @Override
        public void onFragmentStateChange ( int eventId, @NonNull @NotNull MyNotification entity){
            switch (eventId) {
                case UIConstants.EVENT_CREATE_NEW_ITEM:
                    onCreateNewItem();
                    break;
                case UIConstants.EVENT_ITEM_CLICKED:
                    onItemClicked(entity);
                    break;
                case UIConstants.EVENT_DELETION_COMPLETED:
                    onDeleteComplete();
                    break;
                case UIConstants.EVENT_EDIT_ITEM:
                    onEditItem(entity);
                    break;
                case UIConstants.EVENT_ASK_DELETE_CONFIRMATION:
                    onConfirmDelete();
                    break;
                case UIConstants.EVENT_BACK_NAVIGATION_CONFIRMED:
                    isNavigationDisabled = false;
                    if (isConfirmDataLossFromHomeButton) {
                        Intent intent = new Intent(this, EntitySetListActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        super.onBackPressed();
                    }
                    break;
            }
        }

        private void onCreateNewItem () {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count > 0) {
                String name = getSupportFragmentManager().getBackStackEntryAt(count - 1).getName();
                if (name == UIConstants.CREATE_FRAGMENT_TAG || name == UIConstants.MODIFY_FRAGMENT_TAG) {
                    Toast.makeText(this, "Please save your changes first...", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            Bundle arguments = new Bundle();
            arguments.putString(BundleKeys.OPERATION, UIConstants.OP_CREATE);
            MyNotificationSetCreateFragment fragment = new MyNotificationSetCreateFragment();
            fragment.setArguments(arguments);
            int containerId = isMasterDetailView ? R.id.detailFrame : R.id.masterFrame;
            getSupportFragmentManager().beginTransaction()
                    .replace(containerId, fragment, UIConstants.CREATE_FRAGMENT_TAG)
                    .addToBackStack(UIConstants.CREATE_FRAGMENT_TAG)
                    .commit();

        }
        private void onItemClicked (@Nullable MyNotification entity){
            if (!isMasterDetailView) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.masterFrame, new MyNotificationSetDetailFragment(), UIConstants.DETAIL_FRAGMENT_TAG)
                        .addToBackStack(UIConstants.DETAIL_FRAGMENT_TAG)
                        .commit();
            } else {
                Fragment detail = getSupportFragmentManager().findFragmentByTag(UIConstants.DETAIL_FRAGMENT_TAG);
                if (detail == null && entity != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.detailFrame, new MyNotificationSetDetailFragment(), UIConstants.DETAIL_FRAGMENT_TAG)
                            .commit();
                    return;
                }

                if (detail != null && entity == null) {
                    getSupportFragmentManager().beginTransaction().remove(detail).commit();
                    Toolbar sToolbar = findViewById(R.id.secondaryToolbar);
                    if (sToolbar != null) {
                        sToolbar.getMenu().clear();
                        sToolbar.setTitle("");
                    }
                }
            }
        }

        private void onDeleteComplete () {
            Toolbar secondaryToolbar = findViewById(R.id.secondaryToolbar);
            if (secondaryToolbar != null) {
                secondaryToolbar.setVisibility(View.INVISIBLE);
            }
            if (!isMasterDetailView) super.onBackPressed();
        }

        private void onEditItem (@Nullable MyNotification entity){
            Bundle arguments = new Bundle();
            arguments.putString(BundleKeys.OPERATION, UIConstants.OP_UPDATE);
            MyNotificationSetCreateFragment fragment = new MyNotificationSetCreateFragment();
            fragment.setArguments(arguments);
            int containerId = isMasterDetailView ? R.id.detailFrame : R.id.masterFrame;
            getSupportFragmentManager().beginTransaction()
                    .replace(containerId, fragment, UIConstants.MODIFY_FRAGMENT_TAG)
                    .addToBackStack(UIConstants.MODIFY_FRAGMENT_TAG)
                    .commit();
        }

        private void onConfirmDelete () {
            MyNotificationSetActivity.DeleteConfirmationDialogFragment dialog = new MyNotificationSetActivity.DeleteConfirmationDialogFragment();
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), UIConstants.CONFIRMATION_FRAGMENT_TAG);
        }*/

    //ends here

    }
