package com.pdlmaicon.backend.service;

import com.pdlmaicon.backend.dto.ContribuicaoDTO;
import com.pdlmaicon.backend.exception.RegrasNegocioEcception;
import com.pdlmaicon.backend.model.Cafe;
import com.pdlmaicon.backend.model.Funcionario;
import com.pdlmaicon.backend.repository.ContribuicaoRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ContribuicaoService {

    private final JdbcTemplate jdbcTemplate;
    private final ContribuicaoRepository contribuicaoRepository;

    public ContribuicaoService(JdbcTemplate jdbcTemplate, ContribuicaoRepository contribuicaoRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.contribuicaoRepository = contribuicaoRepository;
    }


    // Banco escolhido para o projeto foi o H2.
    // application.properties alterado para manter o banco em arquivo e não em memória.


    // Chama o método da repository que lista os registros.
    public List<ContribuicaoDTO> listar(LocalDate data){
        return contribuicaoRepository.buscaTodos(data);
    }



    // Validações de registros duplicados
    public void validaDuplicata(Funcionario funcionario, Cafe cafe){

        // Busca no banco se existe alguma linha com os mesmos nome e cpf informados,
        // Valida por nome e por cpf e guarda o retorno na variável.
        Optional<Funcionario> funcionarioCpf = contribuicaoRepository.buscaPorCpf(funcionario.getCpf());
        Optional<Funcionario> funcionarioNome = contribuicaoRepository.buscaPorNome(funcionario.getNome());
        boolean funcionarioExiste = false;


        //Verifica se nas consultas acima, existe o mesmo funcionario que foi informado no front,
        //Caso seja um funcionario já existente, pega o id desse funcionário e guarda ele no objeto cafe, para que o item seja incluido,
        //Para esse funcionário sem dar erro.
        if(funcionarioNome.isPresent() && funcionarioCpf.isPresent()){
            if((funcionarioNome.get().getNome().trim().equalsIgnoreCase(funcionario.getNome().trim())) && (funcionarioCpf.get().getCpf().equals(funcionario.getCpf()))){
                funcionarioExiste = true;
                cafe.setFuncionarioId(funcionarioCpf.get().getId());

            }
        }

        //Verifica se o CPF informado já existe no banco, se existir, retorna mensagem informando.
        if (funcionarioCpf.isPresent() && !funcionarioCpf.get().getNome().trim().equalsIgnoreCase(funcionario.getNome().trim())){
            throw new RegrasNegocioEcception(
                    "O CPF " + funcionarioCpf.get().getCpf() + " já está cadastrado para '" + funcionarioCpf.get().getNome() + "'."
            );
        }

        // Verifica se o nome informado já está cadastrado, mas com cpf diferente, caso sim, informa e pede para verificar.
        if (funcionarioNome.isPresent() && !funcionarioNome.get().getCpf().equals(funcionario.getCpf())){
            throw new RegrasNegocioEcception(
              "O colaborador '" + funcionario.getNome() + "' já está cadastrado! Verifique os dados."
            );
        }

        // A data do banco vem como yyyy/MM/dd, aqui ela é formatada para ser mostrada no front com padrão dd/MM/yyyy.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean itemJaExiste = contribuicaoRepository.existeItemNaData(cafe.getItem(), cafe.getData());

        // Se a variável acima, que recebe a resposta da repository (true or false) for verdadeira, retorna erro com mensagem
        
        if (itemJaExiste) {

                throw new RegrasNegocioEcception(
                        "O item '" + cafe.getItem() + "' já foi escolhido para o dia " + cafe.getData().format(formatter) + "! Escolha outro item ou data."
                );
            }


        // Após todas as validações acima, caso não entre no throw, salva o registro.
        salvarContribuicao(funcionario, cafe);

    }

    // Recebe os dados do front, verifica se o registro chegou e chama a repository, cria um funcionario para controle do
    // id do funcionario, que é chave estrangeira na tabela cafe.
    public void salvarContribuicao(Funcionario funcionario, Cafe cafe){
        
        if(cafe.getFuncionarioId()==null){
            contribuicaoRepository.salvarFuncionario(funcionario.getNome(), funcionario.getCpf());
            Optional<Funcionario> funcionarioOpt = contribuicaoRepository.buscaPorCpf(funcionario.getCpf());
            if(funcionarioOpt.isPresent()){
                Funcionario funcionarioTemp = funcionarioOpt.get();
                Long funcionarioId = funcionarioTemp.getId();
                contribuicaoRepository.salvarCafe(funcionarioId, cafe.getItem(), cafe.getData(), cafe.isEntregue());
            }
        }else{
            contribuicaoRepository.salvarCafe(cafe.getFuncionarioId(), cafe.getItem(), cafe.getData(), cafe.isEntregue());
        }

    }

    // Recebe as informações e passa para a repository gravar no banco.
    public int editaCafe(Long id, ContribuicaoDTO contribuicao){
        return contribuicaoRepository.editarCafe(id, contribuicao);
    }

    // Recebe o id do registro que deve ser excluido do banco.
    public int deletarCafe(Long id){
        return contribuicaoRepository.deletarCafe(id);
    }
}
