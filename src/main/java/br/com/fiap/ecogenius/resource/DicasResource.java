package br.com.fiap.ecogenius.resource;

import br.com.fiap.ecogenius.dao.DicasDao;
import br.com.fiap.ecogenius.dto.dicas.AtualizaDicasDto;
import br.com.fiap.ecogenius.dto.dicas.CadastroDicasDto;
import br.com.fiap.ecogenius.dto.dicas.DetalhesDicasDto;
import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.factory.ConnectionFactory;
import br.com.fiap.ecogenius.model.Dicas;
import br.com.fiap.ecogenius.model.Servico;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

//http://localhost:8080/dicas
@Path("/dicas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DicasResource {

    private DicasDao dicasDao;
    private ModelMapper modelMapper;

    public DicasResource() throws Exception{
        dicasDao = new DicasDao(ConnectionFactory.getConnection());
        modelMapper = new ModelMapper();
    }

    @POST
    public Response cadastrar(@Valid CadastroDicasDto dto, @Context UriInfo uriInfo) throws SQLException {
        Dicas dica = modelMapper.map(dto, Dicas.class);

        dica.setServico(new Servico(dto.getIdServico()));

        dicasDao.cadastrar(dica);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(dica.getId()));
        return Response.created(builder.build()).build();
    }

    @GET
    public List<DetalhesDicasDto> listar() throws SQLException{
        return dicasDao.listar().stream()
                .map(d -> modelMapper.map(d, DetalhesDicasDto.class))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public DetalhesDicasDto pesquisa(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException{
        Dicas dica = dicasDao.listarPorId(id);
        DetalhesDicasDto dto = modelMapper.map(dica, DetalhesDicasDto.class);
        return dto;
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@Valid AtualizaDicasDto dto, @PathParam("id") int id) throws SQLException, IdNaoEncontradoException{
        Dicas dica = modelMapper.map(dto, Dicas.class);
        dica.setServico(new Servico(dto.getIdServico()));
        dica.setId(id);
        dicasDao.atualizar(dica);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException{
        dicasDao.deletar(id);
        return Response.noContent().build();
    }
}
