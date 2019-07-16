package com.myapp.projectmanager.dao;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.myapp.ProjectManagerAppApplication;
import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.entity.User;
import com.myapp.projectmanager.repository.ParentTaskManagerRepository;
import com.myapp.projectmanager.repository.TaskManagerRepository;

@RunWith(Parameterized.class)
//@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true)
@ContextConfiguration(classes = ProjectManagerAppApplication.class)
public class TaskManagerDaoIntegrationTestManager {

	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	private TestEntityManager entityMngr;

	@Autowired
	private TaskManagerRepository taskManagerRepository;

	@Autowired
	private ParentTaskManagerRepository parentTaskManagerRepository;

	public List<Task> taskDs1;

	public List<ParentTask> parentTaskDs1;

	public Project prj;

	public List<User> usrList;
	
	@Parameter(value = 0)
	public static List<Task> taskDs1new;

	@Parameter(value = 1)
	public static List<ParentTask> parentTaskDs1new;

	@Parameter(value = 2)
	public static Project prjnew;

	@Parameter(value = 3)
	public static List<User> usrListnew;

	@Parameters
	public static Collection<Object[]> data() {
		Collection<Object[]> params = new ArrayList<>();

	
		params.add(new Object[] { null, null, null, null});
		// params.add(new Object[] { taskDs2, parentTaskDs2, prj2 });

		return params;
	}

	@Before
	public void setUp() throws Exception {

		
		parentTaskDs1 = new ArrayList<ParentTask>();
		ParentTask p1 = new ParentTask(0L, "Use Case - New Connection", null);
		parentTaskDs1.add(p1);
		ParentTask p2 = new ParentTask(2L, "Use Case - Subscriber KYC", null);
		parentTaskDs1.add(p2);
		ParentTask p3 = new ParentTask(3L, "Use case -  Provide options for Postpaid or Prepaid", null);
		parentTaskDs1.add(p3);
		ParentTask p4 = new ParentTask(8L, "Use Case - Check for payments", null);
		parentTaskDs1.add(p4);

		p1 = entityMngr.persist(p1);
		p2 = entityMngr.persist(p2);
		p3 = entityMngr.persist(p3);
		p4 = entityMngr.persist(p4);

		//entityMngr.flush();
		


		Project prj1 = new Project(0L, "Project - Mobile Subscription management system", LocalDate.now(),
				LocalDate.now().plusYears(1), 25);

		Project prj2 = new Project(0L, "Project - Home loan Processing System", LocalDate.now(),
				LocalDate.now().plusYears(2), 30);

		
		prj1 = entityMngr.persist(prj1);
		prj2 = entityMngr.persist(prj2);
		
		prj = prj1;
		
		taskDs1 = new ArrayList<Task>();

		// ############ For project Mobile Subscription management system ############

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
		
		t01 = entityMngr.persist(t01);
		t02 = entityMngr.persist(t02);
		t03 = entityMngr.persist(t03);
		t04 = entityMngr.persist(t04);
		t05 = entityMngr.persist(t05);
		t06 = entityMngr.persist(t06);
		t07 = entityMngr.persist(t07);
		t08 = entityMngr.persist(t08);
		t09 = entityMngr.persist(t09);
		t10 = entityMngr.persist(t10);
		t11 = entityMngr.persist(t11);
		t12 = entityMngr.persist(t12);
		t13 = entityMngr.persist(t13);
		t14 = entityMngr.persist(t14);
		t15 = entityMngr.persist(t15);
		t16 = entityMngr.persist(t16);
		t17 = entityMngr.persist(t17);
		t18 = entityMngr.persist(t18);
		t19 = entityMngr.persist(t19);
		
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

		
		usr10 = entityMngr.persist(usr10);
		usr11 = entityMngr.persist(usr11);
		usr12 = entityMngr.persist(usr12);
		usr13 = entityMngr.persist(usr13);
		usr14 = entityMngr.persist(usr14);
		
		usrList = new ArrayList<User>();
		usrList.add(usr10);
		usrList.add(usr11);
		usrList.add(usr12);
		usrList.add(usr13);
		usrList.add(usr14);

		prj1.setUser(usr10);
		t01.setUser(usr11);
		t02.setUser(usr12);
		t03.setUser(usr13);
		t04.setUser(usr14);
		
		entityMngr.flush();
		
		
		
	}

