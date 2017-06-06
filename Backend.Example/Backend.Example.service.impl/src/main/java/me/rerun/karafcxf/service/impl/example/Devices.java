package me.rerun.karafcxf.service.impl.example;

import me.rerun.karafcxf.service.impl.example.system.*;
import me.rerun.karafcxf.service.impl.example.mqtt.*;
import me.rerun.karafcxf.service.impl.example.func.*;
import me.rerun.karafcxf.service.impl.example.data.*;

import java.util.*;

import java.sql.Timestamp;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;

public class Devices {

	DataBase datauser=new DataBase();
	MqttActions mqttsample= new MqttActions();
	LBS lbs_user=new LBS();
	Time timeGMT=new Time();
	Logger logger = Logger.getLogger(Devices.class);

	public String getNowGPS(Device device) throws JSONException {

		boolean catchDB=false;

		//確認Object內有無資料,位置資訊
		if(device.getTime()==null){catchDB=true;}
		if(device.getLat()==null){catchDB=true;}
		if(device.getLng()==null){catchDB=true;}
		if(device.getLocationType()==null){catchDB=true;}
		//確認Object內有無資料,Device資訊
		if(device.getVoltageLevel()==null){catchDB=true;}
		if(device.getGSMLevel()==null){catchDB=true;}
		if(device.getDeviceStatus()==null){catchDB=true;}
		if(device.getTimezone()==null){catchDB=true;}
		if(device.getDeviceName()==null){catchDB=true;}
		if(device.getDevicePhoneNumber()==null){catchDB=true;}
		if(device.getDeviceModel()==null){catchDB=true;}
		if(device.getLanguage()==null){catchDB=true;}
		if(device.getIMSI()==null){catchDB=true;}

		if(catchDB){
			logger.warn("Device data need to catch DB");
			datauser.loginDB("etc/backend.example.properties");
			//取得位置資訊並存入Object
			String[][] time_last_GPS=datauser.selectDB("Time, Lat, Lng, LocationType", "Locations", "where Time = (select max(Time) as maxtime from Locations where DeviceID = '"+
			device.getDeviceID()+"')", " ");
			if(time_last_GPS.length!=0){
				device.setTime(time_last_GPS[0][0]);
				device.setLat(time_last_GPS[0][1]);
				device.setLng(time_last_GPS[0][2]);
				device.setLocationType(time_last_GPS[0][3]);
				//暫定
				device.setLocationID("00000000");}
			//取得Device資訊並存入Object
			String[][] device_state=datauser.selectDB("VoltageLevel, GSMLevel, TimeZone, DeviceName, DevicePhoneNumber, DeviceStatus", "Devices", "where DeviceID = '"+
			device.getDeviceID()+"'", " ");
			if(device_state.length!=0){			
				device.setVoltageLevel(device_state[0][0]);
				device.setGSMLevel(device_state[0][1]);
				device.setTimezone(device_state[0][2]);
				device.setDeviceName(device_state[0][3]);
				device.setDevicePhoneNumber(device_state[0][4]);
				device.setDeviceStatus(device_state[0][5]);
				//暫定
				device.setDeviceModel("0000");
				device.setLanguage("0");
				device.setIMSI("0000000000000000");}
			datauser.closeDB();
		}		

		String connected="0";
		if(device.getSocket()!=null){connected="1";}

		//為了建構輸出的總資料  宣告JSONObject 
		JSONObject jsonObj_nowGPS = new JSONObject("{'DeviceID':'"+device.getDeviceID()+"'}");
		jsonObj_nowGPS.put("Connected", connected).put("VoltageLevel", device.getVoltageLevel()).put("GSMLevel", device.getGSMLevel())
		.put("Timezone", device.getTimezone()).put("DeviceName", device.getDeviceName()).put("DevicePhoneNumber", device.getDevicePhoneNumber())
		.put("DeviceStatus", device.getDeviceStatus()).put("DeviceModel", device.getDeviceModel()).put("Language", device.getLanguage());

		//已經有接收位置資料才回應LastLocation
		if(device.getTime()!=null){
			Map <String, String> LastLocation = new HashMap <String, String>();
			LastLocation.put("Time",device.getTime());
			//Same to Time
			LastLocation.put("UTCTime",device.getTime());
			LastLocation.put("Lat",device.getLat());
			LastLocation.put("Lng",device.getLng());
			LastLocation.put("LocationType",device.getLocationType());
			LastLocation.put("LocationID",device.getLocationID());
			jsonObj_nowGPS.put("LastLocation", LastLocation);}

	return jsonObj_nowGPS.toString();}

