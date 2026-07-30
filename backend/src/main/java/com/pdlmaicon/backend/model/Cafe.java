package com.pdlmaicon.backend.model;

import java.time.LocalDate;

public class Cafe {
    private Long id;
    private Long funcionarioId;
    private String item;
    private LocalDate data;
    private boolean entregue;

    public Cafe() {
    }

    public Cafe(Long id, Long funcionarioId, String item, LocalDate data, boolean entregue) {
        this.id = id;
        this.funcionarioId = funcionarioId;
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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public boolean isEntregue() {
        return entregue;
    }

    public void setEntregue(boolean entregue) {
        this.entregue = entregue;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }
}
