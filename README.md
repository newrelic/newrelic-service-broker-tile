# New Relic Service Broker

This project is a self-hostable application that provides a service broker which proxies New Relic credentials to applications.  This is typically useful for OSS Cloud Foundry or on-premise deployments of Cloud Foundry that you wish to easily bind to an off-premise New Relic deployment. In order to facilitate self-hosting, the application is designed to work in [Cloud Foundry][p].

<br/>

## Prerequisites

*    One or more New Relic accounts/sub-accounts
*    A New Relic valid license key for each account.  
 		**Note:** You can obtain the license key for each account from your New Relic account under "Account Settings".

*    A running Cloud Foundry environment
*	 Java Development Kit 1.8 or higher
*    Building the application (required for deployment) requires [Maven][v]
*    Proxy host and port details if your PCF environment requires proxy setting

<br/>

## Installation, Deployment, and Usage Procedures

_The following instructions assume that you have [created an account][c] and [installed the `cf` command line tool][l]._

In order to automate the deployment process as much as possible, the project contains a Cloud Foundry [manifest][m].  To build and deploy the service broker follow the steps below:

+ [Download New Relic Service Broker](#download-repo)
+ [Build self-executable JAR](#build-jar)
+ [Edit manifest.yml file](#edit-manifest)
+ [Push Service Broker Application to Cloud Foundry](#push-sb-app)
+ [Create Service Broker](#create-sb)
+ [Enable Access to Service Broker](#-enable-access)
+ [Create Service Instance for each Plan](#create-svc-instance)
+ [Bind the Service to your application](#bind-svc)
+ [(Optional)  Add Proxy configuration](#set-proxy)
+ [Re-Stage or re push your application](#restage-app)

<br/><br/>

### <a id='download-repo'></a>Download New Relic Service Broker

```
git clone https://github.com/newrelic/newrelic-service-broker-tile.git
cd newrelic-service-broker
```

<br/>

### <a id='build-jar'></a>Build self-executable JAR

```
$ mvn package -Dmaven.test.skip=true
$ cf push
```

<br/>

### <a id='edit-manifest'></a>Edit manifest.yml file

+ if necessary, specify the correct "domain" for your PCF environment
+ If you PCF environment is disconnected, change the buildpack to java offline buildpack
+ Modify the **"env:"** section at the end of the file with correct values for the following 3 environment variables:
```
  env:
    SECURITY_USER_NAME: "<DIRECTOR_USER_NAME>"
    SECURITY_USER_PASSWORD: "<DIRECTOR_PASSWORD>"
    NRPLANS: '<A JSON ARRAY CONTAINING ONE OR MORE PLANS/LICENSE KEYS>'
```

+ Update **"NRPLANS"** with correct fields for each plan (required fields: planName, licenseKey).
+ Optionally you can provide "planDescription", but it's not required
+ The "guid" field is not required for OSS Cloud Foundry
+ "oldPlan" and "planOldGuid" are required only if you are upgrading from service broker 1.12.17 or older versions

The “planName” is how your developers will know which New Relic account to use for their applications.  
Name it such that users will know which New Relic account to use.  
**Note:** "planName" cannot contain any spaces (you can use dashes or camelCase to separate words).  

The "licenseKey" value can be found in the "Account Administration" menu option from the top right corner of New Relic. 
Plan names are free form text with no spaces, you can use dashes between words.   
**Note:** NRPLANS json array must be defined all in one line.


#### Environment Variables
Following is list of the required environmenet variables with sample values, which you can define in the manifest file.

| Name | Description
| --- | -----------
| `NRPLANS` | JSON object array with service broker plan names and New Relic account license keys associated with them. e.x. NRPLANS: '[{"planName": "hybris", "licenseKey" : "0123456789abcdef0123456789abcdef01234567", "planOldGuid": ""}, { "planName": "cloundfoundry" , "licenseKey": "1234567890abcdef", "planOldGuid": ""},  { "planName": "ecs", "licenseKey" : "0123456789abcdef0123456789abcdef89abcdef", "planOldGuid": ""}]'
| `SECURITY_USER_NAME` | The username that Cloud Controller should use to authenticate the service. This can be any value.
| `SECURITY_USER_PASSWORD` | The password that Cloud Controller should use to authenticate the service. This can be any value.

<br/>

### <a id='push-sb-app'></a>Push Service Broker Application to Cloud Foundry
```
cf push
```

This command will push the service broker application into Cloud Foundry and start running it.
In the last section of the **cf push** output make note of the value for **routes** which is the url that will be used in the next step.

<br/>

### <a id='create-sb'></a>Create Service Broker
```
cf create-service-broker SERVICE_BROKER_NAME SECURITY_USER_NAME SECURITY_USER_PASSWORD URL
```

	Where:

	`SERVICE_BROKER_NAME` = the service broker name determined by you
	`SECURITY_USER_NAME` = the security user name defined from the manifest.yml file when you pushed the app
	`SECURITY_USER_PASSWORD` = the security password defined from the manifest.yml file when you pushed the app
	`URL` = the url from previous step prepended with protocol (http:// or https://)


sample output:
```
broker: newrelic-broker
service    plan                    access orgs   
newrelic   New-Relic-Test          none        
newrelic   New-Relic-Production    none        
``` 

<br/>

### <a id='-enable-access'></a>Enable Access to the Service Broker
```
cf enable-service-access newrelic [-p PLAN] [-o ORG]
```

You can enable access to specific **"plan"** and/or for specific **"org"**

<br/>

### <a id='create-svc-instance'></a>Create Service Instance for each Plan
Each service plan is associated with a New Relic Account. Create a service instance for each of the plans
```
cf create-service newrelic PLAN SERVICE_INSTANCE
```

	Where:

	`PLAN` = servive plan name that you want to use
	`SERVICE_INSTANCE` = name of the newly created service instance

<br/>

### <a id='bind-svc'></a>Bind the Service to your application
```
cf bind-service MY_APP MY_SERVICE_INSTANCE
```

	Where:

	`MY_APP` = application name to bind to service instance
	`MY_SERVICE_INSTANCE` = the service instance created in previous step

<br/>

### <a id='set-proxy'></a>(Optional)  Add Proxy configuration
If your environment is behind a proxy, add the proxy settings to your application by setting **"JAVA_OPTS"** environment variable
```
cf set-env <MY_APP> JAVA_OPTS "-Dnewrelic.config.proxy_host=proxy.yourCompany.com -Dnewrelic.config.proxy_port=nnn"
```
**Note:** If you're using a proxy across all of your applications, you may want to implement a PCF 'Environment Variable Group' for the staging process.
```
$ cf ssevg '{"JAVA_OPTS":"-Dnewrelic.config.proxy_host=proxy.yourCompany.com -Dnewrelic.config.proxy_port=nnn"}'
Setting the contents of the running environment variable group as admin...
OK
```
```
$ cf sevg
Retrieving the contents of the running environment variable group as admin...
OK
Variable Name   Assigned Value
JAVA_OPTS           -Dnewrelic.config.proxy_host=proxy.yourCompany.com -Dnewrelic.config.proxy_port=nnn
```
This will enable you to set the JAVA_OPTS parameters on a more global basis such that all applications would inherit the settings without the need to add application level settings to each application. You can find more details on [Environment Variable Groups][e]

<br/>

### <a id='restage-app'></a>Re-stage or re-push your application
```
cf push
or
cf restage MY_APP
```

<br/><br/>

## License
The project is released under version 2.0 of the [Apache License][a].

[a]: http://www.apache.org/licenses/LICENSE-2.0
[c]: http://docs.cloudfoundry.com/docs/dotcom/getting-started.html#signup
[l]: http://docs.cloudfoundry.com/docs/dotcom/getting-started.html#install-cf
[m]: manifest.yml
[p]: http://run.pivotal.io
[v]: http://maven.apache.org
[e]: https://docs.pivotal.io/pivotalcf/devguide/deploy-apps/environment-variable.html#evgroups
