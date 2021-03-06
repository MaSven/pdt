/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.php.internal.debug.ui.refactoring;

import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptFolder;

/**
 * Breakpoint participant for project rename.
 * 
 * @since 3.2
 */
public class BreakpointRenameSourceModuleParticipant extends BreakpointRenameParticipant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.internal.debug.core.refactoring.
	 * BreakpointRenameParticipant #accepts(org.eclipse.jdt.core.IModelElement)
	 */
	@Override
	protected boolean accepts(IModelElement element) {
		return element instanceof IScriptFolder;
	}

}
