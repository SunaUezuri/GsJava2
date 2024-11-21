package br.com.fiap.ecogenius.resource;

import br.com.fiap.ecogenius.dao.IdeiasDao;
import br.com.fiap.ecogenius.dto.ideia.AtualizaIdeiasDto;
import br.com.fiap.ecogenius.dto.ideia.CadastroIdeiasDto;
import br.com.fiap.ecogenius.dto.ideia.DetalhesIdeiaDto;
import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.factory.ConnectionFactory;
import br.com.fiap.ecogenius.model.Ideias;
import br.com.fiap.ecogenius.model.Usuario;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

//http://localhost:8080/ideias
@Path("/ideias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IdeiasResource {

    private IdeiasDao ideiasDao;
    private ModelMapper modelMapper;

    public IdeiasResource() throws Exception {
        ideiasDao = new IdeiasDao(ConnectionFactory.getConnection());
        modelMapper = new ModelMapper();
    }

    @POST
    public Response cadastrar(@Valid CadastroIdeiasDto dto, @Context UriInfo uriInfo) throws SQLException {
        Ideias ideia = modelMapper.map(dto, Ideias.class);

        ideia.setUsuario(new Usuario(dto.getIdUser()));

        ideiasDao.cadastrar(ideia);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(ideia.getId()));
        return Response.created(builder.build()).build();
    }

    @GET
    public List<DetalhesIdeiaDto> listar() throws SQLException {
        return ideiasDao.listar().stream()
                .map(j -> modelMapper.map(j, DetalhesIdeiaDto.class))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public DetalhesIdeiaDto pesquisar(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException {
        Ideias ideias = ideiasDao.pesquisarPorId(id);
        DetalhesIdeiaDto dto = modelMapper.map(ideias, DetalhesIdeiaDto.class);
        return dto;
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizaIdeiasDto dto) throws IdNaoEncontradoException, SQLException{
        Ideias ideia = modelMapper.map(dto, Ideias.class);
        ideia.setUsuario(new Usuario(dto.getIdUser()));
        ideia.setId(id);
        ideiasDao.atualizar(ideia);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") int id) throws IdNaoEncontradoException, SQLException{
        ideiasDao.remover(id);
        return Response.noContent().build();
    }
}
