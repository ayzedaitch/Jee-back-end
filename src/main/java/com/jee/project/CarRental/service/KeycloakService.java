package com.jee.project.CarRental.service;

import com.jee.project.CarRental.entity.Customer;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakService {
    private final String keycloakAdminUsername = "keycloak@admin.com";
    private final String keycloakAdminPassword = "admin";
    private final String keycloakAdminRealm = "JEE";
    private final String keycloakAdminClientId = "admin-cli";
    private final String keycloakAdminGrantType = "password";
    private final String keycloakServerUrl = "http://localhost:8080";
    private final Keycloak keycloak = KeycloakBuilder.builder()
            .serverUrl(keycloakServerUrl)
            .realm(keycloakAdminRealm)
            .username(keycloakAdminUsername)
            .password(keycloakAdminPassword)
            .clientId(keycloakAdminClientId)
            .grantType(keycloakAdminGrantType)
            .build();

    public void registerCustomer(Customer customer){
        UsersResource userResource = keycloak.realm(keycloakAdminRealm).users();
        List<UserRepresentation> existingUsers = userResource.search(customer.getEmail());

        if (!existingUsers.isEmpty()){
            throw new RuntimeException("Customer already exists.");
        }

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmail(customer.getEmail());
        userRepresentation.setFirstName(customer.getFirstName());
        userRepresentation.setLastName(customer.getLastName());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(customer.getPassword());
        credentialRepresentation.setTemporary(false);

        userRepresentation.setCredentials(List.of(credentialRepresentation));
        keycloak.realm(keycloakAdminRealm).users().create(userRepresentation);

        String userId = keycloak.realm(keycloakAdminRealm).users().search(userRepresentation.getEmail()).get(0).getId();
        String roleName = "CUSTOMER";
        RoleRepresentation role = keycloak.realm(keycloakAdminRealm).roles().get(roleName).toRepresentation();
        keycloak.realm(keycloakAdminRealm).users().get(userId).roles().realmLevel().add(List.of(role));
    }
}
