package com.myapp.projectmanager.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
public class UserManagerDaoIntegrationTestManager {

	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	private TestEntityManager entityMngr;

	@Autowired
	private UserRepository userRepository;

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

		List<Object[]> testDataCollection = (List<Object[]>) UserManagerDaoIntegrationTestManager.data();
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
	public void testFindAllUsers_shouldReturnCorrectCount() {
		assertTrue(Integer.valueOf(usrDs.size()).equals(userRepository.findAll().size()));
	}

	@Test
	public void testFindUserByFirstName_shouldReturnCorrectDesc() {
		assertTrue(usrDs.get(4).getFirstName()
				.equals(userRepository.findByFirstName(usrDs.get(4).getFirstName()).get().getFirstName()));
	}

	@Test
	public void testFindUserByLastName_shouldReturnCorrectDesc() {
		assertTrue(usrDs.get(4).getLastName()
				.equals(userRepository.findByLastName(usrDs.get(4).getLastName()).get().getLastName()));
	}

	@Test
	public void testFindUserByFirstName_shouldReturnCorrectProject() {
		assertTrue((usrDs.get(0).getProject().getProject())
				.equals(userRepository.findByFirstName(usrDs.get(0).getFirstName()).get().getProject().getProject()));
	}

	@Test
	public void testCreateUser_shouldCreateNewUser() {

		User usr = new User(0L, 0L, "NewUser", "Roy", prj);
		User usrEntity = userRepository.save(usr);

		assertTrue("NewUser".equals(usrEntity.getFirstName()));
		assertTrue(prj.getProject().equals(usrEntity.getProject().getProject()));
	}

	@Test
	public void testDeleteUser_shouldDelete() {

		User u = userRepository.findByFirstName(usrDs.get(5).getFirstName()).get();
		userRepository.delete(u);

		assertFalse(userRepository.findByFirstName(usrDs.get(5).getFirstName()).isPresent());
	}

	@Test
	public void testUpdateUser_shouldUpdateUser() {

		User u = userRepository.findByFirstName(usrDs.get(2).getFirstName()).get();
		u.setLastName("New Last Name");

		u = userRepository.save(u);

		assertTrue("New Last Name".equals((u).getLastName()));
	}

}