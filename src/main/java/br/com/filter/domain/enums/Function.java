package br.com.filter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Function {
    CURRENT_DATE("CURRENT_DATE"),
    NOW("NOW()"),
    AGE("AGE(NOW())");

    private final String sql;

}
