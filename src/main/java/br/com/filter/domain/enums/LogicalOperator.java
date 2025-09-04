package br.com.filter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LogicalOperator {

    AND(";", " AND "),
    OR(",", " OR "),
    NOT("!", " NOT "),
    ;
    private final String rsql;
    private final String sql;

}
