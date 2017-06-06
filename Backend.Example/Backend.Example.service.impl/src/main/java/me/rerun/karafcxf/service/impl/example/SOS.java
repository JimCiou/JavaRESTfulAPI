package me.rerun.karafcxf.service.impl.example;

import me.rerun.karafcxf.service.impl.example.system.*;
import me.rerun.karafcxf.service.impl.example.mqtt.*;
import me.rerun.karafcxf.service.impl.example.data.*;

import java.util.*;

import org.json.JSONObject;
import org.json.JSONException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator; 

public class SOS {

	DataBase datauser=new DataBase();
	MqttActions mqttsample= new MqttActions();
	Logger logger = Logger.getLogger(SOS.class);

	public void setSOSNumberConfig(Device device, String number_phone, String identifier_initial) throws JSONException {

		//預設的Fence設定
		String number1 = "";
		String number2 = "";
		String number3 = "";
		//為了接住輸入的資料  宣告JSONObject 
		JSONObject jsonObj_number_data = new JSONObject(number_phone);

		try {number1 = jsonObj_number_data.get("1").toString();}catch(Exception e){logger.warn("Error: " + e.getMessage());}
		try {number2 = jsonObj_number_data.get("2").toString();}catch(Exception e){logger.warn("Error: " + e.getMessage());}
		try {number3 = jsonObj_number_data.get("3").toString();}catch(Exception e){logger.warn("Error: " + e.getMessage());}

		//MQTT Instruction
		String instructionMQTT="SOS,A";

		String[] phone_array={number1,number2,number3};

		for(int i=0;i<3;i++){
			if(phone_array[i].equals("")){instructionMQTT=instructionMQTT+",";}
			else{instructionMQTT=instructionMQTT+","+phone_array[i];}
		}

		//instructionMQTT="SOS,A,,,0912343556#";//example
		instructionMQTT=instructionMQTT+"#";

		//為了建構MQTT輸出的資料  宣告JSONObject
		JSONObject jsonObj_message = new JSONObject();
	 	jsonObj_message.put("Socket",device.getSocket()).put("Instruction",instructionMQTT).put("Identifier",identifier_initial);
		mqttsample.MqttPublish("etc/backend.example.properties", "/adaptor/"+device.getAdaptorID(), jsonObj_message.toString());

		logger.warn("Publisher MQTT topic " + "/adaptor/"+device.getAdaptorID());
		logger.warn("Publisher MQTT message" + jsonObj_message);
	}

	public String setSOSNumberDB(Device device, String number_phone) throws JSONException {

		//預設的Fence設定
		String number1 = "";
		String number2 = "";
		String number3 = "";

		//為了接住輸入的資料  宣告JSONObject 
		JSONObject jsonObj_number_data = new JSONObject(number_phone);

		try {number1 = jsonObj_number_data.get("1").toString();}catch(Exception e){logger.warn("Error: " + e.getMessage());}
		try {number2 = jsonObj_number_data.get("2").toString();}catch(Exception e){logger.warn("Error: " + e.getMessage());}
		try {number3 = jsonObj_number_data.get("3").toString();}catch(Exception e){logger.warn("Error: " + e.getMessage());}

		String[] phoneID_array={"1","2","3"};
		String[] phone_array={number1,number2,number3};

		JSONObject jsonObj_newSOSOUT = new JSONObject();
		JSONObject jsonObj_newSOS = new JSONObject();

		//MQTT Instruction
		String instructionMQTT="SOS,A";
		String phoneID_out ="";

		datauser.loginDB("etc/backend.example.properties");
		for(int i=0;i<3;i++){
			if(phone_array[i].equals("")){}
			else{
				String[][] checkPhoneOUT=datauser.selectDB("DeviceID, PhoneID", "SOS_phones", "where DeviceID ='"+device.getDeviceID()+"' and PhoneID = '"+phoneID_array[i]+"'", " ");
				if (checkPhoneOUT.length!=0){
					datauser.updateDB("SOS_phones", "Phone = '"+phone_array[i]+"'","DeviceID = "+device.getDeviceID()+" and PhoneID ="+phoneID_array[i]);
					phoneID_out=phoneID_out+":"+phoneID_array[i];}
				else{
					datauser.insertDB("SOS_phones(DeviceID,PhoneID,Phone)", "('"+device.getDeviceID()+"','"+phoneID_array[i]+"','"+phone_array[i]+"')");
					phoneID_out=phoneID_out+":"+phoneID_array[i];}
			}
		}
		datauser.closeDB();

		String[] phoneID_out_token=phoneID_out.split(":");
		jsonObj_newSOS.put("PhoneID", phoneID_out_token);
		jsonObj_newSOSOUT.put("Success", jsonObj_newSOS);

	return jsonObj_newSOSOUT.toString();}

