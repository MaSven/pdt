/*******************************************************************************
 * Copyright (c) 2006 Zend Corporation and IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Zend and IBM - Initial implementation
 *******************************************************************************/
package org.eclipse.php.internal.ui.explorer;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.php.internal.ui.SuperClassLabelProvider;
import org.eclipse.php.internal.ui.util.AppearanceAwareLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.WorkingSet;

/**
 * Provides the labels for the PHP Explorer.
 * <p>
 * It provides labels for the folders in hierarchical layout and in all
 * other cases delegates it to its super class.
 * </p>
 * @since 2.1
 */
public class ExplorerLabelProvider extends AppearanceAwareLabelProvider {

	ILabelProvider superClassLabelProvider = new SuperClassLabelProvider(this);

	protected ExplorerLabelProvider(int textFlags, int imageFlags, ITreeContentProvider cp) {
		super(textFlags, imageFlags);
	}

	public Image getImage(Object object) {
		Image image = superClassLabelProvider.getImage(object);
		if (image != null) {
			return image;
		}
		if (object instanceof WorkingSet) {
			return WorkbenchImages.getImage(IWorkbenchGraphicConstants.IMG_OBJ_WORKING_SETS);
		}
		return super.getImage(object);
	}

	public String getText(Object element) {
		String text = superClassLabelProvider.getText(element);
		if (text != null)
			return text;
		String label = super.getText(element);
		if (label != null && label.startsWith("_lzx_")) { //$NON-NLS-1$
			label = label.substring(5);
			if (label.startsWith("Lz")) //$NON-NLS-1$
				label = "<" + label.substring(2) + ">"; //$NON-NLS-1$ //$NON-NLS-2$
			else
				label = "<_____   id=" + label + ">"; //$NON-NLS-1$ //$NON-NLS-2$
		}

		return label;
	}

}