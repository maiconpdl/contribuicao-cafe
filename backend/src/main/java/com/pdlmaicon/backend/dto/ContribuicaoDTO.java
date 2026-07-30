package com.pdlmaicon.backend.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;


// Classe utilizada para receber os dados do banco, com anotações para sagger
public class ContribuicaoDTO {

    
    @Schema(hidden = true) // Anotação para não exibir no swagger
    private Long id;
    @Schema(hidden = true)
    private Long funcionarioId;
    @Schema(description = "Nome do funcionário.", example = "Talita Souza")
    private String funcionarioNome;
    @Schema(description = "CPF do funcionário.", example = "111.222.333-44")
    private String funcionarioCpf;
    @Schema(description = "Item que será trazido para o café.", example = "Queijo")
    private String item;
    @Schema(description = "Data que será trazido.", example = "2026-07-25")
    private LocalDate data;
    @Schema(hidden = true)
    private boolean entregue;

    public ContribuicaoDTO() {
    }

    public ContribuicaoDTO(Long id, Long funcionarioId, String funcionarioNome, String funcionarioCpf, String item, LocalDate data, boolean entregue) {
        this.id = id;
        this.funcionarioId = funcionarioId;
        this.funcionarioNome = funcionarioNome;
        this.funcionarioCpf = funcionarioCpf;
        this.item = item;
        this.data = data;
        this.entregue = entregue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEntregue() {
        return entregue;
    }

    public void setEntregue(boolean entregue) {
        this.entregue = entregue;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getFuncionarioCpf() {
        return funcionarioCpf;
    }

    public void setFuncionarioCpf(String funcionarioCpf) {
        this.funcionarioCpf = funcionarioCpf;
    }

    public String getFuncionarioNome() {
        return funcionarioNome;
    }

    public void setFuncionarioNome(String funcionarioNome) {
        this.funcionarioNome = funcionarioNome;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }
}
