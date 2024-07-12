# sbt-compile-sass

POC replacement for sbt-sassify that uses dart-sass

See it compile css succesfully

```shell
sbt scripted
```

Then you can compile it and browse what it created

```shell
cd src/main/sbt-test/sbt-compile-sass/simple
sbt assets
tree target/web -L 4
```

and you'll see something like this

```shell
target/web
├── public
│   └── main
│       ├── lib
│       │   ├── govuk-frontend
│       │   └── hmrc-frontend
│       └── stylesheets
│           └── test.css
├── sass
│   └── main
│       └── stylesheets
│           └── test.css
└── web-modules
    └── main
        └── webjars
            └── lib

14 directories, 2 files
```

where the test.scss file contains the following to check stuff works the same

```sass
@import "lib/govuk-frontend/dist/govuk/base";

@import "partial";

body {
  a {
    color: red;
  }

  div {
    font-weight: bold;
  }
}

p {
  @include govuk-font-size($size: 19);
}
```
