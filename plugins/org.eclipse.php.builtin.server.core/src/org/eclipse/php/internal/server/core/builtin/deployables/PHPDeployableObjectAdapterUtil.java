/*******************************************************************************
 * Copyright (c) 2017 Rogue Wave Software, Inc. and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Kaloyan Raev - initial API and implementation
 *******************************************************************************/
package org.eclipse.php.internal.server.core.builtin.deployables;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.php.core.PHPToolkitUtil;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IModuleArtifact;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.util.WebResource;

public class PHPDeployableObjectAdapterUtil {

	public static IModuleArtifact getModuleObject(Object obj) throws CoreException {
		IResource resource = null;
		if (obj instanceof IResource) {
			resource = (IResource) obj;
		} else if (obj instanceof IAdaptable) {
			resource = ((IAdaptable) obj).getAdapter(IResource.class);
		}

		if (resource == null) {
			return null;
		}

		IProject project = resource.getProject();
		if (project != null && !hasInterestedComponents(project)) {
			return null;
		}

		// return Web resource type
		return new WebResource(getModule(project), resource.getProjectRelativePath());
	}

	protected static IModule getModule(IProject project) throws CoreException {
		if (hasInterestedComponents(project)) {
			return ServerUtil.getModule(project);
		}
		return null;
	}

	protected static boolean hasInterestedComponents(IProject project) throws CoreException {
		return PHPToolkitUtil.isPHPProject(project);
	}

}
