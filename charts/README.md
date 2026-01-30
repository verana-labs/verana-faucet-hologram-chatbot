# verana-faucet-hologram-chatbot Helm Chart

## Overview

This Helm chart deploys the `verana-faucet-hologram-chatbot` stack and its Kubernetes components:

- `vs-agent` (Helm dependency)
- `chatbot-backend`
- `datastore`
- `faucet-app`
- `postgres`

- The `vs-agent` component is deployed as a Helm dependency (`vs-agent-chart`) and configured entirely via `values.yaml`.

- Ingress definitions are preconfigured and rely on a shared global domain + host setting.

---

## Custom name

Use `nameOverride` to set a fixed name/prefix for all components (avoids collisions across agents or deployments). If you leave it empty, the chart name is used. Example: to keep the prefix `verana-faucet-hologram`, set it in `values.yaml` or via CLI:

```yaml
nameOverride: verana-faucet-hologram
```

or

```bash
helm upgrade --install hologram-welcome ./charts --namespace <ns> --set nameOverride=verana-faucet-hologram
```

With that setting, resources render as:

- `<nameOverride>-vsa` (VS Agent service and StatefulSet)
- `<nameOverride>-chatbot-backend`
- `<nameOverride>-datastore`
- `<nameOverride>-faucet-app`
- `<nameOverride>-postgres`

---

## Installation Guide

### 1. Lint the Chart

Ensure the Helm chart is correctly formatted:

```bash
helm lint ./charts/
```

---

### 2. Render Templates

Preview the generated Kubernetes manifests:

```bash
helm dependency build ./charts/
```

Then:

```bash
helm template <release-name> ./charts/ --namespace <your-namespace>
```

---

### 3. Dry-Run Installation

Simulate the installation without modifying your cluster:

```bash
helm install --dry-run --debug <release-name> ./charts/ --namespace <your-namespace>
```

---

### 4. Install the Chart

Ensure the target namespace already exists:

```bash
helm upgrade --install <release-name> ./charts/ --namespace <your-namespace>
```

> **Note:** `<release-name>` is a Helm release identifier. For example:

```bash
helm upgrade hologram-generic-chart ./charts --namespace <your-namespace-prod>
```

---

### 5. Uninstall the Chart

To uninstall the release:

```bash
helm uninstall hologram-generic-chart --namespace <your-namespace>
```

---

## Environment Variable Management

All environment variables used by each component are defined inside `values.yaml` under their corresponding section.

Plain variables are injected via `env` entries.

> The `vs-agent` subchart defines its own environment variables directly in its own templates.

---

## Ingress with Global Domain

All ingress resources use the shared domain defined in `global.domain` and `global.host`. This allows centralized control of subdomain routing per component. Example:

```yaml
host: {{ .Values.global.host }}.{{ .Values.global.domain }}
tlsSecretName: public.{{ .Values.global.host }}.{{ .Values.global.domain }}-cert
```

This pattern is applied to all ingress-enabled components.

---

## Services and names

The following services are created by this chart (names depend on `nameOverride`):

- `{{ .Values.nameOverride }}-vsa` (vs-agent, via dependency)
- `{{ .Values.nameOverride }}-chatbot-backend`
- `{{ .Values.nameOverride }}-datastore`
- `{{ .Values.nameOverride }}-faucet-app`
- `{{ .Values.nameOverride }}-postgres`

Internal envs are wired to these service names. Example: the backend points to
`{{ .Values.nameOverride }}-vsa`, `{{ .Values.nameOverride }}-datastore`, and `{{ .Values.nameOverride }}-faucet-app`.

---

## q.* Ingress (avatar)

The avatar image used by the VS Agent is served through a dedicated Ingress on
`q.<host>.<domain>` and routes `/avatar.png` to the backend service.

Values:

```yaml
chatbotBackend:
  ingress:
    enabled: true
    className: nginx
    host: q.{{ .Values.global.host }}.{{ .Values.global.domain }}
    tlsSecret: public.{{ .Values.global.host }}.{{ .Values.global.domain }}-cert
    path: /avatar.png
```

---

## Environment Variables by Component

Below is a summary of the environment variables required by each component. All values must be defined under `values.yaml` in their respective section.

### Chatbot Backend

| Source | Key                         | Description                       |
| ------ | ----------------------------| ----------------------------------|
| Env    | QUARKUS_HTTP_PORT           | Backend HTTP port                 |
| Env    | QUARKUS_DATASOURCE_USERNAME | DB user                           |
| Env    | IO_TWENTYSIXTY_*            | DIDComm/Avatar auth settings      |
| Env    | IO_VERANA_*                 | URLs to datastore/faucet services |
| Env    | QUARKUS_ARTEMIS_URL         | Artemis broker URL                |
| Secret | QUARKUS_DATASOURCE_PASSWORD | DB password                       |
| Secret | QUARKUS_ARTEMIS_PASSWORD    | Artemis password                  |

---

### Vs-Agent (via `vs-agent-chart`)

This subchart is fully configured via the `vs-agent-chart` section in `values.yaml`. All `env` variables are defined directly in the template.

| Key                                    | Description               |
| -------------------------------------- | ------------------------- |
| AGENT_ENDPOINT                         | WebSocket endpoint        |
| AGENT_LABEL                            | Agent display label       |
| AGENT_INVITATION_IMAGE_URL             | Image URL for invitations |
| EVENTS_BASE_URL                        | Event receiver base URL   |
| AGENT_PUBLIC_DID                       | Public DID                |
| REDIRECT_DEFAULT_URL_TO_INVITATION_URL | Redirect control          |
| ADMIN_PORT                             | Admin API port            |

---

### Postgres

| Source | Key               | Description              |
| ------ | ----------------- | ------------------------ |
| Env    | POSTGRES_USER     | Postgres DB user         |
| Env    | POSTGRES_DB       | Name of the DB to create |
| Secret | POSTGRES_PASSWORD | Postgres DB password     |

---

### Datastore

| Source | Key                                      | Description                     |
| ------ | ---------------------------------------- | ------------------------------- |
| Env    | QUARKUS_HTTP_PORT                        | Datastore HTTP port             |
| Env    | IO_TWENTYSIXTY_DATASTORE_TMP_DIR         | Temporary storage path          |
| Env    | IO_TWENTYSIXTY_DATASTORE_REPO_FS_DIR     | Repository storage path         |
| Env    | IO_TWENTYSIXTY_DATASTORE_MEDIA_MAXCHUNKS | Max media chunks                |

---

### Faucet App

| Source | Key            | Description             |
| ------ | -------------- | ----------------------- |
| Env    | PORT           | Service port            |
| Env    | RPC_ENDPOINT   | Verana RPC endpoint     |
| Env    | DENOM          | Token denom             |
| Env    | CHAIN_PREFIX   | Chain prefix            |
| Env    | AMOUNT         | Faucet amount           |
| Secret | FAUCET_MNEMONIC| Faucet wallet mnemonic  |
