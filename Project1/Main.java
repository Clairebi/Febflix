import java.util.Scanner;
import java.sql.*;

public class Main{

	public static void main(String[] args) throws Exception{

		while(true){

			Scanner typeIn = new Scanner(System.in);

			Login.login();

			while(Login.isLogin==true){

				printMenu();

				switch(typeIn.nextLine()){
					case "1":
						showMovieFeatureStar();
						System.out.println("Press Any Key to Continue:");
						System.in.read();
						break;

					case "2":
						insertStar();
						System.out.println("Press Any Key to Continue:");
						System.in.read();
						break;

					case "3":
						insertCustomer();
						System.out.println("Press Any Key to Continue:");
						System.in.read();
						break;

					case "4":
						deleteCustomer();
						System.out.println("Press Any Key to Continue:");
						System.in.read();
						break;

					case "5":
						metadataOfDatabase();
						System.out.println("Press Any Key to Continue:");
						System.in.read();
						break;

					case "6":
						executeSQLCommand();
						System.out.println("Press Any Key to Continue:");
						System.in.read();
						break;

					case "7":
						Login.isLogin = false;
						break;

					case "8":
						System.out.println("See you!!!!!!!!!!!!");
						System.exit(0);
						break;
				}
			}
		}
	}

	/*
	 * print menu
	 */
	public static void printMenu(){
		System.out.println("\nWelcome!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		System.out.println("Please choose the menu:");
		System.out.println("1: Print out the movies featuring a given star");
		System.out.println("2: Insert a new star into database");
		System.out.println("3: Insert a customer into database");
		System.out.println("4: Delete a customer from database");
		System.out.println("5: Provide the metadata of database");
		System.out.println("6: Enter a valid SELECT/UPDATE/INSERT/DELETE SQL command.");
		System.out.println("7: Exit the menu");
		System.out.println("8: Exit the program\n");
	}

	/*
	 * Print out (to the screen) the movies featuring a given star.
	 * All movie attributes should appear, labeled and neatly arranged;
	 * the star can be queried via first name and/or last name or by ID.
	 * First name and/or last name means that a star should be queried by both a) first name AND last name b) first name or last name.
	 */
	public static void showMovieFeatureStar()throws Exception{
		Scanner typeIn = new Scanner(System.in);
		do{
			System.out.println("Please choose to enter FIRST NAME(1) or LAST NAME(2) or FULL NAME(3) or ID(4):");
			String option = typeIn.nextLine();
			String query = null;
			String sql1 = null;
			Statement select = Utility.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			if(option.equals("1") || option.equalsIgnoreCase("first name")){
				System.out.println("Please enter the FIRST NAME of a star(case sensitive):");
				query = typeIn.nextLine();

				sql1 = "SELECT * FROM movies";
				sql1 += " INNER JOIN";
				sql1 += " (SELECT DISTINCT movie_id FROM stars_in_movies";
				sql1 += " INNER JOIN stars ON stars_in_movies.star_id=stars.id";
				sql1 += " where stars.first_name='"+query+"'";
				sql1 += ") A";
				sql1 += " ON movies.id=A.movie_id";
			}
			else if(option.equals("2") || option.equalsIgnoreCase("last name")){
				System.out.println("Please enter the LAST NAME of a star(case sensitive):");
				query = typeIn.nextLine();

				sql1 = "SELECT * FROM movies";
				sql1 += " INNER JOIN";
				sql1 += " (SELECT DISTINCT movie_id FROM stars_in_movies";
				sql1 += " INNER JOIN stars ON stars_in_movies.star_id=stars.id";
				sql1 += " where stars.last_name='"+query+"'";
				sql1 += ") A";
				sql1 += " ON movies.id=A.movie_id";
			}
			else if(option.equals("3") || option.equalsIgnoreCase("full name")){
				System.out.println("Please enter the FULL NAME of a star(case sensitive):");
				query = typeIn.nextLine();
				String[] query_split = query.split("\\s");
				String query_fn = query_split[0];
				String query_ln = query_split[1];

				sql1 = "SELECT * FROM movies";
				sql1 += " INNER JOIN";
				sql1 += " (SELECT DISTINCT movie_id FROM stars_in_movies";
				sql1 += " INNER JOIN stars ON stars_in_movies.star_id=stars.id";
				sql1 += " where stars.first_name='"+query_fn+"'";
				sql1 += " AND stars.last_name='"+query_ln+"'";
				sql1 += ") A";
				sql1 += " ON movies.id=A.movie_id";
			}
			else if(option.equals("4") || option.equalsIgnoreCase("id")){
				System.out.println("Please enter the ID of a star:");
				query = typeIn.nextLine();

				sql1 = "SELECT * FROM movies";
				sql1 += " INNER JOIN";
				sql1 += " (SELECT DISTINCT movie_id FROM stars_in_movies";
				sql1 += " INNER JOIN stars ON stars_in_movies.star_id=stars.id";
				sql1 += " WHERE stars.id="+Integer.parseInt(query);
				sql1 += ") A";
				sql1 += " ON movies.id=A.movie_id";
			}
			try{
		        ResultSet result = select.executeQuery(sql1);

		        if(result.next()){
		        	result.beforeFirst();
			        System.out.println("\nInformation of the movies featuring the star: "+query+"-----------------");
	                while (result.next()){
			           System.out.println("ID = "+result.getInt("id"));
			           System.out.println("TITLE = "+result.getString("title"));
			           System.out.println("YEAR = "+result.getInt("year"));
			           System.out.println("DIRECTOR = "+result.getString("director"));
			           System.out.println("BANNER = "+result.getString("banner_url"));
			           System.out.println("TRAILER = "+result.getString("trailer_url"));
			           System.out.println();
			        }
		        }
		        else{
		        	System.out.println("No record found!");
		        	System.out.println();
		        }							
			}catch(SQLException e1){
				System.err.println("SQL Error!");
			}
			System.out.println("Continue to enter another query? Y/N");
		}while(typeIn.hasNext() && (typeIn.nextLine().equalsIgnoreCase("y")));
	}

	/*
	 * Insert a new star into the database.
	 * If the star has a single name, add it as his last_name and assign an empty string ("") to first_name.
	 */
	public static void insertStar()throws Exception{
		Scanner typeIn = new Scanner(System.in);
		System.out.println("Please enter the FIRST NAME of the star: ");
		String starFirstName = typeIn.nextLine();
		System.out.println("Please enter the LAST NAME of the star: ");
		String starLastName = typeIn.nextLine();
		System.out.println("Please enter the DATE OF BIRTH of the star in the format \"yyyy-[m]m-[d]d\": ");
		String starDateOfBirth = typeIn.nextLine();
		System.out.println("Please enter the PHOTO URL of the star: ");
		String starPhotoURL = typeIn.nextLine();

		Statement insert_star = Utility.connection.createStatement();
		String sql2 = "INSERT INTO stars(first_name,last_name,dob,photo_url) ";
		sql2 += "VALUES('"+starFirstName+"','"+starLastName+"','"+Date.valueOf(starDateOfBirth)+"','"+starPhotoURL+"')";
		
		try{
			int starRetID = insert_star.executeUpdate(sql2);
			if(starRetID==1)
				System.out.println("Insertion complete!");
			else
				System.out.println("Insertion fail!");
		}catch(SQLException e2){
			System.err.println("SQL Error!");
		}
	}

	/*
	 * Insert a customer into the database.
	 * Do not allow insertion of a customer if his credit card does not exist in the credit card table.
	 * The credit card table simulates the bank records.
	 * If the customer has a single name, add it as his last_name and and assign an empty string ("") to first_name.
	 */
	public static void insertCustomer()throws Exception{
		Scanner typeIn = new Scanner(System.in);
		System.out.println("Please enter the FIRST NAME of the customer: ");
		String cusFirstName = typeIn.nextLine();
		System.out.println("Please enter the LAST NAME of the customer: ");
		String cusLastName = typeIn.nextLine();
		System.out.println("Please enter the CREDIT CARD ID of the customer: ");
		String cusCC_ID = typeIn.nextLine();
		System.out.println("Please enter the ADDRESS of the customer: ");
		String cusAddress = typeIn.nextLine();
		System.out.println("Please enter the EMAIL of the customer: ");
		String cusEmail = typeIn.nextLine();				
		System.out.println("Please enter the PASSWORD of the customer: ");
		String cusPassword = typeIn.nextLine();

		Statement insert_cus = 	Utility.connection.createStatement();
		String sql3 = "SELECT * FROM creditcards WHERE id='"+cusCC_ID+"'";
		//Check whether credit card exist in the credit card table
		try{
			if(insert_cus.executeQuery(sql3).next()){	//Exist
				sql3 = "INSERT INTO customers(first_name,last_name,cc_id,address,email,password) ";
				sql3 += "VALUES('"+cusFirstName+"','"+cusLastName+"','"+cusCC_ID+"','"+cusAddress+"','"+cusEmail+"','"+cusPassword+"')";
				int cusRetIDIns = insert_cus.executeUpdate(sql3);
				if(cusRetIDIns==1)
					System.out.println("Insertion complete!");
				else
					System.out.println("Insertion fail!");
			}
			else{
				System.out.println("The credit card ID of the customer does not exist in the bank!");
			}
		}catch(SQLException e3){
			System.err.println("SQL Error!");
		}
	}

	/*
	 * Delete a customer from the database.
	 */
	public static void deleteCustomer()throws Exception{
		Scanner typeIn = new Scanner(System.in);
		System.out.println("Please enter the ID(You may go to the 6 of Menu to find the ID of the customer)： ");
		int deleteID = typeIn.nextInt();
		Statement del_cus = Utility.connection.createStatement();
		String sql4 = "DELETE FROM customers WHERE id ='"+deleteID+"'";
		
		try{
			int cusRetIDDel = del_cus.executeUpdate(sql4);
			if(cusRetIDDel==1){
				System.out.println("Delete complete!");
			}else{
				System.out.println("Delete fail! There is no such record in the table!");	
			}
		}catch(SQLException e4){
			System.err.println("SQL Error!");
		}
	}

	/*
	 * Provide the metadata of the database.
	 * In particular, print out the name of each table and, for each table, each attribute and its type.
	 */
	public static void metadataOfDatabase()throws Exception{
		Scanner typeIn = new Scanner(System.in);
		Statement get_meta = Utility.connection.createStatement();
		String [] tableName = {"movies","stars","stars_in_movies","genres","genres_in_movies","creditcards","customers","sales"};
		ResultSetMetaData [] query_metadata = new ResultSetMetaData[tableName.length];
		
		try{
			for(int i=0; i<tableName.length; i++){
				String queryTable = "SELECT * FROM "+tableName[i];
				query_metadata[i] = get_meta.executeQuery(queryTable).getMetaData();
				System.out.println("Table Name: "+tableName[i]);
				for(int j=1; j<=query_metadata[i].getColumnCount();j++){
					System.out.println("Attribute: "+query_metadata[i].getColumnName(j)+", Type: "+query_metadata[i].getColumnTypeName(j));
				}
				System.out.println();
			}							
		}catch(SQLException e5){
			System.err.println("SQL Error!");
		}
	}

	/*
	 * Enter a valid SELECT/UPDATE/INSERT/DELETE SQL command.
	 * The system should take the corresponding action, and return and display the valid results.
	 * For a SELECT query, display the answers.
	 * For the other types of queries, give enough information about the status of the execution of the query.
	 * For instance, for an UPDATE query, show the user how many records have been successfully changed.
	 */
	public static void executeSQLCommand()throws Exception{
		Scanner typeIn = new Scanner(System.in);
		try{
			do{
				System.out.println("Please choose to enter QUERY(1) or UPDATE(2) operation:");
				String choice = typeIn.nextLine();
				if(choice.equals("1") || choice.equalsIgnoreCase("query")){
					System.out.println("Please enter the QUERY:");
					String sqlQuery = typeIn.nextLine();
					Statement q_statement = Utility.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
					ResultSet q_result = q_statement.executeQuery(sqlQuery);
					ResultSetMetaData q_meta = q_result.getMetaData();
					if(q_result.next()){
						q_result.beforeFirst();
						while(q_result.next()){
							for(int k=1; k<=q_meta.getColumnCount(); k++){
								System.out.println(q_meta.getColumnName(k)+"="+q_result.getString(k));
							}
							System.out.println();
						}
					}
					else{
						System.out.println("No record found!");
			        	System.out.println();
					}
				}
				else if(choice.equals("2") || choice.equals("update") || choice.equals("UPDATE")){
					System.out.println("Please enter the UPDATE:");
					String sqlUpdate = typeIn.nextLine();
					Statement u_statement = Utility.connection.createStatement();
					int u_retID = u_statement.executeUpdate(sqlUpdate);
					if(u_retID>0)
						System.out.println("Successfully complete "+u_retID+ "record(s)");
					else
						System.out.println("Update fail!");
				}
				else{
					System.out.println("You have enter the wrong word!");
				}
				System.out.println("Continue to enter another query? Y/N");
			}while(typeIn.hasNext() && (typeIn.nextLine().equalsIgnoreCase("y")));
		}catch(SQLException e6){
			System.err.println("SQL Error!");
		}
	}
}
