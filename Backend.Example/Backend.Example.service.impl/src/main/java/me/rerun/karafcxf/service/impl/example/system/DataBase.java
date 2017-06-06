package me.rerun.karafcxf.service.impl.example.system;

import java.sql.*;

import com.mysql.jdbc.Driver;
import org.apache.log4j.Logger; 
import org.apache.log4j.PropertyConfigurator;

public class DataBase {

	Statement  stat= null;
	Connection con = null;
	ResultSet  rs  = null; 
	PreparedStatement pst = null; 

	Property propuser = new Property();
	Logger logger = Logger.getLogger(DataBase.class);
 
	//登入資料庫,註冊driver與取得connection
	public void loginDB(String properties)  {

		String db_url = propuser.loadproperties(properties,"DBIP");
		String db_name = propuser.loadproperties(properties,"DBname");
		String db_account = propuser.loadproperties(properties,"DBuser");
		String db_password = propuser.loadproperties(properties,"DBpassword");
		String driverName = Driver.class.getName();
		try { 
			logger.warn("Login into DataBase!!");
			Class.forName(driverName);       
			con = DriverManager.getConnection("jdbc:mysql://"+db_url+"/"+db_name+"?useUnicode=true&characterEncoding=Big5",db_account,db_password);} 
		catch(ClassNotFoundException e) { 
			logger.warn("DriverClassNotFound :"+e.toString());} 
		catch(SQLException x) { 
			logger.warn("Exception :"+x.toString());} 
	}

	//新增Table
	public void createDB(String tb_name, String tb_items) {

		String createdbSQL = "CREATE TABLE "+tb_name+"("+tb_items+")"; 
		try { 
			logger.warn(createdbSQL); 
			stat = con.createStatement(); 
			stat.executeUpdate(createdbSQL);} 
		catch(SQLException e) { 
			logger.warn("CreateDB Exception :" + e.toString());} 
	}

	//刪除Table
	public void dropDB(String tb_name) { 

		String dropdbSQL = "DROP TABLE "+tb_name; 
		try { 
			logger.warn(dropdbSQL);
			stat = con.createStatement(); 
			stat.executeUpdate(dropdbSQL);} 
		catch(SQLException e) { 
			logger.warn("DropDB Exception :" + e.toString());} 
	}

	//新增資料
	public void insertDB(String tb_info, String tb_insert) {

		String insertdbSQL = "insert into "+tb_info+" value "+tb_insert;  
		try { 
			logger.warn(insertdbSQL);
			pst = con.prepareStatement(insertdbSQL);       
			pst.executeUpdate();} 
		catch(SQLException e) { 
			logger.warn("InsertDB Exception :" + e.toString());} 
	}

	//修改資料
	public void updateDB(String tb_name, String tb_update, String tb_items) {

		String updatedbSQL = "update "+tb_name+" set "+tb_update+" where "+tb_items; 
		try { 
			logger.warn(updatedbSQL);
			pst = con.prepareStatement(updatedbSQL);       
			pst.executeUpdate();} 
		catch(SQLException e) { 
			logger.warn("UpdateDB Exception :" + e.toString());} 
	}


	//取得資料
	String[][] Time_interval_GPS;

	public String[][] selectDB(String tb_items, String tb_name, String item_range , String item_order) {

		String selectSQL = "select "+tb_items+" from "+tb_name+" "+item_range+" "+item_order; 
		String[] tb_items_token=tb_items.split(", ");
		int j=0;
		try { 
			logger.warn(selectSQL);
			stat = con.createStatement(); 
			rs = stat.executeQuery(selectSQL); 

			rs.last();
			Time_interval_GPS=new String[rs.getRow()][tb_items_token.length];

			rs.beforeFirst();
			while(rs.next()) 
				{ 
				for(int i=0; i<tb_items_token.length; i++){  
					Time_interval_GPS[j][i]=rs.getString(tb_items_token[i]);}
				j++;}
		} 
		catch(SQLException e) { logger.warn("DropDB Exception :" + e.toString());}
	return Time_interval_GPS;
	}

	//刪除資料
	public void deleteDB(String tb_name, String tb_items) {

		String deletedbSQL = "delete from "+tb_name+" where "+tb_items; 
		try { 
			logger.warn(deletedbSQL);
			pst = con.prepareStatement(deletedbSQL);       
			pst.executeUpdate();} 
		catch(SQLException e) { 
			logger.warn("UpdateDB Exception :" + e.toString());} 
	}

	//完整使用完資料庫後,記得要關閉所有Object 
	public void closeDB() {
	
		try { 
			logger.warn("Close DataBase!!");
			if(con!=null) {
				con.close(); 
				con = null;} 
			if(rs!=null) { 
				rs.close(); 
				rs = null;} 
			if(stat!=null) { 
				stat.close(); 
				stat = null;} 
			if(pst!=null) { 
				pst.close(); 
				pst = null;}} 
		catch(SQLException e) { 
			logger.warn("Close Exception :" + e.toString());} 
		}
}
