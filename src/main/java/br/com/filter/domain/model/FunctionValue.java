package br.com.filter.domain.model;


import br.com.filter.domain.enums.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
public class FunctionValue extends Value {

    private final Function function;

    public static FunctionValue of(Function function) {
        return new FunctionValue(function);
    }

}
