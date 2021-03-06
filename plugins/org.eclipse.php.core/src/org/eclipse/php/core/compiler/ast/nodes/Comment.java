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

import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.utils.CorePrinter;
import org.eclipse.php.internal.core.compiler.ast.visitor.ASTPrintVisitor;

/**
 * Represents a PHP comment
 * 
 * <pre>
 * e.g.
 * 
 * <pre>
 * // this is a single line comment # this is a single line comment /** this is
 * php doc block (end php docblock here)
 */
public class Comment extends ASTNode {

	public final static int TYPE_SINGLE_LINE = 0;
	public final static int TYPE_MULTILINE = 1;
	public final static int TYPE_PHPDOC = 2;

	private final int commentType;
	private List<Scalar> taskTags;

	public Comment(int start, int end, int type) {
		super(start, end);

		this.commentType = type;
	}

	@Override
	public void traverse(ASTVisitor visitor) throws Exception {
		visitor.visit(this);
		visitor.endvisit(this);
	}

	public static String getCommentType(int type) {
		switch (type) {
		case TYPE_SINGLE_LINE:
			return "singleLine"; //$NON-NLS-1$
		case TYPE_MULTILINE:
			return "multiLine"; //$NON-NLS-1$
		case TYPE_PHPDOC:
			return "phpDoc"; //$NON-NLS-1$
		default:
			throw new IllegalArgumentException();
		}
	}

	public int getCommentType() {
		return commentType;
	}

	/**
	 * @return list of todo task tags set by TaskTagBuildParticipantFactory or null
	 */
	public List<Scalar> getTaskTags() {
		return taskTags;
	}

	/**
	 * Todo Task tags will be set (if any) by TaskTagBuildParticipantFactory
	 * 
	 * @param taskTags
	 */
	public void setTaskTags(List<Scalar> taskTags) {
		this.taskTags = taskTags;
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
