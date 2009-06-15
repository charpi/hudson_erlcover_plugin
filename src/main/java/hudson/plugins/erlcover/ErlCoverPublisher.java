/** Copyright(c) 2009 Nicolas Charpentier
    All rights reserved.
    See file $TOP_DIR/COPYING.
**/

package hudson.plugins.erlcover;

import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.Descriptor;
import hudson.model.Result;
import hudson.tasks.Publisher;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import org.apache.tools.ant.types.FileSet;
import org.kohsuke.stapler.StaplerRequest;

public class ErlCoverPublisher extends Publisher implements Serializable {
	private final String includes;

	@SuppressWarnings("deprecation")
	public ErlCoverPublisher(final String includes) {
		this.includes = includes;
	}

	/** {@inheritDoc} */
	@Override
	public Action getProjectAction(final AbstractProject<?, ?> project) {
		return new CoverProjectAction(project);
	}

	public boolean perform(Build<?, ?> build, Launcher launcher,
			final BuildListener listener) throws IOException,
			InterruptedException {
		listener.getLogger().println(
				"Collecting Erlang cover reports:" + includes);

		String rootDirectory = build.getProject().getRootDir()
				.getAbsolutePath()
				+ "/workspace";
		FileSet fs = new FileSet();
		org.apache.tools.ant.Project p = new org.apache.tools.ant.Project();
		fs.setProject(p);
		fs.setDir(new File(rootDirectory));
		fs.setIncludes(includes);

		String[] includedFiles = fs.getDirectoryScanner(p).getIncludedFiles();

		if (includedFiles.length == 0) {
			listener
					.getLogger()
					.println(
							"No erlang cover report files were found. Configuration error?");
			build.setResult(Result.FAILURE);
			return false;

		}

		listener.getLogger().println("Number " + includedFiles.length);
		CoverResult results = new CoverResult(rootDirectory, includedFiles);

		ErlCoverBuildAction action = new ErlCoverBuildAction(build, results);
		build.getActions().add(action);

		build.setResult(Result.SUCCESS);
		return true;
	}

	public Descriptor<Publisher> getDescriptor() {
		return DescriptorImpl.DESCRIPTOR;
	}

	/* package */static class DescriptorImpl extends Descriptor<Publisher> {
		public static final Descriptor<Publisher> DESCRIPTOR = new DescriptorImpl();

		public DescriptorImpl() {
			super(ErlCoverPublisher.class);
		}

		public String getDisplayName() {
			return "Publish Erlang cover result report";
		}

		public String getHelpFile() {
			return "/plugin/erl_cover/help.html";
		}

		public Publisher newInstance(StaplerRequest req) {
			return new ErlCoverPublisher(req.getParameter("erlcover_includes"));
		}
	}

	private static final long serialVersionUID = 1L;
}
