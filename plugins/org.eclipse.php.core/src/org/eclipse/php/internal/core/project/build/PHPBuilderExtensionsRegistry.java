/*******************************************************************************
 * Copyright (c) 2006 Zend Corporation and IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Zend and IBM - Initial implementation
 *******************************************************************************/
package org.eclipse.php.internal.core.project.build;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.php.internal.core.PHPCorePlugin;
import org.eclipse.php.core.project.build.IPHPBuilderExtension;

/**
 * Registry for accessing PHP builder extensions contributed via the extension point: phpBuidlerExtensions
 * @author michael
 */
public class PHPBuilderExtensionsRegistry {

	private static final String DEFAULT_EXTENSION_ID = "org.eclipse.php.core.project.build.DefaultPHPBuilderExtension"; //$NON-NLS-1$
	private static final String EXTENSION_NAME = "phpBuilderExtensions"; //$NON-NLS-1$
	private static final String BUILDER_ELEMENT = "builder"; //$NON-NLS-1$
	private static final String ID_ATTRIBUTE = "id"; //$NON-NLS-1$
	private static final String CLASS_ATTRIBUTE = "class"; //$NON-NLS-1$

	/** PHP builder extensions map (ID to extension instance) */
	private Map<String, Object> extensions;

	/** Instance of {@link PHPBuilderExtensionsRegistry} singleton */
	private static PHPBuilderExtensionsRegistry instance;

	/**
	 * Constructor.
	 * Looks for all contributed PHP builder extensions and initializes them.
	 */
	private PHPBuilderExtensionsRegistry() {

		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor(PHPCorePlugin.ID, EXTENSION_NAME);

		Object defaultExtension = null;

		for (IConfigurationElement element : elements) {
			if (BUILDER_ELEMENT.equals(element.getName()) && DEFAULT_EXTENSION_ID.equals(element.getAttribute(ID_ATTRIBUTE))) {
				try {
					defaultExtension = element.createExecutableExtension(CLASS_ATTRIBUTE);
				} catch (CoreException e) {
					PHPCorePlugin.log(e);
				}
				break;
			}
		}

		if (defaultExtension == null) {
			PHPCorePlugin.log(new IllegalStateException("no default project builder extension")); //$NON-NLS-1$
			return;
		}

		extensions = new LinkedHashMap();
		extensions.put(DEFAULT_EXTENSION_ID, defaultExtension);

		for (int i = 0; i < elements.length; ++i) {
			IConfigurationElement element = elements[i];
			if (BUILDER_ELEMENT.equals(element.getName()) && !DEFAULT_EXTENSION_ID.equals(element.getAttribute(ID_ATTRIBUTE))) {
				try {
					extensions.put(element.getAttribute(ID_ATTRIBUTE), element.createExecutableExtension(CLASS_ATTRIBUTE));
				} catch (CoreException e) {
					PHPCorePlugin.log(e);
				}
			}
		}
	}

	public synchronized static PHPBuilderExtensionsRegistry getInstance() {
		if (instance == null) {
			instance = new PHPBuilderExtensionsRegistry();
		}
		return instance;
	}

	/**
	 * Returns array of all PHP builder extensions contributed via the extension point.
	 * Note: {@link IPHPBuilderExtension#setContainingBuilder(PHPIncrementalProjectBuilder)} must be called before using these extensions.
	 * @return PHP builder extensions
	 */
	public IPHPBuilderExtension[] getExtensions() {
		Collection extensions = instance.extensions.values();
		return (IPHPBuilderExtension[]) extensions.toArray(new IPHPBuilderExtension[extensions.size()]);
	}

	/**
	 * Returns PHP builder extension by its ID.
	 * @param id ID of the extension
	 * @return {@link IPHPBuilderExtension} if found in the map, otherwise <code>null</code>.
	 */
	public IPHPBuilderExtension getExtension(String id) {
		return (IPHPBuilderExtension) instance.extensions.get(id);
	}
}