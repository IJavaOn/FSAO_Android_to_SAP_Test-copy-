package com.sap.copy.mdui.notificationimagetypecollection;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.sap.copy.R;
import com.sap.copy.databinding.FragmentNotificationimagetypecollectionCreateBinding;
import com.sap.copy.mdui.BundleKeys;
import com.sap.copy.mdui.InterfacedFragment;
import com.sap.copy.mdui.UIConstants;
import com.sap.copy.repository.OperationResult;
import com.sap.copy.viewmodel.notificationimagetype.NotificationImageTypeViewModel;
import com.sap.cloud.mobile.fiori.object.ObjectHeader;
import com.sap.cloud.android.odata.eam_ntf_create_entities.NotificationImageType;
import com.sap.cloud.android.odata.eam_ntf_create_entities.EAM_NTF_CREATE_EntitiesMetadata.EntityTypes;
import com.sap.cloud.android.odata.eam_ntf_create_entities.EAM_NTF_CREATE_EntitiesMetadata.EntitySets;
import com.sap.cloud.mobile.fiori.formcell.SimplePropertyFormCell;
import com.sap.cloud.mobile.odata.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.cloud.mobile.odata.ByteStream;
import com.sap.cloud.mobile.odata.StreamBase;
import java.io.InputStream;
import com.sap.cloud.android.odata.eam_ntf_create_entities.EAM_NTF_CREATE_EntitiesMetadata.EntitySets;
/**
 * A fragment that presents a screen to either create or update an existing NotificationImageType entity.
 * This fragment is contained in the {@link NotificationImageTypeCollectionActivity}.
 */
