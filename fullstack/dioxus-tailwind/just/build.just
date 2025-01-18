
# Cleans the build artifacts
[group('build')]
clean:
  cargo clean
  rm -rf node_modules


# Builds all artifacts.
# Generates your server binary and all assets in ./target/dx/web/release/web
[group('build')]
build: build_css
  cd web && dx build --release --platform web


# Serves the app and watches for changes and rebuilds. 
[group('build')]
serve:
  cd web && dx serve


# Builds the CSS from the tailwindcss source and watches for changes.
[group('build')]
build_css options="":
  npx tailwindcss -i ./input.css -o ./web/assets/tailwind.css {{options}}


# Builds the CSS from the tailwindcss source and watches for changes.
[group('build')]
watch_css:
  just build_css "--watch"
