# Spring Boot Demo
This is a starter Spring Boot app that accesses relational JPA data through a hypermedia-based RESTful front end.

It also secures the repositories with Spring Security, so that only authenticated users can access the REST API.

## What is a hypermedia-based RESTful front end?
HATEOAS (Hypermedia as the Engine of Application State) is a [constraint of the REST application architecture](https://en.wikipedia.org/wiki/HATEOAS).

A hypermedia-driven front end provides information to navigate the site's REST interfaces dynamically by including hypermedia links with the responses. What this means is that you can get a glimpse of what a RESTful API has to offer in advance without knowing the interface contracts.

For example, in this app, if you reach the app root <localhost:8080/springboot-demo> without HATEOAS, it will produce a 404 Error because the resource was not found. With HATEOAS it will show a response like this:

```json
{
  "_links" : {
    "customers" : {
      "href" : "http://localhost:8080/springboot-demo/customers"
    },
    "profile" : {
      "href" : "http://localhost:8080/springboot-demo/profile"
    }
  }
}
```
As you can see it shows all the services available at the current endpoint, in this case I just have one: `customers`. The `profile` link shows a more in-depth information about the services. For example, <localhost:8080/springboot-demo/profile/customers> shows:

```json
{
  "alps" : {
    "version" : "1.0",
    "descriptors" : [ {
      "id" : "customer-representation",
      "href" : "http://localhost:8080/springboot-demo/profile/customers",
      "descriptors" : [ {
        "name" : "firstName",
        "type" : "SEMANTIC"
      }, {
        "name" : "lastName",
        "type" : "SEMANTIC"
      } ]
    }, {
      "id" : "create-customers",
      "name" : "customers",
      "type" : "UNSAFE",
      "rt" : "#customer-representation"
    }, {
      "id" : "get-customers",
      "name" : "customers",
      "type" : "SAFE",
      "rt" : "#customer-representation"
    }, {
      "id" : "delete-customer",
      "name" : "customer",
      "type" : "IDEMPOTENT",
      "rt" : "#customer-representation"
    }, {
      "id" : "patch-customer",
      "name" : "customer",
      "type" : "UNSAFE",
      "rt" : "#customer-representation"
    }, {
      "id" : "get-customer",
      "name" : "customer",
      "type" : "SAFE",
      "rt" : "#customer-representation"
    }, {
      "id" : "update-customer",
      "name" : "customer",
      "type" : "IDEMPOTENT",
      "rt" : "#customer-representation"
    }, {
      "name" : "findByLastName",
      "type" : "SAFE",
      "descriptors" : [ {
        "name" : "lastName",
        "type" : "SEMANTIC"
      } ]
    }, {
      "name" : "findByFirstNameStartsWith",
      "type" : "SAFE",
      "descriptors" : [ {
        "name" : "firstName",
        "type" : "SEMANTIC"
      } ]
    } ]
  }
}
```

While <http://localhost:8080/springboot-demo/customers> will only show:

```json
{
  "_embedded" : {
    "customers" : [ {
      "firstName" : "Martin",
      "lastName" : "Mena",
      "_links" : {
        "self" : {
          "href" : "http://localhost:8080/springboot-demo/customers/1"
        },
        "customer" : {
          "href" : "http://localhost:8080/springboot-demo/customers/1"
        }
      }
    } ]
  },
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/springboot-demo/customers"
    },
    "profile" : {
      "href" : "http://localhost:8080/springboot-demo/profile/customers"
    },
    "search" : {
      "href" : "http://localhost:8080/springboot-demo/customers/search"
    }
  }
}
```