import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Contribuicao } from '../models/contribuicao.model';

@Injectable({
  providedIn: 'root'
})
export class ContribuicaoService {

  // URL da API Spring Boot
  private readonly apiUrl = 'https://striking-strength-production-a6f1.up.railway.app/contribuicao';

  // Injeta as requisições HTTP do Angular
  private http = inject(HttpClient);



  // Recebe uma data e chama o get do backend, passando a data como parâmetro
  // A data não é obrigatória, caso não seja enviada, lista os registros de hoje.
  listar(data?: string){
    let params = new HttpParams();

    if (data) {
          params = params.set('data', data);
    }
    return this.http.get<Contribuicao[]>(this.apiUrl, { params });
  }

  //Recebe um objeto contribuicao e envia para o backend via post
  novaContribuicao(contribuicao: Partial<Contribuicao>): Observable<Contribuicao>{
      return this.http.post<Contribuicao>(this.apiUrl, contribuicao);
   }

  //Recebe um objeto contribuicao e envia para o backend via put
  editar(contribuicao: Contribuicao){

       const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

       const url = `${this.apiUrl}/${contribuicao.id}`;
       return this.http.put<Contribuicao>(`${this.apiUrl}/${contribuicao.id}`, contribuicao);
   }

  //Recebe um id e envia para o backend via delete
  excluir(id: number){
      return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
 }


