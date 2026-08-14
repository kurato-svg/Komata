# Komata

Komata is an Android manga, manhwa and manhua reader project designed around a provider architecture.

## Milestone 1

This build intentionally uses a built-in demo provider. It proves the core reading path before website extensions are introduced:

1. Home catalogue
2. Title details
3. Chapter list
4. Vertical Webtoon reader

The provider contract is located at:

`app/src/main/java/app/kcs/komata/core/provider/MangaProvider.kt`

The built-in demo implementation is located at:

`app/src/main/java/app/kcs/komata/providers/demo/DemoProvider.kt`

## Build on GitHub

Upload the full project to a GitHub repository and keep GitHub Actions enabled. The included workflow builds the debug APK on pushes to `main`, or you can run it manually from the Actions tab.

The resulting artifact is named `Komata-debug`.

## Next milestone

After this APK is confirmed working on a phone, the next milestone is to extract a real extension API and implement the first external provider, ToonGod, without hard-coding it into Komata.
