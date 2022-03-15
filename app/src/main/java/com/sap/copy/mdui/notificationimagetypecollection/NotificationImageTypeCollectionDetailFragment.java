package com.sap.copy.mdui.notificationimagetypecollection;

import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.sap.copy.service.SAPServiceManager;
import com.sap.copy.R;
import com.sap.copy.databinding.FragmentNotificationimagetypecollectionDetailBinding;
import com.sap.copy.mdui.BundleKeys;
import com.sap.copy.mdui.InterfacedFragment;
import com.sap.copy.mdui.UIConstants;
import com.sap.copy.mdui.EntityKeyUtil;
import com.sap.copy.repository.OperationResult;
import com.sap.copy.viewmodel.notificationimagetype.NotificationImageTypeViewModel;
import com.sap.cloud.android.odata.eam_ntf_create_entities.EAM_NTF_CREATE_EntitiesMetadata.EntitySets;
import com.sap.cloud.android.odata.eam_ntf_create_entities.NotificationImageType;
import com.sap.cloud.mobile.fiori.object.ObjectHeader;
import com.sap.cloud.mobile.odata.DataValue;
import com.sap.copy.app.SAPWizardApplication;
import android.widget.ImageView;
import com.sap.copy.mediaresource.EntityMediaResource;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

/**
 * A fragment representing a single NotificationImageType detail screen.
 * This fragment is contained in an NotificationImageTypeCollectionActivity.
 */
public class NotificationImageTypeCollectionDetailFragment extends InterfacedFragment<NotificationImageType> {

    /** Generated data binding class based on layout file */
    private FragmentNotificationimagetypecollectionDetailBinding binding;

    /** NotificationImageType entity to be displayed */
    private NotificationImageType notificationImageTypeEntity = null;

    /** Fiori ObjectHeader component used when entity is to be displayed on phone */
    private ObjectHeader objectHeader;

    /** View model of the entity type that the displayed entity belongs to */
    private NotificationImageTypeViewModel viewModel;

    /**
     * Service manager to provide root URL of OData Service for Glide to load images if there are media resources
     * associated with the entity type
     */
    private SAPServiceManager sapServiceManager;

    /** Arguments: NotificationImageType for display */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menu = R.menu.itemlist_view_options;
        sapServiceManager = ((SAPWizardApplication) currentActivity.getApplication()).getSAPServiceManager();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return setupDataBinding(inflater, container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(currentActivity).get(NotificationImageTypeViewModel.class);
        viewModel.getDeleteResult().observe(getViewLifecycleOwner(), this::onDeleteComplete);
        viewModel.getSelectedEntity().observe(getViewLifecycleOwner(), entity -> {
            notificationImageTypeEntity = entity;
            binding.setNotificationImageType(entity);
            setupObjectHeader();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_item:
                listener.onFragmentStateChange(UIConstants.EVENT_EDIT_ITEM, notificationImageTypeEntity);
                return true;
            case R.id.delete_item:
                listener.onFragmentStateChange(UIConstants.EVENT_ASK_DELETE_CONFIRMATION,null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /** Completion callback for delete operation */
    private void onDeleteComplete(@NonNull OperationResult<NotificationImageType> result) {
        if( progressBar != null ) {
            progressBar.setVisibility(View.INVISIBLE);
        }
        viewModel.removeAllSelected(); //to make sure the 'action mode' not activated in the list
        Exception ex = result.getError();
        if (ex != null) {
            showError(getString(R.string.delete_failed_detail));
            return;
        }
        listener.onFragmentStateChange(UIConstants.EVENT_DELETION_COMPLETED, notificationImageTypeEntity);
    }

    /**
     * Set detail image of ObjectHeader.
     * When the entity does not provides picture, set the first character of the masterProperty.
     */
    private void setDetailImage(@NonNull ObjectHeader objectHeader, @NonNull NotificationImageType notificationImageTypeEntity) {
        if (EntityMediaResource.hasMediaResources(EntitySets.notificationImageTypeCollection)) {
            // Glide offers caching in addition to fetching the images
            objectHeader.prepareDetailImageView().setScaleType(ImageView.ScaleType.FIT_CENTER);
            Glide.with(currentActivity)
                    .load(EntityMediaResource.getMediaResourceUrl(notificationImageTypeEntity, sapServiceManager.getServiceRoot()))
                    .apply(new RequestOptions().fitCenter())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(objectHeader.getDetailImageView());
        } else if (notificationImageTypeEntity.getDataValue(NotificationImageType.maintenanceNotification) != null && !notificationImageTypeEntity.getDataValue(NotificationImageType.maintenanceNotification).toString().isEmpty()) {
            objectHeader.setDetailImageCharacter(notificationImageTypeEntity.getDataValue(NotificationImageType.maintenanceNotification).toString().substring(0, 1));
        } else {
            objectHeader.setDetailImageCharacter("?");
        }
    }

    /**
     * Setup ObjectHeader with an instance of NotificationImageType
     */
    private void setupObjectHeader() {
        Toolbar secondToolbar = currentActivity.findViewById(R.id.secondaryToolbar);
        if (secondToolbar != null) {
            secondToolbar.setTitle(notificationImageTypeEntity.getEntityType().getLocalName());
        } else {
            currentActivity.setTitle(notificationImageTypeEntity.getEntityType().getLocalName());
        }

        // Object Header is not available in tablet mode
        objectHeader = currentActivity.findViewById(R.id.objectHeader);
        if (objectHeader != null) {
            // Use of getDataValue() avoids the knowledge of what data type the master property is.
            // This is a convenience for wizard generated code. Normally, developer will use the proxy class
            // get<Property>() method and add code to convert to string
            DataValue dataValue = notificationImageTypeEntity.getDataValue(NotificationImageType.maintenanceNotification);
            if (dataValue != null) {
                objectHeader.setHeadline(dataValue.toString());
            } else {
                objectHeader.setHeadline(null);
            }
            // EntityKey in string format: '{"key":value,"key2":value2}'
            objectHeader.setSubheadline(EntityKeyUtil.getOptionalEntityKey(notificationImageTypeEntity));
            objectHeader.setTag("#tag1", 0);
            objectHeader.setTag("#tag3", 2);
            objectHeader.setTag("#tag2", 1);

            objectHeader.setBody("You can set the header body text here.");
            objectHeader.setFootnote("You can set the header footnote here.");
            objectHeader.setDescription("You can add a detailed item description here.");

            setDetailImage(objectHeader, notificationImageTypeEntity);
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
    private View setupDataBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentNotificationimagetypecollectionDetailBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        binding.setHandler(this);
        return rootView;
    }
}
