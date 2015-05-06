# route-ccrs-web

Exposes the Route CCR calculation as a HTTP service.

## Running

From a `.jar`:

    java -jar ./path/to/route-ccrs-web.jar

From `lein`:

    lein run

From the repl:

    lein repl
    user=> (go)

Requires the following environment variables to be set:

* `DB_HOST` the database server to connect to.
* `DB_NAME` the database instance to connect to.
* `DB_USER` and `DB_PASSWORD` the credentials to use.

You can optionally specify the `PORT` on which the server should run.
If not specified an available port will be selected at random, with
the URL of the server being printed to the console after it starts.

The environment variables can either be set from system environment
variables, a `.lein-env` file, or Java system properties. See the
[environ](https://github.com/weavejester/environ) documentation for
details.

## Using

Make a `GET` request for a part to:

    ; <route-ccrs-web>/parts/<part-number>
    localhost:8080/parts/100105001R03

This will return a [JSON formatted Transit](http://transit-format.org/)
part structure, populated with the current end dates and CCRs. (To
retrieve the part _without_ end dates add `?with-end-dates=false`
to the request URL.)

If the requested part does not exist a `404` response will be
returned. If the part exists but does not conform to the required
schema then a `500` response will be returned with the schema
validation error details in the body (as JSON formatted Transit.)

To refresh the end dates for a part structure, after adding some
`:source` entries for example, `POST` the part structure—again in JSON
encoded Transit format—back to the server at:

    ; <route-ccrs-web>/calculate
    localhost:8080/calculate

The server will recalculate the end dates/CCRs and return the updated
part structure.

## License

Copyright © 2015 Lymington Precision Engineers Co. Ltd.

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
