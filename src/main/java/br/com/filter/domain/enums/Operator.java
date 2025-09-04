package br.com.filter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Operator {

    //String, Number, Date
    EQ("==", " = %s "),                     // == valor
    NOT_EQ("==", " <> %s "),                // <> valor

    //String
    LIKE("~=", " LIKE '%s' "),              // LIKE %valor%
    STARTS("^=", " LIKE '%s' "),            // LIKE valor%
    ENDS("$=", " LIKE '%s' "),              // LIKE %valor

    //Number, Date
    GT("=gt=", " > %s "),                   // > valor
    GE("=ge=", " >= %s "),                  // >= valor
    LT("=lt=", " < %s "),                   // < valor
    LE("=le=", " <= %s "),                  // <= valor
    BTW("=btw=", " BETWEEN %s AND %s "),    // BETWEEN valor1 AND valor2

    //LIST
    IN("=in=", " IN (%s) "),                // IN (value1, value2, value3, ...)
    OUT("=out=", " NOT IN (%s)"),           // NOT IN (value1, value2, value3, ...)

    //NULL
    ISNULL("=isnull=", " IS NULL "),        // IS NULL
    NOTNULL("=notnull=", " IS NOT NULL "),  // IS NOT NULL

    ;

    private final String rsql;
    private final String sql;

}
