# url-redirect

This is a standard sbt project running Akka Http.  To get started:

* Clone this repository.
* Run sbt
* The `test` command will run the tests.
* The `run` command will start the server as localhost on port 8080.

Two endpoints are exposed:
* The `/shorten` endpoint takes a simple json payload and returns a shortened url.
* The `/redirect` endpoint matches the shorted Url with the original URL and sends a redirect to the original website.  If there is no match the status code`No Content` is returned.

To create a shortened url please use the following Curl command.

Curl Post - expect shortened URL as text:

 `curl --header "Content-Type: application/json"  --request POST --data '{"url":"http://www.nike.com"}' http://localhost:8080/shorten`
 
 Expect a text url to be returned that looks like this: `http://localhost:8080/redirect/1`
 
 Paste the above URL into a browser to see the redirect.  Alternatively you can run:
 
 `curl -v http://localhost:8080/redirect/42`
 
