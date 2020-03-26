# User Accreditation at Yieldstreet

To follow the steps in this task, you will need the correct version of Java and a build tool. You can build Play projects with any Java build tool. Since sbt takes advantage of Play features such as auto-reload, the tutorial describes how to build the project with sbt. 

Prerequisites include:

* Java Software Developer's Kit (SE) 1.8 or higher
* sbt 0.13.15 or higher (we recommend 1.2.3)

To check your Java version, enter the following in a command window:

`java -version`

To check your sbt version, enter the following in a command window:

`sbt sbtVersion`

If you do not have the required versions, follow these links to obtain them:

* [Java SE](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [sbt](http://www.scala-sbt.org/download.html)

## Build and run the project

This API Play project was created from a seed template. It includes all Play components and an Akka HTTP server. The project is also configured with filters for Cross-Site Request Forgery (CSRF) protection and security headers.

To build and run the project:

1. Use a command window to change into the example project directory, for example: `cd accreditation`

2. Build the project. Enter: `sbt run`. The project builds and starts the embedded HTTP server. Since this downloads libraries and dependencies, the amount of time required depends partly on your connection's speed.

3. After the message `Server started, ...` displays, use URL in a test client: <http://localhost:9000/user/accreditation> choose POST method , "applications/json" body content type and following JSON:
{
  "user_id": "g8NlYJnk7zK9BlB1J2Ebjs0AkhCTpE1V",
  "payload": {
    "accreditation_type": "BY_INCOME",
    "documents": [
      {
        "name": "2018.pdf",
        "mime_type": "application/pdf",
        "content": "ICAiQC8qIjogWyJzcmMvKiJdCiAgICB9CiAgfQp9Cg=="
      },
      {
        "name": "2019.jpg",
        "mime_type": "image/jpeg",
        "content": "91cy1wcm9taXNlICJeMi4wLjUiCiAgICB0b3Bvc29ydCAiXjIuMC4yIgo="
      }
    ]
  }
}

The Play application responds with JSON:
{
"success": true,
"accredited": true
}

Accridirted calculated rundomly with 70% succsess rate.