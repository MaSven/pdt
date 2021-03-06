/*******************************************************************************
 * Copyright (c) 2013 Zend Technologies and others.
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
package org.eclipse.php.core.tests.compiler_ast.parser;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.php.core.PHPVersion;
import org.eclipse.php.core.compiler.ast.nodes.UsePart;
import org.eclipse.php.core.project.ProjectOptions;
import org.eclipse.php.core.tests.PDTTUtils;
import org.eclipse.php.core.tests.PdttFile;
import org.eclipse.php.core.tests.TestSuiteWatcher;
import org.eclipse.php.core.tests.runner.PDTTList;
import org.eclipse.php.core.tests.runner.PDTTList.Parameters;
import org.eclipse.php.internal.core.compiler.ast.parser.ASTUtils;
import org.eclipse.php.internal.core.compiler.ast.parser.AbstractPHPSourceParser;
import org.eclipse.php.internal.core.compiler.ast.parser.PHPSourceParserFactory;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.RunWith;

@RunWith(PDTTList.class)
public class FindUseStatementByAliasTests {

	@ClassRule
	public static TestWatcher watcher = new TestSuiteWatcher();

	@Parameters
	public static final Map<PHPVersion, String[]> TESTS = new LinkedHashMap<>();

	static {
		TESTS.put(PHPVersion.PHP5_3, new String[] { "/workspace/astutils/find_use_statement_by_alias/php53" }); //$NON-NLS-1$
	};

	private AbstractPHPSourceParser parser;

	public FindUseStatementByAliasTests(PHPVersion version, String[] fileNames) {
		parser = PHPSourceParserFactory.createParser(version);
	}

	@Test
	public void find(String fileName) throws Exception {
		final PdttFile pdttFile = new PdttFile(fileName);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(pdttFile.getFile().trim().getBytes());
		ModuleDeclaration moduleDeclaration = (ModuleDeclaration) parser.parse(new InputStreamReader(inputStream), null,
				ProjectOptions.isSupportingASPTags((IProject) null), ProjectOptions.useShortTags((IProject) null));

		String alias = pdttFile.getConfig().get("alias"); //$NON-NLS-1$
		int offset = Integer.parseInt(pdttFile.getConfig().get("offset")); //$NON-NLS-1$

		UsePart usePart = ASTUtils.findUseStatementByAlias(moduleDeclaration, alias, offset);

		String actual = (usePart == null) ? "null" : usePart.toString(); //$NON-NLS-1$
		PDTTUtils.assertContents(pdttFile.getExpected(), actual);
	}
}
