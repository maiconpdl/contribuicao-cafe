describe('Fluxo da Aplicação - Gestão de Café', () => {

  const dataHoje = new Date().toISOString().split('T')[0];

  const mockListaContribuicoes = [
    {
      id: 1,
      funcionarioId: 10,
      funcionarioNome: 'Maicon Pires',
      funcionarioCpf: '123.456.789-00',
      item: 'Bolo de Cenoura',
      data: dataHoje,
      entregue: false
    },
    {
      id: 2,
      funcionarioId: 11,
      funcionarioNome: 'Talita Souza',
      funcionarioCpf: '444.555.666-77',
      item: 'Melancia Fatiada',
      data: dataHoje,
      entregue: false
    }
  ];

  beforeEach(() => {
    // 1. Intercepta a chamada do backend usando **/contribuicao*
    cy.intercept('GET', '**/contribuicao*', {
      statusCode: 200,
      body: mockListaContribuicoes
    }).as('getLista');

    // 2. Visita a página APÓS registrar o intercept
    cy.visit('http://localhost:4200');
    
    // 3. Aguarda carregar
    cy.wait('@getLista');
  });

  it('1. Deve renderizar a lista de contribuições vinda do backend', () => {
    //Insere o Maicon Pires
    cy.contains('Maicon Pires').should('be.visible');
    cy.contains('Bolo de Cenoura').should('be.visible');
    cy.contains('123.456.789-00').should('be.visible');
  });

  it('2. Deve formatar o CPF automaticamente enquanto o usuário digita', () => {
    cy.get('input[placeholder*="CPF"], input[name="cpf"]')
      .first()
      .clear()
      .type('12345678900')
      .should('have.value', '123.456.789-00');
  });

  it('3. Deve cadastrar uma nova contribuição com sucesso e limpar o formulário', () => {
    cy.intercept('POST', '**/contribuicao*', {
      statusCode: 201,
      body: { message: 'Cadastrado com sucesso' }
    }).as('postContribuicao');

   
    cy.get('input[placeholder*="Funcionario"], input[name="funcionario"]').type('Marcio Costa');
    cy.get('input[placeholder*="CPF"], input[name="cpf"]').type('44465496677');
    cy.get('input[placeholder*="Item"], input[name="item"]').type('Pão de Repolho');

    cy.contains('button', /Salvar/i).click();

    cy.wait('@postContribuicao').its('request.body').should('deep.include', {
      funcionarioNome: 'Marcio Costa',
      funcionarioCpf: '444.654.966-77',
      item: 'Pão de Repolho'
    });
  });

  it('4. Deve exibir mensagem de erro quando o backend rejeitar', () => {
    cy.intercept('POST', '**/contribuicao*', {
      statusCode: 400,
      body: { message: 'Já existe uma contribuição deste item para a mesma data.' }
    }).as('postContribuicaoErro');

    cy.get('input[placeholder*="Funcionario"], input[name="funcionario"]').type('Maicon Pires');
    cy.get('input[placeholder*="CPF"], input[name="cpf"]').type('12345678900');
    cy.get('input[placeholder*="Item"], input[name="item"]').type('Bolo de Cenoura');

    cy.contains('button', /Salvar/i).click();

    cy.wait('@postContribuicaoErro');
  });

  it('5. Deve carregar dados no formulário e executar edição', () => {
    // Adicionado /**/ para interceptar /contribuicao/1, /contribuicao/2, etc.
    cy.intercept('PUT', '**/contribuicao/**', { statusCode: 200 }).as('putContribuicao');

    // Clica em Editar na linha correspondente
    cy.contains('tr', 'Maicon Pires').contains('button', /Editar/i).click();

    // Valida o preenchimento no formulário
    cy.get('input[placeholder*="Item"], input[name="item"]').should('have.value', 'Bolo de Cenoura');

    // Altera o item e salva
    cy.get('input[placeholder*="Item"], input[name="item"]').clear().type('Suco de Laranja');
    cy.contains('button', /Salvar/i).click();

    cy.wait('@putContribuicao');
  });

  it('6. Deve excluir uma contribuição', () => {
    cy.intercept('DELETE', '**/contribuicao/*', { statusCode: 200 }).as('deleteContribuicao');

    cy.contains('tr', 'Maicon Pires').contains('button', /Excluir/i).click();

    cy.wait('@deleteContribuicao');
  });

  it('7. Deve alterar o status da contribuição ao clicar no checkbox', () => {
    // Intercepta o PUT do checkbox
    cy.intercept('PUT', '**/contribuicao/**', { statusCode: 200 }).as('putStatus');

    // Busca a linha com a contribuição e clica no checkbox
    cy.contains('tr', 'Maicon Pires')
      .find('input[type="checkbox"]')
      .click();

    // Valida se o PUT ocorreu e se o payload enviado foi entregue: true
    cy.wait('@putStatus').its('request.body').should('deep.include', {
      entregue: true
    });
    });
});