# Publishing Custom Analytics Events Data with WSO2 API Manager

This repository contains a sample custom analytics data provider for WSO2 API Manager 4.6.0. This enables publishing 
custom analytics data along with the default data using the existing event schema.

This particular example publishes 'soap_action' property which is set from a mediation policy, but the same approach
can be used to add any additional data. 

You can find additional information regarding this on : https://apim.docs.wso2.com/en/latest/monitoring/api-analytics/samples/publishing-custom-analytics-data/

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
(ex : elk, opensearch). 

```
[apim.analytics]
enable = true
type = "opensearch"
properties."publisher.custom.data.provider.class" = "org.wso2.custom.analytics.provider.CustomDataProvider"
```

### Step 6: Restart the Server

Restart the WSO2 API Manager Server.

---
