# Millions - A Stock Trading Game

## Beskrivelse
Denne applikasjonen er utviklet i henhold til en prosjektoppgave gitt i emne Programmering 2 ved
studie Dataingeniør ved NTNU i Trondheim.
Applikasjonen er et aksjespill, som lar deg legge inn en valgfri sum av penger, og kjøpe og selge 
aksjer på utvalgte børser. 



## Hvordan kjøre program

```bash
mvn javafx:run
```

## Hvordan kjøre tester i program

```bash
mvn test
```

## Prosjekt struktur
- model/ - domenelogikk (Player, Exchange, Stock osv.)
- windows/ - JavaFX views og controllers.
- io/ - CSV-lesing/skriving
- utility/ - hjelpeklasser
- app/ - inngangspunkt til applikasjon
- resources/ - csv-filer og css-styling

## Funksjonalitet
- Opprette spiller med startkapital, navn og vanskelighetsgrad
- Kjøpe og selge aksjer på tre forskjellige børser
- Se portefølje med oversikt over aksjer og saldo
- Se transaksjonshistorikk for spiller
- Se kursbevegelser og aksjestatistikk per uke
- Simulere ukentlige prisbevegelser ved å gå fremover i tid
- Se spiller status basert på handelsaktivitet


## Forfattere
Lukas Celius og Noah Magnussen Nalbant
