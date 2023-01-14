# Example drug-application service

## Risks

### OpenFDA SLA, requests quota, paging limits, etc.

After https://open.fda.gov/apis/authentication/:
> With no API key: 240 requests per minute, per IP address. 1,000 requests per day, per IP address.
> 
> With an API key: 240 requests per minute, per key. 120,000 requests per day, per key.

## Questions 

### Search for drug record applications submitted to FDA for approval

* it could be done in a way that we act as a reverse proxy with simplified API (just two filters), but returning the same response as OpenFDA provides
* should we limit further results when using both filters? or search by either of them?

### Alternatives

Download dataset (<8 MB)