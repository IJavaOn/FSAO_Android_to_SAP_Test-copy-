package com.sap.copy.mdui;

import com.sap.copy.app.SAPWizardApplication;

import com.sap.cloud.mobile.flowv2.core.DialogHelper;
import com.sap.cloud.mobile.flowv2.core.Flow;
import com.sap.cloud.mobile.flowv2.core.FlowContext;
import com.sap.cloud.mobile.flowv2.core.FlowContextBuilder;
import com.sap.cloud.mobile.flowv2.core.FlowContextRegistry;
import com.sap.cloud.mobile.flowv2.model.FlowType;
import com.sap.cloud.mobile.flowv2.securestore.UserSecureStoreDelegate;
import android.os.Bundle;
import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kotlin.Unit;
import com.sap.copy.app.WelcomeActivity;
import com.sap.copy.mdui.contactset.ContactSetActivity;
import com.sap.copy.mdui.datemonitorset.DateMonitorSetActivity;
import com.sap.copy.mdui.longtextset.LongTextSetActivity;
import com.sap.copy.mdui.malfunctioneffectset.MalfunctionEffectSetActivity;
import com.sap.copy.mdui.mynotificationset.MyNotificationSetActivity;
import com.sap.copy.mdui.noteset.NoteSetActivity;
import com.sap.copy.mdui.notificationheaderset.NotificationHeaderSetActivity;
import com.sap.copy.mdui.notificationimagetypecollection.NotificationImageTypeCollectionActivity;
import com.sap.copy.mdui.notificationphaseset.NotificationPhaseSetActivity;
import com.sap.copy.mdui.notificationpriorityset.NotificationPrioritySetActivity;
import com.sap.copy.mdui.notificationtypechangeset.NotificationTypeChangeSetActivity;
import com.sap.copy.mdui.notificationtypeset.NotificationTypeSetActivity;
import com.sap.copy.mdui.pmuserdetailsset.PMUserDetailsSetActivity;
import com.sap.copy.mdui.plantsectionvhset.PlantSectionVHSetActivity;
import com.sap.copy.mdui.tofacetfilteritemset.TOFacetFilterItemSetActivity;
import com.sap.copy.mdui.tofacetfilterset.TOFacetFilterSetActivity;
import com.sap.copy.mdui.technicalobjectset.TechnicalObjectSetActivity;
import com.sap.copy.mdui.technicalobjectthumbnailset.TechnicalObjectThumbnailSetActivity;
import com.sap.copy.mdui.texttemplateset.TextTemplateSetActivity;
import com.sap.cloud.mobile.fiori.object.ObjectCell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sap.copy.R;

/*
 * An activity to display the list of all entity types from the OData service
 */
