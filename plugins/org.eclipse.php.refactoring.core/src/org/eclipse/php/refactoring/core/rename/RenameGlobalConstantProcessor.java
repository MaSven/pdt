/*******************************************************************************
 * Copyright (c) 2006, 2015 Zend Technologies and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Zend Technologies - initial API and implementation
 *******************************************************************************/
package org.eclipse.php.refactoring.core.rename;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.php.core.ast.nodes.*;
import org.eclipse.php.internal.core.ast.locator.PHPElementConciliator;
import org.eclipse.php.refactoring.core.PHPRefactoringCoreMessages;
import org.eclipse.php.refactoring.core.rename.logic.RenameGlobalConstant;

/**
 * Rename a constant processor
 * 
 * @author Roy, 2007
 */
public class RenameGlobalConstantProcessor extends AbstractRenameProcessor<IFile> implements ITextUpdating {

	private static final String RENAME_IS_PROCESSING = PHPRefactoringCoreMessages.getString("RenameDefinedProcessor.0"); //$NON-NLS-1$
	private static final String CREATING_MODIFICATIONS_LABEL = PHPRefactoringCoreMessages
			.getString("RenameDefinedProcessor.1"); //$NON-NLS-1$
	private static final String CONSTANT_IS_USED = PHPRefactoringCoreMessages.getString("RenameDefinedProcessor.2"); //$NON-NLS-1$
	private static final String ID_RENAME_CONSTANT = "php.refactoring.ui.rename.constant"; //$NON-NLS-1$
	protected static final String ATTRIBUTE_TEXTUAL_MATCHES = "textual"; //$NON-NLS-1$
	public static final String RENAME_CONSTANT_PROCESSOR_NAME = PHPRefactoringCoreMessages
			.getString("RenameDefinedProcessor.3"); //$NON-NLS-1$

	/**
	 * The original identifier node we want to rename
	 */
	private final Scalar scalar;
	private final String scalarName;

	/**
	 * holds wether or not we want to change also the inlined text
	 */
	private boolean isUpdateTextualMatches;

	public RenameGlobalConstantProcessor(IFile operatedFile, ASTNode locateNode) {
		super(operatedFile);

		this.scalar = getScalar(locateNode);

		final String stringValue = scalar.getStringValue();
		final char charAt = stringValue.length() > 0 ? stringValue.charAt(0) : ' ';
		this.scalarName = charAt != '"' && charAt != '\'' ? stringValue
				: stringValue.substring(1, stringValue.length() - 1);
	}

	private Scalar getScalar(ASTNode locateNode) {
		if (locateNode.getType() != ASTNode.SCALAR) {
			if (locateNode instanceof Identifier && "define".equals(((Identifier) locateNode).getName())) { //$NON-NLS-1$
				FunctionInvocation inv = (FunctionInvocation) locateNode.getParent().getParent();
				List<Expression> parameters = inv.parameters();
				if (parameters != null && parameters.size() > 0) {
					return (Scalar) parameters.get(0);
				}
			} else {
				return null;
			}
		} else {
			return (Scalar) locateNode;
		}
		return null;
	}

	/**
	 * Derive the change
	 */
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		CompositeChange rootChange = new CompositeChange(
				PHPRefactoringCoreMessages.getString("RenameDefinedProcessor.4")); //$NON-NLS-1$
		rootChange.markAsSynthetic();
		try {
			pm.beginTask(RenameGlobalConstantProcessor.RENAME_IS_PROCESSING, participantFiles.size());
			pm.setTaskName(RenameGlobalConstantProcessor.CREATING_MODIFICATIONS_LABEL);

			if (pm.isCanceled()) {
				throw new OperationCanceledException();
			}

			// get target parameters
			final String newElementName = getNewElementName();

			// go over the files and check for global variable usage
			for (Entry<IFile, Program> entry : participantFiles.entrySet()) {
				final IFile file = entry.getKey();
				final Program program = entry.getValue();
				final RenameGlobalConstant rename = new RenameGlobalConstant(file, scalarName, newElementName,
						getUpdateTextualMatches());

				// aggregate the changes identifiers
				program.accept(rename);

				if (pm.isCanceled()) {
					throw new OperationCanceledException();
				}

				pm.worked(1);

				if (rename.hasChanges()) {
					// create the change
					TextFileChange change = acquireChange(file, program);
					rename.updateChange(change);
					rootChange.add(change);
				}
			}
			return rootChange;

		} finally {
			pm.done();
		}
	}

	@Override
	public Object[] getElements() {
		return new Object[] { scalar };
	}

	@Override
	public String getIdentifier() {
		return ID_RENAME_CONSTANT;
	}

	@Override
	public String getProcessorName() {
		return RENAME_CONSTANT_PROCESSOR_NAME;
	}

	@Override
	public Object getNewElement() {
		return getNewElementName();
	}

	@Override
	public String getCurrentElementName() {
		return scalarName;
	}

	@Override
	public boolean canEnableTextUpdating() {
		return true;
	}

	@Override
	public String getCurrentElementQualifier() {
		return scalarName;
	}

	@Override
	public boolean getUpdateTextualMatches() {
		return isUpdateTextualMatches;
	}

	@Override
	public void setUpdateTextualMatches(boolean update) {
		isUpdateTextualMatches = update;
	}

	@Override
	public RefactoringStatus getRefactoringStatus(IFile key, Program program) {
		if (PHPElementConciliator.constantAlreadyExists(program, getNewElementName())) {
			final String message = MessageFormat.format(RenameGlobalConstantProcessor.CONSTANT_IS_USED,
					new Object[] { key.getName() });
			return RefactoringStatus.createWarningStatus(message);
		}
		return null;
	}
}
