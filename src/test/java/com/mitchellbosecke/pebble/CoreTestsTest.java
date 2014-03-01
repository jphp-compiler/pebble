/*******************************************************************************
 * Copyright (c) 2013 by Mitchell Bösecke
 * 
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package com.mitchellbosecke.pebble;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

public class CoreTestsTest extends AbstractTest {

	@Test
	public void testEven() throws PebbleException, IOException {
		Loader loader = new StringLoader();
		PebbleEngine pebble = new PebbleEngine(loader);

		String source = "{% if 2 is even %}yes{% else %}no{% endif %}{% if 3 is even %}no{% else %}yes{% endif %}";
		PebbleTemplate template = pebble.getTemplate(source);

		Writer writer = new StringWriter();
		template.evaluate(writer);
		assertEquals("yesyes", writer.toString());
	}
	
	/**
	 * Pebble parses numbers as longs so we want to make sure our
	 * numerical tests will work even if we force it to take an int
	 * as an input.
	 * 
	 * @throws PebbleException
	 * @throws IOException
	 */
	@Test
	public void testEvenWithInteger() throws PebbleException, IOException {
		Loader loader = new StringLoader();
		PebbleEngine pebble = new PebbleEngine(loader);

		String source = "{% if num is even %}yes{% else %}no{% endif %}";
		PebbleTemplate template = pebble.getTemplate(source);

		Map<String, Object> context = new HashMap<>();
		context.put("num", new Integer(2));
		Writer writer = new StringWriter();
		template.evaluate(writer, context);
		assertEquals("yes", writer.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullEven() throws PebbleException, IOException {
		Loader loader = new StringLoader();
		PebbleEngine pebble = new PebbleEngine(loader);

		String source = "{% if null is even %}yes{% else %}no{% endif %}";
		PebbleTemplate template = pebble.getTemplate(source);

		Writer writer = new StringWriter();
		template.evaluate(writer);
	}

	@Test
	public void testOdd() throws PebbleException, IOException {
		Loader loader = new StringLoader();
		PebbleEngine pebble = new PebbleEngine(loader);

		String source = "{% if 2 is odd %}no{% else %}yes{% endif %}{% if 3 is odd %}yes{% else %}no{% endif %}";
		PebbleTemplate template = pebble.getTemplate(source);

		Writer writer = new StringWriter();
		template.evaluate(writer);
		assertEquals("yesyes", writer.toString());
	}
	
	/**
	 * Pebble parses numbers as longs so we want to make sure our
	 * numerical tests will work even if we force it to take an int
	 * as an input.
	 * 
	 * @throws PebbleException
	 * @throws IOException
	 */
	@Test
	public void testOddWithInteger() throws PebbleException, IOException {
		Loader loader = new StringLoader();
		PebbleEngine pebble = new PebbleEngine(loader);

		String source = "{% if num is odd %}yes{% else %}no{% endif %}";
		PebbleTemplate template = pebble.getTemplate(source);

		Map<String, Object> context = new HashMap<>();
		context.put("num", new Integer(3));
		Writer writer = new StringWriter();
		template.evaluate(writer, context);
		assertEquals("yes", writer.toString());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNullOdd() throws PebbleException, IOException {
		Loader loader = new StringLoader();
		PebbleEngine pebble = new PebbleEngine(loader);

		String source = "{% if null is odd %}yes{% else %}no{% endif %}";
		PebbleTemplate template = pebble.getTemplate(source);

		Writer writer = new StringWriter();
		template.evaluate(writer);
	}

	@Test
	public void testNull() throws PebbleException, IOException {
		Loader loader = new StringLoader();
		PebbleEngine pebble = new PebbleEngine(loader);

		String source = "{% if null is null %}yes{% endif %}{% if obj is null %}yes{% endif %}{% if 2 is null %}no{% else %}yes{% endif %}";
		PebbleTemplate template = pebble.getTemplate(source);
		Map<String, Object> context = new HashMap<>();
		context.put("obj", null);

		Writer writer = new StringWriter();
		template.evaluate(writer, context);
		assertEquals("yesyesyes", writer.toString());
	}

	@Test
	public void testEmpty() throws PebbleException, IOException {
		Loader loader = new StringLoader();
		PebbleEngine pebble = new PebbleEngine(loader);

		String source = "{% if null is empty() %}yes{% endif %}{% if '  ' is empty() %}yes{% endif %}{% if obj is empty() %}yes{% endif %}";
		PebbleTemplate template = pebble.getTemplate(source);
		Map<String, Object> context = new HashMap<>();
		context.put("obj", new ArrayList<String>());

		Writer writer = new StringWriter();
		template.evaluate(writer, context);
		assertEquals("yesyesyes", writer.toString());
	}

	@Test
	public void testIterables() throws PebbleException, IOException {
		Loader loader = new StringLoader();
		PebbleEngine pebble = new PebbleEngine(loader);

		String source = "{% if null is iterable() %}no{% else %}yes{% endif %}{% if obj1 is iterable() %}yes{% else %}no{% endif %}{% if obj2 is iterable() %}no{% else %}yes{% endif %}";
		PebbleTemplate template = pebble.getTemplate(source);
		Map<String, Object> context = new HashMap<>();
		context.put("obj1", new ArrayList<String>());
		context.put("obj2", new HashMap<String, Object>());

		Writer writer = new StringWriter();
		template.evaluate(writer, context);
		assertEquals("yesyesyes", writer.toString());
	}

	@Test
	public void testIsnt() throws PebbleException, IOException {
		Loader loader = new StringLoader();
		PebbleEngine pebble = new PebbleEngine(loader);

		String source = "{% if 2 is not odd %}yes{% else %}no{% endif %}{% if null is not iterable() %}yes{% else %}no{% endif %}";
		PebbleTemplate template = pebble.getTemplate(source);

		Writer writer = new StringWriter();
		template.evaluate(writer);
		assertEquals("yesyes", writer.toString());
	}

	/**
	 * Using the unary "not" operator before a test.
	 * 
	 * Issue #27
	 * 
	 * @throws PebbleException
	 * @throws IOException
	 */
	@Test
	public void testNegativeTest() throws PebbleException, IOException {
		Loader loader = new StringLoader();
		PebbleEngine pebble = new PebbleEngine(loader);

		String source = "{% if not 2 is odd %}yes{% else %}no{% endif %}";
		PebbleTemplate template = pebble.getTemplate(source);

		Writer writer = new StringWriter();
		template.evaluate(writer);
		assertEquals("yes", writer.toString());
	}

	/**
	 * Similar to the testNegativeTest() except with an attribute of an object
	 * in the context.
	 * 
	 * Issue #27
	 * 
	 * @throws PebbleException
	 * @throws IOException
	 */
	@Test
	public void testNegativeTestOnAttribute() throws PebbleException, IOException {
		Loader loader = new StringLoader();
		PebbleEngine pebble = new PebbleEngine(loader);

		String source = "{% if not classroom.students is empty %}yes{% else %}no{% endif %}";
		PebbleTemplate template = pebble.getTemplate(source);
		Map<String, Object> context = new HashMap<>();
		context.put("classroom", new Classroom());
		Writer writer = new StringWriter();
		template.evaluate(writer, context);
		assertEquals("no", writer.toString());
	}

	private static class Classroom {
		@SuppressWarnings("unused")
		public static List<Object> students = new ArrayList<>();
	}

}
