package me.rerun.karafcxf.service.impl.example;

import me.rerun.karafcxf.service.impl.example.system.*;
import me.rerun.karafcxf.service.impl.example.mqtt.*;
import me.rerun.karafcxf.service.impl.example.data.*;

import java.util.*;

import org.json.JSONObject;
import org.json.JSONException;
import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;

public class Falldown {

	DataBase datauser=new DataBase();
	MqttActions mqttsample= new MqttActions();
	Logger logger = Logger.getLogger(Falldown.class);

	public void setFalldownConfig(Device device, String state, String identifier_initial) throws JSONException {

		//example "FDALM,ON,0#"
		//example "FDALM,OFF#"
		String instructionMQTT="FDALM,OFF#";
		if(state.equals("ON")){instructionMQTT="FDALM,ON,0#";}

		//為了建構MQTT輸出的資料  宣告JSONObject
		JSONObject jsonObj_message = new JSONObject();
	 	jsonObj_message.put("Socket",device.getSocket()).put("Instruction",instructionMQTT).put("Identifier",identifier_initial);
		mqttsample.MqttPublish("etc/backend.example.properties", "/adaptor/"+device.getAdaptorID(), jsonObj_message.toString());

		logger.warn("Publisher MQTT topic " + "/adaptor/"+device.getAdaptorID());
		logger.warn("Publisher MQTT message" + jsonObj_message);
	}

	public String setFalldownDB(Device device, String state) throws JSONException {

		/*NO DB*/
		//為了建構輸出的資料  宣告JSONObject
		JSONObject jsonObj_setFall = new JSONObject();
		Map <String, String> Success = new HashMap <String, String>();
		Success.put("DeviceID",device.getDeviceID());
		jsonObj_setFall.put("Success", Success);

	return jsonObj_setFall.toString();}

}