	public String setTimeZone(String zone_data, Device device) throws JSONException {

		//為了接住輸入的資料  宣告JSONObject 
		JSONObject jsonObj_zone_data = new JSONObject(zone_data);
		String timezone_index = jsonObj_zone_data.get("Timezone").toString();

		boolean updateDB=false;

		if(!timezone_index.equals(device.getTimezone())){device.setTimezone(timezone_index);updateDB=true;}

		if(updateDB){
			logger.warn("Zone data need to update DB");
			datauser.loginDB("etc/backend.example.properties");
			datauser.updateDB("Devices", "TimeZone = '"+timezone_index+"'", "DeviceID = "+device.getDeviceID());
			datauser.closeDB();
		}
		String[] timezone_indextokens=timezone_index.split(":");

		//為了建構輸出的資料  宣告JSONObject
		JSONObject jsonObj_setTimeZone = new JSONObject();
		Map <String, String> Success = new HashMap <String, String>();
		Success.put("DeviceID",device.getDeviceID());
		Success.put("Timezone",timezone_indextokens[0]);
		Success.put("Tzindex",timezone_indextokens[1]);
		jsonObj_setTimeZone.put("Success", Success);

	return jsonObj_setTimeZone.toString();}

	public String orderDeviceDown(Device device, String identifier_initial) throws JSONException {

		//example "SHUTDOWN#"
		String instructionMQTT="SHUTDOWN#";
		//為了建構MQTT輸出的資料  宣告JSONObject
		JSONObject jsonObj_message = new JSONObject();
	 	jsonObj_message.put("Socket",device.getSocket()).put("Instruction",instructionMQTT).put("Identifier",identifier_initial);
		mqttsample.MqttPublish("etc/backend.example.properties", "/adaptor/"+device.getAdaptorID(), jsonObj_message.toString());

		logger.warn("Publisher MQTT topic " + "/adaptor/"+device.getAdaptorID());
		logger.warn("Publisher MQTT message" + jsonObj_message);

		//為了建構輸出的資料  宣告JSONObject
		JSONObject jsonObj_orderDevice = new JSONObject();
		Map <String, String> Success = new HashMap <String, String>();
		Success.put("DeviceID",device.getDeviceID());
		jsonObj_orderDevice.put("Success", Success);

	    return jsonObj_orderDevice.toString();}

	public String putDataDevice(Device device, String device_data) throws JSONException {

		//為了接住輸入的資料  宣告JSONObject 
		JSONObject jsonObj_device_data = new JSONObject(device_data);

		String deviceStatus = jsonObj_device_data.get("DeviceStatus").toString();
		String voltageLevel = jsonObj_device_data.get("VoltageLevel").toString();
		String gsmLevel = jsonObj_device_data.get("GSMLevel").toString();

		boolean updateDB=false;

		if(!deviceStatus.equals(device.getDeviceStatus())){device.setDeviceStatus(deviceStatus);updateDB=true;}
		if(!voltageLevel.equals(device.getVoltageLevel())){device.setVoltageLevel(voltageLevel);updateDB=true;}
		if(!gsmLevel.equals(device.getGSMLevel())){device.setGSMLevel(gsmLevel);updateDB=true;}

		if(updateDB){
			logger.warn("Device data need to update DB");
			datauser.loginDB("etc/backend.example.properties");
			datauser.updateDB("Devices", "VoltageLevel = '"+voltageLevel+"', GSMLevel = '"+gsmLevel+"', DeviceStatus = '"+deviceStatus+"'", "DeviceID = "+device.getDeviceID());
			datauser.closeDB();
		}
		//為了建構輸出的資料  宣告JSONObject
		JSONObject jsonObj_putDevice = new JSONObject();
		Map <String, String> Success = new HashMap <String, String>();
		Success.put("DeviceID",device.getDeviceID());
		jsonObj_putDevice.put("Success", Success);

		return jsonObj_putDevice.toString();}

