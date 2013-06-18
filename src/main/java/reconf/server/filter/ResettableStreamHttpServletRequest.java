/*
 *    Copyright 1996-2013 UOL Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package reconf.server.filter;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.collections.*;
import org.apache.commons.io.*;
import org.apache.commons.lang3.*;

/**
 * https://gist.github.com/2071634
 */
@SuppressWarnings("rawtypes")
public class ResettableStreamHttpServletRequest extends HttpServletRequestWrapper {

    private final Set<String> specialHeaders = new TreeSet<String>(Arrays.asList(AuditingFilter.BODY_HEADER, AuditingFilter.USER_HEADER));

    private byte[] rawData;
    private HttpServletRequest request;
    private ResettableServletInputStream servletStream;
    private String body;

    public ResettableStreamHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.request = request;
        this.servletStream = new ResettableServletInputStream();

        try {
            this.body = StringEscapeUtils.unescapeJava(IOUtils.toString(getReader()));

        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            resetInputStream();
        }
    }

    public void resetInputStream() {
        servletStream.stream = new ByteArrayInputStream(rawData);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(getReader());
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return servletStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (rawData == null) {
            rawData = IOUtils.toByteArray(this.request.getInputStream());
            servletStream.stream = new ByteArrayInputStream(rawData);
        }
        return new BufferedReader(new InputStreamReader(servletStream));
    }

    @Override
    public String getHeader(String name) {
        if (StringUtils.equals(name, AuditingFilter.BODY_HEADER)) {
            return body;
        }
        if (StringUtils.equals(name, AuditingFilter.USER_HEADER)) {
            return getHeader("X-ReConf-User");
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration getHeaders(String name) {
        if (specialHeaders.contains(name)) {
            return new Vector(Arrays.asList(getHeader(name))).elements();
        }
        return super.getHeaders(name);
    }

    @Override
    public Enumeration getHeaderNames() {
        List partial = EnumerationUtils.toList(super.getHeaderNames());
        partial.addAll(specialHeaders);
        return new Vector(partial).elements();
    }

    private class ResettableServletInputStream extends ServletInputStream {
        private InputStream stream;

        @Override
        public int read() throws IOException {
            return stream.read();
        }
    }
}