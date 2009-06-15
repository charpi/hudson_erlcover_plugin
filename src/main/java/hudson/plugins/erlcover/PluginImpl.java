/** Copyright(c) 2009 Nicolas Charpentier
    All rights reserved.
    See file $TOP_DIR/COPYING.
**/

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
