package com.myapp.projectmanager.dao;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestContextManager;

import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.repository.ParentTaskManagerRepository;
import com.myapp.projectmanager.repository.TaskManagerRepository;

@RunWith(Parameterized.class)
//@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true)
public class TaskManagerDaoIntegrationTestManager {

	private TestContextManager testContextManager;

	@Autowired
	private TestEntityManager entityMngr;

	@Autowired
	private TaskManagerRepository taskManagerRepository;

	@Autowired
	private ParentTaskManagerRepository parentTaskManagerRepository;

	@Parameter(value = 0)
	public static List<Task> taskDs1;

	@Parameter(value = 1)
	public static List<ParentTask> parentTaskDs1;

	@Parameter(value = 2)
	public static Project prj;

	@Parameters
	public static Collection<Object[]> data() {
		Collection<Object[]> params = new ArrayList<>();

		Project prj1 = new Project(0L, "Project - Mobile Subscription management system", LocalDate.now(),
				LocalDate.now().plusYears(1), 25);

		Project prj2 = new Project(1L, "Project - Home loan Processing System", LocalDate.now(),
				LocalDate.now().plusYears(2), 30);

		parentTaskDs1 = new ArrayList<ParentTask>();
		ParentTask p1 = new ParentTask(0L, "Use Case - New Connection", null);
		parentTaskDs1.add(p1);
		ParentTask p2 = new ParentTask(2L, "Use Case - Subscriber KYC", null);
		parentTaskDs1.add(p2);
		ParentTask p3 = new ParentTask(3L, "Use case -  Provide options for Postpaid or Prepaid", null);
		parentTaskDs1.add(p3);
		ParentTask p4 = new ParentTask(8L, "Use Case - Check for payments", null);
		parentTaskDs1.add(p4);

		taskDs1 = new ArrayList<Task>();

		// ############ For project Mobile Subscription management system ############

		// Tasks with parent as Use Case - New Connection
		Task t01 = new Task(1L, "Use case -	Receive new connection application", LocalDate.now(),
				LocalDate.now().plusDays(15), 20, p1, prj1, false);
		taskDs1.add(t01);

		Task t02 = new Task(2L, "Use case -	Subscriber KYC", LocalDate.now(), LocalDate.now().plusDays(45), 21, p1,
				prj1, false);
		taskDs1.add(t02);

		Task t03 = new Task(3L, "Use case -  Provide options for Postpaid or Prepaid", LocalDate.now(),
				LocalDate.now().plusDays(20), 20, p1, prj1, false);
		taskDs1.add(t03);

		Task t04 = new Task(4L, "Use case -  Allocate mobile number", LocalDate.now(), LocalDate.now().plusDays(12), 15,
				p1, prj1, false);
		taskDs1.add(t04);

		Task t05 = new Task(5L, "Use case -  Activate sim card", LocalDate.now(), LocalDate.now().plusDays(8), 20, p1,
				prj1, false);
		taskDs1.add(t05);

		Task t06 = new Task(6L, "Use case -  Activate billing cycle", LocalDate.now(), LocalDate.now().plusDays(12), 22,
				p1, prj1, false);
		taskDs1.add(t06);

		Task t07 = new Task(7L, "Use case -  Generate monthly bill", LocalDate.now(), LocalDate.now().plusDays(25), 25,
				p1, prj1, false);
		taskDs1.add(t07);

		Task t08 = new Task(8L, "Use case -  Check for payments", LocalDate.now(), LocalDate.now().plusDays(18), 25, p1,
				prj1, false);
		taskDs1.add(t08);

		// Tasks with parent as Use Case - Check for payments
		Task t09 = new Task(9L, "Use case -  Send reminders", LocalDate.now(), LocalDate.now().plusDays(18), 25, p4,
				prj1, false);
		taskDs1.add(t09);

		// Tasks with parent as Use case - Provide options for Postpaid or Prepaid
		Task t10 = new Task(10L, "Use case -  Provide various Postpaid data plans", LocalDate.now(),
				LocalDate.now().plusDays(150), 25, p3, prj1, false);
		taskDs1.add(t10);

		Task t11 = new Task(11L, "Use case -  Provide various Postpaid call plans", LocalDate.now(),
				LocalDate.now().plusDays(120), 25, p3, prj1, false);
		taskDs1.add(t11);

		Task t12 = new Task(12L, "Use case -  Provide various Prepaid data plans", LocalDate.now(),
				LocalDate.now().plusDays(170), 25, p3, prj1, false);
		taskDs1.add(t12);

		Task t13 = new Task(13L, "Use case -  Provide various Prepaid call plans", LocalDate.now(),
				LocalDate.now().plusDays(130), 25, p3, prj1, false);
		taskDs1.add(t13);

		// Tasks with No parent
		Task t14 = new Task(14L, "Use Case - Suspend Connection", LocalDate.now(), LocalDate.now().plusDays(20), 30,
				null, prj1, false);
		taskDs1.add(t14);

		// Tasks with parent as Use Case - Subscriber KYC
		Task t15 = new Task(15L, "Use case - Check for any criminal records", LocalDate.now(),
				LocalDate.now().plusDays(60), 25, p2, prj1, false);
		taskDs1.add(t15);

		Task t16 = new Task(16L, "Use case - Check for Nationality, dual citizenship etc.", LocalDate.now(),
				LocalDate.now().plusDays(60), 25, p2, prj1, false);
		taskDs1.add(t16);

		Task t17 = new Task(17L, "Use case - Check for ID Proofs", LocalDate.now(), LocalDate.now().plusDays(30), 25,
				p2, prj1, false);
		taskDs1.add(t17);

		Task t18 = new Task(18L, "Use case - Check for Address Proofs", LocalDate.now(), LocalDate.now().plusDays(40),
				25, p2, prj1, false);
		taskDs1.add(t18);

		Task t19 = new Task(18L, "Use case - Check for Aadhar card", LocalDate.now(), LocalDate.now().plusDays(20), 25,
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

		params.add(new Object[] { taskDs1, parentTaskDs1, prj1 });
		// params.add(new Object[] { taskDs2, parentTaskDs2, prj2 });

		return params;
	}

	@Before
	public void setUp() throws Exception {

		this.testContextManager = new TestContextManager(getClass());
		this.testContextManager.prepareTestInstance(this);

		parentTaskDs1.forEach(prntTsk -> entityMngr.persist(prntTsk));
		entityMngr.flush();

		taskDs1.forEach(tsk -> entityMngr.persist(tsk));

		entityMngr.persist(prj);
		entityMngr.flush();
	}

	@Test
	public void testFindAllTasks_shouldReturnCorrectCount() {

		System.out.println("#################################");

		System.out.println(this.taskDs1);
		System.out.println("#################################");

		System.out.println(this.parentTaskDs1);
		System.out.println("#################################");

		assertTrue(Integer.valueOf(taskDs1.size()).equals(taskManagerRepository.findAll().size()));
	}

	/*
	 * @Test public void testFindAllParentTasks_shouldReturnCorrectCount() {
	 * assertTrue(Integer.valueOf(3).equals(parentTaskManagerRepository.findAll().
	 * size())); }
	 * 
	 * @Test public void testFindTaskByDesc_shouldReturnCorrectDesc() {
	 * 
	 * assertTrue("Check Politically Exposed Person"
	 * .equals(taskManagerRepository.findByTask("Check Politically Exposed Person").
	 * get().getTask())); }
	 * 
	 * @Test public void testFindParentTaskById_shouldReturnCorrectTitle() {
	 * assertTrue("Home Loan Processing".equals(parentTaskManagerRepository.findById
	 * (1L).get().getParentTaskDesc())); }
	 * 
	 * @Test public void testFindTaskByDesc_shouldReturnCorrectPriority() {
	 * assertTrue( Integer.valueOf(29).equals(taskManagerRepository.
	 * findByTask("Close Loan account").get().getPriority())); }
	 * 
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
	 * /*
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