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
package org.eclipse.php.core.compiler.ast.nodes;

import java.util.Collection;
import java.util.List;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.utils.CorePrinter;
import org.eclipse.php.internal.core.compiler.ast.visitor.ASTPrintVisitor;

/**
 * Represents a global statement
 * 
 * <pre>
 * e.g.
 * 
 * <pre>
 * global $a global $a, $b global ${foo()->bar()}, global $$a
 */
public class GlobalStatement extends Statement {

	private final List<? extends Expression> variables;

	public GlobalStatement(int start, int end, List<? extends Expression> variables) {
		super(start, end);

		assert variables != null;
		this.variables = variables;
	}

	@Override
	public void traverse(ASTVisitor visitor) throws Exception {
		final boolean visit = visitor.visit(this);
		if (visit) {
			for (Expression variable : variables) {
				variable.traverse(visitor);
			}
		}
		visitor.endvisit(this);
	}

	@Override
	public int getKind() {
		return ASTNodeKinds.GLOBAL_STATEMENT;
	}

	public Collection<? extends Expression> getVariables() {
		return variables;
	}

	/**
	 * We don't print anything - we use {@link ASTPrintVisitor} instead
	 */
	@Override
	public final void printNode(CorePrinter output) {
	}

	@Override
	public String toString() {
		return ASTPrintVisitor.toXMLString(this);
	}
}
