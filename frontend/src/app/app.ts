import { Component, signal, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ContribuicaoService } from './services/contribuicao.service';
import { Contribuicao } from './models/contribuicao.model';
import { Observable, timer } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit{


    // Força o carregamento das mudanças na página
    private cdr = inject(ChangeDetectorRef);

    // Lista que vai guardar o retorno do backend
    listaContribuicoes: Contribuicao[] = [];
    

    // Define o timezone da data para o padrão Brasil
    private dataHojeLocal(): string {
        const hoje = new Date();
        hoje.setMinutes(hoje.getMinutes() - hoje.getTimezoneOffset());
        return hoje.toISOString().split('T')[0];
    }

    // Variável para guardar a data selecionada no input (inicia vazia)
    // Depois essa data passa para o campo de filtro de data, mostrando os registros correspondentes
    dataFiltro: string = this.dataHojeLocal();


    //itens formulário

    id = '';
    funcionarioId = '';
    nome = '';
    cpf = '';
    item = '';
    data: string = this.dataHojeLocal();
    entregue = 'false';
    mensagem = '';

    constructor(private contribuicaoService: ContribuicaoService) {}

    // O ngOnInit roda automaticamente quando a página carrega
    ngOnInit(): void {
      this.carregarDados();
    }


    get ehHoje(): boolean {
      const hoje = new Date().toISOString().split('T')[0];
      return !this.dataFiltro || this.dataFiltro === this.dataHojeLocal();
    }

    // Método chamado ao carregar a página ou clicar no botão, carrega os dados na página
    carregarDados(): void {
        this.contribuicaoService.listar(this.dataFiltro).subscribe({
          next: (dados) => {
                  this.listaContribuicoes = [...dados];
                  // Força a atualização do DOM/HTML imediatamente
                  this.cdr.detectChanges();
                },
          error: (err) => console.error('Erro ao carregar:', err)
        });
      }

    // Pega os dados do form e envia para a service
    novaContribuicao(){
      if(!this.nome || !this.cpf){
        return;
      }

      if(this.id){
        this.editarContribuicao();
        return;
      }

      const dataCadastrada = this.data;

      this.contribuicaoService.novaContribuicao({
        funcionarioNome: this.nome,
        funcionarioCpf: this.cpf,
        item: this.item,
        data: this.data,
        entregue: false
         }).subscribe({
                 next: () => {

                   this.dataFiltro = dataCadastrada;
                   this.carregarDados();
                   this.limparFormulario();
                 },
                 error: (err: HttpErrorResponse) => {

                   const mensagemErro = err.error?.message || 'Ocorreu um erro inesperado.';
                   this.mensagem = mensagemErro;
                   this.carregarDados();
                 }
               });
    }

    editarContribuicao(){

      const dataEditada = this.data;

      const parsedId = parseInt(this.id, 10);
      const parsedFuncionarioId = this.funcionarioId ? parseInt(this.funcionarioId, 10) : parsedId;

      this.contribuicaoService.editar({
          id: parsedId,
          funcionarioId: parsedFuncionarioId,
          funcionarioNome: this.nome,
          funcionarioCpf: this.cpf,
          item: this.item,
          data: this.data,
          entregue: false
        }).subscribe({
              next: () => {

                this.dataFiltro = dataEditada;
                this.limparFormulario();
                this.carregarDados();
              },
              error: (err: HttpErrorResponse) => {

                    const mensagemErro = err.error?.message || 'Ocorreu um erro inesperado.';
                    this.mensagem = mensagemErro;
                    this.carregarDados();
                  }
            });

    }

    excluir(id: number){

      this.contribuicaoService.excluir(id).subscribe({
        next: () => {
          // Recarrega a tabela após excluir do banco
          this.carregarDados();
        },
        error: (err) => console.error('Erro ao excluir:', err)
        });
    }


    // Carrega o formulário com os dados para edição
    carregaContribuicaoForm(contribuicao: Contribuicao){

      this.id = contribuicao.id.toString();
      this.funcionarioId = contribuicao.id.toString();
      this.nome = contribuicao.funcionarioNome;
      this.cpf = contribuicao.funcionarioCpf;
      this.data = contribuicao.data;
      this.item = contribuicao.item;

      this.cdr.detectChanges();
    }

    // Salva o status ao ser alterado clicando no checkbox
    alterarStatus(item: Contribuicao): void {

      this.contribuicaoService.editar({
         id: item.id,
         funcionarioId: item.funcionarioId,
         funcionarioNome: item.funcionarioNome,
         funcionarioCpf: item.funcionarioCpf,
         item: item.item,
         data: item.data,
         entregue: item.entregue
       }).subscribe({
               next: () => console.log('Status atualizado com sucesso!'),
               error: (err) => {
                 const msgErro = err.error?.message || "Erro inesperado ao alterar.";
                 alert(msgErro);
                 item.entregue = !item.entregue; //Não grava se der erro
               }
             });

    }


    limparFormulario(): void {
      this.id = '';
      this.funcionarioId = '';
      this.nome = '';
      this.cpf = '';
      this.item = '';
      this.data = this.dataHojeLocal(); // Retorna para a data de hoje formatada
      this.entregue = 'false';
      this.mensagem = '';
    }


  // Formata o cpf enquanto é digitado
  formatarCpf(event: Event): void {
    const input = event.target as HTMLInputElement;


    let valor = input.value.replace(/\D/g, '');

    if (valor.length > 11) {
      valor = valor.substring(0, 11);
    }

    valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
    valor = valor.replace(/(\d{3})(\d)/, '$1.$2');
    valor = valor.replace(/(\d{3})(\d{1,2})$/, '$1-$2');

    // 4. Atualiza o valor no input e na variável do componente
    input.value = valor;
    this.cpf = valor;
  }

  protected readonly title = signal('frontend');
}
