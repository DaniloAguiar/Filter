package br.com.filter.persistence;

import br.com.filter.domain.model.FilterGroup;
import br.com.filter.domain.enums.LogicalOperator;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class SqlWriter {

    /**
     * Constrói o SQL completo e a lista de parâmetros a partir de um FilterGroup.
     *
     * @param group O grupo de filtros a ser convertido em SQL
     * @return SqlResult contendo o SQL gerado e a lista de parâmetros
     */
    public SqlResult toSql(FilterGroup group) {
        List<Object> parameters = new ArrayList<>();
        String sql = buildGroup(group, LogicalOperator.AND, parameters);
        return new SqlResult(sql, parameters);
    }

    private String buildGroup(FilterGroup group, LogicalOperator groupLogical, List<Object> parameters) {
        if (group.getFilters().isEmpty() && group.getOrGroups().isEmpty()) return "";

        // SQL dos filtros do grupo
        String filtersSql = group.getFilters().stream()
                .map(f -> SqlTypeAdapter.applySQL(f, parameters))
                .collect(Collectors.joining(LogicalOperator.AND.getSql()));

        // SQL dos subgrupos OR
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

}