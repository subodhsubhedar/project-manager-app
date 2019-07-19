package com.myapp.projectmanager.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.myapp.projectmanager.repository.UserRepository;
import com.myapp.projectmanager.utils.TestUtils;

@RunWith(Parameterized.class)
@SpringBootTest
public class UserServiceImplTest {

	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@MockBean
	private UserRepository userRepository;

	@Autowired
	private UserService service;

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

		assertNotNull(userRepository);
		assertNotNull(service);
	}

	@Test
	public void testFindAllUsers() throws ProjectManagerServiceException {

		when(userRepository.findAll()).thenReturn(usrDs);
		assertTrue(Integer.valueOf(usrDs.size()).equals(service.findAllUsers().size()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testFindAllUsers_shouldThrowEx() throws ProjectManagerServiceException {
		when(userRepository.findAll()).thenThrow(new RuntimeException("testFindAllUsers_shouldThrowEx"));

		service.findAllUsers().size();
	}

	@Test
	public void testCreateUser() throws ProjectManagerServiceException {
		User usr = new User(0L, 0L, "Jack", "Stokes", prj, null);

		when(userRepository.save(usr)).thenReturn(usr);
		assertTrue("Jack".equals(service.createUser(usr).getFirstName()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testCreateUser_shouldThrowEx() throws ProjectManagerServiceException {
		User usr = new User(0L, 0L, "Jack", "Stokes", prj, null);

		when(userRepository.save(usr)).thenThrow(new RuntimeException("testCreateUser_shouldThrowEx"));
		service.createUser(usr);
	}

	@Test
	public void testUpdateUser() throws ProjectManagerServiceException {

		when(userRepository.findById(usrDs.get(1).getUserId())).thenReturn(Optional.of(usrDs.get(1)));

		User usrUpdated = usrDs.get(1);
		usrUpdated.setFirstName("Updated firstname");
		usrUpdated.setLastName("Updated lastname");

		usrUpdated.setProject(prj);

		when(userRepository.save(usrDs.get(1))).thenReturn((usrUpdated));

		assertTrue("Updated firstname".equals(service.updateUser(usrDs.get(1)).getFirstName()));
		assertTrue("Updated lastname".equals(service.updateUser(usrDs.get(1)).getLastName()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testUpdateUser_throwEx() throws ProjectManagerServiceException {

		when(userRepository.findById(usrDs.get(1).getUserId())).thenReturn(Optional.of(usrDs.get(1)));

		User usrUpdated = usrDs.get(1);
		usrUpdated.setFirstName("Updated firstname");
		usrUpdated.setLastName("Updated lastname");

		usrUpdated.setProject(prj);

		when(userRepository.save(usrDs.get(1))).thenThrow(new RuntimeException("testUpdateUser_throwEx"));

		service.updateUser(usrDs.get(1));
	}

	@Test
	public void testGetUserById() throws ProjectManagerServiceException {

		when(userRepository.findById(usrDs.get(2).getUserId())).thenReturn(Optional.of(usrDs.get(2)));
		assertNotNull(service.getUserById(usrDs.get(2).getUserId()));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testGetUserById_throwEx() throws ProjectManagerServiceException {

		when(userRepository.findById(usrDs.get(2).getUserId()))
				.thenThrow(new RuntimeException("testGetUserById_throwEx"));
		service.getUserById(usrDs.get(2).getUserId());
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testGetUserById_shouldThrowException() throws ProjectManagerServiceException {

		when(userRepository.findById(12347858L)).thenReturn(Optional.empty());
		service.getUserById(12347858L).getUserId();
	}

	@Test
	public void testDeleteUserById() throws ProjectManagerServiceException {
		when(userRepository.findById(usrDs.get(3).getUserId())).thenReturn(Optional.of(usrDs.get(3)));

		service.deleteUserById((usrDs.get(3).getUserId()));

		verify(userRepository).delete(usrDs.get(3));
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testDeleteInvalidUsr_shouldThrowEx() throws ProjectManagerServiceException {

		when(userRepository.findById(12347858L)).thenReturn(Optional.empty());
		service.deleteUserById(12347858L);
	}

	@Test(expected = ProjectManagerServiceException.class)
	public void testDeleteUsr_shouldThrowException() throws ProjectManagerServiceException {

		when(userRepository.findById(usrDs.get(3).getUserId()))
				.thenThrow(new RuntimeException("testDeleteUsr_shouldThrowException"));
		service.deleteUserById((usrDs.get(3).getUserId()));
	}
}
