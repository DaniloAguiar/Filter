package br.com.filter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FieldValueCase {
    NONE("{0}"),
    UPPER("UPPER({0})"),
    LOWER("LOWER({0})"),
    ;
    private final String sql;
}
