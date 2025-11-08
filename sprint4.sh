#!/bin/bash
set -e

# Parâmetros principais do App e do banco
RESOURCE_GROUP_APP="rg-smartmottu"
RESOURCE_GROUP_DB="rg-smartmottu-db"
LOCATION="brazilsouth"
APP_SERVICE_PLAN="smartmottu-plan"
WEBAPP_NAME="app-pt-rm556206"
PLAN_SKU="F1"
RUNTIME="JAVA:17-java17"

SQL_SERVER_NAME="sql-server-smartmottu"
SQL_DB_NAME="db-smartmottu"
SQL_ADMIN_USER="user-smartmottu"
SQL_ADMIN_PASSWORD="Sm@rtM0ttU!2025#"

# Registrar provedores necessários
az provider register --namespace Microsoft.Web
az provider register --namespace Microsoft.OperationalInsights
az provider register --namespace Microsoft.ServiceLinker
az provider register --namespace Microsoft.Sql

# Criar resource groups 
az group create --name "$RESOURCE_GROUP_DB" --location "$LOCATION"
az group create --name "$RESOURCE_GROUP_APP" --location "$LOCATION"

# BANCO DE DADOS SQL SERVER
az sql server create \
    --name $SQL_SERVER_NAME \
    --resource-group $RESOURCE_GROUP_DB \
    --location $LOCATION \
    --admin-user $SQL_ADMIN_USER \
    --admin-password $SQL_ADMIN_PASSWORD \
    --enable-public-network true

az sql db create \
    --resource-group $RESOURCE_GROUP_DB \
    --server $SQL_SERVER_NAME \
    --name $SQL_DB_NAME \
    --service-objective Basic \
    --backup-storage-redundancy Local

# Firewall amplo (DEV APENAS). Para produção troque por IP fixo / VPN.
az sql server firewall-rule create \
    --resource-group $RESOURCE_GROUP_DB \
    --server $SQL_SERVER_NAME \
    --name AllowAllDevTEMP \
    --start-ip-address 0.0.0.0 \
    --end-ip-address 255.255.255.255
echo "⚠️  Firewall 0.0.0.0/255.255.255.255 habilitado somente para desenvolvimento."

# CRIAÇÃO DE OBJETOS E DADOS INICIAIS NO BANCO
echo "Criando tabelas, sequences, constraints e dados iniciais no banco..."
sqlcmd \
    -S "$SQL_SERVER_NAME.database.windows.net" \
    -d "$SQL_DB_NAME" \
    -U "$SQL_ADMIN_USER" \
    -P "$SQL_ADMIN_PASSWORD" \
    -l 60 \
    -N \
    -b \
    -i "$(dirname "$0")/script_bd.sql"

# Criar App Service Plan Linux
az appservice plan create \
    --name "$APP_SERVICE_PLAN" \
    --resource-group "$RESOURCE_GROUP_APP" \
    --location "$LOCATION" \
    --sku "$PLAN_SKU" \
    --is-linux

# Criar Web App com runtime Java 17
az webapp create \
    --name "$WEBAPP_NAME" \
    --resource-group "$RESOURCE_GROUP_APP" \
    --plan "$APP_SERVICE_PLAN" \
    --runtime "$RUNTIME"

# Montar JDBC URL e aplicar app settings
SPRING_DATASOURCE_URL="jdbc:sqlserver://$SQL_SERVER_NAME.database.windows.net:1433;database=$SQL_DB_NAME;user=$SQL_ADMIN_USER@$SQL_SERVER_NAME;password=$SQL_ADMIN_PASSWORD;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;"

az webapp config appsettings set \
    --name "$WEBAPP_NAME" \
    --resource-group "$RESOURCE_GROUP_APP" \
    --settings \
        SPRING_DATASOURCE_URL="$SPRING_DATASOURCE_URL" \
        SPRING_DATASOURCE_USERNAME="$SQL_ADMIN_USER" \
    SPRING_DATASOURCE_PASSWORD="$SQL_ADMIN_PASSWORD" \
    SPRING_FLYWAY_ENABLED=true

az webapp restart --name "$WEBAPP_NAME" --resource-group "$RESOURCE_GROUP_APP"

echo "Recursos provisionados e app settings aplicados para $WEBAPP_NAME."


