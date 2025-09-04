package br.com.filter.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SqlResult {

    private final String sql;
    private final List<Object> parameters;

}
