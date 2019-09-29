//package Server;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.io.PrintStream;
//import java.math.BigInteger;
//import java.net.Socket;
//import java.sql.SQLException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Set;
//
//import Controller.Protocal;
//import Database.DatabaseConnection;
//
//public class CheckClient extends Thread{
//
//	private Socket s;
//	private BufferedReader br;
//	private BufferedWriter  out;
////	private PrintStream out;
//	private String details[];
//	private DatabaseConnection dc;
//	
//	public CheckClient(Socket s,DatabaseConnection dc) throws Exception {
//		this.s=s;
//		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
//		out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
//	//	out = new PrintStream(s.getOutputStream());
//		this.dc = dc;
//	}
//	
//	public void run() {
//	try {
//		
//		/*keep read username&password from client and do corresponding operation*/
//		while(true) {
//			try {
//				/*details[0] is username, details[1] is password*/
//				details=br.readLine().split("&");
//			} catch (IOException e) {
//				//s.close();锟斤拷确锟斤拷锟斤拷锟绞э拷艿幕锟斤拷俅纬锟斤拷值牡锟铰硷拷锟斤拷诨锟斤拷遣锟斤拷锟斤拷锟酵伙拷锟絚lient
//				//Thread.yield();
//				e.printStackTrace();
//			}
//			
//			/*First, check whether this account has connected with Server*/
//			if(Server.lecturers.containsKey(details[0])||Server.students.containsKey(details[0])) {
//				try {
//					/*if this account has been save in map, send 2 back to client to tell login failure*/
//					out.write(3);
//					out.flush();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			/*Then we check whether the username and password is valid for LECTURER by searching in database*/
//			else if(details[0].startsWith("L")&&dc.checkStaffDetails(details[0],details[1])) {
//				try {
//					/*if this account has not logged in and the details are correct, send 2 back to tell login success*/
//					out.write(2);
//					out.flush();
//					
//					/*store the socket of Server connected to client and corresponding username 
//					 * in UserConnectionMap<String,Socket> staffs*/
//					Server.lecturers.put(details[0], s);
//					
//					/*send courses names and cid of lecturer back to client details[0] for making the courses_list interface*/
//					sendLecturerCoursesListBack(details[0]);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				break;
//			}
//			/*Then we check whether the username and password is valid for STUDENT by searching in database*/
//			else if(details[0].startsWith("S")&&dc.checkStudentDetails(details[0],details[1])) {
//				try {
//					/*if this account has not logged in and the details are correct, send 1 back to tell login success*/
//					out.write(1);
//					out.flush();
//					
//					/*store the socket of Server connected to client and corresponding username 
//					 * in UserConnectionMap<String,Socket> students*/
//					Server.students.put(details[0], s);
//					
//					/*send courses names of student back to client details[0] for making the courses_list interface*/
//					sendStudentCoursesListBack(details[0]);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//				break;
//			}
//			else {
//				/*if this account has not logged in but the details are wrong, send 0 back to tell login failure*/
//				try {
//					out.write(0);
//					out.flush();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//		interact();
//	}
//	catch(Exception e) {
//		try {
//			out.close();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		try {
//			br.close();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		try {
//			s.close();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//	}
//		
//		
//	}
//
//
//	/*send courses names of lecturer back to client*/
//	public void sendLecturerCoursesListBack(String username){
//		String coursesList=null;
//		Set<String> courses = dc.listOfCoursesDetailsStaff(username);
//
//		for(String course: courses) {
//			coursesList=coursesList+"&"+course;//锟斤拷锟斤拷coursesList锟斤拷锟斤拷null锟斤拷头锟侥ｏ拷锟斤拷锟杰伙拷影锟斤拷纬瘫锟斤拷锟斤拷锟斤拷锟斤拷锟�
//		}
//		try {
//			out.write(coursesList+"\n");
//			out.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/*send courses names of student back to client*/
//	public void sendStudentCoursesListBack(String username){
//		String coursesList="";
//		Set<String> courses = dc.listOfCoursesDetailsStudent(username);
//
//		for(String course: courses) {
//			coursesList=coursesList+"&"+course;
//		}
//		try {
//			out.write(coursesList+"\n");
//			out.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	/*Server keep dealing with different request from client*/
//	public void interact() {
//		while(true) {
//			try {
//				String line=br.readLine();
//				
//				if(line.startsWith(Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER)&&line.endsWith(Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER)) {
//					
//					String courseCid=Protocal.getRealMsg(line).split(":")[1];//this is cid
//					//String courseCid = "2";
//					System.out.println(courseCid);
//					
//					String lecturerUsername=Server.lecturers.getKeyByValue(s);
//					
//					String attendanceList=dc.getAttendanceListForLecturer(lecturerUsername,courseCid);
//					
//					System.out.println(attendanceList);
//					out.write(attendanceList + "\n" + "\n");
//					out.flush();
//				}
//				else if(line.startsWith(Protocal.OPEN_ATTENDENT_GUI_FOR_STUDENT)&&line.endsWith(Protocal.OPEN_ATTENDENT_GUI_FOR_STUDENT)) {
//					System.out.println("satten1");
//					String courseCid=Protocal.getRealMsg(line).split(":")[1];//this is cid
//					System.out.println("satten2");
//					Date attendenceTryTime=new Date();
//					SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHHmm");
//					String tryTime=ft.format(attendenceTryTime);
//					System.out.println("satten3");
//					String studentUsername=Server.students.getKeyByValue(s);
//					BigInteger tryTimeBI=new BigInteger(tryTime);
//					boolean att=dc.checkAttendanceTime(studentUsername,Integer.parseInt(courseCid),tryTimeBI);
//					
//					if(att) {
//						/*if there is an attendance to take*/
//						out.write(1);
//						out.flush();
//						
//						String code=br.readLine();
//						if(code.equals("GIVEUP")) {
//							break;//锟斤拷止bug
//						}
//						else if(dc.attend(studentUsername,Integer.parseInt(courseCid),tryTimeBI,code)) {
//							/*if the attend code is correct and attend successfully*/
//							out.write(1);
//							out.flush();
//						}
//						else {
//							out.write(0);
//							out.flush();
//						}
//						
//					}
//					else {
//						/*if there is no attendance to take*/
//						out.write(0);
//						out.flush();
//					}
//				}
//				else if(line.startsWith(Protocal.LECTURER_DELETE_AN_ATTENDENCE)&&line.endsWith(Protocal.LECTURER_DELETE_AN_ATTENDENCE)) {
//					String attendanceDetails=Protocal.getRealMsg(line);//this is the "beginTime-endTime&coursename cid" of an attendance
//					String beginTime=attendanceDetails.split("&")[0].split("-")[0];
//					String endTime=attendanceDetails.split("&")[0].split("-")[1];
//					String courseName=attendanceDetails.split("&")[1].split(":")[0];
//					String courseCid=attendanceDetails.split("&")[1].split(":")[1];
//					String lecturerUsername=Server.lecturers.getKeyByValue(s);
//					BigInteger beginTimeBI=new BigInteger(beginTime);
//					BigInteger endTimeBI=new BigInteger(endTime);
//					
//					
//					dc.deleteAvailableAttendance(lecturerUsername,Integer.parseInt(courseCid),beginTimeBI,endTimeBI);
//
//				}
//				else if(line.startsWith(Protocal.LECTURER_ADD_NEW_ATTENDENCE)&&line.endsWith(Protocal.LECTURER_ADD_NEW_ATTENDENCE)) {
//					String attendanceDetails=Protocal.getRealMsg(line);//this is the "courseCid&beginTime&endTime&newCode" of a new attendance
//					String courseCid=attendanceDetails.split("&")[0].split(":")[1];
//					System.out.println(courseCid);
//					String beginTime=attendanceDetails.split("&")[1];
//					String endTime=attendanceDetails.split("&")[2];
//					String newCode=attendanceDetails.split("&")[3];
//					System.out.println(attendanceDetails);
//					boolean success=dc.addAttendanceCode(Integer.parseInt(courseCid), newCode, Integer.parseInt(beginTime), Integer.parseInt(endTime));
//					System.out.println("WORK1");
//					if(success) {
//						System.out.println("WORK2");
//						out.write(1);
//						out.flush();
//					}
//					else {
//						System.out.println("WORK3");
//						out.write(0);
//						out.flush();
//					}
//				}
//				else if(line.startsWith(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER)&&line.endsWith(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER)) {
//					String courseCid=Protocal.getRealMsg(line).split(":")[1];//this is cid
//					
//					String announcementList=dc.getAnnouncementListForCourse(Integer.parseInt(courseCid));//"time1 pid1&time2 pid2"
//					out.write(announcementList+"\n");
//					out.flush();
//				}
//				else if(line.startsWith(Protocal.LECTURER_ADD_NEW_ANNOUNCEMENT)&&line.endsWith(Protocal.LECTURER_ADD_NEW_ANNOUNCEMENT)) {
//					String courseCid=Protocal.getRealMsg(line).split("&")[0].split(":")[1];//this is cid
//					String announcementContent=Protocal.getRealMsg(line).split("&")[1];
//					Date announcementCreatedTime=new Date();
//					SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHHmm");
//					String storedTime=ft.format(announcementCreatedTime);
//					String lecturerUsername=Server.lecturers.getKeyByValue(s);
//					
//					dc.storeAnnouncement(Integer.parseInt(storedTime), Integer.parseInt(courseCid), lecturerUsername, announcementContent);
//				}
//				else if(line.startsWith(Protocal.LECTURER_OPEN_AN_ANNOUNCEMENT)&&line.endsWith(Protocal.LECTURER_OPEN_AN_ANNOUNCEMENT)) {
//					String announcementTime=Protocal.getRealMsg(line).split("&")[0].split(" ")[0];
//					String announcementPid=Protocal.getRealMsg(line).split("&")[0].split(" ")[1];
//					String courseCid=Protocal.getRealMsg(line).split("&")[1];
//					
//					String announcementContent=dc.openAnnouncementContentForCourse(Integer.parseInt(announcementTime),Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
//					int readerCounter=dc.countAnnouncementReader(Integer.parseInt(announcementTime),Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
//					String commentsStudentUsernameTime=dc.getCommentList(Integer.parseInt(announcementTime),Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
//				
//					out.write(readerCounter+"&"+announcementContent+"&"+commentsStudentUsernameTime+"\n");
//					out.flush();
//				}
//				else if(line.startsWith(Protocal.OPEN_COMMENT_FOR_LECTURER)&&line.endsWith(Protocal.OPEN_COMMENT_FOR_LECTURER)) {
//					String commentDetail=Protocal.getRealMsg(line);
//					String cid=commentDetail.split("&")[0];
//					String announcementTime=commentDetail.split("&")[1].split(" ")[0];
//					String announcementPid=commentDetail.split("&")[1].split(" ")[1];
//					String studentUsername=commentDetail.split("&")[2].split(":")[0];
//					String commentTime=commentDetail.split("&")[2].split(":")[1];
//					
//					String commentContent=dc.getCommentContent(Integer.parseInt(cid),Integer.parseInt(announcementTime),Integer.parseInt(announcementPid),studentUsername,Integer.parseInt(commentTime));
//					
//					out.write(commentContent+"/n");
//					out.flush();
//				}
//				else if(line.startsWith(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_STUDENT)&&line.endsWith(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_STUDENT)) {
//					String courseCid=Protocal.getRealMsg(line).split(":")[1];//this is cid
//					String studentUsername=Server.students.getKeyByValue(s);
//					String readAnnouncementList=dc.getReadAnnouncementForStudent(Integer.parseInt(courseCid),studentUsername);
//					String unreadAnnouncementList=dc.getUnreadAnnouncementForStudent(Integer.parseInt(courseCid),studentUsername);
//					String annoucementList=readAnnouncementList+"+"+unreadAnnouncementList;
//					out.write(annoucementList+"\n");
//					out.flush();
////					String announcementList=dc.getAnnouncementListForCourse(Integer.parseInt(courseCid));
////					out.write(announcementList+"\n");
////					out.flush();
//				}
//				else if(line.startsWith(Protocal.STUDENT_REVIEW_AN_ANNOUNCEMENT)&&line.endsWith(Protocal.STUDENT_REVIEW_AN_ANNOUNCEMENT)) {
//					String announcementTime=Protocal.getRealMsg(line).split("&")[0].split(" ")[0];
//					String announcementPid=Protocal.getRealMsg(line).split("&")[0].split(" ")[1];
//					String courseCid=Protocal.getRealMsg(line).split("&")[1];
//					String announcementContent=dc.openAnnouncementContentForCourse(Integer.parseInt(announcementTime),Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
//					String commentsStudentUsernameTime=dc.getCommentList(Integer.parseInt(announcementTime),Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
//					out.write(announcementContent+"&"+commentsStudentUsernameTime+"\n");
//					out.flush();
//				}
//				else if(line.startsWith(Protocal.STUDENT_OPEN_NEW_ANNOUNCEMENT)&&line.endsWith(Protocal.STUDENT_OPEN_NEW_ANNOUNCEMENT)) {
//					String announcementTime=Protocal.getRealMsg(line).split("&")[0].split(" ")[0];
//					String announcementPid=Protocal.getRealMsg(line).split("&")[0].split(" ")[1];
//					String courseCid=Protocal.getRealMsg(line).split("&")[1];
//					
//					String announcementID=dc.getAnnouncementID(Integer.parseInt(courseCid),Integer.parseInt(announcementPid),Integer.parseInt(announcementTime));
//					String studentUsername=Server.students.getKeyByValue(s);
//					dc.studentReadAnnouncementForFirstTime(Integer.parseInt(announcementID),studentUsername);
//					
//					String announcementContent=dc.openAnnouncementContentForCourse(Integer.parseInt(announcementTime),Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
//					String commentsStudentUsernameTime=dc.getCommentList(Integer.parseInt(announcementTime),Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
//					out.write(announcementContent+"&"+commentsStudentUsernameTime+"\n");
//					out.flush();
//					
//				}
//				else if(line.startsWith(Protocal.OPEN_COMMENT_FOR_STUDENT)&&line.endsWith(Protocal.OPEN_COMMENT_FOR_STUDENT)) {
//					String commentDetail=Protocal.getRealMsg(line);
//					String cid=commentDetail.split("&")[0];
//					String announcementTime=commentDetail.split("&")[1].split(" ")[0];
//					String announcementPid=commentDetail.split("&")[1].split(" ")[1];
//					String studentUsername=commentDetail.split("&")[2].split(":")[0];
//					String commentTime=commentDetail.split("&")[2].split(":")[1];
//					
//					String commentContent=dc.getCommentContent(Integer.parseInt(cid),Integer.parseInt(announcementTime),Integer.parseInt(announcementPid),studentUsername,Integer.parseInt(commentTime));
//					
//					out.write(commentContent+"/n");
//					out.flush();
//				}
//				else if(line.startsWith(Protocal.STUDENT_CREATE_COMMENT)&&line.endsWith(Protocal.STUDENT_CREATE_COMMENT)) {
//					String commentDetail=Protocal.getRealMsg(line);
//					String courseCid=commentDetail.split("&")[0];
//					String announcementTime=commentDetail.split("&")[1];
//					String pid=commentDetail.split("&")[2];
//					String commentText=commentDetail.split("&")[3];
//					
//					String announcementID=dc.getAnnouncementID(Integer.parseInt(courseCid),Integer.parseInt(pid),Integer.parseInt(announcementTime));
//					Date commentCreatedTime=new Date();
//					SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHHmm");
//					String commentStoredTime=ft.format(commentCreatedTime);
//					String studentUsername=Server.students.getKeyByValue(s);
//					dc.storeComment(Integer.parseInt(announcementID),Integer.parseInt(commentStoredTime),studentUsername,commentText);
//					System.out.println("create comment successfully");
//				}
//				else if(line.startsWith(Protocal.OPEN_QUIZ_LIST_FOR_LECTURER)&&line.endsWith(Protocal.OPEN_QUIZ_LIST_FOR_LECTURER)) {
//					String courseCid=Protocal.getRealMsg(line).split(":")[1];//this is cid
//					String availableQuizList=dc.getAvailableQuizListForLecturer(Integer.parseInt(courseCid));
//					String releasedQuizList=dc.getReleasedQuizListForLecturer(Integer.parseInt(courseCid));
//					out.write(availableQuizList+"#"+releasedQuizList+"\n");
//					out.flush();
//				}
//				else if(line.startsWith(Protocal.LECTURER_RELEASE_QUIZ)&&line.endsWith(Protocal.LECTURER_RELEASE_QUIZ)) {
//					String courseCid=Protocal.getRealMsg(line).split("&")[0].split(":")[1];
//					String quizTitle=Protocal.getRealMsg(line).split("&")[1].split(":")[0];
//					String quizID=Protocal.getRealMsg(line).split("&")[1].split(":")[1];
//					dc.releaseQuiz(Integer.parseInt(quizID));
//				}
//				else if(line.startsWith(Protocal.LECTURER_DELETE_QUIZ)&&line.endsWith(Protocal.LECTURER_DELETE_QUIZ)) {
//					String quizID=Protocal.getRealMsg(line).split(":")[1];
//					dc.deleteQuiz(Integer.parseInt(quizID));
//				}
//				else if(line.startsWith(Protocal.LECTURER_ADD_NEW_QUIZ)&&line.endsWith(Protocal.LECTURER_ADD_NEW_QUIZ)) {
//					//String quizDetail=Protocal
//				}
//				
//				
//				
//				
//				
//				
//				
//				else if(line.equals(Protocal.STUDENT_BACK_TO_USER_PROFILE)) {
//					String studentUserName=Server.lecturers.getKeyByValue(s);
//					sendStudentCoursesListBack(studentUserName);
//				}
//				else if(line.equals(Protocal.LECTURER_BACK_TO_USER_PROFILE)) {
//					String lecturerUserName=Server.lecturers.getKeyByValue(s);
//					sendLecturerCoursesListBack(lecturerUserName);
//				}
//				
//				
//				
//				
//				
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
//	
//}






