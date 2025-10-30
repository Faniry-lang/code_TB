FROM quay.io/wildfly/wildfly:37.0.1.Final-jdk21

# Copie ton application .jar
COPY banque-devise.war /opt/jboss/wildfly/standalone/deployments/

# Ouverture des ports (8080 pour l'application, 9990 pour l'administration)
EXPOSE 8080 9990

# Lancement du serveur WildFly en mode standalone
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]