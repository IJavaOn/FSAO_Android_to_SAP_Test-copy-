package com.sap.copy.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.sap.copy.service.SAPServiceManager;

import com.sap.cloud.android.odata.eam_ntf_create_entities.EAM_NTF_CREATE_Entities;
import com.sap.cloud.android.odata.eam_ntf_create_entities.EAM_NTF_CREATE_EntitiesMetadata.EntitySets;

import com.sap.cloud.android.odata.eam_ntf_create_entities.Contact;
import com.sap.cloud.android.odata.eam_ntf_create_entities.DateMonitor;
import com.sap.cloud.android.odata.eam_ntf_create_entities.LongText;
import com.sap.cloud.android.odata.eam_ntf_create_entities.MalfunctionEffect;
import com.sap.cloud.android.odata.eam_ntf_create_entities.MyNotification;
import com.sap.cloud.android.odata.eam_ntf_create_entities.Note;
import com.sap.cloud.android.odata.eam_ntf_create_entities.NotificationHeader;
import com.sap.cloud.android.odata.eam_ntf_create_entities.NotificationImageType;
import com.sap.cloud.android.odata.eam_ntf_create_entities.NotificationPhase;
import com.sap.cloud.android.odata.eam_ntf_create_entities.NotificationPriority;
import com.sap.cloud.android.odata.eam_ntf_create_entities.NotificationTypeChange;
import com.sap.cloud.android.odata.eam_ntf_create_entities.NotificationType;
import com.sap.cloud.android.odata.eam_ntf_create_entities.PMUserDetails;
import com.sap.cloud.android.odata.eam_ntf_create_entities.PlantSectionVH;
import com.sap.cloud.android.odata.eam_ntf_create_entities.TOFacetFilterItem;
import com.sap.cloud.android.odata.eam_ntf_create_entities.TOFacetFilter;
import com.sap.cloud.android.odata.eam_ntf_create_entities.TechnicalObject;
import com.sap.cloud.android.odata.eam_ntf_create_entities.TechnicalObjectThumbnail;
import com.sap.cloud.android.odata.eam_ntf_create_entities.TextTemplate;

import com.sap.cloud.mobile.odata.EntitySet;
import com.sap.cloud.mobile.odata.Property;

import java.util.WeakHashMap;

/*
 * Repository factory to construct repository for an entity set
 */
public class RepositoryFactory {

    /*
     * Cache all repositories created to avoid reconstruction and keeping the entities of entity set
     * maintained by each repository in memory. Use a weak hash map to allow recovery in low memory
     * conditions
     */
    private WeakHashMap<String, Repository> repositories;
    /*
     * Service manager to interact with OData service
     */
    private SAPServiceManager sapServiceManager;
    /**
     * Construct a RepositoryFactory instance. There should only be one repository factory and used
     * throughout the life of the application to avoid caching entities multiple times.
     * @param sapServiceManager - Service manager for interaction with OData service
     */
    public RepositoryFactory(SAPServiceManager sapServiceManager) {
        repositories = new WeakHashMap<>();
        this.sapServiceManager = sapServiceManager;
    }

