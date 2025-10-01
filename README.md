# SmartMottu – DevOps Tools & Cloud Computing

Aplicação web Java (Spring Boot 3, Java 17) com front Thymeleaf, hospedada no Azure App Service (Linux) e banco de dados no Azure SQL Database.

## Introdução
A Mottu é uma empresa de aluguel de motos a baixo custo (R$26,40/dia),
oferecendo não só o veículo, mas também crédito, manutenção, assistência,
oportunidades de trabalho (Delivery e iFood) e três modelos principais: Pop, Sport (a
mais econômica do Brasil) e Mottu-E (elétrica). Produz motos em Manaus em
parceria com a Índia e utiliza IoT e GPS para rastreamento.
O problema identificado é a desorganização dos pátios: dificuldade em
localizar motos paradas sem placa, com chassi encoberto ou quando o GPS entra
em “sleeping”. Isso causa retrabalho, atrasos e queda de produtividade,
prejudicando a experiência do cliente.

## Solução Proposta
A proposta consiste em desenvolver uma plataforma inteligente de gestão de
pátios para a Mottu, integrando visão computacional, IoT e QR Code. O sistema
contará com câmeras 360° que captam o ambiente em tempo real, permitindo que a
visão computacional identifique motos mesmo sem placa ou chassi visível. Cada
moto terá um cadastro completo (tipo, modelo, placa, chassi e foto) armazenado em
banco de dados e vinculado a um QR Code exclusivo, que funciona como
identificador digital para acesso rápido a informações e histórico. A plataforma
possibilitará que o operador selecione a moto desejada e, por meio das câmeras, o
sistema a localize visualmente em tempo real. Além disso, será gerado um histórico
individual de movimentações, manutenções e alterações de status.
Com isso, o problema da perda de motos no pátio é solucionado de forma
prática e escalável, garantindo rastreabilidade, eficiência operacional e otimização
do trabalho dos operadores.



## Arquitetura da solução

![Arquitetura](docs/arquitetura.jpg)

Fluxos principais:
- GitHub Actions → Web App: Deploy (CI/CD)
- Browser → Web App: HTTP/HTTPS
- Web App → Azure SQL Database: JDBC (TLS)
- Web App → Application Insights: Telemetria (agent codeless)


## Provisionamento e Deploy (script)
O script `deploy-smartmottu.sh` automatiza:
1) Criação de Resource Group, App Service Plan (Linux) e Web App
2) Criação de Azure SQL Server + Database
3) Configuração de App Settings no Web App (ex.: SPRING_DATASOURCE_URL/USERNAME/PASSWORD, PORT)
4) Aplicação de DDL/DML no Azure SQL via `sqlcmd` (criação de tabelas/constraints e seed inicial)

Notas:
- O seed cria apenas o usuário ADMIN com senha `admin123`.
- O script requer Azure CLI autenticada (`az login`) e `bash`. Para executar DDL/DML, requer `sqlcmd` (ou use Azure Cloud Shell Bash).

DDL completo e comentado: consulte `script_bd.sql` na raiz do projeto.

## Passo a passo (Azure)
1) Pré-requisitos
- Azure CLI logada: `az login`
- Ambiente com `bash` e `sqlcmd` (Windows: Git Bash/WSL + sqlcmd; alternativa: Azure Cloud Shell Bash)

2) Executar o deploy
```bash
./deploy-smartmottu.sh
```

3) Após o deploy
- Acessar: `https://SEU-APP.azurewebsites.net` (nesse caso é `https://smartmottu-api.azurewebsites.net`)

- Login DEV: `admin@email.com` / `admin123`
- Logout: GET `/logout`

4) Teste rápido de funcionalidade
- Acesse `/login` e autentique (Criando a conta ou logando com o admin).
- Vá para `/motos/new` e cadastre uma moto (chassi: exatamente 17 caracteres; placa: 7 caracteres). Se estiver inválido, o formulário mostrará mensagens de erro.
- Liste em `/motos` e verifique a nova moto.

## Troubleshooting (Azure)
- `sqlcmd` falhou (DDL/DML): verifique firewall do Azure SQL e credenciais
- Loop no login: garanta que a senha no banco corresponde ao encoder (repo está com NoOp + `admin123` para DEV)
- Erro 500 em `/motos`: redeploy para garantir templates atualizados; dados de status/modelo precisam existir (template é null-safe)
- Falha de start no Web App: conferir App Settings e ver Log Stream

## Acesso ao Banco de Dados (Azure SQL)
Você pode visualizar e consultar o banco provisionado no Azure SQL de duas formas:

- Azure Portal (Query editor)
	 - Abra o recurso do SQL Database no Portal Azure
	 - Clique em "Query editor (preview) ou Editor de consultas"
	 - Autenticação: SQL Login
		 - Usuário: valor da variável `DB_USERNAME` definida no `deploy-smartmottu.sh`
		 - Senha: valor da variável `DB_PASSWORD` ("db-password" do script)
	 - Exemplos de consulta:
		 - `SELECT TOP 10 * FROM T_SMARTMOTTU_USUARIO;`
		 - `SELECT TOP 10 * FROM T_SMARTMOTTU_MOTO;`

Variáveis úteis no `deploy-smartmottu.sh`:
- `SERVER_NAME`: nome do servidor lógico do Azure SQL (ex.: `sql-server-smartmottu`)
- `DB_NAME`: nome do banco (ex.: `db-smartmottu`)
- `DB_USERNAME`: usuário admin SQL (ex.: `user-smartmottu`)
- `DB_PASSWORD`: senha do admin SQL ("db-password" do script)

## Testes via HTTP (opcional)
Como a aplicação usa login por formulário, recomenda-se testar via navegador. Ainda assim, segue um roteiro opcional:
- Obter sessão autenticada via formulário em `/login`
- Enviar POST de criação de moto para `/motos` com os campos `nmChassi`, `placa`, `unidade`, `statusId`, `modeloId` usando cookies de sessão (complexo com curl; para demonstração use o navegador)

## Vídeo & Repositório
- Vídeo demonstrativo (passo a passo): https://youtu.be/7t5AVv5gmB4
- Repositório: https://github.com/Victor-Estrella/smartmottu-devops-3
