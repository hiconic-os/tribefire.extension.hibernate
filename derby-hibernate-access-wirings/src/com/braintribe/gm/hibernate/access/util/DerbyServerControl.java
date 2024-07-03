// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.gm.hibernate.access.util;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.sql.DriverManager;

import org.apache.derby.drda.NetworkServerControl;

import com.braintribe.logging.Logger;
import com.braintribe.logging.Logger.LogLevel;
import com.braintribe.logging.io.LoggingPrintWriter;
import com.braintribe.util.network.NetworkTools;
import com.braintribe.utils.stream.TeePrintWriter;

public class DerbyServerControl {

	protected static Logger logger = Logger.getLogger(DerbyServerControl.class);

	protected NetworkServerControl serverControl = null;
	protected boolean started = false;

	protected Integer port = null;
	protected boolean trace = false;
	protected File traceDirectory = null;
	protected boolean logConnections = false;
	protected PrintWriter logWriter = new LoggingPrintWriter(logger, LogLevel.DEBUG);

	protected long waitForStartTime = 10_000L;

	public void setServerControl(NetworkServerControl serverControl) {
		this.serverControl = serverControl;
	}

	public void start() throws Exception {
		if (this.started) {
			return;
		}

		if (port == null) {
			getPort();
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Starting Derby Database server.");
		}
		System.setProperty("derby.drda.startNetworkServer", "true");

		if (traceDirectory != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Traces will be stored in: " + traceDirectory.getAbsolutePath());
			}
			System.setProperty("derby.drda.traceDirectory", traceDirectory.getAbsolutePath());
			System.setProperty("derby.drda.traceAll", "" + trace);
		}
		System.setProperty("derby.drda.logConnections", "" + logConnections);

		StringWriter logOutputWriter = new StringWriter();
		TeePrintWriter teePrintWriter = new TeePrintWriter(logWriter, logOutputWriter);

		try {
			serverControl = new NetworkServerControl(InetAddress.getByName("localhost"), port);
			serverControl.start(teePrintWriter);
			this.started = true;
		} catch (Exception e) {
			throw new Exception("Could not start Derby database.", e);
		}

		long start = System.currentTimeMillis();
		do {
			try {
				serverControl.ping();
				break;
			} catch (Exception e) {
				logger.debug("Could not ping Derby DB server. Waiting...");
				Thread.sleep(1000L);
			}
		} while ((System.currentTimeMillis() - start) < waitForStartTime);

		try {
			serverControl.ping();
		} catch (Exception e) {
			teePrintWriter.flush();

			throw new Exception("Could not verify that the Derby DB has been started successfully.\n" + logOutputWriter.toString());
		} finally {
			teePrintWriter.stopTee();
		}

	}

	public void stop() throws Exception {
		if (!this.started) {
			return;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Stopping Derby Database server.");
		}
		System.setProperty("derby.drda.startNetworkServer", "false");
		try {
			if (this.serverControl != null) {
				this.serverControl.shutdown();
				this.serverControl = null;

				try {
					DriverManager.getConnection("jdbc:derby:;shutdown=true");
				} catch (Exception ex) {
					// do nothing; see http://db.apache.org/derby/papers/DerbyTut/embedded_intro.html#shutdown
				}

			}
		} catch (Exception e) {
			throw new Exception("Error while stopping the Derby database.");
		}
	}

	public void setPort(int port) {
		this.port = port;
	}
	public int getPort() {
		if (port == null) {
			port = NetworkTools.getUnusedPortInRange(1527, 1627);
		}
		return port.intValue();
	}

	public void setTraceDirectory(File traceDirectory) {
		if (traceDirectory != null) {
			if (!traceDirectory.exists()) {
				traceDirectory.mkdirs();
			}
			this.trace = true;
			this.traceDirectory = traceDirectory;
		}
	}

	public void setLogConnections(boolean logConnections) {
		this.logConnections = logConnections;
	}

	public void setLogWriter(PrintWriter logWriter) {
		this.logWriter = logWriter;
	}

	public void setWaitForStartTime(long waitForStartTime) {
		this.waitForStartTime = waitForStartTime;
	}

}
