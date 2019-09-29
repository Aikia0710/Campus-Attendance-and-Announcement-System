package Server;

public interface Protocal {
	int length=3;
	String COURSES_NAME="+++";
	
	String OPEN_ANNOUNCEMENT_LIST_FOR_LECTURER="###";
	String OPEN_ANNOUNCEMENT_LIST_FOR_STUDENT="##*";
	
	String OPEN_ATTENDENCE_LIST_FOR_LECTURER="$$$";
	String OPEN_ATTENDENT_GUI_FOR_STUDENT="##$";
	
	String OPEN_QUIZ_LIST_FOR_LECTURER="**#";
	String OPEN_QUIZ_LIST_FOR_STUDENT="*$##";
	
	String LECTURER_DELETE_AN_ATTENDENCE="*#$";
	String LECTURER_ADD_NEW_ATTENDENCE="+*#";
	String STUDENT_SUBMIT_ATTENDENCE_CODE="$+#";
	
	String LECTURER_OPEN_AN_ANNOUNCEMENT="*+$";
	String LECTURER_ADD_NEW_ANNOUNCEMENT="$$#";
	String STUDENT_REVIEW_AN_ANNOUNCEMENT="#++";
	String STUDENT_OPEN_NEW_ANNOUNCEMENT="+>=";
	String STUDENT_CREATE_COMMENT="><+";
	
	String LECTURER_ADD_NEW_QUIZ="+#+";
	String LECTURER_RELEASE_QUIZ=">=>";
	String LECTURER_EDIT_QUIZ=">><";
	String LECTURER_DELETE_QUIZ="<<+";
	
	String OPEN_COMMENT_FOR_LECTURER="=+#";
	String OPEN_COMMENT_FOR_STUDENT=">++";
	
	String LECTURER_BACK_TO_USER_PROFILE="***";
	String STUDENT_BACK_TO_USER_PROFILE="<=>";

	public static String getRealMsg(String line) {
		String realMsg=line.substring(length, line.length()-length);
		return realMsg;
	}
}