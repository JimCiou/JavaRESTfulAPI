package me.rerun.karafcxf.service.impl.example;

import me.rerun.karafcxf.service.impl.example.system.*;
import me.rerun.karafcxf.service.impl.example.mqtt.*;
import me.rerun.karafcxf.service.impl.example.data.*;

import java.util.*;

import org.json.JSONObject;
import org.json.JSONException;
import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;

public class Fences {

	DataBase datauser=new DataBase();
	MqttActions mqttsample= new MqttActions();
	Logger logger = Logger.getLogger(Fences.class);

	public void setGeoFenceConfig(Device device, String fence_data, String identifier_initial) throws JSONException {

		//為了接住輸入的資料  宣告JSONObject 
		JSONObject jsonObj_fence_data = new JSONObject(fence_data);
		String geoFenceID = jsonObj_fence_data.get("GeoFenceID").toString();
		String lat = jsonObj_fence_data.get("Lat").toString();
		String lng = jsonObj_fence_data.get("Lng").toString();
		String radius = jsonObj_fence_data.get("Radius").toString();
		String fencename = jsonObj_fence_data.get("Name").toString();

		//預設的Fence設定
		String alarmStatus = "0";
		String alarmType = "3";
		String alarmMode = "0";
		try {
			alarmStatus = jsonObj_fence_data.get("AlarmStatus").toString();
			alarmType = jsonObj_fence_data.get("AlarmType").toString();
			alarmMode = jsonObj_fence_data.get("AlarmMode").toString();
		}catch(Exception e){
			logger.warn("Error: " + e.getMessage());}

		//MQTT default Instruction
		String alarmStatusMQTT="OFF";
		String alarmTypeMQTT="";
		String alarmModeMQTT="1";
		String latMQTT="Default";
		String lngMQTT="Default";

		if(alarmStatus.equals("1")){alarmStatusMQTT="ON";}
		if(alarmType.equals("1")){alarmTypeMQTT="IN";}
		if(alarmType.equals("2")){alarmTypeMQTT="OUT";}
		if(alarmMode.equals("0")){alarmModeMQTT="0";}

		//instructionMQTT="GFENCES,3,ON,0,+22.33,+120.22,40,IN,1,#";//example
		//instructionMQTT="GFENCES,3,ON,0,+22.33,+120.22,40,,1,#";//example2

		if(Double.parseDouble(lat)>=0){latMQTT="+"+lat;}else{latMQTT="-"+lat;}
		if(Double.parseDouble(lng)>=0){lngMQTT="+"+lng;}else{lngMQTT="-"+lng;}
		String instructionMQTT="GFENCE,"+geoFenceID+","+alarmStatusMQTT+",0,"+latMQTT+","+lngMQTT+","+radius+","+alarmTypeMQTT+","+alarmModeMQTT+",#";

		//為了建構MQTT輸出的資料  宣告JSONObject
		JSONObject jsonObj_message = new JSONObject();
 		jsonObj_message.put("Socket",device.getSocket()).put("Instruction",instructionMQTT).put("Identifier",identifier_initial);
		mqttsample.MqttPublish("etc/backend.example.properties", "/adaptor/"+device.getAdaptorID(), jsonObj_message.toString());

		logger.warn("Publisher MQTT topic " + "/adaptor/"+device.getAdaptorID());
		logger.warn("Publisher MQTT message" + jsonObj_message);
	}

