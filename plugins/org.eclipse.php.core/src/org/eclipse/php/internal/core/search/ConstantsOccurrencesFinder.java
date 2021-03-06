/*******************************************************************************
 * Copyright (c) 2009, 2018 IBM Corporation and others.
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
package org.eclipse.php.internal.core.search;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.php.core.ast.nodes.*;
import org.eclipse.php.internal.core.ast.locator.PHPElementConciliator;

/**
 * Constants occurrences finder.
 * 
 * @author shalom
 */
public class ConstantsOccurrencesFinder extends AbstractOccurrencesFinder {
	public static final String ID = "ConstantsOccurrencesFinder"; //$NON-NLS-1$
	private boolean defineFound;
	private boolean isCaseSensitiveConstant = true; // The default is true
	private String constantName;
	private ASTNode nameNode;
	private Map<ASTNode, String> nodeToFullName = new HashMap<>();
	private Map<ASTNode, OccurrenceLocation> nodeToOccurrence = new HashMap<>();

	/**
	 * @param root
	 *                 the AST root
	 * @param node
	 *                 the selected node (must be an {@link Scalar} or an
	 *                 {@link Identifier} instance)
	 * @return returns a message if there is a problem
	 */
	@Override
	public String initialize(Program root, ASTNode node) {
		fASTRoot = root;
		defineFound = false;
		isCaseSensitiveConstant = true;
		if (node.getType() == ASTNode.SCALAR) {
			nameNode = node;
			constantName = ((Scalar) nameNode).getStringValue();
			if (isQuoted(constantName)) {
				constantName = constantName.substring(1, constantName.length() - 1);
			}
			return null;
		} else if (node.getType() == ASTNode.IDENTIFIER) {
			nameNode = node;
			constantName = ((Identifier) node).getName();
			return null;
		}
		fDescription = "OccurrencesFinder_occurrence_description"; //$NON-NLS-1$
		return fDescription;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.php.internal.ui.search.AbstractOccurrencesFinder#
	 * findOccurrences ()
	 */
	@Override
	protected void findOccurrences() {
		fDescription = Messages.format(BASE_DESCRIPTION, constantName);
		fASTRoot.accept(this);
		if (nodeToFullName.containsKey(nameNode)) {
			String fullName = nodeToFullName.get(nameNode);
			for (Iterator<ASTNode> iterator = nodeToFullName.keySet().iterator(); iterator.hasNext();) {
				ASTNode nameNode = iterator.next();
				if (nodeToFullName.get(nameNode).equalsIgnoreCase(fullName)) {
					fResult.add(nodeToOccurrence.get(nameNode));
				}
			}
		}
	}

	@Override
	public boolean visit(Identifier identifier) {
		if (checkEquality(identifier.getName())) {
			if (PHPElementConciliator.isGlobalNamespacedConstant(identifier)) {
				nodeToFullName.put(identifier,
						getFullName((Identifier) identifier.getParent(), fLastUseParts, fCurrentNamespace));
				nodeToOccurrence.put(identifier, new OccurrenceLocation(identifier.getStart(), identifier.getLength(),
						getOccurrenceType(identifier), fDescription));
			} else if (PHPElementConciliator.isGlobalConstDeclaration(identifier)) {
				nodeToFullName.put(identifier, getFullName(identifier, fLastUseParts, fCurrentNamespace));
				nodeToOccurrence.put(identifier, new OccurrenceLocation(identifier.getStart(), identifier.getLength(),
						getOccurrenceType(identifier), fDescription));
			}
		}
		return true;
	}

	/**
	 * Visit the scalars in the program.
	 */
	@Override
	public boolean visit(Scalar scalar) {
		String scalarValue = scalar.getStringValue();
		if (scalar.getScalarType() == Scalar.TYPE_STRING && scalarValue.length() > 0) {
			// disregard strings
			if (!isQuoted(scalarValue) && (scalar.getParent().getType() != ASTNode.QUOTE)) {
				if (checkEquality(scalarValue)) {
					// Usage of the scalar
					nodeToFullName.put(scalar, getFullName(scalarValue, fLastUseParts, fCurrentNamespace));
					nodeToOccurrence.put(scalar, new OccurrenceLocation(scalar.getStart(), scalar.getLength(),
							getOccurrenceType(scalar), fDescription));
					// fResult.add(new OccurrenceLocation(scalar.getStart(),
					// scalar.getLength(), getOccurrenceType(scalar),
					// fDescription));
				}
			} else {
				// The scalar is quoted, so it might be in a 'define' or a
				// 'constant' call.
				if (isQuoted(scalarValue)) {
					scalarValue = scalarValue.substring(1, scalarValue.length() - 1);
				}
				if (checkEquality(scalarValue)) {
					ASTNode parent = scalar.getParent();
					if (parent.getType() == ASTNode.FUNCTION_INVOCATION) {
						// Check if this is the definition function of the
						// scalar (define).
						FunctionInvocation functionInvocation = (FunctionInvocation) parent;
						Expression name = functionInvocation.getFunctionName().getName();
						if (name instanceof Identifier) {
							String functionName = ((Identifier) name).getName();
							if ("define".equalsIgnoreCase(functionName)) {//$NON-NLS-1$
								defineFound = true;
								// check if the 'define' has a case sensitivity
								// definition
								isCaseSensitiveConstant = isCaseSensitiveDefined(functionInvocation.parameters());
								if (!isCaseSensitiveConstant
										|| isCaseSensitiveConstant && constantName.equals(scalarValue)) {
									String writeDescription = Messages.format(BASE_WRITE_DESCRIPTION,
											scalar.getStringValue());
									nodeToFullName.put(scalar,
											getFullName(scalarValue, fLastUseParts, fCurrentNamespace));
									nodeToOccurrence.put(scalar,
											new OccurrenceLocation(scalar.getStart(), scalar.getLength(),
													IOccurrencesFinder.F_WRITE_OCCURRENCE, writeDescription));
									// fResult
									// .add(new OccurrenceLocation(
									// scalar.getStart(),
									// scalar.getLength(),
									// IOccurrencesFinder.F_WRITE_OCCURRENCE,
									// writeDescription));
								}
							} else if ("constant".equalsIgnoreCase(functionName)) { //$NON-NLS-1$
								if (!isCaseSensitiveConstant
										|| isCaseSensitiveConstant && constantName.equals(scalarValue)) {
									nodeToFullName.put(scalar,
											getFullName(scalarValue, fLastUseParts, fCurrentNamespace));
									nodeToOccurrence.put(scalar, new OccurrenceLocation(scalar.getStart(),
											scalar.getLength(), IOccurrencesFinder.F_READ_OCCURRENCE, fDescription));
									// fResult
									// .add(new OccurrenceLocation(
									// scalar.getStart(),
									// scalar.getLength(),
									// IOccurrencesFinder.F_READ_OCCURRENCE,
									// fDescription));
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	/*
	 * Check the function invocation parameters to see if the case-sensitive
	 * parameter is true or false. Define signature: { define ( string $name , mixed
	 * $value [, bool $case_insensitive ] ) }
	 * 
	 * @param parameters The function invocation parameters.
	 * 
	 * @return True, if the 'define' call does not contain a case parameter or
	 * contains it as true; False, in case that the parameters contain a 'false'
	 * case parameter.
	 */
	private boolean isCaseSensitiveDefined(List<Expression> parameters) {
		if (parameters.size() != 3) {
			// default behavior is case sensitive.
			return true;
		}
		Expression expression = parameters.get(2);
		if (expression.getType() == ASTNode.SCALAR) {
			Scalar scalar = (Scalar) expression;
			String value = scalar.getStringValue();
			return "true".equalsIgnoreCase(value); //$NON-NLS-1$
		}
		return false;
	}

	/*
	 * Check that the given name is equal to the searched constant name. The
	 * equality check is done according to these rules: 1. In case that the
	 * case-sensitive flag is false, or in case that the scalar appears before a
	 * 'define' call, we check for case-insensitive equality. 2. In any other case,
	 * the equality check is case sensitive.
	 * 
	 * @param scalarValue The value to compare.
	 * 
	 * @return True, if equals according to the conditions; False, otherwise.
	 */
	private boolean checkEquality(String scalarValue) {
		if (!isCaseSensitiveConstant || !defineFound) {
			return constantName.equalsIgnoreCase(scalarValue);
		}
		return constantName.equals(scalarValue);
	}

	private static boolean isQuoted(String str) {
		if (str == null || str.length() < 3) {
			return false;
		}
		char first = str.charAt(0);
		char last = str.charAt(str.length() - 1);
		return (first == '\'' || first == '\"') && (last == '\'' || last == '\"');
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.php.internal.ui.search.AbstractOccurrencesFinder#
	 * getOccurrenceReadWriteType (org.eclipse.php.internal.core.ast.nodes.ASTNode)
	 */
	@Override
	protected int getOccurrenceType(ASTNode node) {
		// Default return is F_READ_OCCURRENCE, although the implementation of
		// the Scalar visit might also use F_WRITE_OCCURRENCE
		return IOccurrencesFinder.F_READ_OCCURRENCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.php.internal.ui.search.IOccurrencesFinder#getElementName()
	 */
	@Override
	public String getElementName() {
		return constantName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.php.internal.ui.search.IOccurrencesFinder#getID()
	 */
	@Override
	public String getID() {
		return ID;
	}
}
