package me.rerun.karafcxf.service.impl;

import org.json.JSONObject;
import org.json.JSONException;

public interface HelloService {

	/***************************************************registration*****************************************************/
	public String newUser(String deviceID, String username, String password, String device_data) throws JSONException ;

	public String checkUser(String username , String password) throws JSONException ;


	/***************************************************locations*****************************************************/
	public String getIntervalGPS(String deviceID, String time_interval) throws JSONException ;

	public String putDataGPS(String deviceID, String deviceModel, String imsi, String adaptorID, String socket, String gps_data) throws JSONException ;


	/***************************************************devices*****************************************************/
	public String getNowGPS(String deviceID) throws JSONException ;

	public String setTimeZone(String deviceID, String zone_data) throws JSONException ;

	public String orderDeviceDown(String deviceID) throws JSONException ;

	public String putDataDevice(String deviceID, String timezone, String imsi, String adaptorID, String socket, String device_data) throws JSONException ;

	public void putDeviceWarn(String deviceID, String deviceModel, String imsi, String adaptorID, String socket, String warn_data) throws JSONException ;

	public void putDeviceSet(String deviceID, String deviceModel, String imsi, String adaptorID, String socket, String response_data) throws JSONException ;

	public void putDeviceDiscon(String deviceID) throws JSONException ;


	/***************************************************fences*****************************************************/
	public String setGeoFence(String deviceID, String fence_data) throws JSONException ;

	public String getGeoFence(String deviceID, String fence_id) throws JSONException ;

	public String deleteGeoFence(String deviceID, String geoFenceID) throws JSONException ;


	/***************************************************sos*****************************************************/
	public String setSOSNumber(String deviceID, String number_phone) throws JSONException ;

	public String getSOSNumber(String deviceID, String phoneID) throws JSONException ;

	public String deleteSOSNumber(String deviceID, String phoneID) throws JSONException ;


	/***************************************************falldown*****************************************************/
	public String setFalldown(String deviceID, String state) throws JSONException ;


	/***************************************************debug*****************************************************/
	public String debug() throws JSONException ;

}
