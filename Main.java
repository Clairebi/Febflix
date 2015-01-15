import java.util.Scanner;
import java.sql.*;

public class Main{

	public static void main(String[] args) throws Exception{

		while(true){

			Scanner typeIn = new Scanner(System.in);
			Login.login();
			while(Login.isLogin==true){
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

				switch(typeIn.nextLine()){
					case "1":
						System.out.println("Please enter a star (First Name/Last Name):");
						String query = typeIn.nextLine();
						Statement select = Utility.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
						String sql1 = "SELECT * FROM movies";
						sql1 += " INNER JOIN";
						sql1 += " (SELECT DISTINCT movie_id FROM stars_in_movies";
						sql1 += " INNER JOIN stars ON stars_in_movies.star_id=stars.id";
						sql1 += " where stars.first_name='"+query+"'";
						sql1 += " OR stars.last_name='"+query+"'";
						// sql1 += " OR stars.id="+Integer.parseInt(query);
						sql1 += ") A";
						sql1 += " ON movies.id=A.movie_id";

						try{
					        ResultSet result = select.executeQuery(sql1);

					        if(result.next()){
					        	result.beforeFirst();
						        System.out.println("\nInformation of the movies featuring "+query+"-----------------");
				                while (result.next()){
						           System.out.println("ID = "+result.getInt("id"));
						           System.out.println("TITLE = "+result.getString("title"));
						           System.out.println("YEAR"+result.getInt("year"));
						           System.out.println("DIRECTOR"+result.getString("director"));
						           System.out.println("BANNER"+result.getString("banner_url"));
						           System.out.println("TRAILER"+result.getString("trailer_url"));
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

						System.out.println("Press Any Key to Continue:");
						System.in.read();
						break;

					case "2":
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

						System.out.println("Press Any Key to Continue:");
						System.in.read();
						break;

					case "3":
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
						System.out.println("Press Any Key to Continue:");
						System.in.read();
						break;

					case "4":
						System.out.println("Please enter the FIRST NAME of the customer you want to delete: ");
						String cusFirstNameDel = typeIn.nextLine();
						System.out.println("Please enter the LAST NAME of the customer you want to delete: ");
						String cusLastNameDel = typeIn.nextLine();

						Statement del_cus = Utility.connection.createStatement();
						String sql4 = "DELETE FROM customers WHERE first_name='"+cusFirstNameDel+"' AND last_name='"+cusLastNameDel+"'";
						
						try{
							int cusRetIDDel = del_cus.executeUpdate(sql4);
							if(cusRetIDDel==1)
								System.out.println("Delete complete!");
						}catch(SQLException e4){
							System.err.println("Delete fail! There is no such record in the table!");
						}

						System.out.println("Press Any Key to Continue:");
						System.in.read();
						break;

					case "5":
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

						System.out.println("Press Any Key to Continue:");
						System.in.read();
						break;

					case "6":
						try{
							do{
								System.out.println("Please choose enter QUERY(1) or UPDATE(2) operation:");
								String choice = typeIn.nextLine();
								if(choice.equals("1") || choice.equals("query") || choice.equals("QUERY")){
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
}