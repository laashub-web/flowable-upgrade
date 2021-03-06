/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flowable.upgrade.test;

import java.util.List;

import org.flowable.engine.runtime.Job;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.upgrade.test.helper.RunOnlyWithTestDataFromVersion;
import org.flowable.upgrade.test.helper.UpgradeTestCase;
import org.junit.Test;

import junit.framework.Assert;

/**
 * @author Joram Barrez
 */
@RunOnlyWithTestDataFromVersion(versions = {"5.21.0", "5.22.0"})
public class Activiti5ToV6FailedJobMigrationTest extends UpgradeTestCase {

	@Test
	public void testFailedJobsCorrectlyMigrated() {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey("activiti5-failing-job").singleResult();
		List<Job> deadLetterJobs = managementService.createDeadLetterJobQuery().processInstanceId(processInstance.getId()).list();
		Assert.assertEquals(1, deadLetterJobs.size());
		Assert.assertEquals(0, deadLetterJobs.get(0).getRetries());
	}

}
