# Californium (Fabric)

Californium is a **client-side** Fabric performance mod focused on stabilizing frame-times and maximizing FPS through adaptive controls.

## Included systems
- Adaptive FPS controller with target FPS feedback loop.
- Dynamic render/simulation distance scaling.
- Particle/effect quality throttling under load.
- Distant entity tick throttling.
- In-game configuration screen (press `F8`).
- On-screen benchmark HUD showing live FPS and baseline comparison ratio.

## Notes
- This is designed to be compatibility-first with safe, minimal mixins.
- Real-world gains depend heavily on CPU/GPU, world complexity, and modpack composition.
- For peak performance, pair with Sodium and modern JVM flags.

## Build

### Standard (requires access to Fabric/Maven repositories)
```bash
JAVA_HOME=$HOME/.local/share/mise/installs/java/21.0.2 PATH=$JAVA_HOME/bin:$PATH gradle clean build
```

### Offline fallback artifact
If the environment blocks Maven/Fabric repositories, you can still package an offline dev artifact:
```bash
./scripts/build-offline-jar.sh
```
This creates `build/libs/californium-offline-dev.jar` containing resources and sources for transport/debug workflows.
