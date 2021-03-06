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
 * Represents a declare statement
 * 
 * <pre>
 * e.g.
 * 
 * <pre>
 * declare(ticks=1) { } declare(ticks=2) { for ($x = 1; $x < 50; ++$x) { } }
 */
public class DeclareStatement extends Statement {

	private final List<String> directiveNames;
	private final List<? extends Expression> directiveValues;
	private final Statement action;

	public DeclareStatement(int start, int end, List<String> directiveNames, List<? extends Expression> directiveValues,
			Statement action) {
		super(start, end);

		assert directiveNames != null && directiveValues != null && directiveNames.size() == directiveValues.size();
		this.directiveNames = directiveNames;
		this.directiveValues = directiveValues;
		this.action = action;
	}

	@Override
	public void traverse(ASTVisitor visitor) throws Exception {
		final boolean visit = visitor.visit(this);
		if (visit) {
			for (Expression directiveValue : directiveValues) {
				directiveValue.traverse(visitor);
			}
			action.traverse(visitor);
		}
		visitor.endvisit(this);
	}

	@Override
	public int getKind() {
		return ASTNodeKinds.DECLARE_STATEMENT;
	}

	public Statement getAction() {
		return action;
	}

	public Collection<String> getDirectiveNames() {
		return directiveNames;
	}

	public Collection<? extends Expression> getDirectiveValues() {
		return directiveValues;
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
