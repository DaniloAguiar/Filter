package br.com.filter.domain.model;


import br.com.filter.domain.enums.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

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
        return FunctionValue.of(Function.CURRENT_DATE);
    }

    public static Value now() {
        return FunctionValue.of(Function.NOW);
    }

    public static Value age() {
        return FunctionValue.of(Function.AGE);
    }

}
