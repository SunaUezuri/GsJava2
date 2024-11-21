package br.com.fiap.ecogenius.dao;

import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.model.Consumo;
import br.com.fiap.ecogenius.model.Politicas;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PoliticasDao {

    private static final String INSERT_SQL = "INSERT INTO T_POLITICAS VALUES(SQ_T_POLITICAS.NEXTVAL, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM T_POLITICAS ORDER BY id_politica";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM T_POLITICAS WHERE id_politica = ? ORDER BY id_politica";
    private static final String UPDATE_SQL = "UPDATE T_POLITICAS SET id_consumo = ?, nm_politica = ?, " +
            "nm_setor = ?, reducao_energia = ?, dt_inicio = ?, dt_termino = ? WHERE id_politica = ?";
    private static final String DELETE_SQL = "DELETE FROM T_CONSUMO_ENERGIA WHERE id_politica = ?";

    private Connection connection;

    public PoliticasDao(Connection connection){
        this.connection = connection;
    }

    public void cadastrar(Politicas politica) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(INSERT_SQL,
                new String[] {"id_politica"});

        preencherStatementComPoliticas(stmt, politica);
        stmt.executeUpdate();

        ResultSet resultSet = stmt.getGeneratedKeys();
        resultSet.next();
        politica.setId(resultSet.getInt(1));
    }

    public List<Politicas> listar() throws SQLException{
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(SELECT_ALL_SQL);

        List<Politicas> politicas = new ArrayList<>();
        while (rs.next())
            politicas.add(parsePolitica(rs));
        return politicas;
    }

    public Politicas pesquisaPorId(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID_SQL);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            throw new IdNaoEncontradoException("Não há políticas com este id");

        return parsePolitica(rs);
    }

    public void atualizar(Politicas politica) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL);
        preencherStatementComPoliticas(stmt, politica);
        stmt.setInt(7, politica.getId());
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Não há políticas com este id");
    }

    public void apagar(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(DELETE_SQL);
        stmt.setInt(1, id);
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Não há políticas com este id");
    }

    private void preencherStatementComPoliticas(PreparedStatement stmt, Politicas politica) throws SQLException{
        stmt.setInt(1, politica.getConsumo().getId());
        stmt.setString(2, politica.getNome());
        stmt.setString(3, politica.getSetor());
        stmt.setDouble(4, politica.getReducao());
        stmt.setDate(5, Date.valueOf(politica.getDataInicio()));
        if (politica.getDataTermino() != null) {
            stmt.setDate(6, Date.valueOf(politica.getDataTermino()));
        } else {
            stmt.setNull(6, Types.DATE);
        }

    }

    private Politicas parsePolitica(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id_politica");
        int idConsumo = resultSet.getInt("id_consumo");
        String nome = resultSet.getString("nm_politica");
        String setor = resultSet.getString("nm_setor");
        double reducao = resultSet.getDouble("reducao_energia");
        LocalDate dataInicio = resultSet.getDate("dt_inicio").toLocalDate();
        Date terminoSql = resultSet.getDate("dt_termino");
        LocalDate dataTermino = (terminoSql != null) ? terminoSql.toLocalDate() : null;

        return new Politicas(id, nome, setor, reducao, dataInicio, dataTermino, new Consumo(idConsumo));
    }
}
