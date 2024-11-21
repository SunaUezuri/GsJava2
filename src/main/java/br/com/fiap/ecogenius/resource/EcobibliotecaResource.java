package br.com.fiap.ecogenius.resource;

import br.com.fiap.ecogenius.dao.EcobibliotecaDao;
import br.com.fiap.ecogenius.dto.ecobiblioteca.AtualizaEcobibliotecaDto;
import br.com.fiap.ecogenius.dto.ecobiblioteca.CadastroEcobibliotecaDto;
import br.com.fiap.ecogenius.dto.ecobiblioteca.DetalhesEcobibliotecaDto;
import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.factory.ConnectionFactory;
import br.com.fiap.ecogenius.model.Ecobiblioteca;
import br.com.fiap.ecogenius.model.Servico;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

//http://localhost:8080/ecobiblioteca
@Path("/ecobiblioteca")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EcobibliotecaResource {

    private EcobibliotecaDao ecobibliotecaDao;
    private ModelMapper modelMapper;

    public EcobibliotecaResource() throws Exception{
        ecobibliotecaDao = new EcobibliotecaDao(ConnectionFactory.getConnection());
        modelMapper = new ModelMapper();
    }

    @POST
    public Response cadastrar(@Valid CadastroEcobibliotecaDto dto, @Context UriInfo uriInfo) throws SQLException{
        Ecobiblioteca eco = modelMapper.map(dto, Ecobiblioteca.class);

        eco.setServico(new Servico(dto.getIdServico()));
        ecobibliotecaDao.cadastrar(eco);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(eco.getId()));
        return Response.created(builder.build()).build();
    }

    @GET
    public List<DetalhesEcobibliotecaDto> listar() throws SQLException{
        return ecobibliotecaDao.listar().stream()
                .map(e -> modelMapper.map(e, DetalhesEcobibliotecaDto.class))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public DetalhesEcobibliotecaDto pesquisa(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException{
        Ecobiblioteca eco = ecobibliotecaDao.pesquisarPorId(id);
        DetalhesEcobibliotecaDto dto = modelMapper.map(eco, DetalhesEcobibliotecaDto.class);
        return dto;
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizaEcobibliotecaDto dto) throws SQLException, IdNaoEncontradoException{
        Ecobiblioteca eco = modelMapper.map(dto, Ecobiblioteca.class);
        eco.setServico(new Servico(dto.getIdServico()));
        eco.setId(id);
        ecobibliotecaDao.atualizar(eco);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException{
        ecobibliotecaDao.apagar(id);
        return Response.noContent().build();
    }
}
