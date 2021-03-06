/*******************************************************************************
 * Copyright (c) 2018 IBM Corporation and others.
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
package org.eclipse.php.internal.core.codeassist.strategies;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.php.core.codeassist.ICompletionContext;
import org.eclipse.php.core.codeassist.ICompletionReporter;
import org.eclipse.php.core.codeassist.IElementFilter;
import org.eclipse.php.internal.core.codeassist.contexts.PHPDocTagContext;

/**
 * This strategy completes variable names in 'var' PHPDoc tag
 */
public class PHPDocVarVariableStrategy extends ClassPropertiesStrategy {

	public PHPDocVarVariableStrategy(ICompletionContext context, IElementFilter elementFilter) {
		super(context, elementFilter);
	}

	public PHPDocVarVariableStrategy(ICompletionContext context) {
		super(context);
	}

	@Override
	public void apply(ICompletionReporter reporter) throws BadLocationException {
		ICompletionContext context = getContext();
		if (!(context instanceof PHPDocTagContext)) {
			return;
		}
		PHPDocTagContext tagContext = (PHPDocTagContext) context;

		String prefix = tagContext.getPrefix();
		if (prefix.startsWith("$")) { //$NON-NLS-1$
			super.apply(reporter);
		} else {
			final PHPDocReturnTypeStrategy returnStrategy = new PHPDocReturnTypeStrategy(context);
			returnStrategy.apply(reporter);
		}
	}

}
