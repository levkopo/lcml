# LCML
[![CircleCI](https://circleci.com/gh/levkopo/lcml.svg?style=svg)](https://circleci.com/gh/levkopo/lcml)

LC Markup Language

## Example
```lcml
document {
    title = "LCML simple document"
    authors [
        "levkopo"
    ]
}

database {
    server = "192.168.0.1"
    port = 8923i
    maxConnections = 50i
}

servers (
    default = 2i
) [
    (type = "alpha"){
        ip = "127.0.0.1"
        port = 80
    }
    (type = "beta"){
        ip = "127.0.0.2"
        port = 80
    }
    (type = "release"){
        ip = "127.0.0.3"
        port = 433
    }
]

```
 
