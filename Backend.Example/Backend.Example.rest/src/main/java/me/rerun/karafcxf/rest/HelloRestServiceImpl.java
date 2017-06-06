package me.rerun.karafcxf.rest;

import me.rerun.karafcxf.service.impl.HelloService;

import org.json.JSONObject;
import org.json.JSONException;
import org.apache.log4j.Logger;  
import org.apache.log4j.PropertyConfigurator;

public class HelloRestServiceImpl implements HelloRestService{

	//Just like Spring.  Please add Getters/Setters. Blueprint annotations are still work in progress
	private HelloService helloService;
	Logger logger = Logger.getLogger(HelloRestServiceImpl.class);

	/***************************************************registration*****************************************************/
	public String newUser(String deviceID, String username, String password, String device_data) throws JSONException {
		logger.warn("Used Backend URL: /registration Method:[PUT]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID+"/Username: "+username+"/Password: "+password);
		logger.warn("Backend Received: [Data] "+ device_data);

		String response=helloService.newUser(deviceID, username, password, device_data);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	public String checkUser(String username , String password) throws JSONException {
		logger.warn("Used Backend URL: /registration Method:[GET]");
		logger.warn("Backend Received: [Head] "+" Username: "+username+"/Password: "+password);

		String response=helloService.checkUser(username , password);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	/***************************************************locations*****************************************************/
	public String getIntervalGPS(String deviceID, String time_interval) throws JSONException {
		logger.warn("Used Backend URL: /locations Method:[POST]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID);
		logger.warn("Backend Received: [Data] "+ time_interval);

		String response=helloService.getIntervalGPS(deviceID, time_interval);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	public String putDataGPS(String deviceID, String deviceModel, String imsi, String adaptorID, String socket, String gps_data) throws JSONException {
		logger.warn("Used Backend URL: /locations Method:[PUT]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID+"/DeviceModel: "+deviceModel+"/IMSI: "+imsi+"/AdaptorID: "+adaptorID+"/Socket: "+socket);
		logger.warn("Backend Received: [Data] "+ gps_data);

		String response=helloService.putDataGPS(deviceID, deviceModel, imsi, adaptorID, socket, gps_data);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	/***************************************************devices*****************************************************/
	public String getNowGPS(String deviceID) throws JSONException {
		logger.warn("Used Backend URL: /devices Method:[GET]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID);

		String response=helloService.getNowGPS(deviceID);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	public String setTimeZone(String deviceID, String zone_data) throws JSONException {
		logger.warn("Used Backend URL: /device/timezone Method:[POST]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID);
		logger.warn("Backend Received: [Data] "+ zone_data);

		String response=helloService.setTimeZone(deviceID, zone_data);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	public String orderDeviceDown(String deviceID) throws JSONException {
		logger.warn("Used Backend URL: /device/shutdown Method:[POST]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID);

		String response=helloService.orderDeviceDown(deviceID);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	public String putDataDevice(String deviceID, String deviceModel, String imsi, String adaptorID, String socket, String device_data) throws JSONException {
		logger.warn("Used Backend URL: /devices Method:[POST]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID+"/DeviceModel: "+deviceModel+"/IMSI: "+imsi+"/AdaptorID: "+adaptorID+"/Socket: "+socket);
		logger.warn("Backend Received: [Data] "+ device_data);

		String response=helloService.putDataDevice(deviceID, deviceModel, imsi, adaptorID, socket, device_data);
		logger.warn("Backend Pesponse: [Json] "+ response);

	return response;}

	public void putDeviceWarn(String deviceID, String deviceModel, String imsi, String adaptorID, String socket, String warn_data) throws JSONException {
		logger.warn("Used Backend URL: /device/warnings Method:[PUT]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID+"/DeviceModel: "+deviceModel+"/IMSI: "+imsi+"/AdaptorID: "+adaptorID+"/Socket: "+socket);
		logger.warn("Backend Received: [Data] "+ warn_data);

		helloService.putDeviceWarn(deviceID, deviceModel, imsi, adaptorID, socket, warn_data);
	}

	public void putDeviceSet(String deviceID, String deviceModel, String imsi, String adaptorID, String socket, String response_data) throws JSONException {
		logger.warn("Used Backend URL: /device/settings Method:[POST]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID+"/DeviceModel: "+deviceModel+"/IMSI: "+imsi+"/AdaptorID: "+adaptorID+"/Socket: "+socket);
		logger.warn("Backend Received: [Data] "+ response_data);

		helloService.putDeviceSet(deviceID, deviceModel, imsi, adaptorID, socket, response_data);
	}

	public void putDeviceDiscon(String deviceID) throws JSONException {
		logger.warn("Used Backend URL: /device/disconnect Method:[POST]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID);

		helloService.putDeviceDiscon(deviceID);
	}

	/***************************************************fences*****************************************************/
	public String setGeoFence(String deviceID, String fence_data) throws JSONException {
		logger.warn("Used Backend URL: /fences Method:[PUT]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID);
		logger.warn("Backend Received: [Data] "+ fence_data);

		String response=helloService.setGeoFence(deviceID, fence_data);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	public String getGeoFence(String deviceID, String fence_id) throws JSONException {
		logger.warn("Used Backend URL: /fences Method:[POST]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID);
		logger.warn("Backend Received: [Data] "+ fence_id);

		String response=helloService.getGeoFence(deviceID, fence_id);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	public String deleteGeoFence(String deviceID, String geoFenceID) throws JSONException {
		logger.warn("Used Backend URL: /fences Method:[DELETE]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID+"/GeoFenceID: "+geoFenceID);

		String response=helloService.deleteGeoFence(deviceID, geoFenceID);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	/***************************************************sos*****************************************************/
	public String setSOSNumber(String deviceID, String number_phone) throws JSONException {
		logger.warn("Used Backend URL: /sos Method:[POST]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID);
		logger.warn("Backend Received: [Data] "+ number_phone);

		String response=helloService.setSOSNumber(deviceID, number_phone);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	public String getSOSNumber(String deviceID, String phoneID) throws JSONException {
		logger.warn("Used Backend URL: /sos Method:[GET]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID+"/PhoneID: "+phoneID);

		String response=helloService.getSOSNumber(deviceID, phoneID);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	public String deleteSOSNumber(String deviceID, String phoneID) throws JSONException {
		logger.warn("Used Backend URL: /sos Method:[DELETE]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID+"/PhoneID: "+phoneID);

		String response=helloService.deleteSOSNumber(deviceID, phoneID);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	/***************************************************falldown*****************************************************/
	public String setFalldown(String deviceID, String state) throws JSONException {
		logger.warn("Used Backend URL: /falldown Method:[POST]");
		logger.warn("Backend Received: [Head] "+"DeviceID: "+deviceID+"/State: "+state);

		String response=helloService.setFalldown(deviceID, state);
		logger.warn("Backend Pesponse: [Json] "+ response);
	return response;}

	/***************************************************debug*****************************************************/
	public String debug() throws JSONException {
	return helloService.debug();
	}


	//Constructor
	public HelloRestServiceImpl(){}

	//Getters and Setters
	public HelloService getHelloService() {
		return helloService;}

	public void setHelloService(HelloService helloService) {
		this.helloService = helloService;
	}
}
