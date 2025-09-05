package br.com.filter.domain.model;


import br.com.filter.domain.enums.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class FunctionValue extends Value {

    private final Function function;

    // =========================
    // Métodos de fábrica
    // =========================

    public static FunctionValue of(Function function) {
        return new FunctionValue(function);
    }

}
