package servlet;

import java.io.IOException;

import javax.servlet.http.*;
import javax.annotation.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.sql.DataSource;

import models.Friend;
import models.User;

@WebServlet(urlPatterns={"/Friends"})
public class FriendsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@Resource private DataSource myDataSource;
	
    public FriendsServlet() 
    {
        super();
    	System.out.println("i am in constructor");
    }  

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{		
		try 
		{
	        //Setting up database connection
	        Connection myConnection = myDataSource.getConnection("APP","APP");			
	        System.out.println("i am in do GET FriendsServlet, Got Connection!");

			HttpSession session;
			session = request.getSession();
	        int id = (Integer)session.getAttribute("sessionId");
	        System.out.println("i am in do GET FriendsServlet, preparing  session");
	        
	        //Liste des amis
			PreparedStatement statementFriends = 
					myConnection.prepareStatement("(SELECT u.ID_USER, u.NOM_USER, u.PRENOM_USER, u.EMAIL_USER, u.DATE_OF_BIRTH, u.SEXE, u.USERNAME, u.PASSWORD_USER, u.ADDRESS_USER, u.IMAGE_USER, u.USER_CREATION_DATE, u.USER_MODIFICATION_DATE FROM FRIENDS f inner join USERS u on f.ID_USER_TWO = u.ID_USER WHERE f.ID_USER_ONE = " + id + " AND f.FRIENDS_STATE='Accepted') "
												+ "UNION "
												+ "(SELECT u1.ID_USER, u1.NOM_USER, u1.PRENOM_USER, u1.EMAIL_USER, u1.DATE_OF_BIRTH, u1.SEXE, u1.USERNAME, u1.PASSWORD_USER, u1.ADDRESS_USER, u1.IMAGE_USER, u1.USER_CREATION_DATE, u1.USER_MODIFICATION_DATE  FROM FRIENDS f1 inner join USERS u1 on f1.ID_USER_ONE = u1.ID_USER WHERE f1.ID_USER_TWO = " + id + " AND f1.FRIENDS_STATE='Accepted' )");
			ResultSet newFriendsResult = statementFriends.executeQuery();
			List<Friend> friendsList = new ArrayList<Friend>();
			System.out.println("i am in do GET FriendsServlet, preparing friends list");
			while(newFriendsResult.next())
			{
				System.out.println("i am in do GET FriendsServlet, executed list friends ");
				Friend friendd = new Friend();
				
				int idFriend = newFriendsResult.getInt("ID_USER");
				String nomFriend = newFriendsResult.getString("NOM_USER");
				String prenomFriend = newFriendsResult.getString("PRENOM_USER");
				String sexeFriend = newFriendsResult.getString("SEXE");
				String usernameFriend = newFriendsResult.getString("USERNAME");
				String passwordFriend = newFriendsResult.getString("PASSWORD_USER");
				String emailFriend =  newFriendsResult.getString("EMAIL_USER");
				String BdayFriend =  newFriendsResult.getString("DATE_OF_BIRTH");
				String creationDateFriend =  newFriendsResult.getString("USER_CREATION_DATE");
				String modificationDateFriend =  newFriendsResult.getString("USER_MODIFICATION_DATE");
				String addressFriend =  newFriendsResult.getString("ADDRESS_USER");
				String imageFriend =  newFriendsResult.getString("IMAGE_USER");
				
				friendd.setId(idFriend);
				friendd.setFullName(prenomFriend + " " + nomFriend);
				User currentUserFriend = new User(idFriend,prenomFriend,nomFriend,usernameFriend,passwordFriend,emailFriend,BdayFriend, sexeFriend, addressFriend, imageFriend, modificationDateFriend, creationDateFriend);
				friendd.setUserr(currentUserFriend);
				
				//We have to retieve the liste of common friends not just the number
				PreparedStatement statementFriendsInCommon = 
						myConnection.prepareStatement(
								"SELECT * "
								+ "FROM (SELECT u.ID_USER, u.NOM_USER, u.PRENOM_USER, u.EMAIL_USER, u.DATE_OF_BIRTH, u.SEXE, u.USERNAME, u.PASSWORD_USER, u.ADDRESS_USER, u.IMAGE_USER, u.USER_CREATION_DATE, u.USER_MODIFICATION_DATE  "
								+ "FROM FRIENDS f inner join USERS u on f.ID_USER_TWO = u.ID_USER WHERE f.ID_USER_ONE = " + id + " AND f.FRIENDS_STATE='Accepted' "
								+ "UNION "
								+ "SELECT u1.ID_USER, u1.NOM_USER, u1.PRENOM_USER, u1.EMAIL_USER, u1.DATE_OF_BIRTH, u1.SEXE, u1.USERNAME, u1.PASSWORD_USER, u1.ADDRESS_USER, u1.IMAGE_USER, u1.USER_CREATION_DATE, u1.USER_MODIFICATION_DATE   "
								+ "FROM FRIENDS f1 inner join USERS u1 on f1.ID_USER_ONE = u1.ID_USER WHERE f1.ID_USER_TWO = " + id + " AND f1.FRIENDS_STATE='Accepted' ) as friends "
								+ "WHERE friends.ID_USER IN (SELECT uu.ID_USER "
								+ "FROM FRIENDS ff inner join USERS uu on ff.ID_USER_TWO = uu.ID_USER WHERE ff.ID_USER_ONE = " + idFriend + "  AND ff.FRIENDS_STATE='Accepted'  "
								+ "UNION "
								+ "SELECT uu1.ID_USER "
								+ "FROM FRIENDS ff1 inner join USERS uu1 on ff1.ID_USER_ONE = uu1.ID_USER WHERE ff1.ID_USER_TWO = " + idFriend + " AND ff1.FRIENDS_STATE='Accepted' ) ");
				ResultSet newFriendsInCommon = statementFriendsInCommon.executeQuery();
				int nbrFrCommon = 0;
				List<User> friendsInCommonList = new ArrayList<User>();
				while (newFriendsInCommon.next()){
					int idFriendCommon = newFriendsInCommon.getInt("ID_USER");
					String nomFriendCommon = newFriendsInCommon.getString("NOM_USER");
					String prenomFriendCommon = newFriendsInCommon.getString("PRENOM_USER");
					String sexeFriendCommon = newFriendsInCommon.getString("SEXE");
					String usernameFriendCommon = newFriendsInCommon.getString("USERNAME");
					String passwordFriendCommon = newFriendsInCommon.getString("PASSWORD_USER");
					String emailFriendCommon =  newFriendsInCommon.getString("EMAIL_USER");
					String BdayFriendCommon =  newFriendsInCommon.getString("DATE_OF_BIRTH");
					String creationDateFriendCommon =  newFriendsInCommon.getString("USER_CREATION_DATE");
					String modificationDateFriendCommon =  newFriendsInCommon.getString("USER_MODIFICATION_DATE");
					String addressFriendCommon =  newFriendsInCommon.getString("ADDRESS_USER");
					String imageFriendCommon =  newFriendsInCommon.getString("IMAGE_USER");
					
					nbrFrCommon ++;
					
					User FriendInCommon = new User(idFriendCommon,prenomFriendCommon,nomFriendCommon,usernameFriendCommon,passwordFriendCommon,emailFriendCommon,BdayFriendCommon, sexeFriendCommon, addressFriendCommon, imageFriendCommon, modificationDateFriendCommon, creationDateFriendCommon);
					friendsInCommonList.add(FriendInCommon);
				}
				friendd.setfriendsCommonNumber(nbrFrCommon);
				friendd.setListFriendsCommon(friendsInCommonList);
				
				friendsList.add(friendd);	
			}
			session.setAttribute("friendsList", friendsList);		
			System.out.println("redirecting to friends page");
			
			//--------------------------- MANAGING YOUR PENDING REQUESTS --------------------------------------------			
			System.out.println("MANAGING YOUR PENDING REQUESTS");
			List<User> friendsListPending = new ArrayList<User>();
			
			PreparedStatement statementFriend21 = myConnection.prepareStatement("SELECT *  FROM FRIENDS f1"    
					+ " inner join USERS u1 on f1.ID_USER_TWO = u1.ID_USER "
					+ " WHERE f1.ID_USER_ONE = " + id + " and f1.FRIENDS_STATE = 'Pending'" );     


			ResultSet newFriendsResult21 = statementFriend21.executeQuery();
						
			while(newFriendsResult21.next())
			{
				int idFriend = newFriendsResult21.getInt("ID_USER");
				String nomFriend = newFriendsResult21.getString("NOM_USER");
				String prenomFriend = newFriendsResult21.getString("PRENOM_USER");
				String sexeFriend = newFriendsResult21.getString("SEXE");
				String usernameFriend = newFriendsResult21.getString("USERNAME");
				String passwordFriend = newFriendsResult21.getString("PASSWORD_USER");
				String emailFriend =  newFriendsResult21.getString("EMAIL_USER");
				String BdayFriend =  newFriendsResult21.getString("DATE_OF_BIRTH");
				String creationDateFriend =  newFriendsResult21.getString("USER_CREATION_DATE");
				String modificationDateFriend =  newFriendsResult21.getString("USER_MODIFICATION_DATE");
				String addressFriend =  newFriendsResult21.getString("ADDRESS_USER");
				String imageFriend =  newFriendsResult21.getString("IMAGE_USER");
				
				User newUser = new User(idFriend,prenomFriend,nomFriend,usernameFriend,passwordFriend,emailFriend,BdayFriend, sexeFriend, addressFriend, imageFriend, modificationDateFriend, creationDateFriend);			
				friendsListPending.add(newUser);
			}
			request.setAttribute("friendsListPending", friendsListPending );
			//--------------------------- MANAGING FRIEND REQUESTS --------------------------------------------------
			System.out.println("MANAGING FRIEND REQUESTS");
			List<User> FriendRequestList = new ArrayList<User>();
			PreparedStatement statementFriend22 = myConnection.prepareStatement("SELECT *  FROM FRIENDS f1"    
					+ " inner join USERS u1 on f1.ID_USER_ONE = u1.ID_USER "
					+ " WHERE f1.ID_USER_TWO = " + id + " and f1.FRIENDS_STATE = 'Pending'" );     


			ResultSet newFriendsResult22 = statementFriend22.executeQuery();
						
			while(newFriendsResult22.next())
			{
				int idFriend = newFriendsResult22.getInt("ID_USER");
				String nomFriend = newFriendsResult22.getString("NOM_USER");
				String prenomFriend = newFriendsResult22.getString("PRENOM_USER");
				String sexeFriend = newFriendsResult22.getString("SEXE");
				String usernameFriend = newFriendsResult22.getString("USERNAME");
				String passwordFriend = newFriendsResult22.getString("PASSWORD_USER");
				String emailFriend =  newFriendsResult22.getString("EMAIL_USER");
				String BdayFriend =  newFriendsResult22.getString("DATE_OF_BIRTH");
				String creationDateFriend =  newFriendsResult22.getString("USER_CREATION_DATE");
				String modificationDateFriend =  newFriendsResult22.getString("USER_MODIFICATION_DATE");
				String addressFriend =  newFriendsResult22.getString("ADDRESS_USER");
				String imageFriend =  newFriendsResult22.getString("IMAGE_USER");
				
				User newUser = new User(idFriend,prenomFriend,nomFriend,usernameFriend,passwordFriend,emailFriend,BdayFriend, sexeFriend, addressFriend, imageFriend, modificationDateFriend, creationDateFriend);			
				FriendRequestList.add(newUser);
			}
			request.setAttribute("FriendRequestList", FriendRequestList );
			request.getRequestDispatcher("/FriendsPage.jsp").forward(request, response);

			myConnection.close();
			System.out.println("i am out of do GET FriendsServlet");
		} 		
		catch (Exception e) 
		{
			System.out.println("i am in catch ALL Exception POST FriendsServlet, Got Connection!");
			request.setAttribute("loginMessage", "* Error in connection");
			request.getRequestDispatcher("/LoginPage.jsp").forward(request, response);
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{					
		try
		{
			
		
			String SearchInput = request.getParameter("SearchInput");
			Connection myConnection = myDataSource.getConnection("APP","APP");
			
			
			List<User> PeopleSearchList = new ArrayList<User>();
			
			PreparedStatement statementPeopleSearch = myConnection.prepareStatement("select * from USERS where USERNAME LIKE '%" + SearchInput + "%' or PRENOM_USER LIKE '%" + SearchInput + "%'  or NOM_USER LIKE '%" + SearchInput + "%'");
			
			ResultSet newPeopleSearch = statementPeopleSearch.executeQuery();
			System.out.println("i am in do GET FriendsProfileServlet, preparing friends list");
			while(newPeopleSearch.next())
			{
				int idFriend = newPeopleSearch.getInt("ID_USER");
				String nomFriend = newPeopleSearch.getString("NOM_USER");
				String prenomFriend = newPeopleSearch.getString("PRENOM_USER");
				String sexeFriend = newPeopleSearch.getString("SEXE");
				String usernameFriend = newPeopleSearch.getString("USERNAME");
				String passwordFriend = newPeopleSearch.getString("PASSWORD_USER");
				String emailFriend =  newPeopleSearch.getString("EMAIL_USER");
				String BdayFriend =  newPeopleSearch.getString("DATE_OF_BIRTH");
				String creationDateFriend =  newPeopleSearch.getString("USER_CREATION_DATE");
				String modificationDateFriend =  newPeopleSearch.getString("USER_MODIFICATION_DATE");
				String addressFriend =  newPeopleSearch.getString("ADDRESS_USER");
				String imageFriend =  newPeopleSearch.getString("IMAGE_USER");
				
				User newUser = new User(idFriend,prenomFriend,nomFriend,usernameFriend,passwordFriend,emailFriend,BdayFriend, sexeFriend, addressFriend, imageFriend, modificationDateFriend, creationDateFriend);			
				PeopleSearchList.add(newUser);										
			}
			request.setAttribute("SearchList", PeopleSearchList);
			doGet(request, response);
		}
		catch (SQLException e) 
		{
			System.out.println("i am in catch SQL Exception POST FriendsServlet, Got Connection!");			
			doGet(request, response);
			
		}
		catch (Exception e) 
		{
			System.out.println("i am in catch ALL Exception POST FriendsServlet, Got Connection!");
			request.setAttribute("loginMessage", "* Error in connection");
			request.getRequestDispatcher("/LoginPage.jsp").forward(request, response);
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
}