	public String setGeoFenceDB(Device device, String fence_data) throws JSONException {

		//為了接住輸入的資料  宣告JSONObject 
		JSONObject jsonObj_fence_data = new JSONObject(fence_data);
		String geoFenceID = jsonObj_fence_data.get("GeoFenceID").toString();
		String lat = jsonObj_fence_data.get("Lat").toString();
		String lng = jsonObj_fence_data.get("Lng").toString();
		String radius = jsonObj_fence_data.get("Radius").toString();
		String fencename = jsonObj_fence_data.get("Name").toString();

		//預設的Fence設定
		String alarmStatus = "0";
		String alarmType = "3";
		String alarmMode = "0";
		try {
			alarmStatus = jsonObj_fence_data.get("AlarmStatus").toString();
			alarmType = jsonObj_fence_data.get("AlarmType").toString();
			alarmMode = jsonObj_fence_data.get("AlarmMode").toString();
		}catch(Exception e){
			logger.warn("Error: " + e.getMessage());}

		datauser.loginDB("etc/backend.example.properties");
		String[][] getFenceOUT=datauser.selectDB("GeoFenceID, Lat, Lng, Radius, AlarmStatus, AlarmType, AlarmMode", "Fences", "where DeviceID ='"+
		device.getDeviceID()+"' and GeoFenceID ='"+geoFenceID+"'", " ");
        	String[][] getFenceQuantityOUT=datauser.selectDB("GeoFenceID", "Fences", "where DeviceID ='"+device.getDeviceID()+"'", " ");

		//為了建構輸出的資料  宣告JSONObject
		JSONObject jsonObj_newGeoFence = new JSONObject();
		if((getFenceOUT.length!=0) && (Integer.parseInt(geoFenceID)<=5)){//這個fence已經存在,做更新		
			datauser.updateDB("Fences", "Lat = '"+lat+"', Lng = '"+lng+"', Radius = '"+radius+
			"', AlarmStatus = '"+alarmStatus+"', AlarmType = '"+alarmType+"', AlarmMode = '"+alarmMode+"', FenceName = '"+fencename+"'",
			"DeviceID = "+device.getDeviceID()+" and GeoFenceID ="+geoFenceID);
			Map <String, String> Success = new HashMap <String, String>();
			Success.put("GeoFenceID",geoFenceID);
			jsonObj_newGeoFence.put("Success", Success);}
		else if((getFenceOUT.length==0) && (getFenceQuantityOUT.length<5) && (Integer.parseInt(geoFenceID)<=5)){//這個fence並不存在,且這deviceID設定不到5個fence				
			datauser.insertDB("Fences(GeoFenceID,DeviceID,Lat,Lng,Radius,AlarmStatus,AlarmType,AlarmMode,FenceName)", "('"+geoFenceID+"','"+device.getDeviceID()+
			"','"+lat+"','"+lng+"','"+radius+"','"+alarmStatus+"','"+alarmType+"','"+alarmMode+"','"+fencename+"')");
			Map <String, String> Success = new HashMap <String, String>();
			Success.put("GeoFenceID",geoFenceID);
			jsonObj_newGeoFence.put("Success", Success);}
		else{
			jsonObj_newGeoFence.put("Fail", "Illegal GeoFenceID");}
		datauser.closeDB();
	return jsonObj_newGeoFence.toString();}

	public void getGeoFenceConfig(Device device, String identifier_initial) throws JSONException {

		//為了建構MQTT輸出的資料  宣告JSONObject
		JSONObject jsonObj_message = new JSONObject();
 		jsonObj_message.put("Socket",device.getSocket()).put("Instruction","GFENCE#").put("Identifier",identifier_initial);
		mqttsample.MqttPublish("etc/backend.example.properties", "/adaptor/"+device.getAdaptorID(), jsonObj_message.toString());

		logger.warn("Publisher MQTT topic " + "/adaptor/"+device.getAdaptorID());
		logger.warn("Publisher MQTT message" + jsonObj_message);
	}