public class NotificationImageTypeCollectionCreateFragment extends InterfacedFragment<NotificationImageType> {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationImageTypeCollectionCreateFragment.class);
    //The key for the saved instance of the working entity for device configuration change
    private static final String KEY_WORKING_COPY = "WORKING_COPY";

    /** NotificationImageType object and it's copy: the modifications are done on the copied object. */
    private NotificationImageType notificationImageTypeEntity;
    private NotificationImageType notificationImageTypeEntityCopy;

    /** DataBinding generated class */
    private FragmentNotificationimagetypecollectionCreateBinding binding;

    /** Indicate what operation to be performed */
    private String operation;

    /** NotificationImageType ViewModel */
    private NotificationImageTypeViewModel viewModel;

    /** The update menu item */
    private MenuItem updateMenuItem;

    /**
     * This fragment is used for both update and create for NotificationImageTypeCollection to enter values for the properties.
     * When used for update, an instance of the entity is required. In the case of create, a new instance
     * of the entity with defaults will be created. The default values may not be acceptable for the
     * OData service.
     * Arguments: Operation: [OP_CREATE | OP_UPDATE]
     *            NotificationImageType if Operation is update
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menu = R.menu.itemlist_edit_options;
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        operation = bundle.getString(BundleKeys.OPERATION);
        if (UIConstants.OP_CREATE.equals(operation)) {
            activityTitle = currentActivity.getResources().getString(R.string.title_create_fragment, EntityTypes.notificationImageType.getLocalName());
        } else {
            activityTitle = currentActivity.getResources().getString(R.string.title_update_fragment) + " " + EntityTypes.notificationImageType.getLocalName();
        }

        ((NotificationImageTypeCollectionActivity)currentActivity).isNavigationDisabled = true;
        viewModel = new ViewModelProvider(currentActivity).get(NotificationImageTypeViewModel.class);
        viewModel.getCreateResult().observe(this, result -> onComplete(result));
        viewModel.getUpdateResult().observe(this, result -> onComplete(result));

        if(UIConstants.OP_CREATE.equals(operation)) {
            notificationImageTypeEntity = createNotificationImageType();
        } else {
            notificationImageTypeEntity = viewModel.getSelectedEntity().getValue();
        }

        NotificationImageType workingCopy = null;
        if( savedInstanceState != null ) {
            workingCopy =  (NotificationImageType)savedInstanceState.getParcelable(KEY_WORKING_COPY);
        }
        if( workingCopy == null ) {
            notificationImageTypeEntityCopy = (NotificationImageType) notificationImageTypeEntity.copy();
            notificationImageTypeEntityCopy.setEntityTag(notificationImageTypeEntity.getEntityTag());
            notificationImageTypeEntityCopy.setOldEntity(notificationImageTypeEntity);
            notificationImageTypeEntityCopy.setEditLink((notificationImageTypeEntity.getEditLink()));
        } else {
            //in this case, the old entity and entity tag should already been set.
            notificationImageTypeEntityCopy = workingCopy;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ObjectHeader objectHeader = currentActivity.findViewById(R.id.objectHeader);
        if( objectHeader != null ) objectHeader.setVisibility(View.GONE);
        return setupDataBinding(inflater, container);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(KEY_WORKING_COPY, notificationImageTypeEntityCopy);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(secondaryToolbar != null) {
            secondaryToolbar.setTitle(activityTitle);
        } else {
            getActivity().setTitle(activityTitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item:
                updateMenuItem = item;
                enableUpdateMenuItem(false);
                return onSaveItem();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** 
     * Enables or disables the update menu item base on the given 'enable'
     * @param enable true to enable the menu item, false otherwise
     */
    private void enableUpdateMenuItem(boolean enable) {
        updateMenuItem.setEnabled(enable);
        updateMenuItem.getIcon().setAlpha( enable ? 255 : 130);
    }

    /**
     * Saves the entity
     */
    private boolean onSaveItem() {
        if (!isNotificationImageTypeValid()) {
            return false;
        }
        //set 'isNavigationDisabled' false here to make sure the logic in list is ok, and set it to true if update fails.
        ((NotificationImageTypeCollectionActivity)currentActivity).isNavigationDisabled = false;
        if( progressBar != null ) progressBar.setVisibility(View.VISIBLE);
        if (operation.equals(UIConstants.OP_CREATE)) {
            if (EntitySets.notificationImageTypeCollection.getEntityType().isMedia()) {
                StreamBase media = getDefaultMediaResource();
                viewModel.create(notificationImageTypeEntityCopy, media);
            } else {
                viewModel.create(notificationImageTypeEntityCopy);
            }
        } else {
            viewModel.update(notificationImageTypeEntityCopy);
        }
        return true;
    }

    /**
     * Create a new NotificationImageType instance and initialize properties to its default values
     * Nullable property will remain null
     * @return new NotificationImageType instance
     */
    private NotificationImageType createNotificationImageType() {
        NotificationImageType notificationImageTypeEntity = new NotificationImageType(true);
        return notificationImageTypeEntity;
    }

    /*
     * Get a default media resource when creating a media linked entity
     * Since it is a package resource, exception when converting to byte array is not expected and not handled
     */
    private StreamBase getDefaultMediaResource() {
        InputStream inputStream = getResources().openRawResource(R.raw.blank);
        ByteStream byteStream = ByteStream.fromInput(inputStream);
        byteStream.setMediaType("image/png");
        return byteStream;
    }

    /** Callback function to complete processing when updateResult or createResult events fired */
    private void onComplete(@NonNull OperationResult<NotificationImageType> result) {
        if( progressBar != null ) progressBar.setVisibility(View.INVISIBLE);
        enableUpdateMenuItem(true);
        if (result.getError() != null) {
            ((NotificationImageTypeCollectionActivity)currentActivity).isNavigationDisabled = true;
            handleError(result);
        } else {
            boolean isMasterDetail = currentActivity.getResources().getBoolean(R.bool.two_pane);
            if( UIConstants.OP_UPDATE.equals(operation) && !isMasterDetail) {
                viewModel.setSelectedEntity(notificationImageTypeEntityCopy);
            }
            currentActivity.onBackPressed();
        }
    }

    /** Simple validation: checks the presence of mandatory fields. */
    private boolean isValidProperty(@NonNull Property property, @NonNull String value) {
        boolean isValid = true;
        if (!property.isNullable() && value.isEmpty()) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * Set up data binding for this view
     * @param inflater - layout inflater from onCreateView
     * @param container - view group from onCreateView
     * @return view - rootView from generated data binding code
     */
    private View setupDataBinding(@NonNull LayoutInflater inflater, ViewGroup container) {
        binding = FragmentNotificationimagetypecollectionCreateBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        binding.setNotificationImageType(notificationImageTypeEntityCopy);
        return rootView;
    }

    /** Validate the edited inputs */
    private boolean isNotificationImageTypeValid() {
        LinearLayout linearLayout = getView().findViewById(R.id.create_update_notificationimagetype);
        boolean isValid = true;
        // validate properties i.e. check non-nullable properties are truly non-null
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View viewItem = linearLayout.getChildAt(i);
            SimplePropertyFormCell simplePropertyFormCell = (SimplePropertyFormCell)viewItem;
            String propertyName = (String) simplePropertyFormCell.getTag();
            Property property = EntityTypes.notificationImageType.getProperty(propertyName);
            String value = simplePropertyFormCell.getValue().toString();
            if (!isValidProperty(property, value)) {
                simplePropertyFormCell.setTag(R.id.TAG_HAS_MANDATORY_ERROR, true);
                String errorMessage = getResources().getString(R.string.mandatory_warning);
                simplePropertyFormCell.setErrorEnabled(true);
                simplePropertyFormCell.setError(errorMessage);
                isValid = false;
            }
            else {
                if (simplePropertyFormCell.isErrorEnabled()){
                    boolean hasMandatoryError = (Boolean)simplePropertyFormCell.getTag(R.id.TAG_HAS_MANDATORY_ERROR);
                    if (!hasMandatoryError) {
                        isValid = false;
                    } else {
                        simplePropertyFormCell.setErrorEnabled(false);
                    }
                }
                simplePropertyFormCell.setTag(R.id.TAG_HAS_MANDATORY_ERROR, false);
            }
        }
        return isValid;
    }

    /**
     * Notify user of error encountered while execution the operation
     * @param result - operation result with error
     */
    private void handleError(@NonNull OperationResult<NotificationImageType> result) {
        String errorMessage;
        switch (result.getOperation()) {
            case UPDATE:
                errorMessage = getResources().getString(R.string.update_failed_detail);
                break;
            case CREATE:
                errorMessage = getResources().getString(R.string.create_failed_detail);
                break;
            default:
                throw new AssertionError();
        }
        showError(errorMessage);
    }
}
