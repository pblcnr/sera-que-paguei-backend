# Será que Paguei? - Backend API

## O Problema
Após começar a morar sozinho, percebi que estava esquecendo de pagar contas e acumulando juros desnecessários. A falta de organização financeira pessoal pode gerar prejuízos significativos.

## A Solução
**Será que Paguei?** é uma API REST completa para gerenciamento de contas pessoais com sistema de lembretes automáticos. Nunca mais esqueça de pagar suas contas!

## Funcionalidades

- **Autenticação JWT** - Tokens seguros com expiração configurável
- **Gestão de Usuários** - CRUD completo com soft delete
- **Gerenciamento de Contas** - Criação, listagem, pagamento e recorrência
- **Filtros e Paginação** - Busca otimizada por período, status e categoria
- **Sistema de Notificações** - Lembretes automáticos agendados (simulado)
- **Exception Handling** - Tratamento global de erros padronizado
- **Documentação Swagger** - Interface interativa para testes
- **Testes Unitários** - Cobertura com JUnit e Mockito

## Stack Tecnológica

- **Java 21** - Linguagem principal
- **Spring Boot 3.2.5** - Framework base
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL** - Banco de dados relacional
- **H2 Database** - Banco em memória para testes
- **Docker & Docker Compose** - Containerização
- **JWT** - Tokens de autenticação
- **BCrypt** - Criptografia de senhas
- **Maven** - Gerenciamento de dependências
- **Swagger/OpenAPI** - Documentação automática
- **JUnit & Mockito** - Testes unitários

## Pré-requisitos

- Java 21+
- Docker e Docker Compose
- Maven 3.9+ (opcional, pode usar ./mvnw)

## 🔧 Instalação e Configuração

### Opção 1: Executar com Docker (Recomendado)
```bash
# Clone o repositório
git clone https://github.com/pblcnr/sera-que-paguei-backend.git
cd sera-que-paguei-backend

# Inicie a aplicação e o banco
docker-compose up --build

# A API estará disponível em http://localhost:8080
# Swagger UI em http://localhost:8080/swagger-ui
```

### Opção 2: Executar localmente
```bash
# Clone o repositório
git clone https://github.com/pblcnr/sera-que-paguei-backend.git
cd sera-que-paguei-backend

# Configure as variáveis de ambiente (.env)
DB_PASSWORD=sua_senha_bd
JWT_SECRET=sua_chave_secreta_com_mais_de_32_caracteres

# Inicie apenas o banco de dados
docker-compose up postgres -d

# Execute a aplicação
./mvnw spring-boot:run

# A API estará disponível em http://localhost:8080
```

## Documentação da API
### Swagger UI
Acesse a documentação interativa em: `http://localhost:8080/swagger-ui`

### Principais Endpoints
#### Autenticação
- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login - Fazer login

#### Contas (Requer Autenticação)
- `GET /api/contas` - Listar contas com paginação e filtros 
- `POST /api/contas` - Criar nova conta 
- `PUT /api/contas/{id}/pagar` - Marcar conta como paga 
- `GET /api/contas/vencendo-hoje` - Contas vencendo hoje

#### Usuários (Requer Autenticação)
- `GET /api/users` - Listar usuários 
- `GET /api/users/{id}` - Buscar usuário por ID 
- `PUT /api/users/{id}` - Atualizar usuário 
- `DELETE /api/users/{id}` - Desativar usuário (soft delete)

### Exemplo de Uso
```bash
# 1. Registrar usuário
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@email.com",
    "senha": "senha123",
    "confirmaSenha": "senha123"
  }'

# 2. Fazer login (salve o token retornado)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@email.com",
    "senha": "senha123"
  }'

# 3. Criar conta (use o token do login)
curl -X POST http://localhost:8080/api/contas \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "descricao": "Conta de Luz",
    "valor": 150.00,
    "dataVencimento": "2024-12-30",
    "categoria": "Energia",
    "recorrente": true
  }'
```

## Arquitetura
```
src/main/java/com/github/pblcnr/seraquepaguei/
├── config/         # Configurações (Security, JWT, Swagger)
├── controller/     # Endpoints REST
├── dto/            # Data Transfer Objects
├── entity/         # Entidades JPA
├── enums/          # Enumerações
├── exception/      # Exceções customizadas e handlers
├── repository/     # Interfaces de acesso a dados
├── scheduler/      # Jobs agendados
└── service/        # Lógica de negócio
```

## Testes
```bash
# Executar todos os testes
./mvnw test
```

## Segurança
- Senhas criptografadas com BCrypt (10 rounds)
- Tokens JWT com expiração configurável
- Validação de propriedade de recursos
- Proteção contra SQL Injection via JPA
- Configurações sensíveis externalizadas

### Roadmap Futuro
- Implementar envio real de emails
- Adicionar notificações push
- Criar dashboard com estatísticas

## Contribuindo 
Pull requests são bem-vindos! Para mudanças maiores, abra uma issue primeiro.

## Licença
MIT

## Autor
**Paulo Henrique de Andrade**

![https://www.linkedin.com/in/paulo-henrique-de-andrade/](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)
![https://github.com/pblcnr](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![mailto:paulohenrique.andradefilho@gmail.com](https://img.shields.io/badge/Gmail-D14836?style=for-the-badge&logo=gmail&logoColor=white)

---
Desenvolvido com ☕ e determinação por um desenvolvedor que também esquecia de pagar contas!