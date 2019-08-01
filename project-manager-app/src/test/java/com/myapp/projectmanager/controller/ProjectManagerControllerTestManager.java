package com.myapp.projectmanager.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolationException;

import org.json.JSONException;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.projectmanager.dto.ProjectDTO;
import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.entity.User;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.mapper.ProjectManagerEntityToDtoMapper;
import com.myapp.projectmanager.service.ProjectManagerService;
import com.myapp.projectmanager.utils.TestUtils;

@RunWith(Parameterized.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectManagerControllerTestManager {

	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private ProjectManagerService projectMngrService;

	@Autowired
	private ProjectManagerEntityToDtoMapper mapper;

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
		assertNotNull(projectMngrService);

		assertNotNull(restTemplate);
	}

	/**
	 * 
	 * @return
	 */
	private TestRestTemplate getRestTemplateBasicAuth() {
		return restTemplate.withBasicAuth("subodh", "subodh123");
	}

	@Test
	public void testGetAllProjects_shouldReturnAllProjectsCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		Set<Project> prjSet = new HashSet<>();

		when(projectMngrService.findAllProjects()).thenReturn(prjSet);

		String expected = mapToJson(getMappedDtoSet(prjSet));

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/projects", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(projectMngrService, times(1)).findAllProjects();
	}

	@Test
	public void testGetProjectInvalidProjectId_shouldReturnBadReq()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/project/A", String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testGetProjectMissingProjectId_shouldReturnNotFound()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/project/", String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

	}

	@Test
	public void testGetProjectNonExistingUserId_shouldReturnNotFound()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/project/999", String.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		assertTrue(response.getBody().contains("errors"));
		verify(projectMngrService, times(1)).getProjectById(999L);
	}

	@Test
	public void testGetProjectById_shouldReturnUserCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		when(projectMngrService.getProjectById(2L)).thenReturn(prj);
		String expected = mapToJson(getMappedDto(prj));

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/project/2", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(projectMngrService, times(1)).getProjectById(2L);
	}

	@Test
	public void testPostProject_shouldCreateNewProject()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		Project prj1 = new Project(0L, "Project - Hospital management system", LocalDate.now(),
				LocalDate.now().plusYears(1), 27);

		when(projectMngrService.createProject(any(Project.class))).thenReturn(prj1);

		String expected = mapToJson(getMappedDto(prj1));

		ResponseEntity<String> response = getRestTemplateBasicAuth().postForEntity("/project/add", prj1, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(projectMngrService, times(1)).createProject(any(Project.class));

	}

	@Test
	public void testPostProjectNullReqBody_shouldReturnUnsuppMediaType()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().postForEntity("/project/add", null, String.class);

		assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());

	}

	@Test
	public void testPutProject_shouldUpdateProject()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		Project prj1 = new Project(0L, "Project - Hospital management system", LocalDate.now(),
				LocalDate.now().plusYears(1), 27);
		prj1.setProject("Updated Project - Hospital management system");

		User usr = new User(0L, 0L, "James", "Bond", prj1, null);
		prj1.setUser(usr);

		when(projectMngrService.updateProject(any(Project.class))).thenReturn(prj1);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(mapToJson(getMappedDto(prj1)), headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/project/update", HttpMethod.PUT, entity,
				String.class);

		String expected = mapToJson(prj1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(projectMngrService, times(1)).updateProject(any(Project.class));

	}

	@Test
	public void testDeleteProjectById_shouldDeleteProjectCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/project/delete/1", HttpMethod.DELETE,
				entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(projectMngrService, times(1)).deleteProjectById(1L);
	}

	@Test
	public void testPostProjectInvalidMethOd_shouldReturnMethodNotAllowed()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/project/delete/1", HttpMethod.GET,
				entity, String.class);

		assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());

	}

	@Test
	public void testPutProj_shouldThrowException() throws ProjectManagerServiceException, JsonProcessingException {

		Project prj1 = new Project(0L, null, null, null, -1);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(mapToJson(getMappedDto(prj1)), headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/project/update", HttpMethod.PUT, entity,
				String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

	}

	@Test
	public void testPutProj_shouldThrowConstraintException()
			throws ProjectManagerServiceException, JsonProcessingException {
		Project prj1 = new Project(0L, null, null,
				null,-1);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(mapToJson(getMappedDto(prj1)), headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/project/update", HttpMethod.PUT, entity,
				String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

	} 
	
	
	@Test
	public void testGetProjectById_shouldReturnNotFound()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/project/", String.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}	
	
	@Test
	public void testGetProjectById_shouldReturnNoHandlerFound()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		
		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/projectaa/2", String.class);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		return objectMapper.writeValueAsString(obj);
	}

	/**
	 * 
	 * @param userEntitySet
	 * @return
	 */
	private Set<ProjectDTO> getMappedDtoSet(Set<Project> projectEntity) {
		return mapper.getMappedProjectDtoSet(projectEntity);
	}

	/**
	 * 
	 * @param userEntitySet
	 * @return
	 */
	private ProjectDTO getMappedDto(Project projectEntity) {
		return mapper.getMappedProjectDto(projectEntity);
	}
}
