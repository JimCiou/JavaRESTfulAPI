package me.rerun.karafcxf.service.impl;

import me.rerun.karafcxf.service.impl.example.*;
import me.rerun.karafcxf.service.impl.example.system.*;
import me.rerun.karafcxf.service.impl.example.func.*;
import me.rerun.karafcxf.service.impl.example.data.*;

import java.util.*;

import org.json.JSONException;
import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;

public class HelloServiceImpl extends Thread implements HelloService {

	Hashtable<String,Device> devices_Array = new Hashtable<String,Device>();
	Hashtable<String,String> feedback_identifierArray = new Hashtable<String,String>();

	Registration register=new Registration();
	Devices devicer=new Devices();
	Locations locationer=new Locations();
	Fences fencer=new Fences();
	SOS soser=new SOS();
	Falldown falldowner=new Falldown();

	LBS lbser=new LBS();
	DataBase datauser=new DataBase();
	Logger logger = Logger.getLogger(HelloServiceImpl.class);

	int DelayTime=20;//等待Config Response的上限時間(s)
//	int DelayTime=60;//等待Config Response的上限時間(s) for Debug

	/***************************************************common function**************************************************/
	public Device getDevice(String deviceID) {
		Device device=devices_Array.get(deviceID);
		if(device==null){
			devices_Array.put(deviceID,new Device(deviceID));
			device=devices_Array.get(deviceID);}
	return device;}

	public String feedbackCheck(String deviceID) {
		String identifier_initial = Integer.toString((int)(Math.random()*89999999)+10000000);
		logger.warn("Identifier initial: "+identifier_initial);
		feedback_identifierArray.remove(deviceID);
	return identifier_initial;}

	public boolean feedbackCheck(String deviceID, String identifier_initial) {
		boolean check=false;
		
		for(int i=0;i<DelayTime;i++){
		try { sleep(500); } catch(InterruptedException ie) { ie.printStackTrace(); } 
		if(feedback_identifierArray.get(deviceID)!=null)break;}
		if(identifier_initial.equals((String)feedback_identifierArray.get(deviceID))){
			check=true;
			logger.warn("Identifier pass!!");}
		else{
			logger.warn("Identifier error!!");}
	return	check;}

	/***************************************************registration*****************************************************/
	@Override
	public String newUser(String deviceID, String username, String password, String device_data) throws JSONException {
	return register.newUser(deviceID, username, password, device_data);}

	@Override
	public String checkUser(String username , String password) throws JSONException {
	return register.checkUser(username, password);}

	/***************************************************locations*******************************************************/
	@Override
	public String getIntervalGPS(String deviceID, String time_interval) throws JSONException {
	return locationer.getIntervalGPS(deviceID, time_interval);}

	@Override
	public String putDataGPS(String deviceID, String deviceModel, String imsi, String adaptorID, String socket, String gps_data) throws JSONException {

		Device device=getDevice(deviceID);
		String response="{Error:DeviceID Non-registered}";

		if(device.getRegistered()!=null){//確認DeviceID是否註冊過,沒註冊不給予存取座標,Check Table First
			logger.warn("Check Device registered from Table");
			device.setSocket(socket);
			device.setAdaptorID(adaptorID);
			response=locationer.putDataGPS(device, lbser, gps_data);}
		else{//確認DeviceID是否註冊過,沒註冊不給予存取座標,Check DB Second
			datauser.loginDB("etc/backend.example.properties");
			String[][] device_ckeck=datauser.selectDB("DeviceID", "Devices", "where DeviceID = '"+deviceID+"'", " ");
			datauser.closeDB();
				//Check DB pass
				if(device_ckeck.length!=0){
					logger.warn("Check Device registered from DB");
					device.setSocket(socket);
					device.setAdaptorID(adaptorID);
					device.setRegistered("registered");
					response=locationer.putDataGPS(device, lbser, gps_data);}
				else{logger.warn("DeviceID Non-registered");}
		}
	return response;}

	/***************************************************devices*****************************************************/
	@Override
	public String getNowGPS(String deviceID) throws JSONException {

		Device device=getDevice(deviceID);

	return devicer.getNowGPS(device);}

	@Override
	public String setTimeZone(String deviceID, String zone_data) throws JSONException {

		Device device=getDevice(deviceID);

	return devicer.setTimeZone(zone_data, device);}

