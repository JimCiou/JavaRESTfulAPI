package me.rerun.karafcxf.service.impl.example;

import me.rerun.karafcxf.service.impl.example.system.*;
import me.rerun.karafcxf.service.impl.example.func.*;
import me.rerun.karafcxf.service.impl.example.data.*;

import java.util.*;

import java.sql.Timestamp;
import org.json.JSONObject;
import org.json.JSONException;
import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;

public class Locations {

	DataBase datauser=new DataBase();
	Time timeGMT=new Time();
	Logger logger = Logger.getLogger(Locations.class);

	public String getIntervalGPS(String deviceID, String time_interval) throws JSONException {

		//為了接住輸入的資料  宣告JSONObject 
		JSONObject jsonObj_time_interval = new JSONObject(time_interval);
		String begin_time = jsonObj_time_interval.get("Start").toString();
		String end_time = jsonObj_time_interval.get("End").toString();

		datauser.loginDB("etc/backend.example.properties");
		String[][] Time_interval_GPS = datauser.selectDB("Time, Lat, Lng, LocationType", "Locations", "where DeviceID = '"+deviceID+"' AND Time between '"+begin_time+
		"' and '"+end_time+"'", " ");
		datauser.closeDB();

		//為了建構輸出的總資料  宣告JSONObject
		JSONObject jsonObj_historyGPS = new JSONObject();
		List<JSONObject> locationList= new LinkedList<JSONObject>();

		for(int i=0; i<Time_interval_GPS.length; i++){
			//為了建構輸出的子資料  宣告JSONObject
			JSONObject jsonObj_timeGps = new JSONObject();
			//UTCTime same to Time
			jsonObj_timeGps.put("UTCTime", Time_interval_GPS[i][0]).put("Time", Time_interval_GPS[i][0]).
			put("Lat", Time_interval_GPS[i][1]).put("Lng", Time_interval_GPS[i][2]).put("LocationType", Time_interval_GPS[i][3]);
			locationList.add(jsonObj_timeGps);
		}
		jsonObj_historyGPS.put("Locations", locationList);

	return jsonObj_historyGPS.toString();}

	public String putDataGPS(Device device, LBS lbser, String gps_data) throws JSONException {

		//為了接住輸入的資料  宣告JSONObject 
		JSONObject jsonObj_gps_data = new JSONObject(gps_data);
		String locationType = jsonObj_gps_data.get("LocationType").toString();

		//先取Device的時間
	  	String timeparser=timeGMT.getDeviceTime(device);

		datauser.loginDB("etc/backend.example.properties");
		switch(Integer.parseInt(locationType)){
			case 0://收到封包0x10
				String lat = jsonObj_gps_data.get("Lat").toString();
				String lng = jsonObj_gps_data.get("Lng").toString();
				String speed = jsonObj_gps_data.get("Speed").toString();//還沒用到
				//存入DB & Object
				datauser.insertDB("Locations(DeviceID,Time,Lat,Lng,LocationType)", "('"+device.getDeviceID()+"','"+timeparser+"','"+lat+"','"+lng+"','0')");
				device.setLat(lat);
				device.setLng(lng);
				device.setTime(timeparser);
				device.setLocationType("0");
			break;
			case 1://收到封包0x17 0x18
				String mcc = null;
				String mnc = null;
				for(int i=0;i<jsonObj_gps_data.getJSONArray("LBS").length();i++){
					JSONObject jsonLBS = (JSONObject)jsonObj_gps_data.getJSONArray("LBS").get(i);
					if(i==0){
						mcc = jsonLBS.get("MCC").toString();
						mnc = jsonLBS.get("MNC").toString();
					}
					String lac = jsonLBS.get("LAC").toString();
					String ci = jsonLBS.get("CI").toString();
					String rssi = jsonLBS.get("RSSI").toString();
					if(!lac.equals("0") && !ci.equals("0") && !rssi.equals("0")){					
						datauser.insertDB("LBS(DeviceID,Time,MCC,MNC,LAC,CI,RSSI)", "('"+device.getDeviceID()+"','"+
						timeparser+"','"+mcc+"','"+mnc+"','"+lac+"','"+ci+"','"+rssi+"')");
						try {lbser.setCellInfo(mcc, mnc, lac, ci, rssi);
						}catch(Exception e){System.err.println("Error: " + e.getMessage());}
					}
				}
				String[] LocationfromLBS=lbser.getLocationfromLBS();
				if(LocationfromLBS[0]!=null && LocationfromLBS[1]!=null){
					//存入DB & Object
					datauser.insertDB("Locations(DeviceID,Time,Lat,Lng,LocationType)", "('"+device.getDeviceID()+"','"+timeparser+"','"+
					LocationfromLBS[0]+"','"+LocationfromLBS[1]+"','1')");}
					device.setLat(LocationfromLBS[0]);
					device.setLng(LocationfromLBS[1]);
					device.setTime(timeparser);
					device.setLocationType("1");
			break;
		}
		datauser.closeDB();
		//為了建構輸出的資料  宣告JSONObject
		JSONObject jsonObj_putGPS = new JSONObject();
		Map <String, String> Success = new HashMap <String, String>();
		Success.put("DeviceID",device.getDeviceID());
		jsonObj_putGPS.put("Success", Success);

	return jsonObj_putGPS.toString();}
}
