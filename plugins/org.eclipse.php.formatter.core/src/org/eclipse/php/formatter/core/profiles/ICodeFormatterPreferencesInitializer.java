/*******************************************************************************
 * Copyright (c) 2013, 2018 Zend Techologies Ltd.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Zend Technologies Ltd. - initial API and implementation
 *******************************************************************************/
package org.eclipse.php.formatter.core.profiles;

public interface ICodeFormatterPreferencesInitializer {

	public CodeFormatterPreferences initValues();

	public CodeFormatterPreferences initValues(CodeFormatterPreferences preferences);

}
