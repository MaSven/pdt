/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Zend Technologies
 *******************************************************************************/
package org.eclipse.php.internal.core.codeassist.contexts;

import org.eclipse.dltk.annotations.NonNull;
import org.eclipse.dltk.core.CompletionRequestor;
import org.eclipse.dltk.core.ISourceModule;

/**
 * This context represents the state when staying after 'return' tag in PHPDoc
 * block <br/>
 * Examples:
 * 
 * <pre>
 *   1. /**
 *       * @return |
 *   2. /**
 *       * @return Ty|
 * </pre>
 * 
 * @author michael
 */
public class PHPDocReturnTagContext extends PHPDocTagContext {

	@Override
	public boolean isValid(@NonNull ISourceModule sourceModule, int offset, CompletionRequestor requestor) {
		if (!super.isValid(sourceModule, offset, requestor)) {
			return false;
		}
		return "return".equalsIgnoreCase(getTagName()); //$NON-NLS-1$
	}
}
