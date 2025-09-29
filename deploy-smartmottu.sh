#!/bin/bash
set -e

# VARIÁVEIS #
RESOURCE_GROUP_NAME="rg-smartmottu"
WEBAPP_NAME="smartmottu-api"
APP_SERVICE_PLAN="smartmottu-plan"
LOCATION="brazilsouth"
RUNTIME="JAVA:17-java17"

RG_DB_NAME="rg-smartmottu-db"
DB_USERNAME="user-smartmottu"
DB_NAME="db-smartmottu"
DB_PASSWORD="Sm@rtM0ttU!2025#"
SERVER_NAME="sql-server-smartmottu"

APP_INSIGHTS_NAME="ai-smartmottu"
GITHUB_REPO_NAME="Victor-Estrella/smartmottu-devops-3"
BRANCH="main"


# PROVIDERS E EXTENSÕES
az provider register --namespace Microsoft.Web
az provider register --namespace Microsoft.Insights
az provider register --namespace Microsoft.OperationalInsights
az provider register --namespace Microsoft.ServiceLinker
az provider register --namespace Microsoft.Sql
az extension add --name application-insights || true


az group create --name $RG_DB_NAME --location $LOCATION
az group create --name $RESOURCE_GROUP_NAME --location $LOCATION

# BANCO DE DADOS SQL SERVER
az sql server create \
    --name $SERVER_NAME \
    --resource-group $RG_DB_NAME \
    --location $LOCATION \
    --admin-user $DB_USERNAME \
    --admin-password $DB_PASSWORD \
    --enable-public-network true

az sql db create \
    --resource-group $RG_DB_NAME \
    --server $SERVER_NAME \
    --name $DB_NAME \
    --service-objective Basic \
    --backup-storage-redundancy Local

# Firewall amplo (DEV APENAS). Para produção troque por IP fixo / VPN.
az sql server firewall-rule create \
    --resource-group $RG_DB_NAME \
    --server $SERVER_NAME \
    --name AllowAllDevTEMP \
    --start-ip-address 0.0.0.0 \
    --end-ip-address 255.255.255.255
echo "⚠️  Firewall 0.0.0.0/255.255.255.255 habilitado somente para desenvolvimento."

# CRIAÇÃO DE OBJETOS E DADOS INICIAIS NO BANCO
echo "Criando tabelas, sequences, constraints e dados iniciais no banco..."
sqlcmd -S "$SERVER_NAME.database.windows.net" -d "$DB_NAME" -U "$DB_USERNAME" -P "$DB_PASSWORD" -l 60 -N -b <<'EOF'
CREATE SEQUENCE SQ_T_SMARTMOTTU_USUARIO START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SQ_T_SMARTMOTTU_TIPO_MOTO START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SQ_T_SMARTMOTTU_STATUS_MOTO START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SQ_T_SMARTMOTTU_MOTO START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE SQ_T_SMARTMOTTU_ALUGUEL START WITH 1 INCREMENT BY 1;

-- Tabelas
CREATE TABLE T_SMARTMOTTU_STATUS_MOTO (
    id_status BIGINT NOT NULL PRIMARY KEY DEFAULT NEXT VALUE FOR SQ_T_SMARTMOTTU_STATUS_MOTO,
    status VARCHAR(50) NOT NULL,
    data DATE
);

CREATE TABLE T_SMARTMOTTU_TIPO_MOTO (
    id_tipo BIGINT NOT NULL PRIMARY KEY DEFAULT NEXT VALUE FOR SQ_T_SMARTMOTTU_TIPO_MOTO,
    nm_tipo VARCHAR(50) NOT NULL,
    CHECK (nm_tipo IN ('MOTTU_SPORT_110I','MOTTU_SPORT_ESD_2025','MOTTU_POP_100','MOTTU_POP_150','MOTTU_ELETRICA_X'))
);

CREATE TABLE T_SMARTMOTTU_USUARIO (
    id_usuario BIGINT NOT NULL PRIMARY KEY DEFAULT NEXT VALUE FOR SQ_T_SMARTMOTTU_USUARIO,
    nome VARCHAR(80) NOT NULL,
    email VARCHAR(120) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER'
);

