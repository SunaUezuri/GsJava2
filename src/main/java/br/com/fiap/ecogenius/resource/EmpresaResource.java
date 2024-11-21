package br.com.fiap.ecogenius.resource;

import br.com.fiap.ecogenius.dao.EmpresasDao;
import br.com.fiap.ecogenius.dto.empresas.AtualizaEmpresasDto;
import br.com.fiap.ecogenius.dto.empresas.CadastroEmpresasDto;
import br.com.fiap.ecogenius.dto.empresas.DetalhesEmpresasDto;
import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.exception.NomeNaoEncontradoException;
import br.com.fiap.ecogenius.factory.ConnectionFactory;
import br.com.fiap.ecogenius.model.Empresas;
import br.com.fiap.ecogenius.model.Servico;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.*;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

//http://localhost:8080/empresas
@Path("/empresas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpresaResource {

    private EmpresasDao empresasDao;
    private ModelMapper modelMapper;

    public EmpresaResource() throws Exception{
        empresasDao = new EmpresasDao(ConnectionFactory.getConnection());
        modelMapper = new ModelMapper();
    }

    @POST
    public Response cadastrar(@Valid CadastroEmpresasDto dto, @Context UriInfo uriInfo) throws SQLException {
        Empresas empresa = modelMapper.map(dto, Empresas.class);

        empresa.setServico(new Servico(dto.getIdServico()));

        empresasDao.cadastrar(empresa);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(empresa.getId()));
        return Response.created(builder.build()).build();
    }

    @GET
    @Path("/pesquisa") //http://localhost:8080/empresas/pesquisa?nome=xxx
    public List<DetalhesEmpresasDto> listaPorNome(@DefaultValue("") @QueryParam("nome") String nome) throws SQLException, NomeNaoEncontradoException {
        return empresasDao.pesquisarPorNome(nome).stream()
                .map(e -> modelMapper.map(e, DetalhesEmpresasDto.class))
                .collect(Collectors.toList());
    }

    @GET
    public List<DetalhesEmpresasDto> listar() throws SQLException {
        return empresasDao.listar().stream()
                .map(e -> modelMapper.map(e, DetalhesEmpresasDto.class))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public DetalhesEmpresasDto pesquisar(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException {
        Empresas empresa = empresasDao.listarPorId(id);
        DetalhesEmpresasDto dto = modelMapper.map(empresa, DetalhesEmpresasDto.class);
        return dto;
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizaEmpresasDto dto) throws IdNaoEncontradoException, SQLException {
        Empresas empresas = modelMapper.map(dto, Empresas.class);
        empresas.setId(id);
        empresasDao.atualizar(empresas);
        //Retornar o status 200 OK
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") int id) throws IdNaoEncontradoException, SQLException {
        empresasDao.apagar(id);
        return Response.noContent().build();
    }
}