	public String getGeoFenceDB(Device device, String fence_id, boolean config_result) throws JSONException {

		//為了建構輸出的總資料  宣告JSONObject
		JSONObject jsonObj_allFence = new JSONObject();
		List<JSONObject> geoFenceList= new LinkedList<JSONObject>();

		//預設取全部
		String geoFenceID = "All";
		try {
			JSONObject jsonObj_fence_id = new JSONObject(fence_id);
			geoFenceID = jsonObj_fence_id.get("GeoFenceID").toString();
		}catch(Exception e){
			logger.warn("Error: " + e.getMessage());}

		//取得DataBase資料
		datauser.loginDB("etc/backend.example.properties");
		if(geoFenceID.equals("All")){
			String[][] getFenceOUT=datauser.selectDB("GeoFenceID, Lat, Lng, Radius, AlarmStatus, AlarmType, AlarmMode, FenceName", "Fences", "where DeviceID ='"+
			device.getDeviceID()+"'", " ");
			for(int i=0;i<getFenceOUT.length;i++){
				JSONObject jsonObj_fence = new JSONObject();
				jsonObj_fence.put("GeoFenceID", getFenceOUT[i][0]).put("Lat", getFenceOUT[i][1]).put("Lng", getFenceOUT[i][2]).put("Radius", getFenceOUT[i][3]).
				put("AlarmStatus", getFenceOUT[i][4]).put("AlarmType", getFenceOUT[i][5]).put("AlarmMode", getFenceOUT[i][6]).put("Name", getFenceOUT[0][7]);
				geoFenceList.add(jsonObj_fence);
			}
			jsonObj_allFence.put("GeoFences", geoFenceList);
		}
		else{
			String[] geoFenceID_token=geoFenceID.split(",");
			for(int i=0;i<geoFenceID_token.length;i++){
				String[][] getFenceOUT=datauser.selectDB("GeoFenceID, Lat, Lng, Radius, AlarmStatus, AlarmType, AlarmMode, FenceName", "Fences", "where DeviceID ='"+
				device.getDeviceID()+"' and GeoFenceID ='"+geoFenceID_token[i]+"'", " ");
				if(getFenceOUT.length!=0)//只輸出有取得的資料
				{
					JSONObject jsonObj_fence = new JSONObject();
					jsonObj_fence.put("GeoFenceID", getFenceOUT[0][0]).put("Lat", getFenceOUT[0][1]).put("Lng", getFenceOUT[0][2]).put("Radius", getFenceOUT[0][3]).
					put("AlarmStatus", getFenceOUT[0][4]).put("AlarmType", getFenceOUT[0][5]).put("AlarmMode", getFenceOUT[0][6]).put("Name", getFenceOUT[0][7]);
					geoFenceList.add(jsonObj_fence);
				}
			}
			jsonObj_allFence.put("GeoFences", geoFenceList);
		}
		datauser.closeDB();
		if(!config_result){jsonObj_allFence.put("Data", "OnlyfromDB");}

	return jsonObj_allFence.toString();}

	public void deleteGeoFenceConfig(Device device, String geoFenceID, String identifier_initial) throws JSONException {

		//instructionMQTT="GFENCES,3,OFF,#";//example
		//MQTT Instruction
		String instructionMQTT="GFENCE,"+geoFenceID+",OFF,#";
		//為了建構MQTT輸出的資料  宣告JSONObject
		JSONObject jsonObj_message = new JSONObject();
	 	jsonObj_message.put("Socket",device.getSocket()).put("Instruction",instructionMQTT).put("Identifier",identifier_initial);
		mqttsample.MqttPublish("etc/backend.example.properties", "/adaptor/"+device.getAdaptorID(), jsonObj_message.toString());

		logger.warn("Publisher MQTT topic " + "/adaptor/"+device.getAdaptorID());
		logger.warn("Publisher MQTT message" + jsonObj_message);
	}

	public String deleteGeoFenceDB(Device device, String geoFenceID) throws JSONException {

		String[] geoFenceID_token=geoFenceID.split(",");

		JSONObject jsonObj_deleteFenceOUT = new JSONObject();
		JSONObject jsonObj_deleteFence = new JSONObject();

		//刪除DataBase中的資料
		datauser.loginDB("etc/backend.example.properties");
		for(int i=0;i<geoFenceID_token.length;i++){datauser.deleteDB("Fences","DeviceID ='"+device.getDeviceID()+"' and GeoFenceID ='"+geoFenceID_token[i]+"'");}
		datauser.closeDB();

		jsonObj_deleteFence.put("GeoFenceID",geoFenceID);
		jsonObj_deleteFenceOUT.put("Success",jsonObj_deleteFence);

	return jsonObj_deleteFenceOUT.toString();}
}
