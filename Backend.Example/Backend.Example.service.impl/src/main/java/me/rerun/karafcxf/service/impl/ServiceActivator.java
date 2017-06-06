package me.rerun.karafcxf.service.impl;

import me.rerun.karafcxf.service.impl.example.system.*;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.apache.log4j.Logger;  
import org.apache.log4j.PropertyConfigurator;

public class ServiceActivator implements BundleActivator {

	DataBase datauser=new DataBase();
	Logger logger = Logger.getLogger(ServiceActivator.class);

	public void start(BundleContext context) {
        	logger.warn("Starting the Tracker service Impl");

		datauser.loginDB("etc/backend.example.properties");
		datauser.createDB("Users", "Username VARCHAR(36)"+",Password VARCHAR(128)");
		datauser.createDB("Devices", "DeviceID VARCHAR(15)"+",Username VARCHAR(36)"+",DeviceName VARCHAR(15)"+",DevicePhoneNumber VARCHAR(15)"+
		",VoltageLevel VARCHAR(1)"+",GSMLevel VARCHAR(1)"+",AdaptorID VARCHAR(36)"+",TimeZone VARCHAR(8)"+",DeviceStatus VARCHAR(1)");
		datauser.createDB("Locations", "DeviceID VARCHAR(15)"+",Time DATETIME"+",Lat VARCHAR(24)"+",Lng VARCHAR(24)"+",LocationType VARCHAR(1)");
		datauser.createDB("Fences", "GeoFenceID VARCHAR(1)"+",DeviceID VARCHAR(15)"+",Lat VARCHAR(24)"+",Lng VARCHAR(24)"+",Radius VARCHAR(3)"+
		",AlarmStatus VARCHAR(1)"+",AlarmType VARCHAR(1)"+",AlarmMode VARCHAR(1)"+",FenceName VARCHAR(36)");
		datauser.createDB("SOS_phones", "DeviceID VARCHAR(15)"+",PhoneID VARCHAR(1)"+",Phone VARCHAR(20)");
		datauser.createDB("BSinfo", "StationID VARCHAR(128)"+",Lat VARCHAR(24)"+",Lng VARCHAR(24)");
		datauser.createDB("LBS", "DeviceID VARCHAR(15)"+",Time DATETIME"+",MCC VARCHAR(8)"+",MNC VARCHAR(8)"+",LAC VARCHAR(8)"+",CI VARCHAR(8)"+
		",RSSI VARCHAR(8)");
		datauser.closeDB();
	}

	public void stop(BundleContext context) {
        	logger.warn("Stopping the Tracker service Impl");}

}
