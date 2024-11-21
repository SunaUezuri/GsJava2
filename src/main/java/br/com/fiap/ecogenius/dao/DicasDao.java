package br.com.fiap.ecogenius.dao;

import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.model.Dicas;
import br.com.fiap.ecogenius.model.Servico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DicasDao {

    private static final String INSERT_SQL = "INSERT INTO T_DICAS VALUES(SQ_T_DICAS.NEXTVAL, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM T_DICAS ORDER BY id_dica";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM T_DICAS WHERE id_dica = ? ORDER BY id_dica";
    private static final String UPDATE_SQL = "UPDATE T_DICAS SET id_servico = ?, nm_dica = ?, " +
            "ds_dica = ?, link_dica = ? WHERE id_dica = ?";
    private static final String DELETE_SQL = "DELETE FROM T_DICAS WHERE id_dica = ?";

    private Connection connection;

    public DicasDao(Connection connection){
        this.connection = connection;
    }

    public void cadastrar(Dicas dica) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(INSERT_SQL,
                new String[] {"id_dica"});

        preencherStatementComDicas(stmt, dica);
        stmt.executeUpdate();

        ResultSet resultSet = stmt.getGeneratedKeys();
        resultSet.next();
        dica.setId(resultSet.getInt(1));
    }

    public List<Dicas> listar() throws SQLException{
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(SELECT_ALL_SQL);

        List<Dicas> dicas = new ArrayList<>();
        while (rs.next())
            dicas.add(parseDicas(rs));
        return dicas;
    }

    public Dicas listarPorId(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID_SQL);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            throw new IdNaoEncontradoException("Dica não encontrada");

        return parseDicas(rs);
    }

    public void atualizar(Dicas dica) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL);
        preencherStatementComDicas(stmt, dica);
        stmt.setInt(5, dica.getId());
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Dica não encontrada");
    }

    public void deletar(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(DELETE_SQL);
        stmt.setInt(1, id);
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Dica não encontrada");
    }

    private void preencherStatementComDicas(PreparedStatement stmt, Dicas dica) throws SQLException{
        stmt.setInt(1, dica.getServico().getId());
        stmt.setString(2, dica.getNome());
        stmt.setString(3, dica.getDescricao());
        stmt.setString(4, dica.getLink());
    }

    private Dicas parseDicas(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id_dica");
        int idService = resultSet.getInt("id_servico");
        String nome = resultSet.getString("nm_dica");
        String descricao = resultSet.getString("ds_dica");
        String link = resultSet.getString("link_dica");

        return new Dicas(id, nome, descricao, link, new Servico(idService));
    }
}
