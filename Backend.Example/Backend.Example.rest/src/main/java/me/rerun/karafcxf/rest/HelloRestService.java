package me.rerun.karafcxf.rest;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.json.JSONObject;
import org.json.JSONException;

public interface HelloRestService {

/**
* @PUT提供APP/WEB使用者註冊
* @GET提供APP/WEB登入功能
*/
	//註冊帳戶
	@PUT
	@Path("registration")
	public String newUser(@HeaderParam("DeviceID") String deviceID, @HeaderParam("Username") String username, @HeaderParam("Password") String password, String device_data) throws JSONException;

	//登入
	@GET
	@Path("registration")
	public String checkUser(@HeaderParam("Username") String username, @HeaderParam("Password") String password) throws JSONException;

/**
* @POST給APP/WEB取出歷史GPS資料
* @PUT給Tracker存入GPS資料
*/
	//取出歷史軌跡
	@POST
	@Path("locations")
	public String getIntervalGPS(@HeaderParam("DeviceID") String deviceID, String time_interval) throws JSONException;

	//存入GPS資料
	@PUT
	@Path("locations")
	public String putDataGPS(@HeaderParam("DeviceID") String deviceID, @HeaderParam("DeviceModel") String deviceModel, @HeaderParam("IMSI") String imsi,
	@HeaderParam("AdaptorID") String adaptorID, @HeaderParam("Socket") String socket, String gps_data) throws JSONException;


/**
* @給APP/WEB取出當下GPS資料＆穩態資料 or Order Shutdown
* @給Tracker存入資料
* @其它...給Tracker
*/
	//取得最新的位置&穩態資料
	@GET
	@Path("devices")
	public String getNowGPS(@HeaderParam("DeviceID") String deviceID) throws JSONException;

	//APP/WEB設下Timezone使用
	@POST
	@Path("device/timezone")
	public String setTimeZone(@HeaderParam("DeviceID") String deviceID, String zone_data) throws JSONException;

	//APP/WEB Order Device Shutdown使用
	@POST
	@Path("device/shutdown")
	public String orderDeviceDown(@HeaderParam("DeviceID") String deviceID) throws JSONException;

	//存入Device穩態資料
	@POST
	@Path("devices")
	public String putDataDevice(@HeaderParam("DeviceID") String deviceID, @HeaderParam("DeviceModel") String deviceModel, @HeaderParam("IMSI") String imsi,
	@HeaderParam("AdaptorID") String adaptorID, @HeaderParam("Socket") String socket, String device_data) throws JSONException;

	//存入警告資料
	@PUT
	@Path("device/warnings")
	public void putDeviceWarn(@HeaderParam("DeviceID") String deviceID, @HeaderParam("DeviceModel") String deviceModel, @HeaderParam("IMSI") String imsi,
	@HeaderParam("AdaptorID") String adaptorID, @HeaderParam("Socket") String socket, String warn_data) throws JSONException;

	//Device登錄以及回饋設定的response
	@POST
	@Path("device/settings")
	public void putDeviceSet(@HeaderParam("DeviceID") String deviceID, @HeaderParam("DeviceModel") String deviceModel, @HeaderParam("IMSI") String imsi,
	@HeaderParam("AdaptorID") String adaptorID, @HeaderParam("Socket") String socket, String response_data) throws JSONException;

	//Device斷Connect使用
	@POST
	@Path("device/disconnect")
	public void putDeviceDiscon(@HeaderParam("DeviceID") String deviceID) throws JSONException;

/**
* @PUT給APP/WEB
* @POST給APP/WEB
* @DELETE給APP/WEB
*/
	//設定GeoFence
	@PUT
	@Path("fences")
	public String setGeoFence(@HeaderParam("DeviceID") String deviceID, String fence_data) throws JSONException;

	//取得GeoFence設定
	@POST
	@Path("fences")
	public String getGeoFence(@HeaderParam("DeviceID") String deviceID, String fence_id) throws JSONException;

	//刪除GeoFence
	@DELETE
	@Path("fences")
	public String deleteGeoFence(@HeaderParam("DeviceID") String deviceID, @HeaderParam("GeoFenceID") String geoFenceID) throws JSONException;

/**
* @PUT給APP/WEB
* @POST給APP/WEB
* @DELETE給APP/WEB
*/
	//設定SOS Number
	@POST
	@Path("sos")
	public String setSOSNumber(@HeaderParam("DeviceID") String deviceID, String number_phone) throws JSONException;

	//取得SOS Number設定
	@GET
	@Path("sos")
	public String getSOSNumber(@HeaderParam("DeviceID") String deviceID, @HeaderParam("PhoneID") String phoneID) throws JSONException;

	//刪除SOS Number
	@DELETE
	@Path("sos")
	public String deleteSOSNumber(@HeaderParam("DeviceID") String deviceID, @HeaderParam("PhoneID") String phoneID) throws JSONException;

/**
* @給APP/WEB
*/
	//設定Fall Down
	@POST
	@Path("falldown")
	public String setFalldown(@HeaderParam("DeviceID") String deviceID, @HeaderParam("State") String state) throws JSONException;

/**
* @For Debug
*/
	@GET
	@Path("debug")
	public String debug() throws JSONException;

}
