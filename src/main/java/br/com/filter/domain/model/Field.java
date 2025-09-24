package br.com.filter.domain.model;

import lombok.Data;

import java.util.regex.Pattern;

@Data
public class Field {

    // Começa com letra, seguido de letras/números/underscore, permite múltiplos segmentos ".campo"
    private static final Pattern FIELD_PATTERN =
            Pattern.compile("^[A-Za-z][A-Za-z0-9_]*(?:\\.[A-Za-z][A-Za-z0-9_]*)*$");

    private String value;

    public Field(String value) {
        if (value == null) throw new IllegalArgumentException("O valor da propriedade 'value' não pode ser nulo");
        if (value.isEmpty()) throw new IllegalArgumentException("O valor da propriedade 'value' não pode ser vazio");
        if (!FIELD_PATTERN.matcher(value).matches()) throw new IllegalArgumentException();

        this.value = value;
    }
}
