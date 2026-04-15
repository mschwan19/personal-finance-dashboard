import Keycloak from 'keycloak-js';

const keycloakConfig = {
    url: 'http://localhost:8081',
    realm: 'finvo',
    clientId: 'finvo-vue'
};

const keycloak = new Keycloak(keycloakConfig);

export const initKeycloak = () => {
    return keycloak.init({
        onLoad: 'check-sso',
    });
};

export default keycloak;