# Personal Task Manager API

Spring Boot 3.5 + Java 17 REST API for managing personal tasks, with an AI-powered endpoint that converts natural language into a structured task suggestion.

## Setup Instructions

1. Install Java 17.
2. Verify Java:

```bash
java -version
```

Expected: output includes `17.x`.

3. Clone the repository and move into it:

```bash
git clone <your-repo-url>
cd assessment
```

No database setup is required (H2 in-memory database is built in).

## Run The Project (Single Command)

From the repository root, run exactly one command:

### Windows

```powershell
./mvnw.cmd spring-boot:run
```

### macOS/Linux

```bash
./mvnw spring-boot:run
```

This single command builds and starts the API.

When startup is complete:
- API base URL: http://localhost:8080
- UI: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console

## AI-Powered Endpoint

### Endpoint

- Method: POST
- Path: /tasks/suggest

### What It Does

Accepts a plain-language prompt and returns a structured task suggestion JSON.

If an AI API key is configured, the app calls an OpenAI-compatible chat-completions API.
If no key is configured, the endpoint responds with HTTP 503 and a clear error message.

### AI Configuration

Set API key through environment variable:

```bash
AI_OPENAI_API_KEY=<your-key>
```

Optional overrides:
- `AI_OPENAI_BASE_URL` (default: `https://api.openai.com/v1`)
- `AI_OPENAI_MODEL` (default: `gpt-4o-mini`)

### Example Request

```http
POST /tasks/suggest
Content-Type: application/json

{
  "prompt": "remind me to submit the quarterly report before Friday"
}
```

### Example Response

```json
{
  "title": "Submit quarterly report",
  "description": "Compile metrics and submit report",
  "dueDate": "2026-04-03",
  "priority": "HIGH",
  "status": "TODO",
  "modelUsed": "gpt-4o-mini"
}
```

## CRUD Endpoints

- POST /tasks
- GET /tasks
- GET /tasks/{id}
- PUT /tasks/{id}
- DELETE /tasks/{id}

## Run Tests

### Windows

```powershell
./mvnw.cmd test
```

### macOS/Linux

```bash
./mvnw test
```
