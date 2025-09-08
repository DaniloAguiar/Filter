package br.com.filter.domain.model;


import br.com.filter.domain.enums.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Value {

    private final Class<?> type;

    private final Object value;

    protected Value() {
        this.type = null;
        this.value = null;
    }

    // =========================
    // Métodos de fábrica
    // =========================

    public static Value of(Object value) {
        return new Value(value.getClass(), value);
    }

    public static Value currentDate() {
        return ValueFunction.of(LocalDate.class, Function.CURRENT_DATE);
    }

    public static Value now() {
        return ValueFunction.of(LocalDateTime.class, Function.NOW);
    }

    public static Value age() {
        return ValueFunction.of(Function.AGE);
    }

    public static Value genRandomUuid() {
        return ValueFunction.of(Function.GEN_RANDOM_UUID);
    }

}
