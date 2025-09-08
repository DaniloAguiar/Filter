package br.com.filter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Operator {

    //String, Number, Date
    EQ(" {0} = {1} "),                  // == valor
    NOT_EQ(" {0} <> {1} "),             // <> valor

    //String
    LIKE(" {0} LIKE %{1}% "),           // LIKE %valor%
    STARTS(" {0} LIKE {1}% "),          // LIKE valor%
    ENDS(" {0} LIKE %{1} "),            // LIKE %valor

    //Number, Date
    GT(" {0} > {1} "),                  // > valor
    GE(" {0} >= {1} "),                 // >= valor
    LT(" {0} < {1} "),                  // < valor
    LE(" {0} <= {1} "),                 // <= valor
    BTW(" {0} BETWEEN {1} AND {2} "),   // BETWEEN valor1 AND valor2

    //LIST
    IN(" {0} IN ( {1} ) "),             // IN (value1, value2, value3, ...)
    OUT(" {0} NOT IN ( {1} )"),         // NOT IN (value1, value2, value3, ...)

    //NULL
    ISNULL(" {0} IS NULL "),            // IS NULL
    NOTNULL(" {0} IS NOT NULL "),       // IS NOT NULL
    ;
    private final String sql;
}
