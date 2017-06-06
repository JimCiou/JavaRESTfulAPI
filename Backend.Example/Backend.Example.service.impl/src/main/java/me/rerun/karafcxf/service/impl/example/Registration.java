package me.rerun.karafcxf.service.impl.example;

import me.rerun.karafcxf.service.impl.example.system.*;

import java.util.*;

import org.json.JSONObject;
import org.json.JSONException;
import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;

public class Registration {

	DataBase datauser=new DataBase();
	Logger logger = Logger.getLogger(Registration.class);

	public String newUser(String deviceID, String username, String password, String device_data) throws JSONException {

		//為了接住輸入的資料  宣告JSONObject 
		JSONObject jsonObj_device_data = new JSONObject(device_data);

		String deviceName = jsonObj_device_data.get("DeviceName").toString();
		String devicePhoneNumber = jsonObj_device_data.get("DevicePhoneNumber").toString();

		//為了建構輸出的資料  宣告JSONObject
		JSONObject jsonObj_newUser = new JSONObject();

		//判斷並新增使用者資料
		datauser.loginDB("etc/backend.example.properties");

		String[][] nameCheckout=datauser.selectDB("Username", "Users", "where Username Like '"+username+"'", " ");
		String[][] deviceCheckout=datauser.selectDB("DeviceID", "Devices", "where DeviceID = '"+deviceID+"'", " ");

		if((nameCheckout.length!=0) && (deviceCheckout.length!=0)){
			jsonObj_newUser.put("Fail", "User and Device Existed");}
		else if((nameCheckout.length!=0) && (deviceCheckout.length==0)){
			jsonObj_newUser.put("Fail", "User Existed");}
		else if((nameCheckout.length==0) && (deviceCheckout.length!=0)){
			jsonObj_newUser.put("Fail", "Device Existed");}
	 	else if((nameCheckout.length==0) && (deviceCheckout.length==0)){

			Map <String, String> Success = new HashMap <String, String>();
			Success.put("DeviceID",deviceID);
			Success.put("Username",username);
			jsonObj_newUser.put("Success", Success);

			datauser.insertDB("Users(Username,Password)", "('"+username+"','"+password+"')");
			//TimeZone,DeviceStatus為預設
			datauser.insertDB("Devices(DeviceID,Username,DeviceName,DevicePhoneNumber,TimeZone,DeviceStatus)", "('"+deviceID+"','"+username+"','"+deviceName+"','"+
			devicePhoneNumber+"','-1300:0','0')");
		}
		
		datauser.closeDB();

	return jsonObj_newUser.toString();}

	public String checkUser(String username , String password) throws JSONException {

		//比對使用者資料
		datauser.loginDB("etc/backend.example.properties");

		String[][] nameCheckout=datauser.selectDB("Username, Password", "Users", "where Username Like '"+username+"'", " ");
		String[][] allCheckout=datauser.selectDB("Username, Password", "Users", "where Username Like '"+username+"' and Password Like '"+password+"'", " ");

		//為了建構輸出的資料  宣告JSONObject
		JSONObject jsonObj_checkUser = new JSONObject();

		if((allCheckout.length==0) && (nameCheckout.length==0)){
			jsonObj_checkUser.put("Fail", "No User Existed");}
		else if((allCheckout.length==0) && (nameCheckout.length!=0)){
			jsonObj_checkUser.put("Fail", "Password Error");}
		else{
			String[][] deveceData=datauser.selectDB("DeviceID, DeviceName, DevicePhoneNumber", "Devices", "where Username Like '"+username+"'", " ");

			Map <String, String> Success = new HashMap <String, String>();
			Success.put("DeviceID",deveceData[0][0]);
			Success.put("DeviceName",deveceData[0][1]);
			Success.put("DevicePhoneNumber",deveceData[0][2]);
			jsonObj_checkUser.put("Success", Success);
		}

		datauser.closeDB();

	return jsonObj_checkUser.toString();}

}
