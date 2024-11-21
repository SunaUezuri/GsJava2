package br.com.fiap.ecogenius.dao;

import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.model.Ecobiblioteca;
import br.com.fiap.ecogenius.model.Servico;
import br.com.fiap.ecogenius.model.TipoEnergia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EcobibliotecaDao {

    private static final String INSERT_SQL = "INSERT INTO T_ECOBIBLIOTECA VALUES(SQ_T_ECOBIBLIOTECA.NEXTVAL, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM T_ECOBIBLIOTECA ORDER BY id_energia";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM T_ECOBIBLIOTECA WHERE id_energia = ? ORDER BY id_energia";
    private static final String UPDATE_SQL = "UPDATE T_ECOBIBLIOTECA SET id_servico = ?, tipo_energia = ?, ds_energia = ? " +
            "WHERE id_energia = ?";
    private static final String DELETE_SQL = "DELETE FROM T_ECOBIBLIOTECA WHERE id_energia = ?";

    private Connection connection;

    public EcobibliotecaDao(Connection connection){
        this.connection = connection;
    }

    public void cadastrar(Ecobiblioteca eco) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(INSERT_SQL,
                new String[] {"id_energia"});

        preencherStatementComEcobiblioteca(stmt, eco);
        stmt.executeUpdate();

        ResultSet resultSet = stmt.getGeneratedKeys();
        resultSet.next();
        eco.setId(resultSet.getInt(1));
    }

    public List<Ecobiblioteca> listar() throws SQLException{
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(SELECT_ALL_SQL);

        List<Ecobiblioteca> ecobibliotecas = new ArrayList<>();
        while (rs.next())
            ecobibliotecas.add(parseEcobiblioteca(rs));
        return ecobibliotecas;
    }

    public Ecobiblioteca pesquisarPorId(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID_SQL);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            throw new IdNaoEncontradoException("Ecobiblioteca não encontrada");

        return parseEcobiblioteca(rs);
    }

    public void atualizar(Ecobiblioteca eco) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL);
        preencherStatementComEcobiblioteca(stmt, eco);
        stmt.setInt(4, eco.getId());
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Ecobiblioteca não encontrada");
    }

    public void apagar(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(DELETE_SQL);
        stmt.setInt(1, id);
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Ecobiblioteca não encontrada");
    }

    private void preencherStatementComEcobiblioteca(PreparedStatement stmt, Ecobiblioteca eco) throws SQLException{
        stmt.setInt(1, eco.getServico().getId());
        stmt.setString(2, eco.getEnergia().name());
        stmt.setString(3, eco.getDescricao());
    }

    private Ecobiblioteca parseEcobiblioteca(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id_energia");
        int idService = resultSet.getInt("id_servico");
        String tipoStr = resultSet.getString("tipo_energia");
        TipoEnergia tipoEnergia = tipoStr != null ? TipoEnergia.valueOf(tipoStr) : null;
        String descricao = resultSet.getString("ds_energia");

        return new Ecobiblioteca(id, tipoEnergia, descricao, new Servico(idService));
    }

}
