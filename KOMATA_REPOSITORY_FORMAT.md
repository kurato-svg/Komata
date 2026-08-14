# Komata repository format v1

Milestone 2A reads repository metadata from a JSON URL.

```json
{
  "name": "Komata Extensions",
  "extensions": [
    {
      "id": "toongod",
      "name": "ToonGod",
      "version": "0.1.0",
      "language": "en",
      "downloadUrl": "https://example.com/toongod.kex"
    }
  ]
}
```

M2A displays remote entries as available metadata only. Executing downloaded provider code is deferred to M2B so the loader format can be validated separately.
