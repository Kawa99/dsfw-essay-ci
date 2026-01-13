package com.team_proj.dsfw_team_proj.auth;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Server-side password validation (length-first, blocklist, context checks).
 *
 * Notes:
 * - Avoids mandatory "complexity" rules (upper/lower/special) in favor of length + blocklist.
 * - Adds safer user-identifier checks.
 * - Adds better weak-pattern detection (ascending/descending sequences + common keyboard walks).
 * - Allows spaces and Unicode; does NOT trim user input (trimming would change the secret).
 *
 * IMPORTANT (outside this class):
 * - Make sure you hash passwords with Argon2id/bcrypt/scrypt via Spring Security PasswordEncoder.
 */
public final class PasswordValidationUtil {

    // Suggested default for user-chosen passwords.
    private static final int MIN_PASSWORD_LENGTH = 12;

    // Support long passphrases and prevent pathological inputs.
    private static final int MAX_PASSWORD_LENGTH = 128;

    // Reject if any single character repeats too many times in a row.
    private static final int MAX_CONSECUTIVE_REPEATS = 4;

    // Reject if we see a run of sequential characters of this length or more.
    private static final int SEQUENCE_RUN_LENGTH = 4;

    // If checking email local-part, only use it when it’s at least this long.
    private static final int MIN_IDENTIFIER_TOKEN_LENGTH = 4;

    // Optional: reject control characters (keep it simple; allow spaces, punctuation, unicode letters).
    private static final Pattern CONTROL_CHARS = Pattern.compile("\\p{Cntrl}");

    // Common keyboard walks to catch (case-insensitive). We also check reverse.
    private static final List<String> KEYBOARD_SEQUENCES = List.of(
            "qwertyuiop", "asdfghjkl", "zxcvbnm",
            "qwerty", "asdf", "zxcv",
            "1234567890"
    );

    private PasswordValidationUtil() {
        // Utility class
    }

    public static List<String> validatePassword(String password) {
        return validatePassword(password, null, null, null);
    }

