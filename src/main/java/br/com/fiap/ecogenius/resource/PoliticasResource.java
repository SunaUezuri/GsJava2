package br.com.fiap.ecogenius.resource;

import br.com.fiap.ecogenius.dao.PoliticasDao;
import br.com.fiap.ecogenius.dto.politicas.AtualizaPoliticasDto;
import br.com.fiap.ecogenius.dto.politicas.CadastroPoliticasDto;
import br.com.fiap.ecogenius.dto.politicas.DetalhesPoliticasDto;
import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.factory.ConnectionFactory;
import br.com.fiap.ecogenius.model.Consumo;
import br.com.fiap.ecogenius.model.Politicas;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

//http://localhost:8080/politicas
@Path("/politicas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PoliticasResource {

    private PoliticasDao politicasDao;
    private ModelMapper modelMapper;

    public PoliticasResource() throws Exception{
        politicasDao = new PoliticasDao(ConnectionFactory.getConnection());
        modelMapper = new ModelMapper();
    }

    @POST
    public Response cadastrar(@Valid CadastroPoliticasDto dto, @Context UriInfo uriInfo) throws SQLException{
        Politicas politica = modelMapper.map(dto, Politicas.class);

        politica.setConsumo(new Consumo(dto.getIdConsumo()));

        politicasDao.cadastrar(politica);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(politica.getId()));
        return Response.created(builder.build()).build();
    }

    @GET
    public List<DetalhesPoliticasDto> listar() throws SQLException {
        return politicasDao.listar().stream()
                .map(p -> modelMapper.map(p, DetalhesPoliticasDto.class))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public DetalhesPoliticasDto pesquisar(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException {
        Politicas politica = politicasDao.pesquisaPorId(id);
        DetalhesPoliticasDto dto = modelMapper.map(politica, DetalhesPoliticasDto.class);
        return dto;
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizaPoliticasDto dto) throws IdNaoEncontradoException, SQLException{
        Politicas politica = modelMapper.map(dto, Politicas.class);
        politica.setConsumo(new Consumo(dto.getIdConsumo()));
        politica.setId(id);
        politicasDao.atualizar(politica);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") int id) throws IdNaoEncontradoException, SQLException{
        politicasDao.apagar(id);
        return Response.noContent().build();
    }
}
