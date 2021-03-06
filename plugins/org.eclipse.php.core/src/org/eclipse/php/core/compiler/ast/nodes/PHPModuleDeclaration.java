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
package org.eclipse.php.core.compiler.ast.nodes;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.eclipse.dltk.annotations.NonNull;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.utils.CorePrinter;
import org.eclipse.php.internal.core.compiler.ast.visitor.ASTPrintVisitor;

public class PHPModuleDeclaration extends ModuleDeclaration {

	private List<ASTError> errors;
	private boolean hasErrors;
	private List<VarComment> varComments;
	private List<PHPDocBlock> phpDocBlocks = new LinkedList<>();
	private List<Comment> commentList = new LinkedList<>();

	public PHPModuleDeclaration(int start, int end, @NonNull List<ASTNode> statements, @NonNull List<ASTError> errors,
			@NonNull List<VarComment> varComments) {
		super(end - start, true);
		setStatements(statements);
		setStart(start);
		setEnd(end);
		this.errors = errors;
		this.varComments = varComments;
	}

	/**
	 * This method goes over the AST and builds a list of types and methods declared
	 * in this file
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void doRebuild() {
		List<?> statements = getStatements();
		if (statements != null) {
			Iterator<?> i = statements.iterator();
			while (i.hasNext()) {
				final ASTNode node = (ASTNode) i.next();
				try {
					node.traverse(new ASTVisitor() {
						private Stack<ASTNode> parentStack = new Stack<>();

						@Override
						public boolean visit(MethodDeclaration s) throws Exception {
							if (s != node
									&& (parentStack.isEmpty() || !(parentStack.peek() instanceof TypeDeclaration))) {
								getFunctionList().add(s);
							}
							return super.visit(s);
						}

						@Override
						public boolean visit(TypeDeclaration s) throws Exception {
							if (s instanceof NamespaceDeclaration && ((NamespaceDeclaration) s).isGlobal()) {
								return super.visit(s);
							}
							parentStack.add(s);
							getTypeList().add(s);
							return super.visit(s);
						}

						@Override
						public boolean endvisit(TypeDeclaration s) throws Exception {
							if (s instanceof NamespaceDeclaration && ((NamespaceDeclaration) s).isGlobal()) {
								return super.endvisit(s);
							}
							parentStack.pop();
							return super.endvisit(s);
						}
					});
				} catch (Exception e) {
					if (DLTKCore.DEBUG) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void addStatement(Statement statement) {
		super.addStatement(statement);
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

	/**
	 * due to the nature of the parser and the error recovery method not all errors
	 * can be added to the AST as statements, the error list is made for those
	 * errors.
	 */
	public List<ASTError> getErrors() {
		return errors;
	}

	public List<ASTError> getAllErrors() {
		ErrorSearcher searcher = new ErrorSearcher();
		try {
			traverse(searcher);
		} catch (Exception e) {
		}
		List<ASTError> errorsList = searcher.getErrors();
		errorsList.addAll(getErrors());
		return errorsList;
	}

	public boolean hasErrors() {
		return hasErrors || !errors.isEmpty();
	}

	/**
	 * This method fires a flag that says that this file has compilation errors
	 */
	public void setHasErrors(boolean hasErrors) {
		this.hasErrors = hasErrors;
	}

	public List<VarComment> getVarComments() {
		return varComments;
	}

	public List<PHPDocBlock> getPHPDocBlocks() {
		return phpDocBlocks;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	private class ErrorSearcher extends ASTVisitor {
		private List<ASTError> errors = new LinkedList<>();

		public boolean visit(ASTError error) throws Exception {
			errors.add(error);
			return false;
		}

		@Override
		public boolean visit(Statement s) throws Exception {
			if (s.getKind() == ASTNodeKinds.AST_ERROR) {
				return visit((ASTError) s);
			}
			return super.visit(s);
		}

		public List<ASTError> getErrors() {
			return errors;
		}
	}
}