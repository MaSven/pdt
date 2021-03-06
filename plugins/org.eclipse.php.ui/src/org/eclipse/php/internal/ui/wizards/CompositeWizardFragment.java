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
package org.eclipse.php.internal.ui.wizards;

import org.eclipse.swt.widgets.Composite;

/**
 * A WizardFragment that always hold a Composite.
 * 
 */
public abstract class CompositeWizardFragment extends WizardFragment {

	/**
	 * Constructs a new CompositeWizardFragment.
	 * 
	 */
	public CompositeWizardFragment() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.wst.server.ui.wizard.WizardFragment#hasComposite()
	 */
	@Override
	public boolean hasComposite() {
		return true;
	}

	/**
	 * Returns the composite that was last created by this fragment.
	 * 
	 * @return A Composite (Null if the createComposite was not called yet)
	 */
	public abstract Composite getComposite();
}