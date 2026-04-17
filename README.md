# Client-Server-Architectures---Coursework-


## Conceptual Report

### Part 1: Service Architecture & Setup
**Q: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.**

**Answer:** 
By default, JAX-RS Resource classes are completely request-scoped. The JAX-RS runtime instantiates a brand-new object of the Resource class for every single incoming HTTP request, and the instance is garbage-collected once the response is returned. 
This architectural decision heavily impacts how we handle in-memory data: because resource instances do not persist between network calls, any standard instance variables (like a normal `ArrayList` or `HashMap`) are wiped clean immediately. To prevent data loss and maintain state across the entire API, our data holding structures must be separated from the resource instances (e.g., by utilizing singleton service layers, static collections, or dependency injection). Furthermore, because multiple concurrent HTTP requests will spawn multiple resource instances accessing that shared static state simultaneously, we must utilize thread-safe data structures like `ConcurrentHashMap` or implement strict synchronization blocks to prevent catastrophic race conditions.

**Q: Why is the provision of "Hypermedia" (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?**

**Answer:**
Hypermedia as the Engine of Application State (HATEOAS) transitions an API from being a static database interface to a dynamic, self-discoverable system. By including hypermedia links (such as "self," "next," or "delete") directly inside the JSON response bodies, the server actively guides the client on what actions are currently permitted. 
This benefits client developers significantly because it decouples them from hardcoded URLs and rigid static documentation. If the backend URI routing structure changes in the future, clients dynamically reading hypermedia links will automatically adapt and continue working without code changes. Additionally, the server can use links to dictate state transitions—for example, omitting a "delete" link if a room currently contains sensors, thereby preventing the frontend from even rendering a flawed delete button.

### Part 2: Room Management
**Q: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.**

**Answer:** 
Returning a full list of expanded room objects increases the payload size and network bandwidth consumption, which can negatively impact performance (especially on mobile networks or large datasets). However, it vastly improves client-side processing efficiency because the client receives everything it needs to render a UI in a single HTTP round-trip (avoiding the "N+1 queries" problem). 
Conversely, returning only a list of room IDs significantly shrinks the initial payload for very fast network transfer. But it pushes the burden onto client-side processing: if the UI needs to display the room names and capacities, the client must painstakingly loop through the IDs and dispatch dozens of subsequent HTTP GET requests, increasing latency and server load. A common middle-ground is returning a "summary" object that includes the ID and basic identifying info like Name.

**Q: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.**

**Answer:** 
Yes, the DELETE operation is strictly idempotent. Idempotency guarantees that executing the identical request multiple times yields the same final state on the server as executing it once. 
If a client mistakenly spam-clicks a delete button and sends the exact same `DELETE /rooms/LIB-301` request five times, the first request will successfully remove the room and return a `204 No Content`. The subsequent four requests will look for `LIB-301`, fail to find it, and return a `404 Not Found`. Despite the varying HTTP status codes returned to the client, the actual resource state on the server remains completely unchanged after the first execution (the room remains absent).

### Part 3: Sensor Operations & Linking
**Q: We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?**

**Answer:** 
The `@Consumes(MediaType.APPLICATION_JSON)` annotation acts as a strict firewall and contract for the endpoint. If a client attempts to bypass this by sending a POST request with an unsupported `Content-Type` header (like `text/plain` or `application/xml`), the JAX-RS runtime evaluates this mismatch against the routing tree long before the execution thread reaches our actual Java method. 
Because our resource cannot consume that media type, JAX-RS immediately intercepts and rejects the request at the framework level, automatically responding to the client with an HTTP `415 Unsupported Media Type` error. This guarantees that our underlying code is never forced to catch parsing errors from maliciously or incorrectly formatted payload data.

**Q: You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/v1/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?**

**Answer:** 
In REST architecture, the core URL path denotes the hierarchy of physical resources (e.g., `/sensors`), while query parameters provide functional modifiers to sort or filter those resources. 
Using a path variable like `/sensors/type/CO2` establishes a rigid, hierarchical routing paradigm that falsely implies "type" is a sub-resource. The query parameter approach (`/sensors?type=CO2`) is vastly superior because it is infinitely composable. If a client eventually needed to search for active CO2 sensors, they could simply append parameters (`?type=CO2&status=ACTIVE`). Achieving this same multi-dimensional flexibility using static path variables would require developing messy, convoluted matrices of URLs that violate standard RESTful design.

### Part 4: Deep Nesting with Sub-Resources
**Q: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?**

**Answer:** 
The Sub-Resource Locator pattern allows a parent root class (`SensorResource`) to process intermediate path variables, and then dynamically return an instance of a sub-resource class (`SensorReadingResource`) to handle the rest of the deep URL. 
This provides astronomical architectural benefits in terms of Separation of Concerns. If we strictly defined every nested permutation (e.g. GET readings, POST readings, GET specific reading, DELETE reading) inside a single `SensorResource` file, it would rapidly degrade into an unmaintainable "God Object" with thousands of lines of code. By delegating path execution to dedicated locator classes, we break the system down into small, highly cohesive, single-responsibility files that are substantially easier to unit test, debug, and safely modify within a large development team.

### Part 5: Advanced Error Handling & Exception Mapping
**Q: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?**

**Answer:** 
HTTP `404 Not Found` is traditionally utilized strictly at the routing level to indicate that the requested target endpoint simply does not exist on the server (like `POST /wrong-url`). 
When a client successfully reaches `POST /sensors`, they have hit a valid endpoint. If they provide a syntactically correct JSON payload, but the `roomId` specified inside that JSON points to a non-existent database entry, a 404 is misleading. The server *did* find the endpoint. HTTP `422 Unprocessable Entity` is perfect for this exact scenario: it communicates to the client that the server fundamentally understood the content and syntax, but semantic domain validation (the referential integrity check for the room ID) failed.

**Q: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?**

**Answer:** 
Leaking a raw Java stack trace via an unhandled HTTP 500 error is categorized as a critical Information Exposure vulnerability. Stack traces serve as a highly detailed architectural blueprint of the backend system for malicious actors. 
Attackers can harvest:
1. **Dependency Intelligence**: Stack traces expose exactly which frameworks (e.g. Jersey, Hibernate) and specific version strings are being utilized, allowing attackers to check CVE databases for known library exploits.
2. **File & Directory Structures**: They reveal custom internal package structures, mapping the application’s domain logic.
3. **Database Configurations**: Traces triggered by data layer crashes can expose table names, internal database schemas, or even raw database credentials or constraints if an improperly sanitized SQL exception bubbles up to the top level. 
By wrapping exceptions in mappers and returning sanitized JSON errors, we eliminate this critical reconnaissance vector.
