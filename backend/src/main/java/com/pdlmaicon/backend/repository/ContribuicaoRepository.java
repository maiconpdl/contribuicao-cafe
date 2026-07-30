package com.pdlmaicon.backend.repository;

import com.pdlmaicon.backend.dto.ContribuicaoDTO;
import com.pdlmaicon.backend.model.Cafe;
import com.pdlmaicon.backend.model.Funcionario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ContribuicaoRepository {

    private final JdbcTemplate jdbcTemplate;

    public ContribuicaoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // RowMapper foi usado para trazer cada linha do banco como um Funcionario ou Cafe,
    //  cada coluna da linha para um atributo da classe correspondente
    private final RowMapper<Funcionario> funcionarioRowMapper = new RowMapper<Funcionario>() {
        @Override
        public Funcionario mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Funcionario(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("cpf")
            );
        }
    };

    private final RowMapper<Cafe> cafeRowMapper = new RowMapper<Cafe>() {
        @Override
        public Cafe mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Cafe(
                    rs.getLong("id"),
                    rs.getLong("funcionarioId"),
                    rs.getString("item"),
                    rs.getDate("data").toLocalDate(),
                    rs.getBoolean("entregue")
            );
        }
    };


    private Long id;
    private Long funcionarioId;
    private String funcionarioNome;
    private String funcionarioCpf;
    private String item;
    private LocalDate data;
    private boolean entregue;

    private final RowMapper<ContribuicaoDTO> contribuicaoDTORowMapper = new RowMapper<ContribuicaoDTO>() {
        @Override
        public ContribuicaoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new ContribuicaoDTO(
                    rs.getLong("id"),
                    rs.getLong("funcionario_id"),
                    rs.getString("funcionario_nome"),
                    rs.getString("funcionario_cpf"),
                    rs.getString("item"),
                    rs.getDate("data").toLocalDate(),
                    rs.getBoolean("entregue")
            );
        }
    };


    //Faz uma consulta no banco, trazendo todas as linha em que a data é igual a data informada, 
    //com a data padrão a data atual (hoje) e retorna como uma lista de ContribuicaoDTO.
    public List<ContribuicaoDTO> buscaTodos(LocalDate data){
        String sql = "SELECT \n" +
                "                c.id AS item_id,\n" +
                "                f.id AS funcionario_id,\n" +
                "                f.nome AS funcionario_nome,\n" +
                "                f.cpf AS funcionario_cpf,\n" +
                "                c.item,\n" +
                "                c.data as data,\n" +
                "                c.entregue\n" +
                "            FROM cafe c\n" +
                "            INNER JOIN funcionarios f ON c.funcionario_id = f.id\n" +
                "WHERE c.data = ? " +
                "ORDER BY f.nome, c.item";


        return jdbcTemplate.query(sql, contribuicaoDTORowMapper, data);
    }

    // Busca na tabela funcionarios pela coluna nome, comparando com o nome vindo do formulário.
    // Utilizado para validações de duplicidade.
    public Optional<Funcionario> buscaPorNome(String nome){
        String sql = "SELECT id, nome, cpf FROM funcionarios WHERE LOWER(nome) = LOWER(?)";
        List<Funcionario> result = jdbcTemplate.query(sql, funcionarioRowMapper, nome);
        return result.stream().findFirst();
    }

    // Busca na tabela funcionarios pela coluna cpf, comparando com o cpf vindo do formulário.
    // Utilizado para validações de duplicidade.
    public Optional<Funcionario> buscaPorCpf(String cpf){
        String sql = "SELECT id, nome, cpf FROM funcionarios WHERE cpf = ?";
        List<Funcionario> result = jdbcTemplate.query(sql, funcionarioRowMapper, cpf);
        return result.stream().findFirst();
    }

    // Recebe os valores enviados pelo front e grava no banco os dados do funcionario.
    public int salvarFuncionario(String nome, String cpf){
        String sql = "INSERT INTO funcionarios (nome, cpf) VALUES (?, ?)";
        return jdbcTemplate.update(sql, nome, cpf);
    }

    
    // Recebe os valores enviados pelo front e grava no banco os dados do cafe.
    public int salvarCafe(Long funcionarioId, String item, LocalDate data, boolean entregue){
        String sql = "INSERT INTO cafe (funcionario_id, item, data, entregue) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, funcionarioId, item, data, entregue);
    }

    // Recebe os valores enviados pelo front e atualiza a linha do banco.
    public int editarCafe(Long id, ContribuicaoDTO contribuicao){
        String sql = "UPDATE cafe SET  data = ?, item = ?, entregue = ? WHERE id = ?";
        return jdbcTemplate.update(sql, contribuicao.getData(), contribuicao.getItem(), contribuicao.isEntregue(), id);
    }

    // Recebe um id como parâmetro e executa um delete no banco onde o id corresponde.
    public int deletarCafe(Long id){
        String sql = "DELETE FROM cafe WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    // Faz uma consulta no banco, se o item informado já existe para a mesma data informada.
    public boolean existeItemNaData(String item, LocalDate data) {
        String sql = "SELECT COUNT(*) FROM cafe WHERE LOWER(item) = LOWER(?) AND data = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, item.trim(), data);
        return count != null && count > 0;
    }


}
