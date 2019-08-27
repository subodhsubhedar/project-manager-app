package com.myapp.projectmanager.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.myapp.projectmanager.dto.ParentTaskDTO;
import com.myapp.projectmanager.dto.TaskDTO;
import com.myapp.projectmanager.entity.ParentTask;
import com.myapp.projectmanager.entity.Project;
import com.myapp.projectmanager.entity.Task;
import com.myapp.projectmanager.entity.User;
import com.myapp.projectmanager.exception.ProjectManagerServiceException;
import com.myapp.projectmanager.mapper.ProjectManagerEntityToDtoMapper;
import com.myapp.projectmanager.service.TaskManagerService;
import com.myapp.projectmanager.utils.TestUtils;

@RunWith(Parameterized.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskManagerControllerTestManager {

	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private TaskManagerService taskMngrService;

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
		assertNotNull(taskMngrService);

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
	public void testGetAllTasks_shouldReturnAllTasksCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		when(taskMngrService.findAllTasks()).thenReturn(new HashSet<>(taskDs));
		String expected = mapToJson(getMappedDtoSet(new HashSet<>(taskDs)));

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/tasks", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(taskMngrService, times(1)).findAllTasks();
	}

	@Test
	public void testGetAllParentTasks_shouldReturnAllCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		when(taskMngrService.findAllParenTasks()).thenReturn(new HashSet<>(parentTaskDs));
		String expected = mapToJson(parentTaskDs);

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/parent-tasks", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(taskMngrService, times(1)).findAllParenTasks();
	}

	@Test
	public void testGetTaskInvalidTaskId_shouldReturnBadReq()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/task/A", String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	public void testGetTaskMissingTaskId_shouldReturnNotFound()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/task/", String.class);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

	}

	@Test
	public void testGetTaskNonExistingTaskId_shouldReturnNotFound()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/task/999", String.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

		assertTrue(response.getBody().contains("errors"));
		verify(taskMngrService, times(1)).getTaskById(999L);
	}

	@Test
	public void testGetTaskById_shouldReturnTaskCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		when(taskMngrService.getTaskById(2L)).thenReturn(taskDs.get(0));
		String expected = mapToJson(getMappedDto(taskDs.get(0)));

		ResponseEntity<String> response = getRestTemplateBasicAuth().getForEntity("/task/2", String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);
		verify(taskMngrService, times(1)).getTaskById(2L);
	}

	@Test
	public void testPostTask_shouldCreateNewTask()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ParentTask p = new ParentTask(0L, "Use Case - New Connection", null);

		Task t01 = new Task(0L, "Use case -	New use case", LocalDate.now(), LocalDate.now().plusDays(15), 20, p, prj,
				false);

		t01.setUser(usrDs.get(0));
		
		when(taskMngrService.createTask(any(Task.class))).thenReturn(t01);

		String expected = mapToJson(getMappedDto(t01));

		ResponseEntity<String> response = getRestTemplateBasicAuth().postForEntity("/task/add", t01, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(taskMngrService, times(1)).createTask(any(Task.class));

	}

	@Test
	public void testPostTaskNullReqBody_shouldReturnUnsuppMediaType()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ResponseEntity<String> response = getRestTemplateBasicAuth().postForEntity("/task/add", null, String.class);

		assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());

	}
 
	@Test
	public void testPutTask_shouldUpdateTask()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		ParentTask p = new ParentTask(0L, "Use Case - Test New Connection", null);

		Task t = new Task(0L, "Use case -	Updated use case", LocalDate.now(), LocalDate.now().plusDays(15), 20, p,
				prj, false);
		t.setUser(usrDs.get(0));
		
		when(taskMngrService.updateTask(any(Task.class))).thenReturn(t);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(mapToJson(t), headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/task/update", HttpMethod.PUT, entity,
				String.class);

		String expected = mapToJson(getMappedDto(t));

		assertEquals(HttpStatus.OK, response.getStatusCode());
		JSONAssert.assertEquals(expected, response.getBody(), false);

		verify(taskMngrService, times(1)).updateTask(any(Task.class));

	}

	@Test
	public void testDeleteTaskById_shouldDeleteTaskCorrectly()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/task/delete/1", HttpMethod.DELETE,
				entity, String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());

		verify(taskMngrService, times(1)).deleteTaskById(1L);
	}

	@Test
	public void testPostTaskInvalidMethOd_shouldReturnMethodNotAllowed()
			throws ProjectManagerServiceException, JsonProcessingException, JSONException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = getRestTemplateBasicAuth().exchange("/task/delete/1", HttpMethod.GET, entity,
				String.class);

		assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());

	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		return objectMapper.writeValueAsString(obj);
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	/**
	 * 
	 * @param taskDto
	 * @return
	 */
	private Task getMappedEntity(TaskDTO taskDto) {
		return mapper.getMappedTaskEntity(taskDto);
	}

	/**
	 * 
	 * @param taskEntitySet
	 * @return
	 */
	private Set<TaskDTO> getMappedDtoSet(Set<Task> taskEntitySet) {
		return mapper.getMappedTaskDtoSet(taskEntitySet);
	}

	/**
	 * 
	 * @param parentTaskEntitySet
	 * @return
	 */
	private Set<ParentTaskDTO> getMappedParentDtoSet(Set<ParentTask> parentTaskEntitySet) {
		return mapper.getMappedParentTaskDtoSet(parentTaskEntitySet);
	}

	/**
	 * 
	 * @param taskEntity
	 * @return
	 */
	private TaskDTO getMappedDto(Task taskEntity) {
		return mapper.getMappedTaskDto(taskEntity);
	}

}
