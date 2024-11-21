package br.com.fiap.ecogenius.dao;

import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.model.Servico;
import br.com.fiap.ecogenius.model.TipoServico;
import br.com.fiap.ecogenius.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoDao {

    private static final String INSERT_SQL = "INSERT INTO T_SERVICOS VALUES(SQ_T_SERVICOS.NEXTVAL, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM T_SERVICOS ORDER BY id_servico";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM T_SERVICOS WHERE id_servico = ? ORDER BY id_servico";
    private static final String UPDATE_SQL = "UPDATE T_SERVICOS SET id_usuario = ?, nm_servico = ?, ds_servico = ?, " +
            "tipo_servico = ? WHERE id_servico = ?";
    private static final String DELETE_SQL = "DELETE FROM T_SERVICOS WHERE id_servico = ?";

    private Connection connection;

    public ServicoDao(Connection connection){
        this.connection = connection;
    }

    public void cadastrar(Servico servico) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(INSERT_SQL,
                new String[] {"id_servico"});

        preencherStatementComServico(stmt, servico);
        stmt.executeUpdate();

        ResultSet resultSet = stmt.getGeneratedKeys();
        resultSet.next();
        servico.setId(resultSet.getInt(1));

    }

    public List<Servico> listar() throws SQLException{
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(SELECT_ALL_SQL);

        List<Servico> servicos = new ArrayList<>();
        while (rs.next())
            servicos.add(parseServico(rs));
        return servicos;
    }

    public Servico listarPorId(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID_SQL);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            throw new IdNaoEncontradoException("Serviço não encontrado");

        return parseServico(rs);
    }

    public void atualizar(Servico servico) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL);
        preencherStatementComServico(stmt, servico);
        stmt.setInt(5, servico.getId());
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Serviço não encontrado");
    }

    public void apagar(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(DELETE_SQL);
        stmt.setInt(1, id);
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Serviço não encontrado");
    }

    private void preencherStatementComServico(PreparedStatement stmt, Servico servico) throws SQLException{
        stmt.setInt(1, servico.getUsuario().getId());
        stmt.setString(2, servico.getNome());
        stmt.setString(3, servico.getDescricao());
        stmt.setString(4, servico.getTipo().name());
    }

    private Servico parseServico(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id_servico");
        int idUser = resultSet.getInt("id_usuario");
        String nome = resultSet.getString("nm_servico");
        String descricao = resultSet.getString("ds_servico");
        String tipoStr = resultSet.getString("tipo_servico");
        TipoServico tipoServico = tipoStr != null ? TipoServico.valueOf(tipoStr) : null;

        return new Servico(id, nome, descricao, tipoServico, new Usuario(idUser));
    }
}
