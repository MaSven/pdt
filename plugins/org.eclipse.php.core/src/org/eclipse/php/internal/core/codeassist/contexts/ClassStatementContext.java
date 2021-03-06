/*******************************************************************************
 * Copyright (c) 2009, 2016 IBM Corporation and others.
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
import org.eclipse.dltk.core.*;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.php.core.compiler.PHPFlags;
import org.eclipse.php.internal.core.PHPCorePlugin;
import org.eclipse.php.internal.core.format.PHPHeuristicScanner;

/**
 * This context represents state when staying in a class statements. <br/>
 * Examples:
 * 
 * <pre>
 *  1. class A { | }
 *  2. class A { p| }
 *  etc...
 * </pre>
 * 
 * @author michael
 */
public final class ClassStatementContext extends AbstractGlobalStatementContext {
	private boolean isAssignment = false;

	@Override
	public boolean isValid(@NonNull ISourceModule sourceModule, int offset, CompletionRequestor requestor) {
		if (!super.isValid(sourceModule, offset, requestor)) {
			return false;
		}

		// check whether enclosing element is class
		try {
			IModelElement enclosingElement = getEnclosingElement();
			while (enclosingElement instanceof IField) {
				enclosingElement = enclosingElement.getParent();
			}
			if (enclosingElement instanceof IType && !PHPFlags.isNamespace(((IType) enclosingElement).getFlags())) {
				if (isBeforeName(offset, (IType) enclosingElement)) {
					return false;
				}
				if (offset > 0) {
					// https://bugs.eclipse.org/bugs/show_bug.cgi?id=495022
					offset--;
				}
				PHPHeuristicScanner scanner = PHPHeuristicScanner.createHeuristicScanner(getCompanion().getDocument(),
						offset, true);
				isAssignment = scanner.scanBackward(offset, ((IType) enclosingElement).getSourceRange().getOffset(),
						'=') > -1;
				return true;
			}
			if (enclosingElement instanceof IMethod) {
				if (isBeforeName(offset, (IMethod) enclosingElement)) {
					return true;
				}
			}
		} catch (ModelException e) {
			PHPCorePlugin.log(e);
		} catch (BadLocationException e) {
			PHPCorePlugin.log(e);
		}

		return false;
	}

	public boolean isAssignment() {
		return isAssignment;
	}
}
