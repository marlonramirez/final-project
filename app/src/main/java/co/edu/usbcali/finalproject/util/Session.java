package co.edu.usbcali.finalproject.util;


import co.edu.usbcali.finalproject.model.Student;
import co.edu.usbcali.finalproject.model.Teacher;

/**
 * Created by Marlon.Ramirez on 27/01/2018.
 */

public abstract class Session {
    public static Student userStudent;
    public static Teacher userTeacher;
    static {
        userStudent = new Student();
        userTeacher = new Teacher();
    }
}
