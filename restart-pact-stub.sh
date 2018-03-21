docker rm -f pact-stub
docker run --name pact-stub -d -p 8080:8080 -v "$(pwd)/target/pacts/:/app/pacts" pactfoundation/pact-stub-server -p 8080 -d /app/pacts