package com.myapp.projectmanager.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.entity.User;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.utils.TestUtils;

@RunWith(Parameterized.class)
@DataJpaTest(showSql = true)
public class ProjectManagerDaoIntegrationTestManager {

	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	private TestEntityManager entityMngr;

	@Autowired
	private ProjectManagerRepository repository;

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

		List<Object[]> testDataCollection = (List<Object[]>) ProjectManagerDaoIntegrationTestManager.data();
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
	public void testFindProjectByName_shouldReturnCorrectDesc() {
		assertTrue(prj.getProject().equals(repository.findByProject(prj.getProject()).get().getProject()));
	}

	@Test
	public void testFindProjectByName_shouldReturnCorrectStartDate() {
		assertTrue(prj.getStartDate().equals(repository.findByProject(prj.getProject()).get().getStartDate()));
	}

	@Test
	public void testFindProjectByName_shouldReturnCorrectEndDate() {
		assertTrue(prj.getEndDate().equals(repository.findByProject(prj.getProject()).get().getEndDate()));
	}

	@Test
	public void testFindProjectByName_shouldReturnCorrectPriority() {
		assertTrue(Integer.valueOf(prj.getPriority())
				.equals(repository.findByProject(prj.getProject()).get().getPriority()));
	}

	@Test
	public void testCreateProject_shouldCreateNewProject() {

		Project prjNew = new Project(0L, "Project - New Pet Clinic Mngmtt System", LocalDate.now(),
				LocalDate.now().plusYears(7), 30);

		prjNew.setTasks(new HashSet<>(taskDs));

		prjNew.setUser(usrDs.get(1));

		Project prjEntity = repository.save(prjNew);

		assertTrue(usrDs.get(1).getFirstName().equals(prjEntity.getUser().getFirstName()));
		assertTrue(("Project - New Pet Clinic Mngmtt System").equals(prjEntity.getProject()));
		assertTrue(new HashSet<>(taskDs).equals(prjEntity.getTasks()));
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void testDeleteProject_shouldDelete() {

		Project prj1 = repository.findByProject(prj.getProject()).get();

		repository.delete(prj1);

		assertTrue(repository.findByProject(prj.getProject()).isPresent());
	}

	@Test
	public void testUpdateProject_shouldUpdateProject() {

		Project prj1 = repository.findByProject(prj.getProject()).get();
		prj1.setPriority(0);
		prj1.setProject("New Project");

		prj1 = repository.save(prj1);

		assertTrue("New Project".equals((prj1).getProject()));
	}

}