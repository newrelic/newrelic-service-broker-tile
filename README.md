# newrelic-pcf-agent-tile
==========================================================
- - -

This repository contains the Pivotal Cloud Foundry Tile that allows you to automatically bind New Relic language agents with your applications in PCF on-prem environment for Ops Mgr 1.4 on vSphere and AWS environments, and it also supports Ops Mgr 1.3 on vSphere.


##Prerequisites

*    One or more New Relic accounts/sub-accounts. If you don't have one, feel free to grab one using this link: http://newrelic.com/pivotal.
*    A New Relic valid license key for each account/sub-account. You can obtain the license key for each account from your New Relic account under 'Account Settings'
*    An on-prem Pivotal Cloud Foundry environment with Ops Mgr privileges


##Installation

*    Download the New Relic Service Broker Tile for your environment (vSphere/AWS) and version (1.3/1.4) of Ops Mgr from this repo
*    Login to your PCF Ops Mgr 
*    On the left side of the page click on the button **"Import a Product"**
*    Select the downloaded **.pivotal** tile file (i.e. NewRelic-Broker-v1.0.0-vSphere-OpsMgrv1.4.pivotal)
*    Allow the file upload to be completed

**Note:** The system doesn't give you any indications during the upload, but you will get a pop-up message at the end, as to whether the upload succeeded ot failed.


Once the tile is uploaded to PCF environment:

*    Hover your mouse on **"Newrelic Service Broker v-x.x.x"** on the left side under **"Available Products"**, and select the **"Add"** button to add the tile
*    Click on New Relic Tile that was just added to the page
*    Under **"Settings"** tab select **"Service Broker Application"**
*    For every account or sub-account you want to add click the **"Add"** button on the right side
*    Enter **"Plan Name"**, **"Plan Description"**, and your **"New Relic license key"** for each account/sub-account
*    Once you have entered plans for all desired accounts, click the **"Save"** button
*    Go back to **"Installation Dashboard"** (link on top left of the page)
*    Click the big blue **"Apply Changes"** button on top right of the page. This will take about 30 minutes to finish.
*    Once changes are applied, the tile will appear in the **"Marketplace"**


## Use New Relic agent Tile in PCF

*    Login to your PCF console
*    Select your **"Org"** (or create new **"Org"** and **"Space"** as you wish)
*    Go to **"Marketplace"**
*    Select **"New Relic"** Tile and click on **"View Plan Options"**
*    Select the plan from the left that is associated with your desired New Relic account and click on **"Select this plan"** button
*    Fill in the **"Instance Name"**, 
*    Select the **"space"** you'd like to use
*    Select the **"application"** to which to bind the service
*    Click the **"Add"** button

## Restaging the Application
In rare cases you might need to restage the application.
*    From your system connect to CF
    *    cf api **\<CF_API_ENDPOINT\>** [--skip-ssl-validation]
    *    cf login -u **\<USER\>** -p **\<PASSWORD\>**
    *    **Note:** if you have multiple **"Org"** and/or **"Space"** in your PCF, it will prompt you to select the desired org and space
*    Restage the application you just bound to the service
    *    cf restage **\<APPNAME\>**
*    Login to your New Relic account to check the health of the application


**Note:** After the initial time that the service is added to the space, you can just go to the intended space, scroll down to the bottom of the page, select **"Services"** tab, and click on **"+Bind a Service"** button to use the previously added service.