	@Override
	public String orderDeviceDown(String deviceID) throws JSONException {

		Device device=getDevice(deviceID);
		String response="{Error:Device disconnect}";

		if(device.getSocket()==null){}
		else{
			String identifier_initial=feedbackCheck(deviceID);
			response = devicer.orderDeviceDown(device, identifier_initial);		
			if(feedbackCheck(deviceID, identifier_initial)){}
			else{response="{Error:Device config error}";}
		}
	return response;}

	@Override
	public String putDataDevice(String deviceID, String deviceModel, String imsi, String adaptorID, String socket, String device_data) throws JSONException {

		Device device=getDevice(deviceID);		
		String response="{Error:DeviceID Non-registered}";

		if(device.getRegistered()!=null){//確認DeviceID是否註冊過,沒註冊不給予存取座標,Check Table First
			logger.warn("Check Device registered from Table");
			device.setSocket(socket);
			device.setAdaptorID(adaptorID);
			response=devicer.putDataDevice(device, device_data);}
		else{//確認DeviceID是否註冊過,沒註冊不給予存取座標,Check DB Second
			datauser.loginDB("etc/backend.example.properties");
			String[][] device_ckeck=datauser.selectDB("DeviceID", "Devices", "where DeviceID = '"+deviceID+"'", " ");
			datauser.closeDB();
				//Check DB pass
				if(device_ckeck.length!=0){
					logger.warn("Check Device registered from DB");
					device.setSocket(socket);
					device.setAdaptorID(adaptorID);
					device.setRegistered("registered");
					response=devicer.putDataDevice(device, device_data);}
				else{logger.warn("DeviceID Non-registered");}
		}
	return response;}

	@Override
	public void putDeviceWarn(String deviceID, String deviceModel, String imsi, String adaptorID, String socket, String warn_data) throws JSONException {

		Device device=getDevice(deviceID);
		String response="{Error:DeviceID Non-registered}";

		if(device.getRegistered()!=null){//確認DeviceID是否註冊過,沒註冊不給予存取座標,Check Table First
			logger.warn("Check Device registered from Table");
			device.setSocket(socket);
			device.setAdaptorID(adaptorID);
			devicer.putDeviceWarn(device, lbser, warn_data);}
		else{//確認DeviceID是否註冊過,沒註冊不給予存取座標,Check DB Second
			datauser.loginDB("etc/backend.example.properties");
			String[][] device_ckeck=datauser.selectDB("DeviceID", "Devices", "where DeviceID = '"+deviceID+"'", " ");
			datauser.closeDB();
				//Check DB pass
				if(device_ckeck.length!=0){
					logger.warn("Check Device registered from DB");
					device.setSocket(socket);
					device.setAdaptorID(adaptorID);
					device.setRegistered("registered");
					devicer.putDeviceWarn(device, lbser, warn_data);}
				else{logger.warn("DeviceID Non-registered");}
		}
	}

	@Override
	public void putDeviceSet(String deviceID, String deviceModel, String imsi, String adaptorID, String socket, String response_data) throws JSONException {

		Device device=getDevice(deviceID);
		String response="{Error:DeviceID Non-registered}";

		if(device.getRegistered()!=null){//確認DeviceID是否註冊過,沒註冊不給予存取座標,Check Table First
			logger.warn("Check Device registered from Table");
			device.setSocket(socket);
			device.setAdaptorID(adaptorID);
			String identifier_feedback=devicer.putDeviceSet(device, response_data);
			if(!identifier_feedback.equals("00000000")){
				feedback_identifierArray.put(deviceID, identifier_feedback);
				logger.warn("Identifier feedback from Device: "+identifier_feedback);
			}
		}
		else{//確認DeviceID是否註冊過,沒註冊不給予存取座標,Check DB Second
			datauser.loginDB("etc/backend.example.properties");
			String[][] device_ckeck=datauser.selectDB("DeviceID", "Devices", "where DeviceID = '"+deviceID+"'", " ");
			datauser.closeDB();
			//Check DB pass
			if(device_ckeck.length!=0){
				logger.warn("Check Device registered from DB");
				device.setSocket(socket);
				device.setAdaptorID(adaptorID);
				device.setRegistered("registered");
				String identifier_feedback=devicer.putDeviceSet(device, response_data);
				if(!identifier_feedback.equals("00000000")){
					feedback_identifierArray.put(deviceID, identifier_feedback);
					logger.warn("Identifier feedback from Device: "+identifier_feedback);}
				}
			else{logger.warn("DeviceID Non-registered");}
		}
	}

