package com.y3r9.c47.dog.demo.security;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.*;
import javax.security.auth.spi.LoginModule;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by zyq on 2014/8/19.
 */
public class PropertiesFileBasedLoginModule implements LoginModule {
    private CallbackHandler callbackHandler;
    private Subject subject;
    private Properties props = new Properties();
    private boolean authSucceeded = false;
    private String authUsername = null;

    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.callbackHandler = callbackHandler;
        this.subject = subject;
        String propsFilePath = (String) options.get("properties.file.path");
        File propsFile = new File(propsFilePath);
        try {
            props.load(new FileInputStream(propsFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean login() throws LoginException {
        TextInputCallback usernameInputCallback = new TextInputCallback("用户名：");
        TextInputCallback passwordInputCallback = new TextInputCallback("密码：");
        try {
            callbackHandler.handle(new Callback[] {
               usernameInputCallback, passwordInputCallback
            });
        } catch (Exception e) {
            throw new LoginException(e.getMessage());
        }
        String username = usernameInputCallback.getText();
        if (null == username || "".equals(username.trim())) {
            throw new AccountException("用户名为空");
        } else if (!props.containsKey(username)) {
            throw new AccountNotFoundException("该用户不存在");
        }
        String password = passwordInputCallback.getText();
        if (null == password || "".equals(password.trim())) {
            throw new CredentialException("密码为空");
        } else if (!password.equals(props.get(username))) {
            throw new FailedLoginException("用户名和密码不匹配");
        }
        authSucceeded = true;
        authUsername = username;
        return true;
    }

    @Override
    public boolean commit() throws LoginException {
        if (authSucceeded) {
            this.subject.getPrincipals().add(new UserPrincipal(authUsername));
            authUsername = null;
            authSucceeded = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean abort() throws LoginException {
        authUsername = null;
        if (authSucceeded) {
            authSucceeded = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean logout() throws LoginException {
        Set<Principal> principals = subject.getPrincipals();
        Set<UserPrincipal> userPrincipals = subject.getPrincipals(UserPrincipal.class);
        for (UserPrincipal principal : userPrincipals) {
            if (principal.getName().equals(authUsername)) {
                principals.remove(principal);
                break;
            }
        }
        return true;
    }
}
