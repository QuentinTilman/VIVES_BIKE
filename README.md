# VIVES_BIKE
## Beknopte uitleg van de applicatie
Een netbeans Java Project.  

Binnen het context van het verhuren van fietsen wenst VIVES een applicatie zodat administratieve gebruikers gemakkelijk klanten, fietsen en reservaties kunnen beheren.
VIVES wenst een applicatie die een GUI heeft die dan ook bij fouten duidelijk meld aan de gebruiker wat verkeerd ging.
Daarbij moet alle data op een persistente manier bijgehouden worden zodat indien de applicatie gesloten word (op gewenste of ongewenste manier) er geen data verlies is.



## Project Context

Deze opdracht werd gegeven aan 2de jaar Toegepaste Informatica studenten van VIVES Hogeschool in 2018 voor het vak "Programming"
Het doel van het project was om de concepten van de lagenstructuur design onder de hand te krijgen.
Er werd dan ook gevraagd aan de studenten om deze applicatie defensief te programmeren, m.a.w. de Exceptions af te handelen.
Ook op vlak van JUnittesten moest getoont worden dat de studenten alle edge cases konden testen aan de hand van zowel positive- als negative-testing.

Publieke en Interne documentatie werd niet gevraagd voor deze opdracht, maar werden achteraf wel toegevoegd.



## Opgave van het opdracht

Vives wil naar analogie van de blue-bike, ook fietsen verhuren (standplaats station) bij de
verschillende vestigingen van VIVES.

Om een fiets te kunnen huren moet een persoon lid zijn. Hij betaalt hiervoor per jaar 3€ lidgeld en
krijgt hiervoor een VIVESbike-kaart, en kan dan om het even wanneer één fiets huren (één persoon
kan nooit meerdere fietsen tegelijk huren). Een persoon blijft lid totdat hij zich uitschrijft door z’n
VIVESbike-kaart terug in te dienen. We noteren dan z’n uitschrijfdatum, maar verwijderen de
persoon niet.
  
Naast de standaardgegevens, houden we van een lid opmerkingen bij. Hier kan dan
bijvoorbeeld bijgehouden worden of er dikwijls problemen zijn met deze persoon (betalingen,
beschadigingen, diefstal, …).   
  
Aangezien de inschrijfdatum bijgehouden wordt, kan er vanuit het
systeem ieder jaar automatisch een factuur vertrekken om de 3€ lidgeld aan te rekenen. Het
factureren valt buiten de scope van de oefening.   
  
De gegevens van een lid kunnen gewijzigd worden,zolang deze ingeschreven is. 
Het gaat dan om naam, voornaam, geslacht, telefoonnummer, e-mail enopmerkingen. De startdatum kan ook gewijzigd worden, maar moet altijd voor de start van de eersterit blijven, anders zou de persoon een fiets gehuurd hebben zonder lid te zijn. De einddatum wordtingevuld wanneer het lid zich uitschrijft, en kan niet meer gewijzigd worden. Een lid kan zich maaruitschrijven wanneer hij/zij geen fiets aan het huren is.

De fietsen die gehuurd kunnen worden staan steeds aan het station. Wie een fiets afhaalt aan het
station betaalt 1€ en mag die 24 uur gebruiken. Wie de fiets langer wil gebruiken, mag dat, maar die
betaalt voor elke 24 uur opnieuw 1€. Bij het afhalen aan het station kan de huurder met z’n
VIVESbike-kaart de fietssleutel ophalen aan de sleutelautomaat. Op dit moment start zijn huurtijd.
De fiets moet telkens teruggebracht worden naar hetzelfde station. Op het moment dat de huurder
z’n sleutel terug binnenbrengt, eindigt de huurtijd. De prijs wordt automatisch berekend en
bijgehouden. Uiteraard kan een fiets niet aan twee leden tegelijk verhuurd worden.

Een fiets kan nooit van standplaats wijzigen. Er kan ten allen tijde een opmerking toegevoegd
worden aan een fiets. Een fiets kan zich in 3 verschillende toestanden bevinden: actief, in herstel of
uit omloop.
• ACTIEF: Een fiets kan maar gehuurd worden wanneer die zich in de toestand actief bevindt.
Wanneer een fiets wordt toegevoegd is hij per definitie actief.
* IN HERSTEL: Een fiets kan na een rit beschadigd zijn. De huurder kan dit na de rit melden aan
de sleutelautomaat. Wanneer zo’n melding wordt gemaakt, wordt de fiets in toestand
‘herstel’ gebracht. Er kan aanvullend een opmerking toegevoegd worden aan het dossier van
de huurder of aan de gehuurde fiets. Wanneer de fiets hersteld is, wordt die weer actief
gemaakt.
• UIT OMLOOP: Er kan beslist worden dat een fiets niet meer gebruikt zal worden en dus uit
omloop wordt gehaald. Dergelijke fiets wordt nooit meer in omloop gebracht. 

Volgende zaken worden dus bijgehouden in de database:
  
### Lid (per jaar 3€, niets mee doen)
* rijksregisternummer
* naam
* voornaam
* geslacht
* telefoonnummer
* e-mail
* start lidmaatschap
* einde lidmaatschap
* opmerkingen (bv. dikwijls schade aan fiets, diefstal van fiets, verlies van sleutel,
slechte betalen, …)

### Fiets
* registratienummer
* standplaats (station Roeselare, Kortrijk, Torhout, Tielt, Oostende, Brugge)
* status (actief, herstel, uit_omloop)
* opmerkingen (bv. waarom de fiets uit omloop gehaald werd)
### Rit
* id
 *starttijd
*eindtijd
* prijs
* huurder
* gehuurde fiets


## Design
### Applicatie architectuur

Er werd gekozen om een zogenaamd laagmodel, a.k.a. Multitier/Multilayer architecture. Waarbij elke tier zin specifiek gedrag heeft.
De meest gebruikte lagen zijn als volgt; 

* Presentatie laag; GUI
* Applicatie laag; Service laag of Controller laag vanuit de GRASP principes.
* Business laag: Laag waar de business logica terug te vinden is.
* Data Acces layer (DAO): peristentie layer, is de laag waar de database aangeproken wordt.

![Image](Icon-pictures.png "icon")

### Database architectuur
![Image](Icon-pictures.png "icon")

## Opstarten van de Applicatie

Om dit project te runnen moet je eerst een database aanmaken en de DB.properties file aanpassen.
Hier maken we een DB aan op PhpMyAdmin - MySQL door gebruik te maken van USBWserver.
Download dus eerst de laatste versie van USBWserver via volgende link : https://www.usbwebserver.net/webserver/ 
  
Maak daarna een nieuwe database aan en noem die "vivesbike".
Selecteer de reeds aangemaakte DB, ga naar de SQL togglet en voer de SQL code vanuit VivesBikeSQL.ddl file uit.
Dit zou de DB moeten hebben geinstantieerd.
  
Open nu het project via NetBeans 8.2 met JRE 8.1, clean built en run.
Voor verdere uitleg kan je de "handleiding voor vivesbike" raadplegen.



