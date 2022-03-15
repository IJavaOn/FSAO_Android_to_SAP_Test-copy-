package com.sap.copy.mdui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sap.cloud.android.odata.eam_ntf_create_entities.MyNotification;
import com.sap.cloud.mobile.fiori.object.ObjectHeader;
import com.sap.cloud.mobile.flowv2.core.DialogHelper;
import com.sap.cloud.mobile.odata.DataValue;
import com.sap.copy.R;
import com.sap.copy.databinding.FragmentMynotificationsetDetailBinding;
import com.sap.copy.mdui.mynotificationset.MyNotificationSetActivity;
import com.sap.copy.mdui.mynotificationset.MyNotificationSetCreateFragment;
import com.sap.copy.mdui.mynotificationset.MyNotificationSetDetailFragment;
import com.sap.copy.mdui.mynotificationset.MyNotificationSetListFragment;
import com.sap.copy.mdui.notificationheaderset.NotificationHeaderSetActivity;
import com.sap.copy.repository.OperationResult;
import com.sap.copy.service.SAPServiceManager;
import com.sap.copy.viewmodel.EntityViewModel;

import android.app.Application;
import android.os.Parcelable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.sap.cloud.android.odata.eam_ntf_create_entities.EAM_NTF_CREATE_EntitiesMetadata.EntitySets;
import com.sap.copy.viewmodel.mynotification.MyNotificationViewModel;

public class Test extends MyNotificationSetActivity implements InterfacedFragment.InterfacedFragmentListener<MyNotification>{
    //implements InterfacedFragment.InterfacedFragmentListener<MyNotification> {
    //implements EntityViewModel<MyNotification> {

    private static final String KEY_IS_NAVIGATION_DISABLED = "isNavigationDisabled";
    private static final String KEY_IS_NAVIGATION_FROM_HOME = "isNavigationFromHome";

    /** Flag to indicate whether both master and detail frames should be visible at the same time  */
    private Boolean isMasterDetailView;

    /** Flag to indicate whether requesting user confirmation before navigation is needed */
    protected boolean isNavigationDisabled = false;

    /** Flag to tell whether back action is from home click or or others */
    private boolean isConfirmDataLossFromHomeButton = false;

    /**
     * In this lifecycle state, some instance state variables gets loaded, {@link MyNotificationSetListFragment} is
     * getting instantiated (which will load {@link MyNotificationSetDetailFragment} if needed), visibilities of
     * master and detail frames gets set and the toolbar with buttons gets set.
     */


    /**
     * Generated data binding class based on layout file
     */
  /*  private FragmentMynotificationsetDetailBinding binding;

    /**
     * MyNotification entity to be displayed
     */
  //  private MyNotification myNotificationEntity = null;

    /**
     * Fiori ObjectHeader component used when entity is to be displayed on phone
     */
 //   private ObjectHeader objectHeader;

    /**
     * View model of the entity type that the displayed entity belongs to
     */
  //  private MyNotificationViewModel viewModel;

    /**
     * Service manager to provide root URL of OData Service for Glide to load images if there are media resources
     * associated with the entity type
     */
  //  private SAPServiceManager sapServiceManager;

    /** Arguments: MyNotification for display */
  /*  //odata starts here
    private static final String KEY_IS_NAVIGATION_DISABLED = "isNavigationDisabled";
    private static final String KEY_IS_NAVIGATION_FROM_HOME = "isNavigationFromHome";

    /**
     * Flag to indicate whether both master and detail frames should be visible at the same time
     */
  /*  private Boolean isMasterDetailView;

    /**
     * Flag to indicate whether requesting user confirmation before navigation is needed
     */
    // protected boolean isNavigationDisabled = false;

    /**
     * Flag to tell whether back action is from home click or or others
     */
    //  private boolean isConfirmDataLossFromHomeButton = false;

    /**
     * In this lifecycle state, some instance state variables gets loaded, {@link MyNotificationSetListFragment} is
     * getting instantiated (which will load {@link MyNotificationSetDetailFragment} if needed), visibilities of
     * master and detail frames gets set and the toolbar with buttons gets set.
     */
//odata ends here*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        isMasterDetailView = getResources().getBoolean(R.bool.two_pane);

