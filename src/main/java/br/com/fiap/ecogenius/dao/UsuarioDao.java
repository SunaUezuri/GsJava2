package br.com.fiap.ecogenius.dao;

import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.model.Usuario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {

    private static final String INSERT_SQL = "INSERT INTO T_USUARIO VALUES(SQ_T_USUARIO.NEXTVAL, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM T_USUARIO ORDER BY id_usuario";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM T_USUARIO WHERE id_usuario = ? ORDER BY id_usuario";
    private static final String UPDATE_SQL = "UPDATE T_USUARIO SET nm_usuario = ?, ds_email = ?, ds_senha = ?, dt_nascimento = ? WHERE id_usuario = ?";
    private static final String DELETE_SQL = "DELETE FROM T_USUARIO WHERE id_usuario = ?";
    private static final String SELECT_BY_EMAIL_SQL = "SELECT * FROM T_USUARIO WHERE ds_email = ?";  // SQL para pesquisa por email

    private Connection connection;

    public UsuarioDao(Connection connection){
        this.connection = connection;
    }

    public void cadastrar(Usuario user) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(INSERT_SQL,
                new String[] {"id_usuario"});
        preencherStatementComUsuario(stmt, user);
        stmt.executeUpdate();
        //recuperar o id gerado pela sequence
        ResultSet resultSet = stmt.getGeneratedKeys();
        resultSet.next();
        user.setId(resultSet.getInt(1));
    }

    public List<Usuario> listar() throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL);
        List<Usuario> users = new ArrayList<>();
        while (rs.next())
            users.add(parseUsuario(rs));
        return users;
    }

    public Usuario pesquisarPorId(int id) throws IdNaoEncontradoException, SQLException{
        PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID_SQL);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            throw new IdNaoEncontradoException("Usuário não encontrado");

        return parseUsuario(rs);
    }

    public Usuario pesquisarPorEmail(String email) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(SELECT_BY_EMAIL_SQL);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return parseUsuario(rs);
        }
        return null;
    }


    public void atualizar(Usuario usuario) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL);
        preencherStatementComUsuario(stmt, usuario);
        stmt.setInt(5, usuario.getId());
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Usuário não encontrado");
    }

    public void remover(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(DELETE_SQL);
        stmt.setInt(1, id);
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Usuário não encontrado");
    }

    private void preencherStatementComUsuario(PreparedStatement stmt, Usuario usuario) throws SQLException {
        stmt.setString(1, usuario.getNome());
        stmt.setString(2, usuario.getEmail());
        stmt.setString(3, usuario.getSenha());
        stmt.setDate(4, Date.valueOf(usuario.getDataNascimento()));
    }

    private Usuario parseUsuario(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id_usuario");
        String nome = resultSet.getString("nm_usuario");
        String email = resultSet.getString("ds_email");
        String senha = resultSet.getString("ds_senha");
        LocalDate dataNascimento = resultSet.getDate("dt_nascimento").toLocalDate();

        return new Usuario(id, nome, email, senha, dataNascimento);
    }
}
