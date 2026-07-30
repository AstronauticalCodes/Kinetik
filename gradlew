#!/usr/bin/env sh
# Gradle wrapper script
GRADLE_BIN=""
if command -v gradle >/dev/null 2>&1; then
  GRADLE_BIN="gradle"
elif [ -x "/Users/jay/.gradle/wrapper/dists/gradle-9.6.1-bin/4ticwg1pgcbps2hj28r8so764/gradle-9.6.1/bin/gradle" ]; then
  GRADLE_BIN="/Users/jay/.gradle/wrapper/dists/gradle-9.6.1-bin/4ticwg1pgcbps2hj28r8so764/gradle-9.6.1/bin/gradle"
else
  GRADLE_BIN=$(find /Users/jay/.gradle/wrapper/dists -name "gradle" 2>/dev/null | head -n 1)
fi

if [ -z "$GRADLE_BIN" ]; then
  echo "Error: Could not locate gradle executable." >&2
  exit 1
fi

exec "$GRADLE_BIN" "$@"
