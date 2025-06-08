## Project Description:

This project uses an H2 in memory database along with a Simulated Storage Service for images.

While the project is running it will keep all the information stored in the DB but once it is stopped all the information
will be lost, in terms of the Storage Service, everytime the service starts it will delete all the objects in the storage,
this allows the application to be consistence and start with a clear environment.

## Key Considerations:

- In terms of the purpose of the service we would choose availability over consistency, we can support eventual consistency.
- The Api is protected by an API KEY, for a request to pass it must contain X-API-KEY header with 5fb5a408-8d5d-4291-b5b1-02d49ac39fff value.
- The Simulated Storage Service is trying to simulate an AWS S3 bucket with presigned URLs, this is basically a directory within the local machine along with custom URLs built in the application.


## Implemented Features:
- Create a user: This insert user information into H2 DB.
```
curl --location 'localhost:8080/api/v1/users' \
  --header 'X-API-KEY: 5fb5a408-8d5d-4291-b5b1-02d49ac39fff' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "name":"pepito",
  "email":"pepito@test.com"
  }'
```

- Create a post: This insert post information into H2 DB and store image into Simulated Storage Service.
```
curl --location 'localhost:8080/api/v1/posts' \
--header 'X-API-KEY: 5fb5a408-8d5d-4291-b5b1-02d49ac39fff' \
--form 'postDto="{\"description\":\"hello world\", \"userEmail\":\"pepito@test.com\"}";type=application/json' \
--form 'image=@"/dirPath/src/test/resources/images/test.png"'
```
- Fetch details of an individual post: This will return post information along with a custom URL to see the image (For example: http://localhost:8080/api/v1/images/1_7648264969221619536.png) .
```
curl --location 'localhost:8080/api/v1/posts/postId' \
--header 'X-API-KEY: 5fb5a408-8d5d-4291-b5b1-02d49ac39fff'
```
- Fetch a list of posts the user made: This will show a list of all the post that a user has made.
```
curl --location 'localhost:8080/api/v1/posts/user/userId' \
--header 'X-API-KEY: 5fb5a408-8d5d-4291-b5b1-02d49ac39fff'
```
- Authenticate a user: this basically is an ApiKey based auth, the key must be provided for any call, otherwise an error will be shown:
```
{
    "errorCode": "INVALID_API_KEY",
    "description": "Provide API Key is not valid"
}
```
- View Image: this basically shows the image for a certain post
```
For example
http://localhost:8080/api/v1/images/1_7648264969221619536.png
```
- To prove that the API is working, please include some tests to verify critical  functionality
## Improvements:

- H2 DB is used for facility in terms of setting up a DB for this test, but it can be improved later with a more complex DB.( For example a SQL Db for maintaining integrity and relationships and NoSQL DB for high availability, fault tolerance and scalability).
- Simulated Storage Service can be replaced by S3 Service.
- Api Key Security can be replaced later with JWT along with Authorization and Resource servers.


## How to run:
- Check application.yml for being sure the app is starting in 8080 port and configure app.images.path to the directory that you want.
- Use gradlew bootRun from terminal to start the service.


## How to run tests:
- Use gradlew clean build or gradlew test