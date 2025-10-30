package com.banque.centrale.config;

import com.banque.devise.repository.DeviseRemote;
import com.management.courant.remote.ClientRemote;
import com.management.courant.remote.CompteCourantRemote;
import com.management.courant.remote.UtilisateurRemote;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.beans.factory.annotation.Value;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

@Configuration
public class EJBConfig {

    @Value("${ejb.server.courant.url}")
    private String ejbServerCourantUrl;

    @Value("${ejb.server.devise.url}")
    private String ejbServerDeviseUrl;

    private Context createCourantContext() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put(Context.PROVIDER_URL, ejbServerCourantUrl);
        return new InitialContext(props);
    }

    private Context createDeviseContext() throws NamingException {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        props.put(Context.PROVIDER_URL, ejbServerDeviseUrl);
        props.put(Context.SECURITY_PRINCIPAL, "berthin");
        props.put(Context.SECURITY_CREDENTIALS, "berthin");
        return new InitialContext(props);
    }

    /** ============================
     *  🧩 BEANS POUR COMPTE COURANT (Serveur: 127.0.0.1:8310)
     *  ============================ */

    @Bean
    public ClientRemote clientRemote() throws NamingException {
        Context ctx = createCourantContext();
        return (ClientRemote)
                ctx.lookup("ejb:/banque-compte-courant-1.0/ClientService!com.management.courant.remote.ClientRemote");
    }

    @Bean
    public CompteCourantRemote compteCourantRemote() throws NamingException {
        Context ctx = createCourantContext();
        return (CompteCourantRemote)
                ctx.lookup("ejb:/banque-compte-courant-1.0/CompteCourantService!com.management.courant.remote.CompteCourantRemote");
    }

    /** ============================
     *  🧩 BEANS POUR DEVISE (Serveur: 127.0.0.1:8320)
     *  ============================ */

    @Bean
    public DeviseRemote deviseRemote() throws NamingException {
        Context ctx = createDeviseContext();
        return (DeviseRemote)
                ctx.lookup("ejb:/banque-devise-1.0-SNAPSHOT/DeviseService!com.banque.devise.repository.DeviseRemote");
    }

    /** ============================
     *  🧩 MÉTHODES POUR BEANS STATEFUL (Serveur: 127.0.0.1:8310)
     *  ============================ */

    // ⚠️ Ne pas en faire un @Bean, car chaque utilisateur doit avoir sa propre instance
    public UtilisateurRemote createUtilisateurRemote() throws NamingException {
        try {
            Context ctx = createCourantContext();
            return (UtilisateurRemote)
                    ctx.lookup("ejb:/banque-compte-courant-1.0/UtilisateurService!com.management.courant.remote.UtilisateurRemote?stateful");
        } catch (NamingException e) {
            throw new RuntimeException("Impossible de créer une session utilisateur stateful", e);
        }
    }
}