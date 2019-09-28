package mysqldatabasehandling;

import java.sql.*;
public class SqlHandling {
	private static Connection con;
	// MySql Connection method 
	public static void mysqlconnect(String id,String pass) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Data base driver loaded!");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank",id,pass);
			System.out.println("Data base connection done");
		}
		catch(Exception e) {
			System.out.println("Connection Error");
			System.out.println(e);
		}
	}
	//MySql Login chack
	public static String logincheck(String id, String pass) {
		try {
			Statement stmt=con.createStatement();  
			
			ResultSet rs=stmt.executeQuery("select * from userlogin where account_number="+id+" and pass=md5('"+pass+"');");
			
			if (rs.next()){
				int temp=-1;
				ResultSet rs1=stmt.executeQuery("select * from account where account_number="+id+";");
				if (rs1.next()) {
					temp= rs1.getInt("balance");
					String name;
					ResultSet rs2=stmt.executeQuery("select * from person where account_number="+id+";");
					if(rs2.next()) {
						name=rs2.getString("name");
						String ResultStr=name+" "+temp;
						return ResultStr;
					}
				
				}
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	//MySql Transaction Method
	public static String transaction(String amount,String from,String to) {
		try {
			Statement stmt=con.createStatement();
			ResultSet rsFrom=stmt.executeQuery("select * from account where account_number = "+from+" ;");
			if (rsFrom.next()) {
				if(rsFrom.getInt("balance")  >= Integer.parseInt(amount)) {
					Integer newBalF=rsFrom.getInt("balance")-Integer.parseInt(amount);
					stmt.executeUpdate("update account set balance = "+newBalF+" where account_number= "+from+" ;");
					ResultSet rsTo=stmt.executeQuery("select * from account where account_number="+to+";");
					if (rsTo.next()) {
						Integer newBalT=rsTo.getInt("balance")+Integer.parseInt(amount);
						stmt.executeUpdate("update account set balance = "+newBalT+" where account_number = "+to+" ;");
						//Log part to be done!!
					}
					return newBalF.toString();
				}
			}
			
		} catch (Exception e) {
			System.out.println("Error in Transation!");
			System.out.println(e);
		}
		return null;
		
	}
	//MySql Refresh command
	public static String refresh(String id) {
		try {
			Statement stmt=con.createStatement();
			ResultSet rsFrom=stmt.executeQuery("select * from account where account_number = "+id+" ;");
			if (rsFrom.next()) {
				return rsFrom.getInt("balance")+"";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	/***MySql Disconnect Method ***/
	public static void mysqldissconnect() {
		try {
			
			con.close();
			System.out.println("Dissconnected properly!");
		} catch (Exception e) {
			System.out.println("Error in disconnect");
			System.out.println(e);
		}
	}
}
