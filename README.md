# my-first-spring-project
Erstellt zusammen mit Umschülern in der Mischok Academy, Modul Software-Architektur 1. Hierunter eine vorab pro Session vorbereitete Anleitung, die wir über mehrere Sitzungen befolgt haben.

# My first Spring Backend (+Thymeleaf Frontend)

Das hier ist eine Schritt-für-Schritt-Anleitung für die Erstellung eines ersten Spring Backends. Entstanden im Rahmen vom Modul "Software-Architektur 1" in der Mischok Academy. Erste Version von Alex.

## Was wir bauen
TODO ALEX aufschreiben

## Prerequisites
- Betriebssystem: Ubuntu wäre gut
- im Terminal installierte/verfügbare Programme:
  - mvn
  - curl
  - ... TODO?
- IntelliJ oder anderer Editor / IDE
- Optional:
  - git installiert und Grundlagen dafür gelernt, um nach jedem Schritt (oder nach wenigen Schritten) einen schönen neuen Commit anzulegen

Vorab (TODO):
-  git:
	- git clone
	- git pull
	- git push
	- .gitignore
- HTML?:
	- Form basteln mit POST request


## Start und ein erster Endpunkt

### Leeres Spring Projekt mit passenden Dependencies starten

- ausführliches Spring Tutorial, falls man es selbst mal durchlesen möchte, für die ersten paar Schritte: https://spring.io/guides/gs/serving-web-content
	- das dort anfangs verlinkte pre-initialized project herunterladen: https://start.spring.io/#!type=maven-project&language=java&packaging=jar&jvmVersion=11&groupId=com.example&artifactId=serving-web-content&name=serving-web-content&description=Demo%20project%20for%20Spring%20Boot&packageName=com.example.serving-web-content&dependencies=web,thymeleaf,devtools
	- Spring initializr mit Auswahl:
    - TODO SCREENSHOT
		- Spring Web
		- Thymeleaf
		- Spring Boot DevTools
  - das legt uns direkt die richtigen Dependencies in die maven `pom.xml`:
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

### in IntelliJ öffnen und einfachen GET-Endpunkt erstellen
- in IntelliJ öffnen, am besten direkt die `pom.xml` auswählen
- erstelle neben der main-Klasse auch einen GreetingController, dort zuerst nicht Code aus Tutorial einfügen
	- zuerst komplett ohne RequestParam
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
	- Code ähnlich wie in Tutorial (mit eingesetzter model-Variable) dann später, wenn wir es ohne Variable ausprobiert haben
- erstelle `greeting.html` in `src/main/resources/templates`
	- Code aus Anleitung kopieren, aber hier noch `${name}` rausnehmen (evtl Zeile ersetzen und auskommentieren).
    - ACHTUNG!: die einzelnen Anführungszeichen um `Hello World!` im folgenden Beispielcode sind wichtig! Durch die doppelten Anführungszeichen wird erst der Input für Thymeleaf ermöglicht, aber dann muss man die einzelnen Anführungszeichen noch als String-Delimiter setzen. Das nicht zu tun, wäre so verwerflich, wie in Java `String text = Hello World!;` ohne String-Delimiter zu schreiben.
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
    - beachte beim Code-Beispiel von dem Tutorial die Pipe-Zeichen "`|`", durch die man in Spring Strings markiert, in denen man Platzhalter ähnlich wie bei Javascript Strings einsetzen kann. Für diese Anleitung lieber Strings konkatenieren wie in basic Java

### Backend lokal laufen lassen mit maven
- bei IntelliJ auf Play drücken, oder vorher Rechtsklick auf die `pom.xml` und dort "Ausführen" (mit grünem Play-Symbol daneben) oder sowas anklicken, oder auf die Java-Klasse mit der main Rechtsklick und "Ausführen". Nach dem ersten erfolgreichen Run sollte bei IntelliJ oben rechts ein grüner Play-Button verfügbar sein.
- ohne IntelliJ oder sonstige IDE: Terminal öffnen im Projektordner und ausführen:
  ```
  ./mvnw spring-boot:run
  ```
- aufrufen: http://localhost:8080/greeting
- jetzt Controller ähnlich wie im Tutorial schreiben
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
      ```
      localhost:8080/greeting
      localhost:8080/greeting?n=Alex
      ```
  - beachte, dass man die ganzen Bezeichnungen hierüber (someName, n, inputName) auch alle gleich benennen könnte, zB "name". Die sind hier nur verschieden, um klar zu machen, welcher Name was tut.

## Abstecher zu JSON als Antwort, Lombok und statische Seiten

### JSON als Antwort vom Endpunkt
Als nächstes wollen wir vom Endpunkt aus json zurückgeben. Dafür nicht mehr mit Thymeleaf und mit dem model und String-Rückgabe beim Endpunkt. Sondern wir geben einfach ein POJO zurück und Spring-Web konvertiert das mit Jackson für uns direkt zu einem JSON-Objekt.

