# AI Build Transcript

## Session Context
- Tool: GitHub Copilot (GPT-5.3-Codex)
- Goal: Build a Java 17 Spring Boot personal task manager with CRUD, AI endpoint, UI, tests, and README.

## User Prompt (Summary)
Build a Java 17 Spring Boot REST API with:
- Task CRUD endpoints
- At least one AI-powered endpoint
- Minimal frontend UI
- Unit/integration tests
- README with one-command run instructions
- Include transcript

## Build Log (Condensed)
1. Initialized Spring Boot project skeleton from Spring Initializr with Maven wrapper and dependencies (`web`, `data-jpa`, `validation`, `h2`).
2. Implemented domain model:
   - `Task` entity with `id`, `title`, `description`, `dueDate`, `priority`, `status`
   - enums `Priority`, `TaskStatus`
3. Added persistence:
   - `TaskRepository extends JpaRepository<Task, Long>`
4. Implemented service layer:
   - `TaskService` methods for create/list/get/update/delete
   - `AiTaskSuggestionService` for AI endpoint orchestration
5. Implemented REST API:
   - `TaskController` with `POST/GET/GET{id}/PUT{id}/DELETE{id}` on `/tasks`
   - `POST /tasks/suggest` for AI-generated task suggestion
6. Added AI integration:
   - `AiTaskSuggestionClient` interface
   - `OpenAiTaskSuggestionClient` implementation using OpenAI-compatible Chat Completions API
   - Configurable properties: base URL, model, API key
   - Graceful `503` when API key is missing
7. Added error handling:
   - `TaskNotFoundException`
   - `RestExceptionHandler` for 404/400/503 responses
8. Added minimal UI in `src/main/resources/static/index.html` to:
   - View tasks
   - Create tasks
   - Trigger `/tasks/suggest` and render JSON response
9. Added tests:
   - `TaskServiceTest` (unit tests per service method + not-found behavior)
   - `TaskControllerIntegrationTest` (CRUD endpoints end-to-end)
   - `AiTaskSuggestionEndpointTest` (mocked external AI client)
10. Added reviewer-oriented `README.md` with setup/run/test instructions and AI endpoint examples.

## Prompt List By Component

### 1. Project Scaffolding
- "Create a Java 17 Spring Boot project using Maven with dependencies: web, data-jpa, validation, h2."
- "Generate a clean package structure under `com.example.taskmanager` for controller, service, repository, model, dto, exception, and ai."

### 2. Domain Model (`Task`, enums)
- "Implement a JPA `Task` entity with fields: id, title, description, dueDate, priority, status."
- "Add validation so title is required and keep enums for `Priority` and `TaskStatus`."

### 3. Repository Layer
- "Create `TaskRepository` extending `JpaRepository<Task, Long>` and keep it minimal."

### 4. Service Layer (CRUD)
- "Implement `TaskService` with methods to create, list, get by id, update, and delete tasks."
- "Throw a custom `TaskNotFoundException` when an id does not exist."
- "Use DTOs for request/response mapping and keep service methods focused on business logic."

### 5. REST Controller (CRUD Endpoints)
- "Add `/tasks` endpoints for POST, GET all, GET by id, PUT by id, DELETE by id."
- "Return proper HTTP status codes: 201 for create, 200 for reads/updates, 204 for delete."

### 6. AI Endpoint (`POST /tasks/suggest`)
- "Create an AI-powered endpoint `POST /tasks/suggest` that accepts a natural-language prompt and returns structured task JSON."
- "Define request/response DTOs for the suggest endpoint and include `modelUsed` in response for observability."

### 7. AI Client Integration
- "Implement an `AiTaskSuggestionClient` interface and an `OpenAiTaskSuggestionClient` using an OpenAI-compatible chat completions API."
- "Make base URL, model, and API key configurable via Spring properties."
- "If API key is missing, return a clear 503 error rather than failing silently."

### 8. Error Handling
- "Add a global `RestExceptionHandler` that maps `TaskNotFoundException` to 404 and validation errors to 400."
- "Include a readable error payload for API consumers."

### 9. Minimal Frontend UI
- "Create a simple `index.html` served by Spring static resources to list tasks and create tasks."
- "Add a form/button that calls `/tasks/suggest` and displays the returned JSON."

### 10. Testing
- "Write unit tests for each `TaskService` method (happy path at minimum)."
- "Add integration tests that start Spring context and verify CRUD endpoints end to end."
- "Add a test for `/tasks/suggest` that mocks the external AI client and verifies structured response fields."

### 11. Documentation (`README.md`)
- "Write a reviewer-focused README with setup steps and a single command to build and run locally."
- "Document the AI endpoint with example request/response and expected behavior when API key is missing."
