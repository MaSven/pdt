/*******************************************************************************
 * Copyright (c) 2012, 2016, 2017 PDT Extension Group and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     PDT Extension Group - initial API and implementation
 *     Kaloyan Raev - [501269] externalize strings
 *******************************************************************************/
package org.eclipse.php.composer.core;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.php.composer.core.model.ModelAccess;
import org.eclipse.php.composer.core.resources.IComposerProject;
import org.eclipse.php.composer.internal.core.resources.ComposerProject;
import org.osgi.framework.BundleContext;

public class ComposerPlugin extends Plugin {

	private static ComposerPlugin plugin;

	public static final String ID = "org.eclipse.php.composer.core"; //$NON-NLS-1$

	private static final String DEBUG = "org.eclipse.php.composer.core/debug"; //$NON-NLS-1$

	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);

		plugin = this;

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IResourceChangeListener listener = new IResourceChangeListener() {
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				if (event.getType() == IResourceChangeEvent.PRE_DELETE && event.getResource() instanceof IProject) {
					ModelAccess.getInstance().getPackageManager().removeProject((IProject) event.getResource());
				}
			}
		};
		workspace.addResourceChangeListener(listener);

	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {

		super.stop(bundleContext);
		plugin = null;
	}

	public static ComposerPlugin getDefault() {
		return plugin;
	}

	public static void debug(String message) {
		if (plugin == null) {
			// tests
			return;
		}

		String debugOption = Platform.getDebugOption(DEBUG);

		if (plugin.isDebugging() && Boolean.parseBoolean(debugOption)) {
			plugin.getLog().log(new Status(Status.INFO, ID, message));
		}
	}

	public static void logException(Exception e) {
		IStatus status = new Status(Status.ERROR, ComposerPlugin.ID, e.getMessage(), e);
		plugin.getLog().log(status);
	}

	public IEclipsePreferences getProjectPreferences(IProject project) {
		ProjectScope ps = new ProjectScope(project);
		return ps.getNode(ID);
	}

	public boolean isBuildpathContainerEnabled() {
		return Platform.getPreferencesService().getBoolean(ComposerPlugin.ID,
				ComposerPluginConstants.PREF_BUILDPATH_ENABLE, true, null);
	}

	public IComposerProject getComposerProject(IScriptProject project) {
		if (project == null) {
			return null;
		}
		return new ComposerProject(project);
	}

	public IComposerProject getComposerProject(IProject project) {
		if (project == null) {
			return null;
		}
		return new ComposerProject(project);
	}
}