	public void getSOSNumberConfig(Device device, String identifier_initial) throws JSONException {

		//為了建構MQTT輸出的資料  宣告JSONObject
		JSONObject jsonObj_message = new JSONObject();
	 	jsonObj_message.put("Socket",device.getSocket()).put("Instruction","SOS#").put("Identifier",identifier_initial);
		mqttsample.MqttPublish("etc/backend.example.properties", "/adaptor/"+device.getAdaptorID(), jsonObj_message.toString());

		logger.warn("Publisher MQTT topic " + "/adaptor/"+device.getAdaptorID());
		logger.warn("Publisher MQTT message" + jsonObj_message);
	}

	public String getSOSNumberDB(Device device, String phoneID, boolean config_result) throws JSONException {

		//為了建構輸出的總資料  宣告JSONObject
		JSONObject jsonObj_getSOS = new JSONObject();

		//DataBase中的資料
		datauser.loginDB("etc/backend.example.properties");
		if(phoneID==null){
			//Get ALL
			phoneID="1,2,3";

			String[] phoneID_token=phoneID.split(",");
			for(int i=0;i<phoneID_token.length;i++){
				String[][] getPhoneData=datauser.selectDB("DeviceID, PhoneID, Phone", "SOS_phones", "where PhoneID ='"+
				phoneID_token[i]+"' and DeviceID ='"+device.getDeviceID()+"'", " ");
				//有取得的資料
				if(getPhoneData.length!=0){
					jsonObj_getSOS.put(getPhoneData[0][1], getPhoneData[0][2]);}
				else{jsonObj_getSOS.put(phoneID_token[i],"");}
			}
		}
		else{	
			String[] phoneID_token=phoneID.split(",");

			for(int i=0;i<phoneID_token.length;i++){
				String[][] getPhoneData=datauser.selectDB("DeviceID, PhoneID, Phone", "SOS_phones", "where PhoneID ='"+
				phoneID_token[i]+"' and DeviceID ='"+device.getDeviceID()+"'", " ");
				//有取得的資料
				if(getPhoneData.length!=0){
					jsonObj_getSOS.put(getPhoneData[0][1], getPhoneData[0][2]);}
				else{jsonObj_getSOS.put(phoneID_token[i],"");}
			}
		}
		datauser.closeDB();
		if(!config_result){jsonObj_getSOS.put("Data", "OnlyfromDB");}

	return jsonObj_getSOS.toString();}

	public void deleteSOSNumberConfig(Device device, String phoneID, String identifier_initial) throws JSONException {

		//MQTT Instruction
		//instructionMQTT="SOS,D,0,0,0912343556#"????;//example
		String instructionMQTT="SOS,D,"+phoneID+"#";
		//為了建構MQTT輸出的資料  宣告JSONObject
		JSONObject jsonObj_message = new JSONObject();
	 	jsonObj_message.put("Socket",device.getSocket()).put("Instruction",instructionMQTT).put("Identifier",identifier_initial);
		mqttsample.MqttPublish("etc/backend.example.properties", "/adaptor/"+device.getAdaptorID(), jsonObj_message.toString());

		logger.warn("Publisher MQTT topic " + "/adaptor/"+device.getAdaptorID());
		logger.warn("Publisher MQTT message" + jsonObj_message);
	}

	public String deleteSOSNumberDB(Device device, String phoneID) throws JSONException {

		String[] phoneID_token=phoneID.split(",");
		//為了建構輸出的總資料  宣告JSONObject
		JSONObject jsonObj_deleteSOSOUT = new JSONObject();
		JSONObject jsonObj_deleteSOS = new JSONObject();
		//刪除DataBase中的資料
		datauser.loginDB("etc/backend.example.properties");
		for(int i=0;i<phoneID_token.length; i++){datauser.deleteDB("SOS_phones","DeviceID ='"+device.getDeviceID()+"' and PhoneID ='"+phoneID_token[i]+"'");}
		datauser.closeDB();

		jsonObj_deleteSOS.put("PhoneID",phoneID_token);
		jsonObj_deleteSOSOUT.put("Success",jsonObj_deleteSOS);

	return jsonObj_deleteSOSOUT.toString();}
}
