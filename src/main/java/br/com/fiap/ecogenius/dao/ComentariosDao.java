package br.com.fiap.ecogenius.dao;

import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.model.Comentarios;
import br.com.fiap.ecogenius.model.Ideias;
import br.com.fiap.ecogenius.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComentariosDao {

    private static final String INSERT_SQL = "INSERT INTO T_COMENTARIOS VALUES(SQ_T_COMENTARIOS.NEXTVAL, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM T_COMENTARIOS ORDER BY id_comentario";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM T_COMENTARIOS WHERE id_comentario = ? ORDER BY id_comentario";
    private static final String UPDATE_SQL = "UPDATE T_COMENTARIOS SET id_ideia = ?, id_usuario = ?, ds_comentario = ? WHERE id_comentario = ?";
    private static final String DELETE_SQL = "DELETE FROM T_COMENTARIOS WHERE id_comentario = ?";

    private Connection connection;

    public ComentariosDao(Connection connection){
        this.connection = connection;
    }

    public void cadastrar(Comentarios comentario) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(INSERT_SQL,
                new String[] {"id_comentario"});

        preencherStatementComComentario(stmt, comentario);
        stmt.executeUpdate();

        ResultSet resultSet = stmt.getGeneratedKeys();
        resultSet.next();
        comentario.setId(resultSet.getInt(1));
    }

    public List<Comentarios> listar() throws SQLException{
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(SELECT_ALL_SQL);
        List<Comentarios> comentarios = new ArrayList<>();
        while (rs.next())
            comentarios.add(parseComentario(rs));
        return comentarios;
    }

    public Comentarios pesquisaPorId(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID_SQL);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            throw new IdNaoEncontradoException("Comentário não encontrado");

        return parseComentario(rs);
    }

    public void atualizar(Comentarios comentario) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL);
        preencherStatementComComentario(stmt, comentario);
        stmt.setInt(4, comentario.getId());
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Comentário não encontrado");
    }

    public void apagar(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(DELETE_SQL);
        stmt.setInt(1, id);
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Comentário não encontrado");
    }

    private void preencherStatementComComentario(PreparedStatement stmt, Comentarios comentarios) throws SQLException{
        stmt.setInt(1, comentarios.getIdeia().getId());
        stmt.setInt(2, comentarios.getUsuario().getId());
        stmt.setString(3, comentarios.getComentario());
    }

    private Comentarios parseComentario(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id_comentario");
        int idIdeia = resultSet.getInt("id_ideia");
        int idUsuario = resultSet.getInt("id_usuario");
        String comentario = resultSet.getString("ds_comentario");

        return new Comentarios(id, comentario, new Ideias(idIdeia), new Usuario(idUsuario));
    }
}
