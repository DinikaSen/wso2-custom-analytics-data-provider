package org.wso2.custom.analytics.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.rest.RESTConstants;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.dto.OrganizationDetailsDTO;
import org.wso2.carbon.apimgt.common.analytics.collectors.AnalyticsCustomDataProvider;
import org.wso2.carbon.apimgt.gateway.handlers.security.APISecurityUtils;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;
import org.wso2.carbon.apimgt.impl.APIAdminImpl;
import org.wso2.carbon.apimgt.keymgt.SubscriptionDataHolder;
import org.wso2.carbon.apimgt.keymgt.model.SubscriptionDataStore;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CustomDataProvider implements AnalyticsCustomDataProvider {

    private static final Log log = LogFactory.getLog(CustomDataProvider.class);
    private static final Map<String, String> orgNameCache = new ConcurrentHashMap<>();

    public CustomDataProvider() {
        log.info("CustomDataProvider successfully initialized");
    }

    @Override public Map<String, Object> getCustomProperties(Object context) {

        if (context instanceof MessageContext) {
            Map<String, Object> customProperties = new HashMap<>();

            // Add UserOrganization (stored as INTERNAL_USER_ORGANIZATION_PROP per PR #12960)
            AuthenticationContext authContext = APISecurityUtils.getAuthenticationContext((MessageContext) context);
            if (authContext != null && authContext.getApplicationId() != null) {
                try {
                    String apiContext = (String) ((MessageContext) context).getProperty(RESTConstants.REST_API_CONTEXT);
                    String tenantDomain = MultitenantUtils.getTenantDomainFromRequestURL(apiContext);
                    if (tenantDomain == null) {
                        tenantDomain = MultitenantConstants.SUPER_TENANT_DOMAIN_NAME;
                    }
                    SubscriptionDataStore store = SubscriptionDataHolder.getInstance()
                            .getTenantSubscriptionStore(tenantDomain);
                    if (store != null) {
                        int appId = Integer.parseInt(authContext.getApplicationId());
                        org.wso2.carbon.apimgt.keymgt.model.entity.Application app = store.getApplicationById(appId);
                        if (app != null) {
                            String userOrg = app.getAttributes().get("INTERNAL_USER_ORGANIZATION_PROP");
                            if (userOrg != null) {
                                log.debug("UserOrganization added to analytics data : " + userOrg);
                                customProperties.put("userOrganization", userOrg);
                                String displayName = orgNameCache.get(userOrg);
                                if (displayName == null) {
                                    try {
                                        OrganizationDetailsDTO details =
                                                new APIAdminImpl().getOrganizationDetails(userOrg, tenantDomain);
                                        displayName = (details != null && details.getName() != null)
                                                ? details.getName() : "";
                                    } catch (APIManagementException e) {
                                        log.warn("Failed to resolve display name for organization ID: " + userOrg, e);
                                        displayName = "";
                                    }
                                    orgNameCache.put(userOrg, displayName);
                                }
                                customProperties.put("userOrganizationName", displayName);
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    log.warn("Failed to parse application ID: " + authContext.getApplicationId());
                }
            }
            return customProperties;
        }
        return null;
    }
}