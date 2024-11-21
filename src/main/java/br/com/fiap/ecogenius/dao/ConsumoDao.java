package br.com.fiap.ecogenius.dao;

import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.model.Consumo;
import br.com.fiap.ecogenius.model.Servico;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ConsumoDao {

    private static final String INSERT_SQL = "INSERT INTO T_CONSUMO_ENERGIA VALUES(SQ_T_CONSUMO.NEXTVAL, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM T_CONSUMO_ENERGIA ORDER BY id_consumo";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM T_CONSUMO_ENERGIA WHERE id_consumo = ? ORDER BY id_consumo";
    private static final String UPDATE_SQL = "UPDATE T_CONSUMO_ENERGIA SET id_servico = ?, previsao_energia = ?, " +
            "condicao_climatica = ?, horario = ?, data_previsao = ? WHERE id_consumo = ?";
    private static final String DELETE_SQL = "DELETE FROM T_CONSUMO_ENERGIA WHERE id_consumo = ?";

    private Connection connection;

    public ConsumoDao(Connection connection){
        this.connection = connection;
    }

    public void cadastrar(Consumo consumo) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(INSERT_SQL,
                new String[] {"id_consumo"});

        preencherStatementComConsumo(stmt, consumo);
        stmt.executeUpdate();

        ResultSet resultSet = stmt.getGeneratedKeys();
        resultSet.next();
        consumo.setId(resultSet.getInt(1));
    }

    public List<Consumo> listar() throws SQLException{
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(SELECT_ALL_SQL);

        List<Consumo> consumos = new ArrayList<>();
        while (rs.next())
            consumos.add(parseConsumo(rs));
        return consumos;
    }

    public Consumo pesquisaPorId(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID_SQL);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            throw new IdNaoEncontradoException("Dados de consumo não encontrados");

        return parseConsumo(rs);
    }

    public void atualizar(Consumo consumo) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL);
        preencherStatementComConsumo(stmt, consumo);
        stmt.setInt(6, consumo.getId());
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Dados de consumo não encontrados");
    }

    public void apagar(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(DELETE_SQL);
        stmt.setInt(1, id);
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Dados de consumo não encontrados");
    }

    private void preencherStatementComConsumo(PreparedStatement stmt, Consumo consumo) throws SQLException{
        stmt.setInt(1, consumo.getServico().getId());
        stmt.setDouble(2, consumo.getPrevisao());
        stmt.setString(3, consumo.getCondicaoClimatica());
        stmt.setInt(4, consumo.getHorario());
        stmt.setDate(5, Date.valueOf(consumo.getDataPrevisao()));
    }

    private Consumo parseConsumo(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id_consumo");
        int idService = resultSet.getInt("id_servico");
        double previsao = resultSet.getDouble("previsao_energia");
        String condicao = resultSet.getString("condicao_climatica");
        int horario = resultSet.getInt("horario");
        LocalDate dataPrevisao = resultSet.getDate("data_previsao").toLocalDate();

        return new Consumo(id, previsao, condicao, horario, dataPrevisao, new Servico(idService));
    }

}
