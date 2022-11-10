build: # compile and build application
	mvn clean package

build-docker-image: # build docker image
	@docker build . --tag currency-app

test: # compile and test
	mvn clean test

# compile, build docker image and run application
build-run: build build-docker-image
	@docker run -it --rm -p 8080:8080 currency-app:latest

run: # run docker image
	@docker run -it --rm -p 8080:8080 currency-app:latest
