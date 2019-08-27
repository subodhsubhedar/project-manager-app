package com.myapp.projectmanager.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.entity.User;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.repository.ProjectManagerRepository;
import com.myapp.projectmanager.utils.TestUtils;

@RunWith(Parameterized.class)
public class ProjectManagerServiceUnitTestManager {

	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@InjectMocks
	private ProjectManagerServiceImpl service;

	@Mock
	private ProjectManagerRepository pmRepositoryMock;

	@Mock
	private UserService userService;

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
	public void whenSmokeTest_thenSuccess() {

		assertNotNull(service);
	}

	@Before
	public void setUp() {
		service = new ProjectManagerServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testFindAllProjects_shouldReturnCorrectCount() throws ProjectManagerServiceException {

		List<Project> prjList = new ArrayList<>();
		prjList.add(prj);

		when(pmRepositoryMock.findAll()).thenReturn(prjList);

		assertTrue(Integer.valueOf(prjList.size()).equals(service.findAllProjects().size()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindAllProjects_shouldThrowEx() throws ProjectManagerServiceException {
		when(pmRepositoryMock.findAll()).thenThrow(new RuntimeException("testFindAllProjects_shouldThrowEx"));

		service.findAllProjects().size();
	}

	@Test
	public void testFindProjectByDesc_shouldReturnCorrectDesc() throws ProjectManagerServiceException {

		when(pmRepositoryMock.findByProject(prj.getProject())).thenReturn(Optional.of(prj));
		assertTrue(prj.getProject().equals(service.getProject(prj.getProject()).getProject()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindProjectByDesc_shouldThrowEx() throws ProjectManagerServiceException {
		when(pmRepositoryMock.findByProject(prj.getProject()))
				.thenThrow(new RuntimeException("testFindProjectByDesc_shouldThrowEx"));

		service.getProject(prj.getProject());
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindNonExistingProjectByDesc_shouldThrowEx() throws ProjectManagerServiceException {
		when(pmRepositoryMock.findByProject("abc")).thenReturn(Optional.empty());

		service.getProject("abc");
	}

	@Test
	public void testFindProjectByDesc_shouldReturnCorrectPriority() throws ProjectManagerServiceException {

		when(pmRepositoryMock.findByProject(prj.getProject())).thenReturn(Optional.of(prj));

		assertTrue(Integer.valueOf(prj.getPriority()).equals((service.getProject(prj.getProject())).getPriority()));
	}

	@Test
	public void testFindProjectById_shouldReturnTask() throws ProjectManagerServiceException {

		when(pmRepositoryMock.findById(prj.getProjectId())).thenReturn(Optional.of(prj));
		assertNotNull(service.getProjectById(prj.getProjectId()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindProjectByNonExistingId_shouldThrowEx() throws ProjectManagerServiceException {

		when(pmRepositoryMock.findById(999999L)).thenReturn(Optional.empty());
		service.getProjectById(999999L);
	}

	@Test
	public void testFindProjectByDesc_shouldReturnCorrectStartDate() throws ProjectManagerServiceException {

		when(pmRepositoryMock.findByProject(prj.getProject())).thenReturn(Optional.of(prj));

		assertTrue(prj.getStartDate().equals((service.getProject(prj.getProject())).getStartDate()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindProjectByDesc_shouldThrowException() throws ProjectManagerServiceException {

		when(pmRepositoryMock.findByProject(prj.getProject())).thenReturn(null);

		service.getProject(prj.getProject());
	}

	@Test
	public void testFindProjectByDesc_shouldReturnCorrectEndDate() throws ProjectManagerServiceException {

		when(pmRepositoryMock.findByProject(prj.getProject())).thenReturn(Optional.of(prj));

		assertTrue(prj.getEndDate().equals(service.getProject(prj.getProject()).getEndDate()));
	}

	@Test
	public void testCreateProject_shouldCreateNewProject() throws ProjectManagerServiceException {

		Project prj1 = new Project(0L, "Project - New Mobile Subscription management system", LocalDate.now(),
				LocalDate.now().plusYears(1), 45);
		prj1.setUser(usrDs.get(0)); 
		
		when(userService.getUserById(prj.getUser().getUserId())).thenReturn(usrDs.get(0));

		when(pmRepositoryMock.save(prj1)).thenReturn(prj1);
		
		assertTrue(
				"Project - New Mobile Subscription management system".equals(service.createProject(prj1).getProject()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testCreateProject_shouldThrowException() throws ProjectManagerServiceException {

		Project prj2 = new Project(0L, "Project - New Mobile Subscription management system", LocalDate.now(),
				LocalDate.now().plusYears(1), 45);
		prj2.setUser(usrDs.get(0));
		
		when(userService.getUserById(0L)).thenReturn(usrDs.get(0));
		
		when(pmRepositoryMock.save(prj2)).thenThrow(new RuntimeException("testCreateProject_shouldThrowException"));
		service.createProject(prj2).getProject();
	}

	@Test
	public void testUpdateProject_shouldRetUpdatedProject() throws ProjectManagerServiceException {

		Project pUpdated = prj;
		prj.setPriority(1);
		User usr = new User(0L, 0L, "Jack", "Stokes", prj);

		prj.setUser(usr);

		when(userService.getUserById(prj.getUser().getUserId())).thenReturn(usr);

		when(pmRepositoryMock.findById(prj.getProjectId())).thenReturn(Optional.of(prj));

		when(pmRepositoryMock.save(prj)).thenReturn((pUpdated));

		assertTrue(Integer.valueOf(1).equals(service.updateProject(prj).getPriority()));
		assertTrue((service.updateProject(prj).getUser().getFirstName().equals("Jack")));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testUpdateProject_shouldThrowEx() throws ProjectManagerServiceException {

		when(pmRepositoryMock.findById(prj.getProjectId())).thenReturn(Optional.of(prj));
		when(pmRepositoryMock.save(prj)).thenThrow(new RuntimeException("testUpdateProject_shouldThrowEx"));

		service.updateProject(prj);
	}

	@Test
	public void testDeleteProject_shouldDelete() throws ProjectManagerServiceException {

		when(userService.getUserById(prj.getUser().getUserId())).thenReturn(usrDs.get(0));
		
		when(pmRepositoryMock.findById(prj.getProjectId())).thenReturn(Optional.of(prj));

		service.deleteProjectById((prj.getProjectId()));

		verify(pmRepositoryMock).delete(prj);
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testDeleteProject_shouldThrowException() throws ProjectManagerServiceException {

		when(pmRepositoryMock.findById(prj.getProjectId()))
				.thenThrow(new RuntimeException("testDeleteProject_shouldThrowException"));
		service.deleteProjectById((prj.getProjectId()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testDeleteInvalidProject_shouldThrowEx() throws ProjectManagerServiceException {

		when(pmRepositoryMock.findById(12347858L)).thenReturn(Optional.empty());
		service.deleteProjectById(12347858L);
	}

}