	public void putDeviceWarn(Device device, LBS lbser, String warn_data) throws JSONException {

		//為了接住輸入的資料  宣告JSONObject 
		JSONObject jsonObj_warn_data = new JSONObject(warn_data);

		JSONObject jsonLBS = (JSONObject)jsonObj_warn_data.getJSONArray("LBS").get(0);
		String ci = jsonLBS.get("CI").toString();
		String mnc = jsonLBS.get("MNC").toString();
		String mcc = jsonLBS.get("MCC").toString();
		String lac = jsonLBS.get("LAC").toString();

		String voltageLevel = jsonObj_warn_data.get("VoltageLevel").toString();
		String locationType = jsonObj_warn_data.get("LocationType").toString();
		String gsmLevel = jsonObj_warn_data.get("GSMLevel").toString();
		String deviceStatus = jsonObj_warn_data.get("DeviceStatus").toString();
		String warning = jsonObj_warn_data.get("Warning").toString();
		String geoFenceID = jsonObj_warn_data.get("GeoFenceID").toString();
		String fenceName ="Default";

		datauser.loginDB("etc/backend.example.properties");
		String[][] getFenceName=datauser.selectDB("FenceName", "Fences", "where DeviceID ='"+
				device.getDeviceID()+"' and GeoFenceID ='"+geoFenceID+"'", " ");		
		datauser.closeDB();
		if(getFenceName.length!=0) {fenceName=getFenceName[0][0];}

		device.setVoltageLevel(voltageLevel);
		device.setGSMLevel(gsmLevel);
		device.setDeviceStatus(deviceStatus);

		//為了建構MQTT輸出的資料  宣告JSONObject
		JSONObject jsonObj_message = new JSONObject();

		//先取Device的時間
	  	String timeparser=timeGMT.getDeviceTime(device);
		jsonObj_message.put("Time",timeparser);

		switch(Integer.parseInt(deviceStatus)){
			case 1:jsonObj_message.put("Class","Shake");break;
			case 2:jsonObj_message.put("Class","PowerOff");break;
			case 3:jsonObj_message.put("Class","Low Power");break;
			case 4:jsonObj_message.put("Class","SOS");break;
			case 5:jsonObj_message.put("Class","GeoFence").put("Status","In").put("GeoFenceID",geoFenceID).put("Name",fenceName);break;
			case 6:jsonObj_message.put("Class","GeoFence").put("Status","Out").put("GeoFenceID",geoFenceID).put("Name",fenceName);break;
			case 7:jsonObj_message.put("Class","Fall");break;
			default:jsonObj_message.put("Class","Normal");
		}

		mqttsample.MqttPublish("etc/backend.example.properties", "/device/"+device.getDeviceID(), jsonObj_message.toString());

		logger.warn("Publisher MQTT topic " + "/device/"+device.getDeviceID());
		logger.warn("Publisher MQTT message" + jsonObj_message);

		String rssi="0";//暫定

		datauser.loginDB("etc/backend.example.properties");
		//存入DB & Object
		datauser.updateDB("Devices", "VoltageLevel = '"+voltageLevel+"', GSMLevel = '"+gsmLevel+"', DeviceStatus ='"+deviceStatus+"'", "DeviceID = "+device.getDeviceID());
		device.setDeviceStatus(deviceStatus);
		device.setVoltageLevel(voltageLevel);
		device.setGSMLevel(gsmLevel);
		//存入DB
		datauser.insertDB("LBS(DeviceID,Time,MCC,MNC,LAC,CI,RSSI)", "('"+device.getDeviceID()+"','"+timeparser+"','"+mcc+"','"+mnc+"','"+lac+"','"+ci+"','"+rssi+"')");

		//若為收到封包0x16 存入GPS座標
		if((jsonObj_warn_data.toString().indexOf("Lat")!=-1) && (jsonObj_warn_data.toString().indexOf("Lng")!=-1)){
			String lat = jsonObj_warn_data.get("Lat").toString();
			String lng = jsonObj_warn_data.get("Lng").toString();
			String speed = jsonObj_warn_data.get("Speed").toString();
			//存入DB & Object
			datauser.insertDB("Locations(DeviceID,Time,Lat,Lng,LocationType)", "('"+device.getDeviceID()+"','"+timeparser+"','"+lat+"','"+lng+"','0')");
			device.setLat(lat);
			device.setLng(lng);
			device.setTime(timeparser);
			device.setLocationType("0");}
		else{   //若收到封包0x19 存入LBS座標
			try {lbser.setCellInfo(mcc, mnc, lac, ci, rssi);}
			catch(Exception e){System.err.println("Error: " + e.getMessage());}
			String[] LocationfromLBS=lbser.getLocationfromLBS();
			//存入DB & Object
			datauser.insertDB("Locations(DeviceID,Time,Lat,Lng,LocationType)", "('"+device.getDeviceID()+"','"+timeparser+"','"+LocationfromLBS[0]+"','"+LocationfromLBS[1]+"','1')");
			device.setLat(LocationfromLBS[0]);
			device.setLng(LocationfromLBS[1]);
			device.setTime(timeparser);
			device.setLocationType("1");}
		datauser.closeDB();
	}

