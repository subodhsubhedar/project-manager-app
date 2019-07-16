package com.myapp.projectmanager.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.entity.User;

/**
 * 
 * @author Admin
 *
 */
public class TestUtils {

	public static Collection<Object[]> getTestData() {
		Collection<Object[]> params = new ArrayList<>();

		// ############ For project Mobile Subscription management system ############
		prepareTestDataItr1(params);
		
		
		// ############ For project Home Loan Application management system ############
		prepareTestDataItr2(params);

		return params;
	}

	/**
	 * 
	 * // ############ Test data For project Mobile Subscription management system ############
	 * 
	 * @param params
	 */
	private static void prepareTestDataItr1(Collection<Object[]> params) {

		// build parent task test object
		List<ParentTask> parentTaskDs1 = new ArrayList<ParentTask>();
		ParentTask p1 = new ParentTask(0L, "Use Case - New Connection", null);
		parentTaskDs1.add(p1);
		ParentTask p2 = new ParentTask(2L, "Use Case - Subscriber KYC", null);
		parentTaskDs1.add(p2);
		ParentTask p3 = new ParentTask(3L, "Use case -  Provide options for Postpaid or Prepaid", null);
		parentTaskDs1.add(p3);
		ParentTask p4 = new ParentTask(8L, "Use Case - Check for payments", null);
		parentTaskDs1.add(p4);

		// build project test object
		Project prj1 = new Project(0L, "Project - Mobile Subscription management system", LocalDate.now(),
				LocalDate.now().plusYears(1), 25);

		// build task test object

		List<Task> taskDs1 = new ArrayList<Task>();

		// Tasks with parent as Use Case - New Connection
		Task t01 = new Task(0L, "Use case -	Receive new connection application", LocalDate.now(),
				LocalDate.now().plusDays(15), 20, p1, prj1, false);
		taskDs1.add(t01);

		Task t02 = new Task(0L, "Use case -	Subscriber KYC", LocalDate.now(), LocalDate.now().plusDays(45), 21, p1,
				prj1, false);
		taskDs1.add(t02);

		Task t03 = new Task(0L, "Use case -  Provide options for Postpaid or Prepaid", LocalDate.now(),
				LocalDate.now().plusDays(20), 20, p1, prj1, false);
		taskDs1.add(t03);

		Task t04 = new Task(0L, "Use case -  Allocate mobile number", LocalDate.now(), LocalDate.now().plusDays(12), 15,
				p1, prj1, false);
		taskDs1.add(t04);

		Task t05 = new Task(0L, "Use case -  Activate sim card", LocalDate.now(), LocalDate.now().plusDays(8), 20, p1,
				prj1, false);
		taskDs1.add(t05);

		Task t06 = new Task(0L, "Use case -  Activate billing cycle", LocalDate.now(), LocalDate.now().plusDays(12), 22,
				p1, prj1, false);
		taskDs1.add(t06);

		Task t07 = new Task(0L, "Use case -  Generate monthly bill", LocalDate.now(), LocalDate.now().plusDays(25), 25,
				p1, prj1, false);
		taskDs1.add(t07);

		Task t08 = new Task(0L, "Use case -  Check for payments", LocalDate.now(), LocalDate.now().plusDays(18), 25, p1,
				prj1, false);
		taskDs1.add(t08);

		// Tasks with parent as Use Case - Check for payments
		Task t09 = new Task(0L, "Use case -  Send reminders", LocalDate.now(), LocalDate.now().plusDays(18), 25, p4,
				prj1, false);
		taskDs1.add(t09);

		// Tasks with parent as Use case - Provide options for Postpaid or Prepaid
		Task t10 = new Task(0L, "Use case -  Provide various Postpaid data plans", LocalDate.now(),
				LocalDate.now().plusDays(150), 25, p3, prj1, false);
		taskDs1.add(t10);

		Task t11 = new Task(0L, "Use case -  Provide various Postpaid call plans", LocalDate.now(),
				LocalDate.now().plusDays(120), 25, p3, prj1, false);
		taskDs1.add(t11);

		Task t12 = new Task(0L, "Use case -  Provide various Prepaid data plans", LocalDate.now(),
				LocalDate.now().plusDays(170), 25, p3, prj1, false);
		taskDs1.add(t12);

		Task t13 = new Task(0L, "Use case -  Provide various Prepaid call plans", LocalDate.now(),
				LocalDate.now().plusDays(130), 25, p3, prj1, false);
		taskDs1.add(t13);

		// Tasks with No parent
		Task t14 = new Task(0L, "Use Case - Suspend Connection", LocalDate.now(), LocalDate.now().plusDays(20), 30,
				null, prj1, false);
		taskDs1.add(t14);

		// Tasks with parent as Use Case - Subscriber KYC
		Task t15 = new Task(0L, "Use case - Check for any criminal records", LocalDate.now(),
				LocalDate.now().plusDays(60), 25, p2, prj1, false);
		taskDs1.add(t15);

		Task t16 = new Task(0L, "Use case - Check for Nationality, dual citizenship etc.", LocalDate.now(),
				LocalDate.now().plusDays(60), 25, p2, prj1, false);
		taskDs1.add(t16);

		Task t17 = new Task(0L, "Use case - Check for ID Proofs", LocalDate.now(), LocalDate.now().plusDays(30), 25, p2,
				prj1, false);
		taskDs1.add(t17);

		Task t18 = new Task(0L, "Use case - Check for Address Proofs", LocalDate.now(), LocalDate.now().plusDays(40),
				25, p2, prj1, false);
		taskDs1.add(t18);

		Task t19 = new Task(0L, "Use case - Check for Aadhar card", LocalDate.now(), LocalDate.now().plusDays(20), 25,
				p2, prj1, false);
		taskDs1.add(t19);

		p1.addSubTasks(t01);
		p1.addSubTasks(t02);
		p1.addSubTasks(t03);
		p1.addSubTasks(t04);
		p1.addSubTasks(t05);
		p1.addSubTasks(t06);
		p1.addSubTasks(t07);
		p1.addSubTasks(t08);

		p2.addSubTasks(t15);
		p2.addSubTasks(t16);
		p2.addSubTasks(t17);
		p2.addSubTasks(t18);
		p2.addSubTasks(t19);

		p3.addSubTasks(t10);
		p3.addSubTasks(t11);
		p3.addSubTasks(t12);
		p3.addSubTasks(t13);

		p4.addSubTasks(t09);

		prj1.setTasks(new HashSet<>(taskDs1));

		User usr10 = new User(0L, 0L, "Subodh", "Subhedar", prj1, null);
		User usr11 = new User(0L, 0L, "Vinay", "Roy", null, t01);
		User usr12 = new User(0L, 0L, "David", "Ray", null, t02);
		User usr13 = new User(0L, 0L, "Ryan", "Jacobs", null, t03);
		User usr14 = new User(0L, 0L, "Nick", "Wells", null, t04);

		List<User> usrDs1 = new ArrayList<User>();
		usrDs1.add(usr10);
		usrDs1.add(usr11);
		usrDs1.add(usr12);
		usrDs1.add(usr13);
		usrDs1.add(usr14);

		prj1.setUser(usr10);
		t01.setUser(usr11);
		t02.setUser(usr12);
		t03.setUser(usr13);
		t04.setUser(usr14);

		params.add(new Object[] { taskDs1, parentTaskDs1, prj1, usrDs1, 1 });

	}

	
	/**
	 * 
	 * // ############ Test data For project Mobile Subscription management system ############
	 * 
	 * @param params
	 */
	private static void prepareTestDataItr2(Collection<Object[]> params) {

		// build parent task test object
		List<ParentTask> parentTaskDs1 = new ArrayList<ParentTask>();
		ParentTask p1 = new ParentTask(0L, "Use Case - Home Loan Processing", null);
		parentTaskDs1.add(p1);
		ParentTask p2 = new ParentTask(2L, "Use Case - KYC", null);
		parentTaskDs1.add(p2);
		ParentTask p3 = new ParentTask(3L, "Use Case - Home Loan Closure", null);
		parentTaskDs1.add(p3);

		// build project test object
		Project prj1 = new Project(0L, "Project - Home loan Processing System", LocalDate.now(),
				LocalDate.now().plusYears(2), 30);

		// build task test object

		List<Task> taskDs1 = new ArrayList<Task>();

		// Tasks with parent as Use Case - Home Loan Processing
		Task t01 = new Task(0L, "Use case - Receive Loan Application", LocalDate.now(),
				LocalDate.now().plusDays(60), 26, p1, prj1, false);
		taskDs1.add(t01);

		Task t02 = new Task(0L, "Use case - Procure Customer Documents", LocalDate.now(), LocalDate.now().plusDays(45), 21, p1,
				prj1, false);
		taskDs1.add(t02);

		Task t03 = new Task(0L, "Use case - Procure Property Details and Documents", LocalDate.now(),
				LocalDate.now().plusDays(20), 20, p1, prj1, false);
		taskDs1.add(t03);

		Task t04 = new Task(0L, "Use case - KYC", LocalDate.now(), LocalDate.now().plusDays(12), 15,
				p1, prj1, false);
		taskDs1.add(t04);

		Task t05 = new Task(0L, "Use case - Check Loan eligibility", LocalDate.now(), LocalDate.now().plusDays(8), 20, p1,
				prj1, false);
		taskDs1.add(t05);

		Task t06 = new Task(0L, "Use case - Issue loan pre approval letter", LocalDate.now(), LocalDate.now().plusDays(12), 22,
				p1, prj1, false);
		taskDs1.add(t06);

		Task t07 = new Task(0L, "Use case - Approve/Reject Loan application", LocalDate.now(), LocalDate.now().plusDays(25), 25,
				p1, prj1, false);
		taskDs1.add(t07);

		Task t08 = new Task(0L, "Use case - Generate loan account number", LocalDate.now(), LocalDate.now().plusDays(18), 25, p1,
				prj1, false);
		taskDs1.add(t08);

		// Tasks with parent as Use Case - KYC
		Task t09 = new Task(0L, "Use case - Check criminal records", LocalDate.now(), LocalDate.now().plusDays(18), 25, p2,
				prj1, false);
		taskDs1.add(t09);

		// Tasks with parent as Use Case - Home Loan Closure
		Task t10 = new Task(0L, "Use case - Receive Loan Closure Application", LocalDate.now(),
				LocalDate.now().plusDays(150), 25, p3, prj1, false);
		taskDs1.add(t10);

		Task t11 = new Task(0L, "Use case - Check outstanding balance", LocalDate.now(),
				LocalDate.now().plusDays(120), 25, p3, prj1, false);
		taskDs1.add(t11);

		Task t12 = new Task(0L, "Use case - Settle payments", LocalDate.now(),
				LocalDate.now().plusDays(170), 25, p3, prj1, false);
		taskDs1.add(t12);

		Task t13 = new Task(0L, "Use case - Close Loan account", LocalDate.now(),
				LocalDate.now().plusDays(130), 25, p3, prj1, false);
		taskDs1.add(t13);

		p1.addSubTasks(t01);
		p1.addSubTasks(t02);
		p1.addSubTasks(t03);
		p1.addSubTasks(t04);
		p1.addSubTasks(t05);
		p1.addSubTasks(t06);
		p1.addSubTasks(t07);
		p1.addSubTasks(t08);

		p2.addSubTasks(t09);

		p3.addSubTasks(t10);
		p3.addSubTasks(t11);
		p3.addSubTasks(t12);
		p3.addSubTasks(t13);


		prj1.setTasks(new HashSet<>(taskDs1));

		User usr10 = new User(0L, 0L, "Kim", "Carrie", prj1, null);
		User usr11 = new User(0L, 0L, "Vinay", "Jae", null, t04);
		User usr12 = new User(0L, 0L, "David", "Malcom", null, t05);
		User usr13 = new User(0L, 0L, "Ryan", "Wells", null, t07);
		User usr14 = new User(0L, 0L, "Nick", "Flemington", null, t08);

		List<User> usrDs1 = new ArrayList<User>();
		usrDs1.add(usr10);
		usrDs1.add(usr11);
		usrDs1.add(usr12);
		usrDs1.add(usr13);
		usrDs1.add(usr14);

		prj1.setUser(usr10);
		t04.setUser(usr11);
		t05.setUser(usr12);
		t07.setUser(usr13);
		t08.setUser(usr14);

		params.add(new Object[] { taskDs1, parentTaskDs1, prj1, usrDs1, 2 });

	}
}
