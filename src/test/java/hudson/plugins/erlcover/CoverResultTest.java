package hudson.plugins.erlcover;

import hudson.plugins.erlcover.CoverResult.UnknownApplicationException;

import java.util.Enumeration;
import java.util.Hashtable;

import org.junit.Before;
import junit.framework.TestCase;

public class CoverResultTest extends TestCase {

	private CoverResult result;

	@Before
	public void setUp() {
		String directory = new String(getClass().getResource("/first.xml")
				.getPath());
		directory = directory.substring(0, directory.lastIndexOf('/'));
		String[] files = new String[2];
		files[0] = new String("first.xml");
		files[1] = new String("second.xml");
		result = new CoverResult(directory, files);
	}

	public void test_application_names() {
		Enumeration<String> expected = application_names_for_test();
		Enumeration<String> names = result.applicationNames();

		while (names.hasMoreElements()) {
			assertEquals(names.nextElement(), expected.nextElement());
		}
	}

	public void test_module_names() throws UnknownApplicationException {
		Enumeration<String> expected = module_names_for_test();
		Enumeration<String> names = result.moduleNames("sample_rake");

		while (names.hasMoreElements()) {
			assertEquals(names.nextElement(), expected.nextElement());
		}
	}

	public void test_module_names_for_unknown_application() {
		try {
			result.moduleNames("unknown");
			fail("Must throw an exception");
		} catch (UnknownApplicationException e) {
		}
	}

	public void test_total_for_existing_application() throws Exception {
		assertEquals("54", result.total_for("sample_rake"));
	}

	public void test_called_for_existing_application() throws Exception {
		assertEquals("2", result.called_for("sample_rake"));
	}
	
	public void test_coverage_for_existing_application() throws Exception {
		assertEquals("3", result.coverage_for("sample_rake"));
		assertEquals("97", result.uncoverage_for("sample_rake"));
	}
	
	
	private Enumeration<String> application_names_for_test() {
		Hashtable<String, Object> applications = new Hashtable<String, Object>();
		applications.put("sample_rake", this);
		applications.put("common", this);

		Enumeration<String> expected = applications.keys();
		return expected;
	}

	private Enumeration<String> module_names_for_test() {
		Hashtable<String, Object> modules = new Hashtable<String, Object>();
		modules.put("Data", this);
		modules.put("sample_rake", this);
		modules.put("sample_rake_sup", this);
		modules.put("sample_rake_app", this);
		modules.put("sample", this);
		modules.put("second", this);
		Enumeration<String> expected = modules.keys();
		return expected;
	}
}