	public String putDeviceSet(Device device, String response_data) throws JSONException {

		//為了接住輸入的資料  宣告JSONObject 
		JSONObject jsonObj_response_data = new JSONObject(response_data);
		String identifier = jsonObj_response_data.get("Identifier").toString();
		String instruction = jsonObj_response_data.get("Instruction").toString();

		//收到封包0x81,為登入用途, 其他都為"回饋用途"
		if(identifier.equals("00000000")){

			datauser.loginDB("etc/backend.example.properties");
			//收到封包0x81,為登入用途,不帶fences
			if(instruction.indexOf("ALLGFENCES")==-1){

				int iniSOS=instruction.indexOf("SOS");
				int endSOS=instruction.indexOf(";",iniSOS);

				String sosInstruction=instruction.substring(iniSOS,endSOS);
				String[] sosInstructionToken=sosInstruction.split("SOS=|,");

				for(int i=1;i<4;i++){
					//Device中無此phone
					if(i>=sosInstructionToken.length){datauser.deleteDB("SOS_phones","DeviceID ='"+device.getDeviceID()+"' and PhoneID ='"+i+"'");}
					//Device中有此phone
					else{
						//DataBase中無此phone,新增
						if((datauser.selectDB("PhoneID", "SOS_phones", "where PhoneID ='"+i+"'and DeviceID ='"+device.getDeviceID()+"'", " ").length)==0){
							datauser.insertDB("SOS_phones(DeviceID,PhoneID,Phone)", "('"+device.getDeviceID()+"','"+i+"','"+sosInstructionToken[i]+"')");}
						//DataBase中有此phone,更新
						datauser.updateDB("SOS_phones", "Phone = '"+sosInstructionToken[i]+"'",
						"DeviceID = "+device.getDeviceID()+" and PhoneID ="+i);}
				}
			}
			//收到封包0x81,為登入用途,帶fences
			else{

				int inifence=instruction.indexOf("ALLGFENCES:");
				int endfence=instruction.indexOf("#",inifence);

				String fenceInstruction=instruction.substring(inifence,endfence);
				String[] fenceInstructionToken=fenceInstruction.split("ALLGFENCES:|;|#");

				for(int i=1;i<fenceInstructionToken.length;i++){
					String[] fenceToken=fenceInstructionToken[i].split(",");
					//Device中無此fence
					if(fenceToken[1].equals("0")){datauser.deleteDB("Fences","DeviceID ='"+device.getDeviceID()+"' and GeoFenceID ='"+fenceToken[0]+"'");}
					//Device中有此fence
					else{
						//DataBase中無此fence,新增,名稱為Default
						if((datauser.selectDB("GeoFenceID", "Fences", "where GeoFenceID ='"+fenceToken[0]+"'and DeviceID ='"+device.getDeviceID()+"'", " ").length)==0){
							datauser.insertDB("Fences(GeoFenceID,DeviceID,Lat,Lng,Radius,AlarmStatus,AlarmType,AlarmMode,FenceName)", 
							"('"+fenceToken[0]+"','"+device.getDeviceID()+"','"+fenceToken[5]+"','"+fenceToken[6]+"','"+fenceToken[7]+
							"','"+"1"+"','"+fenceToken[8]+"','"+fenceToken[9]+"','Default')");}
						//DataBase中有此fence,更新
						datauser.updateDB("Fences", "Lat = '"+fenceToken[5]+"', Lng = '"+fenceToken[6]+"', Radius = '"+fenceToken[7]+
						"', AlarmStatus = '"+"1"+"', AlarmType = '"+fenceToken[8]+"', AlarmMode = '"+fenceToken[9]+"'",
						"DeviceID = "+device.getDeviceID()+" and GeoFenceID ="+fenceToken[0]);}
				}

			}
			datauser.closeDB();
		}
		//"回饋用途"
		else{putDeviceConfigDB(device.getDeviceID(), identifier, instruction);}
	return identifier;}

	public void putDeviceConfigDB(String deviceID, String identifier, String instruction) {
		//"回饋用途"Non-完成
	}

}
