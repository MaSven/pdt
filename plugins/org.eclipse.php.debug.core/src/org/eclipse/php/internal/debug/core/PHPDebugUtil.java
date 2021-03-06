/*******************************************************************************
 * Copyright (c) 2015, 2017 Zend Technologies and others.
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
package org.eclipse.php.internal.debug.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.php.internal.core.util.SyncObject;
import org.eclipse.php.internal.debug.core.debugger.DebuggerSettingsKind;
import org.eclipse.php.internal.debug.core.debugger.DebuggerSettingsManager;
import org.eclipse.php.internal.debug.core.debugger.IDebuggerSettings;
import org.eclipse.php.internal.debug.core.xdebug.dbgp.XDebugDebuggerConfiguration;
import org.eclipse.php.internal.debug.core.xdebug.dbgp.XDebugDebuggerSettingsConstants;
import org.eclipse.php.internal.debug.core.zend.debugger.ZendDebuggerConfiguration;
import org.eclipse.php.internal.debug.core.zend.debugger.ZendDebuggerSettingsConstants;
import org.eclipse.php.internal.debug.core.zend.debugger.ZendDebuggerSettingsUtil;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import com.ibm.icu.text.MessageFormat;

/**
 * Class with common utility methods for PHP debuggers.
 * 
 * @author Bartlomiej Laczkowski
 */
public final class PHPDebugUtil {

	private PHPDebugUtil() {
	}

	/**
	 * Opens URL from debug/run launch configuration. Users of this method should
	 * handle exceptions that might be thrown but without need to log the exception
	 * info (it is already handled by this implementation).
	 * 
	 * @param launchURL
	 * @throws DebugException
	 * @throws MalformedURLException
	 */
	public static void openLaunchURL(final String launchURL) throws DebugException {
		openLaunchURL(launchURL, true);
	}

