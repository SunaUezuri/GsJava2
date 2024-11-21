package br.com.fiap.ecogenius.resource;

import br.com.fiap.ecogenius.dao.ConsumoDao;
import br.com.fiap.ecogenius.dto.consumo.AtualizaConsumoDto;
import br.com.fiap.ecogenius.dto.consumo.CadastroConsumoDto;
import br.com.fiap.ecogenius.dto.consumo.DetalhesConsumoDto;
import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.factory.ConnectionFactory;
import br.com.fiap.ecogenius.model.Consumo;
import br.com.fiap.ecogenius.model.Servico;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

//http://localhost:8080/consumo
@Path("/consumo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConsumoResource {

    private ConsumoDao consumoDao;
    private ModelMapper modelMapper;

    public ConsumoResource() throws Exception{
        consumoDao = new ConsumoDao(ConnectionFactory.getConnection());
        modelMapper = new ModelMapper();
    }

    @POST
    public Response cadastrar(@Valid CadastroConsumoDto dto, @Context UriInfo uriInfo) throws SQLException {
        Consumo consumo = modelMapper.map(dto, Consumo.class);

        consumo.setServico(new Servico(dto.getIdServico()));

        consumoDao.cadastrar(consumo);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(consumo.getId()));
        return Response.created(builder.build()).build();
    }

    @GET
    public List<DetalhesConsumoDto> listar() throws SQLException {
        return consumoDao.listar().stream()
                .map(c -> modelMapper.map(c, DetalhesConsumoDto.class))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public DetalhesConsumoDto pesquisar(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException {
        Consumo consumo = consumoDao.pesquisaPorId(id);
        DetalhesConsumoDto dto = modelMapper.map(consumo, DetalhesConsumoDto.class);
        return dto;
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizaConsumoDto dto) throws IdNaoEncontradoException, SQLException{
        Consumo consumo = modelMapper.map(dto, Consumo.class);
        consumo.setServico(new Servico(dto.getIdServico()));
        consumo.setId(id);
        consumoDao.atualizar(consumo);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") int id) throws IdNaoEncontradoException, SQLException{
        consumoDao.apagar(id);
        return Response.noContent().build();
    }
}
