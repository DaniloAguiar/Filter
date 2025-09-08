package br.com.filter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;

@Getter
@AllArgsConstructor
public enum SqlType {

    UUID(UUID.class, "?::UUID", "{0}"),
    DATE(LocalDate.class, "?::DATE", "DATE_TRUNC(''day'', {0})"),
    TIMESTAMP(LocalDateTime.class, "?::TIMESTAMP", "DATE_TRUNC(''hour'', {0})"),
    INSTANT(Instant.class, "?::TIMESTAMP", "DATE_TRUNC(''hour'', {0})"),
    TIME(LocalTime.class, "?::TIME", "CAST({0} AS time)"),
    BOOLEAN(Boolean.class, "?::BOOLEAN", "{0}"),
    BIGINT(BigInteger.class, "?::BIGINT", "{0}");


    private final Class<?> type;
    private final String sqlType;
    private final String sqlCastType;

    public static String placeholder(Class<?> type) {
        if (type == null) return "?";

        return Arrays.stream(values())
                .filter(p -> p.getType().isAssignableFrom(type))
                .findFirst()
                .map(SqlType::getSqlType)
                .orElse("?");
    }

    public static String castType(Class<?> type, String expr) {
        if (type == null) return expr;
        return Arrays.stream(values())
                .filter(p -> p.getType().isAssignableFrom(type))
                .findFirst()
                .map(sqlType -> MessageFormat.format(sqlType.getSqlCastType(), expr))
                .orElse(expr);
    }

}
