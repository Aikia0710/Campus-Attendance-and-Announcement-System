package Database;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
//import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DatabaseConnection {
	
	public static void main(String[] args) throws ClassNotFoundException {
		
		Class.forName("org.postgresql.Driver");
		System.out.println("Driver loaded");
	}
	
	private String loginUsername;
	private String password;
	private String url;
	private Connection connection;

	public DatabaseConnection(String url,String loginUsername, String password) {
		this.url = url;
		this.loginUsername = loginUsername;
		this.password = password;
		
		
				try {
					Connection connection = DriverManager.getConnection(url, loginUsername, password);
					this.connection = connection;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
		
		
		
	}
	//method for checking details for logging in returns true if correct false otherwise
	public boolean checkStudentDetails(String inputUsername, String password) {
		
		try {
			PreparedStatement checkUsername = this.connection
					.prepareStatement("SELECT * FROM student WHERE username= ?");
			checkUsername.setString(1, inputUsername);
			
			try (ResultSet resultSet = checkUsername.executeQuery()) {
				while (resultSet.next()) {
					// 2nd way
					String usernamePassword = resultSet.getString("password");
					if(usernamePassword.equals(password)) {
						return true; //this only occurs when username and password is correct				
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //select the whole row where username is given username
		return false; //only happens if username or password are not a pair in the database	
	}
	
		//method for checking details for logging in returns true if correct false otherwise
		public boolean checkStaffDetails(String inputUsername, String password) {
			
			try {
				PreparedStatement checkUsername = this.connection
						.prepareStatement("SELECT * FROM Staff WHERE username= ?");
				checkUsername.setString(1, inputUsername);
				
				try (ResultSet resultSet = checkUsername.executeQuery()) {
					while (resultSet.next()) {						

						// 2nd way
						String usernamePassword = resultSet.getString("password");
						if(usernamePassword.equals(password)) {
							return true; //this only occurs when username and password is correct
						}						
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //select the whole row where username is given username
			return false; //only happens if username or password are not a pair in the database				
		}
	

	//method to return a set of courses the user is a part of for their homepage
	public Set<String> listOfCoursesStudent(String inputUsername){
		Set<String> result = new HashSet<String>();
		try {
			PreparedStatement checkUsername = this.connection
					.prepareStatement("SELECT courses_list.name FROM student_courses,courses_list, WHERE courses_list.cid = student_courses.cid AND student_courses.sid = (SELECT sid FROM student WHERE username = ?)");
			checkUsername.setString(1, inputUsername);
			try (ResultSet resultSet = checkUsername.executeQuery()) {
				while (resultSet.next()) {
					String currentCourse = resultSet.getString("name"); //gives name of course on each line of result			
					result.add(currentCourse); //adds each course line by line to the set		
				}
				return result;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //select the whole row where username is given username
		return result; //only happens if no result for sql statement with given usernamne
	}
	
	
	
	//method to return a set of courses the user is a part of for their homepage
	public Set<String> listOfCoursesStaff(String inputUsername){
		Set<String> result = new HashSet<String>();
		try {
			PreparedStatement checkUsername = this.connection
					.prepareStatement("SELECT courses_list.name FROM staff_courses,courses_list, WHERE courses_list.cid = staff_courses.cid AND staff_courses.pid = (SELECT pid FROM staff WHERE username = ?)");
			checkUsername.setString(1, inputUsername);
				
			try (ResultSet resultSet = checkUsername.executeQuery()) {
				while (resultSet.next()) {
					String currentCourse = resultSet.getString("name"); //gives name of course on each line of results					
					result.add(currentCourse); //adds each course line by line to the set				
				}
				return result;
			}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //select the whole row where username is given username
			return result; //only happens if no result for sql statement with given username
						
		}
	
	/*this method is used for lecturer(lecturerUsername) to get his/her coursesSet containing "coursename:cid"*/
	public Set<String> listOfCoursesDetailsStaff(String lecturerUsername){
		/**
		 * get the coursename and corresponding cid of courses for lecturerUsername,
		 * put this in a Set<String> like {"music:14254","art:13589","math:58961"}*/
		Set<String> result = new HashSet<String>();
		try {
			PreparedStatement checkUsername = this.connection
					.prepareStatement("SELECT courses_list.name,courses_list.cid FROM staff_courses,courses_list WHERE courses_list.cid = staff_courses.cid AND staff_courses.pid = (SELECT pid FROM staff WHERE username = ?)");
			checkUsername.setString(1, lecturerUsername);
				
			try (ResultSet resultSet = checkUsername.executeQuery()) {
				while (resultSet.next()) {
					String currentCourse = resultSet.getString("name") + ":" + resultSet.getInt("cid"); //gives name of course on each line of results					
					result.add(currentCourse); //adds each course line by line to the set				
				}
				return result;
			}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //select the whole row where username is given username
			return result; //only happens if no result for sql statement with given username
	}
	
	
	/*this method is used for student(studentUsername) to get his/her coursesSet containing "coursename:cid"*/
	public Set<String> listOfCoursesDetailsStudent(String studentUsername){
		/**
		 * get the coursename and correspoding cid of courses for studentUsername,
		 * put this in a Set<String> like {"music 14254","art 13589","math 58961"}*/
		Set<String> result = new HashSet<String>();
		try {
			PreparedStatement checkUsername = this.connection
					.prepareStatement("SELECT courses_list.name, courses_list.cid FROM student_courses,courses_list "
							+ "WHERE courses_list.cid = student_courses.cid AND student_courses.sid = (SELECT sid FROM student WHERE username = ?)");
			checkUsername.setString(1, studentUsername);
			
			try (ResultSet resultSet = checkUsername.executeQuery()) {
				while (resultSet.next()) {
					String currentCourse = resultSet.getString("name") + ":" + resultSet.getInt("cid"); //gives name of course on each line of results
					result.add(currentCourse); //adds each course line by line to the set		
				}
				return result;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //select the whole row where username is given username
		return result; //only happens if no result for sql statement with given username
	}
	
	
	//this method is used to add an attendance code and should be used for when staff member wants to create an attendance code for one of their courses
		public boolean addAttendanceCode(int cid, String inputCode, BigInteger beginTime, BigInteger endTime) {
			/**this method should first check whether there is the same attendance(same cid, begintime and endtime)
			 * if there is no duplicate recording in table, add this attendance to table and return true
			 * else if fail to add, return false*/
			try {
				PreparedStatement checkStatement = connection
						.prepareStatement("SELECT * FROM  attendance WHERE cid = ? AND ((begintime<=? AND endtime>=?) OR (begintime<=? AND endtime>=?)) ");
				
				checkStatement.setInt(1, cid);
				checkStatement.setObject(2, beginTime);
				checkStatement.setObject(3, beginTime);
				checkStatement.setObject(4, endTime);
				checkStatement.setObject(5, endTime);
				
				try(ResultSet resultSet = checkStatement.executeQuery()){
					if(resultSet.next()) {
						return false;
					}
				}
				PreparedStatement insertStatement = connection
						.prepareStatement("INSERT INTO  attendance (cid, beginTime, endTime, code) VALUES (?,?,?,?) ");
				
				insertStatement.setInt(1, cid);
				insertStatement.setObject(2, beginTime);
				insertStatement.setObject(3, endTime);
				insertStatement.setString(4, inputCode);   
				insertStatement.execute();
				return true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false; //shouldnt get here
			}
		}
	
		/*this method is used for lecturer(lecturerUsername) to get his/her attendenceList of certain course(courseName)*/
		public String getAttendanceListForLecturer(String lecturerUsername,String courseCid) throws SQLException {
			/**get attendances' time and code, and combine them as "begintime1-endtime1: code1&begintime2-endtime2: code2&begintime3-endtime3: code3"
			 * then store this in attendenceList*/
			String attendanceList="";
			
			try( PreparedStatement Statement = this.connection.prepareStatement("SELECT begintime,endtime,code FROM attendance WHERE cid=?");) {
				System.out.println("DC0");
				Statement.setInt(1, Integer.parseInt(courseCid));
				System.out.println("DC1");
			try (ResultSet resultSet = Statement.executeQuery()) {
				int test = 0;
					while (resultSet.next()) {
						test = 1;
						String eachAttendance = resultSet.getObject("begintime") + "-" + resultSet.getObject("endtime") + ": " + resultSet.getString("code") + "&";
						attendanceList += eachAttendance;		
						}
					if (test==1) {
						attendanceList = attendanceList.substring(0, attendanceList.length()-1);
					}
					
					}
			}
			
			return attendanceList;
		}
		
	
		/*this method is for lecturer to delete one attendance for certain course*/
		public void deleteAvailableAttendance(String lecturerUsername,int courseCid,BigInteger beginTime,BigInteger endTime) {
			try( PreparedStatement deleteStatement = this.connection.prepareStatement("DELETE FROM attendance WHERE cid=? AND begintime=? AND endtime=?");) {
				deleteStatement.setInt(1, courseCid);
				deleteStatement.setObject(2, beginTime);
				deleteStatement.setObject(3, endTime);
				
				deleteStatement.execute();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
		public boolean checkAttendanceTime(String studentUsername,int courseCid,BigInteger tryTime){
			/**
			 * check whether a student(studentUsername) has attendance (for courseCid) to take at (tryTime,e.g 201902131705)
			 * if true then there is an attendance now*/
			System.out.println("DAT-2");
			try( PreparedStatement Statement = this.connection.prepareStatement("SELECT * FROM attendance WHERE cid=? AND begintime<? AND endtime>?");) {
				System.out.println("DAT-1");
				Statement.setInt(1, courseCid);
				Statement.setObject(2, tryTime);
				Statement.setObject(3, tryTime);
				System.out.println("DAT0");
				try (ResultSet resultSet = Statement.executeQuery()) {
					System.out.println("DAT1");
						if (resultSet.next()) {
							int annid = resultSet.getInt("attendid");
							int sid = 0;
							try (
									PreparedStatement annStatement = this.connection
											.prepareStatement("SELECT * FROM student WHERE username=?");) {

									annStatement.setString(1,studentUsername);
									
									try(ResultSet result = annStatement.executeQuery()){
										if(result.next()) {
											sid = result.getInt("sid");
										}
									}
								}
							try (
									
									
									PreparedStatement statement = this.connection
											.prepareStatement("SELECT * FROM student_attend WHERE attendid=? AND sid=?");) {
								System.out.println("a:" + annid);
								System.out.println("sid:" + sid);
									statement.setInt(1,annid);
									statement.setInt(2,sid);
									
									try(ResultSet result = statement.executeQuery()){
										if(result.next()) {
											return false;
										}
									}
								}
								return true;	
							}
						}
				System.out.println("DAT2");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return false;
		}
	
	public String getCurrentAttendentCode(String studentUsername,int courseCid,int tryTime){
		/**
		 * get the attendance code of courseCid for studentUsername which is valid at tryTime*/
		try( PreparedStatement Statement = this.connection.prepareStatement("SELECT code FROM attendance WHERE cid=? AND begintime>? AND endtime<?");) {
			
			Statement.setInt(1, courseCid);
			Statement.setInt(2, tryTime);
			Statement.setInt(3, tryTime);
			try (ResultSet resultSet = Statement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getString("code");		
					}
				}
		
			//shouldn't reach here unless no code to be found
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return " ";
	}
	
	//this method is used for a student to attend a particular attendance quiz
		public boolean attend(String studentUsername, int courseCid, BigInteger time, String inputCode) {
			/**TO DO:
			 * Junlin changed the parameter for this method
			 * Oliver will change the method body corresponding
			 * this method will change the status of an attendance and return true
			 * or if the inputCode is wrong,it will do nothing and return false*/
			try( PreparedStatement Statement = this.connection.prepareStatement("SELECT * FROM attendance WHERE cid=? AND begintime<? AND endtime>?");) {
				int sid = 0;
				Statement.setInt(1, courseCid);
				Statement.setObject(2, time);
				Statement.setObject(3, time);
				try (ResultSet resultSet = Statement.executeQuery()) {
					if (resultSet.next()) {
						if(inputCode.equals(resultSet.getString("code"))){
							int attendid = resultSet.getInt("attendid");
							try( PreparedStatement getsid = this.connection.prepareStatement("SELECT sid FROM student WHERE username = ?");){
								System.out.println(studentUsername);
								getsid.setString(1, studentUsername);
								
								try(ResultSet sidresult = getsid.executeQuery()){
									if(sidresult.next()) {
									 sid = sidresult.getInt("sid");
									 System.out.println(sid);
									
									}
								}
								
							}
						
							try( PreparedStatement addStatement = this.connection.prepareStatement("INSERT INTO student_attend (attendid, sid, cid) VALUES (?,?,?)");) {
								addStatement.setInt(1, attendid);
								addStatement.setInt(2, sid);
								addStatement.setInt(3, courseCid);
								return (!addStatement.execute());
								
							}
							}
						}
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return false;
			}	

/**
 * ANNOUNCEMTNET BELOW
 * */
	

	
	//this method is for LECTURER AND STUDENT to get list of announcements of courseCid as a string
	public String getAnnouncementListForCourse(int courseCid) throws SQLException {
		String announcementList="";
		/**
		 * get announcement time, and combine them as "time1 pid1&time2 pid2"
		 * then store this in announcementList*/
		try (
				PreparedStatement returnStatement = this.connection
						.prepareStatement("SELECT * FROM announcement WHERE cid=?");) {
			
			returnStatement.setInt(1, courseCid);
			
			try(ResultSet resultSet = returnStatement.executeQuery()){
				while(resultSet.next()) {
					announcementList += resultSet.getObject("time") + " " + resultSet.getInt("pid") + "&";
				}
			}
		}
			return announcementList;
	}
	
	//this method get the list of announcements for certain course which were read by this student 
	public String getReadAnnouncementForStudent(int courseCid,String studentUsername) throws SQLException {
		/**TO DO:
		 * firstly, select all announcementIDs(AnouID1 from Student_read_announcement table where studentUsername=this.studentUsername
		 * secondly, select all announcementIDs(AnouID2) from Announcement table where courseCid=this.courseCid
		 * thirdly, get intersection(AnnouID3) of AnouuID1 and AnnouID2
		 * finally, return corresponding String "time1 pid1&time2 pid2&time3 pid3" of AnnouID3*/
		String announcementList = "";
		int a = 0;
		String annid = "";
		try (
				PreparedStatement sid = this.connection.prepareStatement("SELECT sid FROM student WHERE username=?");){
			sid.setString(1, studentUsername);
			try(ResultSet resultSet = sid.executeQuery()){
				if(resultSet.next()) {
					a = resultSet.getInt("sid");
				}
			}
		}
		try (
				PreparedStatement ann = this.connection.prepareStatement("SELECT annid FROM announcement WHERE cid=? INTERSECT SELECT annid FROM student_read_announcement WHERE sid=?");){
			ann.setInt(1, courseCid);
			ann.setInt(2,a);
			try(ResultSet resultSet = ann.executeQuery()){
				while(resultSet.next()) {
					annid = resultSet.getString("annid");
					try (
							PreparedStatement anno = this.connection.prepareStatement("SELECT time,pid FROM announcement WHERE annid=?");){
						anno.setInt(1, Integer.parseInt(annid));//ZHUYI
						try(ResultSet result = anno.executeQuery()){
							if(result.next()) {
								announcementList += result.getObject("time") + " " + result.getInt("pid") + "&";

							}
						}
					}
					
				}
			}
		}
		
		return announcementList;
	}
	
	//this method get the list of announcements for certain course which are not read by this student yet
	public String getUnreadAnnouncementForStudent(int courseCid,String studentUsername) throws SQLException {
		/**TO DO:
		 * firstly, select all announcementIDs(AnouID1 from Student_read_announcement table where studentUsername=this.studentUsername
		 * secondly, select all announcementIDs(AnouID2) from Announcement table where courseCid=this.courseCid
		 * thirdly, get intersection(AnnouID3) of AnouuID1 and AnnouID2
		 * then, get AnnouID4=AnnouID2-AnnouID3,which means announcementID for this courseCid but not in the Student_read_announcement table 
		 * finally, return corresponding String "time1 pid1&time2 pid2&time3 pid3" of AnnouID4*/
		String announcementList = "";
		try (
				PreparedStatement annStatement = this.connection
						.prepareStatement("SELECT * FROM announcement WHERE cid=? AND announcement.annid NOT IN(SELECT annid FROM student_read_announcement WHERE cid=?)");) {
			
			annStatement.setInt(1, courseCid);
			annStatement.setInt(2, courseCid);
			try(ResultSet resultSet = annStatement.executeQuery()){
				while(resultSet.next()) {
					announcementList += resultSet.getObject("time") + " " + resultSet.getInt("pid") + "&";
				}
			}
		}
		return announcementList;
	}
	
	//method to store announcement text
	public void storeAnnouncement(BigInteger Time, int cid, String lecturerUsername, String announcement) throws SQLException {
		int pid = 0;
		try (
			PreparedStatement insertStatement = this.connection
					.prepareStatement("INSERT INTO announcement (time,cid,pid,context) VALUES (?,?,?,?) ");) {
			
			try(PreparedStatement getPid = this.connection.prepareStatement("SELECT pid FROM staff WHERE username =?");){
				getPid.setString(1,lecturerUsername);
				try(ResultSet resultSet = getPid.executeQuery()){
					if(resultSet.next()) {
						pid = resultSet.getInt("pid");
					}
				}
			}
			insertStatement.setObject(1, Time);
			insertStatement.setInt(2, cid);
			insertStatement.setInt(3, pid);
			insertStatement.setString(4, announcement);
		
			insertStatement.executeUpdate();
		}
	
	}
	
	
	/*this method selects and returns the content of an announcement by its time, pid and cid*/
	public String openAnnouncementContentForCourse(BigInteger time,int pid,int cid) throws SQLException{
		String context = "";
		try (
				PreparedStatement returnStatement = this.connection
						.prepareStatement("SELECT context FROM announcement WHERE time=? AND pid=? AND cid=?");) {
			
			returnStatement.setObject(1, time);
			returnStatement.setInt(2, pid);
			returnStatement.setInt(3, cid);
			
			try(ResultSet resultSet = returnStatement.executeQuery()){
				if (resultSet.next()) {
					context = resultSet.getString("context");
				}
			}
		}
		return context;
	}

	
	/*this method add a recording to the studentReadAnnounce table*/
	public void studentReadAnnouncementForFirstTime(int announcementID,String studentUsername) throws SQLException {
		
		int sid = 0;
		try (
				PreparedStatement insStatement = this.connection
						.prepareStatement("INSERT INTO student_read_announcement (annid,sid) VALUES (?,?)");) {
			try(PreparedStatement getSid = this.connection.prepareStatement("SELECT sid FROM student WHERE username =?");){
				getSid.setString(1,studentUsername);
				try(ResultSet resultSet = getSid.executeQuery()){
					if(resultSet.next()) {
						sid = resultSet.getInt("sid");
					}
				}
			}
			
			insStatement.setObject(1, announcementID);
			insStatement.setInt(2, sid);
			insStatement.execute();
		}
	}
	
	/*this method get the number of student by whom this announcement was read*/
	public int countAnnouncementReader(BigInteger announcementTime,int announcementPid,int courseCid) throws SQLException {
		/**TO DO:
		 * use the three parameter to get announcementID first,
		 * and count how many times this announcementID appears in studentReadAnnounce table*/
		int announcementId = 0;
		int result = 0;
		try (
				PreparedStatement annStatement = this.connection
						.prepareStatement("SELECT annid FROM announcement WHERE time=? AND pid=? AND cid=?");) {

				annStatement.setObject(1,announcementTime);
				annStatement.setObject(2,announcementPid);
				annStatement.setObject(3,courseCid);
				try(ResultSet resultSet = annStatement.executeQuery()){
					if(resultSet.next()) {
						announcementId = resultSet.getInt("annid");
					}
				}
			}
		try (
				PreparedStatement countStatement = this.connection
						.prepareStatement("SELECT COUNT(annid) FROM student_read_announcement WHERE annid=?");) {

				countStatement.setInt(1,announcementId);
				
				try(ResultSet resultSet = countStatement.executeQuery()){
					if(resultSet.next()) {
						 result = resultSet.getInt("count");
					}
				}
		}
			return result;	
	}
	
	/*this method get all studentUsername and commentTime of comments from comments table and combine them togather*/
	public String getCommentList(BigInteger announcementTime,int announcementPid,int courseCid) throws SQLException{
		/**TO DO:
		 * use the three parameter to get announcementID first,
		 * and use this announcementID to select its comments information in comments table,
		 * combine the informations as "studentUsername1:commentTime1+studentUsername2:commentTime2"*/
		int announcementId = 0;
		int sid = 0;
		String content = "";
		String username = "";
		//int time = 0;
		BigInteger time = new BigInteger("0");
		try (
				PreparedStatement annStatement = this.connection
						.prepareStatement("SELECT annid FROM announcement WHERE time=? AND pid=? AND cid=?");) {

				annStatement.setObject(1,announcementTime);
				annStatement.setObject(2,announcementPid);
				annStatement.setObject(3,courseCid);
				try(ResultSet resultSet = annStatement.executeQuery()){
					if(resultSet.next()) {
						announcementId = resultSet.getInt("annid");
					}
				}
			}
		try (
				PreparedStatement contStatement = this.connection
						.prepareStatement("SELECT * FROM comments WHERE annid=?");) {

				contStatement.setObject(1,announcementId);
				
				try(ResultSet resultSet = contStatement.executeQuery()){
					while(resultSet.next()) {
						sid = resultSet.getInt("sid");
						
						time = BigInteger.valueOf((long) resultSet.getObject("commenttime"));
						
						try (
								PreparedStatement Statement = this.connection
										.prepareStatement("SELECT * FROM student WHERE sid=?");) {

								Statement.setInt(1,sid);
//								Statement.setObject(2, time);
//								Statement.setObject(3,announcementId);
								try(ResultSet res = Statement.executeQuery()){
									if(res.next()) {
										username = res.getString("username");
										content += username + ":" + time + "#";
									}
								}
							}
						
					}
				}
			}
		return content;
	}
	
	/*this method get the content of one comment*/
	public String getCommentContent(int cid,BigInteger announcementTime,int announcementPid,String studentUsername,BigInteger commentTime) throws SQLException {
		int annid = 0;
		String cont = "";
		int sid = 0;
		try (
				PreparedStatement annStatement = this.connection
						.prepareStatement("SELECT annid FROM announcement WHERE time=? AND pid=? AND cid=?");) {

				annStatement.setObject(1,announcementTime);
				annStatement.setInt(2,announcementPid);
				annStatement.setInt(3,cid);
				try(ResultSet resultSet = annStatement.executeQuery()){
					if(resultSet.next()) {
						annid = resultSet.getInt("annid");
					}
				}
			}
		try (
				PreparedStatement annStatement = this.connection
						.prepareStatement("SELECT sid FROM student WHERE username=?");) {

				annStatement.setString(1,studentUsername);
				
				try(ResultSet resultSet = annStatement.executeQuery()){
					if(resultSet.next()) {
						sid = resultSet.getInt("sid");
					}
				}
			}
		try (
				PreparedStatement annStatement = this.connection
						.prepareStatement("SELECT content FROM comments WHERE annid=? AND sid=? AND commenttime=?");) {

				annStatement.setInt(1,annid);
				annStatement.setInt(2,sid);
				annStatement.setObject(3,commentTime);
				try(ResultSet resultSet = annStatement.executeQuery()){
					if(resultSet.next()) {
						cont = resultSet.getString("content");
					}
				}
			}
		return cont;
		/**TO DO:
		 * first get announcementID by cid,announcementTime and announcementPid
		 * then get comment content by announcementID,studentUsername and commentTime
		 * return content*/
	}
	
	/*this method return the announcmentID for certain anouncement by its cid, pid and annoucementTime*/
	public String getAnnouncementID(int cid,int pid,BigInteger announcementTime) throws SQLException {
		int annid = 0;
		try (
				PreparedStatement annStatement = this.connection
						.prepareStatement("SELECT annid FROM announcement WHERE time=? AND pid=? AND cid=?");) {

				annStatement.setObject(1,announcementTime);
				annStatement.setInt(2,pid);
				annStatement.setInt(3,cid);
				try(ResultSet resultSet = annStatement.executeQuery()){
					if(resultSet.next()) {
						annid = resultSet.getInt("annid");
					}
				}
			}
		return "" + annid;
	}
	
	/*this method store a comment into comment table*/
	public void storeComment(int announcementID,BigInteger commentStoredTime,String studentUsername,String commentText) throws SQLException {
		int sid = 0;
		try (
				PreparedStatement annStatement = this.connection
						.prepareStatement("SELECT sid FROM student WHERE username=?");) {

				annStatement.setString(1,studentUsername);
				
				try(ResultSet resultSet = annStatement.executeQuery()){
					if(resultSet.next()) {
						sid = resultSet.getInt("sid");
					}
				}
			}
		try (
				PreparedStatement annStatement = this.connection
						.prepareStatement("INSERT INTO comments (annid, sid, commenttime,content) VALUES (?,?,?,?)");) {

				annStatement.setInt(1,announcementID);
				annStatement.setInt(2,sid);
				annStatement.setObject(3,commentStoredTime);
				annStatement.setString(4, commentText);
				annStatement.execute();		
				}
	}
	
/**
 * QUIZ BELOW
 * */	
	
	
	
	//method to add questions 
	public void addQuestions(int cid, String question, String a, String b, String c, String d, String answer ) throws SQLException {
		try (
			PreparedStatement insertStatement = this.connection
					.prepareStatement("INSERT INTO questions (cid,question,a,b,c,d,answer) VALUES (?,?,?,?,?,?,?) ");) {

		
			insertStatement.setInt(1, cid);
			insertStatement.setString(2, question);
			insertStatement.setString(3, a);
			insertStatement.setString(4, b);
			insertStatement.setString(5, c);
			insertStatement.setString(6, d);
			insertStatement.setString(7, answer);
		
			insertStatement.executeUpdate();
		}
	
	
	}

	//method to create quiz
	public void createQuiz(String title, int cid, int q1id, int q2id, int q3id, int q4id, int q5id) throws SQLException {
		try (
			PreparedStatement insertStatement = this.connection
					.prepareStatement("INSERT INTO quizes (title,cid,released,q1id,q2id,q3id,q4id,q5id) VALUES (?,?,?,?,?,?,?,?) ");) {

	
			insertStatement.setString(1, title);
			insertStatement.setInt(2, cid);
			insertStatement.setInt(3, 0); //0 means false as in not released
			insertStatement.setInt(4, q1id);
			insertStatement.setInt(5, q2id);
			insertStatement.setInt(6, q3id);
			insertStatement.setInt(7, q4id);
			insertStatement.setInt(8, q5id);
		
			insertStatement.executeUpdate();
		}
	}
	
//	//this method is used to delete a quiz
//	public void deleteQuiz(int quizID) {
//		/**NOTICE:
//		 * This method should delete a quiz in quiz_table and the quiz_student table/quiz_lecturer table at samw time!!!*/
//	}
	
	//this method is used to change the status of a quiz to released
	public void releaseQuiz(int Quizid) throws SQLException {
		try (
			PreparedStatement updateStatement = this.connection.prepareStatement("UPDATE quizzes SET released=1 WHERE quizid=Quizid");) {
			updateStatement.executeUpdate();
		}
	}
	
//	//this method is used to get available quiz list for lecturer(quiz not released)
//	public String getAvailableQuizListForLecturer(int courseCid) {
//		/**TO DO:
//		 * this method return a string like "quizTitle1:quizID1&quizTitle2:quizID2"*/
//		
//	}
	
//	//this method is used to get released quiz list for lecturer(quiz has been released)
//	public String getReleasedQuizListForLecturer(int courseCid) {
//		/**TO DO:
//		 * this method return a string like "quizTitle1:quizID1&quizTitle2:quizID2"*/
//		
//	}

	
	
//public boolean checkAttendanceCode(String inputCode) {
//		
//		try {
//			PreparedStatement checkUsername = this.connection
//					.prepareStatement("Select statement to get attendance code for todays date ");
//			
//			
//			try (ResultSet resultSet = checkUsername.executeQuery()) {
//				while (resultSet.next()) {
//
//					
//
//					
//					String attendanceCode = resultSet.getString("codes"); //gets attendance code for given date
//					if(inputCode.equals(attendanceCode)) {
//						return true; //this only occurs when input code matches code for the correct date
//						
//						
//					}
//					
//					
//
//					
//				}
//				
//			}
//
//			
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} //select the whole row where username is given username
//		return false; //only happens if code incorrect
//					
//				
//	}
	
}

