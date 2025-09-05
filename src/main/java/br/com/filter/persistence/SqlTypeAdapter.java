package br.com.filter.persistence;

import br.com.filter.domain.enums.FieldValueCase;
import br.com.filter.domain.enums.Operator;
import br.com.filter.domain.model.Filter;
import br.com.filter.domain.model.FunctionValue;
import br.com.filter.domain.model.Value;
import lombok.experimental.UtilityClass;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@UtilityClass
public class SqlTypeAdapter {

    /**
     * Retorna o placeholder correto para o tipo do valor,
     * incluindo cast no SQL (PostgreSQL style)
     */
    public String buildPlaceholder(Value value) {
        if (value instanceof FunctionValue)
            return ((FunctionValue) value).getFunction().getSql(); // insere a função segura

        Object v = value.getValue();
        if (v instanceof UUID) return "?::UUID";
        if (v instanceof LocalDate) return "?::DATE";
        if (v instanceof LocalDateTime || v instanceof Instant) return "?::TOMESTAMP";
        if (v instanceof LocalTime) return "?::TIME";
        if (v instanceof Boolean) return "?::BOOLEAN";
        return "?";
    }

    /**
     * Aplica placeholder + tipo + case
     */
    public String buildPlaceholderCase(Value value, FieldValueCase fieldCase) {
        return applyCase(applyTypeCast(buildPlaceholder(value), value.getValue()), fieldCase);
    }

    /**
     * Aplica field + tipo + case
     */
    public String buildField(Filter filter) {
        String typed = applyTypeCast(filter.getField(), filter.getValues().isEmpty() ? null : filter.getValues().get(0).getValue());
        return applyCase(typed, filter.getFieldValueCase());
    }

    /**
     * aplica operação SQL adequada para o tipo do valor
     */
    public String buildOperation(Filter filter) {
        Operator op = filter.getOperator();

        switch (op) {
            case ISNULL:
            case NOTNULL:
                return String.format(op.getSql(), buildField(filter));

            case BTW: {
                Value v1 = filter.getValues().get(0);
                Value v2 = filter.getValues().get(1);

                String fieldExpr = buildField(filter);
                String placeholder1 = buildPlaceholderCase(v1, filter.getFieldValueCase());
                String placeholder2 = buildPlaceholderCase(v2, filter.getFieldValueCase());

                return String.format(op.getSql(), fieldExpr, placeholder1, placeholder2);
            }

            case IN:
            case OUT: {
                List<String> placeholders = filter.getValues().stream()
                        .map(v -> buildPlaceholderCase(v, filter.getFieldValueCase()))
                        .collect(Collectors.toList());

                return String.format(op.getSql(), buildField(filter), String.join(", ", placeholders));
            }

            default: {
                Value v = filter.getValues().get(0);
                return String.format(op.getSql(), buildField(filter), buildPlaceholderCase(v, filter.getFieldValueCase()));
            }
        }
    }

    /**
     * Aplica o truncamento/cast adequado para o tipo do valor
     * Usado tanto para campo quanto para placeholder
     */
    public String applyTypeCast(String expr, Object value) {
        if (value instanceof LocalDate) return "DATE_TRUNC('day', " + expr + ")";
        if (value instanceof LocalDateTime || value instanceof Instant) return "DATE_TRUNC('hour', " + expr + ")";
        if (value instanceof LocalTime) return "CAST(" + expr + " AS time)";
        return expr;
    }

    /**
     * Combina FieldCase (UPPER/LOWER) com expressão SQL
     */
    public String applyCase(String expr, FieldValueCase fieldCase) {
        return String.format(fieldCase.getSql(), expr);
    }

    /**
     * Gera o SQL adequado para o filtro informado
     */
    public String applySQL(Filter filter, List<Object> parameters) {
        Operator op = filter.getOperator();

        // Adiciona os valores no parameters
        switch (op) {
            case ISNULL:
            case NOTNULL:
                // Sem parâmetro
                break;
            case BTW:
                parameters.add(filter.getValues().get(0).getValue());
                parameters.add(filter.getValues().get(1).getValue());
                break;
            case IN:
            case OUT:
                filter.getValues().forEach(v -> parameters.add(v.getValue()));
                break;
            default:
                parameters.add(filter.getValues().get(0).getValue());
                break;
        }

        // Retorna a SQL pronta com placeholders
        return buildOperation(filter);
    }

}
