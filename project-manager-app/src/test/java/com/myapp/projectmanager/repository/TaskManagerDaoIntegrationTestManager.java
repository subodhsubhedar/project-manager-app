package com.myapp.projectmanager.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.entity.User;
import com.myapp.projectmanager.utils.TestUtils;

@RunWith(Parameterized.class)
@DataJpaTest(showSql = true)
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

	@Parameter(value = 0)
	public static List<Task> taskDs;

	@Parameter(value = 1)
	public static List<ParentTask> parentTaskDs;

	@Parameter(value = 2)
	public static Project prj;

	@Parameter(value = 3)
	public static List<User> usrDs;

	@Parameter(value = 4)
	public static Integer iteration;

	public static int testCaseCounter;

	public static int prevIteration;

	@Parameters
	public static Collection<Object[]> data() {

		Collection<Object[]> testParms = TestUtils.getTestData();
		testCaseCounter = 0;

		return testParms;
	}

	@Before
	public void setUp() throws Exception {
		if (prevIteration != 0) {
			resetTestData();
		}
		persistTestData();
	}

	@After
	public void reset() throws Exception {

		prevIteration = iteration;
		testCaseCounter++;
	}

	public void resetTestData() {
		// reset test objects

		List<Object[]> testDataCollection = (List<Object[]>) TaskManagerDaoIntegrationTestManager.data();
		Object[] objArray = testDataCollection.get(iteration - 1);

		taskDs = (List<Task>) objArray[0];
		parentTaskDs = (List<ParentTask>) objArray[1];
		prj = (Project) objArray[2];
		usrDs = (List<User>) objArray[3];
		iteration = (Integer) objArray[4];
	}

	public void persistTestData() {

		// persist parent task
		parentTaskDs.forEach(p -> {
			p = entityMngr.persist(p);
		});

		// persist project
		prj = entityMngr.persist(prj);

		// persist task
		taskDs.forEach(t -> {
			t = entityMngr.persist(t);
		});

		// persist users
		usrDs.forEach(u -> {
			u = entityMngr.persist(u);
		});

		entityMngr.flush();
	}

	@Test
	public void testFindAllTasks_shouldReturnCorrectCount() {
		assertTrue(Integer.valueOf(taskDs.size()).equals(taskManagerRepository.findAll().size()));
	}

	@Test
	public void testFindAllParentTasks_shouldReturnCorrectCount() {
		assertTrue(Integer.valueOf(parentTaskDs.size()).equals(parentTaskManagerRepository.findAll().size()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectDesc() {
		assertTrue(taskDs.get(4).getTask()
				.equals(taskManagerRepository.findByTask(taskDs.get(4).getTask()).get().getTask()));
	}

	@Test
	public void testFindParentTaskById_shouldReturnCorrectTitle() {
		assertTrue(parentTaskDs.get(0).getParentTaskDesc()
				.equals(parentTaskManagerRepository.findById(0L).get().getParentTaskDesc()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectPriority() {
		assertTrue(Integer.valueOf(taskDs.get(6).getPriority())
				.equals(taskManagerRepository.findByTask(taskDs.get(6).getTask()).get().getPriority()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectParentTask() {
		assertTrue(taskDs.get(7).getParentTask().getParentTaskDesc().equals(
				taskManagerRepository.findByTask(taskDs.get(7).getTask()).get().getParentTask().getParentTaskDesc()));
	}

	@Test
	public void testFindTaskById_shouldReturnCorrectTask() {
		assertTrue(taskDs.get(5).getTask()
				.equals(taskManagerRepository.findById(taskDs.get(5).getTaskId()).get().getTask()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectStartDate() {
		assertTrue(taskDs.get(7).getStartDate()
				.equals(taskManagerRepository.findByTask(taskDs.get(7).getTask()).get().getStartDate()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectEndDate() {
		assertTrue(taskDs.get(4).getEndDate()
				.equals(taskManagerRepository.findByTask(taskDs.get(4).getTask()).get().getEndDate()));
	}

	@Test
	public void testCreateTaskNoParent_shouldCreateNewTask() {

		Task t20 = new Task(0L, "Miscellanous", LocalDate.now(), LocalDate.now().plusDays(200), 29, null, prj, false);
		assertTrue("Miscellanous".equals((taskManagerRepository.save(t20)).getTask()));
	}

	@Test
	public void testCreateTaskWithParent_shouldCreateNewTask() {
		ParentTask p99 = new ParentTask(0L, "Misc", null);

		Task t99 = new Task(0L, "Miscellanous", LocalDate.now(), LocalDate.now().plusDays(200), 29, p99, prj, false);

		assertTrue("Miscellanous".equals((taskManagerRepository.save(t99)).getTask()));
	}

	@Test
	public void testCreateTaskWithParent_shouldCreateNewTaskCorrectParent() {
		ParentTask p99 = new ParentTask(0L, "Misc1", null);

		Task t99 = new Task(0L, "Miscellanous1", LocalDate.now(), LocalDate.now().plusDays(200), 29, p99, prj, false);

		assertTrue("Misc1".equals((taskManagerRepository.save(t99)).getParentTask().getParentTaskDesc()));
	}

	@Test
	public void testDeleteTask_shouldDelete() {

		Task t = taskManagerRepository.findByTask(taskDs.get(9).getTask()).get();
		taskManagerRepository.delete(t);

		assertFalse(taskManagerRepository.findByTask(taskDs.get(9).getTask()).isPresent());
	}

	@Test
	public void testUpdateTask_shouldUpdateTask() {

		Task t = taskManagerRepository.findByTask(taskDs.get(11).getTask()).get();

		t.setTask("updated task desc");

		t = taskManagerRepository.save(t);

		assertTrue("updated task desc".equals((t).getTask()));
	}

}