<!-- - bevor wir mehr Endpunkte aufrufen, verschieben wir das `"/greeting"` von `@GetMapping` (und löschen da die dann leere Klammer) und setzen stattdessen für den ganzen Controller oben direkt unter `@Controller` diese Zeile:`@RequestMapping("/greeting")` -->
- jetzt brauchen wir ein POJO, das wir dann zurückgeben können. Für diese Anleitung Arbeiten wir beispielhaft mit einer Klasse `Person`:
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
  - Achtung: hier auch `model` bei den Eingabeparametern weglassen!


### Lombok Annotations
Wir löschen in unserem POJO die Getter und Setter und nutzen stattdessen die passenden Annotations von Lombok. Mehr Infos [bei Baeldung](https://www.baeldung.com/intro-to-project-lombok).
- So aktivieren wir Lombok in IntelliJ:
  - Dependency in die `pom.xml` packen. Am besten recherchieren wir dafür alle selbst, wie wir die Dependency kriegen. Suchmaschine: "maven lombok"
    ```xml
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <version>1.18.34</version>
    </dependency>
    ```
  - dann noch bei IntelliJ sollte ein kleiner Dialog aufploppen mit "Enable Lombok Annotation Processing"
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
- optional: `@Builder`-Annotation einsetzen, um unser POJO mit dem Builder-Pattern zu erzeugen, statt mit Konstruktoren und hinterher gesetzten Werten zu Arbeiten.
  - Vorsicht bei Lombok: Die `@Builder`-Annotation macht den leeren Konstrktur kaputt macht bei der POJO, d.h. wenn man den braucht, braucht man noch die Lombok-Annotation `@NoArgsConstructor`. Dann aber geht der Builder wieder kaputt, außer man setzt dann noch die Annotation `@AllArgsConstructor`.
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



<!--
### Welcome Page
Damit wir uns nicht die Pfade merken müssen, basteln wir uns eine statische, stinknormale HTML-Startseite, ganz ohne Thymeleaf, in der wir die Links klickbar anzeigen. Dafür legen wir eine Datei `index.html` in den Ordner `src/main/resources/static`:
  ```html
  <!DOCTYPE HTML>
  <html>
  <head>
    <title>Meine Seite</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  </head>
  <body>
    <p>Get your greeting <a href="/greeting">here</a></p>
  </body>
  </html>
  ```
  - wie wir die Unterseite mit Input öffnen können, ohne ihn selbst in die URL zu schreiben, sehen wir später. Zunächst reicht uns der default `Hello World!`


## POST-Requests und HTML-forms

### POST-Request basteln mit @PostMapping, TODO weiter machen?
- zuerst einen JSON-Endpunkt, der uns einfach true oder false zurückgibt, wenn etwas geklappt hat
  - mit curl Testen, Infos im JSON-Body mitschicken
- danach auch mit Thymeleaf als Rückgabe


## Daten persistieren

### H2 Datenbank erstellen

*** H2 DB einbinden (In-Memory zuerst, danach als File persistiert) ***
(siehe https://www.baeldung.com/spring-boot-h2-database oder h2.odt von Julius)

ENTWEDER zuerst im Initializer eine Sache mehr klicken:
	- Spring Data JPA
ODER einfach in der pom.xml folgendes einfügen in dem <dependencies> Tag:
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

Jetzt starten im Terminal führt zu Fehler, "Error creating bean with name 'entityManagerFactory' ..."
	- also mit ./mvnw spring-boot:run

Einfügen in pom.xml:
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
</dependency>

Einfügen in application.properties:
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

Als file anlegen (damit wir über den Browser mal reinschauen können):
spring.datasource.url=jdbc:h2:file:./data/mynewdb

Zugriff auf H2 Datenbank:
localhost:8080/h2-console
	dort passenden Pfad zur Datei

Schreibe SQL-Queries, um eine neue Tabelle zu erstellen mit 2-3 Einträgen:
CREATE TABLE journal_entry (
    id int NOT NULL,
    name varchar(50) NOT NULL,
    description varchar(255),
    content text,
    PRIMARY KEY (id)
);
INSERT INTO journal_entry VALUES (1, 'Start', 'es geht los', 'So fing alles an. Was ist schon lange her, dass ...');
INSERT INTO journal_entry VALUES (2, 'Abend', 'hammer', 'Was ein Tag es war, was ich alles gelernt habe ...');

^ später als flyway-Migration, jetzt erstmal egal


************

Entity in Java als Objekt anlegen, außerdem Repository (ohne Service), per Controller zurückgeben (siehe Projekt)
 -->
