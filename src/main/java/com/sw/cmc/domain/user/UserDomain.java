package com.sw.cmc.domain.user;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import com.sw.cmc.common.advice.CmcException;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * packageName    : com.sw.cmc.domain.user
 * fileName       : UserDomain
 * author         : SungSuHan
 * date           : 2025-02-17
 * description    : 유저 도메인
 */
@Getter
@Setter
public class UserDomain {
    private long userNum;
    private String userId;
    private String password;
    private String refreshToken;
    private String username;
    private String email;
    private String userRole;

    private String resultMessage;


    private static final List<Character> LETTERS = new ArrayList<>();
    private static final List<Character> DIGITS = new ArrayList<>();
    private static final List<Character> ALL_CHARACTERS = new ArrayList<>();
    private static final SecureRandom RANDOM = new SecureRandom();

    static {
        IntStream.rangeClosed('A', 'Z').forEach(c -> LETTERS.add((char) c));
        IntStream.rangeClosed('a', 'z').forEach(c -> LETTERS.add((char) c));
        IntStream.rangeClosed('0', '9').forEach(c -> DIGITS.add((char) c));
        ALL_CHARACTERS.addAll(LETTERS);
        ALL_CHARACTERS.addAll(DIGITS);
    }

    // RandomPassword
    public static String createRandomPassword() {
        List<Character> result = new ArrayList<>();
        result.add(LETTERS.get(RANDOM.nextInt(LETTERS.size()))); // 알파벳 1개 추가
        result.add(DIGITS.get(RANDOM.nextInt(DIGITS.size())));  // 숫자 1개 추가

        // 나머지 문자 랜덤 추가
        IntStream.range(2, 11)
                .mapToObj(i -> ALL_CHARACTERS.get(RANDOM.nextInt(ALL_CHARACTERS.size())))
                .forEach(result::add);

        // 리스트를 섞고 문자열로 변환
        Collections.shuffle(result);
        StringBuilder sb = new StringBuilder();
        result.forEach(sb::append);

        return sb.toString();
    }

    // Validation
    public static void validateUserId(String userId) {
        if (StringUtils.length(userId) < 4 || StringUtils.length(userId) > 15) {
            throw new CmcException("USER003");
        }
    }
    public static void validatePassword(String password) {
        final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d).{6,}$";
        if (!Pattern.matches(PASSWORD_REGEX, password)) {
            throw new CmcException("USER004");
        }
    }
    public static void validateEmail(String email) {
        final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new CmcException("USER005");
        }
    }
    public static void validateUsername(String username) {
        if (StringUtils.length(username) < 2 || StringUtils.length(username) > 20) {
            throw new CmcException("USER006");
        }
    }
}
