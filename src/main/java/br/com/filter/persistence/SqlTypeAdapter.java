package br.com.filter.persistence;

import br.com.filter.domain.enums.FieldValueCase;
import br.com.filter.domain.enums.Operator;
import br.com.filter.domain.enums.SqlType;
import br.com.filter.domain.model.Filter;
import br.com.filter.domain.model.ValueFunction;
import br.com.filter.domain.model.Value;
import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class SqlTypeAdapter {

    /**
     * Retorna o placeholder correto para o tipo do valor,
     * incluindo cast no SQL (PostgreSQL style)
     */
    public String buildPlaceholder(Value value) {
        if (value instanceof ValueFunction)
            return ((ValueFunction) value).getFunction().getSql(); // insere a função segura
        return SqlType.placeholder(value.getType());
    }

    /**
     * Aplica placeholder + tipo + case
     */
    public String buildPlaceholderCase(Value value, FieldValueCase fieldCase) {
        return applyCase(applyTypeCast(buildPlaceholder(value), value), fieldCase);
    }

    /**
     * Aplica field + tipo + case
     */
    public String buildField(Filter filter) {
        String typed = applyTypeCast(filter.getField(), filter.getValues().isEmpty() ? null : filter.getValues().get(0));
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
                return MessageFormat.format(op.getSql(), buildField(filter));

            case BTW: {
                Value v1 = filter.getValues().get(0);
                Value v2 = filter.getValues().get(1);

                String fieldExpr = buildField(filter);
                String placeholder1 = buildPlaceholderCase(v1, filter.getFieldValueCase());
                String placeholder2 = buildPlaceholderCase(v2, filter.getFieldValueCase());

                return MessageFormat.format(op.getSql(), fieldExpr, placeholder1, placeholder2);
            }

            case IN:
            case OUT: {
                List<String> placeholders = filter.getValues().stream()
                        .map(v -> buildPlaceholderCase(v, filter.getFieldValueCase()))
                        .collect(Collectors.toList());

                return MessageFormat.format(op.getSql(), buildField(filter), String.join(", ", placeholders));
            }

            default: {
                Value v = filter.getValues().get(0);
                return MessageFormat.format(op.getSql(), buildField(filter), buildPlaceholderCase(v, filter.getFieldValueCase()));
            }
        }
    }

    /**
     * Aplica o truncamento/cast adequado para o tipo do valor
     * Usado tanto para campo quanto para placeholder
     */
    public String applyTypeCast(String expr, Value value) {
        if (value == null) return expr;
        return SqlType.castType(value.getType(), expr);
    }

    /**
     * Combina FieldCase (UPPER/LOWER) com expressão SQL
     */
    public String applyCase(String expr, FieldValueCase fieldCase) {
        return MessageFormat.format(fieldCase.getSql(), expr);
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
                if (!(filter.getValues().get(0) instanceof ValueFunction))
                    parameters.add(filter.getValues().get(0).getValue());
                if (!(filter.getValues().get(1) instanceof ValueFunction))
                    parameters.add(filter.getValues().get(1).getValue());
                break;
            case IN:
            case OUT:
                filter.getValues().forEach(v -> {
                    if (!(v instanceof ValueFunction)) parameters.add(v.getValue());
                });
                break;
            default:
                if (!(filter.getValues().get(0) instanceof ValueFunction))
                    parameters.add(filter.getValues().get(0).getValue());
                break;
        }

        // Retorna a SQL pronta com placeholders
        return buildOperation(filter);
    }

}
