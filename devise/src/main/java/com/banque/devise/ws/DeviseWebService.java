package com.banque.devise.ws;

import com.banque.devise.entity.Devise;
import com.banque.devise.repository.DeviseRemote;

import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/devises")
@Produces(MediaType.APPLICATION_JSON)
public class DeviseWebService {

    @EJB
    private DeviseRemote deviseRemote;

    @GET
    public Response obtenirToutesDevises() {
        try {
            List<Devise> devises = deviseRemote.obtenirToutesDevises();
            return Response.ok(devises).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération des devises: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/actives")
    public Response obtenirDevisesActives() {
        try {
            List<Devise> devisesActives = deviseRemote.obtenirDevisesActives();
            return Response.ok(devisesActives).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la récupération des devises actives: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/active/{libelle}")
    public Response estDeviseActive(@PathParam("libelle") String libelle) {
        try {
            boolean estActive = deviseRemote.estDeviseActive(libelle);
            // Retourner un JSON simple avec le résultat
            return Response.ok().entity("{\"estActive\": " + estActive + "}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erreur lors de la vérification de la devise: " + e.getMessage())
                    .build();
        }
    }
}