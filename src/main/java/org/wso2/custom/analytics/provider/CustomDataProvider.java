package org.wso2.custom.analytics.provider;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.wso2.carbon.apimgt.common.analytics.collectors.AnalyticsCustomDataProvider;
import org.wso2.carbon.apimgt.gateway.handlers.security.AuthenticationContext;
import org.wso2.carbon.apimgt.gateway.handlers.streaming.websocket.WebSocketUtils;

import java.util.HashMap;
import java.util.Map;

public class CustomDataProvider implements AnalyticsCustomDataProvider {

    private static final Log log = LogFactory.getLog(CustomDataProvider.class);

    public CustomDataProvider() {
        log.info("CustomDataProvider successfully initialized");
    }

    @Override public Map<String, Object> getCustomProperties(Object context) {

        if (context instanceof MessageContext) {
            Map<String, Object> customProperties = new HashMap<>();

            // Add SOAPAction
            Object soapAction = ((MessageContext) context).getProperty("soap_action");
            if (soapAction != null) {
                log.debug("SOAPAction added to analytics data : " + soapAction);
                customProperties.put("SOAPAction", soapAction.toString());
            }

            // Add any other additional data

            return customProperties;
        }

        return null;
    }
}