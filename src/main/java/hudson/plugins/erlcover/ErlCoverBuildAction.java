package hudson.plugins.erlcover;

import org.kohsuke.stapler.StaplerProxy;

import hudson.model.AbstractBuild;
import hudson.model.HealthReport;
import hudson.model.HealthReportingAction;

public class ErlCoverBuildAction implements StaplerProxy, HealthReportingAction {

	protected final AbstractBuild<?, ?> owner;
	protected final CoverResult results;

	public ErlCoverBuildAction(AbstractBuild<?, ?> owner, CoverResult results) {
		this.owner = owner;
		this.results = results;
	}

	public HealthReport getBuildHealth() {
		return new HealthReport();
	}

	public String getDisplayName() {
		return "Erlang Coverage report";
	}

	public String getIconFileName() {
		return "graph.gif";
	}

	public String getUrlName() {
		return "erlcover";
	}

	public CoverResult getResults() {
		return results;
	}

	public Object getTarget() {
		// TODO Auto-generated method stub
		return null;
	}
}
