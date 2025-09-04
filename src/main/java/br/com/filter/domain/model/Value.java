package br.com.filter.domain.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Value {

    private final Class<?> type;

    private final Object value;

    // =========================
    // Métodos de fábrica
    // =========================

    public static Value of(Object value) {
        return new Value(value.getClass(), value);
    }

}
