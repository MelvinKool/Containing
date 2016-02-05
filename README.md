# Containing
Simulatie haven rotterdam

## Simulatie configuratie
Voor de simulatie is het mogelijk om de simulatiesnelheid en het ip-adres van de server waarme verbonden moet worden in te stellen. Er staat een JSON-bestand in de resources map van de simulator (./Simulator/dist) waar je deze waardes in kan voeren. Het bestand kan er als volgt uitzien:

```json
{
    "simulation_speed": 60,
    "server_ip": "127.0.0.1",
    "server_port": 1337
}
```

Aanbevolen is om een simulatiesnelheid van 60 keer de snelheid van de server in te stellen, dit zorgt voor de beste stabiliteit.

## Uitvoeren van de simulatie en server
Om de simulatie te starten kan eerst de server opgestart worden (./Server/Server of make run). De server zal om een tijd modifier vragen, dit is de snelheid waarmee tijd in de server voorbij zal gaan. Daarna zal er worden gevraagd om welke XML-bestanden er in de database geladen moeten worden. Dit kan lang duren als het bestand groot is, maar hoeft maar 1 keer gedaan te worden omdat de informatie in de database blijft staan. Als er wel een ander XML-bestand moet worden gebruikt moet die opnieuw ingeladen worden. Hierna kan de simulator opgestart worden. Deze staat in ./Simulator/dist als jar-bestand en kan worden uitgevoerd met `java -jar Simulator.jar`. De server begint met de simulatie zodra de simulator verbonden is.
