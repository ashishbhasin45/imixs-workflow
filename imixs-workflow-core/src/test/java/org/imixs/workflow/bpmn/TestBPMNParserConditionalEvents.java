package org.imixs.workflow.bpmn;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Assert;

import org.imixs.workflow.ItemCollection;
import org.imixs.workflow.exceptions.ModelException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 * Test class test the Imixs BPMNParser.
 * 
 * Special case: Conditional-Events
 * 
 * @see issue #299
 * @author rsoika
 */
public class TestBPMNParserConditionalEvents {

	@Before
	public void setup() {

	}

	@After
	public void teardown() {

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSimple()
			throws ParseException, ParserConfigurationException, SAXException, IOException, ModelException {

		InputStream inputStream = getClass().getResourceAsStream("/bpmn/conditional_event1.bpmn");

		BPMNModel model = null;
		try {
			model = BPMNParser.parseModel(inputStream, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (ModelException e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertNotNull(model);

		// Test Environment
		ItemCollection profile = model.getDefinition();
		Assert.assertNotNull(profile);

		// test count of elements
		Assert.assertEquals(3, model.findAllTasks().size());

		// test task 1000
		ItemCollection task = model.getTask(1000);
		Assert.assertNotNull(task);

		// test events for task 1000
		List<ItemCollection> events = model.findAllEventsByTask(1000);
		Assert.assertNotNull(events);
		Assert.assertEquals(1, events.size());

		// test activity 1000.10 submit
		ItemCollection activity = model.getEvent(1000, 10);
		Assert.assertNotNull(activity);
		Assert.assertEquals("conditional event", activity.getItemValueString("txtname"));

		Assert.assertEquals(1000, activity.getItemValueInteger("numNextProcessID"));

		// Now we need to evaluate if the Event is marked as an conditional Event with
		// the condition list copied from the gateway.
		Assert.assertTrue(activity.hasItem("keyConditions"));
		Map<Integer,String> conditions=(Map<Integer, String>) activity.getItemValue("keyConditions").get(0);
		Assert.assertNotNull(conditions);
		Assert.assertEquals("(workitem._budget && workitem._budget[0]>100)", conditions.get(1100));
		Assert.assertEquals("(workitem._budget && workitem._budget[0]<=100)", conditions.get(1200));
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	@Ignore
	public void testFollowUp()
			throws ParseException, ParserConfigurationException, SAXException, IOException, ModelException {

		InputStream inputStream = getClass().getResourceAsStream("/bpmn/conditional_event2.bpmn");

		BPMNModel model = null;
		try {
			model = BPMNParser.parseModel(inputStream, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Assert.fail();
		} catch (ModelException e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertNotNull(model);

		// Test Environment
		ItemCollection profile = model.getDefinition();
		Assert.assertNotNull(profile);

		// test count of elements
		Assert.assertEquals(3, model.findAllTasks().size());

		// test task 1000
		ItemCollection task = model.getTask(1000);
		Assert.assertNotNull(task);

		// test events for task 1000
		List<ItemCollection> events = model.findAllEventsByTask(1000);
		Assert.assertNotNull(events);
		Assert.assertEquals(2, events.size());

		// test activity 1000.10 submit
		ItemCollection activity = model.getEvent(1000, 10);
		Assert.assertNotNull(activity);
		Assert.assertEquals("conditional event", activity.getItemValueString("txtname"));

		Assert.assertEquals(1000, activity.getItemValueInteger("numNextProcessID"));


		// Now we need to evaluate if the Event is marked as an conditional Event with
		// the condition list copied from the gateway.
		Assert.assertTrue(activity.hasItem("keyConditions"));
		Map<Integer,String> conditions=(Map<Integer, String>) activity.getItemValue("keyConditions").get(0);
		Assert.assertNotNull(conditions);
		Assert.assertEquals("(workitem._budget && workitem._budget[0]>100)", conditions.get(1100));
		Assert.assertEquals("(workitem._budget && workitem._budget[0]<=100)", conditions.get(1200));

	}

}