CREATE TABLE T_SMARTMOTTU_MOTO (
    id_moto BIGINT NOT NULL PRIMARY KEY DEFAULT NEXT VALUE FOR SQ_T_SMARTMOTTU_MOTO,
    nm_chassi VARCHAR(17) NOT NULL,
    placa VARCHAR(7) NOT NULL,
    unidade VARCHAR(100),
    fk_id_status BIGINT NOT NULL,
    fk_id_tipo BIGINT NOT NULL,
    fk_id_usuario BIGINT NULL,
    CONSTRAINT FK_MOTO_STATUS FOREIGN KEY (fk_id_status) REFERENCES T_SMARTMOTTU_STATUS_MOTO(id_status),
    CONSTRAINT FK_MOTO_TIPO FOREIGN KEY (fk_id_tipo) REFERENCES T_SMARTMOTTU_TIPO_MOTO(id_tipo),
    CONSTRAINT FK_MOTO_USUARIO FOREIGN KEY (fk_id_usuario) REFERENCES T_SMARTMOTTU_USUARIO(id_usuario)
);

-- Tabela de Aluguel
CREATE TABLE T_SMARTMOTTU_ALUGUEL (
    id_aluguel   BIGINT NOT NULL PRIMARY KEY DEFAULT NEXT VALUE FOR SQ_T_SMARTMOTTU_ALUGUEL,
    fk_usuario_id BIGINT NOT NULL,
    fk_moto_id    BIGINT NOT NULL,
    data_inicio   DATE   NOT NULL,
    data_fim      DATE       NULL,
    valor_total   DECIMAL(10,2) NULL,
    status        VARCHAR(20) NOT NULL,
    CONSTRAINT FK_ALUGUEL_USUARIO FOREIGN KEY (fk_usuario_id)
        REFERENCES T_SMARTMOTTU_USUARIO (id_usuario),
    CONSTRAINT FK_ALUGUEL_MOTO FOREIGN KEY (fk_moto_id)
        REFERENCES T_SMARTMOTTU_MOTO (id_moto)
);

-- Inserts
INSERT INTO T_SMARTMOTTU_STATUS_MOTO (status, data) VALUES ('ATIVO', '2025-01-10');
INSERT INTO T_SMARTMOTTU_STATUS_MOTO (status, data) VALUES ('MANUTENCAO', '2025-02-15');
INSERT INTO T_SMARTMOTTU_STATUS_MOTO (status, data) VALUES ('CANCELADO', '2025-03-20');
INSERT INTO T_SMARTMOTTU_STATUS_MOTO (status, data) VALUES ('BLOQUEADO', '2025-04-25');
INSERT INTO T_SMARTMOTTU_STATUS_MOTO (status, data) VALUES ('INATIVO', '2025-05-30');
INSERT INTO T_SMARTMOTTU_STATUS_MOTO (status, data) VALUES ('ALUGADA', '2025-06-02');

INSERT INTO T_SMARTMOTTU_TIPO_MOTO (nm_tipo) VALUES ('MOTTU_SPORT_110I');
INSERT INTO T_SMARTMOTTU_TIPO_MOTO (nm_tipo) VALUES ('MOTTU_SPORT_ESD_2025');
INSERT INTO T_SMARTMOTTU_TIPO_MOTO (nm_tipo) VALUES ('MOTTU_POP_100');
INSERT INTO T_SMARTMOTTU_TIPO_MOTO (nm_tipo) VALUES ('MOTTU_POP_150');
INSERT INTO T_SMARTMOTTU_TIPO_MOTO (nm_tipo) VALUES ('MOTTU_ELETRICA_X');

INSERT INTO T_SMARTMOTTU_USUARIO (nome, email, senha, role) VALUES ('Mauricio Silva', 'mauricio@gmail.com', '{noop}senha123', 'USER');
INSERT INTO T_SMARTMOTTU_USUARIO (nome, email, senha, role) VALUES ('Rodrigo Vieira', 'rodrigo@gmail.com', '{noop}senha456', 'USER');
INSERT INTO T_SMARTMOTTU_USUARIO (nome, email, senha, role) VALUES ('Renato Souza', 'renato@gmail.com', '{noop}7852578', 'USER');
INSERT INTO T_SMARTMOTTU_USUARIO (nome, email, senha, role) VALUES ('Maria Dantas', 'maria@gmail.com', '{noop}abcdef', 'USER');
INSERT INTO T_SMARTMOTTU_USUARIO (nome, email, senha, role) VALUES ('Joao Pedro', 'joao@gmail.com', '{noop}123456', 'USER');
INSERT INTO T_SMARTMOTTU_USUARIO (nome, email, senha, role) VALUES ('Administrador', 'admin@email.com', '{bcrypt}$2a$10$veKob3hpyAUsj3R7x4QFgOc8R4I6DrHU9aTmANETcCq.Xgy4NCgmW', 'ADMIN');

