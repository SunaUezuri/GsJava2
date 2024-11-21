package br.com.fiap.ecogenius.exception;

import br.com.fiap.ecogenius.dto.erro.MensagemErroDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e){
        //Validando o tipo de entrada
        if (e instanceof IdNaoEncontradoException){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new MensagemErroDto(e.getMessage()))
                    .build();
        }
        if (e instanceof NomeNaoEncontradoException){
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new MensagemErroDto(e.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new MensagemErroDto(e.getMessage()))
                .build();
    }
}
