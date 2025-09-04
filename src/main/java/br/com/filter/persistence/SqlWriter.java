package br.com.filter.persistence;

import br.com.filter.domain.model.Filter;
import br.com.filter.domain.model.FilterGroup;
import br.com.filter.domain.model.Value;
import br.com.filter.domain.enums.LogicalOperator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SqlWriter {

    public static SqlResult toSql(FilterGroup group) {
        List<Object> parameters = new ArrayList<>();
        String sql = buildGroup(group, LogicalOperator.AND, parameters);
        return new SqlResult(sql, parameters);
    }

    private static String buildGroup(FilterGroup group, LogicalOperator groupLogical, List<Object> parameters) {
        if ((group.getFilters().isEmpty()) && (group.getOrGroups().isEmpty())) return "";

        // SQL dos filtros
        String filtersSql = group.getFilters().stream()
                .map(f -> buildFilter(f, parameters))
                .collect(Collectors.joining(LogicalOperator.AND.getSql()));

        // SQL dos subgrupos
        String orGroupsSql = group.getOrGroups().stream()
                .map(subGroup -> buildGroup(subGroup, LogicalOperator.OR, parameters))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(LogicalOperator.OR.getSql()));

        // Combina filtros e subgrupos
        String combined = Stream.of(filtersSql, orGroupsSql)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining(groupLogical.getSql()));

        return "(" + combined + ")";
    }

    private static String buildFilter(Filter f, List<Object> parameters) {
        String fieldExpr = String.format(f.getFieldCase().getSql(), f.getField());

        switch (f.getOperator()) {
            case ISNULL, NOTNULL:
                return fieldExpr + f.getOperator().getSql();

            case BTW:
                parameters.add(convertValue(f.getValues().get(0)));
                parameters.add(convertValue(f.getValues().get(1)));
                return fieldExpr + String.format(f.getOperator().getSql(), "?", "?");

            case IN, OUT:
                String placeholders = f.getValues().stream()
                        .map(v -> {
                            parameters.add(convertValue(v));
                            return "?";
                        })
                        .collect(Collectors.joining(", "));
                return fieldExpr + String.format(f.getOperator().getSql(), placeholders);

            default:
                parameters.add(convertValue(f.getValues().getFirst()));
                return fieldExpr + String.format(f.getOperator().getSql(), "?");

        }
    }


    private static Object convertValue(Value value) {
        Object val = value.getValue();
        if (val instanceof java.time.LocalDate) {
            return java.sql.Date.valueOf((java.time.LocalDate) val);
        } else if (val instanceof java.time.LocalDateTime) {
            return java.sql.Timestamp.valueOf((java.time.LocalDateTime) val);
        } else if (val instanceof Enum<?>) {
            return ((Enum<?>) val).name(); // ou ordinal() se preferir
        }
        return val; // String, Number, Boolean etc.
    }

}