package br.com.filter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Function {
    CURRENT_DATE("CURRENT_DATE"),
    NOW("NOW()"),
    AGE("AGE(NOW())"),
    GEN_RANDOM_UUID("gen_random_uuid()"),
    ;

    private final String sql;

}
