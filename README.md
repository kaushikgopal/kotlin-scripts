# setup


```
// install sdkman (if you haven't already)
curl -s "https://get.sdkman.io" | bash  # install sdkman
sdk install maven
sdk install kotlin

// alternatively with homebrew
brew install maven
```

# running the script

You can either use kscript or kotlinc native. I recommend the former. As it stands kscript does some caching which might make your program run a tad bit faster.



```
// kscript 
sdk install kscript
// or `brew install holgerbrandl/tap/kscript` if you prefer homebrew

kscript -script <my-kts-script>.kts


// if you want to try kotlinc
brew install kotlinc
kotlinc -script list_folders.kts ~
```

# Demos using kotlin as a scripting language

## simple shell script to `ls` your folder

```
kscript -script list_folders.kts ~
```

## Tabulate hackathon votes

See the accompanying blog post for more details.

```
  cd /directory/with/csv/file
  kscript /code/path/carrot-wars-tabulate.kts /carrot-wars-results.csv
```

_Note: The first time you run the script it'll look like it's taking forever, thereafter the script runs super fast_.
