☕ Contribuição Café
Aplicação desenvolvida para organizar e gerenciar os itens das listas de café da manhã da equipe. O sistema permite o cadastro de colaboradores, itens e datas, garantindo a organização sem duplicidades no mesmo dia.

Aplicação disponivel em https://contribuicao-cafe-production.up.railway.app/

Links da aplicação:

Swagger : https://striking-strength-production-a6f1.up.railway.app/swagger-ui/index.html


🚀 Opção 1: Executando via Docker Compose (Recomendado)
Esta forma sobe tanto o backend (Spring Boot) quanto o frontend (Angular) já integrados em containers de forma automatizada.

📋 Pré-requisitos
Docker Desktop instalado e em execução na máquina.

🏃 Como Executar
Abra o terminal na raiz do projeto (onde está o arquivo docker-compose.yml).

Execute o comando para construir e subir os containers:


    docker compose up --build

Assim que finalizado, a aplicação estará disponível em:

Frontend (Angular): http://localhost:4200

Backend (Spring Boot API): http://localhost:8080

Nota: Nas execuções seguintes (sem alterações no código-fonte), você pode rodar apenas 

    docker compose up.

💻 Opção 2: Executando Manualmente
Caso queira rodar o projeto localmente sem utilizar containers.

📋 Pré-requisitos
JDK 17 ou superior instalado.

Node.js (versão 18 ou superior) e npm instalados.

Angular CLI instalado  (npm install -g @angular/cli).

🍃 1. Backend (Spring Boot)
Como a aplicação utiliza o H2 Database local, não é necessário instalar nem subir nenhum servidor de banco de dados externo.

Abra um terminal na pasta do backend:


    cd backend

Execute a aplicação utilizando o Maven:

Windows (CMD / PowerShell):

    .\mvnw.cmd spring-boot:run


Linux / macOS:

    ./mvnw spring-boot:run


O servidor backend iniciará na porta 8080.



🅰️ 2. Frontend (Angular)
Abra um segundo terminal na pasta do frontend:


cd frontend
Instale as dependências do projeto (apenas primeira execução):


    npm install

Inicie o servidor de desenvolvimento:


    npm start   ou   ng serve

Acesse no navegador: http://localhost:4200.



📑 Documentação da API (Swagger / OpenAPI)

URL de acesso: http://localhost:8080/swagger-ui/index.html



🧪 Testes End-to-End (E2E) e de Integração

📋 Pré-requisitos
Antes de executar o comando, garanta que o seu ambiente de desenvolvimento possui os seguintes itens instalados:

Node.js: Versão 18.x ou superior (com o gerenciador de pacotes npm embutido).

Para verificar se está instalado: node -v e npm -v

Dependências do Projeto: As bibliotecas do projeto (incluindo o próprio Cypress) devem estar instaladas.

Caso ainda não tenha instalado: Execute npm install na raiz do projeto frontend.

Navegador Web: Google Chrome, Mozilla Firefox ou Microsoft Edge instalados na máquina (o Cypress usará um deles para rodar os testes).

🚀 Passos para Execução

Acesse o a pasta do frontend no terminal e navegue até a pasta raiz da aplicação Angular.

Abra a Interface do Cypress:

Execute esse comando no terminal: npx cypress open


Selecione o Tipo de Teste:

Na tela inicial do Cypress, clique na opção E2E Testing (Testes de Ponta a Ponta).


Escolha o Navegador:

Selecione o navegador de sua preferência (ex: Chrome) e clique em Start E2E Testing in Chrome.


Rode a Suíte de Testes:

O painel do Cypress abrirá uma janela mostrando os arquivos de teste (ex: contribuicao.cy.ts).

Clique sobre o arquivo desejado para iniciar a execução interativa dos testes e visualizar a aplicação sendo testada em tempo real.