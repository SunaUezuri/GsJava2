package br.com.fiap.ecogenius.dao;


import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.model.Ideias;
import br.com.fiap.ecogenius.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IdeiasDao {

    private static final String INSERT_SQL = "INSERT INTO T_IDEIAS (id_ideia, id_usuario, nm_ideia, ds_descricao) VALUES (SQ_T_IDEIAS.NEXTVAL, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM T_IDEIAS ORDER BY id_ideia";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM T_IDEIAS WHERE id_ideia = ? ORDER BY id_ideia";
    private static final String SELECT_BY_USER_SQL = "SELECT * FROM T_IDEIAS WHERE id_usuario = ? ORDER BY id_ideia";
    private static final String UPDATE_SQL = "UPDATE T_IDEIAS SET id_usuario = ?, nm_ideia = ?, ds_descricao = ? WHERE id_ideia = ?";
    private static final String DELETE_SQL = "DELETE FROM T_IDEIAS WHERE id_ideia = ?";

    private Connection connection;

    public IdeiasDao(Connection connection){
        this.connection = connection;
    }

    public void cadastrar(Ideias ideias) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(INSERT_SQL,
                new String[] {"id_ideia"});

        preencherStatementComIdeias(stmt, ideias);
        stmt.executeUpdate();
        //Recuperar o id da sequence
        ResultSet resultSet = stmt.getGeneratedKeys();
        resultSet.next();
        ideias.setId(resultSet.getInt(1));
    }

    public List<Ideias> listar() throws SQLException{
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(SELECT_ALL_SQL);
        List<Ideias> ideias = new ArrayList<>();
        while (rs.next())
            ideias.add(parseIdeias(rs));
        return ideias;
    }

    public List<Ideias> listarPorUsuario(int id) throws SQLException, IdNaoEncontradoException {
        PreparedStatement stmt = connection.prepareStatement(SELECT_BY_USER_SQL);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        List<Ideias> ideias = new ArrayList<>();
        while (rs.next())
            ideias.add(parseIdeias(rs));

        if (ideias.isEmpty())
            throw new IdNaoEncontradoException("Ideia não encontrada");
        return ideias;
    }

    public Ideias pesquisarPorId(int id) throws IdNaoEncontradoException, SQLException {
        PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID_SQL);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            throw new IdNaoEncontradoException("Ideia não encontrado");
        return parseIdeias(rs);
    }

    public void atualizar(Ideias ideias) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL);
        preencherStatementComIdeias(stmt, ideias);
        stmt.setInt(4, ideias.getId());
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Ideia não encontrada.");
    }

    public void remover(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(DELETE_SQL);
        stmt.setInt(1, id);
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Ideia não encontrada");
    }

    private void preencherStatementComIdeias(PreparedStatement stmt, Ideias ideias) throws SQLException {
        stmt.setInt(1, ideias.getUsuario().getId());
        stmt.setString(2, ideias.getNome());
        stmt.setString(3, ideias.getDescricao());

    }

    private Ideias parseIdeias(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id_ideia");
        int idUsuario = resultSet.getInt("id_usuario");
        String nome = resultSet.getString("nm_ideia");
        String descricao = resultSet.getString("ds_descricao");

        return new Ideias(id, nome, descricao, new Usuario(idUsuario));
    }
}
