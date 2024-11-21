package br.com.fiap.ecogenius.resource;

import br.com.fiap.ecogenius.dao.ComentariosDao;
import br.com.fiap.ecogenius.dto.comentario.AtualizaComentarioDto;
import br.com.fiap.ecogenius.dto.comentario.CadastroComentarioDto;
import br.com.fiap.ecogenius.dto.comentario.DetalhesComentarioDto;
import br.com.fiap.ecogenius.dto.ideia.AtualizaIdeiasDto;
import br.com.fiap.ecogenius.dto.ideia.DetalhesIdeiaDto;
import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.factory.ConnectionFactory;
import br.com.fiap.ecogenius.model.Comentarios;
import br.com.fiap.ecogenius.model.Ideias;
import br.com.fiap.ecogenius.model.Usuario;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

//http://localhost:8080/comentarios
@Path("/comentarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ComentariosResource {

    private ComentariosDao comentariosDao;
    private ModelMapper modelMapper;

    public ComentariosResource() throws Exception{
        comentariosDao = new ComentariosDao(ConnectionFactory.getConnection());
        modelMapper = new ModelMapper();
    }

    @POST
    public Response cadastrar(@Valid CadastroComentarioDto dto, @Context UriInfo uriInfo) throws SQLException {
        Comentarios comentario = modelMapper.map(dto, Comentarios.class);

        // Configura manualmente os objetos relacionados
        comentario.setUsuario(new Usuario(dto.getIdUser()));
        comentario.setIdeia(new Ideias(dto.getIdeiaId()));

        comentariosDao.cadastrar(comentario);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(comentario.getId()));
        return Response.created(builder.build()).build();
    }


    @GET
    public List<DetalhesComentarioDto> listar() throws SQLException {
        return comentariosDao.listar().stream()
                .map(j -> modelMapper.map(j, DetalhesComentarioDto.class))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public DetalhesComentarioDto pesquisar(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException {
        Comentarios comentarios = comentariosDao.pesquisaPorId(id);
        DetalhesComentarioDto dto = modelMapper.map(comentarios, DetalhesComentarioDto.class);
        return dto;
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizaComentarioDto dto) throws IdNaoEncontradoException, SQLException{
        Comentarios comentario = modelMapper.map(dto, Comentarios.class);
        comentario.setIdeia(new Ideias(dto.getIdeiaId()));
        comentario.setUsuario(new Usuario(dto.getIdUser()));
        comentario.setId(id);
        comentariosDao.atualizar(comentario);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") int id) throws IdNaoEncontradoException, SQLException{
        comentariosDao.apagar(id);
        return Response.noContent().build();
    }
}
