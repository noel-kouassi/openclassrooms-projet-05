## READ ME

### Configuration de la base de données

- Installer une base de données MySQL (lien : [MySQL Workbench](https://dev.mysql.com/downloads/workbench/))
- Créer un schéma dénommé "test" avec un utilisateur dénommé "user" ayant comme mot de passe "123456"
- Attribuer tous les droits nécessaires à l'utilisateur "user" sur le schéma "test"
- Se connecter au schéma "test" avec l'utilisateur "user"
- Exécuter le script contenu dans le répertoire `ressources/sql/script.sql`

### Installation de l'application

- Cloner l'application via les liens github suivants: 
  - Lien SSH: `git@github.com:noel-kouassi/openclassrooms-projet-05.git`
  - Lien HTTPS: `https://github.com/noel-kouassi/openclassrooms-projet-05.git`
- Installer Java 11 (lien : [OpenJDK Downloads](https://www.openlogic.com/openjdk-downloads?page=1))
- Se positionner dans le répertoire `back` et exécuter la commande : 
  - `mvn clean install`
- Se positionner dans le répertoire `front` et exécuter les commandes suivantes:
  - `npm install`
  - `npm install -D cypress@13.6.6`
  - `npm run start`

### Execution des tests unitaires

- Front-end
  - Se positionner dans le répertoire `front`
  - Exécuter la commande : `npm run test --coverage`
- End-to-end
  - Se positionner dans le répertoire `front`
  - Exécuter la commande : `npx cypress run`
- Back-end
  - Se positionner dans le répertoire `back`
  - Exécuter la commande : `mvn test`

### Génération des rapports de couverture

- Front-end
  - Se positionner dans le répertoire `front/coverage/jest/lcov-report`
  - Cliquer et lancer le fichier `index.html`
- End-to-end
  - Se positionner dans le répertoire `front/coverage/lcov-report`
  - Cliquer et lancer le fichier `index.html`
- Back-end
  - Se positionner dans le répertoire `back/target/site/jacoco`
  - Cliquer et lancer le fichier `index.html`