# SmartMottu – Azure (visão e deploy)

Aplicação web Java (Spring Boot) hospedada no Azure App Service (Linux) com banco de dados no Azure SQL Database. Este README foca apenas na Azure: arquitetura, provisionamento, configuração, segurança, operação e passo a passo de deploy.

## Arquitetura na Azure
- Compute: Azure App Service (Linux) executando Java 17 (Spring Boot)
- Plano: App Service Plan (SKU ajustável conforme custo/performance)
- Banco de dados: Azure SQL Database (single database) em um Azure SQL Server lógico
- Configuração: App Settings do Web App para variáveis de ambiente (datasource, porta, etc.)
- Conexão: JDBC com TLS (encrypt=true) para Azure SQL
- Opcional: Application Insights, Azure Key Vault, Managed Identity, Private Endpoint

## Provisionamento e Deploy (script)
O script `deploy-smartmottu.sh` automatiza:
1) Criação de Resource Group, App Service Plan (Linux) e Web App
2) Criação de Azure SQL Server + Database
3) Configuração de App Settings no Web App (ex.: SPRING_DATASOURCE_URL/USERNAME/PASSWORD, PORT)
4) Aplicação de DDL/DML no Azure SQL via `sqlcmd` (criação de tabelas/constraints e seed inicial)

Notas:
- O seed cria apenas o usuário ADMIN com senha `admin123` (para DEV/demo). Para produção, troque para BCrypt e segredos protegidos.
- O script requer Azure CLI autenticada (`az login`) e `bash`. Para executar DDL/DML, requer `sqlcmd` (ou use Azure Cloud Shell Bash).

## Configuração do App Service (Linux)
- Runtime: Java 17
- App Settings relevantes:
	- `SPRING_DATASOURCE_URL` (ex: `jdbc:sqlserver://<servidor>.database.windows.net:1433;database=<db>;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;`)
	- `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
	- `PORT` (o App Service injeta; a aplicação já lê essa variável)
- Logging/Diagnóstico: habilitar Log Stream; considerar Application Insights
- Always On: recomendado para evitar cold starts

## Azure SQL Database
- Firewall: habilitar "Allow Azure services" e/ou IPs necessários
- Conexão segura: TLS por padrão (encrypt=true). Evite `trustServerCertificate=true` em produção
- Esquema/Seed: aplicados pelo `deploy-smartmottu.sh` via `sqlcmd`
- Tamanho/performance: escolher tier (DTU/vCore) conforme custo/uso

## Rede e Segurança
- HTTPS obrigatório (redirect no App Service)
- Segredos: preferir Key Vault + Managed Identity; evitar segredos em texto plano
- Access Restrictions: limitar origem de tráfego quando apropriado
- Banco: considerar Private Endpoint para tráfego privado

## Observabilidade
- Application Insights (opcional): métricas, traces e live metrics
- App Service Diagnostics e Log Stream para troubleshooting

## Escalabilidade e custos
- Scale up: alterar SKU do App Service Plan
- Scale out: aumentar instâncias (manual ou autoscale por métricas)
- Banco: ajustar tier/DTU/vCore no Azure SQL

## Passo a passo (Azure)
1) Pré-requisitos
- Azure CLI logada: `az login`
- Ambiente com `bash` e `sqlcmd` (Windows: Git Bash/WSL + sqlcmd; alternativa: Azure Cloud Shell Bash)

2) Executar o deploy
```bash
./deploy-smartmottu.sh
```

3) Após o deploy
- Acessar: `https://SEU-APP.azurewebsites.net`
- Login DEV: email semeado no script e senha `admin123`
- Logout: GET `/logout`

## Troubleshooting (Azure)
- `sqlcmd` falhou (DDL/DML): verifique firewall do Azure SQL e credenciais
- Loop no login: garanta que a senha no banco corresponde ao encoder (repo está com NoOp + `admin123` para DEV)
- Erro 500 em `/motos`: redeploy para garantir templates atualizados; dados de status/modelo precisam existir (template é null-safe)
- Falha de start no Web App: conferir App Settings e ver Log Stream

## Segurança (produção)
- Trocar NoOp por BCrypt no PasswordEncoder
- Semear ADMIN com senha já criptografada
- Mover segredos para Azure Key Vault e usar Managed Identity
- Considerar Private Endpoint no Azure SQL e Access Restrictions no Web App
