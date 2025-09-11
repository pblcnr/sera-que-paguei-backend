# Será que Paguei? - Backend API

## O Problema
Após começar a morar sozinho, percebi que estava esquecendo de pagar contas e acumulando juros desnecessários. A falta de organização financeira pessoal pode gerar prejuízos significativos.

## A Solução
**Será que Paguei?** é uma API REST para gerenciamento de contas pessoais com sistema de lembretes automáticos. Nunca mais esqueça de pagar suas contas!

## Funcionalidades

- **Autenticação segura** com JWT
- **Gestão de usuários** com criptografia BCrypt
- **CRUD completo de contas** a pagar
- **Sistema de recorrência** automática
- **Validação de propriedade** dos dados
- **Soft delete** para recuperação de dados

## Stack Tecnológica

- **Java 21** - Linguagem principal
- **Spring Boot 3.3.x** - Framework
- **Spring Security** - Autenticação e autorização
- **PostgreSQL** - Banco de dados relacional
- **JPA/Hibernate** - ORM
- **Docker** - Containerização
- **JWT** - Tokens de autenticação
- **BCrypt** - Criptografia de senhas
- **Maven** - Gerenciamento de dependências

## Pré-requisitos

- Java 21+
- Maven 3.9+
- Docker e Docker Compose
- PostgreSQL (via Docker)

## Instalação e Configuração

1. **Clone o repositório**
```bash
git clone https://github.com/pblcnr/sera-que-paguei-backend.git
cd sera-que-paguei-backend
```

2. **Configure as variáveis de ambiente**

    Crie um arquivo ```application-dev.properties``` em ```src/main/resources/```:

```properties
spring.datasource.password=sua_senha_aqui
jwt.secret=sua_chave_secreta_com_mais_de_32_caracteres
```

3. **Inicie o banco de dados**
```bash
docker-compose up -d
```

4. **Execute a aplicação**
```bash
./mvnw spring-boot:run
```
A API estará disponível em ```http://localhost:8080```

## Documentação da API
### Autenticação

**Registrar novo usuário**
```http
POST /api/auth/register
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123",
  "confirmaSenha": "senha123"
}
```

**Login**
```http 
POST /api/auth/login
Content-Type: application/json

{
  "email": "joao@email.com",
  "senha": "senha123"
}
```

### Gestão de Contas
**Criar conta (Autenticado)**

```http 
POST /api/contas
Authorization: Bearer {token}
Content-Type: application/json

{
  "descricao": "Conta de Luz",
  "valor": 150.00,
  "dataVencimento": "2024-12-30",
  "categoria": "Energia",
  "recorrente": true
}
```

**Listar contas do usuário**
```http 
GET /api/contas
Authorization: Bearer {token}
```

**Pagar conta**
```http 
PUT /api/contas/{id}/pagar
Authorization: Bearer {token}
```

## Arquitetura
O projeto segue uma arquitetura em camadas:
```
src/main/java/com/github/pblcnr/seraquepaguei/
├── controller/     # Endpoints REST
├── service/        # Lógica de negócio
├── repository/     # Acesso a dados
├── entity/         # Entidades JPA
├── dto/            # Objetos de transferência
├── config/         # Configurações
└── enums/          # Enumerações
```

## Segurança
- Senhas criptografadas com BCrypt
- Autenticação via JWT
- Validação de propriedade de recursos
- Configurações sensíveis em variáveis de ambiente

## Contribuindo
Este é um projeto pessoal em desenvolvimento, mas sugestões são bem-vindas!

## Licença
MIT

## Autor
Paulo Henrique de Andrade

[LinkedIn](https://www.linkedin.com/in/paulo-henrique-de-andrade/)
[Gmail](mailto:paulohenrique.andradefilho@gmail.com)

---
Desenvolvido com ☕ e determinação