    /**
     * Validates password with optional user context.
     *
     * @param password  password to validate (do NOT trim before passing)
     * @param email     optional email
     * @param firstName optional first name
     * @param lastName  optional last name
     * @return list of error messages (empty => valid)
     */
    public static List<String> validatePassword(String password, String email, String firstName, String lastName) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.isEmpty()) {
            errors.add("Password cannot be empty");
            return errors;
        }

        // Reject control characters (helps avoid invisible/odd login issues).
        if (CONTROL_CHARS.matcher(password).find()) {
            errors.add("Password contains control characters that are not allowed");
        }

        // Length checks
        if (password.length() < MIN_PASSWORD_LENGTH) {
            errors.add("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long (a passphrase is recommended)");
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            errors.add("Password cannot exceed " + MAX_PASSWORD_LENGTH + " characters");
        }

        // Common/breached check (case-insensitive)
        String pwLower = toLower(password);
        if (PasswordBlockList.isBlocked(pwLower)) {
            errors.add("This password is too common and easily guessed. Please choose a more unique password");
        }

        // Optional: warn about leading/trailing spaces (do not reject by default).
        // If you DO want to reject, change to errors.add(...)
        if (!password.equals(password.strip())) {
            errors.add("Password has leading or trailing spaces (this is allowed, but easy to mistype). Consider removing them");
        }

        // Context-based checks: email / names
        Set<String> forbiddenTokens = buildForbiddenTokens(email, firstName, lastName);

        // Only apply token checks if token is meaningfully long (avoid rejecting short tokens like "a", "al", etc.)
        for (String token : forbiddenTokens) {
            if (token.length() >= MIN_IDENTIFIER_TOKEN_LENGTH && pwLower.contains(token)) {
                errors.add("Password cannot contain personal information (e.g., your name or email/username)");
                break;
            }
        }

        // Weak-pattern checks
        if (hasSequentialRun(password, SEQUENCE_RUN_LENGTH)) {
            errors.add("Password contains too many sequential characters (e.g., '1234', 'abcd', '4321')");
        }
        if (hasConsecutiveRepeats(password, MAX_CONSECUTIVE_REPEATS)) {
            errors.add("Password contains too many repeated characters in a row (e.g., 'aaaa', '1111')");
        }
        if (containsKeyboardWalk(pwLower, SEQUENCE_RUN_LENGTH)) {
            errors.add("Password contains a common keyboard pattern (e.g., 'qwerty', 'asdf')");
        }

        return errors;
    }

    private static String toLower(String s) {
        return s.toLowerCase(Locale.ROOT);
    }

    private static Set<String> buildForbiddenTokens(String email, String firstName, String lastName) {
        Set<String> tokens = new HashSet<>();

        // Email local-part + split on separators
        if (email != null && !email.isBlank()) {
            String e = email.trim();
            int at = e.indexOf('@');
            if (at > 0) {
                String local = toLower(e.substring(0, at));
                tokens.add(local);
                tokens.addAll(splitIntoTokens(local));
            } else {
                // If email is malformed, still add tokenized string (best-effort)
                tokens.addAll(splitIntoTokens(toLower(e)));
            }
        }

        if (firstName != null && !firstName.isBlank()) {
            tokens.addAll(splitIntoTokens(toLower(firstName.trim())));
        }
        if (lastName != null && !lastName.isBlank()) {
            tokens.addAll(splitIntoTokens(toLower(lastName.trim())));
        }

        return tokens;
    }

    private static Set<String> splitIntoTokens(String s) {
        // Split on common separators; keep alphanumerics grouped.
        String[] parts = s.split("[^\\p{L}\\p{Nd}]+"); // letters or digits, unicode-aware
        Set<String> out = new HashSet<>();
        for (String p : parts) {
            if (p != null && !p.isBlank()) out.add(p);
        }
        return out;
    }

    /**
     * Detects ascending OR descending sequential runs of length >= runLength
     * using Unicode code points (works for ASCII letters/digits; “best effort” for other scripts).
     */
    private static boolean hasSequentialRun(String password, int runLength) {
        if (password.length() < runLength) return false;

        // Work on code points to be safer with Unicode
        int[] cps = password.codePoints().toArray();
        int asc = 1;
        int desc = 1;

        for (int i = 1; i < cps.length; i++) {
            if (cps[i] == cps[i - 1] + 1) {
                asc++;
            } else {
                asc = 1;
            }

            if (cps[i] == cps[i - 1] - 1) {
                desc++;
            } else {
                desc = 1;
            }

            if (asc >= runLength || desc >= runLength) {
                return true;
            }
        }

        return false;
    }

    private static boolean hasConsecutiveRepeats(String password, int maxRepeats) {
        if (password.length() < maxRepeats) return false;

        int[] cps = password.codePoints().toArray();
        int repeatCount = 1;

        for (int i = 1; i < cps.length; i++) {
            if (cps[i] == cps[i - 1]) {
                repeatCount++;
                if (repeatCount >= maxRepeats) return true;
            } else {
                repeatCount = 1;
            }
        }
        return false;
    }

    /**
     * Detects common keyboard walks like "qwerty", "asdf", "zxcv", including reverse.
     * Uses runLength to avoid rejecting tiny fragments.
     */
    private static boolean containsKeyboardWalk(String pwLower, int runLength) {
        for (String seq : KEYBOARD_SEQUENCES) {
            if (seq.length() >= runLength && pwLower.contains(seq.substring(0, runLength))) {
                // If password contains at least a runLength prefix, check more thoroughly:
                if (containsAnySubsequence(pwLower, seq, runLength)) return true;

                String rev = new StringBuilder(seq).reverse().toString();
                if (containsAnySubsequence(pwLower, rev, runLength)) return true;
            } else {
                // Still check normally
                if (containsAnySubsequence(pwLower, seq, runLength)) return true;

                String rev = new StringBuilder(seq).reverse().toString();
                if (containsAnySubsequence(pwLower, rev, runLength)) return true;
            }
        }
        return false;
    }

    private static boolean containsAnySubsequence(String haystack, String sequence, int minLen) {
        if (sequence.length() < minLen) return false;
        // Slide a window across the sequence and see if any chunk appears in the password
        for (int i = 0; i <= sequence.length() - minLen; i++) {
            String chunk = sequence.substring(i, i + minLen);
            if (haystack.contains(chunk)) return true;
        }
        return false;
    }
}
