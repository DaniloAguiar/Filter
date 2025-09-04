package br.com.filter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FieldCase {
    NONE("", ""),
    UPPER(":upper", "UPPER(%s)"),
    LOWER(":lower", "LOWER(%s)"),
    ;
    private final String rsql;
    private final String sql;
}
