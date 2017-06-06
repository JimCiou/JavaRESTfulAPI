# JavaRESTfulAPI
These are only for Jim's backup, the final version has not been tested.

Hint: This application is installed in the Karaf container, the background environment needs to have Java and Maven.
## Need install several features

for API:
- feature:repo-add cxf 3.1.8
- feature:install cxf

for Database:
- feature:repo-add pax-jdbc 0.6.0
- feature:install pax-jdbc-mysql

## Need a third-party bundle
Use command "bundle:install file:/home/../.."

- org.eclipse.paho.client.mqttv3-X.X.X.jar

## Adaptor config file
Change config file, then put it under apache-karaf-X.X.X/etc/

```
#These informations are used to login into SQL
DBuser=root
DBname=example
DBpassword=12345
DBIP=localhost

#Send order to Adaptor and Send alarm to Mobile APP, both by MQTT.
#Topics to Adaptor and APP are /adaptor/# & /"DeviceID"/#.
MQqos=2
MQport=1883
MQIP=localhost
MQuserName=socket
MQpassword=12345  
```
- backend.example.properties

## Common config file
File already under apache-karaf-X.X.X/etc/

- users.properties, for Users.
- org.apache.karaf.shell.cfg, for SSH login.
- org.ops4j.pax.logging.cfg, for Logger.
