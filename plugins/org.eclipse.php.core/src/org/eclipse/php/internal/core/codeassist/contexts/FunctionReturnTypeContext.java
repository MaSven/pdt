/*******************************************************************************
 * Copyright (c) 2015, 2017 Dawid Pakuła and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Dawid Pakuła - initial API and implementation
 *******************************************************************************/
package org.eclipse.php.internal.core.codeassist.contexts;

import org.eclipse.dltk.annotations.NonNull;
import org.eclipse.dltk.core.CompletionRequestor;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.php.core.PHPVersion;
import org.eclipse.php.internal.core.util.text.PHPTextSequenceUtilities;
import org.eclipse.php.internal.core.util.text.TextSequence;

/**
 * <pre>
 *  1. function foo():| {}
 *  2. function foo():s| {}
 *  3. function foo():?| {}
 *  4. function foo():?s| {}
 * </pre>
 * 
 * @author Dawid Pakuła
 */
public class FunctionReturnTypeContext extends FunctionDeclarationContext {

	@Override
	public boolean isValid(@NonNull ISourceModule sourceModule, int offset, CompletionRequestor requestor) {
		if (!super.isValid(sourceModule, offset, requestor)) {
			return false;
		}
		if (getCompanion().getPHPVersion().isLessThan(PHPVersion.PHP7_0)) {
			return false;
		}

		TextSequence statementText = getStatementText();
		int curr = statementText.length();
		curr = PHPTextSequenceUtilities.readBackwardSpaces(statementText, curr);
		if (curr < 1) {
			return false;
		}
		curr = PHPTextSequenceUtilities.readIdentifierStartIndex(statementText, curr, false);
		if (curr < 1) {
			return false;
		}

		curr = PHPTextSequenceUtilities.readBackwardSpaces(statementText, curr);
		if (curr < 1) {
			return false;
		}
		char prevChar = statementText.charAt(curr - 1);
		if (prevChar != ':' && prevChar != '?') {
			return false;
		}
		if (prevChar == '?') {
			if (getCompanion().getPHPVersion().isLessThan(PHPVersion.PHP7_1)) {
				return false;
			}
			curr = PHPTextSequenceUtilities.readBackwardSpaces(statementText, curr - 1);
			if (curr < 1 || statementText.charAt(curr - 1) != ':') {
				return false;
			}
		}

		curr = PHPTextSequenceUtilities.readBackwardSpaces(statementText, curr - 1);
		if (curr > 0 && statementText.charAt(curr - 1) == ')') {
			return true;
		}

		return false;
	}
}