        if (savedInstanceState != null) {
            this.isNavigationDisabled = savedInstanceState.getBoolean(KEY_IS_NAVIGATION_DISABLED, false);
            this.isConfirmDataLossFromHomeButton = savedInstanceState.getBoolean(KEY_IS_NAVIGATION_FROM_HOME, false);
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.masterFrame, new MyNotificationSetListFragment(), UIConstants.LIST_FRAGMENT_TAG)
                    .commit();
        }
      /*  Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
     //   menu = R.menu.itemlist_view_options;
     //   setHasOptionsMenu(true);
    }
    /** Let the Navigate Up button work like Back button */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_IS_NAVIGATION_DISABLED, this.isNavigationDisabled);
        outState.putBoolean(KEY_IS_NAVIGATION_FROM_HOME, this.isConfirmDataLossFromHomeButton);
        super.onSaveInstanceState(outState);
    }

    /**
     * Handles backwards navigation when user presses back button.
     */
    @Override
    public void onBackPressed() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
        askUserBeforeNavigation();
    }

    /**
     * Every fragment handles its own OptionsMenu so the activity does not have to.
     *
     * @return false, because this activity does not handles OptionItems
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_home) {
            isConfirmDataLossFromHomeButton = true;
            askUserBeforeNavigation();
            return true;
        }
        return false;
    }

    @Override
    public void onFragmentStateChange(int eventId, @Nullable MyNotification entity) {
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
                if(isConfirmDataLossFromHomeButton) {
                    Intent intent = new Intent(this, EntitySetListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    super.onBackPressed();
                }
                break;
        }
    }

    /**
     * Opens the UI to create a new entity.
     */
    private void onCreateNewItem() {
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

    /**
     * Handles the item click event from the list.
     *
     * @param entity The item clicked in the list
     */
    private void onItemClicked(@Nullable MyNotification entity) {
        if( !isMasterDetailView ) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.masterFrame, new MyNotificationSetDetailFragment(), UIConstants.DETAIL_FRAGMENT_TAG)
                    .addToBackStack(UIConstants.DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Fragment detail = getSupportFragmentManager().findFragmentByTag(UIConstants.DETAIL_FRAGMENT_TAG);
            if( detail == null && entity != null ) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detailFrame, new MyNotificationSetDetailFragment(), UIConstants.DETAIL_FRAGMENT_TAG)
                        .commit();
                return;
            }

            if( detail != null && entity == null ) {
                getSupportFragmentManager().beginTransaction().remove(detail).commit();
                Toolbar sToolbar = findViewById(R.id.secondaryToolbar);
                if( sToolbar != null ) {
                    sToolbar.getMenu().clear();
                    sToolbar.setTitle("");
                }
            }
        }
    }

    /**
     * Handles the situation after an entity is deleted, hide the progress bar, go back if neccessary.
     */
    private void onDeleteComplete() {
        Toolbar secondaryToolbar = findViewById(R.id.secondaryToolbar);
        if( secondaryToolbar != null ) {
            secondaryToolbar.setVisibility(View.INVISIBLE);
        }
        if( !isMasterDetailView ) super.onBackPressed();
    }

    /**
     * Edits the <code>entity</code>.
     *
     * @param entity The entity to be edited.
     */
    private void onEditItem(@Nullable MyNotification entity) {
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

    /**
     * Shows a dialog when user wants to delete an entity.
     */
    private void onConfirmDelete() {
        MyNotificationSetActivity.DeleteConfirmationDialogFragment dialog = new MyNotificationSetActivity.DeleteConfirmationDialogFragment();
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), UIConstants.CONFIRMATION_FRAGMENT_TAG);
    }

    /**
     * Shows an AlertDialog when user wants to leave {@link MyNotificationSetCreateFragment}.
     */
    public void askUserBeforeNavigation() {
        boolean editFragmentPresent = getSupportFragmentManager().findFragmentByTag(UIConstants.MODIFY_FRAGMENT_TAG) != null
                || getSupportFragmentManager().findFragmentByTag(UIConstants.CREATE_FRAGMENT_TAG) != null;
        if( editFragmentPresent && isNavigationDisabled ) {
            MyNotificationSetActivity.ConfirmationDialogFragment dialog = new MyNotificationSetActivity.ConfirmationDialogFragment();
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), UIConstants.CONFIRMATION_FRAGMENT_TAG);
        } else {
            onFragmentStateChange(UIConstants.EVENT_BACK_NAVIGATION_CONFIRMED,null);
        }
    }

    /**
     * Represents the confirmation dialog fragment when users tries to leave the create or edit
     * fragment to prevent data loss.
     */
    public static class ConfirmationDialogFragment extends DialogFragment {
        public ConfirmationDialogFragment() {
            super();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
            builder.setTitle(R.string.before_navigation_dialog_title);
            builder.setMessage(R.string.before_navigation_dialog_message);
            builder.setPositiveButton(R.string.before_navigation_dialog_positive_button, (dialog, which) -> {
                ((MyNotificationSetActivity)getActivity()).onFragmentStateChange(UIConstants.EVENT_BACK_NAVIGATION_CONFIRMED, null);
            });
            builder.setNegativeButton(R.string.before_navigation_dialog_negative_button, (dialog, which) -> {

            });
            return builder.create();
        }
    }

    /**
     * Represents the delete confirmation dialog fragment when users tries to delete an entity or entities
     */
    public static class DeleteConfirmationDialogFragment extends DialogFragment {
        public DeleteConfirmationDialogFragment() {
            super();
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
            MyNotificationViewModel viewModel = new ViewModelProvider(getActivity()).get(MyNotificationViewModel.class);
            if (viewModel.numberOfSelected() > 1) {
                builder.setTitle(R.string.delete_dialog_title).setMessage(R.string.delete_more_items);
            } else {
                builder.setTitle(R.string.delete_dialog_title).setMessage(R.string.delete_one_item);
            }

            builder.setPositiveButton(R.string.delete, (dialog,which) -> {
                try {
                    View progressBar = getActivity().findViewById(R.id.indeterminateBar);
                    if( progressBar != null ) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    if(viewModel.numberOfSelected() == 0) {
                        viewModel.addSelected(viewModel.getSelectedEntity().getValue());
                    }
                    viewModel.deleteSelected();
                } catch(Exception ex) {
                    new DialogHelper(getContext(), R.style.OnboardingDefaultTheme_Dialog_Alert)
                            .showOKOnlyDialog(
                                    getActivity().getSupportFragmentManager(),
                                    getResources().getString(R.string.delete_failed_detail),
                                    null, null, null);
                }
            });

            builder.setNegativeButton(R.string.cancel, (dialog,which) -> {
            });
            return builder.create();
        }
    }

 /*  @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return setupDataBinding(inflater, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(currentActivity).get(MyNotificationViewModel.class);
        viewModel.getDeleteResult().observe(getViewLifecycleOwner(), this::onDeleteComplete);
        viewModel.getSelectedEntity().observe(getViewLifecycleOwner(), entity -> {
            myNotificationEntity = entity;
            binding.setMyNotification(entity);
            setupObjectHeader();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_item:
                listener.onFragmentStateChange(UIConstants.EVENT_EDIT_ITEM, myNotificationEntity);
                return true;
            case R.id.delete_item:
                listener.onFragmentStateChange(UIConstants.EVENT_ASK_DELETE_CONFIRMATION,null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onNavigationClickedToNotificationHeaderSet_Header(View v) {
        Intent intent = new Intent(this.currentActivity, NotificationHeaderSetActivity.class);
        intent.putExtra("parent", myNotificationEntity);
        intent.putExtra("navigation", "Header");
        startActivity(intent);
    }


    /** Completion callback for delete operation */
   /* private void onDeleteComplete(@NonNull OperationResult<MyNotification> result) {
        if( progressBar != null ) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        viewModel.removeAllSelected(); //to make sure the 'action mode' not activated in the list
        Exception ex = result.getError();
        if (ex != null) {
            showError(getString(R.string.delete_failed_detail));
            return;
        }
        listener.onFragmentStateChange(UIConstants.EVENT_DELETION_COMPLETED, myNotificationEntity);
    }

    /**
     * Set detail image of ObjectHeader.
     * When the entity does not provides picture, set the first character of the masterProperty.
     */
  /*  private void setDetailImage(@NonNull ObjectHeader objectHeader, @NonNull MyNotification myNotificationEntity) {
        if (myNotificationEntity.getDataValue(MyNotification.notificationNumber) != null && !myNotificationEntity.getDataValue(MyNotification.notificationNumber).toString().isEmpty()) {
            objectHeader.setDetailImageCharacter(myNotificationEntity.getDataValue(MyNotification.notificationNumber).toString().substring(0, 1));
        } else {
            objectHeader.setDetailImageCharacter("?");
        }
    }

    /**
     * Setup ObjectHeader with an instance of MyNotification
     */
  /*  private void setupObjectHeader() {
        Toolbar secondToolbar = currentActivity.findViewById(R.id.secondaryToolbar);
        if (secondToolbar != null) {
            secondToolbar.setTitle(myNotificationEntity.getEntityType().getLocalName());
        } else {
            currentActivity.setTitle(myNotificationEntity.getEntityType().getLocalName());
        }

        // Object Header is not available in tablet mode
        objectHeader = currentActivity.findViewById(R.id.objectHeader);
        if (objectHeader != null) {
            // Use of getDataValue() avoids the knowledge of what data type the master property is.
            // This is a convenience for wizard generated code. Normally, developer will use the proxy class
            // get<Property>() method and add code to convert to string
            DataValue dataValue = myNotificationEntity.getDataValue(MyNotification.notificationNumber);
            if (dataValue != null) {
                objectHeader.setHeadline(dataValue.toString());
            } else {
                objectHeader.setHeadline(null);
            }
            // EntityKey in string format: '{"key":value,"key2":value2}'
            objectHeader.setSubheadline(EntityKeyUtil.getOptionalEntityKey(myNotificationEntity));
            objectHeader.setTag("#tag1", 0);
            objectHeader.setTag("#tag3", 2);
            objectHeader.setTag("#tag2", 1);

            objectHeader.setBody("You can set the header body text here.");
            objectHeader.setFootnote("You can set the header footnote here.");
            objectHeader.setDescription("You can add a detailed item description here.");

            setDetailImage(objectHeader, myNotificationEntity);
            objectHeader.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Set up databinding for this view
     *
     * @param inflater - layout inflater from onCreateView
     * @param container - view group from onCreateView
     * @return view - rootView from generated databinding code
     */
  /*  private View setupDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentMynotificationsetDetailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        binding.setHandler(this);
        return rootView;
    }*/

}
//odata section
      /*  public MyNotificationViewModel(Application application) {
            super(application, EntitySets.myNotificationSet, MyNotification.notificationNumber);
        }

        public MyNotificationViewModel(Application application, String navigationPropertyName, Parcelable entityData) {
            super(application, EntitySets.myNotificationSet, MyNotification.notificationNumber, navigationPropertyName, entityData);
        }*/
     /*   isMasterDetailView = getResources().getBoolean(R.bool.two_pane);
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
   // }

    /*   /**
     * Let the Navigate Up button work like Back button
     */
 /*   @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_IS_NAVIGATION_DISABLED, this.isNavigationDisabled);
        outState.putBoolean(KEY_IS_NAVIGATION_FROM_HOME, this.isConfirmDataLossFromHomeButton);
        super.onSaveInstanceState(outState);
    }

    /**
     * Handles backwards navigation when user presses back button.
     */
 /*   @Override
    public void onBackPressed() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        askUserBeforeNavigation();
    }

    /**
     * Every fragment handles its own OptionsMenu so the activity does not have to.
     *
     * @return false, because this activity does not handles OptionItems
     */
  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_home) {
            isConfirmDataLossFromHomeButton = true;
            askUserBeforeNavigation();
            return true;
        }
        return false;
    }


    @Override
    public void onFragmentStateChange(int eventId, @Nullable MyNotification entity) {
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
        //remove bracket for not using odata
    }

    private void onCreateNewItem() {
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

    /**
     * Handles the item click event from the list.
     *
     * @param entity The item clicked in the list
     */
 /*   private void onItemClicked(@Nullable MyNotification entity) {
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


        /**
         * Handles the situation after an entity is deleted, hide the progress bar, go back if neccessary.
         */
  /*      private void onDeleteComplete () {
            Toolbar secondaryToolbar = findViewById(R.id.secondaryToolbar);
            if (secondaryToolbar != null) {
                secondaryToolbar.setVisibility(View.INVISIBLE);
            }
            if (!isMasterDetailView) super.onBackPressed();
        }

        /**
         * Edits the <code>entity</code>.
         *
         * @param entity The entity to be edited.
         */
    /*    private void onEditItem (@Nullable MyNotification entity){
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

        /**
         * Shows a dialog when user wants to delete an entity.
         */
   /*     private void onConfirmDelete () {
            MyNotificationSetActivity.DeleteConfirmationDialogFragment dialog = new MyNotificationSetActivity.DeleteConfirmationDialogFragment();
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), UIConstants.CONFIRMATION_FRAGMENT_TAG);
        }

        /**
         * Shows an AlertDialog when user wants to leave {@link MyNotificationSetCreateFragment}.
         */
    /*    public void askUserBeforeNavigation () {
            boolean editFragmentPresent = getSupportFragmentManager().findFragmentByTag(UIConstants.MODIFY_FRAGMENT_TAG) != null
                    || getSupportFragmentManager().findFragmentByTag(UIConstants.CREATE_FRAGMENT_TAG) != null;
            if (editFragmentPresent && isNavigationDisabled) {
                MyNotificationSetActivity.ConfirmationDialogFragment dialog = new MyNotificationSetActivity.ConfirmationDialogFragment();
                dialog.setCancelable(false);
                dialog.show(getSupportFragmentManager(), UIConstants.CONFIRMATION_FRAGMENT_TAG);
            } else {
                onFragmentStateChange(UIConstants.EVENT_BACK_NAVIGATION_CONFIRMED, null);
            }
        }

        /**
         * Represents the confirmation dialog fragment when users tries to leave the create or edit
         * fragment to prevent data loss.
         */
   /*     public static class ConfirmationDialogFragment extends DialogFragment {
            public ConfirmationDialogFragment() {
                super();
            }

            @NonNull
            @Override
            public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
                builder.setTitle(R.string.before_navigation_dialog_title);
                builder.setMessage(R.string.before_navigation_dialog_message);
                builder.setPositiveButton(R.string.before_navigation_dialog_positive_button, (dialog, which) -> {
                    ((MyNotificationSetActivity) getActivity()).onFragmentStateChange(UIConstants.EVENT_BACK_NAVIGATION_CONFIRMED, null);
                });
                builder.setNegativeButton(R.string.before_navigation_dialog_negative_button, (dialog, which) -> {

                });
                return builder.create();
            }
        }

        /**
         * Represents the delete confirmation dialog fragment when users tries to delete an entity or entities
         */
    /*    public static class DeleteConfirmationDialogFragment extends DialogFragment {
            public DeleteConfirmationDialogFragment() {
                super();
            }

            @NonNull
            @Override
            public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
                MyNotificationViewModel viewModel = new ViewModelProvider(getActivity()).get(MyNotificationViewModel.class);
                if (viewModel.numberOfSelected() > 1) {
                    builder.setTitle(R.string.delete_dialog_title).setMessage(R.string.delete_more_items);
                } else {
                    builder.setTitle(R.string.delete_dialog_title).setMessage(R.string.delete_one_item);
                }

                builder.setPositiveButton(R.string.delete, (dialog, which) -> {
                    try {
                        View progressBar = getActivity().findViewById(R.id.indeterminateBar);
                        if (progressBar != null) {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        if (viewModel.numberOfSelected() == 0) {
                            viewModel.addSelected(viewModel.getSelectedEntity().getValue());
                        }
                        viewModel.deleteSelected();
                    } catch (Exception ex) {
                        new DialogHelper(getContext(), R.style.OnboardingDefaultTheme_Dialog_Alert)
                                .showOKOnlyDialog(
                                        getActivity().getSupportFragmentManager(),
                                        getResources().getString(R.string.delete_failed_detail),
                                        null, null, null);
                    }
                });

                builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                });
                return builder.create();
            }
        }*/

    //}




// }