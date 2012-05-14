/*
 * Copyright (c) 2006-2011 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     bstefanescu
 *
 */
package org.eclipse.ecr.web.framework.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.ecr.web.framework.PathResolver;

/**
 * @author bstefanescu
 *
 */
public class TestPaths {

	static PathResolver create(String rootUrl, String baseUrl) throws Exception {
		DummyRequest r = new DummyRequest("http://locahost:8080", "/ecr", "");
		r.setHeader("ROOT_URL", rootUrl);
		r.setHeader("BASE_URL", baseUrl);
		return PathResolver.create(r, "qa", "/skin", "ROOT_URL", "BASE_URL");
	}
	
	public static void main(String[] args) throws Exception {		
		PathResolver b; 
		b = create(null, null);
		System.out.println("#########\n"+b+" -> "+b.getPath("qa/a/domain/mybase"));
		b = create("http://app.quandora.com", "http://app.quandora.com/..");
		System.out.println("#########\n"+b+" -> "+b.getPath("qa/a/domain/mybase"));
		b = create("http://app.quandora.com", "http://domain.quandora.com/../../..");
		System.out.println("#########\n"+b+" -> "+b.getPath("qa/a/domain/mybase"));
	}

	static class DummyRequest implements HttpServletRequest {

		protected URL url;
		protected HashMap<String, Object> attrs = new HashMap<String, Object>();
		protected HashMap<String, String> headers = new HashMap<String, String>();
		
		protected String ctxPath;
		protected String servletPath;
		protected String pathInfo;
		protected String method;
		
		protected DummyRequest(String url, String ctxPath, String servletPath) throws MalformedURLException {
			this.url = new URL(url);
			this.ctxPath = ctxPath;
			this.servletPath = servletPath;
		}
		
		@Override
		public Object getAttribute(String arg0) {
			return attrs.get(arg0);
		}

		@Override
		public Enumeration getAttributeNames() {
			return Collections.enumeration(attrs.keySet());
		}

		@Override
		public String getCharacterEncoding() {
			return null;
		}

		@Override
		public int getContentLength() {
			return 0;
		}

		@Override
		public String getContentType() {
			return null;
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			return null;
		}

		@Override
		public String getLocalAddr() {
			return null;
		}

		@Override
		public String getLocalName() {
			return null;
		}

		@Override
		public int getLocalPort() {
			return 0;
		}

		@Override
		public Locale getLocale() {
			return null;
		}

		@Override
		public Enumeration getLocales() {
			return null;
		}

		@Override
		public String getParameter(String arg0) {
			return null;
		}

		@Override
		public Map getParameterMap() {
			return null;
		}

		@Override
		public Enumeration getParameterNames() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String[] getParameterValues(String arg0) {
			return null;
		}

		@Override
		public String getProtocol() {
			return url.getProtocol();
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return null;
		}

		@Override
		public String getRealPath(String arg0) {
			return null;
		}

		@Override
		public String getRemoteAddr() {
			return null;
		}

		@Override
		public String getRemoteHost() {
			return null;
		}

		@Override
		public int getRemotePort() {
			return 0;
		}

		@Override
		public RequestDispatcher getRequestDispatcher(String arg0) {
			return null;
		}

		@Override
		public String getScheme() {
			return url.getProtocol();
		}

		@Override
		public String getServerName() {
			return url.getHost();
		}

		@Override
		public int getServerPort() {
			int port = url.getPort();
			if (port == -1) {
				return isSecure() ? 443 : 80;				
			} else {
				return port;
			}
		}

		@Override
		public boolean isSecure() {
			return "https".equals(url.getProtocol());
		}

		@Override
		public void removeAttribute(String arg0) {
		}

		@Override
		public void setAttribute(String arg0, Object arg1) {
		}

		@Override
		public void setCharacterEncoding(String arg0)
				throws UnsupportedEncodingException {
		}

		@Override
		public String getAuthType() {
			return null;
		}

		@Override
		public String getContextPath() {
			return ctxPath;
		}

		@Override
		public Cookie[] getCookies() {
			return null;
		}

		@Override
		public long getDateHeader(String arg0) {
			return 0;
		}

		@Override
		public String getHeader(String arg0) {
			return headers.get(arg0);
		}
		
		public void setHeader(String key, String value) {
			headers.put(key, value);
		}

		@Override
		public Enumeration getHeaderNames() {
			return Collections.enumeration(headers.keySet());
		}

		@Override
		public Enumeration getHeaders(String arg0) {
			return Collections.enumeration(headers.values());
		}

		@Override
		public int getIntHeader(String arg0) {
			return 0;
		}

		@Override
		public String getMethod() {
			return method;
		}

		@Override
		public String getPathInfo() {
			return pathInfo;
		}

		@Override
		public String getPathTranslated() {
			return null;
		}

		@Override
		public String getQueryString() {
			return null;
		}

		@Override
		public String getRemoteUser() {
			return null;
		}

		@Override
		public String getRequestURI() {
			return url.toString();
		}

		@Override
		public StringBuffer getRequestURL() {
			return new StringBuffer(url.toString());
		}

		@Override
		public String getRequestedSessionId() {
			return null;
		}

		@Override
		public String getServletPath() {
			return servletPath;
		}

		@Override
		public HttpSession getSession() {
			return null;
		}

		@Override
		public HttpSession getSession(boolean arg0) {
			return null;
		}

		@Override
		public Principal getUserPrincipal() {
			return null;
		}

		@Override
		public boolean isRequestedSessionIdFromCookie() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromURL() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromUrl() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdValid() {
			return false;
		}

		@Override
		public boolean isUserInRole(String arg0) {
			return false;
		}
		
	}
}
