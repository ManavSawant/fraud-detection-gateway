# 🚀 Intelligent Fraud Detection Gateway

---

## 🧠 Overview

This project is a **microservices-based fraud detection system** built using **Spring Cloud Gateway** and **Spring WebFlux**.

It intercepts incoming API requests at the gateway level, evaluates them using a dedicated fraud detection service, and makes real-time decisions to:

- ✅ Allow safe requests  
- ⚠️ Flag suspicious requests (adds metadata)  
- ❌ Block high-risk requests  

The system is designed with **resilience, scalability, and extensibility** in mind.

---

## 🏗️ Architecture

```
Client → API Gateway → Fraud Detection Service → Decision Engine → Backend Service
```

---

## 🔄 Flow

1. Client sends a request  
2. Gateway intercepts using a **Global Filter**  
3. Metadata (IP, User-Agent, Path) sent to Fraud Service  
4. Fraud Service calculates risk score  
5. Gateway applies the decision:

- **ALLOW** → forward request  
- **FLAG** → add header & forward  
- **BLOCK** → deny request  

---

## ⚙️ Tech Stack

- Java 21  
- Spring Boot  
- Spring Cloud Gateway  
- Spring WebFlux (Reactive Programming)  
- Maven  

---

## 🔥 Key Features

### 🔹 1. Gateway Pre-Filter Security
- All requests intercepted before reaching backend  
- Reduces load on business services  

---

### 🔹 2. Microservices Architecture
- Gateway Service  
- Fraud Detection Service  
- Backend/Test Service  

---

### 🔹 3. Decision Engine

| Decision | Behavior |
|--------|--------|
| ALLOW | Request forwarded |
| FLAG | Header added (`X-Fraud-Flag`) |
| BLOCK | Request blocked (403) |

---

### 🔹 4. AI Scoring Layer (Pluggable Design)
- Implemented using interface-based abstraction  
- Currently uses a **simulated AI model**  
- Can be easily replaced with:
  - OpenAI  
  - Together AI  
  - Custom ML models  

---

### 🔹 5. Hybrid Risk Scoring

```
Final Score = 0.7 * AI Score + 0.3 * Rule Score
```

---

### 🔹 6. Resilience & Fault Tolerance

- ⏱️ Timeout handling (2 seconds)  
- 🔓 Fail-Open (low-risk endpoints)  
- 🔒 Fail-Closed (high-risk endpoints like `/admin`)  
- Non-blocking reactive flow  

---

### 🔹 7. Request Mutation

Adds header for flagged requests:

```
X-Fraud-Flag: true
```

---

## 📂 Project Structure

```
gateway-service/
fraud-service/
test-service/
```

---
