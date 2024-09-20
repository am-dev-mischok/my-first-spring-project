# My first Spring Backend (+Thymeleaf Frontend, auch mit JSON als Antwort)

Das hier ist eine Schritt-für-Schritt-Anleitung für die Erstellung eines ersten [Spring](https://spring.io/) Backends.
Entstanden im Rahmen vom Modul "Software-Architektur 1" in der Mischok Academy.


## 0) Einleitung

### 0.a) Für Dozierende und Wiederholungstäter
Beim YouTuber *Amigoscode* gibt es ein ganz nettes, fixes, nur 97-minütiges Video [Spring Boot Tutorial | Full Course [2023] [NEW]](https://www.youtube.com/watch?v=9SGDpanrc8U), in dem er mit nur wenigen Unterschieden zur Vorgehensweise aus der folgenden Anleitung die Basics erklärt.
Wer schon die Grundlagen von Spring mal gesehen hat, ist dort gut aufgehoben.
Im Video werden aber weniger Sachen behandelt, als in dieser Anleitung.
Das hier sind alle Unterschiede, die ich beim ersten Erstellen der Anleitung (Alex, September 2024) beim schnellen Anschauen gesehen habe.
Im Video nicht bearbeitete Konzepte stehen nicht in dieser Liste:
- benutzt nicht ***Lombok***, aber erwähnt es
- benutzt nicht ***Flyway***
- Datenbank mit ***Postgres***, nicht mit ***H2***
  - erstellt in Postgres CLI eine Datenbank, gibt dann dem eigenen User alle Rechte, dessen Authentication er in die `application.properties` legt?? (vor 40:00)
- benutzt **kein Thymeleaf**, als Rückgabe **nur JSON**
  - Controller-Klasse mit `@RestController`, statt `@Controller`, sodass man sich bei JSON-Rückgabe bei den Endpunkten die Annotation `@ResponseBody` sparen kann (wird bei Thymeleaf-Rückgabe evtl stören)
  - am Anfang schreibt er einen GET-Endpunkt mit den nötigen Annotations direkt bei der Klasse mit `main` rein, lagert das aber später in eigene Klasse aus
  - benutzt `@RequestMapping` bei Controllerklasse, um allen den gleichen Pfad zu geben
- **Ordnerstruktur**: für jede Entity eigenen Ordner mit den Klassen, statt einen Ordner mit allen Controllern, einen mit allen Services etc
- erwähnt Annotation `@Transient` bei Entity-Klasse, um das Feld wegzulassen bei der Persistierung durch JPA
- nutzt `@Transactional`, um eine Entity mit `.setName(...)` zu updaten
  - benutzt dabei aber PUT, statt PATCH, obwohl er nur einzelne Sachen mitgibt und aktualisiert, statt der ganzen Entity


### 0.b) Was wir bauen
Eine funktionierende, kleine ***Spring***-Anwendung.
Ausgangspunkt ist ein ausführliches [Spring Tutorial zu "*Serving Web Content with Spring MVC*"](https://spring.io/guides/gs/serving-web-content), falls man es selbst mal durchlesen möchte (lückenhaft und teilweise nicht so gut).
In unserer Anleitung gehen wir darüber hinaus und nutzen u.a. Annotations von **Lombok**, bekommen auch **JSON** als Antwort von unseren Endpunkten, verschicken **POST-Requests** und binden eine **H2-Datenbank** ein.

Folgende Funktionalitäten werden wir haben:
- Dependencies verwalten mit ***Maven*** in einer `pom.xml`
- **GET**- und **POST-Requests** annehmen, Ausblick auch auf **PUT** und **DELETE**
  - Anfrage
    - GET zuerst nur mit der URL-Zeile vom Browser
    - für POST dann mit `curl` aus dem Terminal
    - später alles über klickbare Links und **Formulare** (HTML oder Thymeleaf, `<form>`-Tag)
  - Antwort
    - mit ***Thymeleaf*** direkt eine HTML-Seite als renderbares Frontend
    - oder einfach Daten als **JSON**
      - später auch mit selbst gesetztem *HTTP-Status-Code* und weiteren Informationen im *HTTP-Header* (TODO ALEX)
- mit Code-Generator ***Lombok*** sparen wir uns Boilerplate-Code und lernen **Annotations** kennen
- ganz normale HTML-Seiten anzeigen
- Datenbank zum Speichern von Daten
  - wir nutzen ***H2***, zuerst *In-Memory*, dann auch persistiert lokal als Datei
  - dank Spring ***JPA*** sparen wir uns eigene SQL-Queries
  - *Datenbank-Migrations* mit ***Flyway***, um initiale Queries und andere Datenbankänderungen reproduzierbar und automatisiert durchzuführen
- *Automatisierte Tests* (Coming Soon, im Modul Software-Architektur 2)


### 0.c) Voraussetzungen
- Vorwissen:
  - **Java** Grundlagen
  - **HTML** und **CSS** Grundlagen
  - ***Terminal*** in einem Ordner öffnen und keine Angst davor haben
  - optional: ***git*** installiert und Grundlagen dafür gelernt, um nach jedem Schritt (oder nach wenigen Schritten) einen schönen neuen Commit anzulegen
- Betriebssystem: ***Ubuntu*** wäre gut
- im Terminal installierte/verfügbare Programme:
  - **mvn**
  - **curl**
- ***IntelliJ*** oder anderer Editor / IDE
  - mit Java ***17*** oder höher



## 1) Start und ein erster Endpunkt

### 1.a) Leeres Spring Projekt mit passenden Dependencies erstellen

Wir starten mit der Grundstruktur, die wir aus dem [Spring initializr](https://start.spring.io/) herausbekommen.
![Screenshot vom Initializr mit ausgewählten Sachen](images/initializr.png "Screenshot vom Initializr mit ausgewählten Sachen")
- entweder die Vorauswahl in [diesem pre-initialized project](https://start.spring.io/#!type=maven-project&language=java&packaging=jar&jvmVersion=17&groupId=com.example&artifactId=my_first_spring_project&name=my_first_spring_project&description=Spring-Projekt%20mit%20grundlegenden%20Funktionen&packageName=com.example.my_first_spring_project&dependencies=web,thymeleaf,devtools) nehmen, Textfelder ausfüllen und mit Klick auf den Button "GENERATE" herunterladen
- oder selbst im [Spring initializr](https://start.spring.io/) mit Auswahl:
  - Maven als Buildtool
  - Textfelder passend ausfüllen
  - Dependencies auswählen:
    - Spring Web
    - Thymeleaf
    - Spring Boot DevTools
- diese Dependencies liegen dann direkt in unserer Maven-Verwaltungs-Datei `pom.xml`:
  ```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>

  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
  </dependency>
  ```
- dieses Startprojekt am besten direkt auch mit Hilfe von **git** versionieren, optional auch remote auf GitLab oder GitHub, und im Laufe der Anleitung passende Commits erstellen.
  Mit git kannst du deine Fortschritte sauber und nachhaltig festhalten und mit anderen Teilen.
  Das hat keinen direkten Einfluss auf deinen fertigen Code.
  Wenn du git noch nicht kennst, kannst du also auch ohne git weiter machen.
  Im Anhang, ganz am Ende der Anleitung, findest du einen minimalen Refresher zu git.


### 1.b) in IntelliJ öffnen und einfachen GET-Endpunkt erstellen
- in IntelliJ öffnen, am besten direkt die `pom.xml` auswählen
- erstelle neben der main-Klasse auch eine Klasse `GreetingController.java` als Controller für unsere Endpunkte
	- zuerst komplett ohne RequestParam, nur mit `Model model` für Thymeleaf
    ```java
    package com.example.simple;

    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestMapping;

    @Controller
    public class GreetingController {

        @GetMapping("/greeting")
        public String greeting(Model model) {
            return "greeting";
        }
    }
    ```
- erstelle `greeting.html` in `src/main/resources/templates`, mit folgenden Code (genommen aus der [Spring-Anleitung](https://spring.io/guides/gs/serving-web-content#initial), aber hier noch `${name}` rausgenommen)
  - **ACHTUNG!**: die einzelnen Anführungszeichen um `Hello World!` im folgenden Beispielcode sind wichtig!
    Durch die doppelten Anführungszeichen wird erst der Input für Thymeleaf ermöglicht, aber dann muss man die einzelnen Anführungszeichen noch als String-Delimiter setzen.
    Das nicht zu tun, wäre so falsch, wie in Java `String text = Hello World!;` ohne String-Delimiter zu schreiben.
    ```html
    <!DOCTYPE HTML>
    <html xmlns:th="http://www.thymeleaf.org">
    <head>
        <title>Hallo :-)</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    </head>
    <body>
      <p th:text="'Hello World!'"></p>
    </body>
    </html>
    ```


### 1.c) Backend lokal laufen lassen mit maven
Zum Ausführen des Projekts lernen wir zwei Möglichkeiten:
- in einer IDE: bei IntelliJ auf Play drücken, oder vorher Rechtsklick auf die `pom.xml` und dort "Ausführen" (mit grünem Play-Symbol daneben) oder sowas anklicken, oder auf die Java-Klasse mit der main Rechtsklick und "Ausführen". Nach dem ersten erfolgreichen Run sollte bei IntelliJ oben rechts ein grüner Play-Button verfügbar sein.
- ohne IntelliJ oder sonstige IDE: Terminal öffnen im Projektordner und ausführen:
  ```
  ./mvnw spring-boot:run
  ```

Bis wir die Ausführung im Terminal wieder stoppen, können wir jetzt unseren Endpunkt erreichen, zB in einem Webbrowser wie Firefox.
- aufrufen: http://localhost:8080/greeting
  - `localhost` ist der Name des virtuellen Servers, der auf dem eigenen Computer läuft. Der gleiche Ort ist erreichbar über die IP-Adresse 127.0.0.1 und wird oft verwendet von lokal laufenden Anwendungen, die eine Webseite/-server für die Entwicklung lokal simulieren wollen. Die Zahl dahinter steht für den verwendeten Port.

Jetzt ändern wir den Inhalt im Thymeleaf-Template durch einen Request-Parameter am Ende der URL. Dafür müssen wir unseren Controller anpassen:
- mit `@RequestParam` und Variable eingefügt in `model`, dafür auch das Thymeleaf Template (`greeting.html`) anpassen
  ```java
  @GetMapping("/greeting")
  public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String someName, Model model) {
      model.addAttribute("name", someName);
      return "greeting";
  }
  ```
  ```html
  <p th:text="'Hello ' + ${name} + '!'"></p>
  ```
  ```
  localhost:8080/greeting
  localhost:8080/greeting?name=Alex
  ```
  - die Variable beim `@RequestParam` auch mal "`someName`" oder so nennen, um abzugrenzen vom RequestParam `name="name"` <- letzteren auch mal nur `"n"` nennen oder `"u"`. Alternativer Endpunkt und `<p>`-Tag in Thymeleaf und was man im Browser aufrufen muss:
    ```java
    @GetMapping
    public String greeting(@RequestParam(name = "n", required = false, defaultValue = "World") String someName, Model model) {
        model.addAttribute("inputName", someName);
        return "greeting";
    }
    ```
    ```html
    <p th:text="'Hello ' + ${inputName} + '!'"></p>
    ```
    - im Code-Beispiel von der [Spring-Anleitung](https://spring.io/guides/gs/serving-web-content#initial) steht es ein bisschen anders dran, mit dem gleichen Ergebnis.
      Hier werden Pipe-Zeichen "`|`" verwendet, durch die man in Spring Strings markiert, in denen man Platzhalter ähnlich wie bei Strings in JavaScript oder bei Pythons f-Strings einsetzen kann.
      D.h. wir können den String mit Pipe-Zeichen umschließen, statt mit einfachen Anführungszeichen, und dann einfach im Textfluss Thymeleaf-Variablen einsetzen:
      ```html
      <p th:text="|Hello ${inputName}!|"></p>
      ```
- beachte, dass man die ganzen Bezeichnungen hierüber (`someName`, `n`, `inputName`) auch alle gleich benennen könnte, zB "name".
  Die sind hier nur verschieden, um klar zu machen, welcher Name was tut.



## 2) JSON als Antwort vom Endpunkt
Als nächstes wollen wir vom Endpunkt aus JSON zurückgeben.
Dafür nicht mehr mit Thymeleaf und mit dem model und String-Rückgabe beim Endpunkt.
Sondern wir geben einfach ein POJO (=*Plain Old Java Object*) zurück und Spring-Web konvertiert das mit Jackson für uns direkt zu einem JSON-Objekt.

<!-- - bevor wir mehr Endpunkte aufrufen, verschieben wir das `"/greeting"` von `@GetMapping` (und löschen da die dann leere Klammer) und setzen stattdessen für den ganzen Controller oben direkt unter `@Controller` diese Zeile:`@RequestMapping("/greeting")` -->
- jetzt brauchen wir ein POJO, das wir dann zurückgeben können.
  Für diese Anleitung Arbeiten wir beispielhaft mit einer Klasse `Person`:
  ```java
  package com.example.simple;

  public class Person {
    private int id;
    private String name;

    public int getId() {
        return this.id;
    }
    public String getName() {
        return this.name;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
  }
  ```

- GET Endpunkt mit JSON-Antwort schreiben, hier brauchen wir auch noch die Annotation `@ResponseBody`:
  ```java
  @GetMapping("/greetingJson")
  @ResponseBody
  public Person greetingJson(@RequestParam(name = "name", defaultValue = "Max") String someName) {
      Person person = new Person();
      person.setId(1);
      person.setName(someName);

      return person;
  }
  ```
  - **Achtung**: hier auch `Model model` bei den Eingabeparametern weglassen, das brauchen wir nur für Thymeleaf!



### 3) Lombok Annotations
Wir löschen in unserem POJO die Getter und Setter und nutzen stattdessen die passenden Annotations von Lombok.
Tiefergehende Infos [bei Baeldung](https://www.baeldung.com/intro-to-project-lombok).
- So aktivieren wir Lombok in IntelliJ:
  - Dependency in die `pom.xml` packen.
    Am besten recherchieren wir dafür alle selbst, wie wir die Dependency kriegen.
    Suchmaschine: "maven lombok"
    ```xml
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.34</version>
    </dependency>
    ```
  - jetzt würde das schon compilen, aber damit IntelliJ das für die Unterstützung beim Code Schreiben auch versteht, müssen wir das Plugin Lombok noch installieren.
  Dann eventuell noch bei IntelliJ bei kleinem aufploppendem Dialog auf den Button mit "Enable Lombok Annotation Processing" klicken
- jetzt löschen wir bei unserem POJO die Getter und Setter und setzen stattdessen lombok-Annotations:
  ```java
  package com.example.simple;

  import lombok.Getter;
  import lombok.Setter;

  @Getter
  @Setter
  public class Person {
      private int id;
      private String name;
  }
  ```
- optional: `@Builder`-Annotation einsetzen, um unser POJO im Stile des Builder-Patterns zu erzeugen, statt mit Konstruktoren und hinterher gesetzten Werten zu arbeiten.
  - Vorsicht bei Lombok: Die `@Builder`-Annotation macht den leeren Konstrktur kaputt macht bei der POJO, d.h. wenn man den braucht, braucht man noch die Lombok-Annotation `@NoArgsConstructor`.
    Dann aber geht der Builder wieder kaputt, außer man setzt dann noch die Annotation `@AllArgsConstructor`.
    Da JPA (brauchen wir später) sowieso den leeren (oder den vollen?) Konstruktor braucht, sieht man diese beiden Annotations auch häufig bei Entity-Klassen in Spring-Projekten.
  ```java
  import lombok.Builder;
  import lombok.Getter;
  import lombok.Setter;

  @Getter
  @Setter
  @Builder
  public class Person {
      private Long id;
      private String name;
  }
  ```
  ```java
  @GetMapping("/person")
  @ResponseBody
  public Person personJson() {
      // Objekt erstellen, zB aus Datenbank holen, sonstige Business-Logik
      Person person = Person.builder()
              .id(7L)
              .name("Paul")
              .build();

      return person;
  }
  ```



### 4) Statische Seiten: Welcome Page
Damit wir uns nicht die Pfade merken müssen, basteln wir uns eine statische, stinknormale HTML-Startseite, ganz ohne Thymeleaf.
Spring (bzw. Spring Boot?) erkennt alle in `src/main/resources/static` abgelegten HTML-Dateien und antwortet auf GET-Requests mit einem Pfad, der sich mit dem Pfad von abgelegten Dateien auf `static/` deckt, mit der entsprechenden HTML-Datei.

In folgender Hauptseite (`static/index.html`) zeigen wir unsere bisherigen Links klickbar an.
Dafür legen wir eine Datei `index.html` in den Ordner `src/main/resources/static` und diese können wir dann anzeigen, wenn das Programm läuft und wir `localhost:8080` öffnen:
  ```html
  <!DOCTYPE HTML>
  <html lang="de">
  <head>
    <title>Meine Seite</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  </head>
  <body>
    <p>
      Gerade angekommen? <a href="/greeting">Hier entlang.</a>
    </p>
    <p>
      Bist du Alex? Dann <a href="/greeting?n=Alex">bitte hier</a>.
    </p>
  </body>
  </html>
  ```

Wie wir die Unterseite mit Input öffnen können, ohne ihn selbst in die URL zu schreiben, sehen wir später im Kapitel zu HTML-Forms.
Zunächst reichen uns diese fest gesetzten Links.

Wie bei statischen HTML-Seiten üblich, wird die `index.html` geöffnet, wenn man zu dem enthaltenden Ordner navigiert, aber keine Datei angibt.
Die im folgenden erstellte Datei erreichen wir dann lokal also unter `localhost:8080` und unter `localhost:8080/index.html`.
Wenn wir ein paar mehr Ordner und dann darin eine Datei erstellen, mit Pfad `src/main/resources/static/ordner/nocheiner/hey.html`, dann müssen wir sie so aufrufen:  
`localhost:8080/ordner/nocheiner/hey.html`



## 5) Dateien in Thymeleaf verlinken: Styling mit CSS

### 5.a) Thymeleaf mit Styling

Wie auch in HTML-Dateien, können wir oben im `<head>`-Tag Thymeleaf-Template eine CSS-Datei mithilfe des HTML-Tags `<link>` einbinden.
Damit das Backend, auch wenn es gebaut ist und auf irgendeinem Server läuft, noch den Pfad zur Datei findet, nutzen wir Thymeleafs URL-Syntax.
Dadurch können wir den Pfad ab dem Ordner `src/main/resources/static/` angeben.
Dafür schreiben wir im `<link>`-Tag: `th:href="@{/...}"`.

Mehr zu [Thymeleafs URL-Syntax in der Dokumentation](https://www.thymeleaf.org/doc/articles/standardurlsyntax.html).

Als Beispiel stylen wir unser Template für die Begrüßung.
- erstellen unter `src/main/resources/static/greeting-styles/boring.css` eine CSS-Datei:
  ```css
  p {
    font-family: monospace;
    font-weight: bold;
    font-size: 5rem;
    font-style: italic;
  }
  ```
- in der `greeting.html` fügen innterhalb vom `<head>`-Tag den folgenden Tag hinzu:
  ```html
  <link rel="stylesheet" th:href="@{/greeting-styles/boring.css}">
  ```



### 5.b) CSS je nach RequestParam
Jetzt wollen wir eine weitere CSS-Datei erstellen und diese nutzen, wenn ein passender RequestParam beim GET-Request mitgegeben wurde.
- Endpunkt anpassen:
  ```java
  @GetMapping("/greeting")
  public String greeting(
          @RequestParam(name = "n", required = false, defaultValue = "World") String someName,
          @RequestParam(name = "css", required = false, defaultValue = "") String cssFileName,
          Model model
  ) {
      model.addAttribute("inputName", someName);
      model.addAttribute("cssFileName", cssFileName);
      return "greeting";
  }
  ```
- diese für Thymeleaf bereitgelegte Variable `cssFileName` setzen wir ein im Template.
  Als Default verwenden wir aber weiterhin unsere alte CSS-Datei, falls der RequestParam leer geblieben ist.
  Dafür basteln wir uns den String für den fertigen Pfad zusammen, u.a. mit der aus Java bekannten String-Methode `.isEmpty()`:
  ```html
  <!DOCTYPE HTML>
  <html xmlns:th="http://www.thymeleaf.org">
  <head>
      <title>Hallo :-)</title>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <!--    <link rel="stylesheet" th:href="@{/greeting-styles/boring.css}">-->
      <link
        rel="stylesheet"
        th:href="@{'/greeting-styles/' + ${cssFileName.isEmpty() ? 'boring' : cssFileName} + '.css'}"
      >
  </head>
  <body>
      <p th:text="'Hello ' + ${inputName} + '!'"></p>
  </body>
  </html>
  ```
    - Schöner wäre es, den default-Wert einfach beim Endpunkt direkt zu behandeln, zB in der Annotation `@RequestParam`, direkt beim Input `defaultValue="boring"`.
      Wir wollen aber auch mal etwas in Thymeleaf ausprobieren.
- neue CSS-Datei könnte unter `src/main/resources/static/greeting-styles/style-alex.css` erstellt werden und so aussehen:
  ```css
  p {
    color: darkcyan;
    background-color: white;
    padding: 0.5rem 4rem;

    transition: background-color 0.5s, scale 0.5s, rotate 1s;

    font-weight: bold;
    font-family: Arial;
    font-size: 5rem;
  }
  p:hover {
    background-color: black;
    scale: -1.2 1.2;
    rotate: 4deg;
  }

  body {
    background-color: darkcyan;

    display: flex;
    justify-content: center;
    align-items: center;

    height: 100vh;
    width: 100vw;
  }
  ```
- in unserer Hauptseite unter `src/main/resources/static/index.html` passen wir den zweiten `<p>`-Tag an, sodass er die Begrüßung mit dem richtigen Style aufruft:
  ```html
  <p>
      Bist du Alex? Dann <a href="/greeting?n=Alex&css=style-alex">bitte hier</a>.
  </p>
  ```

Die alte, jetzt auskommentierte Zeile `<link rel="stylesheet" th:href="@{/greeting-styles/boring.css}">` in `greeting.html` könnten wir auch drinnen lassen, damit dieses Styling auch dann greift, wenn der in `cssFileName` mitgegebene Wert nicht zu einer existierenden CSS-Datei passt.
In der ausgewählten CSS-Datei können alle Werte aus `boring.css` überschrieben werden, weil bei CSS neuere (untere) Regeln die alten (oberen) überschreiben.



## 6) Optionaler Abschweifer: Kollaborativ arbeiten mit git (mit einem remote Repository, zB bei Github oder Gitlab)
Gemeinsam erweitern wir unsere Hauptseite um weitere Begrüßungslinks.
Folgende konzeptionellen Schritte müssen wir dafür durchgehen:
- wir ziehen uns mit `git clone` den aktuellen Stand vom Dozenten
- erstellen dann jeweils einen neuen Branch
- implementieren unseren Teil und erstellen einen Commit
- diesen neuen Branch laden wir hoch
- bei Github erstellen wir einen *Pull Request* (bei Gitlab anderer Name: *Merge Request*), sodass der Dozent die Änderungen sehen und automatisiert bei sich einpflegen kann

Dafür werden wir folgende Befehle brauchen:
1. `git clone`
    - `git clone <URL_TO_REPO>` lädt ein Projekt von Github oder Gitlab runter auf unseren Laptop, inklusive aller git-Sachen, die dran hängen
2. `git branch`
    - Branch erstellen: `git branch <BRANCH_NAME>`, dann müssen wir noch wechseln zum gerade erstellten Branch
3. `git checkout`
    - zu einem Branch wechseln: `git checkout <BRANCH_NAME>`
    - einen Branch erstellen und direkt dahin wechseln: `git checkout -b <BRANCH_NAME>`
    - alle lokalen Änderungen einer Datei rückgängig machen, die noch nicht commitet wurden (Vorsicht, die Änderungen sind für immer weg!): `git checkout <FILE_NAME>`
4. `git merge`
    - `git merge <BRANCH_NAME>` versucht, die Änderungen des angegebenen Branches in den rein zu ziehen, in dem wir uns aktuell lokal befinden
    - `git mergetool` zum Nutzen einer optional eingerichteten Software, mit der mögliche Merge Konflikte mit einer GUI behandelt werden können

Nach Ausführen von Befehlen 1 bis 3, können wir lokal das Projekt in unserer IDE öffnen und unsere Begrüßung ergänzen.
- erstelle neue CSS-Datei unter `static/greeting-styles/`
  - möglichst Namenskollisionen mit anderen Leuten im Kurs vermeiden, damit es später leichter ist, die Pull Requests ohne Konflikte zu vereinen
- ergänze Link mit passenden RequestParams für Name und CSS-Datei in der `index.html`
- erstelle Commit, wenn alles funktioniert
- mit `git push` wird der branch mit dem neuen Commit gepusht
- am Beamer geht es weiter mit der UI von Github, um die branches in den main-Branch zu mergen

Befehl 4 führen wir nur aus, um lokal zwei Branches zu mergen.
Wenn es dabei zu Konflikten kommt, kann ein unter `git mergetool` eingerichtetes Programm helfen, übersichtlich Änderungen manuell zu vereinen.

Stattdessen nutzen wir aber die GUI von Github, um einen Pull Request zu erstellen, den der Repository-Besitzer (oder jemand mit passenden Rechten) auch in der GUI von Github annehmen kann.
  - **Achtung**: bei Github muss man erst als "Collaborator" hinzugefügt werden und hat dann auch Rechte, auf main zu pushen, und das kann wohl nicht eingestellt werden.
    Bei Gitlab können "Members" mit verschiedenen Berechtigungen hinzugefügt werden, die dann zB nur auf Branches pushen können, die nicht speziell geschützt sind (wie zB der main-Branch).



## 7) `curl` und verschiedene Rückgabe dank Request-Headers

### 7.a) Im Webbrowser (zB Firefox)

Die Headers eines HTTP-Requests können dem empfangenden Backend weitere Informationen mitgeben, wie gewünschtes Dateiformat oder Sprache.
Beispielhaft wollen wir unsere bisherigen zwei Endpunkte mit jeweils HTML- und JSON-Rückgabe abändern, sodass sie unter dem gleichen Pfad erreichbar sind.

<b style="color: pink; background-color: black; padding: 0.1rem;">
**Achtung**:
In unserem Beispiel machen diese zwei Endpunkte ganz verschiedene Dinge. Einer gibt eine Begrüßung zurück, der andere gibt ein Dummy POJO zurück.
Zwei Endpunkte sollten nur unter dem gleichen Pfad erreichbar sein, wenn sie konzeptionell die gleiche Sache machen und zurückgeben.
Damit unser Beispielcode schlank und einfach bleibt, ändern wir die JSON-Rückgabe aber nicht ab.
</b>

<br>

Wir öffnen unsere Hauptseite im Firefox Web Browser, öffnen die Dev-Tools (zB mit `F12`), klicken dort auf den **Network**-Tab und klicken dann auf unserer Seite auf den Link zur Begrüßung.
Im Network-Tab sehen wir, wenn wir den entsprechenden GET-Request anklicken, dass in den Headers `Accept: text/html,[...]` steht.

![1. Screenshot der Dev-Tools mit markierten Stellen, die man anklicken sollte.](images/ff-dev-accept.png "1. Screenshot Klickpfad Dev-Tools")

Mit einem Rechtsklick auf den Request oben und dann Klick auf `Edit and Resend`, können wir die Headers ändern und schreiben bei Accept rein: `application/json` und schicken den Request ab.

![2. Screenshot der Dev-Tools mit markierten Stellen, die man anklicken sollte.](images/ff-dev-new-accept.png "2. Screenshot Klickpfad Dev-Tools")

Als Antwort bekommen wir jetzt den Status-Code `406 Not Acceptable`, denn unser Backend liefert hinter diesem Pfad noch keine JSON.
Das ändern wir jetzt und probieren das gleiche nochmal.


### 7.b) Endpunkte mit gleichem Pfad, aber verschiedenen Rückgabetypen

Wenn wir bei unseren beiden bisherigen Endpunkten in der Annotation `@GetMapping("...")` den gleichen String als Pfad übergeben und dann unser Programm starten, stürzt es direkt ab, u.a. mit Fehler
`java.lang.IllegalStateException: Ambiguous mapping. Cannot map 'greetingController' method`

Wir müssen die Endpunkte noch nach Rückgabetyp unterscheiden.
Bei Spring reicht es, in der HTTP-Verb-Annotation die Variable `produces` zu setzen mit dem korrekten Wert für die entsprechenden Rückgabetypen.
Anstatt standardisierte Strings wie `"text/html"` oder `"application/json"` auswendig zu kennen, können wir hier den Enum `MediaType` verwenden.
Wir tauschen also `@GetMapping("/greeting")` beim ersten Endpunkt aus durch:
  ```java
  @GetMapping(value = "/greeting", produces = MediaType.TEXT_HTML_VALUE)
  ```
und beim JSON-Endpunkt schreiben wir:
  ```java
  @GetMapping(value = "/greeting", produces = MediaType.APPLICATION_JSON_VALUE)
  ```
Ansonsten ändern wir nichts an den Endpunkten.

Jetzt können wir unseren Request mit angepassten Headern erneut in den Dev-Tools von Firefox abschicken und die JSON-Antwort sehen.


### 7.c) Requests abschicken mit `curl`

Die beiden GET-Requests können wir folgendermaßen im Terminal mit `curl` ausführen, um uns für spätere POST-Requests aufzuwärmen:

```
curl localhost:8080

curl localhost:8080/greeting -H "accept: text/html"
curl localhost:8080/greeting -H "accept: application/json"

curl -H "accept: text/html" localhost:8080/greeting
curl -H "accept: application/json" localhost:8080/greeting

curl localhost:8080/greeting -H "accept: text/html"
curl localhost:8080/greeting -H "accept: application/json"

curl 'localhost:8080/greeting?name=Alex&css=style-alex' -H "accept: text/html"
curl localhost:8080/greeting?name=Paul -H "accept: application/json"
```

Beim vorletzten `curl`-Befehl sind die String-Delimiter beim Pfad wichtig, da sonst das `&`-Zeichen im Pfad zu Problemen führt.

**Tipp**: wenn du einen langen Befehl im Terminal übersichtlicher schreiben willst, kannst du auch im Terminal mehrere Zeilen nutzen.
Wenn du `\` eingibst und dann `Enter` (Taste für neue Zeile) drückst, dann wird der Befehl nicht ausgeführt, sondern eine neue Zeile gestartet.
Wenn du dann irgendwann `Enter` drückst, ohne direkt davor `\` einzugeben, wird der Befehl ausgeführt.
So kann man auch mehrzeilige Befehle zum Kopieren bereitstellen, die man sich dann auch mehrzeilig ins Terminal einfügen kann.
Hier ist der letzte `curl`-Befehl nochmal, in mehreren Zeilen:
```
curl localhost:8080/greeting?name=Paul \
  -H "accept: application/json"
```



## 8) POST-Requests

Laut HTTP ist das POST-Verb dafür gedacht, Datensätze anzulegen.
Praktisch wird es häufig einfach dafür verwendet, bei einem HTTP-Request einen Request Body mitschicken zu können -- normalerweise mit einem JSON-Objekt als Datenobjekt.

Wir basteln zuerst nur einen POST-Request, der keine Daten anlegt.
Unser erstes Ziel ist es, die Daten im Body richtig zu empfangen.
Um zu sehen, ob wir sie passend empfangen, können wir sie zB über `System.out.println(...)` im Terminal anzeigen, oder sie in einer Antwort mit JSON oder Thymeleaf zurückgeben.

Dafür bereiten wir einen POST-Endpunkt vor, der im Body die Daten enthält, mit denen wir unser vorher verwendetes POJO basteln können.
Später werden wir dann dieses Objekt in einer Datenbank persistieren wollen.

Dafür brauchen wir nur statt `@GetMapping` ein `@PostMapping` und außerdem noch bei den Eingabeparametern die Annotation `@RequestBody`, die uns automatisch den JSON-Body des Requests zu unserem Java Objekt umwandelt.

- wir erweitern zunächst unser POJO um ein paar weitere Felder
  ```java
  import lombok.*;

  @Getter
  @Setter
  @Builder
  public class Person {
      private Long id;
      private String name;
      private String email;
      private Integer age;
      private Boolean married;
  }
  ```
- POST-Endpunkt basteln
  ```java
  @PostMapping(value = "/person")
  @ResponseBody
  public Person createPersonFromJson(@RequestBody Person person) {
      // hier sollten wir die Person speichern, aber wir haben noch keine Datenbank
      System.out.println(person.getId());
      System.out.println(person.getName());
      System.out.println(person.getEmail());
      System.out.println(person.getAge());
      System.out.println(person.getMarried());

      // wir geben die Person als JSON zurück
      return person;
  }
  ```
- mit curl Testen, Infos im JSON-Body mitschicken
  ```
  curl -H 'Content-Type: application/json' \
    -d '{ "name":"Paul", "email":"paul@example.com", "age":"25", "married": true}' \
    -X POST \
    localhost:8080/person
  ```



## 9) HTML-Form für GET- und POST-Requests
Die Daten, die wir mit einem Request mitschicken, wollen wir aus einer Eingabe vom User nehmen.

### 9.a) Ohne Thymeleaf, nur HTML
Da es bei einem GET-Request keinen Body gibt, werden die Eingaben als Key-Value-Pairs (oder hier treffender: Name-Value-Pairs) in die Request-Parameter hinter den Pfad gehängt.

Fügen wir die folgende HTML-Form in unsere `index.html` ein, dann kann der User den anzuzeigenden Namen eintippen und die anzuzeigende CSS auswählen.
Beim `<form>`-Tag sind die Attribute `action` für den Pfad und `method` für das HTTP-Verb (bzw die HTTP-Methode) wichtig.
Probiere es aus und schaue nach, wie nach Abschicken des Formulars der Request in der URL-Leiste deines Browsers (oder in den Dev-Tools) aussieht.

**Achtung**: im folgenden Code sind CSS-Dateinamen enthalten, die bei Anfertigung der Anleitung von Umschülern erstellt wurden, nicht hier im Beispiel.
```html
<h2>Begrüßungs-Generator</h2>
<form action="/greeting" method="GET">
  <div>
    <label for="userInput">Dein Name?</label>
    <input name="n" id="userInput" placeholder="Gib deinen Namen ein" />
  </div>
  <div>
    <label for="styling">Styling?</label>
    <select name="css" id="styling">
      <option value="" selected>Standard</option>
      <option value="style-mathias">Mathias</option>
      <option value="style-stefan">Stefan</option>
      <option value="style-max">Max</option>
      <option value="style-jasmin">Jasmin</option>
      <option value="style-alex">Alex</option>
      <option value="a">Leer</option>
    </select>
  </div>
  <div>
    <button>Hallo?</button>
  </div>
</form>
```

Für einen POST-Request müssen wir das Attribut `method` anpassen.
Und in unserem Fall auch den Pfad, da wir unter `/greeting` keinen POST-Request haben.

Beim POST-Request werden die Inputs leider nicht direkt als JSON-Objekt im Body mitgesendet, sondern wie beim GET-Request eigentlich in der URL.
Die Annotation `@RequestBody` brauchen wir deswegen nicht mehr.
Spring wandelt die Daten in der URL dann ohne Annotation zu unserem POJO um.

Ähnlich wie bei den beiden GET-Requests mit dem gleichen Pfad, können wir auch hier wieder den gleichen Pfad nutzen.
Dann müssen wir aber in der Annotation `@PostMapping` den Wert `consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE` setzen.

```java
@PostMapping(value = "/person", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
public String createPersonFromForm(Person person) {
    // hier sollten wir die Person speichern, aber wir haben noch keine Datenbank
    System.out.println(person.getId());
    System.out.println(person.getName());
    System.out.println(person.getEmail());
    System.out.println(person.getAge());
    System.out.println(person.getMarried());

    // wir leiten den User als Antwort einfach auf die Hauptseite zurück
    return "redirect:/";

    // stattdessen könnten wir den Namen der Person auch mit model.addAttribute("inputName", person.name()); für Thymeleaf bereitstellen und dann unser greeting.html Template zurückgeben
}
```
```html
<h2>Person speichern</h2>
<p>
  Neuerdings kann man auf dieser Webseite auch Personen speichern. Wow! Fülle dafür das folgende Formular aus und schicke es mit dem Absenden-Button ab.
</p>
<form action="/person" method="POST">
  <div>
    <label for="name">Name</label>
    <input name="name" id="name" type="text" placeholder="Gib deinen Namen ein" />
  </div>
  <div>
    <label for="email">E-Mail-Adresse</label>
    <input name="email" id="email" type="email" placeholder="und deine E-Mail" />
  </div>
  <div>
    <label for="age">Alter</label>
    <input name="age" id="age" type="number" value="25" />
  </div>
  <div>
    <input name="married" id="married" type="checkbox" />
    <label for="married">Verheiratet?</label>
  </div>
  <div>
    <button>Absenden</button>
  </div>
</form>
```

Den POST-Request, der von der Form abgeschickt wird, können wir folgendermaßen mit `curl` abschicken:
```
curl -X POST \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'name=Paul&email=paul%40example.com&age=25&married=on' \
  localhost:8080/person
```


### 9.b) Mit (möglichst wenig) Thymeleaf

Hier ist der Link zur Dokumentation von Thymeleaf bezüglich Forms: https://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html#creating-a-form

In einem späteren Kapitel basteln wir ein Thymeleaf-Template, in dem die Eingabefelder durch Daten aus einer bestehenden Entity (teilweise) vorausgefüllt sind.
Dieses eine Template können wir dann verwenden, um neue Entities zu erstellen, existierende zu bearbeiten, und existierende im Detail anzuzeigen.
Also drei Funktionen in einem Template!




## 10) JPA und Datenbank

Unsere Daten wollen wir jetzt auch mal abspeichern, sodass sie nach einem Request noch verfügbar sind.


### 10.a) JPA und Datenbank anbinden

Es gibt viele Lösungen für Datenbanken, die man anbinden kann.
Manche laufen als "In-Memory"-Datenbank und werden, wie der Arbeitsspeicher bei einem Laptop, komplett gelöscht beim Ausschalten des Programms.
In solchen Datenbanken werden vor allem Dateien gecached, d.h. nicht auf Dauer zur Nutzung über mehrere Sitzungen persistiert.

Wir verwenden hier H2 ("Hypersonic 2") als Datenbank.
H2 ist in Java geschrieben und bietet bei Spring eine einfache Einbindung mit tollen Features zum Herumprobieren.

Mehr Infos zu Spring mit H2: https://www.baeldung.com/spring-boot-h2-database


#### 10.a.I) JPA Dependency im Projekt einbinden

Mit JPA (und für Feinschmecker dabei: [JPQL](https://www.baeldung.com/spring-data-jpa-query)) sparen wir uns das Schreiben der SQL-Queries.
Damit JPA irgendwelche Daten ansprechen kann, brauchen wir dann auch eine Datenbank, mit der sich unser Programm verbinden kann.

Wir fügen in der `pom.xml` folgendes in den `<dependencies>`-Tag ein:
  ```xml
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
  </dependency>
  ```
  - **Achtung**: Die von uns eingebundene Dependency [Spring Boot Starter Data JPA](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-jpa) ist nicht das gleiche, wie [Spring Data JPA](https://mvnrepository.com/artifact/org.springframework.data/spring-data-jpa), stelle unbedingt sicher, dass du die richtige Dependency hast!

Wenn wir jetzt unser Programm starten (wie immer im Terminal mit `./mvnw spring-boot:run` oder über den Play-Button in IntelliJ), gibt es einen Fehler:
  ```
  Error creating bean with name 'entityManagerFactory' [...]
  ```
  - kurz gesagt: Spring findet keine Datenbank, also erstellen wir eine

#### 10.b.II) H2-Datenbank einbinden

Mit Maven binden wir ganz einfach H2 ein.
Sieht dann zum Beispiel so aus als neue Dependency in unserer `pom.xml`:
```xml
<dependency>
  <groupId>com.h2database</groupId>
  <artifactId>h2</artifactId>
</dependency>
```
- falls bisher noch nicht erwähnt: wenn wir uns die Dependency online holen, wird direkt unter dem `<version>`-Tag wahrscheinlich noch ein `<scope>test</scope>` stehen.
  Hier wird festgelegt, in welchem Scope diese Dependency zur Verfügung stehen soll.
  Damit wollen wir uns aktuell nicht auseinander setzen, also können wir die Zeile auch weglassen.
  Mehr Infos: [Baeldung -- Maven Dependency Scopes](https://www.baeldung.com/maven-dependency-scopes)

Spring setzt bei H2 automatisch Default-Werte.
Diese Setzen wir aber trotzdem noch manuell, weil wir sie gleich direkt noch anpassen möchten.
Und auch damit wir ein Gefühl für solche Konfigurationen bekommen.
Dafür kopieren wir folgende Zeilen in die Datei `application.properties`:
  ```
  spring.datasource.url=jdbc:h2:mem:testdb
  spring.datasource.driverClassName=org.h2.Driver
  spring.datasource.username=sa
  spring.datasource.password=
  ```
  - die letzte Zeile hat einen leeren Wert, damit das Passwort leer bleibt und wir kein Passwort eingeben müssen.
    Für die lokale Entwicklung ist das praktisch.

#### 10.b.III) H2: Modus wechseln von In-Memory zu File
Mit diesen Default-Werten läuft die H2-Datenbank im **In-Memory**-Mode.
Das heißt, dass die Datenbank bei jedem Programmstart neu erzeugt wird und beim Herunterfahren des Programms auch wieder gelöscht wird.
Stattdessen können wir die Datei aber auch als Datei erstellt bekommen, im **File**-Mode.
Dafür müssen wir nur die erste der oberen Konfigurationszeilen ändern.
Der Teil `mem` muss zu `file` geändert werden und nach dem folgenden Doppelpunkt schreiben wir den Pfad hin, unter dem die Datenbank-Datei abgelegt werden soll.
Für diese Anleitung stellen wir es so ein:
  ```
  spring.datasource.url=jdbc:h2:file:./data/mynewdb
  ```
  - die Datei liegt dann in einem Ordner `data/`, der durch das `./` (direkt davor) im Projektordner erstellt wird.

**Tipp**: in die `.gitignore` die Zeile `data/` einfügen, damit die DB-Datei nicht commited wird und die lokalen Testdaten damit auch lokal bleiben, statt damit den Speicherplatz auf Github (oder Gitlab) zu vermüllen.


#### 10.b.IV) H2-Console

Mit dem Modus umgestellt zu File, können wir die H2-Console nutzen, um unsere Datenbank zu sehen und Queries auszuführen.
Die H2-Console ist (wenn man die Default-Werte nicht extra geändert hat) verfügbar unter `localhost:8080/h2-console`, während unser Programm läuft.
Dort muss man sich dann einloggen und auch den Dateipfad angeben, mit den Informationen aus unseren `application.properties`:

![Screenshot vom Login bei der H2-Console](images/h2-console-login.png "Screenshot vom Login bei der H2-Console")

Und hier sieht man, wie es nach dem Login aussieht, wenn man links bei "PERSON" auf den "+"-Knopf gedrückt hat, um die Spaltennamen auszuklappen, und dann rechts eine einfache Query geschrieben hat, mit der man alle vorhandenen Einträge angezeigt bekommt.
Zu Demonstrationszwecken wurde hier vorab ein Eintrag erstellt.
Dieser sollte jetzt noch nicht existieren, wenn man nur diese Anleitung bis hierher befolgt hat.

![Screenshot von der H2-Console](images/h2-console-login.png "Screenshot von der H2-Console")


##### Ausblick:  
Bei Bedarf, oder bei größeren Projekten, würden wir eine andere Datenbank benutzen, statt H2.
Zum Beispiel eine ***Postgres***-Datenbank, die neben höherer Performance bei vielen Usern auch noch den SQL-Dialekt ***PostgresQL*** hat, welcher viele Features bietet, den normales SQL nicht hat.
Bei H2 gibt es sogar die Möglichkeit, PostgresQL und andere SQL-Dialekte einzustellen, die dann von H2 imitiert werden.


### 10.b) Optionaler Abschweifer: Initiale Tabellen und Datensätze anlegen (auf veraltete und unschöne Art)

Dieses Unterkapitel kann zum Verständnis und für die Arbeit mit verranztem Legacy-Code ganz nützlich sein.
Es kann aber auch komplett übersprungen werden, wenn man weniger interessiert ist an nicht so gut funktionierenden, umständlicheren Lösungen.
Im nächsten Unterkapitel werden wir stattdessen ordentlich mit ***Flyway*** initiale Tabellen und Daten anlegen und auch lernen, wie man spätere Anpassungen mit Flyway umsetzt.


#### 10.b.I) Optionaler Abschweifer: Vorüberlegungen für initiale Daten (auf veraltete und unschöne Art)

Wir könnten mit POST-Requests (zB mit `curl` oder mit dafür gebasteltem Frontend) selbst Daten einpflegen, aber wir haben noch keine Speicherung der Daten über Endpunkte implementiert in dieser Anleitung.
Außerdem haben wir auch noch kein Repository, das uns die Queries ausführt (OR-Mapper, machen wir aber weiter unten).

Aber selbst wenn wir bereits passende POST-Endpunkte hätten und ein für JPA eingerichtetes Repository, bräuchten wir passende Tabellen in der Datenbank.
Diese können (oder "sollten") wir nicht durch Endpunkte anlegen lassen, da sonst unsere ganze Spring [MVC](https://de.wikipedia.org/wiki/Model_View_Controller)-Struktur verkompliziert oder unmöglich gemacht wird.

Was wir also brauchen, 

Auch nichts, was ich empfehle, aber anscheinend kann man bei Spring beim Ausführen des Programmes Code ausführen mit der `CommandLineRunner` Bean, und so direkt beim Start ein paar Datensätze mit JPA (oder auch klassisch mit JDBC und selbstgeschriebenen Queries) anlegen. Mehr Infos hier: https://spring.io/guides/gs/accessing-data-jpa#_create_an_application_class

TODO ALEX finish

#### 10.c) Flyway zum Anlegen initialer Tabellen und Datensätze, sowie zum späteren Ändern des DB-Schemas

Kommen wir zu einer schöneren Methode, die auch besonders empfehlenswert ist.
Und zwar die Nutzung eines DB-Migration Tools wie ***Flyway*** oder ***Liquibase***.

DB-Migrations sind im Grunde einfach versionierte Datenbankqueries, die für uns ausgeführt werden, wenn sie bei einer Datenbank noch nicht ausgeführt wurden.
So können wir sicherstellen, dass Queries nicht mehrfach ausgeführt werden, auch wenn unser Programm zB täglich neu gestartet wird.
Flyway übernimmt für uns dabei den Aufwand, das zu kontrollieren.
Bei Flyway selbst schreiben wir aber wieder normale SQL-Queries (oder den verfügbaren Dialekt, zB PostgresQL bei einer Postgres-Datenbank).

Wir werden Flyway verwenden.

Hier ist ein Tutorial dazu bei Baeldung, aber nicht speziell für Spring und deswegen für uns nur teilweise brauchbar.
Vieles davon können wir uns sparen: https://www.baeldung.com/database-migrations-with-flyway


#### 10.c.I) Flyway einbinden (in einem Spring Projekt)

Das müssen wir tun:
- Dependency in unsere `pom.xml`:
  ```xml
  <dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
  </dependency>
  ```

... und das war es eigentlich schon, weil Spring alles andere für uns übernimmt.


#### 10.c.II) Flyway nutzen

Jetzt können wir immer, wenn wir eine einmalige Änderung an der Datenbank vornehmen wollen, eine SQL-Query (oder den von unserer Datenbank unterstützen SQL-Dialekt) schreiben und dank Flyway wird diese Query bei jeder Datenbank, die mit unserem Programm im Einsatz ist (zB lokal auf Geräten der Entwickler, beim online Testing-System, beim vom Kunden genutzten online Produktiv-System, ...) nur genau einmal ausgeführt.
Damit die Flyway-Skripte kompatibel sind mit verschiedenen SQL-Dialekten, ist es eine gute Angewohnheit, möglichst wenige Features aus Dialekten zu nutzen und stattdessen bei normalem SQL zu bleiben, wenn möglich.

Wenn wir neue Queries haben, die durch Flyway 1x ausgeführt werden sollen, müssen wir nur eine Datei erstellen im vom Spring dafür standardmäßig vorgesehenen Ordner: `src/main/resources/db/migration/`
Diese Datei sollte, nach Name alphabetisch sortiert, nach allen anderen bereits vorhandenen Dateien kommen, damit FLyway die Dateien in der richtigen Reihenfolge ausführt.
Standardmäßig werden Migrations-Dateien bei Flyway nach diesem Schema benannt (und ich habe beim Schreiben dieser Anleitung nicht ausprobiert, ob das nur eine optionale oder eine verpflichtende Konvention ist. Bonusaufgabe für den interessierten Leser, der sich nicht an Konventionen halten will und sich damit unbeliebt macht bei anderen Entwicklern):
```
<Prefix><Version>__<Description>.sql
```

So könnten zum Beispiel mehrere Dateinamen in einem fortgeschrittenen Projekt aussehen:
```
V1.000__CREATE_TABLE_PERSON.sql
...
V1.005__DROP_UNIQUE_NAME_FROM_USER.sql
...
V1.017__ALTER_TABLE_TEACHER.sql
...
V2.013__CREATE_TABLE_TASK_AND_INSERT_STARTING_ENTRIES.sql
...
```

**Achtung!**: eine erstellte Migration-Datei, die auf irgendeiner Datenbank bereits ausgeführt wurde, darf nicht mehr verändert werden.
Selbst SQL-Kommentare oder Dateinamenänderungen werden sonst zu einem Fehler führen, da Flyway (oder auch Liquibase) mithilfe von Hashes sicherstellt, dass die Migrations konsistent bleiben.
Kurz: wenn das Programm mal irgendwo lief, dann sollte die Migration-Datei nicht mehr angefasst werden.
Wenn man eine fehlerhafte Migation-Datei lokal ausgeführt hat, sollte man hoffen, ein Backup der lokalen Daten zu haben.

In einer einzigen Migration-Datei können beliebig viele SQL-Queries ausgeführt werden.
Meistens werden Tabellen erstellt oder bearbeitet (zB Spalten oder Constraints ändern, hinzufügen, löschen etc), oder benötigte (Beispiel)Datensätze angelegt. Oder es werden automatisiert Einträge geändert, wenn sich die Business-Logik zum Beispiel entscheidet, dass ein Wert nicht mehr optional ist und dann alle NULL-Werte auf einen Default-Wert geändert werden.

Jede Migration-Datei sollte eine abgeschlossene Aufgabe beinhalten, und entsprechend aus den dafür benötigten Queries bestehen, anstatt jede einzelne Query in eine eigene Datei zu stecken.

Für diese Anleitung erstellen wir eine Migration-Datei, die uns die Datenbanktabelle für unsere Entity `Person` anlegt.
Sie heißt `V1.000__CREATE_TABLE_PERSON.sql`, liegt in `src/main/resources/db/migration/` und sieht so aus:
```sql
CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    age INT,
    married BOOLEAN
);
```

Für die Spalte `id` verwenden wir hier eigentlich den PostgresQL-Dialekt, von dem wir zwei tolle Features nutzen.
Und da H2 mit dem PostgresQL-Dialekt klar kommt, können wir die Features auch gut nutzen:
- `PRIMARY KEY` können wir direkt bei der Spaltenbezeichnung dazuschreiben, statt wie in normalem SQL am Ende einen `CONSTRAINT` extra zu deklarieren, bei dem wir den Spaltennamen angeben müssen
- `BIGSERIAL` ist eigentlich der Datentyp `BIGINT` (Integer mit mehr Speicherplatz; ähnlich wie in Java ein `Long` das gleiche ist wie ein `Integer` mit mehr Speicherplatz), kombiniert mit der Funktionalität, dass Ids automatisch aufeinanderfolgend vergeben werden können

In nativem H2-Dialekt sähe eine ähnliche Query so aus:
```sql
CREATE TABLE person (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255),
    email VARCHAR(255),
    age INT,
    married BOOLEAN,
    CONSTRAINT pk_person PRIMARY KEY (id)
);
```

Und bei gewöhnlichem SQL müsste man komplett von Hand die Sequenz erstellen.

Jetzt haben wir mit Flyway eine Tabelle für die Entity `Person` erstellt und können diese befüllen.
Für unsere Anleitung legen wir nicht mit Flyway weitere Personen an, sondern basteln uns gleich Endpunkte, die mit JPA Personen speichern und zurückgeben können.









  curl localhost:8080/person \
      -X POST \
      -H 'Content-Type: application/json' \
      -d '{ "name":"Paul", "email":"paul@example.com", "married": true}'



### TODO
- JPA Query Methods Link: https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html


## 11) GET-Requests mit `@Pathvariable`

TODO Wiederholung: @RequestBody, @RequestParam, @ResponseBody

TODO neu: @PathVariable, für GET einzelne Entity

```java
@GetMapping(value = "/person")
@ResponseBody
public List<Person> getPersons() {
    return personService.getAll();
}

@GetMapping(value = "/person/{personId}")
@ResponseBody
public Person getPersonById(
  @PathVariable(name="personId") Long personId
) {
    return personService.getById(personId);
}
```



## 12) Entity mit allen CRUD-Operationen

***TODO ALEX*** eine Beispielentity: Klasse, Repository, Service, Controller. Mit implementierten CRUD-Operationen mit JPA und den dazugehörigen Standardchecks, zunächst nur als JSON-Endpunkte, evtl auch mit Erwähnung von der Annotation `@RestController`



## 13) Dateien und Methoden sauber und strukturiert ablegen

***TODO ALEX*** Trennung Controller <-> Service <-> Repository <-> Entity und sonstige von Spring benötigte Klassen, damit nicht alles in einem Ordner liegt.



## 14) Entities anzeigen, anlegen oder bearbeiten mit nur einem Thymeleaf-Template

***TODO ALEX***



## 15) User-Authentication und Rollen / Berechtigungen (ohne Datenbankeinträge)

Wir stellen ein, welche Endpunkte unseres Backends nur noch nach erfolgreichem Login erreichbar sind.
Dafür erstellen wir auch hardgecoded (also fest im Programmcode enthalten, nicht zB aus einer Datenbank geladen) testweise User mit verschiedenen Rollen.
Mit diesen können wir uns dann auch einloggen, um die Funktionalität zu testen.

Beachte, dass nach Durchführung dieses Kapitels deine Endpunkte eventuell nur noch nach erfolgreichem Login erreichbar sind und ein einfacher `curl`-Befehl dann nicht mehr funktionieren wird!


## 15.a) Spring Default Login-Seite, Absicherung von Pfaden, hardcoded Users

Vieles aus diesem Unterkapitel findet sich auch bei Baeldung gut aufgeschrieben: [Spring Security Form Login](https://www.baeldung.com/spring-security-login)

In unserer `pom.xml` fügen fügen wir folgendes ein, um ***Spring Boot Starter Security*** nutzen zu können:
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

Jetzt erstellen wir eine neue Datei mit folgendem Inhalt.
Was die einzelnen Zeilen machen und welche anderen zeilen wir nutzen könnten, steht in Kommentaren im Code.
```java
package com.academy.my_first_spring_project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // die Reihenfolge der folgenden Zeilen innerhalb von authorizeHttpRequests ist sehr wichtig. Was zuerst kommt, wird nicht mehr überschrieben. D.h. wenn man in der ersten Zeile den Zugang zu bestimmten Seiten ermöglicht oder verbietet, dann wird das keine der kommenden Zeilen mehr ändern können.
    http.authorizeHttpRequests((requests) -> requests
          // .requestMatchers("/welcome").permitAll() // der Pfad "/welcome" ist für alle erlaubt, auch für nicht-eingeloggte User
          // .requestMatchers("/welcome/**").permitAll() // alle Pfade, die mit "/welcome" anfangen, sind für alle erlaubt, auch für nicht-eingeloggte User
          // .requestMatchers("/manage/**").hasRole("ADMIN") // alle Pfade, die mit "/manage" anfangen, sind nur für User mit Rolle "ADMIN" erreichbar
          // .requestMatchers("/manage/**").hasAnyRole("ADMIN", "TEACHER") // alle Pfade, die mit "/manage" anfangen, sind nur für User mit Rolle "ADMIN" oder "TEACHER" erreichbar
          .anyRequest().authenticated() // alle anderen Pfade sind nur mit erfolgreichem Login erreichbar
    );

    // erstellt eine Standard-Login-Seite (im Bootstrap-Design) unter dem Pfad "/login" und leitet nicht-eingeloggte User, die sich einloggen müssen, automatisch dorthin. Außerdem hat diese Seite eine rote Meldung bei fehlgeschlagenen Logins. Zusätzlich gibt es durch diese Zeile unter dem Pfad "/logout" eine Seite mit einem Knopf zum ausloggen. Vorsicht!: dieser Logout-Knopf leitet zwar wieder zu "/login", aber der User ist noch nicht wirklich ausgeloggt. Für diese Funktionalität braucht man noch die Zeile http.logout(configurer -> configurer.invalidateHttpSession(true));
    http.formLogin(Customizer.withDefaults());

    // // verwende stattdessen diese Zeile, zu der der User redirected wird, um eine eigene Login-Seite anzuzeigen, mit weiter einstellbarem Verhalten (zwei Vorschläge exemplarisch eingefügt). Die Seite sollte über den hier gewählten Pfad erreichbar sein. Dort sollte es eine Möglichkeit für den User geben, per POST "/login" den Usernamen und das Passwort abzuschicken, wie zB in der Standard-Login-Seite. Im RequestBody sollte dann sein: { username: "...", password: "..." }
    // http.formLogin((form) -> form
    //     .loginPage("/mein/login/pfad").permitAll()
    //     .defaultSuccessUrl("/pfad/nach/erfolgreichem/login") // hier wird der User nach erfolgreichem Login hingeschickt, wenn er nicht bereits versuchte, eine gesicherte Seite aufzurufen, zu der man ihn sinnvollerweise weiterleiten würde
    //     .failureUrl("/pfad/nach/fehlgeschlagenem/login") // default "/login?error"
    // );

    // hiermit wird der nicht-eingeloggte User über ein im Browser eingebautes Fenster zum Login (Username & Passwort) aufgefordert, falls die Zeile direkt hierüber fehlt (oder nicht funktioniert?) und auch sonst keine Login-Seite eingestellt wurde. Nach erfolgreichem Login landet man auf der gewünschten Seite, ansonsten wird die besuchte Seite komplett leer angezeigt.
    http.httpBasic(Customizer.withDefaults());

    // erstellt einen Endpunkt POST "/logout", durch dessen Aufruf der eingeloggte User ausgeloggt wird. Der Button auf der Standard-Logout-Seite (erzeugt zB durch http.formLogin(Customizer.withDefaults());) schickt genau diesen Request ab.
    http.logout(configurer -> configurer.invalidateHttpSession(true));

    return http.build();
  }

  // im folgenden Code erstellen wir hardgecoded zwei User. Einen mit Rolle "USER" und einen mit Rolle "ADMIN", jeweils mit passendem Username und Passwort. Die Rollenbezeichnungen können wir beliebig wählen. Hier können wir zum Rumprobieren User erstellen. Bei einem echten Projekt müssten wir diese Infos in einer Datenbank speichern.
  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user = User
        .withUsername("user")
        .password(passwordEncoder().encode("user"))
        .roles("USER")
        .build();

    UserDetails admin = User
        .withUsername("admin")
        .password(passwordEncoder().encode("admin"))
        .roles("ADMIN")
        .build();

    return new InMemoryUserDetailsManager(user, admin);
  }

  // das brauchen wir auch noch, damit die Passwörter ordentlich gehasht werden beim Erstellen und beim Login
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
```

Die folgenden vordefinierten Weiterleitungen gibt es mit den oberen Einstellungen, wenn man sie nicht selbst überschreibt.
Man kann sie zB nutzen, um in einem Thymeleaf-Template entsprechende Meldungen anzuzeigen:
- bei falschem Login: `"/login?error"`
- bei erfolgreichem Login: Der aufgerufende Pfad, dazu `"?continue"`.
  Wenn der Pfad zur Login-Seite aufgerufen wurde, landet man beim leeren Pfad (immer noch mit `"?continue"`)
- bei Logout: `"/login?logout"`




### 15.b) `@PreAuthorize` für Rollenprüfung auf Methodenlevel

Hier eine gute Dokumentationsseite zum Thema für mehr Details: https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html#method-security-architecture

Wenn wir zu unserer mit `@Configuration` annotierten Klasse `WebSecurityConfig` vom vorherigen Unterkapitel noch die Annotation `@EnableMethodSecurity` hinzufügen, können wir die Annotation `@PreAuthorize` nutzen, um Endpunkte und sonstige (Service-)Methoden oder auch ganze (Controller- / Service-)Klassen nur für User mit bestimmten Rollen zu erlauben.

Dafür setzen wir als Annotation an den Kopf einer Klasse oder Methode zB so eine Zeile: `@PreAuthorize("hasRole('ADMIN')")`

***TODO ALEX*** checken ob man dann parallel noch `@EnableWebSecurity` nutzen kann, oder ob nur eins davon geht.



### 15.c) Spring Security in Thymeleaf

Da das Template noch im Backend zu HTML umgewandelt wird (also Server-Side-Rendering), ist es nicht verwerflich, direkt im Thymeleaf-Template basierend auf dem Login des Users noch Sachen anzuzeigen oder nicht anzuzeigen.
In einem JavaScript Frontend, oder sonstigem beim User zusammengebauten Frontend, sollte man das natürlich niemals tun.

Hier sind zwei gute kurze Artikel dazu, mit Beispielen:
- [Spring Security with Thymeleaf](https://www.baeldung.com/spring-security-thymeleaf)
- [Thymeleaf + Spring Security integration basics](https://www.thymeleaf.org/doc/articles/springsecurity.html)



### 15.d) Für weitere Recherche?

Ein guter Link von der offiziellen Spring Seite zur Einführung in die Basics, ähnlich wie hierüber in der Anleitung: https://spring.io/guides/gs/securing-web

Von Baeldung auch einer zu den Basics:
https://www.baeldung.com/spring-enablewebsecurity-vs-enableglobalmethodsecurity

Hier gibt es richtig viel für alle möglichen Funktionalitäten, die man sich wünschen kann (wurde nicht überprüft, ob das funktioniert, sieht aber gut aus, wenn auch eher veraltet):
https://www.geeksforgeeks.org/spring-security-tutorial/



## 16) User (auch mit anderen Feldern) in der Datenbank

***TODO ALEX*** User speichern und auch beim Login aus Datenbank auslesen.



# Anhang

## A) git, ein knapper Refresher

Bei Ubuntu normalerweise vorinstalliert. Bei Windows [installieren](https://git-scm.com/download/win) und dann neues "git Bash"-Terminal nutzen.

### A.1) Nützliche Quellen
Tolle Videos zum Einstieg und als Refresher von Fireship:
- [Git explained in 100 Seconds](https://www.youtube.com/watch?v=hwP7WQkmECE)
- [13 Advanced (but useful) Git Techniques](https://www.youtube.com/watch?v=ecK3EnyGD8o)

Die offizielle Seite hat auch eine [ausführliche Anleitung](https://git-scm.com/book/en/v2).


### A.2) Überblick Befehle

#### git init
- erstellt den versteckten .git-Ordner, durch den ein Ordner zu einem git-Repository wird

---

#### git add**
- bereitet Änderungen darauf vor, im nächsten Commit abgespeichert zu werden
- **git add .** bereitet alle Änderungen des aktuellen Ordners (und seiner Nachfahren) für den nächsten Commit vor
- **git add -p** entscheide stückweise, welche Änderungen für den nächsten Commit vorbereitet werden

#### git commit -m "hier meine Commit message"
- erstellt einen neuen Commit, welcher alle vorbereiteten Änderungen als neue Version speichert, zusammen mit einer Text, der die Änderungen dieser Version beschreibt
- lasse **-m** und die Commit-Message weg, dann öffnet sich ein Editor, in den du die Message eintippen musst. Jeder Commit braucht eine Commit-Message

#### git push
- lade aktuellen Stand hoch auf remote Repository (muss vorher eingerichtet werden, zB auf github; am besten dort ein leeres Repository erstellen und dann in dem leeren Repository in der Web UI die Befehle nachschauen)

#### git pull
- lade aktuellen Stand von remote herunter (muss vorher eingerichtet sein)

---

#### git status
- zeigt aktuellen Status geänderter Dateien und ob sie für den nächsten Commit vorbereitet wurden, inklusiver Tipps zum Vorgehen für den nächsten Commit

#### git diff
- zeige alle Änderungen seit dem letzten Commit an, die noch nicht für den nächsten Commit vorbereitet wurden. Runterscrollen im Terminal mit Enter oder Leertaste , Beenden mit der Taste "q".
- am Ende noch zwei Commit-Hashes oder zwei Dateinamen hinzufügen, um diese jeweils miteinander zu vergleichen

#### git log
zeige alle vergangenen Commits an

---

#### git clone
- lädt ein Projekt von Github oder Gitlab runter auf unseren Laptop, inklusive aller git-Sachen, die dran hängen

#### git branch
- Branch erstellen: **git branch <BRANCH_NAME>**, dann muss man noch wechseln zum gerade erstellten Branch

#### git checkout
- zu einem Branch wechseln: **git checkout <BRANCH_NAME>**
- einen Branch erstellen und direkt wechseln: **git checkout -b <BRANCH_NAME>**
- alle lokalen Änderungen einer Datei rückgängig machen, die noch nicht commitet wurden (Vorsicht, die Änderungen sind für immer weg!): **git checkout <FILE_NAME>**
#### git merge
- **git merge <BRANCH_NAME>** versucht, die Änderungen des angegebenen Branches in den rein zu ziehen, in dem wir uns aktuell lokal befinden
- **git mergetool** zum Nutzen einer optional eingerichteten Software, mit der mögliche Merge Konflikte mit einer GUI behandelt werden können

---

#### git alias
- setze Abkürzungen und kombinierte Befehle, die du dann vereinfacht nutzen kannst. Mehr Infos und Beispiele: https://git-scm.com/book/en/v2/Git-Basics-Git-Aliases

