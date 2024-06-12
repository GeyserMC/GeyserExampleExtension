# Using this template

This guide assumes that you've used this template, and created your own repository based on it.

Let's now get started with your extension!

1. Locate the `gradle.properties` file, and change the id, extensionName, and author fields there.
Those will also be applied to the extension when that's being built.
Note: The id must be lowercase, cannot start with a number, and cannot contain whitespaces. 
It may contain underscores (_). <br>
The name may contain uppercase and lowercase characters, dots, underscores, and dashes.

2. Rename the `org.geyser.extension.exampleid` package to something more unique.
For example, `net.myname.extension.customapples` would work.

3. Locate the `ExampleExtension` class, which should be located in your new package (you should also rename it :p).
You'll notice that it implements the `Extension` interface - that is required in order for Geyser to load your extension properly.

For more info on how to proceed, take a look at the code in that class, or at [our extension documentation](https://wiki.geysermc.org/geyser/extensions/).

Further notes:
- You do **not** need to modify the extension.yml; those properties are pulled automatically from the gradle.properties file.
- To build the extension, you should run `gradle build` command. The built extension will be located in the `build/libs/` folder.
You will notice that there are two jars: an "unshaded" one, and another. The unshaded jar will not contain any further dependencies
that you might add to the `build.gradle.kts`, while the other jar will have these. To use the extension, you'll want to use the jar without
the `unshaded` classifier.

Do you have further questions? Ask in the Geyser discord for help (#extensions channel)!
