#Web Scraper
Web Scraper built using Spring Boot, Angular, and MongoDB. This tool scrapes the data from the website stores it in the database and will show the data in table view with filters and sorting options in the frontend.

**Technology Used**
- Backend: Spring Boot, MongoDB, Swagger2, JUnit
- Frontend: Angular, Redux, TypeScript, Angular Material

**DEMO**
- Deployed to Heroku Cloud:

  https://shoppers-ecom-app.herokuapp.com

  **Note:** It is running on a free dyno, so the services go to sleep if not in use.
       For the first time, it may take some time to respond.
 
**FEATURES**

- Scrapes the data from the website and pushes the data to database concurrently.
- Shows the data in the frontend in table view.
- Provides option to sort and filter the data.

**Steps to run backend application:**
1. Clone/Download the repository.

2. Set the environmental variables.     
   ACTIVE_PROFILE=<prod/dev>;
   MONGODB_URI=<Connection String>
   CLIENT_URL=<Frontend Client URL>

3. Build the jar file from springboot-server directory.
   ```
      ./mvnw install -DskipTests
   ```

4. Run the jar file from springboot-server directory.
   ```
      java -jar -Djdk.tls.client.protocols=TLSv1.2 target/scraper-data-service.jar
   ```

**Steps to run frontend application:**
1. Run the below command from angular-client directory.
   ```
      ng serve
   ```

**References**  
1. https://ngrx.io/docs
2. https://angular.io/docs
3. https://material.angular.io/
