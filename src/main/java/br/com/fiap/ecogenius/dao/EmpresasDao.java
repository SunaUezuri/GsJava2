package br.com.fiap.ecogenius.dao;

import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.exception.NomeNaoEncontradoException;
import br.com.fiap.ecogenius.model.Empresas;
import br.com.fiap.ecogenius.model.Servico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresasDao {

    private static final String INSERT_SQL = "INSERT INTO T_EMPRESAS VALUES(SQ_T_EMPRESAS.NEXTVAL, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM T_EMPRESAS ORDER BY id_empresa";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM T_EMPRESAS WHERE id_empresa = ? ORDER BY id_empresa";
    private static final String SELECT_BY_NAME_SQL = "SELECT * FROM T_EMPRESAS WHERE upper(nm_empresa) like upper(?)";
    private static final String UPDATE_SQL = "UPDATE T_EMPRESAS SET id_servico = ?, nm_empresa = ?, " +
            "nr_cnpj = ?, ds_descricao = ? WHERE id_empresa = ?";
    private static final String DELETE_SQL = "DELETE FROM T_EMPRESAS WHERE id_empresa = ?";

    private Connection connection;

    public EmpresasDao(Connection connection){
        this.connection = connection;
    }

    public void cadastrar(Empresas empresa) throws SQLException{
        PreparedStatement stmt = connection.prepareStatement(INSERT_SQL,
                new String[] {"id_empresa"});

        preencherStatementComEmpresa(stmt, empresa);
        stmt.executeUpdate();

        ResultSet resultSet = stmt.getGeneratedKeys();
        resultSet.next();
        empresa.setId(resultSet.getInt(1));
    }

    public List<Empresas> listar() throws SQLException{
        Statement stm = connection.createStatement();
        ResultSet rs = stm.executeQuery(SELECT_ALL_SQL);

        List<Empresas> empresas = new ArrayList<>();
        while (rs.next())
            empresas.add(parseEmpresa(rs));
        return empresas;
    }

    //Método para listar por nome
    public List<Empresas> pesquisarPorNome(String nome) throws SQLException, NomeNaoEncontradoException{
        PreparedStatement stm = connection.prepareStatement(SELECT_BY_NAME_SQL);
        stm.setString(1, "%" + nome + "%");
        ResultSet resultSet = stm.executeQuery();
        List<Empresas> lista = new ArrayList<>();
        while (resultSet.next()){
            lista.add(parseEmpresa(resultSet));
        }

        if (!resultSet.next())
            throw new NomeNaoEncontradoException("Empresa não encontrada");

        return lista;
    }

    public Empresas listarPorId(int id) throws SQLException, IdNaoEncontradoException {
        PreparedStatement stmt = connection.prepareStatement(SELECT_BY_ID_SQL);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (!rs.next())
            throw new IdNaoEncontradoException("Empresa não encontrada");

        return parseEmpresa(rs);
    }

    public void atualizar(Empresas empresa) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(UPDATE_SQL);
        preencherStatementComEmpresa(stmt, empresa);
        stmt.setInt(5, empresa.getId());
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Empresa não encontrada");
    }

    public void apagar(int id) throws SQLException, IdNaoEncontradoException{
        PreparedStatement stmt = connection.prepareStatement(DELETE_SQL);
        stmt.setInt(1, id);
        if (stmt.executeUpdate() == 0)
            throw new IdNaoEncontradoException("Empresa não encontrada");
    }

    private void preencherStatementComEmpresa(PreparedStatement stmt, Empresas empresa) throws SQLException{
        stmt.setInt(1, empresa.getServico().getId());
        stmt.setString(2, empresa.getNome());
        stmt.setString(3, empresa.getCnpj());
        stmt.setString(4, empresa.getDescricao());
    }

    private Empresas parseEmpresa(ResultSet resultSet) throws SQLException{
        int id = resultSet.getInt("id_empresa");
        int idService = resultSet.getInt("id_servico");
        String nome = resultSet.getString("nm_empresa");
        String cnpj = resultSet.getString("nr_cnpj");
        String descricao = resultSet.getString("ds_descricao");

        return new Empresas(id, nome, cnpj, descricao, new Servico(idService));
    }
}
