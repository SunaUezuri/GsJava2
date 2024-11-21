package br.com.fiap.ecogenius.resource;

import br.com.fiap.ecogenius.dao.ServicoDao;
import br.com.fiap.ecogenius.dto.servico.AtualizaServicoDto;
import br.com.fiap.ecogenius.dto.servico.CadastroServicoDto;
import br.com.fiap.ecogenius.dto.servico.DetalhesServicoDto;
import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.factory.ConnectionFactory;
import br.com.fiap.ecogenius.model.Servico;
import br.com.fiap.ecogenius.model.Usuario;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

//http://localhost:8080/servicos
@Path("/servicos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServicoResource {

    private ServicoDao servicoDao;
    private ModelMapper modelMapper;

    public ServicoResource() throws Exception{
        servicoDao = new ServicoDao(ConnectionFactory.getConnection());
        modelMapper = new ModelMapper();
    }

    @POST
    public Response cadastrar(@Valid CadastroServicoDto dto, @Context UriInfo uriInfo) throws SQLException{
        Servico servico = modelMapper.map(dto, Servico.class);

        servico.setUsuario(new Usuario(dto.getIdUsuario()));

        servicoDao.cadastrar(servico);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(servico.getId()));
        return Response.created(builder.build()).build();
    }

    @GET
    public List<DetalhesServicoDto> listar() throws SQLException{
        return servicoDao.listar().stream()
                .map(s -> modelMapper.map(s, DetalhesServicoDto.class))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public DetalhesServicoDto pesquisar(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException{
        Servico servico = servicoDao.listarPorId(id);
        DetalhesServicoDto dto = modelMapper.map(servico, DetalhesServicoDto.class);
        return dto;
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizaServicoDto dto) throws SQLException, IdNaoEncontradoException{
        Servico servico = modelMapper.map(dto, Servico.class);
        servico.setUsuario(new Usuario(dto.getIdUsuario()));
        servico.setId(id);
        servicoDao.atualizar(servico);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException{
        servicoDao.apagar(id);
        return Response.noContent().build();
    }
}
