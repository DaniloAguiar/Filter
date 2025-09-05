package br.com.filter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Operator {

    //String, Number, Date
    EQ("==", " %s = %s "),                                        // == valor
    NOT_EQ("==", " %s <> %s "),                                   // <> valor

    //String
    LIKE("~=", " %s LIKE %%%s%% "),                                   // LIKE %valor%
    STARTS("^=", " %s LIKE %s%% "),                                 // LIKE valor%
    ENDS("$=", " %s LIKE %%%s "),                                   // LIKE %valor

    //Number, Date
    GT("=gt=", " %s > %s "),                                      // > valor
    GE("=ge=", " %s >= %s "),                                     // >= valor
    LT("=lt=", " %s < %s "),                                      // < valor
    LE("=le=", " %s <= %s "),                                     // <= valor
    BTW("=btw=", " %s BETWEEN %s AND %s "),      // BETWEEN valor1 AND valor2

    //LIST
    IN("=in=", " %s IN ( %s ) "),                                 // IN (value1, value2, value3, ...)
    OUT("=out=", " %s NOT IN ( %s )"),                            // NOT IN (value1, value2, value3, ...)

    //NULL
    ISNULL("=isnull=", " %s IS NULL "),                                        // IS NULL
    NOTNULL("=notnull=", " %s IS NOT NULL "),    // IS NOT NULL
    ;

    private final String rsql;
    private final String sql;
}
