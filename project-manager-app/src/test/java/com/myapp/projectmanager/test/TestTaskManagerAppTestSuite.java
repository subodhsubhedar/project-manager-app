package com.myapp.projectmanager.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.myapp.projectmanager.controller.TaskManagerControllerTestManager;
import com.myapp.projectmanager.controller.UserManagerControllerTestManager;
import com.myapp.projectmanager.repository.ProjectManagerDaoIntegrationTestManager;
import com.myapp.projectmanager.repository.TaskManagerDaoIntegrationTestManager;
import com.myapp.projectmanager.repository.UserManagerDaoIntegrationTestManager;
import com.myapp.projectmanager.service.TaskManagerServiceIntegrationTestManager;
import com.myapp.projectmanager.service.TaskManagerServiceUnitTestManager;
import com.myapp.projectmanager.service.UserServiceImplTest;

@RunWith(Suite.class)
@SuiteClasses({ TaskManagerControllerTestManager.class, 
		TaskManagerDaoIntegrationTestManager.class,
		TaskManagerServiceIntegrationTestManager.class, 
		TaskManagerServiceUnitTestManager.class,
		UserManagerControllerTestManager.class,
		UserServiceImplTest.class,
		UserManagerDaoIntegrationTestManager.class,
		ProjectManagerDaoIntegrationTestManager.class
})

public class TestTaskManagerAppTestSuite {

	private static final Logger logger = LoggerFactory.getLogger(TestTaskManagerAppTestSuite.class);

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(TestTaskManagerAppTestSuite.class);
		int i = 1;

		for (Failure failure : result.getFailures()) {
			logger.error("###############  " + "TEST FAILURE : " + i + "  ###############");

			logger.error("\n" + failure.toString());
			failure.getException().printStackTrace();
			i++;
		}

		logger.debug("Test successful? " + result.wasSuccessful());

		logger.debug("\n###############  " + "END OF TEST, TOTAL RUNS : " + i + "  ###############");
	}

}
