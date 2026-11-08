# Wardencannon Minecraft Plugin

Wardencannon is a Minecraft Paper plugin for 1.21.11 that adds a custom Warden Cannon trident with four uses, Nether-style fire-producing explosions, and a vertical sequence of Warden sonic boom effects.

## Install

1. Build the plugin with Java 21 and Maven:
   ```bash
   mvn package
   ```
2. Copy the generated `target/plugin.jar` file into your server's `plugins` directory.
3. Start or restart a Paper 1.21.11 server.
4. Grant players the `wardencannon.use` permission.

## Use

1. Run:
   ```text
   /wardencannon shot
   ```
2. The command gives the player a Warden Cannon trident with four remaining shots.
3. Throw the trident at a target location.
4. When the trident lands, it creates a power-3.5 explosion that can damage blocks and starts five sonic boom particle effects in a vertical column toward the world height limit.
5. Each sonic boom effect occurs one tick after the previous one and plays the Warden sonic boom sound.
6. Each landing consumes one shot. The remaining uses are stored on the trident.

The command requires the `wardencannon.use` permission, which is disabled by default.

---

More Minecraft plugin projects by EnderDevelopment: https://github.com/EnderDevelopment

Project page: https://dash.enderdevelopment.com/community/389c80ea-ce43-4f39-8bed-67cb101ccc1c?utm_source=github&utm_medium=readme&utm_campaign=auto_publish&utm_content=389c80ea-ce43-4f39-8bed-67cb101ccc1c
