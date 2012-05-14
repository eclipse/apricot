/*
 * Copyright (c) 2006-2012 Nuxeo SA (http://nuxeo.com/) and others.
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

package org.eclipse.ecr.web.framework;

import java.net.URI;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriInfo;

import org.eclipse.ecr.runtime.api.Framework;
import org.eclipse.ecr.web.jaxrs.context.RequestContext;

public class WebContext {


	public static void setActiveContext(WebContext ctx) {
		RequestContext.getActiveContext().put(WebContext.class.getName(), ctx);
	}

	public static void setActiveContext(ServletRequest request, WebContext ctx) {
		RequestContext.getActiveContext(request).put(
				WebContext.class.getName(), ctx);
	}

	public static WebContext getActiveContext() {
		return (WebContext) RequestContext.getActiveContext().get(
				WebContext.class.getName());
	}

	public static WebContext getActiveContext(ServletRequest request) {
		return (WebContext) RequestContext.getActiveContext(request).get(
				WebContext.class.getName());
	}

	protected AbstractResource root;
	
	protected UriInfo uriInfo;

	protected HttpServletRequest request;

	protected HttpServletResponse response;

	protected WebApplication app;

	protected PathResolver resolver;
	
	protected Locale locale;
	
	protected Breadcrumb breadcrumb;

	protected Map<String, Object> properties;

	public WebContext() {
		this (Framework.getLocalService(WebFramework.class).getDefaultApplication());
	}

	public WebContext(String appName) {
		this (Framework.getLocalService(WebFramework.class).getApplication(appName));
	}
	
	public WebContext(WebApplication app) {
		this.app = app;
		breadcrumb = new Breadcrumb();
		properties = new HashMap<String, Object>();
	}
	
	public WebApplication getApplication() {
		return app;
	}

	public void setUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

	public UriInfo getUriInfo() {
		return uriInfo;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Principal getPrincipal() {
		return request.getUserPrincipal();
	}

	public PathResolver getPathResolver() {
		return resolver;
	}
	
	public void init(AbstractResource root, PathResolver resolver) {
		this.root = root;
		this.resolver = resolver;
	}
	
	/**
	 * Get the application root URL. Example: "http://localhost:8080/ecr/qa"
	 * @return
	 */
	public final String getRootUrl() {
		return resolver.getRootUrl();
	}

	/**
	 * Get the skins base URL. Ex: "http://localhost:8080/ecr/root/skin"
	 * @return
	 */
	public final String getSkinPath() {
		return resolver.getSkinUrl();
	}

//	public URI getUri(String absPath) {
//		return uriInfo.getAbsolutePathBuilder().replacePath(absPath).build();
//	}
	
	public String getRequestUrl() {
		return request.getRequestURL().toString();
	}

	public WebRoot getRoot() {
		List<Object> res = uriInfo.getMatchedResources();
		return (WebRoot) res.get(res.size() - 1);
	}

	public String getMessage(String key) {
		return getApplication().getMessage(getLocale().getLanguage(), key);
	}

	public String getMessage(String key, Object... vars) {
		return getApplication()
				.getMessage(getLocale().getLanguage(), key, vars);
	}

	public String getMessage(String key, List<Object> vars) {
		return getApplication()
				.getMessage(getLocale().getLanguage(), key, vars);
	}

	public void setLanguage(String language) {
		this.locale = new Locale(language);
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Locale getLocale() {
		if (locale == null) {
			return request.getLocale();
		}
		return locale;
	}

	public Breadcrumb getBreadcrumb() {
		return breadcrumb;
	}

	public String getParameter(String key) {
		return request.getParameter(key);
	}

	public String getParameter(String key, String defValue) {
		String v = request.getParameter(key);
		return v == null ? defValue : v;
	}

	public String[] getParameters(String key) {
		return request.getParameterValues(key);
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public Object getProperty(String key) {
		return properties.get(key);
	}

	public void setProperty(String key, Object value) {
		properties.put(key, value);
	}

	
	/** Path mapping helpers */
	
	public final static String normalizeUrl(String url) {
		return url.endsWith("/") ? url.substring(0, url.length()-1) : url; 
	}
	
	/**
	 * Normalize the given path:
	 * if path is null or "" or "/" returns ""
	 * else remove trailing slash and append leading slash if none
	 * @param path
	 * @return
	 */
	public final static String normalizePath(String path) {
		if (path == null || path.length() == 0 || "/".equals(path)) {
			return "";
		}
		if (path.endsWith("/")) {
			path = path.substring(0, path.length()-1);
			return path.startsWith("/") ? path : "/".concat(path);
			//return path.startsWith("/") ? path.substring(0, path.length()-1) : :  
		} else if (!path.startsWith("/")) {
			return "/".concat(path);
		} else { // already normalized
			return path;
		}
	}
	
	/**
	 * Append path to basePath.
	 * @param basePath
	 * @param path
	 * @return
	 */
	public final static String appendPath(String basePath, String path) {
		if (basePath == null || basePath.length() == 0) {
			return path;
		}
		if (basePath.endsWith("/")) {
			if (path.startsWith("/")) {
				return basePath.concat(path.substring(1));
			} else {				
				return basePath.concat(path);
			}
		} else {
			if (path.startsWith("/")) {
				return basePath.concat(path);
			} else {
				return new StringBuilder().append(basePath).append('/').append(path).toString();
			}
		}
	}
	
	/**
	 * Shift the given path with 'shift' segments count. (shifting is done on left).
	 * Example: Using a shift of 2: /c/domain/q => /q
	 * 
	 * @param path
	 * @param shift
	 * @return
	 */
	public final static String shiftPath(String path, int shift) {
		int s = path.startsWith("/") ? 1 : 0;
		for (int i = 0; i<shift; i++) {
			int e = path.indexOf('/', s);
			if (e == -1) {
				return "/";
			}
			s = e+1;
		}
		return s > 1 ? path.substring(s-1) : path;		
	}
	
	public static String getDefaultBaseUrl(HttpServletRequest req, String defBasePath) {
		String scheme = req.getScheme();
		int port = req.getServerPort();
		StringBuilder url = new StringBuilder();
		url.append(scheme).append("://").append(req.getServerName());
		if ((scheme.equals ("http") && port != 80)
				|| (scheme.equals ("https") && port != 443)) {
			url.append (':');
			url.append (req.getServerPort ());
		}
		if (defBasePath != null) {
			url.append(defBasePath);
		}
		return url.toString();
	}

}
