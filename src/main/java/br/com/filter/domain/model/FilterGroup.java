package br.com.filter.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FilterGroup {

    private final List<Filter> filters = new ArrayList<>();
    private final List<FilterGroup> orGroups = new ArrayList<>();

    // =========================
    // Métodos de fábrica
    // =========================
    public static FilterGroup empty() {
        return new FilterGroup();
    }

    public static FilterGroup of(Filter... filters) {
        FilterGroup group = new FilterGroup();
        group.addFilters(filters);
        return group;
    }

    public static FilterGroup withOrGroups(FilterGroup... groups) {
        FilterGroup group = new FilterGroup();
        group.addOrGroups(groups);
        return group;
    }

    // =========================
    // Métodos fluentes de conveniência
    // =========================
    public FilterGroup addFilter(Filter filter) {
        this.filters.add(filter);
        return this;
    }

    public FilterGroup addFilters(Filter... filters) {
        this.filters.addAll(Arrays.asList(filters));
        return this;
    }

    public FilterGroup addOrGroup(FilterGroup group) {
        this.orGroups.add(group);
        return this;
    }

    public FilterGroup addOrGroups(FilterGroup... groups) {
        this.orGroups.addAll(Arrays.asList(groups));
        return this;
    }
}

