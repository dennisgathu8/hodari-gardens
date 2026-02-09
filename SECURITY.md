# Security Policy

## Reporting a Vulnerability

We take the security of Hodari Gardens Resort seriously. If you believe you have found a security vulnerability, please report it to us responsibly.

**Please do not open a public issue.** Instead, send an email to:
`dennisgathu8@gmail.com`

Include as much detail as possible, including steps to reproduce the issue. We aim to respond within 48 hours.

## Security Invariants

The following principles guide the security architecture of this platform:

1. **Immutability**: All application state is treated as immutable, preventing a wide class of state-based injection attacks.
2. **Stateless Backend**: The API layer stores no session state on the server, leveraging cryptographic tokens where applicable.
3. **Data-Driven Validation**: All incoming data is validated against strict Malli schemas before processing.
4. **Secure Defaults**: Production builds enforce HTTPS, HSTS, and secure cookie headers.
5. **Principle of Least Privilege**: The application runs with minimal filesystem and network permissions in its container.

---
Copyright © 2026 dennisgathu8. All rights reserved.
