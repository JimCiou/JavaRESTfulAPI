package me.rerun.karafcxf.service.impl.example.data;

public class BS {

	//基本變數
	String stationID;
	//位置變數
	String lat;
	String lng;

	//Constructor
	public BS(String stationID){this.stationID=stationID;}
	//可存入之變數 位置變數
	public void setLat(String lat){this.lat=lat;}
	public void setLng(String lng){this.lng=lng;}

	//可取出之變數 基本變數
	public String getStationID(){return this.stationID;}
	//可取出之變數 位置變數
	public String getLat(){return this.lat;}
	public String getLng(){return this.lng;}
}