	/**
	 * Opens URL from debug/run launch configuration, through a synchronous or an
	 * asynchronous UI thread call. When the UI thread is unavailable, the URL is
	 * always handled synchronously in a non-graphical way. Users of this method
	 * should handle exceptions that might be thrown but without need to log the
	 * exception info (it is already handled by this implementation). <b>Note that
	 * exceptions are only thrown when parameter doSyncExec is set to true or when
	 * the UI thread is unavailable.</b>
	 * 
	 * @param launchURL
	 * @param doSyncExec
	 *                       opens URL synchronously when true, asynchronously
	 *                       otherwise
	 * @throws DebugException
	 * @throws MalformedURLException
	 */
	public static void openLaunchURL(final String launchURL, final boolean doSyncExec) throws DebugException {
		// If no display is available, it means eclipse is shutting down,
		// try then to call the URL in a non-graphical way
		// https://bugs.eclipse.org/bugs/show_bug.cgi?id=517792
		if (PlatformUI.getWorkbench().getDisplay().isDisposed()) {
			try {
				URL url = new URL(launchURL);
				URLConnection con = url.openConnection();
				// Set the connection timeout to 5 seconds and the read timeout
				// to 5 seconds
				con.setConnectTimeout(5000);
				con.setReadTimeout(5000);
				InputStream is = con.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				while (reader.readLine() != null) {
				}
				reader.close();
			} catch (Throwable t) {
				Logger.logException(MessageFormat.format("Error initializing the connection for debug/launch URL: {0}", //$NON-NLS-1$
						launchURL), t);
				String errorMessage = PHPDebugCoreMessages.Debugger_Unexpected_Error_1;
				throw new DebugException(new Status(IStatus.ERROR, PHPDebugPlugin.getID(),
						IPHPDebugConstants.INTERNAL_ERROR, errorMessage, t));
			}

			return;
		}

		final SyncObject<DebugException> e = doSyncExec ? new SyncObject<>() : null;

		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					final URL urlToOpen = new URL(launchURL);
					StringBuilder browserTitle = new StringBuilder(urlToOpen.getProtocol()).append("://").append( //$NON-NLS-1$
							urlToOpen.getHost());
					if (urlToOpen.getPort() != -1) {
						browserTitle.append(':').append(urlToOpen.getPort());
					}
					browserTitle.append(urlToOpen.getPath());
					IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
					IWebBrowser browser = browserSupport.createBrowser(
							IWorkbenchBrowserSupport.LOCATION_BAR | IWorkbenchBrowserSupport.NAVIGATION_BAR
									| IWorkbenchBrowserSupport.STATUS,
							"PHP Debugger Browser", //$NON-NLS-1$
							browserTitle.toString(), browserTitle.toString());
					if (PHPDebugPlugin.DEBUG) {
						System.out.println("Opening debug/launch URL in a Web Browser: " //$NON-NLS-1$
								+ urlToOpen.toString());
					}
					browser.openURL(urlToOpen);
				} catch (Throwable t) {
					Logger.logException(
							MessageFormat.format("Error initializing the Web Browser for debug/launch URL: {0}", //$NON-NLS-1$
									launchURL),
							t);
					String errorMessage = PHPDebugCoreMessages.Debugger_Unexpected_Error_1;
					if (e != null) {
						e.set(new DebugException(new Status(IStatus.ERROR, PHPDebugPlugin.getID(),
								IPHPDebugConstants.INTERNAL_ERROR, errorMessage, t)));
					}
				}
			}
		};

		if (doSyncExec) {
			// Run synchronously to pass exception if any
			PlatformUI.getWorkbench().getDisplay().syncExec(r);
			@SuppressWarnings("null")
			DebugException ex = e.get();
			if (ex != null) {
				throw ex;
			}
		} else {
			PlatformUI.getWorkbench().getDisplay().asyncExec(r);
		}
	}

	/**
	 * Computes and returns a set of non-duplicated port numbers that are common for
	 * all of the possible settings for particular debugger type.
	 * 
	 * @param debuggerId
	 * @return set of unique port numbers for given debugger type
	 */
	public static Set<Integer> getDebugPorts(String debuggerId) {
		Set<Integer> ports = new HashSet<>();
		if (debuggerId.equals(ZendDebuggerConfiguration.ID)) {
			// Get default port from preferences first
			Integer defaultPort = PHPDebugPlugin.getDebugPort(debuggerId);
			ports.add(defaultPort);
			// Get ports from dedicated settings
			List<IDebuggerSettings> allSettings = DebuggerSettingsManager.INSTANCE.findSettings(debuggerId);
			for (IDebuggerSettings settings : allSettings) {
				String clientPort = settings.getAttribute(ZendDebuggerSettingsConstants.PROP_CLIENT_PORT);
				try {
					Integer dedicatedPort = Integer.valueOf(clientPort);
					ports.add(dedicatedPort);
				} catch (Exception e) {
					// ignore
				}
			}
		} else if (debuggerId.equals(XDebugDebuggerConfiguration.ID)) {
			// Get default port from preferences first
			Integer defaultPort = PHPDebugPlugin.getDebugPort(debuggerId);
			ports.add(defaultPort);
			// Get ports from all of debugger dedicated settings
			for (IDebuggerSettings settings : DebuggerSettingsManager.INSTANCE.findSettings(debuggerId)) {
				String clientPort = settings.getAttribute(XDebugDebuggerSettingsConstants.PROP_CLIENT_PORT);
				try {
					Integer dedicatedPort = Integer.valueOf(clientPort);
					ports.add(dedicatedPort);
				} catch (Exception e) {
					// ignore
				}
			}
		}
		return ports;
	}

	/**
	 * Finds and returns all registered Zend Debugger client hosts/IPs.
	 * 
	 * @return all registered Zend Debugger client hosts/IPs.
	 */
	public static String getZendAllHosts() {
		Set<String> merged = new LinkedHashSet<>();
		// Check default list from preferences first
		String defaultHosts = PHPDebugPlugin.getDebugHosts();
		for (String host : getZendHostsArray(defaultHosts)) {
			merged.add(host);
		}
		// Check all possible server settings
		List<IDebuggerSettings> allSettings = DebuggerSettingsManager.INSTANCE
				.findSettings(ZendDebuggerConfiguration.ID);
		for (IDebuggerSettings settings : allSettings) {
			if (settings.getKind() == DebuggerSettingsKind.PHP_SERVER) {
				String settingsHosts = ZendDebuggerSettingsUtil.getDebugHosts(settings.getOwnerId());
				for (String host : getZendHostsArray(settingsHosts)) {
					merged.add(host);
				}
			}
		}
		String[] mergedArray = merged.toArray(new String[merged.size()]);
		return getZendHostsString(mergedArray);
	}

	/**
	 * Converts string of Zend Debugger specific hosts/IPs to an array.
	 * 
	 * @param hostsString
	 * @return array of Zend Debugger specific hosts/IPs.
	 */
	public static String[] getZendHostsArray(String hostsString) {
		if (hostsString.isEmpty()) {
			return new String[0];
		}
		String[] hosts = hostsString.split(","); //$NON-NLS-1$
		for (int i = 0; i < hosts.length; i++) {
			hosts[i] = hosts[i].trim();
		}
		return hosts;
	}

	/**
	 * Converts an array of Zend Debugger specific hosts/IPs into single string.
	 * 
	 * @param hostsArray
	 * @return Zend Debugger specific hosts/IPs single string
	 */
	public static String getZendHostsString(String[] hostsArray) {
		if (hostsArray.length == 1) {
			return hostsArray[0];
		} else if (hostsArray.length > 1) {
			StringBuilder StringBuilder = new StringBuilder();
			StringBuilder.append(hostsArray[0]);
			for (int i = 1; i < hostsArray.length; i++) {
				StringBuilder.append(", " + hostsArray[i]); //$NON-NLS-1$
			}
			return StringBuilder.toString();
		}
		return ""; //$NON-NLS-1$
	}

	public static boolean isSystem5() {
		String system = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
		return ("os400".equals(system) || "aix".equals(system)); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
