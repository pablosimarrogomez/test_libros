# Troubleshooting

## Configurcion CA Certs en Java
Asegurarnos que la variable JAVA_HOME este definida
```
echo $JAVA_HOME
```

En caso de estar vacia, definir la variable. 
```
# Por ejemplo para Java17 en Ubuntu
export JAVA_HOME="/usr/lib/jvm/java-1.17.0-openjdk-amd64"
```

Crear y añadir el certificado CA a los certificados de confianza de Java
```
ca_cert_path="./local/env/services/minica/app/certs/ca_cert.pem"

# Si estás importando en el keystore de CA predeterminado de Java, usa la opción -cacerts:
keytool -import -trustcacerts -file $ca_cert_path -alias my-ca-cert -cacerts

# En caso contrario puedes especificar una ruta especifica.
ca_certs_path="$JAVA_HOME/lib/security/cacerts"
keytool -import -trustcacerts -file $ca_cert_path -alias my-ca-cert -keystore $ca_certs_path
```

## Configuracion SSL ActiveMQ
Copiar keystore en el directorio ssl de los recursos del proyecto
```
ks_path="./local/env/services/minica/app/certs/stores/local-env.com.ks"
ssl_ks_path="./src/main/resources/ssl/activemq-local-env.ks"
cp $ks_path $ssl_ks_path
```

Copiar truststore en el directorio ssl de los recursos del proyecto
```
ts_path="./local/env/services/minica/app/certs/stores/ca-cert.ts"
ssl_ts_path="./src/main/resources/ssl/activemq-local-env.ts"
cp $ts_path $ssl_ts_path
```

## Configuracion SSL Kafka
Crear kafka truststore en el directorio ssl de los recursos del proyecto
```
ts_path="./local/env/services/minica/app/certs/stores/ca-cert.ts"
ssl_ts_path="./src/main/resources/ssl/kafka-local-env.ts"
cp $ts_path $ssl_ts_path
```
