package me.rerun.karafcxf.service.impl.example.func;

import me.rerun.karafcxf.service.impl.example.system.*;
import me.rerun.karafcxf.service.impl.example.data.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import java.util.*;
import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;

public class LBS {

	DataBase datauser=new DataBase();
	Hashtable<String,BS> bs_Array = new Hashtable<String,BS>();

	LinkedList<String> latlist = new LinkedList<String>(); //lat list
	LinkedList<String> lnglist = new LinkedList<String>(); //lng list
	LinkedList<Integer> rssilist = new LinkedList<Integer>(); //rssi list
	Logger logger = Logger.getLogger(LBS.class);

	public void setCellInfo(String mcc, String mnc, String lac, String ci, String rssi) throws Exception {

		String stationID=mcc+"-"+mnc+"-"+lac+"-"+ci;
		//先取記憶體,沒有則創造一個
		BS bs=bs_Array.get(stationID);
		if(bs==null){
			bs_Array.put(stationID,new BS(stationID));
			bs=bs_Array.get(stationID);}

		boolean catchDB=false;
		//存入比較List
		if(bs.getLat()==null){latlist.offer(bs.getLat());catchDB=true;}
		if(bs.getLng()==null){lnglist.offer(bs.getLng());catchDB=true;}

		if(catchDB){
			logger.warn("BS data need to catch DB");
			datauser.loginDB("etc/backend.tracker.properties");
			String[][] station_check=datauser.selectDB("StationID, Lat, Lng", "BSinfo", "where StationID ='"+stationID+"'"," ");
			//DataBase有的基地台
			if(station_check.length!=0){
				//存入比較List & 記憶體
				rssilist.offer(Integer.parseInt(rssi));
				latlist.offer(station_check[0][1]);
				lnglist.offer(station_check[0][2]);
				bs.setLat(station_check[0][1]);
				bs.setLng(station_check[0][2]);}
			//DB沒有基地台資訊  上網找基地台 有找到才存入list
			else{	
				logger.warn("BS data need to catch Network");
				LBS http = new LBS();
				JSONObject cell_jsonObj = new JSONObject(http.getCellLocation(mcc,mnc,lac,ci));
				if(cell_jsonObj.get("errcode").toString().equals("0")){
					logger.warn("Get cell locations success!!");
					String lat=cell_jsonObj.get("lat").toString();
					String lng=cell_jsonObj.get("lon").toString(); //lon=lng
					//存入比較DB & List & 記憶體 
					datauser.insertDB("BSinfo(StationID,Lat,Lng)", "('"+stationID+"','"+lat+"','"+lng+"')");
					rssilist.offer(Integer.parseInt(rssi));
	                		latlist.offer(lat);
	                		lnglist.offer(lng);
					bs.setLat(lat);
					bs.setLng(lng);}
				else{logger.warn("Get cell locations from Network fail!!");}
			}
			datauser.closeDB();
		}

	}

	public String[] getLocationfromLBS() {

		//給一個預設 不可能同時看到1000個基地台
		int index_minRSSI=1000;
		//先取功率最大的基地台座標作為Tracker座標(RSSI為Nagitive 範圍1~255) 取最小
		try{index_minRSSI=rssilist.indexOf(Collections.min(rssilist));}
		catch(Exception e){System.err.println("Error: " + e.getMessage());}

		String lat=null;
		String lng=null;
		if(index_minRSSI!=1000){
			lat=latlist.get(index_minRSSI);
			lng=lnglist.get(index_minRSSI);}

		logger.warn("The closest Cell is at "+lat+" : "+lng);

		String[] LocationfromLBS={lat,lng};

		latlist.clear();
		lnglist.clear();
		rssilist.clear();

	return LocationfromLBS;}

	//Http cell location request
	public String getCellLocation(String mcc, String mnc, String lac, String ci) throws Exception {

		String url = "http://api.cellocation.com/cell/?mcc="+mcc+"&mnc="+mnc+"&lac="+lac+"&ci="+ci+"&output=json";
		logger.warn("Try to find BS location from: "+url);

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		int responseCode = con.getResponseCode();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}
}
