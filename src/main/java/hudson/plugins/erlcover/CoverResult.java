/** Copyright(c) 2009 Nicolas Charpentier
    All rights reserved.
    See file $TOP_DIR/COPYING.
**/

package hudson.plugins.erlcover;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class CoverResult {

	public class UnknownApplicationException extends Throwable {
		private static final long serialVersionUID = 5255002999611061065L;
	}

	class Summary {
		int called;
		int uncalled;

		public String toString() {
			return (called + ":" + uncalled + ":" + (called + uncalled));
		}
	}

	protected Hashtable<String, Hashtable<String, Summary>> applications = new Hashtable<String, Hashtable<String, Summary>>();

	public CoverResult(String rootDirectory, String[] includedFiles) {
		for (String file : includedFiles) {
			parse(rootDirectory, file);
		}
	}

	public Enumeration<String> applicationNames() {
		return applications.keys();
	}

	public Enumeration<String> moduleNames(String application)
			throws UnknownApplicationException {
		if (!applications.containsKey(application))
			throw new UnknownApplicationException();
		return applications.get(application).keys();
	}

	public String total_for(String application) {
		Hashtable<String, Summary> modules = applications.get(application);
		int total = 0;
		for (Summary summary : modules.values()) {
			total += summary.called + summary.uncalled;
		}
		return String.valueOf(total);
	}

	public String called_for(String application) {
		Hashtable<String, Summary> modules = applications.get(application);
		int total = 0;
		for (Summary summary : modules.values()) {
			total += summary.called;
		}
		return String.valueOf(total);
	}

	public String uncoverage_for(String name) {
		return String.valueOf(100 - Integer.parseInt(coverage_for(name)));
	}

	public String coverage_for(String application) {
		Hashtable<String, Summary> modules = applications.get(application);
		int called = 0;
		int uncalled = 0;
		for (Summary summary : modules.values()) {
			called += summary.called;
			uncalled += summary.uncalled;
		}
		return String.valueOf(called * 100 / (called + uncalled));
	}

	public String total_for(String application, String module) {
		Summary summary = applications.get(application).get(module);
		return String.valueOf(summary.called + summary.uncalled);
	}

	public String called_for(String application, String module) {
		Summary summary = applications.get(application).get(module);
		return String.valueOf(summary.called);
	}

	public String uncoverage_for(String application, String module) {

		return String.valueOf(100 - Integer.parseInt(coverage_for(application,
				module)));
	}

	public String coverage_for(String application, String module) {
		Summary summary = applications.get(application).get(module);
		return String.valueOf(summary.called * 100
				/ (summary.called + summary.uncalled));
	}

	private void parse(String rootDirectory, String string) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler() {
				String moduleName;
				String applicationName;

				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
						throws SAXException {
					if (qName.equalsIgnoreCase("module")) {
						moduleName = attributes.getValue("name");
						applicationName = attributes.getValue("application");

						if (!applications.containsKey(applicationName)) {
							Hashtable<String, Summary> modules = new Hashtable<String, Summary>();
							applications.put(applicationName, modules);
						}

						Summary moduleCoverage = new Summary();
						applications.get(applicationName).put(moduleName,
								moduleCoverage);
					} else if (qName.equalsIgnoreCase("lines")) {
						Summary moduleCoverage = applications.get(
								applicationName).get(moduleName);
						moduleCoverage.called = Integer.parseInt(attributes
								.getValue("called"));
						moduleCoverage.uncalled = Integer.parseInt(attributes
								.getValue("uncalled"));
					}
				}
			};
			saxParser.parse(rootDirectory + "/" + string, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
