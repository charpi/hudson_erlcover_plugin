package hudson.plugins.erlcover;

import hudson.Plugin;
import hudson.tasks.BuildStep;

public class PluginImpl extends Plugin {
	@SuppressWarnings("deprecation")
	public void start() throws Exception {
		BuildStep.PUBLISHERS
				.addRecorder(ErlCoverPublisher.DescriptorImpl.DESCRIPTOR);
	}
}
