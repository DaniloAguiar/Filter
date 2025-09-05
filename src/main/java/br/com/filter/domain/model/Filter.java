package br.com.filter.domain.model;

import br.com.filter.domain.enums.FieldValueCase;
import br.com.filter.domain.enums.Operator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Filter {

    private String field;
    private FieldValueCase fieldValueCase;
    private Operator operator;
    private List<Value> values;

    // =========================
    // Métodos de fábrica
    // =========================

    public static Filter equals(String field, Value value) {
        return equals(field, FieldValueCase.NONE, value);
    }

    public static Filter equals(String field, FieldValueCase fieldValueCase, Value value) {
        return new Filter(field, fieldValueCase, Operator.EQ, Collections.singletonList(value));
    }


    public static Filter notEquals(String field, Value value) {
        return equals(field, FieldValueCase.NONE, value);
    }

    public static Filter notEquals(String field, FieldValueCase fieldValueCase, Value value) {
        return new Filter(field, fieldValueCase, Operator.NOT_EQ, Collections.singletonList(value));
    }


    public static Filter contains(String field, Value value) {
        return contains(field, FieldValueCase.NONE, value);
    }

    public static Filter contains(String field, FieldValueCase fieldValueCase, Value value) {
        return new Filter(field, fieldValueCase, Operator.LIKE, Collections.singletonList(value));
    }


    public static Filter startsWith(String field, Value value) {
        return startsWith(field, FieldValueCase.NONE, value);
    }

    public static Filter startsWith(String field, FieldValueCase fieldValueCase, Value value) {
        return new Filter(field, fieldValueCase, Operator.STARTS, Collections.singletonList(value));
    }


    public static Filter endsWith(String field, Value value) {
        return endsWith(field, FieldValueCase.NONE, value);
    }

    public static Filter endsWith(String field, FieldValueCase fieldValueCase, Value value) {
        return new Filter(field, fieldValueCase, Operator.ENDS, Collections.singletonList(value));
    }


    public static Filter greaterThan(String field, Value value) {
        return new Filter(field, FieldValueCase.NONE, Operator.GT, Collections.singletonList(value));
    }

    public static Filter greaterThanOrEqual(String field, Value value) {
        return new Filter(field, FieldValueCase.NONE, Operator.GE, Collections.singletonList(value));
    }


    public static Filter lessThan(String field, Value value) {
        return new Filter(field, FieldValueCase.NONE, Operator.LT, Collections.singletonList(value));
    }

    public static Filter lessThanOrEqual(String field, Value value) {
        return new Filter(field, FieldValueCase.NONE, Operator.LE, Collections.singletonList(value));
    }


    public static Filter between(String field, Value value1, Value value2) {
        return new Filter(field, FieldValueCase.NONE, Operator.BTW, Arrays.asList(value1, value2));
    }


    public static Filter in(String field, Value... values) {
        return new Filter(field, FieldValueCase.NONE, Operator.IN, Arrays.asList(values));
    }

    public static Filter notIn(String field, Value... values) {
        return new Filter(field, FieldValueCase.NONE, Operator.OUT, Arrays.asList(values));
    }


    public static Filter isNull(String field) {
        return new Filter(field, FieldValueCase.NONE, Operator.ISNULL, Collections.emptyList());
    }

    public static Filter notNull(String field) {
        return new Filter(field, FieldValueCase.NONE, Operator.NOTNULL, Collections.emptyList());
    }

}