package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.math.BigInteger;
import java.net.Socket;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import Controller.Protocal;
import Database.DatabaseConnection;

public class CheckClient extends Thread{

	private Socket s;
	private BufferedReader br;
	private BufferedWriter  out;
//	private PrintStream out;
	private String details[];
	private DatabaseConnection dc;
	
	public CheckClient(Socket s,DatabaseConnection dc) throws Exception {
		this.s=s;
	//	out = new PrintStream(s.getOutputStream());
		this.dc = dc;
	}
	
	public void run() {
	try {
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		/*keep read username&password from client and do corresponding operation*/
		while(true) {
				/*details[0] is username, details[1] is password*/
				details=br.readLine().split("&");
			
			/*First, check whether this account has connected with Server*/
			if(Server.lecturers.containsKey(details[0])||Server.students.containsKey(details[0])) {
					/*if this account has been save in map, send 2 back to client to tell login failure*/
					out.write(3);
					out.flush();
			}
			/*Then we check whether the username and password is valid for LECTURER by searching in database*/
			else if(details[0].startsWith("L")&&dc.checkStaffDetails(details[0],details[1])) {
					/*if this account has not logged in and the details are correct, send 2 back to tell login success*/
					out.write(2);
					out.flush();
					
					/*store the socket of Server connected to client and corresponding username 
					 * in UserConnectionMap<String,Socket> staffs*/
					Server.lecturers.put(details[0], s);
					
					/*send courses names and cid of lecturer back to client details[0] for making the courses_list interface*/
					sendLecturerCoursesListBack(details[0]);
				break;
			}
			/*Then we check whether the username and password is valid for STUDENT by searching in database*/
			else if(details[0].startsWith("S")&&dc.checkStudentDetails(details[0],details[1])) {
					/*if this account has not logged in and the details are correct, send 1 back to tell login success*/
					out.write(1);
					out.flush();
					
					/*store the socket of Server connected to client and corresponding username 
					 * in UserConnectionMap<String,Socket> students*/
					Server.students.put(details[0], s);
					
					/*send courses names of student back to client details[0] for making the courses_list interface*/
					sendStudentCoursesListBack(details[0]);
				break;
			}
			else {
				/*if this account has not logged in but the details are wrong, send 0 back to tell login failure*/
					out.write(0);
					out.flush();
			}
		}
		interact();
	}	
	catch(Exception e) {
		Server.lecturers.removeByValue(s);
		Server.students.removeByValue(s);
		e.printStackTrace();
	}
	finally {
		try {
			if(out!=null)
				out.close();
			if(br!=null)
				br.close();
			if(s!=null)
				s.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	}


	/*send courses names of lecturer back to client*/
	public void sendLecturerCoursesListBack(String username){
		String coursesList="";
		Set<String> courses = dc.listOfCoursesDetailsStaff(username);

		for(String course: courses) {
			coursesList=coursesList+"&"+course;//锟斤拷锟斤拷coursesList锟斤拷锟斤拷null锟斤拷头锟侥ｏ拷锟斤拷锟杰伙拷影锟斤拷纬瘫锟斤拷锟斤拷锟斤拷锟斤拷锟�
			System.out.println(coursesList);
		}
		try {
			out.write(coursesList+"\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*send courses names of student back to client*/
	public void sendStudentCoursesListBack(String username){
		String coursesList="";
		Set<String> courses = dc.listOfCoursesDetailsStudent(username);

		for(String course: courses) {
			coursesList=coursesList+"&"+course;
		}
		try {
			out.write(coursesList+"\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*Server keep dealing with different request from client*/
	public void interact() {
		try {
			while(true) {
			
				String line=br.readLine();
				System.out.println("New Line: "+line);
				
				if(line.startsWith(Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER)&&line.endsWith(Protocal.OPEN_ATTENDENCE_LIST_FOR_LECTURER)) {
					
					String courseCid=Protocal.getRealMsg(line).split(":")[1];//this is cid
					//String courseCid = "2";
					System.out.println(courseCid + "please");
					System.out.println("please");
					String lecturerUsername=Server.lecturers.getKeyByValue(s);
					
					String attendanceList=dc.getAttendanceListForLecturer(lecturerUsername,courseCid);
					
					System.out.println(attendanceList);
					out.write(attendanceList + "\n");
					out.flush();
				}
				else if(line.startsWith(Protocal.OPEN_ATTENDENT_GUI_FOR_STUDENT)&&line.endsWith(Protocal.OPEN_ATTENDENT_GUI_FOR_STUDENT)) {
					String courseCid=Protocal.getRealMsg(line).split(":")[1];//this is cid
					Date attendenceTryTime=new Date();
					SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHHmm");
					String tryTime=ft.format(attendenceTryTime);
					String studentUsername=Server.students.getKeyByValue(s);
					System.out.println(studentUsername + ":" + courseCid + ":" + tryTime);
					BigInteger forTime = new BigInteger(tryTime);
					boolean att=dc.checkAttendanceTime(studentUsername,Integer.parseInt(courseCid),forTime);
					System.out.println(att);
					if(att) {
						/*if there is an attendance to take*/
						out.write(1);
						out.flush();
						boolean val = true;
						while(val) {
						System.out.println("this far0");
						String code=br.readLine();
						System.out.println("this far1");
						System.out.println(code);
						if(code.equals("GIVEUP")) {
							break;//锟斤拷止bug
						}
						else if(dc.attend(studentUsername,Integer.parseInt(courseCid),forTime,code)) {
							/*if the attend code is correct and attend successfully*/
							System.out.println("right code");
							val = false;
							//out.write("right code");
							out.write(1);
							out.flush();
						}
						else {
							System.out.println("wrong code");
							//out.write("wrong code");
							out.write(0);
							out.flush();
						}
						}
					}
					else {
						/*if there is no attendance to take*/
						System.out.println("HEREEEEEEE");
						out.write(0);
						out.flush();
					}
				}
				else if(line.startsWith(Protocal.LECTURER_DELETE_AN_ATTENDENCE)&&line.endsWith(Protocal.LECTURER_DELETE_AN_ATTENDENCE)) {
					String attendanceDetails=Protocal.getRealMsg(line);//this is the "beginTime-endTime&coursename cid" of an attendance
					String beginTime=attendanceDetails.split("&")[0].split("-")[0];
					String endTime=attendanceDetails.split("&")[0].split("-")[1];
					String courseName=attendanceDetails.split("&")[1].split(":")[0];
					String courseCid=attendanceDetails.split("&")[1].split(":")[1];
					String lecturerUsername=Server.lecturers.getKeyByValue(s);
					System.out.println(courseCid);
					BigInteger bTime = new BigInteger(beginTime);
					BigInteger eTime = new BigInteger(endTime);
					dc.deleteAvailableAttendance(lecturerUsername,Integer.parseInt(courseCid),bTime,eTime);
					System.out.println("performed the delete");
					out.write(1);
					out.flush();

				}
				else if(line.startsWith(Protocal.LECTURER_ADD_NEW_ATTENDENCE)&&line.endsWith(Protocal.LECTURER_ADD_NEW_ATTENDENCE)) {
					String attendanceDetails=Protocal.getRealMsg(line);//this is the "courseCid&beginTime&endTime&newCode" of a new attendance
					
					System.out.println(attendanceDetails); //I HAVE REACHED HERE 
					String courseCid=attendanceDetails.split("&")[0].split(":")[1];
					String beginTime=attendanceDetails.split("&")[1];
					String endTime=attendanceDetails.split("&")[2];
					String newCode=attendanceDetails.split("&")[3];
					BigInteger bTime = new BigInteger(beginTime);
					BigInteger eTime = new BigInteger(endTime);
					boolean success=dc.addAttendanceCode(Integer.parseInt(courseCid), newCode, bTime, eTime);
					System.out.println(success);
					if(success) {
						out.write(1);
						out.flush();
					}
					else {
						out.write(0);
						out.flush();
					}
				}
				else if(line.startsWith(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER)&&line.endsWith(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER)) {
					String courseCid=Protocal.getRealMsg(line).split(":")[1];//this is cid
					String announcementList=dc.getAnnouncementListForCourse(Integer.parseInt(courseCid));//"time1 pid1&time2 pid2"
					out.write(announcementList+"\n");
					out.flush();
				}
				else if(line.startsWith(Protocal.LECTURER_ADD_NEW_ANNOUNCEMENT)&&line.endsWith(Protocal.LECTURER_ADD_NEW_ANNOUNCEMENT)) {
					String courseCid=Protocal.getRealMsg(line).split("&")[0].split(":")[1];//this is cid
					String announcementContent=Protocal.getRealMsg(line).split("&")[1];
					Date announcementCreatedTime=new Date();
					SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHHmm");
					String storedTime=ft.format(announcementCreatedTime);
					BigInteger storedTimeBI=new BigInteger(storedTime);
					String lecturerUsername=Server.lecturers.getKeyByValue(s);
					
					dc.storeAnnouncement(storedTimeBI, Integer.parseInt(courseCid), lecturerUsername, announcementContent);
				}
				else if(line.startsWith(Protocal.LECTURER_OPEN_AN_ANNOUNCEMENT)&&line.endsWith(Protocal.LECTURER_OPEN_AN_ANNOUNCEMENT)) {
					String announcementTime=Protocal.getRealMsg(line).split("&")[0].split(" ")[0];
					BigInteger announcementTimeBI=new BigInteger(announcementTime);
					String announcementPid=Protocal.getRealMsg(line).split("&")[0].split(" ")[1];
					String courseCid=Protocal.getRealMsg(line).split("&")[1];
					
					String announcementContent=dc.openAnnouncementContentForCourse(announcementTimeBI,Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
					if(announcementContent=="") {
						announcementContent=" ";
					}
					int readerCounter=dc.countAnnouncementReader(announcementTimeBI,Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
					System.out.println("get counter");
					//="";
					String commentsStudentUsernameTime=dc.getCommentList(announcementTimeBI,Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
					if(commentsStudentUsernameTime=="") {
						commentsStudentUsernameTime=" ";
					}
					out.write(readerCounter+"&"+announcementContent+"&"+commentsStudentUsernameTime+"\n");
					System.out.println("open annouce for lect"+"comment list: "+commentsStudentUsernameTime);
					out.flush();
				}
				else if(line.startsWith(Protocal.OPEN_COMMENT_FOR_LECTURER)&&line.endsWith(Protocal.OPEN_COMMENT_FOR_LECTURER)) {
					String commentDetail=Protocal.getRealMsg(line);
					String cid=commentDetail.split("&")[0];
					String announcementTime=commentDetail.split("&")[1].split(" ")[0];
					BigInteger announcementTimeBI=new BigInteger(announcementTime);
					String announcementPid=commentDetail.split("&")[1].split(" ")[1];
					String studentUsername=commentDetail.split("&")[2].split(":")[0];
					String commentTime=commentDetail.split("&")[2].split(":")[1];
					BigInteger commentTimeBI=new BigInteger(commentTime);
					//"this is comment content test for lecturer";
					String commentContent=dc.getCommentContent(Integer.parseInt(cid),announcementTimeBI,Integer.parseInt(announcementPid),studentUsername,commentTimeBI);
					
					out.write(commentContent+"\n");
					out.flush();
				}
				else if(line.startsWith(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_STUDENT)&&line.endsWith(Protocal.OPEN_ANNOUNCEMENT_LIST_FOR_STUDENT)) {
					String courseCid=Protocal.getRealMsg(line).split(":")[1];//this is cid
					String studentUsername=Server.students.getKeyByValue(s);
					String readAnnouncementList=dc.getReadAnnouncementForStudent(Integer.parseInt(courseCid),studentUsername);
					String unreadAnnouncementList=dc.getUnreadAnnouncementForStudent(Integer.parseInt(courseCid),studentUsername);
					System.out.println("readAList:"+readAnnouncementList+"\n"+"unreadAList:"+unreadAnnouncementList);
					if(readAnnouncementList=="") {
						readAnnouncementList=" ";
					}
					if(unreadAnnouncementList=="") {
						unreadAnnouncementList=" ";
					}
					String annoucementList=readAnnouncementList+"#"+unreadAnnouncementList;
					out.write(annoucementList+"\n");
					out.flush();
					System.out.println("send announcement back successfully");

				}
				else if(line.startsWith(Protocal.STUDENT_REVIEW_AN_ANNOUNCEMENT)&&line.endsWith(Protocal.STUDENT_REVIEW_AN_ANNOUNCEMENT)) {
					String announcementTime=Protocal.getRealMsg(line).split("&")[0].split(" ")[0];
					BigInteger announcementTimeBI=new BigInteger(announcementTime);
					String announcementPid=Protocal.getRealMsg(line).split("&")[0].split(" ")[1];
					String courseCid=Protocal.getRealMsg(line).split("&")[1];
					String announcementContent=dc.openAnnouncementContentForCourse(announcementTimeBI,Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
					System.out.println("announcementContent: "+announcementContent);
					
					//="";
					String commentsStudentUsernameTime=dc.getCommentList(announcementTimeBI,Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
					System.out.println("commentsStudentUsernameTime: "+commentsStudentUsernameTime);
					if(announcementContent=="") {
						announcementContent=" ";
					}
					if(commentsStudentUsernameTime=="") {
						commentsStudentUsernameTime=" ";
					}
					out.write(announcementContent+"&"+commentsStudentUsernameTime+"\n");
					out.flush();
					System.out.println("AAAAA");
				}
				else if(line.startsWith(Protocal.STUDENT_OPEN_NEW_ANNOUNCEMENT)&&line.endsWith(Protocal.STUDENT_OPEN_NEW_ANNOUNCEMENT)) {
					String announcementTime=Protocal.getRealMsg(line).split("&")[0].split(" ")[0];
					BigInteger announcementTimeBI=new BigInteger(announcementTime);
					String announcementPid=Protocal.getRealMsg(line).split("&")[0].split(" ")[1];
					String courseCid=Protocal.getRealMsg(line).split("&")[1];
					
					String announcementID=dc.getAnnouncementID(Integer.parseInt(courseCid),Integer.parseInt(announcementPid),announcementTimeBI);
					String studentUsername=Server.students.getKeyByValue(s);
					System.out.println("dc gets announcementID");
					dc.studentReadAnnouncementForFirstTime(Integer.parseInt(announcementID),studentUsername);
					System.out.println("dc store student read an announcement");
					String announcementContent=dc.openAnnouncementContentForCourse(announcementTimeBI,Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
					//="";
					String commentsStudentUsernameTime=dc.getCommentList(announcementTimeBI,Integer.parseInt(announcementPid),Integer.parseInt(courseCid));
					if(announcementContent=="") {
						announcementContent=" ";
					}
					if(commentsStudentUsernameTime=="") {
						commentsStudentUsernameTime=" ";
					}
					out.write(announcementContent+"&"+commentsStudentUsernameTime+"\n");
					out.flush();
					
				}
				else if(line.startsWith(Protocal.OPEN_COMMENT_FOR_STUDENT)&&line.endsWith(Protocal.OPEN_COMMENT_FOR_STUDENT)) {
					String commentDetail=Protocal.getRealMsg(line);
					String cid=commentDetail.split("&")[0];
					String announcementTime=commentDetail.split("&")[1].split(" ")[0];
					BigInteger announcementTimeBI=new BigInteger(announcementTime);
					String announcementPid=commentDetail.split("&")[1].split(" ")[1];
					String studentUsername=commentDetail.split("&")[2].split(":")[0];
					String commentTime=commentDetail.split("&")[2].split(":")[1];
					BigInteger commentTimeBI=new BigInteger(commentTime);
					
					//"comment test";
					String commentContent=dc.getCommentContent(Integer.parseInt(cid),announcementTimeBI,Integer.parseInt(announcementPid),studentUsername,commentTimeBI);
					out.write(commentContent+"\n");
					out.flush();
					System.out.println("open");

				}
				else if(line.startsWith(Protocal.STUDENT_CREATE_COMMENT)&&line.endsWith(Protocal.STUDENT_CREATE_COMMENT)) {
					String commentDetail=Protocal.getRealMsg(line);
					String courseCid=commentDetail.split("&")[0];
					String announcementTime=commentDetail.split("&")[1];
					BigInteger announcementTimeBI=new BigInteger(announcementTime);
					String pid=commentDetail.split("&")[2];
					String commentText=commentDetail.split("&")[3];
					System.out.println("create comment successfully1");
					String announcementID=dc.getAnnouncementID(Integer.parseInt(courseCid),Integer.parseInt(pid),announcementTimeBI);
					System.out.println("create comment successfully2");
					Date commentCreatedTime=new Date();
					SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMddHHmm");
					String commentStoredTime=ft.format(commentCreatedTime);
					BigInteger commentStoredTimeBI=new BigInteger(commentStoredTime);
					String studentUsername=Server.students.getKeyByValue(s);
					dc.storeComment(Integer.parseInt(announcementID),commentStoredTimeBI,studentUsername,commentText);
					System.out.println("create comment successfully3");
				}
				else if(line.startsWith(Protocal.LECTURER_RELEASE_QUIZ)&&line.endsWith(Protocal.LECTURER_RELEASE_QUIZ)) {
					String courseCid=Protocal.getRealMsg(line).split("&")[0].split(":")[1];
					String quizTitle=Protocal.getRealMsg(line).split("&")[1].split(":")[0];
					String quizID=Protocal.getRealMsg(line).split("&")[1].split(":")[1];
					dc.releaseQuiz(Integer.parseInt(quizID));
				}
				else if(line.startsWith(Protocal.LECTURER_ADD_NEW_QUIZ)&&line.endsWith(Protocal.LECTURER_ADD_NEW_QUIZ)) {
					//String quizDetail=Protocal
				}
				
				
				
				
				
				
				
				else if(line.equals(Protocal.STUDENT_BACK_TO_USER_PROFILE)) {
					String studentUserName=Server.students.getKeyByValue(s);
					System.out.println(studentUserName);
					sendStudentCoursesListBack(studentUserName);
				}
				else if(line.equals(Protocal.LECTURER_BACK_TO_USER_PROFILE)) {
					System.out.println("get back to userPro request");
					String lecturerUserName=Server.lecturers.getKeyByValue(s);
					System.out.println("get username");
					sendLecturerCoursesListBack(lecturerUserName);
					System.out.println("send course list to client");
				}
				else if(line.equals("LOG OUT")) {
					s.close();
				}
				
			
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Server.lecturers.removeByValue(s);
			Server.students.removeByValue(s);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Server.lecturers.removeByValue(s);
			Server.students.removeByValue(s);
		}
		finally {
			try {
				if(out!=null)
					out.close();
				if(br!=null)
					br.close();
				if(s!=null)
					s.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}

