package com.team_proj.dsfw_team_proj.auth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class PasswordBlockList {

    private static final Set<String> BLOCKED = new HashSet<>();

    static {
        try (InputStream is = PasswordBlockList.class
                .getClassLoader()
                .getResourceAsStream("password-blocklist.txt")) {

            if (is == null) {
                throw new IllegalStateException("Password blocklist file not found");
            }

            new BufferedReader(new InputStreamReader(is))
                    .lines()
                    .map(String::trim)
                    .map(s -> s.toLowerCase(Locale.ROOT))
                    .filter(s -> !s.isEmpty())
                    .forEach(BLOCKED::add);

        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static boolean isBlocked(String password) {
        return BLOCKED.contains(password.toLowerCase(Locale.ROOT));
    }
}
