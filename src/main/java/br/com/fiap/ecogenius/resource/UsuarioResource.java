package br.com.fiap.ecogenius.resource;


import br.com.fiap.ecogenius.dao.IdeiasDao;
import br.com.fiap.ecogenius.dao.UsuarioDao;
import br.com.fiap.ecogenius.dto.ideia.DetalhesIdeiaDto;
import br.com.fiap.ecogenius.dto.user.AtualizaUsuarioDto;
import br.com.fiap.ecogenius.dto.user.CadastroUsuarioDto;
import br.com.fiap.ecogenius.dto.user.DetalhesUsuarioDto;
import br.com.fiap.ecogenius.exception.IdNaoEncontradoException;
import br.com.fiap.ecogenius.factory.ConnectionFactory;
import br.com.fiap.ecogenius.model.Usuario;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

//http://localhost:8080/usuarios
@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    private UsuarioDao usuarioDao;
    private ModelMapper modelMapper;
    private IdeiasDao ideiasDao;

    //Construtor
    public UsuarioResource() throws Exception{
        usuarioDao = new UsuarioDao(ConnectionFactory.getConnection());
        ideiasDao = new IdeiasDao(ConnectionFactory.getConnection());
        modelMapper = new ModelMapper();
    }

    @POST
    public Response cadastrar(@Valid CadastroUsuarioDto dto, @Context UriInfo uriInfo) throws SQLException {
        Usuario user = modelMapper.map(dto, Usuario.class);

        usuarioDao.cadastrar(user);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(String.valueOf(user.getId()));
        return Response.created(builder.build()).build();
    }

    @POST
    @Path("/login")
    public Response login(CadastroUsuarioDto dto) throws SQLException {
        Usuario usuario = usuarioDao.pesquisarPorEmail(dto.getEmail());

        if (usuario == null || !usuario.getSenha().equals(dto.getSenha())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\":\"Email ou senha inválidos\"}")
                    .build();
        }

        DetalhesUsuarioDto usuarioDto = modelMapper.map(usuario, DetalhesUsuarioDto.class);
        return Response.ok(usuarioDto).build();
    }

    @GET
    public List<DetalhesUsuarioDto> listar() throws SQLException {
        return usuarioDao.listar().stream()
                .map(j -> modelMapper.map(j, DetalhesUsuarioDto.class))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public DetalhesUsuarioDto pesquisar(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException{
        Usuario usuario = usuarioDao.pesquisarPorId(id);
        DetalhesUsuarioDto dto = modelMapper.map(usuario, DetalhesUsuarioDto.class);
        return dto;
    }

    @GET
    @Path("/{id}/ideias") //http://localhost:8080/usuarios/{id}/ideias
    public List<DetalhesIdeiaDto> listarIdeiasPorUsuario(@PathParam("id") int id) throws SQLException, IdNaoEncontradoException{
        return ideiasDao.listarPorUsuario(id).stream()
                .map(i -> modelMapper.map(i, DetalhesIdeiaDto.class))
                .collect(Collectors.toList());
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizaUsuarioDto dto) throws IdNaoEncontradoException, SQLException{
        Usuario usuario = modelMapper.map(dto, Usuario.class);
        usuario.setId(id);
        usuarioDao.atualizar(usuario);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") int id) throws IdNaoEncontradoException, SQLException {
        usuarioDao.remover(id);
        return Response.noContent().build();
    }
}
