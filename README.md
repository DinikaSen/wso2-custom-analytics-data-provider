# Publishing Custom Analytics Events Data with WSO2 API Manager

This repository contains a sample custom analytics data provider for WSO2 API Manager 4.7.0. This enables publishing 
custom analytics data along with the default data using the existing event schema.

This particular example is related to B2B API Management in WSO2 API Manager. You can read more about this feature in : https://apim.docs.wso2.com/en/latest/api-developer-portal/b2b-api-consumption/api-consumption/ 

This custom analytics data provider publishes the 'organization ID' and 'organization name' properties to Moesif 
analytics provider so that custom dashboards can be implemented per organization. 

You can find additional information regarding this custom component in : https://apim.docs.wso2.com/en/latest/monitoring/api-analytics/samples/publishing-custom-analytics-data/

## Try Out Instructions

Follow these steps to deploy and configure the custom analytics data provider:

### Step 1: Build the Project

Build the project to generate the artifact:

```bash
mvn clean install
```

This will generate the JAR file: `target/org.wso2.custom.analytics.provider-1.0.jar`

### Step 2: Deploy the Artifact

Copy the generated JAR file to the <APIM_HOME>/repository/components/lib/ folder:

```bash
cp target/org.wso2.custom.analytics.provider-1.0.jar <APIM_HOME>/repository/components/lib/
```

### Step 3: Add the configurations

Add the below configuration to the deployment.toml. The 'type' could be any of the supported analytics platforms 
(ex : elk, opensearch). In this example, we are using 'Moesif' as the analytics provider. 
The publisher.custom.data.provider.class property should point to the fully qualified class name of the custom data provider implementation.

```
[apim.analytics]
enable = true
type = "moesif"

[apim.analytics.properties]
moesifKey = "<MOESIF_KEY>"
"publisher.custom.data.provider.class" = "org.wso2.custom.analytics.provider.CustomDataProvider"

```

### Step 4: Restart the Server

Restart the WSO2 API Manager Server.

---
