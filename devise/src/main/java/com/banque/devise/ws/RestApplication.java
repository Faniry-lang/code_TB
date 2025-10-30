package com.banque.devise.ws;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class RestApplication extends Application {
    // La configuration est vide, mais l'annotation @ApplicationPath définit le préfixe de l'URL.
}