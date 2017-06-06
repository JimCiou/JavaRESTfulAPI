package me.rerun.karafcxf.service.impl.example.func;

import me.rerun.karafcxf.service.impl.example.system.*;
import me.rerun.karafcxf.service.impl.example.data.*;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.CopticChronology;
import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;

public class Time {

	DataBase datauser=new DataBase();
	Logger logger = Logger.getLogger(Time.class);

	public String getDeviceTime(Device device) {

		boolean catchDB=false;
		if(device.getTimezone()==null){catchDB=true;}

		if(catchDB){
			logger.warn("Zone data need to catch DB");
			datauser.loginDB("etc/backend.tracker.properties");
        		String[][] timeZoneDB=datauser.selectDB("TimeZone", "Devices", "where DeviceID ='"+device.getDeviceID()+"'", " ");
			datauser.closeDB();
			device.setTimezone(timeZoneDB[0][0]);
		}

        String[] timeZonetokens = device.getTimezone().split(":");

	//gmt_h為小時正負號  gmt_m為分鐘皆為正
	int gmt_h=Integer.parseInt(timeZonetokens[0].substring(0,timeZonetokens[0].length()-2));
	int gmt_m=Integer.parseInt(timeZonetokens[0].substring(timeZonetokens[0].length()-2,timeZonetokens[0].length()-0));

	//(2, 15)    +02:15  or (-2, 15)   -02:15
	DateTime dt1 = new DateTime(DateTimeZone.forOffsetHoursMinutes(gmt_h,gmt_m));

	//HH 0~23
	return dt1.toString("yyyy-MM-dd HH:mm:ss");}
}
