# setup

Each script should have instructions + the command on how to run it.
But in general you'll need to install kotlin like so:

```sh
# install sdkman
# i like sdkman cause it allows us to install different versions
curl -s "https://get.sdkman.io" | bash
# install kotlin (1.9.24 until 2.0 starts working well with the libs used)
sdk install kotlin 1.9.24

# run the script
kotlin 0.hello.main.kts Hello 🌎
```

Alternatively, if you want to make the script executable

```sh
chmod +x 0.hello.main.kts

# you can now run the script like so:
./0.hello.main.kts Hello 🌎
```

_Note: The first time you run the script it'll look like it's taking forever, thereafter the script runs super fast_.
