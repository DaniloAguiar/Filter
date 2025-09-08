package br.com.filter.persistence;

import br.com.filter.domain.enums.FieldValueCase;
import br.com.filter.domain.model.Filter;
import br.com.filter.domain.model.FilterGroup;
import br.com.filter.domain.model.Value;
import br.com.filter.persistence.domain.moodel.SqlResult;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SqlWriterTest {

    @Test
    public void testWhereClauseWithoutDeleted() {
        // Valores dos filtros
        Integer idEmpresa = 1;
        Integer idAgenda = 123;

        // Cria filtros
        Filter filterIdEmpresa = Filter.equals("agenda.id_empresa", Value.of(idEmpresa));
        Filter filterIdAgenda = Filter.equals("agenda.id", Value.of(idAgenda));
        Filter filterDeletedAt = Filter.isNull("agenda.deleted_at");

        // Cria grupo de filtros
        FilterGroup group = FilterGroup.of(filterIdEmpresa, filterIdAgenda);
        group.addFilter(filterDeletedAt);

        // Gera SQL
        SqlResult sqlResult = SqlWriter.toSql(group);

        // Verifica SQL
        assertEquals(
                "( agenda.id_empresa = ?  AND  agenda.id = ?  AND  agenda.deleted_at IS NULL )",
                sqlResult.getSql()
        );

        // Verifica parâmetros
        assertEquals(Arrays.asList(idEmpresa, idAgenda), sqlResult.getParameters());
    }

    @Test
    public void testWhereClauseWithoutDeletedOnlyMandatory() {
        // Valores dos filtros
        Integer idEmpresa = 2;
        Integer idAgenda = 456;

        // Cria filtros
        Filter filterIdEmpresa = Filter.equals("agenda.id_empresa", Value.of(idEmpresa));
        Filter filterIdAgenda = Filter.equals("agenda.id", Value.of(idAgenda));

        // Cria grupo de filtros sem deleted_at
        FilterGroup group = FilterGroup.of(filterIdEmpresa, filterIdAgenda);

        // Gera SQL
        SqlResult sqlResult = SqlWriter.toSql(group);

        // Verifica SQL
        assertEquals("( agenda.id_empresa = ?  AND  agenda.id = ? )", sqlResult.getSql());

        // Verifica parâmetros
        assertEquals(Arrays.asList(idEmpresa, idAgenda), sqlResult.getParameters());
    }

    @Test
    public void testWhereClauseWithUuidAndDeleted() {
        // Valores dos filtros
        Integer idEmpresa = 1;
        UUID myUuid = UUID.randomUUID();
        boolean aIncluirDeletado = false;

        // Cria filtros
        Filter filterIdEmpresa = Filter.equals("agenda.id_empresa", Value.of(idEmpresa));
        Filter filterMyUuid = Filter.equals("agenda.myuuid", Value.of(myUuid));
        Filter filterDeletedAt = Filter.isNull("agenda.deleted_at");

        // Cria grupo de filtros
        FilterGroup group = FilterGroup.of(filterIdEmpresa, filterMyUuid);
        if (!aIncluirDeletado) {
            group.addFilter(filterDeletedAt);
        }

        // Gera SQL
        SqlResult sqlResult = SqlWriter.toSql(group);

        // Verifica SQL
        String expectedSql = "( agenda.id_empresa = ?  AND  agenda.myuuid = ?::UUID  AND  agenda.deleted_at IS NULL )";
        assertEquals(expectedSql, sqlResult.getSql());

        // Verifica parâmetros
        assertEquals(Arrays.asList(idEmpresa, myUuid), sqlResult.getParameters());
    }

    @Test
    public void testWhereClauseWithUuidIncludeDeleted() {
        // Valores dos filtros
        Integer idEmpresa = 2;
        UUID myUuid = UUID.randomUUID();
        boolean aIncluirDeletado = true;

        // Cria filtros
        Filter filterIdEmpresa = Filter.equals("agenda.id_empresa", Value.of(idEmpresa));
        Filter filterMyUuid = Filter.equals("agenda.myuuid", Value.of(myUuid));

        // Cria grupo de filtros
        FilterGroup group = FilterGroup.of(filterIdEmpresa, filterMyUuid);

        // Gera SQL
        SqlResult sqlResult = SqlWriter.toSql(group);

        // Verifica SQL
        String expectedSql = "( agenda.id_empresa = ?  AND  agenda.myuuid = ?::UUID )";
        assertEquals(expectedSql, sqlResult.getSql());

        // Verifica parâmetros
        assertEquals(Arrays.asList(idEmpresa, myUuid), sqlResult.getParameters());
    }


    @Test
    public void testWhereClauseWithoutDeleted_WhenNotIncludingDeleted() {
        // Valores para os filtros
        Integer idEmpresa = 1;
        LocalDate dataInicio = LocalDate.of(2025, 9, 4);
        LocalDate dataFim = LocalDate.of(2025, 9, 10);
        boolean aIncluirDeletado = false;

        // Cria filtros baseados nas condições
        Filter filterIdEmpresa = Filter.equals("agenda.id_empresa", Value.of(idEmpresa));
        Filter filterDataInicio = Filter.greaterThanOrEqual("agenda.data", Value.of(dataInicio));
        Filter filterDataFim = Filter.lessThanOrEqual("agenda.data", Value.of(dataFim));
        Filter filterDeletedAt = Filter.isNull("agenda.deleted_at");

        // Cria grupo de filtros
        FilterGroup group = FilterGroup.of(filterIdEmpresa, filterDataInicio, filterDataFim);
        if (!aIncluirDeletado) {
            group.addFilter(filterDeletedAt);
        }

        // Gera SQL e parâmetros
        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado
        String expectedSql = "( agenda.id_empresa = ?  AND  DATE_TRUNC('day', agenda.data) >= DATE_TRUNC('day', ?::DATE)  "
                + "AND  DATE_TRUNC('day', agenda.data) <= DATE_TRUNC('day', ?::DATE)  AND  agenda.deleted_at IS NULL )";

        // Verifica SQL gerado
        assertEquals(expectedSql, sqlResult.getSql());

        // Verifica parâmetros
        List<Object> expectedParams = Arrays.asList(idEmpresa, dataInicio, dataFim);
        assertEquals(expectedParams, sqlResult.getParameters());
    }

    @Test
    public void testWhereClauseWithoutDeleted_WhenIncludingDeleted() {
        // Valores para os filtros
        Integer idEmpresa = 2;
        LocalDate dataInicio = LocalDate.of(2025, 9, 4);
        LocalDate dataFim = LocalDate.of(2025, 9, 10);
        boolean aIncluirDeletado = true;

        // Cria filtros baseados nas condições (sem filtro de deleted_at)
        Filter filterIdEmpresa = Filter.equals("agenda.id_empresa", Value.of(idEmpresa));
        Filter filterDataInicio = Filter.greaterThanOrEqual(
                "agenda.data", Value.of(dataInicio));
        Filter filterDataFim = Filter.lessThanOrEqual(
                "agenda.data", Value.of(dataFim));

        // Cria grupo de filtros
        FilterGroup group = FilterGroup.of(filterIdEmpresa, filterDataInicio, filterDataFim);

        // Gera SQL e parâmetros
        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado
        String expectedSql = "( agenda.id_empresa = ?  AND  DATE_TRUNC('day', agenda.data) >= DATE_TRUNC('day', ?::DATE)  "
                + "AND  DATE_TRUNC('day', agenda.data) <= DATE_TRUNC('day', ?::DATE) )";

        // Verifica SQL gerado
        assertEquals(expectedSql, sqlResult.getSql());

        // Verifica parâmetros
        List<Object> expectedParams = Arrays.asList(idEmpresa, dataInicio, dataFim);
        assertEquals(expectedParams, sqlResult.getParameters());
    }


    @Test
    public void testDateTruncWhereClause() {
        // Simula os filtros do WHERE
        FilterGroup group = FilterGroup.of(
                Filter.greaterThanOrEqual("age.data", Value.of(LocalDate.of(2025, 9, 4))),
                Filter.lessThanOrEqual("age.data", Value.of(LocalDate.of(2025, 9, 10)))
        );

        // Gera SQL e parâmetros
        SqlResult result = SqlWriter.toSql(group);

        // SQL esperado
        String expectedSql =
                "( DATE_TRUNC('day', age.data) >= DATE_TRUNC('day', ?::DATE)  AND  DATE_TRUNC('day', age.data) <= DATE_TRUNC('day', ?::DATE) )";

        // Verifica SQL gerado
        assertEquals(expectedSql, result.getSql());

        // Verifica parâmetros
        List<Object> params = result.getParameters();
        assertEquals(2, params.size());
        assertEquals(LocalDate.of(2025, 9, 4), params.get(0));
        assertEquals(LocalDate.of(2025, 9, 10), params.get(1));
    }

    @Test
    public void testWhereClauseForCaixaAndLancamento() {
        // Valores
        Integer idEmpresa = 1;
        Integer idCaixa = 100;
        LocalDate dataLancamento = LocalDate.of(2025, 9, 4);

        // Criação dos filtros
        Filter filterIdEmpresa = Filter.equals("caixa.id_empresa", Value.of(idEmpresa));
        Filter filterIdCaixa = Filter.equals("caixa.id", Value.of(idCaixa));
        Filter filterDataLancamento = Filter.equals("lanc.data", Value.of(dataLancamento));
        Filter filterTipo = Filter.equals("lanc.tipo", Value.of("SALDO"));
        Filter filterDeletedAt = Filter.isNull("lanc.deleted_at");

        // Grupo de filtros
        FilterGroup group = FilterGroup.of(filterIdEmpresa, filterIdCaixa, filterDataLancamento, filterTipo, filterDeletedAt);

        // Geração do SQL
        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado
        String expectedSql = "( caixa.id_empresa = ?  AND  caixa.id = ?  AND  lanc.data = ?::DATE  AND  lanc.tipo = ?  AND  lanc.deleted_at IS NULL )";

        // Validações
        assertEquals(expectedSql, sqlResult.getSql());
        assertEquals(Arrays.asList(idEmpresa, idCaixa, dataLancamento), sqlResult.getParameters());
    }

    @Test
    public void testWhereClauseWithUpperAndDeletedCondition() {
        // Valores dos filtros
        Integer idRepresentada = 10;
        String nomeCategoria = "Categoria Exemplo";

        // Criação dos filtros
        Filter filterIdRepresentada = Filter.equals("cat.id_representada", Value.of(idRepresentada));
        Filter filterNomeCategoria = Filter.equals("cat.nome", FieldValueCase.UPPER, Value.of(nomeCategoria.toUpperCase()));
        Filter filterDeletedAt = Filter.isNull("cat.deleted_at");

        // Cria grupo de filtros
        FilterGroup group = FilterGroup.of(filterIdRepresentada, filterNomeCategoria);
        group.addFilter(filterDeletedAt);

        // Geração do SQL
        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado
        String expectedSql = "( cat.id_representada = ?  AND  UPPER(cat.nome) = UPPER(?)  AND  cat.deleted_at IS NULL )";

        // Verifica SQL gerado
        assertEquals(expectedSql, sqlResult.getSql());

        // Verifica parâmetros
        List<Object> expectedParams = Arrays.asList(idRepresentada, nomeCategoria.toUpperCase());
        assertEquals(expectedParams, sqlResult.getParameters());
    }

    @Test
    public void testWhereClauseWithUpperIncludeDeleted() {
        // Valores dos filtros
        Integer idRepresentada = 15;
        String nomeCategoria = "Outra Categoria";
        boolean aIncluirDeletado = true;

        // Criação dos filtros
        Filter filterIdRepresentada = Filter.equals("cat.id_representada", Value.of(idRepresentada));
        Filter filterNomeCategoria = Filter.equals("cat.nome", FieldValueCase.UPPER, Value.of(nomeCategoria.toUpperCase()));

        // Cria grupo de filtros (não inclui deleted_at porque aIncluirDeletado = true)
        FilterGroup group = FilterGroup.of(filterIdRepresentada, filterNomeCategoria);

        // Geração do SQL
        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado
        String expectedSql = "( cat.id_representada = ?  AND  UPPER(cat.nome) = UPPER(?) )";

        // Verifica SQL gerado
        assertEquals(expectedSql, sqlResult.getSql());

        // Verifica parâmetros
        List<Object> expectedParams = Arrays.asList(idRepresentada, nomeCategoria.toUpperCase());
        assertEquals(expectedParams, sqlResult.getParameters());
    }


    @Test
    public void testWhereClauseForCliente() {
        // Valores para os filtros
        Integer idEmpresa = 1;
        Boolean defaultConsumidorFinal = true;

        // Cria os filtros
        Filter filterIdEmpresa = Filter.equals("cli.id_empresa", Value.of(idEmpresa));
        Filter filterDefaultConsumidorFinal = Filter.equals("cli.default_consumidor_final", Value.of(defaultConsumidorFinal));
        Filter filterDeletedAt = Filter.isNull("cli.deleted_at");

        // Cria grupo de filtros
        FilterGroup group = FilterGroup.of(filterIdEmpresa, filterDefaultConsumidorFinal, filterDeletedAt);

        // Gera o SQL e os parâmetros
        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado
        String expectedSql = "( cli.id_empresa = ?  AND  cli.default_consumidor_final = ?::BOOLEAN  AND  cli.deleted_at IS NULL )";

        // Verifica SQL gerado
        assertEquals(expectedSql, sqlResult.getSql());

        // Verifica parâmetros
        List<Object> expectedParams = Arrays.asList(idEmpresa, defaultConsumidorFinal);
        assertEquals(expectedParams, sqlResult.getParameters());
    }

    @Test
    public void testWhereClauseWithBigintCast() {
        // Valores para os filtros
        Integer idEmpresa = 1;
        Integer idCedente = 22;
        BigInteger nossoNumeroSequencial = new BigInteger("123456789");

        // Cria os filtros
        Filter filterIdEmpresa = Filter.equals("cob.id_empresa", Value.of(idEmpresa));
        Filter filterIdCedente = Filter.equals("cob.id_cedente", Value.of(idCedente));
        Filter filterNossoNumeroSeq = Filter.equals("cob.nosso_numero_sequencial", Value.of(nossoNumeroSequencial));

        // Cria o grupo de filtros
        FilterGroup group = FilterGroup.of(filterIdEmpresa, filterIdCedente, filterNossoNumeroSeq);

        // Gera o SQL e os parâmetros
        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado (com CAST para Bigint)
        String expectedSql = "( cob.id_empresa = ?  AND  cob.id_cedente = ?  AND  cob.nosso_numero_sequencial = ?::BIGINT )";

        // Verificações
        assertEquals(expectedSql, sqlResult.getSql());
        assertEquals(Arrays.asList(idEmpresa, idCedente, nossoNumeroSequencial), sqlResult.getParameters());
    }

    @Test
    public void testWhereClauseClienteVencimentoAtrasado() {
        // Valores para os filtros
        Integer idCliente = 7;
        Integer idEmpresa = 3;

        // Cria os filtros
        Filter filterIdCliente = Filter.equals("cob.id_cliente", Value.of(idCliente));
        Filter filterIdEmpresa = Filter.equals("cob.id_empresa", Value.of(idEmpresa));
        Filter filterPagamentoIsNull = Filter.isNull("cob.pagamento");
        Filter filterVencimentoAntesDeHoje = Filter.lessThan("cob.vencimento", Value.currentDate());
        Filter filterDeletedAtIsNull = Filter.isNull("cob.deleted_at");

        // Cria o grupo de filtros (na mesma ordem do WHERE de referência)
        FilterGroup group = FilterGroup.of(
                filterIdCliente,
                filterIdEmpresa,
                filterPagamentoIsNull,
                filterVencimentoAntesDeHoje,
                filterDeletedAtIsNull
        );

        // Gera o SQL e os parâmetros
        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado
        String expectedSql =
                "( cob.id_cliente = ?  AND  cob.id_empresa = ?  AND  cob.pagamento IS NULL" +
                        "  AND  DATE_TRUNC('day', cob.vencimento) < DATE_TRUNC('day', CURRENT_DATE)  AND  cob.deleted_at IS NULL )";

        // Verificações
        assertEquals(expectedSql, sqlResult.getSql());
        assertEquals(Arrays.asList(idCliente, idEmpresa), sqlResult.getParameters());
    }

    @Test
    public void testWhereClauseWithLowerCase() {
        // Valores para os filtros
        Integer idVendedor = 123;
        String nomeTabela = "TabelaExemplo";

        // Cria os filtros
        Filter filterIdVendedor = Filter.equals("id_vendedor", Value.of(idVendedor));
        Filter filterNomeTabela = Filter.equals("nome_tabela", FieldValueCase.LOWER, Value.of(nomeTabela.toLowerCase()));

        // Cria grupo de filtros
        FilterGroup group = FilterGroup.of(filterIdVendedor, filterNomeTabela);

        // Gera o SQL e os parâmetros
        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado
        String expectedSql = "( id_vendedor = ?  AND  LOWER(nome_tabela) = LOWER(?) )";

        // Verifica SQL gerado
        assertEquals(expectedSql, sqlResult.getSql());

        // Verifica parâmetros
        List<Object> expectedParams = Arrays.asList(idVendedor, nomeTabela.toLowerCase());
        assertEquals(expectedParams, sqlResult.getParameters());
    }

    @Test
    public void testWhereClauseWithMultipleAndConditions() {
        // Valores para os filtros
        Integer idEmpresa = 1;

        // Cria os filtros
        Filter filterIdEmpresa = Filter.equals("lv.id_empresa", Value.of(idEmpresa));
        Filter filterCatalogoGratuito = Filter.equals("licped.catalogo_gratuito", Value.of(Boolean.FALSE));

        Filter filterCategoriaApp = Filter.notEquals("licped.categoria_licenca", Value.of("app"));
        Filter filterCategoriaEcommerce = Filter.notEquals("licped.categoria_licenca", Value.of("ecommerce"));
        Filter filterCategoriaErp = Filter.notEquals("licped.categoria_licenca", Value.of("erp"));

        Filter filterDeletedAt = Filter.isNull("lv.deleted_at");

        // Cria o grupo de filtros
        FilterGroup group = FilterGroup.of(
                filterIdEmpresa,
                filterCatalogoGratuito,
                filterCategoriaApp,
                filterCategoriaEcommerce,
                filterCategoriaErp,
                filterDeletedAt
        );

        // Gera o SQL e os parâmetros
        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado
        String expectedSql =
                "( lv.id_empresa = ? " +
                        " AND  licped.catalogo_gratuito = ?::BOOLEAN " +
                        " AND  licped.categoria_licenca <> ? " +
                        " AND  licped.categoria_licenca <> ? " +
                        " AND  licped.categoria_licenca <> ? " +
                        " AND  lv.deleted_at IS NULL )";

        // Verifica o SQL gerado
        assertEquals(expectedSql, sqlResult.getSql());

        // Verifica os parâmetros passados
        List<Object> expectedParams = Arrays.asList(
                idEmpresa,
                Boolean.FALSE,
                "app",
                "ecommerce",
                "erp"
        );
        assertEquals(expectedParams, sqlResult.getParameters());
    }


    @Test
    public void testComplexWhereClauseWithDateTrunc() {
        // Valores para os filtros
        Integer idEmpresa = 1;

        // Cria os filtros
        Filter filterIdEmpresa = Filter.equals("lv.id_empresa", Value.of(idEmpresa));

        Filter filterCategoriaErp = Filter.notEquals("licped.categoria_licenca", Value.of("erp"));
        Filter filterCategoriaEcommerce = Filter.notEquals("licped.categoria_licenca", Value.of("ecommerce"));
        Filter filterLicencasApp = Filter.notEquals("licped.categoria_licenca", Value.of("app"));

        Filter filterDateTrunc = Filter.greaterThanOrEqual("lv.fim", Value.now());

        // Cria o grupo principal de filtros
        FilterGroup group = FilterGroup.of(filterIdEmpresa, filterCategoriaErp, filterCategoriaEcommerce, filterLicencasApp, filterDateTrunc);

        // Gera o SQL e os parâmetros
        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado
        String expectedSql =
                "( lv.id_empresa = ? " +
                        " AND  licped.categoria_licenca <> ? " +
                        " AND  licped.categoria_licenca <> ? " +
                        " AND  licped.categoria_licenca <> ? " +
                        " AND  DATE_TRUNC('hour', lv.fim) >= DATE_TRUNC('hour', NOW()) )";

        // Verifica o SQL gerado
        assertEquals(expectedSql, sqlResult.getSql());

        // Verifica os parâmetros esperados
        List<Object> expectedParams = Arrays.asList(idEmpresa, "erp", "ecommerce", "app");

        assertEquals(expectedParams, sqlResult.getParameters());
    }


    @Test
    public void testWhereClausePedidoItemGrid() {
        // WHERE pro.id_representada = ?
        //   AND item.deleted_at IS NULL
        //   AND (
        //         item.quantidade IS NOT NULL
        //         OR (item.quantidade IS NULL AND pro.deleted_at IS NULL)
        //       )

        Integer idRepresentada = 123;

        // Filtros base
        Filter filterIdRepresentada = Filter.equals("pro.id_representada", Value.of(idRepresentada));
        Filter filterItemDeletedNull = Filter.isNull("item.deleted_at");

        // Parte OR: ( item.quantidade IS NOT NULL ) OR ( item.quantidade IS NULL AND pro.deleted_at IS NULL )
        FilterGroup orRight = FilterGroup.of(Filter.isNull("item.quantidade"), Filter.isNull("pro.deleted_at"));
        FilterGroup orLeft = FilterGroup.of(Filter.notNull("item.quantidade")).addOrGroups(orRight);


        // Grupo principal: pro.id_representada = ? AND item.deleted_at IS NULL AND ( ... OR ... )
        // Observação: o SqlWriter atual concatena como "( filtros AND (left) OR (right) )"
        FilterGroup group = FilterGroup.of(filterIdRepresentada, filterItemDeletedNull)
                .addOrGroups(orLeft);

        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado de acordo com a implementação atual do SqlWriter
        String expectedSql = "( pro.id_representada = ?  AND  item.deleted_at IS NULL  AND" +
                " ( item.quantidade IS NOT NULL  OR ( item.quantidade IS NULL  AND  pro.deleted_at IS NULL )))";
        assertEquals(expectedSql, sqlResult.getSql());

        // Parâmetros
        assertEquals(Arrays.asList(idRepresentada), sqlResult.getParameters());
    }


    @Test
    public void testWhereClauseWithInListPedidos() {
        // WHERE cab.id_empresa = ?
        //   AND cab.id IN (?,?,?)

        Integer idEmpresa = 5;
        List<Integer> idsPedidos = Arrays.asList(10, 20, 30);

        // Filtros
        Filter filterIdEmpresa = Filter.equals("cab.id_empresa", Value.of(idEmpresa));
        Filter filterIdsPedidos = Filter.in(
                "cab.id",
                Value.of(idsPedidos.get(0)),
                Value.of(idsPedidos.get(1)),
                Value.of(idsPedidos.get(2))
        );

        // Grupo principal
        FilterGroup group = FilterGroup.of(filterIdEmpresa, filterIdsPedidos);

        // Geração do SQL
        SqlResult sqlResult = SqlWriter.toSql(group);

        // SQL esperado
        String expectedSql = "( cab.id_empresa = ?  AND  cab.id IN ( ?, ?, ? ) )";
        assertEquals(expectedSql, sqlResult.getSql());

        // Parâmetros esperados
        assertEquals(Arrays.asList(idEmpresa, 10, 20, 30), sqlResult.getParameters());
    }

    @Test
    public void testWhereClauseProdutoBuscaLivre() {
        // WHERE:
        // prod.id_representada = ?
        // AND prod.deleted_at IS NULL
        // AND ( UPPER(prod.codigo) LIKE UPPER(?) OR UPPER(prod.nome) LIKE UPPER(?) OR prod.codigo_barra LIKE ? OR UPPER(prod.referencia) LIKE UPPER(?) )

        Integer idRepresentada = 42;
        String termo = "abc"; // termo de busca

        // Filtros AND base
        Filter fIdRepresentada = Filter.equals("prod.id_representada", Value.of(idRepresentada));
        Filter fDeletedNull = Filter.isNull("prod.deleted_at");

        // Bloco OR:
        // UPPER(prod.codigo) LIKE %?%
        FilterGroup orCodigo = FilterGroup.of(Filter.contains("prod.codigo", FieldValueCase.UPPER, Value.of(termo.toUpperCase())));
        // UPPER(prod.nome) LIKE %?%
        FilterGroup orNome = FilterGroup.of(Filter.contains("prod.nome", FieldValueCase.UPPER, Value.of(termo.toUpperCase())));
        // prod.codigo_barra LIKE %?%
        FilterGroup orCodigoBarra = FilterGroup.of(Filter.contains("prod.codigo_barra", Value.of(termo)));
        // UPPER(prod.referencia) LIKE %?%
        FilterGroup orReferencia = FilterGroup.of(Filter.contains("prod.referencia", FieldValueCase.UPPER, Value.of(termo.toUpperCase())));

        // Grupo principal: AND entre base + OR entre as alternativas
        FilterGroup group = FilterGroup.of(fIdRepresentada, fDeletedNull)
                .addOrGroups(FilterGroup.empty().addOrGroups(orCodigo, orNome, orCodigoBarra, orReferencia));

        SqlResult sqlResult = SqlWriter.toSql(group);

        // Observação: o SqlWriter atual envolve cada subgrupo OR com parênteses, resultando em:
        // ( prod.id_representada = ?  AND  prod.deleted_at IS NULL  AND  ( UPPER(prod.codigo) LIKE %{?}% ) OR ( UPPER(prod.nome) LIKE %{?}% ) OR ( prod.codigo_barra LIKE %{?}% ) OR ( UPPER(prod.referencia) LIKE %{?}% ) )
        String expectedSql = "( prod.id_representada = ?  AND  prod.deleted_at IS NULL  AND (( UPPER(prod.codigo) LIKE %UPPER(?)% ) OR ( UPPER(prod.nome) LIKE %UPPER(?)% ) OR ( prod.codigo_barra LIKE %?% ) OR ( UPPER(prod.referencia) LIKE %UPPER(?)% )))";
        assertEquals(expectedSql, sqlResult.getSql());

        // Parâmetros na ordem em que aparecem
        assertEquals(Arrays.asList(idRepresentada, termo.toUpperCase(), termo.toUpperCase(), termo, termo.toUpperCase()), sqlResult.getParameters());
    }


}