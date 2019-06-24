package com.myapp.projectmanager.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.repository.ParentTaskManagerRepository;
import com.myapp.projectmanager.repository.TaskManagerRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskManagerServiceIntegrationTestManager {

	@MockBean
	private TaskManagerRepository taskManagerRepository;

	@MockBean
	private ParentTaskManagerRepository parentTaskManagerRepository;

	@Autowired
	private ProjectManagerService service;

	private List<Task> taskDs;

	private List<ParentTask> parentTaskDs;

	@Test
	public void runSmokeTest() {

		assertNotNull(taskManagerRepository);

		assertNotNull(parentTaskManagerRepository);

		assertNotNull(service);
	}

	@Before
	public void setUpDS() {

	}

	@Test
	public void testFindAllParentTasks_shouldReturnCorrectCount() throws ProjectManagerServiceException {
		when(parentTaskManagerRepository.findAll()).thenReturn(parentTaskDs);

		assertTrue(Integer.valueOf(3).equals(service.findAllParenTasks().size()));
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

		assertTrue(Integer.valueOf(13).equals(service.findAllTasks().size()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindAllTasks_shouldThrowEx() throws ProjectManagerServiceException {
		when(taskManagerRepository.findAll()).thenThrow(new RuntimeException("testFindAllTasks_shouldThrowEx"));

		service.findAllTasks().size();
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectDesc() throws ProjectManagerServiceException {

		when(taskManagerRepository.findByTask("Check criminal records")).thenReturn(Optional.of(taskDs.get(6)));

		assertTrue("Check criminal records".equals((service.getTask("Check criminal records")).getTask()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindTaskByDesc_shouldThrowEx() throws ProjectManagerServiceException {
		when(taskManagerRepository.findByTask("Check criminal records"))
				.thenThrow(new RuntimeException("testFindTaskByDesc_shouldThrowEx"));

		service.getTask("Check criminal records");
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindNonExistingTaskByDesc_shouldThrowEx() throws ProjectManagerServiceException {
		when(taskManagerRepository.findByTask("abc")).thenReturn(Optional.empty());

		service.getTask("abc");
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectPriority() throws ProjectManagerServiceException {

		when(taskManagerRepository.findByTask("Check Politically Exposed Person"))
				.thenReturn(Optional.of(taskDs.get(7)));

		assertTrue(Integer.valueOf(20).equals((service.getTask("Check Politically Exposed Person")).getPriority()));
	}

	@Test
	public void testFindTaskById_shouldReturnTask() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(12L)).thenReturn(Optional.of(taskDs.get(10)));
		assertNotNull(service.getTaskById(12L));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindTaskByNonExistingId_shouldThrowEx() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(1123342L)).thenReturn(Optional.empty());
		service.getTaskById(1123342L);
	}

	@Test
	public void testFindParentTaskById_shouldReturnCorrectDesc() throws ProjectManagerServiceException {

		when(parentTaskManagerRepository.findById(6L)).thenReturn(Optional.of(parentTaskDs.get(2)));

		assertTrue("KYC".equals((service.getParentTaskById(6L).getParentTaskDesc())));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindParentTaskById_shouldThrowEx() throws ProjectManagerServiceException {

		when(parentTaskManagerRepository.findById(6L))
				.thenThrow(new RuntimeException("testFindParentTaskById_shouldThrowEx"));

		service.getParentTaskById(6L).getParentTaskDesc();

	}

	public void testFindParentTaskById_shouldRetNullAndPass() throws ProjectManagerServiceException {

		when(parentTaskManagerRepository.findById(101111L)).thenReturn(Optional.of(parentTaskDs.get(2)));

		assertNull((service.getParentTaskById(101111L).getParentTaskDesc()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectCompletionStatus() throws ProjectManagerServiceException {

		when(taskManagerRepository.findByTask("Check ID Proofs")).thenReturn(Optional.of(taskDs.get(9)));

		assertTrue(Boolean.valueOf(true).equals((service.getTask("Check ID Proofs")).getTaskComplete()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectStartDate() throws ProjectManagerServiceException {

		when(taskManagerRepository.findByTask("Check ID Proofs")).thenReturn(Optional.of(taskDs.get(9)));

		assertTrue(LocalDate.now().equals((service.getTask("Check ID Proofs")).getStartDate()));
	}

	@Test
	public void testFindTaskByDesc_shouldReturnCorrectEndDate() throws ProjectManagerServiceException {

		when(taskManagerRepository.findByTask("Check ID Proofs")).thenReturn(Optional.of(taskDs.get(9)));

		assertTrue(LocalDate.of(2019, 7, 30).equals((service.getTask("Check ID Proofs")).getEndDate()));
	}

/*	@Test
	public void testCreateTask_shouldCreateNewTask() throws ProjectManagerServiceException {

		Task t = new Task(15L, "Address verification", LocalDate.now(), LocalDate.of(2019, 9, 30), 20,
				parentTaskDs.get(2), true);

		when(taskManagerRepository.save(t)).thenReturn(t);
		assertTrue("Address verification".equals(service.createTask(t).getTask()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testCreateTask_shouldThrowException() throws ProjectManagerServiceException {

		Task t = new Task(15L, "Address verification", LocalDate.now(), LocalDate.of(2019, 9, 30), 20,
				parentTaskDs.get(2), true);

		when(taskManagerRepository.save(t)).thenThrow(new RuntimeException("testCreateTask_shouldThrowException"));
		service.createTask(t).getTask();
	}

	@Test
	public void testUpdateTask_shouldRetUpdatedTask() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(10L)).thenReturn(Optional.of(taskDs.get(9)));

		Task t7 = new Task(10L, "Check criminal records - Updated", LocalDate.now(), LocalDate.of(2019, 11, 28), 20,
				parentTaskDs.get(2), true);

		when(taskManagerRepository.save(taskDs.get(9))).thenReturn((t7));

		assertTrue("Check criminal records - Updated".equals(service.updateTask(t7).getTask()));
		assertTrue(LocalDate.of(2019, 11, 28).equals(service.updateTask(t7).getEndDate()));
		assertTrue((service.updateTask(t7).getTaskComplete()));

	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testUpdateTask_shouldThrowEx() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(10L)).thenReturn(Optional.of(taskDs.get(9)));

		Task t7 = new Task(10L, "Check criminal records - Updated", LocalDate.now(), LocalDate.of(2019, 11, 28), 20,
				parentTaskDs.get(2), true);

		when(taskManagerRepository.save(taskDs.get(9))).thenThrow(new RuntimeException("testUpdateTask_shouldThrowEx"));

		service.updateTask(t7);

	}

	@Test
	public void testCreateTaskWithoutParentTask_shouldPass() throws ProjectManagerServiceException {

		Task t = new Task(15L, "Miscellanous", LocalDate.now(), LocalDate.of(2019, 10, 30), 1, null, true);

		when(taskManagerRepository.save(t)).thenReturn(t);

		assertNull((service.createTask(t).getParentTask()));
	}
*/
	@Test
	public void testDeleteTask_shouldDelete() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(12L)).thenReturn(Optional.of(taskDs.get(10)));

		service.deleteTaskById((taskDs.get(10).getTaskId()));

		verify(taskManagerRepository).delete(taskDs.get(10));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testDeleteTask_shouldThrowException() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(12L))
				.thenThrow(new RuntimeException("testDeleteTask_shouldThrowException"));

		service.deleteTaskById((taskDs.get(10).getTaskId()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testDeleteInvalidTask_shouldThrowEx() throws ProjectManagerServiceException {

		when(taskManagerRepository.findById(12347858L)).thenReturn(Optional.empty());

		service.deleteTaskById(12347858L);
	}

}