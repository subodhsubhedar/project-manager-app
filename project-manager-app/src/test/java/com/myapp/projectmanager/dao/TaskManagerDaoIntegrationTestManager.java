package com.myapp.projectmanager.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.repository.ParentTaskManagerRepository;
import com.myapp.projectmanager.repository.TaskManagerRepository;

@RunWith(SpringRunner.class)
@DataJpaTest(showSql = true)
public class TaskManagerDaoIntegrationTestManager {

	@Autowired
	private TestEntityManager entityMngr;

	@Autowired
	private TaskManagerRepository taskManagerRepository;

	@Autowired
	private ParentTaskManagerRepository parentTaskManagerRepository;

	@Before
	public void setUp() {

	}

	@Test
	public void testFindAllTasks_shouldReturnCorrectCount() {
		assertTrue(Integer.valueOf(13).equals(taskManagerRepository.findAll().size()));
	}

	@Test
	public void testFindAllParentTasks_shouldReturnCorrectCount() {
		assertTrue(Integer.valueOf(3).equals(parentTaskManagerRepository.findAll().size()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectDesc() {

		assertTrue("Check Politically Exposed Person"
				.equals(taskManagerRepository.findByTask("Check Politically Exposed Person").get().getTask()));
	}

	@Test
	public void testFindParentTaskById_shouldReturnCorrectTitle() {
		assertTrue("Home Loan Processing".equals(parentTaskManagerRepository.findById(1L).get().getParentTaskDesc()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectPriority() {
		assertTrue(
				Integer.valueOf(29).equals(taskManagerRepository.findByTask("Close Loan account").get().getPriority()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectParentTask() {
		assertTrue("Home Loan Closure".equals(
				taskManagerRepository.findByTask("Close Loan account").get().getParentTask().getParentTaskDesc()));
	}

	@Test
	public void testFindTaskById_shouldReturnCorrectTask() {
		assertTrue("Home Loan Closure".equals(taskManagerRepository.findById(2L).get().getTask()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectStartDate() {
		assertTrue(LocalDate.now().equals(taskManagerRepository.findByTask("Close Loan account").get().getStartDate()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectEndDate() {
		assertTrue(LocalDate.of(2019, 12, 30)
				.equals(taskManagerRepository.findByTask("Close Loan account").get().getEndDate()));
	}

/*	@Test
	@DirtiesContext
	public void testCreateTaskNoParent_shouldCreateNewTask() {

		Task t14 = new Task(0L, "Miscellanous", LocalDate.now(), LocalDate.of(2019, 12, 30), 29, null, false);
		assertTrue("Miscellanous".equals((taskManagerRepository.save(t14)).getTask()));
	}

	@Test
	@DirtiesContext
	public void testCreateTaskWithParent_shouldCreateNewTask() {
		ParentTask p2 = new ParentTask(2L, "Home Loan Closure", null);

		Task t14 = new Task(0L, "Closure- Miscellanous", LocalDate.now(), LocalDate.of(2019, 12, 30), 29, p2, false);

		assertTrue("Closure- Miscellanous".equals((taskManagerRepository.save(t14)).getTask()));
	}

	@Test
	@DirtiesContext
	public void testCreateTaskWithParent_shouldCreateNewTaskCorrectParent() {
		ParentTask p2 = new ParentTask(2L, "Home Loan Closure", null);

		Task t14 = new Task(0L, "Closure- Miscellanous", LocalDate.now(), LocalDate.of(2019, 12, 30), 29, p2, false);

		assertTrue("Home Loan Closure".equals((taskManagerRepository.save(t14)).getParentTask().getParentTaskDesc()));
	}
*/
	@Test
	@DirtiesContext
	public void testDeleteTask_shouldDelete() {

		Task t = taskManagerRepository.findByTask("Settle payments").get();
		taskManagerRepository.delete(t);

		assertFalse(taskManagerRepository.findByTask("Settle payments").isPresent());
	}

	@Test
	@DirtiesContext
	public void testUpdateTask_shouldUpdateTask() {

		Task t = taskManagerRepository.findByTask("Check ID Proofs").get();

		t.setTask("Check ID Proofs - updated");

		t = taskManagerRepository.save(t);

		assertTrue("Check ID Proofs - updated".equals((t).getTask()));
	}

}