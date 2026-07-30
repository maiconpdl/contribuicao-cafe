package com.pdlmaicon.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RegrasNegocioEcception extends RuntimeException{

    // Define uma exception personalizada do tipo RuntimeException

    public RegrasNegocioEcception(String message){
        super(message);
    }
}