	@Test
	public void testFindAllTasks_shouldReturnCorrectCount() {
		assertTrue(Integer.valueOf(taskDs1.size()).equals(taskManagerRepository.findAll().size()));
	}

	@Test
	public void testFindAllParentTasks_shouldReturnCorrectCount() {
		assertTrue(Integer.valueOf(parentTaskDs1.size()).equals(parentTaskManagerRepository.findAll().size()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectDesc() {
		assertTrue(taskDs1.get(4).getTask()
				.equals(taskManagerRepository.findByTask(taskDs1.get(4).getTask()).get().getTask()));
	}

	@Test
	public void testFindParentTaskById_shouldReturnCorrectTitle() {
		assertTrue(parentTaskDs1.get(0).getParentTaskDesc()
				.equals(parentTaskManagerRepository.findById(0L).get().getParentTaskDesc()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectPriority() {
		assertTrue(Integer.valueOf(taskDs1.get(6).getPriority())
				.equals(taskManagerRepository.findByTask(taskDs1.get(6).getTask()).get().getPriority()));
	}

	/*
	 * @Test public void testFindTaskByDesc_shouldReturnCorrectParentTask() {
	 * assertTrue("Home Loan Closure".equals(
	 * taskManagerRepository.findByTask("Close Loan account").get().getParentTask().
	 * getParentTaskDesc())); }
	 * 
	 * @Test public void testFindTaskById_shouldReturnCorrectTask() {
	 * assertTrue("Home Loan Closure".equals(taskManagerRepository.findById(2L).get(
	 * ).getTask())); }
	 * 
	 * @Test public void testFindTaskByDesc_shouldReturnCorrectStartDate() {
	 * assertTrue(LocalDate.now().equals(taskManagerRepository.
	 * findByTask("Close Loan account").get().getStartDate())); }
	 * 
	 * @Test public void testFindTaskByDesc_shouldReturnCorrectEndDate() {
	 * assertTrue(LocalDate.of(2019, 12, 30)
	 * .equals(taskManagerRepository.findByTask("Close Loan account").get().
	 * getEndDate())); }
	 * 
	 * @Test
	 * 
	 * @DirtiesContext public void testCreateTaskNoParent_shouldCreateNewTask() {
	 * 
	 * Task t14 = new Task(0L, "Miscellanous", LocalDate.now(), LocalDate.of(2019,
	 * 12, 30), 29, null, false);
	 * assertTrue("Miscellanous".equals((taskManagerRepository.save(t14)).getTask())
	 * ); }
	 * 
	 * @Test
	 * 
	 * @DirtiesContext public void testCreateTaskWithParent_shouldCreateNewTask() {
	 * ParentTask p2 = new ParentTask(2L, "Home Loan Closure", null);
	 * 
	 * Task t14 = new Task(0L, "Closure- Miscellanous", LocalDate.now(),
	 * LocalDate.of(2019, 12, 30), 29, p2, false);
	 * 
	 * assertTrue("Closure- Miscellanous".equals((taskManagerRepository.save(t14)).
	 * getTask())); }
	 * 
	 * @Test
	 * 
	 * @DirtiesContext public void
	 * testCreateTaskWithParent_shouldCreateNewTaskCorrectParent() { ParentTask p2 =
	 * new ParentTask(2L, "Home Loan Closure", null);
	 * 
	 * Task t14 = new Task(0L, "Closure- Miscellanous", LocalDate.now(),
	 * LocalDate.of(2019, 12, 30), 29, p2, false);
	 * 
	 * assertTrue("Home Loan Closure".equals((taskManagerRepository.save(t14)).
	 * getParentTask().getParentTaskDesc())); }
	 * 
	 * @Test
	 * 
	 * @DirtiesContext public void testDeleteTask_shouldDelete() {
	 * 
	 * Task t = taskManagerRepository.findByTask("Settle payments").get();
	 * taskManagerRepository.delete(t);
	 * 
	 * assertFalse(taskManagerRepository.findByTask("Settle payments").isPresent());
	 * }
	 * 
	 * @Test
	 * 
	 * @DirtiesContext public void testUpdateTask_shouldUpdateTask() {
	 * 
	 * Task t = taskManagerRepository.findByTask("Check ID Proofs").get();
	 * 
	 * t.setTask("Check ID Proofs - updated");
	 * 
	 * t = taskManagerRepository.save(t);
	 * 
	 * assertTrue("Check ID Proofs - updated".equals((t).getTask())); }
	 */

}