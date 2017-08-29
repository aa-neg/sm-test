
Requirements
* gradle (https://gradle.org/install/)
* java 8 (http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html)
* npm/yarn (https://nodejs.org/en/download/) (https://yarnpkg.com/lang/en/docs/install/)



To Run backend tests and server
```
gradle build && java -jar build/libs/siteminder-test-0.1.0.jar --mailgun.apiKey=<INSERT MAILGUN API KEY> --sendgrid.apiKey=<INSERT SENDGRID API KEY HERE>
```

View application on http://localhost:8080


[OPTIONAL] To compile front-end
```
npm install && npm run build
```

TODO

* frontend tests
