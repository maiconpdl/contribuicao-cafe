package com.pdlmaicon.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pdlmaicon.backend.dto.ContribuicaoDTO;
import com.pdlmaicon.backend.model.Cafe;
import com.pdlmaicon.backend.model.Funcionario;
import com.pdlmaicon.backend.repository.ContribuicaoRepository;
import com.pdlmaicon.backend.service.ContribuicaoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/contribuicao")
@Tag(name = "Contribuições", description = "Endpoints para gerenciamento do café da manhã!")
public class ContribuicaoController {

    private final ContribuicaoRepository contribuicaoRepository;
    private final ContribuicaoService contribuicaoService;

    public ContribuicaoController(ContribuicaoRepository contribuicaoRepository, ContribuicaoService contribuicaoService) {
        this.contribuicaoRepository = contribuicaoRepository;
        this.contribuicaoService = contribuicaoService;
    }

    @Operation(
        summary = "Listar contribuições por data", 
        description = "Retorna os registros do banco correspondentes à data informada. Caso nenhuma data seja fornecida, assume a data atual."
    )
    @GetMapping
    // lista todos os registros que tem a data atual (hoje) ou registros na data informada no form
    public List<ContribuicaoDTO> listar(
            @RequestParam(required = false) LocalDate data
    ){
        LocalDate dataFiltro = (data != null) ? data : LocalDate.now();
        return contribuicaoService.listar(dataFiltro);
    }


    @Operation(
        summary = "Cadastrar nova contribuição", 
        description = "Inclui uma nova colaboração para o café da manhã (permite registrar 1 item por vez e valida regras de duplicidade)."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // Salva os dados informados no front no banco de dados
    public void salvarContribuicao(@RequestBody ContribuicaoDTO contribuicao){
        Funcionario funcionario = new Funcionario();
        Cafe cafe = new Cafe();

        funcionario.setNome(contribuicao.getFuncionarioNome());
        funcionario.setCpf(contribuicao.getFuncionarioCpf());
        cafe.setItem(contribuicao.getItem());
        cafe.setData(contribuicao.getData());
        cafe.setEntregue(contribuicao.isEntregue());

        contribuicaoService.validaDuplicata(funcionario, cafe);
    }


    @Operation(
        summary = "Editar contribuição", 
        description = "Atualiza os dados de uma contribuição existente (como item, data ou status de entrega) com base no ID informado."
    )
    @PutMapping("/{id}")
    // Salva os dados do form, atualizando a linha do banco com mesmo id
    public ResponseEntity<Void> editar(@PathVariable Long id, @RequestBody ContribuicaoDTO contribuicao){
        int linhasAfetadas = contribuicaoService.editaCafe(id, contribuicao);
        if(linhasAfetadas == 0){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Excluir contribuição", 
        description = "Remove o registro de contribuição correspondente ao ID informado."
    )
    @DeleteMapping("/{id}")
    // Exclui o registro do banco referente ao id informado
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        int linhasAfetadas = contribuicaoService.deletarCafe(id);
        if(linhasAfetadas == 0){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();

    }
}
