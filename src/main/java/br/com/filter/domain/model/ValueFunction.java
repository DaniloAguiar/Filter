package br.com.filter.domain.model;


import br.com.filter.domain.enums.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class ValueFunction extends Value {

    private final Class<?> type;

    private final Function function;

    // =========================
    // Métodos de fábrica
    // =========================

    public static ValueFunction of(Class<?> type, Function function) {
        return new ValueFunction(type, function);
    }

}
