package me.rerun.karafcxf.service.impl.example.data;

public class Device {

	//基本變數
	String deviceID;
	//位置變數
	String lat;
	String lng;
	String time;
	String locationType;
	String locationID;
	//心跳包常用變數
	String voltageLevel;
	String gsmLevel;
	String deviceStatus;
	//APP常用變數
	String timezone;
	String deviceName;
	String devicePhoneNumber;
	String deviceModel;
	String language;
	String imsi;
	//管理變數
	String socket;
	String adaptorID;
	String registered;

	//Constructor
	public Device(String deviceID){this.deviceID=deviceID;}
	//可存入之變數 位置變數
	public void setLat(String lat){this.lat=lat;}
	public void setLng(String lng){this.lng=lng;}
	public void setTime(String time){this.time=time;}
	public void setLocationType(String locationType){this.locationType=locationType;}
	public void setLocationID(String locationID){this.locationID=locationID;}
	//可存入之變數 心跳包常用變數
	public void setVoltageLevel(String voltageLevel){this.voltageLevel=voltageLevel;}
	public void setGSMLevel(String gsmLevel){this.gsmLevel=gsmLevel;}
	public void setDeviceStatus(String deviceStatus){this.deviceStatus=deviceStatus;}
	//可存入之變數 APP常用變數
	public void setTimezone(String timezone){this.timezone=timezone;}
	public void setDeviceName(String deviceName){this.deviceName=deviceName;}
	public void setDevicePhoneNumber(String devicePhoneNumber){this.devicePhoneNumber=devicePhoneNumber;}
	public void setDeviceModel(String deviceModel){this.deviceModel=deviceModel;}
	public void setLanguage(String language){this.language=language;}
	public void setIMSI(String imsi){this.imsi=imsi;}
	//可存入之變數 管理變數
	public void setSocket(String socket){this.socket=socket;}
	public void setAdaptorID(String adaptorID){this.adaptorID=adaptorID;}
	public void setRegistered(String registered){this.registered=registered;}

	//可取出之變數 基本變數
	public String getDeviceID(){return this.deviceID;}
	//可取出之變數 位置變數
	public String getLat(){return this.lat;}
	public String getLng(){return this.lng;}
	public String getTime(){return this.time;}
	public String getLocationType(){return this.locationType;}
	public String getLocationID(){return this.locationID;}
	//可取出之變數 心跳包常用變數
	public String getVoltageLevel(){return this.voltageLevel;}
	public String getGSMLevel(){return this.gsmLevel;}
	public String getDeviceStatus(){return this.deviceStatus;}
	//可取出之變數 APP常用變數
	public String getTimezone(){return this.timezone;}
	public String getDeviceName(){return this.deviceName;}
	public String getDevicePhoneNumber(){return this.devicePhoneNumber;}
	public String getDeviceModel(){return this.deviceModel;}
	public String getLanguage(){return this.language;}
	public String getIMSI(){return this.imsi;}
	//可取出之變數 管理變數
	public String getSocket(){return this.socket;}
	public String getAdaptorID(){return this.adaptorID;}
	public String getRegistered(){return this.registered;}
}
