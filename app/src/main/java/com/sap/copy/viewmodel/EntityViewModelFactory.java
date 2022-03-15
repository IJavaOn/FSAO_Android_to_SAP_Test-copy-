package com.sap.copy.viewmodel;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import android.os.Parcelable;

import com.sap.copy.viewmodel.contact.ContactViewModel;
import com.sap.copy.viewmodel.datemonitor.DateMonitorViewModel;
import com.sap.copy.viewmodel.longtext.LongTextViewModel;
import com.sap.copy.viewmodel.malfunctioneffect.MalfunctionEffectViewModel;
import com.sap.copy.viewmodel.mynotification.MyNotificationViewModel;
import com.sap.copy.viewmodel.note.NoteViewModel;
import com.sap.copy.viewmodel.notificationheader.NotificationHeaderViewModel;
import com.sap.copy.viewmodel.notificationimagetype.NotificationImageTypeViewModel;
import com.sap.copy.viewmodel.notificationphase.NotificationPhaseViewModel;
import com.sap.copy.viewmodel.notificationpriority.NotificationPriorityViewModel;
import com.sap.copy.viewmodel.notificationtypechange.NotificationTypeChangeViewModel;
import com.sap.copy.viewmodel.notificationtype.NotificationTypeViewModel;
import com.sap.copy.viewmodel.pmuserdetails.PMUserDetailsViewModel;
import com.sap.copy.viewmodel.plantsectionvh.PlantSectionVHViewModel;
import com.sap.copy.viewmodel.tofacetfilteritem.TOFacetFilterItemViewModel;
import com.sap.copy.viewmodel.tofacetfilter.TOFacetFilterViewModel;
import com.sap.copy.viewmodel.technicalobject.TechnicalObjectViewModel;
import com.sap.copy.viewmodel.technicalobjectthumbnail.TechnicalObjectThumbnailViewModel;
import com.sap.copy.viewmodel.texttemplate.TextTemplateViewModel;


/**
 * Custom factory class, which can create view models for entity subsets, which are
 * reached from a parent entity through a navigation property.
 */
public class EntityViewModelFactory implements ViewModelProvider.Factory {

	// application class
    private Application application;
	// name of the navigation property
    private String navigationPropertyName;
	// parent entity
    private Parcelable entityData;

	/**
	 * Creates a factory class for entity view models created following a navigation link.
	 *
	 * @param application parent application
	 * @param navigationPropertyName name of the navigation link
	 * @param entityData parent entity
	 */
    public EntityViewModelFactory(Application application, String navigationPropertyName, Parcelable entityData) {
        this.application = application;
        this.navigationPropertyName = navigationPropertyName;
        this.entityData = entityData;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        T retValue = null;
		switch(modelClass.getSimpleName()) {



			case "ContactViewModel":
				retValue = (T) new ContactViewModel(application, navigationPropertyName, entityData);
				break;
			case "DateMonitorViewModel":
				retValue = (T) new DateMonitorViewModel(application, navigationPropertyName, entityData);
				break;
			case "LongTextViewModel":
				retValue = (T) new LongTextViewModel(application, navigationPropertyName, entityData);
				break;
			case "MalfunctionEffectViewModel":
				retValue = (T) new MalfunctionEffectViewModel(application, navigationPropertyName, entityData);
				break;
			case "MyNotificationViewModel":
				retValue = (T) new MyNotificationViewModel(application, navigationPropertyName, entityData);
				break;
			case "NoteViewModel":
				retValue = (T) new NoteViewModel(application, navigationPropertyName, entityData);
				break;
			case "NotificationHeaderViewModel":
				retValue = (T) new NotificationHeaderViewModel(application, navigationPropertyName, entityData);
				break;
			case "NotificationImageTypeViewModel":
				retValue = (T) new NotificationImageTypeViewModel(application, navigationPropertyName, entityData);
				break;
			case "NotificationPhaseViewModel":
				retValue = (T) new NotificationPhaseViewModel(application, navigationPropertyName, entityData);
				break;
			case "NotificationPriorityViewModel":
				retValue = (T) new NotificationPriorityViewModel(application, navigationPropertyName, entityData);
				break;
			case "NotificationTypeChangeViewModel":
				retValue = (T) new NotificationTypeChangeViewModel(application, navigationPropertyName, entityData);
				break;
			case "NotificationTypeViewModel":
				retValue = (T) new NotificationTypeViewModel(application, navigationPropertyName, entityData);
				break;
			case "PMUserDetailsViewModel":
				retValue = (T) new PMUserDetailsViewModel(application, navigationPropertyName, entityData);
				break;
			case "PlantSectionVHViewModel":
				retValue = (T) new PlantSectionVHViewModel(application, navigationPropertyName, entityData);
				break;
			case "TOFacetFilterItemViewModel":
				retValue = (T) new TOFacetFilterItemViewModel(application, navigationPropertyName, entityData);
				break;
			case "TOFacetFilterViewModel":
				retValue = (T) new TOFacetFilterViewModel(application, navigationPropertyName, entityData);
				break;
			case "TechnicalObjectViewModel":
				retValue = (T) new TechnicalObjectViewModel(application, navigationPropertyName, entityData);
				break;
			case "TechnicalObjectThumbnailViewModel":
				retValue = (T) new TechnicalObjectThumbnailViewModel(application, navigationPropertyName, entityData);
				break;
			case "TextTemplateViewModel":
				retValue = (T) new TextTemplateViewModel(application, navigationPropertyName, entityData);
				break;
		}
		return retValue;
	}
}