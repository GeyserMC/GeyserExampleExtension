# Example Extension
A Geyser extension template to make making extensions easier.

## Usage
1. Create a new repository using this template, make your extension - basic structure already exists
2. Run `./gradlew build` to build the extension
3. Copy the built jar from `build/libs` to your Geyser's extensions folder

Notes:
- extension.yml is required for Geyser to load the extension. It must be in the resources folder.
- Geyser Extensions: https://github.com/GeyserMC/Geyser/blob/master/api/src/main/java/org/geysermc/geyser/api/extension/Extension.java
- Geyser API docs: https://github.com/GeyserMC/Geyser/blob/master/api/src/main/java/org/geysermc/geyser/api/

