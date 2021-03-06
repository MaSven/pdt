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
package org.eclipse.php.internal.ui.actions;

import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

public interface IPHPEditorActionDefinitionIds extends ITextEditorActionDefinitionIds {

	// edit

	/**
	 * Action definition ID of the refactor -> move element action (value
	 * <code>"org.eclipse.php.ui.edit.text.move.element"</code>).
	 */
	String MOVE_ELEMENT = "org.eclipse.php.ui.edit.text.move.element"; //$NON-NLS-1$

	/**
	 * Action definition ID of the refactor -> rename element action (value
	 * <code>"org.eclipse.php.ui.edit.text.rename.element"</code>).
	 */
	String RENAME_ELEMENT = "org.eclipse.php.ui.edit.text.rename.element"; //$NON-NLS-1$

	/**
	 * Action definition ID of the edit -> content assist complete prefix action
	 * (value: <code>"org.eclipse.php.ui.edit.text.complete.prefix"</code>).
	 * 
	 * @since 3.0
	 */
	String CONTENT_ASSIST_COMPLETE_PREFIX = "org.eclipse.php.ui.edit.text.complete.prefix"; //$NON-NLS-1$

	/**
	 * Action definition ID of the edit -> show PHPdoc action (value
	 * <code>"org.eclipse.php.ui.edit.text.show.phpdoc"</code>).
	 */
	String SHOW_PHPDOC = "org.eclipse.php.ui.edit.text.show.phpdoc"; //$NON-NLS-1$

	/**
	 * Action definition ID of the navigate -> Show Outline action (value
	 * <code>"org.eclipse.php.ui.edit.text.show.outline"</code>).
	 * 
	 * @since 2.1
	 */
	String SHOW_OUTLINE = "org.eclipse.php.ui.edit.text.show.outline"; //$NON-NLS-1$

	// source

	/**
	 * Action definition ID of the source -> comment action (value
	 * <code>"org.eclipse.php.ui.edit.text.comment"</code>).
	 */
	String COMMENT = "org.eclipse.php.ui.edit.text.comment"; //$NON-NLS-1$

	/**
	 * Action definition ID of the source -> uncomment action (value
	 * <code>"org.eclipse.php.ui.edit.text.uncomment"</code>).
	 */
	String UNCOMMENT = "org.eclipse.php.ui.edit.text.uncomment"; //$NON-NLS-1$

	/**
	 * Action definition ID of the source -> add PHP Doc (value
	 * <code>"org.eclipse.php.ui.edit.text.add.description"</code>).
	 * 
	 * @since 3.0
	 */
	String ADD_DESCRIPTION = "org.eclipse.php.ui.edit.text.add.description"; //$NON-NLS-1$

	/**
	 * Action definition ID of the source -> toggle comment action (value
	 * <code>"org.eclipse.php.ui.edit.text.toggle.comment"</code>).
	 * 
	 * @since 3.0
	 */
	String TOGGLE_COMMENT = "org.eclipse.wst.sse.ui.toggle.comment"; //$NON-NLS-1$

	/**
	 * Action definition ID of the source -> add block comment action (value
	 * <code>"org.eclipse.wst.sse.ui.add.block.comment"</code>).
	 * 
	 * @since 3.0
	 */
	String ADD_BLOCK_COMMENT = "org.eclipse.wst.sse.ui.add.block.comment"; //$NON-NLS-1$

	/**
	 * Action definition ID of the source -> remove block comment action (value
	 * <code>"org.eclipse.php.ui.edit.text.remove.block.comment"</code>).
	 * 
	 * @since 3.0
	 */
	String REMOVE_BLOCK_COMMENT = "org.eclipse.wst.sse.ui.remove.block.comment"; //$NON-NLS-1$

	/**
	 * Action definition ID of the source -> indent action (value
	 * <code>"org.eclipse.php.ui.edit.text.indent"</code>).
	 */
	String INDENT = "org.eclipse.php.ui.edit.text.indent"; //$NON-NLS-1$

	/**
	 * Action definition ID of the source -> format action (value
	 * <code>"org.eclipse.php.ui.edit.text.format"</code>).
	 */
	String FORMAT = "org.eclipse.php.ui.edit.text.format"; //$NON-NLS-1$

	/**
	 * Action definition id of the quick format action (value:
	 * <code>"org.eclipse.php.ui.edit.text.quick.format"</code>).
	 * 
	 * @since 3.0
	 */
	String QUICK_FORMAT = "org.eclipse.php.ui.edit.text.quick.format"; //$NON-NLS-1$

	/**
	 * Action definition ID of the navigate -> open action (value
	 * <code>"org.eclipse.php.ui.edit.text.open.editor"</code>).
	 */
	String OPEN_EDITOR = "org.eclipse.php.ui.edit.text.open.editor"; //$NON-NLS-1$

	/**
	 * Action definition ID of the navigate -> show in php explorer action (value
	 * <code>"org.eclipse.php.ui.edit.text.show.in.explorer.view"</code> ).
	 */
	String SHOW_IN_EXPLORER_VIEW = "org.eclipse.php.ui.edit.text.show.in.explorer.view"; //$NON-NLS-1$

