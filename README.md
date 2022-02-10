# MessyFiles / Design / Data Store

ArangoDB or OrientDB? Let's get some hands-on in both!

## ArangoDB

### Set Up

First start an ArangoDB instance, e.g., with Docker:

```shell
docker run --name arangodb -e ARANGO_ROOT_PASSWORD=changeit -p 8529:8529 -d arangodb
```

Then initialise the database using the `InitialiseArangoDb` script.
