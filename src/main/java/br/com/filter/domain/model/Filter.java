package br.com.filter.domain.model;

import br.com.filter.domain.enums.FieldCase;
import br.com.filter.domain.enums.Operator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Filter {

    private String field;
    private FieldCase fieldCase;
    private Operator operator;
    private List<Value> values;

    // =========================
    // Métodos de fábrica
    // =========================

    public static Filter equals(String field, Value value) {
        return equals(field, FieldCase.NONE, value);
    }

    public static Filter equals(String field, FieldCase fieldCase, Value value) {
        return new Filter(field, fieldCase, Operator.EQ, List.of(value));
    }

    public static Filter notEquals(String field, Value value) {
        return equals(field, FieldCase.NONE, value);
    }

    public static Filter notEquals(String field, FieldCase fieldCase, Value value) {
        return new Filter(field, fieldCase, Operator.NOT_EQ, List.of(value));
    }


    public static Filter contains(String field, Value value) {
        return contains(field, FieldCase.NONE, value);
    }

    public static Filter contains(String field, FieldCase fieldCase, Value value) {
        return new Filter(field, fieldCase, Operator.LIKE, List.of(value));
    }

    public static Filter startsWith(String field, Value value) {
        return startsWith(field, FieldCase.NONE, value);
    }

    public static Filter startsWith(String field, FieldCase fieldCase, Value value) {
        return new Filter(field, fieldCase, Operator.STARTS, List.of(value));
    }

    public static Filter endsWith(String field, Value value) {
        return endsWith(field, FieldCase.NONE, value);
    }

    public static Filter endsWith(String field, FieldCase fieldCase, Value value) {
        return new Filter(field, fieldCase, Operator.ENDS, List.of(value));
    }

    public static Filter greaterThan(String field, Value value) {
        return new Filter(field, FieldCase.NONE, Operator.GT, List.of(value));
    }

    public static Filter greaterThanOrEqual(String field, Value value) {
        return new Filter(field, FieldCase.NONE, Operator.GE, List.of(value));
    }

    public static Filter lessThan(String field, Value value) {
        return new Filter(field, FieldCase.NONE, Operator.LT, List.of(value));
    }

    public static Filter lessThanOrEqual(String field, Value value) {
        return new Filter(field, FieldCase.NONE, Operator.LE, List.of(value));
    }

    public static Filter between(String field, Value value1, Value value2) {
        return new Filter(field, FieldCase.NONE, Operator.BTW, List.of(value1, value2));
    }

    public static Filter in(String field, Value... values) {
        return new Filter(field, FieldCase.NONE, Operator.IN, Arrays.asList(values));
    }

    public static Filter notIn(String field, Value... values) {
        return new Filter(field, FieldCase.NONE, Operator.OUT, Arrays.asList(values));
    }

    public static Filter isNull(String field) {
        return new Filter(field, FieldCase.NONE, Operator.ISNULL, List.of());
    }

    public static Filter notNull(String field) {
        return new Filter(field, FieldCase.NONE, Operator.NOTNULL, List.of());
    }

}
