/*******************************************************************************
 * Copyright (c) 2009, 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Zend Technologies
 *     Dawid Pakuła - convert to JUnit4
 *******************************************************************************/
package org.eclipse.php.core.tests.errors;

import org.eclipse.php.core.PHPVersion;
import org.eclipse.php.core.tests.runner.PDTTList;
import org.eclipse.php.core.tests.runner.PDTTList.Parameters;
import org.junit.runner.RunWith;

@RunWith(PDTTList.class)
public class PHP72ErrorReportingTests extends AbstractErrorReportingTests {

	@Parameters
	public static final String[] TEST_DIRS = { "/workspace/errors/php54", "/workspace/errors/php55",
			"/workspace/errors/php56", "/workspace/errors/php7", "/workspace/errors/php71", "/workspace/errors/php72" };

	public PHP72ErrorReportingTests(String[] fileNames) {
		super(fileNames);
	}

	@Override
	protected PHPVersion getPHPVersion() {
		return PHPVersion.PHP7_2;
	}
}