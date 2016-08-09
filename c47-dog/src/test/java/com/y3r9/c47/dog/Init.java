package com.y3r9.c47.dog;

import java.security.Permission;

import org.apache.commons.lang3.StringUtils;

/**
 * The class Init.
 *
 * @version 1.0
 */
final class Init {

    public static void init() {
        forbidSystemExitCall();
    }

    private static void forbidSystemExitCall() {
        final SecurityManager securityManager = new SecurityManager() {
            public void checkPermission(Permission permission) {
                if (permission.getName().startsWith("exitVM")) {
                    Exception ex = new RuntimeException("Something called exit ");

                    StackTraceElement[] stackElements = ex.getStackTrace();
                    if (stackElements == null) {
                        return;
                    }

                    // System.out.println(StringUtils.repeat(' ', 20));
                    // for (int i = 0; i < stackElements.length; i++) {
                    // final StackTraceElement elm = stackElements[i];
                    // System.out.println(String.format("%s.%s() (%s %s)", elm.getClassName(),
                    // elm.getMethodName(), elm.getFileName(), elm.getLineNumber()));
                    // }
                    // System.out.println(StringUtils.repeat(' ', 20));

                    if (stackElements.length < 5) {
                        return;
                    }

                    StackTraceElement forkedBooterElm = stackElements[4];
                    if ("org.apache.maven.surefire.booter.ForkedBooter".equals(forkedBooterElm
                            .getClassName())) {
                        return;
                    }
                    try {
                        throw ex;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        System.setSecurityManager(securityManager);
    }
}
