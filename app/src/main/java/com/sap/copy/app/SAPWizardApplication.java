package com.sap.copy.app;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;
import com.sap.cloud.mobile.foundation.model.AppConfig;
import com.sap.copy.service.SAPServiceManager;
import com.sap.copy.repository.RepositoryFactory;
import com.sap.cloud.mobile.foundation.mobileservices.MobileService;
import com.sap.cloud.mobile.foundation.mobileservices.SDKInitializer;
import java.util.ArrayList;
import java.util.List;
import com.sap.cloud.mobile.foundation.logging.LoggingService;
import com.sap.cloud.mobile.foundation.settings.policies.LogPolicy;

public class SAPWizardApplication extends Application {

    public boolean isApplicationUnlocked = false;
    public static SharedPreferences sp;

    public static final String KEY_LOG_SETTING_PREFERENCE = "key.log.settings.preference";

    /**
     * Manages and provides access to OData stores providing data for the app.
     */
    private SAPServiceManager sapServiceManager;
    /**
     * Application-wide RepositoryFactory
     */
    private RepositoryFactory repositoryFactory;
    /**
     * Returns the application-wide service manager.
     *
     * @return the service manager
     */
    public SAPServiceManager getSAPServiceManager() {
        return sapServiceManager;
    }
    /**
     * Returns the application-wide repository factory
     *
     * @return the repository factory
     */
    public RepositoryFactory getRepositoryFactory() {
        return repositoryFactory;
    }

    /**
     * Clears all user-specific data from the application, essentially resetting
     * it to its initial state.
     *
     * If client code wants to handle the reset logic of a service, here is an example:
     * 
     *     SDKInitializer.INSTANCE.resetServices( service -> {
     *             if(service instanceof PushService) {
     *                 PushService.unregisterPushSync(new RemoteNotificationClient.CallbackListener() {
     *                     @Override
     *                     public void onSuccess() {
     *
     *                     }
     *
     *                     @Override
     *                     public void onError(@NonNull Throwable throwable) {
     *
     *                     }
     *                 });
     *                 return true;
     *             } else {
     *                 return false;
     *             }
     *         });
     */
    public void resetApp() {
        sp.edit().clear().apply();
        isApplicationUnlocked = false;
        repositoryFactory.reset();
        SDKInitializer.INSTANCE.resetServices(null);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        initService();
    }

    private void initService() {
        List<MobileService> services = new ArrayList<>();
        LoggingService loggingService = new LoggingService(false);
        loggingService.setPolicy(new LogPolicy(true, 0, "WARN", 0, 4));
        loggingService.setLogToConsole(true);
        services.add(loggingService);


        SDKInitializer.INSTANCE.start(this, services.toArray(new MobileService[0]), null);
    }

    /**
     * Initialize service manager with application configuration
     *
     * @param appConfig the application configuration
     */
    public void initializeServiceManager(AppConfig appConfig) {
        sapServiceManager = new SAPServiceManager(appConfig);
        repositoryFactory = new RepositoryFactory(sapServiceManager);
    }

}
