#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
OUT_DIR="$ROOT_DIR/build/libs"
TMP_DIR="$ROOT_DIR/build/tmp/offline-mod"

rm -rf "$TMP_DIR"
mkdir -p "$TMP_DIR" "$OUT_DIR"

cp -r "$ROOT_DIR/src/main/resources"/* "$TMP_DIR"/
cp -r "$ROOT_DIR/src/client/java" "$TMP_DIR"/sources

(
  cd "$TMP_DIR"
  jar cf "$OUT_DIR/californium-offline-dev.jar" .
)

echo "Created $OUT_DIR/californium-offline-dev.jar"