	@Override
	public void putDeviceDiscon(String deviceID) throws JSONException {

		Device device=getDevice(deviceID);

		device.setSocket(null);
		device.setAdaptorID(null);
		feedback_identifierArray.remove(deviceID);
	}

	/***************************************************fences*****************************************************/
	@Override
	public String setGeoFence(String deviceID, String fence_data) throws JSONException {

		Device device=getDevice(deviceID);
		String response="{Error:Device disconnect}";

		if(device.getSocket()==null){}
		else{
			String identifier_initial=feedbackCheck(deviceID);
			fencer.setGeoFenceConfig(device, fence_data, identifier_initial);		
			if(feedbackCheck(deviceID, identifier_initial)){
				response=fencer.setGeoFenceDB(device, fence_data);}
			else{response="{Error:Device config error}";}}
	return response;}

	@Override
	public String getGeoFence(String deviceID, String fence_id) throws JSONException {

		Device device=getDevice(deviceID);
		String response="{Error:Device disconnect}";

		if(device.getSocket()==null){}
		else{
			String identifier_initial=feedbackCheck(deviceID);
			fencer.getGeoFenceConfig(device, identifier_initial);		
			if(feedbackCheck(deviceID, identifier_initial)){
				response = fencer.getGeoFenceDB(device, fence_id, true);}
			else{response = fencer.getGeoFenceDB(device, fence_id, false);}}
	return response;}

	@Override
	public String deleteGeoFence(String deviceID, String geoFenceID) throws JSONException {

		Device device=getDevice(deviceID);
		String response="{Error:Device disconnect}";

		if(device.getSocket()==null){}
		else{
			String identifier_initial=feedbackCheck(deviceID);
			fencer.deleteGeoFenceConfig(device, geoFenceID, identifier_initial);		
			if(feedbackCheck(deviceID, identifier_initial)){
				response = fencer.deleteGeoFenceDB(device, geoFenceID);}
			else{response="{Error:Device config error}";}}
	return response;}

	/***************************************************sos*****************************************************/
	@Override
	public String setSOSNumber(String deviceID, String number_phone) throws JSONException {

		Device device=getDevice(deviceID);
		String response="{Error:Device disconnect}";

		if(device.getSocket()==null){}
		else{
			String identifier_initial=feedbackCheck(deviceID);
			soser.setSOSNumberConfig(device, number_phone, identifier_initial);		
			if(feedbackCheck(deviceID, identifier_initial)){
				response=soser.setSOSNumberDB(device, number_phone);}
			else{response="{Error:Device config error}";}}
	return response;}

	@Override
	public String getSOSNumber(String deviceID, String phoneID) throws JSONException {

		Device device=getDevice(deviceID);
		String response="{Error:Device disconnect}";

		if(device.getSocket()==null){}
		else{
			String identifier_initial=feedbackCheck(deviceID);
			soser.getSOSNumberConfig(device, identifier_initial);		
			if(feedbackCheck(deviceID, identifier_initial)){
				response = soser.getSOSNumberDB(device, phoneID, true);}
			else{response = soser.getSOSNumberDB(device, phoneID, false);}}
	return response;}

	@Override
	public String deleteSOSNumber(String deviceID, String phoneID) throws JSONException {

		Device device=getDevice(deviceID);
		String response="{Error:Device disconnect}";

		if(device.getSocket()==null){}
		else{
			String identifier_initial=feedbackCheck(deviceID);
			soser.deleteSOSNumberConfig(device, phoneID, identifier_initial);		
			if(feedbackCheck(deviceID, identifier_initial)){
				response = soser.deleteSOSNumberDB(device, phoneID);}
			else{response="{Error:Device config error}";}}
	return response;}

	/***************************************************falldown*****************************************************/
	@Override
	public String setFalldown(String deviceID, String state) throws JSONException {

		Device device=getDevice(deviceID);
		String response="{Error:Device disconnect}";

		if(device.getSocket()==null){}
		else{
			String identifier_initial=feedbackCheck(deviceID);
			falldowner.setFalldownConfig(device, state, identifier_initial);		
			if(feedbackCheck(deviceID, identifier_initial)){
				response = falldowner.setFalldownDB(device, state);}
			else{response="{Error:Device config error}";}}
	return response;}

	/***************************************************debug*****************************************************/
	@Override
	public String debug() throws JSONException {

		String response="No design";

	return response;}
}
