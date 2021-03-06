package org.flowable.upgrade.test;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.upgrade.test.helper.RunOnlyWithTestDataFromVersion;
import org.flowable.upgrade.test.helper.UpgradeTestCase;
import org.junit.Test;

/**
 * This is an upgrade test added for the 5.11 release. In that release, we've
 * added a database column to store the process definition description
 * (documentation element in bpmn 2.0 xml).
 * 
 * @author Joram Barrez
 */
@RunOnlyWithTestDataFromVersion(versions = {"5.7", "5.8", "5.9", "5.10"})
public class VerifyProcessDefinitionDescriptionTest extends UpgradeTestCase {

  @Test
  public void testProcessDefinitionDescription() {

    // We don't upgrade the process definition description, we only add the column.
    // So we'll just verify if the process definition description is null and if
    // we can add a process description afterwards

    ProcessDefinition processDefinition = getLatestVersionOfProcessDefinition();
    assertNotNull(processDefinition);
    assertNull(processDefinition.getDescription());

    // If we now redeploy the same process definition, the description should be  set
    deployTestProcess();
    processDefinition = getLatestVersionOfProcessDefinition();
    assertEquals("This is not really a very usable process...", processDefinition.getDescription());

    // Cleanup
	List<Deployment> deployments = repositoryService.createDeploymentQuery().processDefinitionKey("verifyProcessDefinitionDescription").list();
	for (Deployment deployment : deployments) {
		repositoryService.deleteDeployment(deployment.getId(), true);
	}
  }

  protected void deployTestProcess() {
    processEngine.getRepositoryService().createDeployment().name("verifyProcessDefinitionDescription")
            .addClasspathResource("org/activiti/upgrade/test/VerifyProcessDefinitionDescriptionTest.bpmn20.xml").deploy();
  }

  protected ProcessDefinition getLatestVersionOfProcessDefinition() {
    return processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("verifyProcessDefinitionDescription").latestVersion()
            .singleResult();
  }

}
