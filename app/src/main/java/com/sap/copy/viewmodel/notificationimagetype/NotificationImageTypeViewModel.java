package com.sap.copy.viewmodel.notificationimagetype;

import android.app.Application;
import android.os.Parcelable;

import com.sap.copy.viewmodel.EntityViewModel;
import com.sap.cloud.android.odata.eam_ntf_create_entities.NotificationImageType;
import com.sap.cloud.android.odata.eam_ntf_create_entities.EAM_NTF_CREATE_EntitiesMetadata.EntitySets;

/*
 * Represents View model for NotificationImageType
 * Having an entity view model for each <T> allows the ViewModelProvider to cache and
 * return the view model of that type. This is because the ViewModelStore of
 * ViewModelProvider cannot not be able to tell the difference between EntityViewModel<type1>
 * and EntityViewModel<type2>.
 */
public class NotificationImageTypeViewModel extends EntityViewModel<NotificationImageType> {

    /**
    * Default constructor for a specific view model.
    * @param application - parent application
    */
    public NotificationImageTypeViewModel(Application application) {
        super(application, EntitySets.notificationImageTypeCollection, NotificationImageType.maintenanceNotification);
    }

    /**
    * Constructor for a specific view model with navigation data.
    * @param application - parent application
    * @param navigationPropertyName - name of the navigation property
    * @param entityData - parent entity (starting point of the navigation)
    */
	 public NotificationImageTypeViewModel(Application application, String navigationPropertyName, Parcelable entityData) {
        super(application, EntitySets.notificationImageTypeCollection, NotificationImageType.maintenanceNotification, navigationPropertyName, entityData);
    }
}
