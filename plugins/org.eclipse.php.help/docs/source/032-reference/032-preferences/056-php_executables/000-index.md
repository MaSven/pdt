# PHP Executables Preferences

<!--context:php_executables--><!--context:php_executables_preferences-->

The PHP Executables Preferences page allows you to add, edit, remove and find PHP Executables.

The internal debugger in the defined PHP Executable is used for [local PHP Script debugging](../../024-tasks/152-debugging/024-locally_debugging_a_php_script.md).

<!--note-start-->

#### Note:

You must configure a PHP Executable before you can debug locally.

<!--note-end-->

The PHP Executables Preferences Preferences page is accessed from Window | Preferences | PHP | PHP Executables.

![php_executables.png](images/php_executables.png "php_executables.png")

<!--ref-start-->

To add a PHP executable to the list:

 1. Click Add.  The Add PHP Executable dialog will appear.<p>![php_executable_add_pdt.png](images/php_executable_add_pdt.png "php_executable_add_pdt.png")</p>
 2. Enter a name for the PHP Executable.
 3. In the Executable path selection, enter the location of the PHP executable on your file system.
 4. Select the PHP ini file to be associated with the PHP Executable by clicking Browse (optional).
 5. Press Next to open page with debugger settings.<p>![php_executable_add_debug_pdt.png](images/php_executable_add_debug_pdt.png "php_executable_add_debug_pdt.png")</p>
 6. If there is a need, change the default debugger connection settings.
 7. Click Finish.

The PHP executable will be added to your list.

![php_executables_added.png](images/php_executables_added.png "php_executables_added.png")

<!--ref-end-->

<!--ref-start-->

To search for a PHP executable on your local file system:

 1. Click Search.
 2. In the Directory Selection dialog, select the folder to search.
 3. Click OK. PDT will search for PHP executables in the location specified.

Any found PHP executables will be added to the list.

To change the name of the new PHP executable, select it from the list and click Edit.

<!--ref-end-->

<!--toc-->

<!--links-start-->

#### Related Links:

 * [PHP Support](../../../016-concepts/008-php_support.md)
 * [PHP Interpreter Preferences](../064-php_interpreter.md)

<!--links-end-->
