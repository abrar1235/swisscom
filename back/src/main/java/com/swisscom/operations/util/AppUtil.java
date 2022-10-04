package com.swisscom.operations.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.swisscom.operations.entity.MaintenanceDTO;
import com.swisscom.operations.enums.Status;
import com.swisscom.operations.exceptions.IllegalDateException;
import com.swisscom.operations.exceptions.IllegalRolesException;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.swisscom.operations.constant.Keys.*;

@Service
public class AppUtil {
    private static final String SECRET = "2193407b-8ef8-437a-8de7-5b045ad5f1c6";
    private static final String ISSUER = "SYSTEM";

    public String encryptPassword(String plainPassword) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = messageDigest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
        BigInteger bigInteger = new BigInteger(1, bytes);
        StringBuilder hexString = new StringBuilder(bigInteger.toString(16));
        while (hexString.length() < 32) hexString.insert(0, '0');
        return hexString.toString();
    }

    public List<Selection<?>> parseColumns(List<String> columns, Root<?> root) {
        return columns.stream().map(root::get).collect(Collectors.toList());
    }

    public void valueCheck(List<String> valueList, String valueToCheck, Supplier<? extends IllegalRolesException> message) throws IllegalRolesException {
        if (valueList.stream().noneMatch(x -> x.equals(valueToCheck))) throw message.get();
    }

    public void dateCheck(Date startDate, Date endDate, List<MaintenanceDTO> scheduledTimes) throws IllegalDateException {
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        scheduledTimes.forEach(times -> {
            calendar.setTime(times.getStartTime());
            Date startTime = calendar.getTime();
            calendar.setTime(times.getEndTime());
            Date endTime = calendar.getTime();
            times.setStartTime(startTime);
            times.setEndTime(endTime);
        });
        if (startDate.before(today) || endDate.before(today))
            throw new IllegalDateException("Start/End date cannot be less then today");
        else if (scheduledTimes.stream().anyMatch(time -> startDate.equals(time.getStartTime()) || startDate.before(time.getEndTime())))
            throw new IllegalDateException("Maintenance Already Scheduled for this window");
    }

    public String generateToken(String userId, String role) {
        Calendar calendar = Calendar.getInstance();
        Date issued = calendar.getTime();
        calendar.add(Calendar.HOUR_OF_DAY, 7);
        Date expired = calendar.getTime();
        return JWT.create()
                .withClaim(USER_ID, userId)
                .withClaim(ROLE, role)
                .withClaim(STATUS, Status.ACTIVE.toString())
                .withIssuer(ISSUER)
                .withIssuedAt(issued)
                .withExpiresAt(expired)
                .sign(Algorithm.HMAC256(SECRET));
    }

    public DecodedJWT parseToken(String token) {
        if (isTokenExpired(token)) return null;
        return JWT.decode(token);
    }

    private boolean isTokenExpired(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).withIssuer(ISSUER).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getExpiresAt().before(new Date());
    }
}
