☕ Contribuição Café
Aplicação full-stack desenvolvida para organizar e gerenciar os itens das listas de café da manhã da equipe. O sistema permite o cadastro de colaboradores, itens e datas, garantindo a organização sem duplicidades no mesmo dia.

🛠️ Tecnologias Utilizadas
Backend: Java 17+, Spring Boot 3, Spring Data JPA / JDBC, H2 Database (Banco em memória), OpenAPI 3 / Swagger

Frontend: Angular 18, TypeScript, RxJS, HTML5 / SCSS

Containerização: Docker & Docker Compose

🚀 Opção 1: Executando via Docker Compose (Recomendado)
Esta forma sobe tanto o backend (Spring Boot) quanto o frontend (Angular) já integrados em containers de forma automatizada.

📋 Pré-requisitos
Docker Desktop instalado e em execução na máquina.

🏃 Como Executar
Abra o terminal na raiz do projeto (onde está o arquivo docker-compose.yml).

Execute o comando para construir e subir os containers:

Bash
docker compose up --build
Acompanhe a inicialização dos logs no terminal. Assim que finalizado, a aplicação estará disponível em:

Frontend (Angular): http://localhost:4200

Backend (Spring Boot API): http://localhost:8080

Nota: Nas execuções seguintes (sem alterações no código-fonte), você pode rodar apenas docker compose up.

💻 Opção 2: Executando Manualmente (Modo Desenvolvimento)
Caso queira rodar o projeto localmente para realizar alterações de código sem utilizar containers.

📋 Pré-requisitos
JDK 17 ou superior instalado.

Node.js (versão 18 ou superior) e npm instalados.

Angular CLI instalado globalmente (npm install -g @angular/cli).

🍃 1. Backend (Spring Boot)
Como a aplicação utiliza o H2 Database em memória, não é necessário instalar nem subir nenhum servidor de banco de dados externo.

Abra um terminal na pasta do backend:

Bash
cd backend
Execute a aplicação utilizando o wrapper do Maven:

Windows (CMD / PowerShell):

DOS
.\mvnw.cmd spring-boot:run
Linux / macOS:

Bash
./mvnw spring-boot:run
O servidor backend iniciará na porta 8080.

🅰️ 2. Frontend (Angular)
Abra um segundo terminal na pasta do frontend:

Bash
cd frontend
Instale as dependências do projeto (apenas na primeira vez):

Bash
npm install
Inicie o servidor de desenvolvimento:

Bash
npm start
# ou
ng serve
Acesse no navegador: http://localhost:4200.

📝 Regras de Negócio & Como Cadastrar Contribuições
📋 Regras de Cadastro
Atribuição de Item: Cada colaborador seleciona/informa o item que levará no café para uma determinada data.

Unicidade de Item por Data: Não é permitido o cadastro de um mesmo item mais de uma vez para a mesma data.

Formatação do Item: Cada colaboração deve conter apenas 1 item por registro (ex: cadastrar "Café" e "Pão" em registros separados) para evitar duplicidades na validação de termos.

Filtro por Data: É possível listar as contribuições registradas para uma data específica informando o parâmetro no formato YYYY-MM-DD.

📑 Documentação da API (Swagger / OpenAPI)
Toda a API REST está documentada e testável via interface interativa do Swagger UI.

URL de acesso: http://localhost:8080/swagger-ui.html (ou /swagger-ui/index.html)

Através da interface do Swagger você pode consultar os schemas de ContribuicaoDTO, testar os endpoints GET, POST, PUT, DELETE e verificar os códigos de resposta HTTP (200 OK, 201 Created, 400 Bad Request, etc.).

🧪 Testes End-to-End (E2E) e de Integração
Executando no Backend:
Para executar a suíte de testes unitários e de integração no Spring Boot, abra o terminal na pasta backend e rode:

Bash
# Windows
.\mvnw.cmd test

# Linux/macOS
./mvnw test
Executando no Frontend:
Para rodar os testes da interface no Angular:

Bash
cd frontend
ng test