    /**
     * Construct or return an existing repository for the specified entity set
     * @param entitySet - entity set for which the repository is to be returned
     * @param orderByProperty - if specified, collection will be sorted ascending with this property
     * @return a repository for the entity set
     */
    public Repository getRepository(@NonNull EntitySet entitySet, @Nullable Property orderByProperty) {
        EAM_NTF_CREATE_Entities eAM_NTF_CREATE_Entities = sapServiceManager.getEAM_NTF_CREATE_Entities();
        String key = entitySet.getLocalName();
        Repository repository = repositories.get(key);
        if (repository == null) {
            if (key.equals(EntitySets.contactSet.getLocalName())) {
                repository = new Repository<Contact>(eAM_NTF_CREATE_Entities, EntitySets.contactSet, orderByProperty);
            } else if (key.equals(EntitySets.dateMonitorSet.getLocalName())) {
                repository = new Repository<DateMonitor>(eAM_NTF_CREATE_Entities, EntitySets.dateMonitorSet, orderByProperty);
            } else if (key.equals(EntitySets.longTextSet.getLocalName())) {
                repository = new Repository<LongText>(eAM_NTF_CREATE_Entities, EntitySets.longTextSet, orderByProperty);
            } else if (key.equals(EntitySets.malfunctionEffectSet.getLocalName())) {
                repository = new Repository<MalfunctionEffect>(eAM_NTF_CREATE_Entities, EntitySets.malfunctionEffectSet, orderByProperty);
            } else if (key.equals(EntitySets.myNotificationSet.getLocalName())) {
                repository = new Repository<MyNotification>(eAM_NTF_CREATE_Entities, EntitySets.myNotificationSet, orderByProperty);
            } else if (key.equals(EntitySets.noteSet.getLocalName())) {
                repository = new Repository<Note>(eAM_NTF_CREATE_Entities, EntitySets.noteSet, orderByProperty);
            } else if (key.equals(EntitySets.notificationHeaderSet.getLocalName())) {
                repository = new Repository<NotificationHeader>(eAM_NTF_CREATE_Entities, EntitySets.notificationHeaderSet, orderByProperty);
            } else if (key.equals(EntitySets.notificationImageTypeCollection.getLocalName())) {
                repository = new Repository<NotificationImageType>(eAM_NTF_CREATE_Entities, EntitySets.notificationImageTypeCollection, orderByProperty);
            } else if (key.equals(EntitySets.notificationPhaseSet.getLocalName())) {
                repository = new Repository<NotificationPhase>(eAM_NTF_CREATE_Entities, EntitySets.notificationPhaseSet, orderByProperty);
            } else if (key.equals(EntitySets.notificationPrioritySet.getLocalName())) {
                repository = new Repository<NotificationPriority>(eAM_NTF_CREATE_Entities, EntitySets.notificationPrioritySet, orderByProperty);
            } else if (key.equals(EntitySets.notificationTypeChangeSet.getLocalName())) {
                repository = new Repository<NotificationTypeChange>(eAM_NTF_CREATE_Entities, EntitySets.notificationTypeChangeSet, orderByProperty);
            } else if (key.equals(EntitySets.notificationTypeSet.getLocalName())) {
                repository = new Repository<NotificationType>(eAM_NTF_CREATE_Entities, EntitySets.notificationTypeSet, orderByProperty);
            } else if (key.equals(EntitySets.pmUserDetailsSet.getLocalName())) {
                repository = new Repository<PMUserDetails>(eAM_NTF_CREATE_Entities, EntitySets.pmUserDetailsSet, orderByProperty);
            } else if (key.equals(EntitySets.plantSectionVHSet.getLocalName())) {
                repository = new Repository<PlantSectionVH>(eAM_NTF_CREATE_Entities, EntitySets.plantSectionVHSet, orderByProperty);
            } else if (key.equals(EntitySets.toFacetFilterItemSet.getLocalName())) {
                repository = new Repository<TOFacetFilterItem>(eAM_NTF_CREATE_Entities, EntitySets.toFacetFilterItemSet, orderByProperty);
            } else if (key.equals(EntitySets.toFacetFilterSet.getLocalName())) {
                repository = new Repository<TOFacetFilter>(eAM_NTF_CREATE_Entities, EntitySets.toFacetFilterSet, orderByProperty);
            } else if (key.equals(EntitySets.technicalObjectSet.getLocalName())) {
                repository = new Repository<TechnicalObject>(eAM_NTF_CREATE_Entities, EntitySets.technicalObjectSet, orderByProperty);
            } else if (key.equals(EntitySets.technicalObjectThumbnailSet.getLocalName())) {
                repository = new Repository<TechnicalObjectThumbnail>(eAM_NTF_CREATE_Entities, EntitySets.technicalObjectThumbnailSet, orderByProperty);
            } else if (key.equals(EntitySets.textTemplateSet.getLocalName())) {
                repository = new Repository<TextTemplate>(eAM_NTF_CREATE_Entities, EntitySets.textTemplateSet, orderByProperty);
            } else {
                throw new AssertionError("Fatal error, entity set[" + key + "] missing in generated code");
            }
            repositories.put(key, repository);
        }
        return repository;
    }

    /**
     * Get rid of all cached repositories
     */
    public void reset() {
        repositories.clear();
    }
 }
