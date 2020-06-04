<br />
<p align="center">
  <h3 align="center">Hype Logger for Java Applications</h3>

  <p align="center">
    Fluent logging API that generates better structured logs and it is easy to test. Depends just on SLF4J.
    <br />
    <br />
    <a href="https://github.com/flsusp/hyplogger/issues">Report Bug</a>
    ·
    <a href="https://github.com/flsusp/hyplogger/issues">Request Feature</a>
  </p>
</p>



## Table of Contents

* [About the Project](#about-the-project)
* [Getting Started](#getting-started)
  * [Installation](#installation)
* [Usage](#usage)
* [Roadmap](#roadmap)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)



## About The Project

There are many logging APIs available in the JVM platform and SLF4J has become the mainstream solution. The versatility 
and the integration mechanisms provided by SLF4J are great, and we think we should build on top of it an API that allows
generating structured logs. With the new tools around log analytics, having logs that are easy to search through is critical
for most enterprise applications. On the other hand, searching logs using regular expressions are not the most scalable strategy.

### Structured Logs

A log message have been historically used merely as an informational asset to help on troubleshooting applications. But with
the scale of applications today, troubleshooting is no longer the only requirement for logging and we started to use logs as
a tool for observing the behaviour of applications in a general view.

So, for that kind of requirement, textual log messages are not the best approach. Most of the new tools that handles large scaled
logs uses field mapping as a mechanism of indexing the log messages in a way that it is possible to handle this kind of scale.
Field mapping, basically, consists in identifying the pattern `field=value` in the log message and creating a field and an
index on this field, so it is possible to query logs for this field.

Example of structured log:

```
18:37:22.665 [main] INFO someField=value otherField=otherValue
```

### Depends On
This section should list any major frameworks that you built your project using. Leave any add-ons/plugins for the acknowledgements section. Here are a few examples.
* [SLF4J](http://www.slf4j.org/)



## Getting Started

### Installation

#### Maven

```
<dependency>
    <groupId>com.github.flsusp.hyplogger</groupId>
    <artifactId>hyplogger</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Usage

TBD


## Roadmap

See the [open issues](https://github.com/flsusp/hyplogger/issues) for a list of proposed features (and known issues).



## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



## License

Distributed under the Apache 2.0 License. See `LICENSE` for more information.



## Contact

Fábio Lima Santos - [@flsusp](https://twitter.com/flsusp) - flsusp@gmail.com

Project Link: [https://github.com/flsusp/hyplogger](https://github.com/flsusp/hyplogger)
