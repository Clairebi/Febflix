import java.util.Scanner;
import java.sql.*;

public class Login{

	public static boolean isLogin = false;

	public static void login() throws Exception {
		if(isLogin==false){
			Scanner sc = new Scanner(System.in);
			do{
				System.out.println("Welcome to the Febflix-------------");
				System.out.print("Please enter your username:");
				String username = sc.nextLine();
				System.out.print("Please enter your password:");
				String password = sc.nextLine();

				try{
					// Incorporate mySQL driver
			        Class.forName("com.mysql.jdbc.Driver").newInstance(); 
			             
		        	Utility.connection = DriverManager.getConnection("jdbc:mysql:///MovieDB", username,password);
		        	isLogin = true;
		        	break;
		        }catch(SQLException e){
		        	// e.printStackTrace();
		        	System.err.println("Username or Password is not valid");
		        	System.out.println("Exit or Not?[Y/N]");
		        }
			}while(sc.hasNext() && (sc.nextLine().equalsIgnoreCase("n")));
		}
	}
}