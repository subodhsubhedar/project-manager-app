package com.myapp.projectmanager.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.entity.User;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.repository.ParentTaskManagerRepository;
import com.myapp.projectmanager.repository.TaskManagerRepository;
import com.myapp.projectmanager.utils.TestUtils;

@RunWith(Parameterized.class)
@SpringBootTest
public class TaskManagerServiceIntegrationTestManager {

	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@MockBean
	private TaskManagerRepository taskManagerRepository;

	@MockBean
	private ParentTaskManagerRepository parentTaskManagerRepository;

	@Autowired
	private TaskManagerService service;

	@MockBean
	private UserService userService;

	@MockBean
	private ProjectManagerService pmService;

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

	@Parameters
	public static Collection<Object[]> data() {
		return TestUtils.getTestData();
	}

	@Test
	public void runSmokeTest() {

		assertNotNull(taskManagerRepository);

		assertNotNull(parentTaskManagerRepository);

		assertNotNull(service);
	}

	@Test
	public void testFindAllParentTasks_shouldReturnCorrectCount() throws ProjectManagerServiceException {
		when(parentTaskManagerRepository.findAll()).thenReturn(parentTaskDs);

		assertTrue(Integer.valueOf(parentTaskDs.size()).equals(service.findAllParenTasks().size()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindAllParentTasks_shouldThrowEx() throws ProjectManagerServiceException {
		when(parentTaskManagerRepository.findAll())
				.thenThrow(new RuntimeException("testFindAllParentTasks_shouldThrowEx"));

		service.findAllParenTasks().size();
	}

	@Test
	public void testFindAllTasks_shouldReturnCorrectCount() throws ProjectManagerServiceException {

		when(taskManagerRepository.findAll()).thenReturn(taskDs);

		assertTrue(Integer.valueOf(taskDs.size()).equals(service.findAllTasks().size()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindAllTasks_shouldThrowEx() throws ProjectManagerServiceException {
		when(taskManagerRepository.findAll()).thenThrow(new RuntimeException("testFindAllTasks_shouldThrowEx"));

		service.findAllTasks().size();
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectDesc() throws ProjectManagerServiceException {

		when(taskManagerRepository.findByTask(taskDs.get(8).getTask())).thenReturn(Optional.of(taskDs.get(8)));

		assertTrue(taskDs.get(8).getTask().equals((service.getTask(taskDs.get(8).getTask())).getTask()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindTaskByDesc_shouldThrowEx() throws ProjectManagerServiceException {
		when(taskManagerRepository.findByTask(taskDs.get(8).getTask()))
				.thenThrow(new RuntimeException("testFindTaskByDesc_shouldThrowEx"));

		service.getTask(taskDs.get(8).getTask());
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindNonExistingTaskByDesc_shouldThrowEx() throws ProjectManagerServiceException {
		when(taskManagerRepository.findByTask("abc")).thenReturn(Optional.empty());

		service.getTask("abc");
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectPriority() throws ProjectManagerServiceException {

		when(taskManagerRepository.findByTask(taskDs.get(12).getTask())).thenReturn(Optional.of(taskDs.get(12)));

		assertTrue(Integer.valueOf(taskDs.get(12).getPriority())
				.equals((service.getTask(taskDs.get(12).getTask())).getPriority()));
	}

	@Test
	public void testFindTaskById_shouldReturnTask() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(taskDs.get(11).getTaskId())).thenReturn(Optional.of(taskDs.get(11)));
		assertNotNull(service.getTaskById(taskDs.get(11).getTaskId()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindTaskByNonExistingId_shouldThrowEx() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(999999L)).thenReturn(Optional.empty());
		service.getTaskById(999999L);
	}

	@Test
	public void testFindParentTaskById_shouldReturnCorrectDesc() throws ProjectManagerServiceException {

		when(parentTaskManagerRepository.findById(parentTaskDs.get(2).getParentId()))
				.thenReturn(Optional.of(parentTaskDs.get(2)));

		assertTrue(parentTaskDs.get(2).getParentTaskDesc()
				.equals((service.getParentTaskById(parentTaskDs.get(2).getParentId()).getParentTaskDesc())));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindParentTaskById_shouldThrowEx() throws ProjectManagerServiceException {

		when(parentTaskManagerRepository.findById(6L))
				.thenThrow(new RuntimeException("testFindParentTaskById_shouldThrowEx"));

		service.getParentTaskById(6L).getParentTaskDesc();

	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindParentTaskById_shouldRetNullAndPass() throws ProjectManagerServiceException {

		when(parentTaskManagerRepository.findById(999999L)).thenReturn(null);
		service.getParentTaskById(999999L);
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectCompletionStatus() throws ProjectManagerServiceException {

		when(taskManagerRepository.findByTask(taskDs.get(7).getTask())).thenReturn(Optional.of(taskDs.get(7)));

		assertTrue(Boolean.valueOf(taskDs.get(7).getTaskComplete())
				.equals((service.getTask(taskDs.get(7).getTask())).getTaskComplete()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectStartDate() throws ProjectManagerServiceException {

		when(taskManagerRepository.findByTask(taskDs.get(9).getTask())).thenReturn(Optional.of(taskDs.get(9)));

		assertTrue(taskDs.get(9).getStartDate().equals((service.getTask(taskDs.get(9).getTask())).getStartDate()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindTaskByDesc_shouldThrowException() throws ProjectManagerServiceException {

		when(taskManagerRepository.findByTask(taskDs.get(7).getTask())).thenReturn(Optional.of(taskDs.get(7)));

		service.getTask(taskDs.get(6).getTask()).getEndDate();
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectEndDate() throws ProjectManagerServiceException {

		when(taskManagerRepository.findByTask(taskDs.get(6).getTask())).thenReturn(Optional.of(taskDs.get(6)));

		assertTrue(taskDs.get(6).getEndDate().equals((service.getTask(taskDs.get(6).getTask())).getEndDate()));
	}

	@Test
	public void testCreateTask_shouldCreateNewTask() throws ProjectManagerServiceException {

		ParentTask p99 = new ParentTask(0L, "Misc1", null);
		Task t99 = new Task(0L, "Miscellanous1", LocalDate.now(), LocalDate.now().plusDays(200), 29, p99, prj, false);

		when(pmService.getProjectById(prj.getProjectId())).thenReturn(prj);
		
		when(taskManagerRepository.save(t99)).thenReturn(t99);
		assertTrue("Miscellanous1".equals(service.createTask(t99).getTask()));
	}
 
	@Test(expected = ProjectManagerServiceException.class)
	public void testCreateTask_shouldThrowException() throws ProjectManagerServiceException {

		ParentTask p99 = new ParentTask(0L, "Misc1", null);
		Task t99 = new Task(0L, "Miscellanous1", LocalDate.now(), LocalDate.now().plusDays(200), 29, p99, prj, false);

		when(taskManagerRepository.save(t99)).thenThrow(new RuntimeException("testCreateTask_shouldThrowException"));
		service.createTask(t99).getTask();
	}

	@Test
	public void testUpdateTask_shouldRetUpdatedTask() throws ProjectManagerServiceException {

		when(userService.getUserById(0L)).thenReturn(usrDs.get(4));
		when(taskManagerRepository.findById(taskDs.get(4).getTaskId())).thenReturn(Optional.of(taskDs.get(4)));
		when(pmService.getProjectById(prj.getProjectId())).thenReturn(prj);

		Task tUpdated = taskDs.get(4);
		tUpdated.setTask("Updated");
		tUpdated.setTaskComplete(true);

		when(taskManagerRepository.save(taskDs.get(4))).thenReturn((tUpdated));

		assertTrue("Updated".equals(service.updateTask(taskDs.get(4)).getTask()));
		assertTrue((service.updateTask(taskDs.get(4)).getTaskComplete()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testUpdateTask_shouldThrowEx() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(taskDs.get(9).getTaskId())).thenReturn(Optional.of(taskDs.get(9)));
		when(taskManagerRepository.save(taskDs.get(9))).thenThrow(new RuntimeException("testUpdateTask_shouldThrowEx"));

		service.updateTask(taskDs.get(9));
	}

	@Test
	public void testCreateTaskWithoutParentTask_shouldPass() throws ProjectManagerServiceException {

		Task t99 = new Task(0L, "Miscellanous1", LocalDate.now(), LocalDate.now().plusDays(200), 29, null, prj, false);
		when(pmService.getProjectById(prj.getProjectId())).thenReturn(prj);

		when(taskManagerRepository.save(t99)).thenReturn(t99);

		assertNull((service.createTask(t99).getParentTask()));
	}

	@Test
	public void testDeleteTask_shouldDelete() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(taskDs.get(5).getTaskId())).thenReturn(Optional.of(taskDs.get(5)));

		service.deleteTaskById((taskDs.get(5).getTaskId()));

		verify(taskManagerRepository).delete(taskDs.get(5));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testDeleteTask_shouldThrowException() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(taskDs.get(10).getTaskId()))
				.thenThrow(new RuntimeException("testDeleteTask_shouldThrowException"));
		service.deleteTaskById((taskDs.get(10).getTaskId()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testDeleteInvalidTask_shouldThrowEx() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(12347858L)).thenReturn(Optional.empty());
		service.deleteTaskById(12347858L);
	}

}