	/**
	 * Action definition ID of the navigate -> show in navigator action (value
	 * <code>"org.eclipse.php.ui.edit.text.show.in.navigator.view"</code>).
	 */
	String SHOW_IN_NAVIGATOR_VIEW = "org.eclipse.php.ui.edit.text.show.in.navigator.view"; //$NON-NLS-1$

	/**
	 * Action definition ID of the search -> occurrences in file > method exits
	 * action (value
	 * <code>"org.eclipse.php.ui.edit.text.search.method.exits"</code>).
	 * 
	 * @since 3.4
	 */
	String SEARCH_METHOD_EXIT_OCCURRENCES = "org.eclipse.php.ui.edit.text.search.method.exits"; //$NON-NLS-1$

	/**
	 * Action definition ID of the search -> occurrences in file quick menu action
	 * (value
	 * <code>"org.eclipse.php.ui.edit.text.search.occurrences.in.file.quickMenu"</code>
	 * ).
	 * 
	 * @since 3.1
	 */
	String SEARCH_OCCURRENCES_IN_FILE_QUICK_MENU = "org.eclipse.php.ui.edit.text.search.occurrences.in.file.quickMenu"; //$NON-NLS-1$

	// miscellaneous

	/**
	 * Action definition ID of the toggle text hover tool bar button action (value
	 * <code>"org.eclipse.php.ui.edit.text.toggle.text.hover"</code>).
	 */
	String TOGGLE_TEXT_HOVER = "org.eclipse.php.ui.edit.text.toggle.text.hover"; //$NON-NLS-1$

	/**
	 * Action definition ID of the remove occurrence annotations action (value
	 * <code>"org.eclipse.php.ui.edit.text.remove.occurrence.annotations"</code> ).
	 * 
	 * @since 3.0
	 */
	String REMOVE_OCCURRENCE_ANNOTATIONS = "org.eclipse.php.ui.edit.text.remove.occurrence.annotations"; //$NON-NLS-1$

	/**
	 * Action definition id of addDescriptionAction (value:
	 * <code>"org.eclipse.php.ui.edit.text.add.description"</code>).
	 * 
	 * @since 3.0
	 */
	String ADD_PHP_DOC = "org.eclipse.php.ui.edit.text.add.description"; //$NON-NLS-1$

	/**
	 * Action definition id of toggle mark occurrences action (value:
	 * <code>"org.eclipse.php.ui.edit.text.toggleMarkOccurrences"</code>).
	 * 
	 * @since 3.0
	 */
	String TOGGLE_MARK_OCCURRENCES = "org.eclipse.php.ui.edit.text.toggleMarkOccurrences"; //$NON-NLS-1$

	/**
	 * Action definition ID of the edit -> go to matching bracket action (value
	 * <code>"org.eclipse.php.editor.goto.matching.bracket"</code>).
	 * 
	 * @since 2.1
	 */
	String GOTO_MATCHING_BRACKET = "org.eclipse.php.ui.editor.goto.matching.bracket"; //$NON-NLS-1$

	String OPEN_DECLARATION = "org.eclipse.php.ui.edit.text.open.editor"; //$NON-NLS-1$

	String ORGANIZE_USE_STATEMENT = "org.eclipse.php.ui.editor.organize.use.statements"; //$NON-NLS-1$

	/**
	 * Action definition ID of the navigate -> open type hierarchy action (value
	 * <code>"org.eclipse.php.ui.edit.text.php.open.type.hierarchy"</code>).
	 */
	String OPEN_TYPE_HIERARCHY = "org.eclipse.php.ui.edit.text.php.open.type.hierarchy"; //$NON-NLS-1$

	/**
	 * Action definition ID of the navigate -> open type hierarchy action (value
	 * <code>"org.eclipse.php.ui.edit.text.php.open.call.hierarchy"</code>).
	 */
	String OPEN_CALL_HIERARCHY = "org.eclipse.php.ui.edit.text.php.open.call.hierarchy"; //$NON-NLS-1$

	/**
	 * Action definition ID of the navigate -> open type hierarchy action (value
	 * <code>"org.eclipse.php.ui.edit.text.php.open.call.hierarchy"</code>).
	 */
	String OPEN_PHP_MANUAL = "org.eclipse.php.ui.edit.text.php.open.manual"; //$NON-NLS-1$

	/**
	 * Action definition ID of the edit -> select enclosing action (value
	 * <code>"org.eclipse.pdt.ui.edit.text.select.enclosing"</code>).
	 */
	String SELECT_ENCLOSING = "org.eclipse.pdt.ui.edit.text.select.enclosing"; //$NON-NLS-1$

	/**
	 * Action definition ID of the edit -> select next action (value
	 * <code>"org.eclipse.pdt.ui.edit.text.select.next"</code>).
	 */
	String SELECT_NEXT = "org.eclipse.pdt.ui.edit.text.select.next"; //$NON-NLS-1$

	/**
	 * Action definition ID of the edit -> select previous action (value
	 * <code>"org.eclipse.pdt.ui.edit.text.select.previous"</code>).
	 */
	String SELECT_PREVIOUS = "org.eclipse.pdt.ui.edit.text.select.previous"; //$NON-NLS-1$

	/**
	 * Action definition ID of the edit -> select restore last action (value
	 * <code>"org.eclipse.pdt.ui.edit.text.select.last"</code>).
	 */
	String SELECT_LAST = "org.eclipse.pdt.ui.edit.text.select.last"; //$NON-NLS-1$

}
