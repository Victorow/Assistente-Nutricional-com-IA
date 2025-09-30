# 🍎 Assistente Nutricional com IA

Sistema completo para gestão de pacientes nutricionais com conformidade LGPD, criptografia avançada e integração com IA.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.13-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)
![LGPD](https://img.shields.io/badge/LGPD-100%25%20Compliance-green)

## 🎯 Visão Geral

Aplicação enterprise desenvolvida para nutricionistas gerenciarem seus pacientes de forma segura e eficiente, com total conformidade às leis de proteção de dados (LGPD).

## ✨ Funcionalidades Principais

### 🔐 Segurança & LGPD
- **Criptografia AES-128** para dados sensíveis (telefone, email)
- **BCrypt força 12** para senhas
- **Conformidade LGPD completa** com todos os direitos do titular
- **Auditoria completa** com logs de acesso e timestamps
- **Consentimento explícito** com registro de IP e data

### 👥 Gestão de Dados
- **CRUD completo** para nutricionistas e pacientes
- **Status de anamnese** (Pendente, Em Andamento, Concluída, Aprovada)
- **Validações robustas** com constraints de banco
- **Relacionamentos JPA** otimizados

### 📊 Relatórios e Estatísticas
- **Dashboard** com estatísticas em tempo real
- **Exportação de dados** (direito à portabilidade)
- **Histórico de ações** para auditoria

## 🏗️ Arquitetura

Frontend Backend Database
(React) ←→ (Spring Boot) ←→ (PostgreSQL)
[Futuro] Port: 8080 Port: 5432
↓
AI Service
(Python)
[Futuro]

## 🛠️ Stack Tecnológica

### Backend (Java)
- **Spring Boot 3.3.13** - Framework principal
- **Java 21 LTS** - Linguagem de programação
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Autenticação e autorização
- **Hibernate** - ORM
- **Maven** - Gerenciamento de dependências

### Banco de Dados
- **PostgreSQL 16** - Banco principal
- **Docker** - Containerização

### DevOps & Deploy
- **Docker Compose** - Orquestração local
- **GitHub Actions** - CI/CD (futuro)
- **Cloud Ready** - Preparado para AWS/GCP

## 🚀 Como Executar

### Pré-requisitos
- Java 21+
- Maven 3.6+
- Docker & Docker Compose

### Execução Local

1. **Clonar repositório**

git clone https://github.com/Victorow/Assistente-Nutricional-com-IA.git
cd Assistente-Nutricional-com-IA

2. **Subir banco de dados**
docker-compose up postgres -d

3. **Executar aplicação**
cd backend/core-service
mvn spring-boot:run

4. **Testar API**
curl http://localhost:8080/api/status

## 📚 API Endpoints

### Nutricionistas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/nutricionistas` | Criar nutricionista |
| GET | `/api/nutricionistas` | Listar todos |
| GET | `/api/nutricionistas/{id}` | Buscar por ID |
| POST | `/api/nutricionistas/login` | Autenticação |

### Pacientes
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/api/pacientes` | Criar paciente |
| GET | `/api/pacientes/nutricionista/{id}` | Listar por nutricionista |
| GET | `/api/pacientes/{id}` | Buscar por ID |
| PUT | `/api/pacientes/{id}/status` | Atualizar status |

### LGPD (Compliance)
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/pacientes/{id}/dados-lgpd` | Exportar dados |
| GET | `/api/pacientes/{id}/logs-lgpd` | Consultar logs |
| DELETE | `/api/pacientes/{id}/dados-lgpd` | Direito ao esquecimento |
| PUT | `/api/pacientes/{id}/revogar-consentimento` | Revogar consentimento |

## 🔒 Conformidade LGPD

### Direitos Implementados
- ✅ **Acesso** - Consulta de dados pessoais
- ✅ **Portabilidade** - Exportação em JSON estruturado
- ✅ **Correção** - Atualização de dados
- ✅ **Exclusão** - Direito ao esquecimento
- ✅ **Informação** - Transparência no tratamento
- ✅ **Revogação** - Retirada de consentimento

### Medidas de Segurança
- **Criptografia** de dados sensíveis
- **Auditoria** completa de acessos
- **Logs** detalhados com timestamps
- **Validação** de entrada de dados
- **Backup** e recuperação

## 🧪 Exemplos de Uso

### Criar Nutricionista
curl -X POST http://localhost:8080/api/nutricionistas
-H "Content-Type: application/json"
-d '{
"nome": "Dr. João Silva",
"email": "joao@exemplo.com",
"senha": "minhasenha123",
"crn": "CRN-12345"
}'

### Criar Paciente
curl -X POST http://localhost:8080/api/pacientes
-H "Content-Type: application/json"
-d '{
"nome": "Maria Santos",
"telefone": "11987654321",
"email": "maria@exemplo.com",
"idade": 35,
"peso": 70.5,
"altura": 1.65,
"nutricionistaId": 1
}'


### Exportar Dados LGPD
curl "http://localhost:8080/api/pacientes/1/dados-lgpd"

## 📊 Estatísticas do Projeto

- **Endpoints:** 20+
- **Entidades JPA:** 3
- **Compliance LGPD:** 100%
- **Cobertura de segurança:** Enterprise level

## 🔮 Roadmap

### Fase 2 - IA Integration
- [ ] Microsserviço Python com Google Gemini
- [ ] Geração automática de dietas
- [ ] Análise nutricional inteligente

### Fase 3 - Comunicação
- [ ] Integração WhatsApp Business API
- [ ] Anamnese conversacional
- [ ] Notificações automáticas

### Fase 4 - Frontend
- [ ] Dashboard React
- [ ] Interface responsiva
- [ ] PWA mobile

### Fase 5 - Deploy Production
- [ ] CI/CD com GitHub Actions
- [ ] Deploy AWS/GCP
- [ ] Monitoramento e alertas

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 👨‍💻 Desenvolvedor

**Victor Ferreira*
- GitHub: [@Victorow](https://github.com/Victorow)
- LinkedIn: [Victor](https://www.linkedin.com/in/victoraugustoferreiradossantos/)


---

⭐ **Se este projeto te ajudou, dê uma estrela!** ⭐



