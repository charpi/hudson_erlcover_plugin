package hudson.plugins.erlcover;

import hudson.model.AbstractProject;
import hudson.model.ProminentProjectAction;
import hudson.model.Actionable;

public class CoverProjectAction extends Actionable implements
		ProminentProjectAction {

	protected final AbstractProject<?, ?> project;

	public CoverProjectAction(AbstractProject<?, ?> project) {
		this.project = project;
	}

	public String getDisplayName() {
		return "Erlang Coverage Report";
	}

	public String getUrlName() {
		return "erlcover";
	}

	public ErlCoverBuildAction getLastResult() {
		return null;
	}

	public Integer getLastResultBuild() {
		return null;
	}

	public String getSearchUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getIconFileName() {
		// TODO Auto-generated method stub
		return null;
	}
}