INSERT INTO T_SMARTMOTTU_MOTO (nm_chassi, placa, unidade, fk_id_status, fk_id_tipo) VALUES ('9MCNSL953VV182782', 'HMK9012', 'Bras', 1, 1);
INSERT INTO T_SMARTMOTTU_MOTO (nm_chassi, placa, unidade, fk_id_status, fk_id_tipo) VALUES ('1PSCNS391DX139403', 'LPS0292', 'Butanta', 1, 2);
INSERT INTO T_SMARTMOTTU_MOTO (nm_chassi, placa, unidade, fk_id_status, fk_id_tipo) VALUES ('5MDPEM424SD029432', 'MSK2145', 'Carrao', 2, 1);
INSERT INTO T_SMARTMOTTU_MOTO (nm_chassi, placa, unidade, fk_id_status, fk_id_tipo) VALUES ('9BWZZZ377VT004252', 'KSH2323', 'Lapa', 2, 2);
GO
EOF

# APPLICATION INSIGHTS
az monitor app-insights component create \
    --app $APP_INSIGHTS_NAME \
    --location $LOCATION \
    --resource-group $RESOURCE_GROUP_NAME \
    --application-type web

CONNECTION_STRING=$(az monitor app-insights component show \
    --app $APP_INSIGHTS_NAME \
    --resource-group $RESOURCE_GROUP_NAME \
    --query connectionString \
    --output tsv)

# APP SERVICE PLAN + WEBAPP
az appservice plan create \
    --name $APP_SERVICE_PLAN \
    --resource-group $RESOURCE_GROUP_NAME \
    --location $LOCATION \
    --sku F1 \
    --is-linux

az webapp create \
    --name $WEBAPP_NAME \
    --resource-group $RESOURCE_GROUP_NAME \
    --plan $APP_SERVICE_PLAN \
    --runtime "$RUNTIME"

# CONFIGURAR VARIÁVEIS DO APP
SPRING_DATASOURCE_URL="jdbc:sqlserver://$SERVER_NAME.database.windows.net:1433;database=$DB_NAME;user=$DB_USERNAME@$SERVER_NAME;password=$DB_PASSWORD;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"

az webapp config appsettings set \
        --name "$WEBAPP_NAME" \
        --resource-group "$RESOURCE_GROUP_NAME" \
        --settings \
            APPLICATIONINSIGHTS_CONNECTION_STRING="$CONNECTION_STRING" \
            ApplicationInsightsAgent_EXTENSION_VERSION="~3" \
            XDT_MicrosoftApplicationInsights_Mode="Recommended" \
            XDT_MicrosoftApplicationInsights_PreemptSdk="1" \
            SPRING_DATASOURCE_USERNAME=$DB_USERNAME \
            SPRING_DATASOURCE_PASSWORD=$DB_PASSWORD \
            SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL \
            SPRING_FLYWAY_ENABLED=true 

az webapp restart --name $WEBAPP_NAME --resource-group $RESOURCE_GROUP_NAME

# (Opcional) Adicionar GitHub Actions para deploy contínuo
if [ "$GITHUB_REPO_NAME" != "organizacao/repositorio" ]; then
    echo "⚙️  Configurando GitHub Actions (deploy contínuo)..."
    az webapp deployment github-actions add \
        --name $WEBAPP_NAME \
        --resource-group $RESOURCE_GROUP_NAME \
        --repo $GITHUB_REPO_NAME \
        --branch $BRANCH \
        --login-with-github || echo "(Aviso) Não foi possível configurar GitHub Actions automaticamente."
fi

echo "✅ Deploy concluído com sucesso!"
echo "🌐 URL: https://$WEBAPP_NAME.azurewebsites.net"
echo "📊 Application Insights: $APP_INSIGHTS_NAME"
echo "🗄  Banco: $DB_NAME @ $SERVER_NAME"
echo "🔐 Lembre de restringir firewall em produção."