public class EntitySetListActivity extends AppCompatActivity {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntitySetListActivity.class);
    private static final int BLUE_ANDROID_ICON = R.drawable.ic_android_blue;
    private static final int WHITE_ANDROID_ICON = R.drawable.ic_android_white;

    public enum EntitySetName {
        ContactSet("ContactSet", R.string.eset_contactset,BLUE_ANDROID_ICON),
        DateMonitorSet("DateMonitorSet", R.string.eset_datemonitorset,WHITE_ANDROID_ICON),
        LongTextSet("LongTextSet", R.string.eset_longtextset,BLUE_ANDROID_ICON),
        MalfunctionEffectSet("MalfunctionEffectSet", R.string.eset_malfunctioneffectset,WHITE_ANDROID_ICON),
        MyNotificationSet("MyNotificationSet", R.string.eset_mynotificationset,BLUE_ANDROID_ICON),
        NoteSet("NoteSet", R.string.eset_noteset,WHITE_ANDROID_ICON),
        NotificationHeaderSet("NotificationHeaderSet", R.string.eset_notificationheaderset,BLUE_ANDROID_ICON),
        NotificationImageTypeCollection("NotificationImageTypeCollection", R.string.eset_notificationimagetypecollection,WHITE_ANDROID_ICON),
        NotificationPhaseSet("NotificationPhaseSet", R.string.eset_notificationphaseset,BLUE_ANDROID_ICON),
        NotificationPrioritySet("NotificationPrioritySet", R.string.eset_notificationpriorityset,WHITE_ANDROID_ICON),
        NotificationTypeChangeSet("NotificationTypeChangeSet", R.string.eset_notificationtypechangeset,BLUE_ANDROID_ICON),
        NotificationTypeSet("NotificationTypeSet", R.string.eset_notificationtypeset,WHITE_ANDROID_ICON),
        PMUserDetailsSet("PMUserDetailsSet", R.string.eset_pmuserdetailsset,BLUE_ANDROID_ICON),
        PlantSectionVHSet("PlantSectionVHSet", R.string.eset_plantsectionvhset,WHITE_ANDROID_ICON),
        TOFacetFilterItemSet("TOFacetFilterItemSet", R.string.eset_tofacetfilteritemset,BLUE_ANDROID_ICON),
        TOFacetFilterSet("TOFacetFilterSet", R.string.eset_tofacetfilterset,WHITE_ANDROID_ICON),
        TechnicalObjectSet("TechnicalObjectSet", R.string.eset_technicalobjectset,BLUE_ANDROID_ICON),
        TechnicalObjectThumbnailSet("TechnicalObjectThumbnailSet", R.string.eset_technicalobjectthumbnailset,WHITE_ANDROID_ICON),
        TextTemplateSet("TextTemplateSet", R.string.eset_texttemplateset,BLUE_ANDROID_ICON);

        private final int titleId;
        private final int iconId;
        private final String entitySetName;

        EntitySetName(String name, int titleId, int iconId) {
            this.entitySetName = name;
            this.titleId = titleId;
            this.iconId = iconId;
        }

        public int getTitleId() {
                return this.titleId;
        }

        public String getEntitySetName() {
                return this.entitySetName;
        }
    }

    private final List<String> entitySetNames = new ArrayList<>();
    private final Map<String, EntitySetName> entitySetNameMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_entity_set_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        entitySetNames.clear();
        entitySetNameMap.clear();
        for (EntitySetName entitySet : EntitySetName.values()) {
            String entitySetTitle = getResources().getString(entitySet.getTitleId());
            entitySetNames.add(entitySetTitle);
            entitySetNameMap.put(entitySetTitle, entitySet);
        }

        final ListView listView = findViewById(R.id.entity_list);
        final EntitySetListAdapter adapter = new EntitySetListAdapter(this, R.layout.element_entity_set_list, entitySetNames);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            EntitySetName entitySetName = entitySetNameMap.get(adapter.getItem(position));
            Context context = EntitySetListActivity.this;
            Intent intent;
            switch (entitySetName) {
                case ContactSet:
                    intent = new Intent(context, ContactSetActivity.class);
                    break;
                case DateMonitorSet:
                    intent = new Intent(context, DateMonitorSetActivity.class);
                    break;
                case LongTextSet:
                    intent = new Intent(context, LongTextSetActivity.class);
                    break;
                case MalfunctionEffectSet:
                    intent = new Intent(context, MalfunctionEffectSetActivity.class);
                    break;
                case MyNotificationSet:
                    intent = new Intent(context, MyNotificationSetActivity.class);
                    break;
                case NoteSet:
                    intent = new Intent(context, NoteSetActivity.class);
                    break;
                case NotificationHeaderSet:
                    intent = new Intent(context, NotificationHeaderSetActivity.class);
                    break;
                case NotificationImageTypeCollection:
                    intent = new Intent(context, NotificationImageTypeCollectionActivity.class);
                    break;
                case NotificationPhaseSet:
                    intent = new Intent(context, NotificationPhaseSetActivity.class);
                    break;
                case NotificationPrioritySet:
                    intent = new Intent(context, NotificationPrioritySetActivity.class);
                    break;
                case NotificationTypeChangeSet:
                    intent = new Intent(context, NotificationTypeChangeSetActivity.class);
                    break;
                case NotificationTypeSet:
                    intent = new Intent(context, NotificationTypeSetActivity.class);
                    break;
                case PMUserDetailsSet:
                    intent = new Intent(context, PMUserDetailsSetActivity.class);
                    break;
                case PlantSectionVHSet:
                    intent = new Intent(context, PlantSectionVHSetActivity.class);
                    break;
                case TOFacetFilterItemSet:
                    intent = new Intent(context, TOFacetFilterItemSetActivity.class);
                    break;
                case TOFacetFilterSet:
                    intent = new Intent(context, TOFacetFilterSetActivity.class);
                    break;
                case TechnicalObjectSet:
                    intent = new Intent(context, TechnicalObjectSetActivity.class);
                    break;
                case TechnicalObjectThumbnailSet:
                    intent = new Intent(context, TechnicalObjectThumbnailSetActivity.class);
                    break;
                case TextTemplateSet:
                    intent = new Intent(context, TextTemplateSetActivity.class);
                    break;
                    default:
                        return;
            }
            context.startActivity(intent);
        });
    }

    public class EntitySetListAdapter extends ArrayAdapter<String> {

        EntitySetListAdapter(@NonNull Context context, int resource, List<String> entitySetNames) {
            super(context, resource, entitySetNames);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            EntitySetName entitySetName = entitySetNameMap.get(getItem(position));
            if (convertView == null) {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.element_entity_set_list, parent, false);
            }
            String headLineName = getResources().getString(entitySetName.titleId);
            ObjectCell entitySetCell = convertView.findViewById(R.id.entity_set_name);
            entitySetCell.setHeadline(headLineName);
            entitySetCell.setDetailImage(entitySetName.iconId);
            return convertView;
        }
    }
                
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entity_set_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_delete_registration).setEnabled(
                UserSecureStoreDelegate.getInstance().getRuntimeMultipleUserModeAsync() != null
                        && UserSecureStoreDelegate.getInstance().getRuntimeMultipleUserModeAsync());
        menu.findItem(R.id.menu_delete_registration).setVisible(
                UserSecureStoreDelegate.getInstance().getRuntimeMultipleUserModeAsync() != null
                        && UserSecureStoreDelegate.getInstance().getRuntimeMultipleUserModeAsync());
        return super.onPrepareOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LOGGER.debug("onOptionsItemSelected: " + item.getTitle());
        switch (item.getItemId()) {
            case R.id.menu_settings:
                LOGGER.debug("settings screen menu item selected.");
                this.startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.menu_logout:
                FlowContext flowContext_logout = new FlowContextBuilder(FlowContextRegistry.getFlowContext())
                        .setFlowType(FlowType.LOGOUT)
                        .build();
                Flow.start(this, flowContext_logout, (requestCode, resultCode, data) -> {
                    if (resultCode == RESULT_OK) {

                        Intent intent = new Intent(this, WelcomeActivity.class);
                        intent.addFlags(
                                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    return null;
                });
                return true;

            case R.id.menu_delete_registration:
                DialogFragment dialogFragment = new DialogHelper.ErrorDialogFragment(
                        getString(R.string.delete_registration_warning),
                        R.style.Flows_Dialog,
                        getString(R.string.dialog_warn_title),
                        getString(R.string.confirm_yes),
                        getString(R.string.cancel),
                        () -> {
                            FlowContext flowContext_del_reg = new FlowContextBuilder(FlowContextRegistry.getFlowContext())
                                    .setFlowType(FlowType.DEL_REGISTRATION)
                                    .build();
                            Flow.start(this, flowContext_del_reg, (requestCode, resultCode, data) -> {
                                if (resultCode == RESULT_OK) {
                                    Intent intent = new Intent(this, WelcomeActivity.class);
                                    intent.addFlags(
                                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                return null;
                            });
                            return Unit.INSTANCE;
                        },
                        null
                );
                dialogFragment.setCancelable(false);
                dialogFragment.show(getSupportFragmentManager(), getString(R.string.delete_registration));
                return true;

            default:
                return false;
        }
    }

}
